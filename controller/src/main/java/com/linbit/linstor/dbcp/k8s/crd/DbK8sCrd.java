package com.linbit.linstor.dbcp.k8s.crd;

import com.linbit.ImplementationError;
import com.linbit.InvalidNameException;
import com.linbit.ServiceName;
import com.linbit.SystemServiceStartException;
import com.linbit.linstor.ControllerK8sCrdDatabase;
import com.linbit.linstor.InitializationException;
import com.linbit.linstor.LinStorDBRuntimeException;
import com.linbit.linstor.core.ClassPathLoader;
import com.linbit.linstor.core.cfg.CtrlConfig;
import com.linbit.linstor.dbcp.migration.k8s.crd.BaseK8sCrdMigration;
import com.linbit.linstor.dbcp.migration.k8s.crd.K8sCrdMigration;
import com.linbit.linstor.dbdrivers.DatabaseException;
import com.linbit.linstor.dbdrivers.k8s.crd.LinstorVersionCrd;
import com.linbit.linstor.dbdrivers.k8s.crd.RollbackCrd;
import com.linbit.linstor.logging.ErrorReporter;
import com.linbit.linstor.transaction.ControllerK8sCrdTransactionMgr;
import com.linbit.linstor.transaction.ControllerK8sCrdTransactionMgrGenerator;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.inject.Provider;
import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinition;
import io.fabric8.kubernetes.api.model.apiextensions.v1.CustomResourceDefinitionList;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.NonNamespaceOperation;
import io.fabric8.kubernetes.client.dsl.Resource;

@Singleton
public class DbK8sCrd implements ControllerK8sCrdDatabase
{
    private static final ServiceName SERVICE_NAME;
    private static final String SERVICE_INFO = "K8s CRD handler";
    private static final String K8S_SCHEME = "k8s";

    private final AtomicBoolean atomicStarted = new AtomicBoolean(false);
    private final ControllerK8sCrdTransactionMgrGenerator k8sTxGenerator;

    private final ErrorReporter errorReporter;
    private final CtrlConfig ctrlCfg;
    private final Provider<ControllerK8sCrdDatabase> controllerDatabaseProvider;

    private KubernetesClient k8sClient;

    static
    {
        try
        {
            SERVICE_NAME = new ServiceName("K8sCrdDatabaseService");
        }
        catch (InvalidNameException invalidNameExc)
        {
            throw new ImplementationError(invalidNameExc);
        }
    }

    @Inject
    public DbK8sCrd(
        ErrorReporter errorReporterRef,
        CtrlConfig ctrlCfgRef,
        ControllerK8sCrdTransactionMgrGenerator k8sTxGeneratorRef,
        Provider<ControllerK8sCrdDatabase> controllerDatabaseProviderRef
    )
    {
        errorReporter = errorReporterRef;
        ctrlCfg = ctrlCfgRef;
        k8sTxGenerator = k8sTxGeneratorRef;
        controllerDatabaseProvider = controllerDatabaseProviderRef;
    }

    @Override
    public void setTimeout(int timeoutRef)
    {
        // ignored (for now)
    }

    @Override
    public void initializeDataSource(String dbConnectionUrlRef) throws DatabaseException
    {
        try
        {
            start();
        }
        catch (SystemServiceStartException systemServiceStartExc)
        {
            throw new ImplementationError(systemServiceStartExc);
        }
    }

    @Override
    public void migrate(String dbTypeRef) throws InitializationException
    {
        ControllerK8sCrdTransactionMgr currentTx = k8sTxGenerator.startTransaction();

        currentTx.rollbackIfNeeded();

        Integer dbVersion = currentTx.getDbVersion();

        ControllerK8sCrdDatabase k8sDb = controllerDatabaseProvider.get();

        if (dbVersion == null)
        {
            errorReporter.logTrace("No database found. Creating...");
            // ControllerK8sCrdDatabase k8sDb = controllerDatabaseProvider.get();
            NonNamespaceOperation<CustomResourceDefinition, CustomResourceDefinitionList, Resource<CustomResourceDefinition>> crdApi = k8sDb
                .getClient().apiextensions().v1()
                .customResourceDefinitions();

            crdApi.createOrReplace(
                crdApi.load(DbK8sCrd.class.getResourceAsStream("/" + LinstorVersionCrd.getYamlLocation())).get()
            );

            crdApi.createOrReplace(
                crdApi.load(
                    DbK8sCrd.class.getResourceAsStream("/" + RollbackCrd.getYamlLocation())
                ).get()
            );

            dbVersion = 0;
        }
        else
        {
            errorReporter.logTrace("Found database version %d", dbVersion - 1);
        }

        ClassPathLoader classPathLoader = new ClassPathLoader(errorReporter);
        List<Class<? extends BaseK8sCrdMigration>> k8sMigrationClasses = classPathLoader.loadClasses(
            BaseK8sCrdMigration.class.getPackage().getName(),
            Collections.singletonList(""),
            BaseK8sCrdMigration.class,
            K8sCrdMigration.class
        );

        TreeMap<Integer, BaseK8sCrdMigration> migrations = new TreeMap<>();
        try
        {
            for (Class<? extends BaseK8sCrdMigration> k8sMigrationClass : k8sMigrationClasses)
            {
                BaseK8sCrdMigration migration = k8sMigrationClass.newInstance();
                int version = migration.getVersion();
                if (migrations.containsKey(version))
                {
                    throw new ImplementationError(
                        "Duplicated migration version: " + version + ". " +
                            migrations.get(version).getDescription() + " " +
                            migration.getDescription()
                    );
                }
                migrations.put(version, migration);
            }
            // also copy version 1 to 0, so we can start the loop at 0
            migrations.put(0, migrations.get(1));

            checkIfAllMigrationsLinked(migrations);
        }
        catch (InstantiationException | IllegalAccessException exc)
        {
            throw new InitializationException("Failed to migrate ETCD server", exc);
        }

        try
        {
            int highestKey = migrations.lastKey();
            while (dbVersion <= highestKey)
            {
                BaseK8sCrdMigration migration = migrations.get(dbVersion);
                errorReporter.logDebug(
                    "Migration DB: %d -> %d: %s",
                    migration.getVersion() - 1,
                    migration.getNextVersion() - 1,
                    migration.getDescription()
                );
                migration.migrate(k8sDb);

                dbVersion = migration.getNextVersion();

                currentTx.getTransaction().updateLinstorVersion(dbVersion);

                currentTx.commit();
                currentTx = k8sTxGenerator.startTransaction();
            }
        }
        catch (Exception exc)
        {
            throw new LinStorDBRuntimeException("Exception occured during migration", exc);
        }
    }

    private void checkIfAllMigrationsLinked(TreeMap<Integer, BaseK8sCrdMigration> migrationsRef)
    {
        Set<BaseK8sCrdMigration> unreachableMigrations = new HashSet<>(migrationsRef.values());
        BaseK8sCrdMigration current = migrationsRef.get(0);
        while (current != null)
        {
            unreachableMigrations.remove(current);
            current = migrationsRef.get(current.getNextVersion());
        }
        if (!unreachableMigrations.isEmpty())
        {
            StringBuilder errorMsg = new StringBuilder("Found unreachable migrations: ");
            for (BaseK8sCrdMigration mig : unreachableMigrations)
            {
                errorMsg.append("  ").append(mig.getVersion()).append(" -> ").append(mig.getNextVersion())
                    .append(": ").append(mig.getClass().getSimpleName()).append(", ")
                    .append(mig.getDescription()).append("\n");
            }
            throw new ImplementationError(errorMsg.toString());
        }
    }

    @Override
    public boolean closeAllThreadLocalConnections()
    {
        return true;
    }

    @Override
    public void shutdown()
    {
        try
        {
            if (atomicStarted.compareAndSet(true, false))
            {
                k8sClient.close();
            }
        }
        catch (Exception exc)
        {
            errorReporter.reportError(exc);
        }
    }

    @Override
    public void checkHealth() throws DatabaseException
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void setServiceInstanceName(ServiceName instanceNameRef)
    {

    }

    @Override
    public void start() throws SystemServiceStartException
    {
        if (ctrlCfg.getDbConnectionUrl() == null || !K8S_SCHEME.equalsIgnoreCase(ctrlCfg.getDbConnectionUrl().trim()))
        {
            throw new SystemServiceStartException("Not configured for kubernetes connection!", true);
        }
        // final String origConUrl = ctrlCfg.getDbConnectionUrl();
        // final String connectionUrl = origConUrl.toLowerCase().startsWith(K8S_SCHEME) ?
        // origConUrl.substring(K8S_SCHEME.length()) :
        // origConUrl;

        k8sClient = new DefaultKubernetesClient();
        atomicStarted.set(true);
    }

    @Override
    public void awaitShutdown(long timeoutRef) throws InterruptedException
    {

    }

    @Override
    public ServiceName getServiceName()
    {
        return SERVICE_NAME;
    }

    @Override
    public String getServiceInfo()
    {
        return SERVICE_INFO;
    }

    @Override
    public ServiceName getInstanceName()
    {
        return SERVICE_NAME;
    }

    @Override
    public boolean isStarted()
    {
        return atomicStarted.get();
    }

    @Override
    public KubernetesClient getClient()
    {
        return k8sClient;
    }
}
