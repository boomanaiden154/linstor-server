package com.linbit.linstor.core.apicallhandler.controller;

import com.linbit.ImplementationError;
import com.linbit.InvalidNameException;
import com.linbit.linstor.annotation.ApiContext;
import com.linbit.linstor.api.ApiCallRc;
import com.linbit.linstor.api.ApiCallRcImpl;
import com.linbit.linstor.api.ApiConsts;
import com.linbit.linstor.api.BackupToS3;
import com.linbit.linstor.core.BackupInfoManager;
import com.linbit.linstor.core.BackupInfoManager.AbortInfo;
import com.linbit.linstor.core.BackupInfoManager.AbortS3Info;
import com.linbit.linstor.core.CtrlSecurityObjects;
import com.linbit.linstor.core.apicallhandler.ScopeRunner;
import com.linbit.linstor.core.apicallhandler.response.ApiDatabaseException;
import com.linbit.linstor.core.apicallhandler.response.ApiRcException;
import com.linbit.linstor.core.identifier.RemoteName;
import com.linbit.linstor.core.identifier.ResourceName;
import com.linbit.linstor.core.objects.Node;
import com.linbit.linstor.core.objects.Resource;
import com.linbit.linstor.core.objects.ResourceConnection;
import com.linbit.linstor.core.objects.ResourceDefinition;
import com.linbit.linstor.core.objects.S3Remote;
import com.linbit.linstor.core.objects.Snapshot;
import com.linbit.linstor.core.objects.SnapshotDefinition;
import com.linbit.linstor.core.repository.RemoteRepository;
import com.linbit.linstor.dbdrivers.DatabaseException;
import com.linbit.linstor.logging.ErrorReporter;
import com.linbit.linstor.security.AccessContext;
import com.linbit.linstor.security.AccessDeniedException;
import com.linbit.locks.LockGuardFactory;
import com.linbit.locks.LockGuardFactory.LockObj;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import reactor.core.publisher.Flux;

@Singleton
public class CtrlSnapshotShippingAbortHandler
{
    private final AccessContext apiCtx;
    private final ScopeRunner scopeRunner;
    private final LockGuardFactory lockGuardFactory;
    private final CtrlTransactionHelper ctrlTransactionHelper;
    private final CtrlApiDataLoader ctrlApiDataLoader;
    private final Provider<CtrlSnapshotDeleteApiCallHandler> snapDelHandlerProvider;
    private final BackupInfoManager backupInfoMgr;
    private final BackupToS3 backupHandler;
    private final RemoteRepository remoteRepo;
    private final ErrorReporter errorReporter;
    private final CtrlSecurityObjects ctrlSecObj;

    @Inject
    public CtrlSnapshotShippingAbortHandler(
        @ApiContext AccessContext apiCtxRef,
        ScopeRunner scopeRunnerRef,
        LockGuardFactory lockguardFactoryRef,
        CtrlTransactionHelper ctrlTransactionHelperRef,
        CtrlApiDataLoader ctrlApiDataLoaderRef,
        Provider<CtrlSnapshotDeleteApiCallHandler> snapDelHandlerProviderRef,
        BackupInfoManager backupInfoMgrRef,
        BackupToS3 backupHandlerRef,
        RemoteRepository remoteRepoRef,
        ErrorReporter errorReporterRef,
        CtrlSecurityObjects ctrlSecObjRef
    )
    {
        apiCtx = apiCtxRef;
        scopeRunner = scopeRunnerRef;
        lockGuardFactory = lockguardFactoryRef;
        ctrlTransactionHelper = ctrlTransactionHelperRef;
        ctrlApiDataLoader = ctrlApiDataLoaderRef;
        snapDelHandlerProvider = snapDelHandlerProviderRef;
        backupInfoMgr = backupInfoMgrRef;
        backupHandler = backupHandlerRef;
        remoteRepo = remoteRepoRef;
        errorReporter = errorReporterRef;
        ctrlSecObj = ctrlSecObjRef;
    }

    public Flux<ApiCallRc> abortAllShippingPrivileged(Node nodeRef)
    {
        return scopeRunner
            .fluxInTransactionalScope(
                "Abort all snapshot shipments to node",
                lockGuardFactory.create()
                    .read(LockObj.NODES_MAP)
                    .write(LockObj.RSC_DFN_MAP)
                    .buildDeferred(),
                () -> abortAllShippingPrivilegedInTransaction(nodeRef)
            );
    }

    private Flux<ApiCallRc> abortAllShippingPrivilegedInTransaction(Node nodeRef)
    {
        Flux<ApiCallRc> flux = Flux.empty();
        try
        {

            for (Snapshot snap : nodeRef.getSnapshots(apiCtx))
            {
                SnapshotDefinition snapDfn = snap.getSnapshotDefinition();
                if (
                    snapDfn.getFlags().isSet(apiCtx, SnapshotDefinition.Flags.SHIPPING) &&
                        !snap.getFlags().isSet(apiCtx, Snapshot.Flags.BACKUP_TARGET)
                )
                {
                    if (snapDfn.getFlags().isSet(apiCtx, SnapshotDefinition.Flags.BACKUP))
                    {
                        abortBackupShippings(nodeRef);
                    }
                    flux = flux.concatWith(snapDelHandlerProvider.get()
                        .deleteSnapshot(snapDfn.getResourceName().displayValue, snapDfn.getName().displayValue, null)
                    );
                }
            }
            ctrlTransactionHelper.commit();
        }
        catch (AccessDeniedException exc)
        {
            throw new ImplementationError(exc);
        }
        return flux;
    }

    public Flux<ApiCallRc> abortSnapshotShippingPrivileged(ResourceDefinition rscDfn)
    {
        return scopeRunner
            .fluxInTransactionalScope(
                "Abort snapshot shipments of rscDfn",
                lockGuardFactory.create()
                    .read(LockObj.NODES_MAP)
                    .write(LockObj.RSC_DFN_MAP)
                    .buildDeferred(),
                () -> abortSnapshotShippingPrivilegedInTransaction(rscDfn)
            );
    }

    private Flux<ApiCallRc> abortSnapshotShippingPrivilegedInTransaction(ResourceDefinition rscDfn)
    {
        Flux<ApiCallRc> flux = Flux.empty();
        try
        {

            for (SnapshotDefinition snapDfn : rscDfn.getSnapshotDfns(apiCtx))
            {
                if (
                    snapDfn.getFlags().isSet(apiCtx, SnapshotDefinition.Flags.SHIPPING) &&
                        !snapDfn.getFlags().isSet(apiCtx, SnapshotDefinition.Flags.BACKUP)
                )
                {
                    flux = flux.concatWith(
                        snapDelHandlerProvider.get()
                            .deleteSnapshot(
                                snapDfn.getResourceName().displayValue,
                                snapDfn.getName().displayValue,
                                null
                            )
                    );
                }
            }
            ctrlTransactionHelper.commit();
        }
        catch (AccessDeniedException exc)
        {
            throw new ImplementationError(exc);
        }
        return flux;
    }

    public Flux<ApiCallRc> abortBackupShippingPrivileged(ResourceDefinition rscDfn)
    {
        return scopeRunner
            .fluxInTransactionalScope(
                "Abort backup shipments of rscDfn",
                lockGuardFactory.create()
                    .read(LockObj.NODES_MAP)
                    .write(LockObj.RSC_DFN_MAP)
                    .buildDeferred(),
                () -> abortBackupShippingPrivilegedInTransaction(rscDfn)
            );
    }

    private Flux<ApiCallRc> abortBackupShippingPrivilegedInTransaction(ResourceDefinition rscDfn)
    {
        Flux<ApiCallRc> flux = Flux.empty();
        try
        {

            for (SnapshotDefinition snapDfn : rscDfn.getSnapshotDfns(apiCtx))
            {
                if (
                    snapDfn.getFlags().isSet(apiCtx, SnapshotDefinition.Flags.SHIPPING, SnapshotDefinition.Flags.BACKUP)
                )
                {
                    for (Snapshot snap : snapDfn.getAllSnapshots(apiCtx))
                    {
                        if (!snap.getFlags().isSet(apiCtx, Snapshot.Flags.BACKUP_TARGET))
                        {
                            flux = flux.concatWith(
                                snapDelHandlerProvider.get()
                                    .deleteSnapshot(
                                        snapDfn.getResourceName().displayValue,
                                        snapDfn.getName().displayValue,
                                        null
                                    )
                            );
                            abortBackupShippings(snap.getNode());
                        }
                    }
                }
            }
            ctrlTransactionHelper.commit();
        }
        catch (AccessDeniedException exc)
        {
            throw new ImplementationError(exc);
        }
        return flux;
    }

    public void markSnapshotShippingAborted(SnapshotDefinition snapDfnRef)
    {
        try
        {
            snapDfnRef.getFlags().disableFlags(
                apiCtx,
                SnapshotDefinition.Flags.SHIPPING,
                SnapshotDefinition.Flags.SHIPPING_CLEANUP,
                SnapshotDefinition.Flags.SHIPPED
            );
            snapDfnRef.getFlags().enableFlags(apiCtx, SnapshotDefinition.Flags.SHIPPING_ABORT);
            ResourceName rscName = snapDfnRef.getResourceName();

            Collection<Snapshot> snapshots = snapDfnRef.getAllSnapshots(apiCtx);
            for (Snapshot snap : snapshots)
            {
                Resource rsc1 = snap.getNode().getResource(apiCtx, rscName);
                for (Snapshot snap2 : snapshots)
                {
                    if (snap != snap2)
                    {
                        Resource rsc2 = snap2.getNode().getResource(apiCtx, rscName);

                        ResourceConnection rscConn = rsc1.getAbsResourceConnection(apiCtx, rsc2);
                        if (rscConn != null)
                        {
                            rscConn.setSnapshotShippingNameInProgress(null);
                        }
                    }
                }
            }
        }
        catch (AccessDeniedException exc)
        {
            throw new ImplementationError(exc);
        }
        catch (DatabaseException exc)
        {
            throw new ApiDatabaseException(exc);
        }
    }

    private void abortBackupShippings(Node nodeRef)
    {
        Map<SnapshotDefinition.Key, AbortInfo> abortEntries = backupInfoMgr.abortCreateGetEntries(nodeRef.getName());
        if (abortEntries != null && !abortEntries.isEmpty())
        {
            for (Entry<SnapshotDefinition.Key, AbortInfo> abortEntry : abortEntries.entrySet())
            {
                AbortInfo abortInfo = abortEntry.getValue();
                List<AbortS3Info> abortS3List = abortInfo.abortS3InfoList;
                if (!abortInfo.isEmpty())
                {
                    for (AbortS3Info abortS3Info : abortS3List)
                    {
                        try
                        {
                            S3Remote remote = remoteRepo.getS3(apiCtx, new RemoteName(abortS3Info.remoteName));
                            byte[] masterKey = ctrlSecObj.getCryptKey();
                            if (masterKey == null || masterKey.length == 0)
                            {
                                throw new ApiRcException(
                                    ApiCallRcImpl
                                        .entryBuilder(
                                            ApiConsts.FAIL_NOT_FOUND_CRYPT_KEY,
                                            "Unable to decrypt the S3 access key and secret key without having a master key"
                                        )
                                        .setCause("The masterkey was not initialized yet")
                                        .setCorrection("Create or enter the master passphrase")
                                        .build()
                                );
                            }
                            backupHandler.abortMultipart(
                                abortS3Info.backupName,
                                abortS3Info.uploadId,
                                remote,
                                apiCtx,
                                masterKey
                            );
                        }
                        catch (SdkClientException exc)
                        {
                            if (exc.getClass() == AmazonS3Exception.class)
                            {
                                AmazonS3Exception s3Exc = (AmazonS3Exception) exc;
                                if (s3Exc.getStatusCode() != 404)
                                {
                                    errorReporter.reportError(exc);
                                }
                            }
                            else
                            {
                                errorReporter.reportError(exc);
                            }
                        }
                        catch (AccessDeniedException | InvalidNameException exc)
                        {
                            throw new ImplementationError(exc);
                        }
                    }
                    // nothing to do for AbortL2LInfo entries, just enable the ABORT flag

                    SnapshotDefinition.Key snapDfnkey = abortEntry.getKey();
                    backupInfoMgr.abortCreateDeleteEntries(nodeRef.getName(), snapDfnkey);
                    enableFlagsPrivileged(
                        ctrlApiDataLoader.loadSnapshotDfn(
                            snapDfnkey.getResourceName(),
                            snapDfnkey.getSnapshotName(),
                            true
                        ),
                        SnapshotDefinition.Flags.SHIPPING_ABORT
                    );
                }
            }
        }
    }

    private void enableFlagsPrivileged(SnapshotDefinition snapDfn, SnapshotDefinition.Flags... snapDfnFlags)
    {
        try
        {
            snapDfn.getFlags().enableFlags(apiCtx, snapDfnFlags);
        }
        catch (AccessDeniedException exc)
        {
            throw new ImplementationError(exc);
        }
        catch (DatabaseException exc)
        {
            throw new ApiDatabaseException(exc);
        }
    }

}
