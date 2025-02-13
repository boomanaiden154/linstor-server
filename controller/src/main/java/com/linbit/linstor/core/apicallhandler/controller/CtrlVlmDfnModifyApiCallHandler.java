package com.linbit.linstor.core.apicallhandler.controller;

import com.linbit.ImplementationError;
import com.linbit.linstor.LinstorParsingUtils;
import com.linbit.linstor.annotation.ApiContext;
import com.linbit.linstor.annotation.PeerContext;
import com.linbit.linstor.api.ApiCallRc;
import com.linbit.linstor.api.ApiCallRcImpl;
import com.linbit.linstor.api.ApiConsts;
import com.linbit.linstor.api.prop.LinStorObject;
import com.linbit.linstor.core.BackupInfoManager;
import com.linbit.linstor.core.CoreModule;
import com.linbit.linstor.core.apicallhandler.ScopeRunner;
import com.linbit.linstor.core.apicallhandler.controller.internal.CtrlSatelliteUpdateCaller;
import com.linbit.linstor.core.apicallhandler.controller.utils.SatelliteResourceStateDrbdUtils;
import com.linbit.linstor.core.apicallhandler.response.ApiAccessDeniedException;
import com.linbit.linstor.core.apicallhandler.response.ApiDatabaseException;
import com.linbit.linstor.core.apicallhandler.response.ApiOperation;
import com.linbit.linstor.core.apicallhandler.response.ApiRcException;
import com.linbit.linstor.core.apicallhandler.response.ApiSuccessUtils;
import com.linbit.linstor.core.apicallhandler.response.CtrlResponseUtils;
import com.linbit.linstor.core.apicallhandler.response.ResponseContext;
import com.linbit.linstor.core.apicallhandler.response.ResponseConverter;
import com.linbit.linstor.core.identifier.NodeName;
import com.linbit.linstor.core.identifier.ResourceName;
import com.linbit.linstor.core.identifier.VolumeNumber;
import com.linbit.linstor.core.objects.Resource;
import com.linbit.linstor.core.objects.ResourceDefinition;
import com.linbit.linstor.core.objects.StorPool;
import com.linbit.linstor.core.objects.Volume;
import com.linbit.linstor.core.objects.VolumeDefinition;
import com.linbit.linstor.core.objects.VolumeDefinition.Flags;
import com.linbit.linstor.dbdrivers.DatabaseException;
import com.linbit.linstor.propscon.Props;
import com.linbit.linstor.security.AccessContext;
import com.linbit.linstor.security.AccessDeniedException;
import com.linbit.linstor.stateflags.FlagsHelper;
import com.linbit.linstor.storage.kinds.DeviceLayerKind;
import com.linbit.linstor.storage.kinds.DeviceProviderKind;
import com.linbit.linstor.storage.utils.LayerUtils;
import com.linbit.linstor.utils.layer.LayerRscUtils;
import com.linbit.linstor.utils.layer.LayerVlmUtils;
import com.linbit.locks.LockGuard;
import com.linbit.utils.Pair;

import static com.linbit.linstor.core.apicallhandler.controller.CtrlVlmDfnApiCallHandler.getVlmDfnDescriptionInline;
import static com.linbit.linstor.core.apicallhandler.controller.CtrlVlmDfnApiCallHandler.makeVlmDfnContext;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.function.Supplier;
import java.util.stream.Stream;

import reactor.core.publisher.Flux;

@Singleton
public class CtrlVlmDfnModifyApiCallHandler implements CtrlSatelliteConnectionListener
{
    private final AccessContext apiCtx;
    private final ScopeRunner scopeRunner;
    private final CtrlTransactionHelper ctrlTransactionHelper;
    private final CtrlPropsHelper ctrlPropsHelper;
    private final CtrlApiDataLoader ctrlApiDataLoader;
    private final CtrlSatelliteUpdateCaller ctrlSatelliteUpdateCaller;
    private final ResponseConverter responseConverter;
    private final ReadWriteLock rscDfnMapLock;
    private final Provider<AccessContext> peerAccCtx;
    private final BackupInfoManager backupInfoMgr;

    @Inject
    CtrlVlmDfnModifyApiCallHandler(
        @ApiContext AccessContext apiCtxRef,
        ScopeRunner scopeRunnerRef,
        CtrlTransactionHelper ctrlTransactionHelperRef,
        CtrlPropsHelper ctrlPropsHelperRef,
        CtrlApiDataLoader ctrlApiDataLoaderRef,
        CtrlSatelliteUpdateCaller ctrlSatelliteUpdateCallerRef,
        ResponseConverter responseConverterRef,
        @Named(CoreModule.RSC_DFN_MAP_LOCK) ReadWriteLock rscDfnMapLockRef,
        @PeerContext Provider<AccessContext> peerAccCtxRef,
        BackupInfoManager backupInfoMgrRef
    )
    {
        apiCtx = apiCtxRef;
        scopeRunner = scopeRunnerRef;
        ctrlTransactionHelper = ctrlTransactionHelperRef;
        ctrlPropsHelper = ctrlPropsHelperRef;
        ctrlApiDataLoader = ctrlApiDataLoaderRef;
        ctrlSatelliteUpdateCaller = ctrlSatelliteUpdateCallerRef;
        responseConverter = responseConverterRef;
        rscDfnMapLock = rscDfnMapLockRef;
        peerAccCtx = peerAccCtxRef;
        backupInfoMgr = backupInfoMgrRef;
    }

    @Override
    public Collection<Flux<ApiCallRc>> resourceDefinitionConnected(ResourceDefinition rscDfn, ResponseContext context)
        throws AccessDeniedException
    {
        List<Flux<ApiCallRc>> fluxes = new ArrayList<>();

        ResourceName rscName = rscDfn.getName();

        Iterator<VolumeDefinition> vlmDfnIter = rscDfn.iterateVolumeDfn(apiCtx);
        while (vlmDfnIter.hasNext())
        {
            VolumeDefinition vlmDfn = vlmDfnIter.next();
            boolean resizing = vlmDfn.getFlags().isSet(apiCtx, VolumeDefinition.Flags.RESIZE);
            if (resizing)
            {
                fluxes.add(updateSatellites(rscName, vlmDfn.getVolumeNumber(), true));
            }
        }

        return fluxes;
    }

    public Flux<ApiCallRc> modifyVlmDfn(
        UUID vlmDfnUuid,
        String rscName,
        int vlmNr,
        Long size,
        Map<String, String> overrideProps,
        Set<String> deletePropKeys,
        List<String> vlmDfnFlags
    )
    {
        ResponseContext context = makeVlmDfnContext(
            ApiOperation.makeModifyOperation(),
            rscName,
            vlmNr
        );

        return scopeRunner
            .fluxInTransactionalScope(
                "Modify volume definition",
                LockGuard.createDeferred(rscDfnMapLock.writeLock()),
                () -> modifyVlmDfnInTransaction(
                    vlmDfnUuid,
                    rscName,
                    vlmNr,
                    size,
                    overrideProps,
                    deletePropKeys,
                    vlmDfnFlags
                )
            )
            .transform(responses -> responseConverter.reportingExceptions(context, responses));
    }

    private Flux<ApiCallRc> modifyVlmDfnInTransaction(
        UUID vlmDfnUuid,
        String rscNameStr,
        int vlmNrInt,
        Long size,
        Map<String, String> overrideProps,
        Set<String> deletePropKeys,
        List<String> vlmDfnFlagsRef
    )
    {
        ApiCallRcImpl responses = new ApiCallRcImpl();
        boolean notifyStlts = false;
        ResourceName rscName = LinstorParsingUtils.asRscName(rscNameStr);
        VolumeNumber vlmNr = LinstorParsingUtils.asVlmNr(vlmNrInt);
        VolumeDefinition vlmDfn = ctrlApiDataLoader.loadVlmDfn(rscName, vlmNr, true);

        if (vlmDfnUuid != null && !vlmDfnUuid.equals(vlmDfn.getUuid()))
        {
            throw new ApiRcException(ApiCallRcImpl.simpleEntry(
                ApiConsts.FAIL_UUID_VLM_DFN,
                "UUID check failed. Given UUID: " + vlmDfnUuid + ". Persisted UUID: " + vlmDfn.getUuid()
            ));
        }
        if (backupInfoMgr.restoreContainsRscDfn(vlmDfn.getResourceDefinition()))
        {
            throw new ApiRcException(
                ApiCallRcImpl.simpleEntry(
                    ApiConsts.FAIL_IN_USE,
                    rscNameStr + " is currently being restored from a backup. " +
                        "Please wait until the restore is finished"
                )
            );
        }

        notifyStlts = ctrlPropsHelper.fillProperties(responses, LinStorObject.VOLUME_DEFINITION, overrideProps,
            getVlmDfnProps(vlmDfn), ApiConsts.FAIL_ACC_DENIED_VLM_DFN) || notifyStlts;

        try
        {
            notifyStlts = ctrlPropsHelper.remove(
                responses,
                LinStorObject.VOLUME_DEFINITION,
                getVlmDfnProps(vlmDfn),
                deletePropKeys,
                Collections.emptyList()) || notifyStlts;
        }
        catch (AccessDeniedException exc)
        {
            throw new ApiAccessDeniedException(
                exc,
                "Access denied to remove property",
                ApiConsts.FAIL_ACC_DENIED_VLM
            );
        }
        catch (DatabaseException exc)
        {
            throw new ApiDatabaseException(exc);
        }

        Pair<Set<Flags>, Set<Flags>> flagPair = FlagsHelper.extractFlagsToEnableOrDisable(
            VolumeDefinition.Flags.class,
            vlmDfnFlagsRef
        );

        boolean isGrossFlagCurrentlySet = isFlagSet(vlmDfn, VolumeDefinition.Flags.GROSS_SIZE);
        boolean shouldGrossFlagBeEnabled = !isGrossFlagCurrentlySet &&
            flagPair.objA.contains(VolumeDefinition.Flags.GROSS_SIZE);
        boolean shouldGrossFlagBeDisabled = isGrossFlagCurrentlySet &&
            flagPair.objB.contains(VolumeDefinition.Flags.GROSS_SIZE);

        boolean updateForResize = false;

        if (shouldGrossFlagBeDisabled)
        {
            unsetFlag(vlmDfn, VolumeDefinition.Flags.GROSS_SIZE);
            updateForResize = true;
        }
        else
        if (shouldGrossFlagBeEnabled)
        {
            if (hasDeployedVolumes(vlmDfn))
            {
                ensureShrinkingIsSupported(vlmDfn);
            }
            setFlag(vlmDfn, VolumeDefinition.Flags.GROSS_SIZE);
            updateForResize = true;
        }

        boolean sizeChanges = size != null;
        boolean shrink = false;
        if (sizeChanges)
        {
            long diffSize = size - getVlmDfnSize(vlmDfn);

            shrink = diffSize < 0;
            if (shrink)
            {
                ensureShrinkingIsSupported(vlmDfn);
            }
            else
            {
                ensureAllStorPoolsHaveEnoughFreeSpace(vlmDfn, diffSize);
            }

            updateForResize = true;
            notifyStlts = true;
            setVlmDfnSize(vlmDfn, size);
        }

        Flux<ApiCallRc> updateResponses = Flux.empty();
        if (updateForResize)
        {
            try
            {
                Iterator<Resource> itRsc = vlmDfn.getResourceDefinition().iterateResource(apiCtx);
                while (itRsc.hasNext())
                {
                    final Resource rsc = itRsc.next();
                    if (!rsc.isDiskless(apiCtx) &&
                        !SatelliteResourceStateDrbdUtils.allVolumesUpToDate(rsc.getNode().getPeer(apiCtx), rscName))
                    {
                        throw new ApiRcException(ApiCallRcImpl.singleApiCallRc(
                            ApiConsts.FAIL_NOT_ALL_UPTODATE,
                            "Cannot resize volume, because we have a non-UpToDate DRBD device."
                        ));
                    }
                }
            }
            catch (AccessDeniedException exc)
            {
                throw new ApiAccessDeniedException(
                    exc,
                    "Access denied to check UpToDate",
                    ApiConsts.FAIL_ACC_DENIED_VLM
                );
            }

            /*
             * If the VlmDfn will grow in size, we have to
             * * set the RESIZE flag on all volumes
             * * let all satellites do an update
             * * set the DRBD_RESIZE flag on one volume (DRBD-resizing is cluster-aware)
             * * let all satellites do the update where actually only one performs the DRBD resize.
             * * unset all RESIZE and DRBD_RESIZE flags
             *
             * If the VlmDfn will shrink, we have to:
             * * set the DRBD_RESIZE flag on one volume + update stlts
             * * set the RESIZE flag on all volumes + updatestlts
             * * unset RESIZE and DRBD_RESIZE flags
             *
             * Note: Satellites do not care about vlmDfn RESIZE flag, only about vlm RESIZE and DRBD_RESIZE flag
             */
            Iterator<Volume> vlmIter = iterateVolumes(vlmDfn);

            if (vlmIter.hasNext())
            {
                markVlmDfnResize(vlmDfn);
            }

            if (shrink)
            {
                updateResponses = resizeDrbdInTransaction(rscName, vlmNr)
                    .concatWith(resizeNonDrbd(rscName, vlmNr))
                    .concatWith(finishResize(rscName, vlmNr));
            }
            else
            {
                updateResponses = resizeNonDrbdInTransaction(rscName, vlmNr)
                    .concatWith(resizeDrbd(rscName, vlmNr))
                    .concatWith(finishResize(rscName, vlmNr));
            }
        }
        else
        {
            if (notifyStlts)
            {
                updateResponses = updateResponses.concatWith(updateSatellites(rscName, vlmNr, false));
            }
        }

        ctrlTransactionHelper.commit();

        responses.addEntry(ApiSuccessUtils.defaultModifiedEntry(vlmDfn.getUuid(), getVlmDfnDescriptionInline(vlmDfn)));

        return Flux.just((ApiCallRc) responses)
            .concatWith(updateResponses);
    }

    /**
     * All participating layers & providers (including providers for metadata) must support shrinking, or an Exception
     * is thrown
     */
    private void ensureShrinkingIsSupported(VolumeDefinition vlmDfnRef)
    {
        try
        {
            Iterator<Resource> rscIt = vlmDfnRef.getResourceDefinition().iterateResource(apiCtx);
            Set<DeviceLayerKind> layerKindSet = new HashSet<>();
            Set<DeviceProviderKind> providerKindSet = new HashSet<>();
            while (rscIt.hasNext())
            {
                Resource rsc = rscIt.next();
                layerKindSet.addAll(LayerRscUtils.getLayerStack(rsc, apiCtx));
                Set<StorPool> storPools = LayerVlmUtils.getStorPools(rsc, apiCtx, true);
                for (StorPool sp : storPools)
                {
                    providerKindSet.add(sp.getDeviceProviderKind());
                }
            }

            Supplier<ApiRcException> createExc = () -> new ApiRcException(
                ApiCallRcImpl.simpleEntry(
                    ApiConsts.FAIL_INVLD_VLM_SIZE,
                    "Deployed volumes can only grow in size, not shrink. Changing volume's size from " +
                        "usable (net) to allocated (gross) could require the volume to shrink"
                )
            );
            for (DeviceLayerKind kind : layerKindSet)
            {
                if (!kind.isShrinkingSupported())
                {
                    throw createExc.get();
                }
            }
            for (DeviceProviderKind kind : providerKindSet)
            {
                if (!kind.isShrinkingSupported())
                {
                    throw createExc.get();
                }
            }
        }
        catch (AccessDeniedException exc)
        {
            throw new ImplementationError(exc);
        }
    }

    /**
     * All participating storage pools must have at least the additional free space left.
     * Thin pools always fulfill this requirement.
     *
     * NOTE:
     * The controller can only use the estimated additional size for checking. That means, even
     * if the controller (barely) passes this check, the satellite might still run into an issue
     * when executing the resize with the actual additional space, which might be more than we get
     * here.
     */
    private void ensureAllStorPoolsHaveEnoughFreeSpace(VolumeDefinition vlmDfnRef, long additionalSize)
    {
        try
        {
            Iterator<Resource> rscIt = vlmDfnRef.getResourceDefinition().iterateResource(apiCtx);
            Set<StorPool.Key> storPoolKeySet = new TreeSet<>();
            while (rscIt.hasNext())
            {
                Resource rsc = rscIt.next();
                Set<StorPool> storPools = LayerVlmUtils.getStorPools(rsc, apiCtx, true);
                for (StorPool sp : storPools)
                {
                    if (!sp.getDeviceProviderKind().usesThinProvisioning())
                    {
                        if (sp.getFreeSpaceTracker().getFreeCapacityLastUpdated(apiCtx).orElse(0L) < additionalSize)
                        {
                            storPoolKeySet.add(new StorPool.Key(sp));
                        }
                    }
                }
            }

            if (!storPoolKeySet.isEmpty())
            {
                StringBuilder sb = new StringBuilder();
                for (StorPool.Key key : storPoolKeySet)
                {
                    sb.append("Node: ").append(key.getNodeName().displayValue)
                        .append(", StorPool: ").append(key.getStorPoolName().displayValue).append("\n");
                }
                sb.setLength(sb.length() - 1); // cut last \n

                throw new ApiRcException(
                    ApiCallRcImpl.simpleEntry(
                        ApiConsts.FAIL_NOT_ENOUGH_FREE_SPACE,
                        "Cannot grow the volume definition by " + additionalSize +
                            "KiB, as the following storage pool do not have enough free space:\n" + sb
                    )
                );
            }
        }
        catch (AccessDeniedException exc)
        {
            throw new ImplementationError(exc);
        }
    }

    // Restart from here when connection established and RESIZE flag set
    private Flux<ApiCallRc> updateSatellites(ResourceName rscName, VolumeNumber vlmNr, boolean resize)
    {
        return scopeRunner
            .fluxInTransactionlessScope(
                "Update for volume definition modification",
                LockGuard.createDeferred(rscDfnMapLock.readLock()),
                () -> updateSatellitesInScope(rscName, vlmNr, resize)
            );
    }

    private Flux<ApiCallRc> updateSatellitesInScope(ResourceName rscName, VolumeNumber vlmNr, boolean resize)
    {
        VolumeDefinition vlmDfn = ctrlApiDataLoader.loadVlmDfn(rscName, vlmNr, false);

        Flux<ApiCallRc> flux;

        if (vlmDfn == null)
        {
            flux = Flux.empty();
        }
        else
        {
            Flux<ApiCallRc> nextStep = Flux.empty();
            if (resize)
            {
                if (hasDrbd(vlmDfn))
                {
                    nextStep = resizeDrbd(rscName, vlmNr);
                }
                else
                {
                    nextStep = finishResize(rscName, vlmNr);
                }
            }
            flux = ctrlSatelliteUpdateCaller.updateSatellites(vlmDfn.getResourceDefinition(), nextStep)
                .transform(
                    updateResponses -> CtrlResponseUtils.combineResponses(
                        updateResponses,
                        rscName,
                        "Updated volume " + vlmNr + " of {1} on {0}"
                    )
                )
                .concatWith(nextStep)
                .onErrorResume(
                CtrlResponseUtils.DelayedApiRcException.class,
                ignored -> Flux.empty()
            );
        }

        return flux;
    }

    private boolean hasDrbd(VolumeDefinition vlmDfnRef)
    {
        boolean anyResourceHasDrbdLayer;
        try
        {
            anyResourceHasDrbdLayer = vlmDfnRef.streamVolumes(apiCtx).anyMatch(this::hasDrbd);
        }
        catch (AccessDeniedException exc)
        {
            throw new ImplementationError(exc);
        }
        return anyResourceHasDrbdLayer;
    }

    private boolean hasDrbd(Volume vlm)
    {
        boolean hasDrbdLayer;
        try
        {
            hasDrbdLayer = !LayerUtils.getChildLayerDataByKind(
                vlm.getAbsResource().getLayerData(apiCtx),
                DeviceLayerKind.DRBD
            ).isEmpty();
        }
        catch (AccessDeniedException exc)
        {
            throw new ImplementationError(exc);
        }
        return hasDrbdLayer;
    }

    private Flux<ApiCallRc> resizeDrbd(ResourceName rscName, VolumeNumber vlmNr)
    {
        return scopeRunner
            .fluxInTransactionalScope(
                "Resize DRBD",
                LockGuard.createDeferred(rscDfnMapLock.writeLock()),
                () -> resizeDrbdInTransaction(rscName, vlmNr)
            );
    }

    private Flux<ApiCallRc> resizeDrbdInTransaction(ResourceName rscName, VolumeNumber vlmNr)
    {
        VolumeDefinition vlmDfn = ctrlApiDataLoader.loadVlmDfn(rscName, vlmNr, false);

        Flux<ApiCallRc> flux;

        if (vlmDfn == null)
        {
            flux = Flux.empty();
        }
        else
        {
            Optional<Volume> drbdResizeVlm = streamVolumesPrivileged(vlmDfn)
                .filter(this::isDrbdDiskful)
                .findAny();
            drbdResizeVlm.ifPresent(this::markVlmDrbdResize);

            ctrlTransactionHelper.commit();

            flux = ctrlSatelliteUpdateCaller.updateSatellites(vlmDfn.getResourceDefinition(), Flux.empty())
                .transform(
                    updateResponses -> CtrlResponseUtils.combineResponses(
                        updateResponses,
                        rscName,
                        getNodeNames(drbdResizeVlm),
                        "Resized DRBD resource {1} on {0}",
                        null
                    )
                );
        }

        return flux;
    }

    private Flux<ApiCallRc> resizeNonDrbd(ResourceName rscName, VolumeNumber vlmNr)
    {
        return scopeRunner
            .fluxInTransactionalScope(
                "Resize Non DRBD",
                LockGuard.createDeferred(rscDfnMapLock.writeLock()),
                () -> resizeNonDrbdInTransaction(rscName, vlmNr)
            );
    }

    private Flux<ApiCallRc> resizeNonDrbdInTransaction(ResourceName rscName, VolumeNumber vlmNr)
    {
        VolumeDefinition vlmDfn = ctrlApiDataLoader.loadVlmDfn(rscName, vlmNr, false);

        Flux<ApiCallRc> flux;

        if (vlmDfn == null)
        {
            flux = Flux.empty();
        }
        else
        {
            Set<NodeName> nodeNames = new HashSet<>();
            Iterator<Volume> vlmIter = iterateVolumes(vlmDfn);
            while (vlmIter.hasNext())
            {
                Volume vlm = vlmIter.next();

                markVlmResize(vlm);
                nodeNames.add(vlm.getAbsResource().getNode().getName());
            }

            ctrlTransactionHelper.commit();

            flux = ctrlSatelliteUpdateCaller.updateSatellites(vlmDfn.getResourceDefinition(), Flux.empty())
                .transform(
                    updateResponses -> CtrlResponseUtils.combineResponses(
                        updateResponses,
                        rscName,
                        nodeNames,
                        "Resized resource {1} on {0}",
                        null
                    )
                );
        }

        return flux;
    }

    private boolean isDrbdDiskful(Volume vlm)
    {
        boolean diskless;
        try
        {
            diskless = vlm.getAbsResource().isDrbdDiskless(apiCtx);
        }
        catch (AccessDeniedException accDeniedExc)
        {
            throw new ImplementationError(accDeniedExc);
        }
        return !diskless;
    }

    private Flux<ApiCallRc> finishResize(ResourceName rscName, VolumeNumber vlmNr)
    {
        return scopeRunner
            .fluxInTransactionalScope(
                "Clean up after resize",
                LockGuard.createDeferred(rscDfnMapLock.writeLock()),
                () -> finishResizeInTransaction(rscName, vlmNr)
            );
    }

    private Flux<ApiCallRc> finishResizeInTransaction(ResourceName rscName, VolumeNumber vlmNr)
    {
        VolumeDefinition vlmDfn = ctrlApiDataLoader.loadVlmDfn(rscName, vlmNr, false);

        if (vlmDfn != null)
        {
            streamVolumesPrivileged(vlmDfn).forEach(
                vlm ->
                {
                    unmarkVlmDrbdResizePrivileged(vlm);
                    unmarkVlmResizePrivileged(vlm);
                }
            );

            unmarkVlmDfnResizePrivileged(vlmDfn);

            ctrlTransactionHelper.commit();
        }

        return updateSatellites(rscName, vlmNr, false);
    }

    private Props getVlmDfnProps(VolumeDefinition vlmDfn)
    {
        Props props;
        try
        {
            props = vlmDfn.getProps(peerAccCtx.get());
        }
        catch (AccessDeniedException accDeniedExc)
        {
            throw new ApiAccessDeniedException(
                accDeniedExc,
                "access the properties of " + getVlmDfnDescriptionInline(vlmDfn),
                ApiConsts.FAIL_ACC_DENIED_VLM_DFN
            );
        }
        return props;
    }

    private void markVlmDfnResize(VolumeDefinition vlmDfn)
    {
        try
        {
            vlmDfn.getFlags().enableFlags(peerAccCtx.get(), VolumeDefinition.Flags.RESIZE);
        }
        catch (AccessDeniedException accDeniedExc)
        {
            throw new ApiAccessDeniedException(
                accDeniedExc,
                "set volume definition resize flag",
                ApiConsts.FAIL_ACC_DENIED_VLM_DFN
            );
        }
        catch (DatabaseException sqlExc)
        {
            throw new ApiDatabaseException(sqlExc);
        }
    }

    private void unmarkVlmDfnResizePrivileged(VolumeDefinition vlmDfn)
    {
        try
        {
            vlmDfn.getFlags().disableFlags(apiCtx, VolumeDefinition.Flags.RESIZE);
        }
        catch (AccessDeniedException accDeniedExc)
        {
            throw new ImplementationError(accDeniedExc);
        }
        catch (DatabaseException sqlExc)
        {
            throw new ApiDatabaseException(sqlExc);
        }
    }

    private void markVlmResize(Volume vlm)
    {
        try
        {
            vlm.getFlags().enableFlags(peerAccCtx.get(), Volume.Flags.RESIZE);
        }
        catch (AccessDeniedException accDeniedExc)
        {
            throw new ApiAccessDeniedException(
                accDeniedExc,
                "set volume resize flag",
                ApiConsts.FAIL_ACC_DENIED_VLM
            );
        }
        catch (DatabaseException sqlExc)
        {
            throw new ApiDatabaseException(sqlExc);
        }
    }

    private void unmarkVlmResizePrivileged(Volume vlm)
    {
        try
        {
            vlm.getFlags().disableFlags(apiCtx, Volume.Flags.RESIZE);
        }
        catch (AccessDeniedException accDeniedExc)
        {
            throw new ImplementationError(accDeniedExc);
        }
        catch (DatabaseException sqlExc)
        {
            throw new ApiDatabaseException(sqlExc);
        }
    }

    private void markVlmDrbdResize(Volume vlm)
    {
        try
        {
            vlm.getFlags().enableFlags(peerAccCtx.get(), Volume.Flags.DRBD_RESIZE);
        }
        catch (AccessDeniedException accDeniedExc)
        {
            throw new ApiAccessDeniedException(
                accDeniedExc,
                "set volume DRBD resize flag",
                ApiConsts.FAIL_ACC_DENIED_VLM
            );
        }
        catch (DatabaseException sqlExc)
        {
            throw new ApiDatabaseException(sqlExc);
        }
    }

    private void unmarkVlmDrbdResizePrivileged(Volume vlm)
    {
        try
        {
            vlm.getFlags().disableFlags(apiCtx, Volume.Flags.DRBD_RESIZE);
        }
        catch (AccessDeniedException accDeniedExc)
        {
            throw new ImplementationError(accDeniedExc);
        }
        catch (DatabaseException sqlExc)
        {
            throw new ApiDatabaseException(sqlExc);
        }
    }

    private long getVlmDfnSize(VolumeDefinition vlmDfn)
    {
        long volumeSize;
        try
        {
            volumeSize = vlmDfn.getVolumeSize(peerAccCtx.get());
        }
        catch (AccessDeniedException accDeniedExc)
        {
            throw new ApiAccessDeniedException(
                accDeniedExc,
                "access Volume definition's size",
                ApiConsts.FAIL_ACC_DENIED_VLM_DFN
            );
        }
        return volumeSize;
    }

    private void setVlmDfnSize(VolumeDefinition vlmDfn, Long size)
    {
        try
        {
            vlmDfn.setVolumeSize(peerAccCtx.get(), size);
        }
        catch (AccessDeniedException accDeniedExc)
        {
            throw new ApiAccessDeniedException(
                accDeniedExc,
                "update Volume definition's size",
                ApiConsts.FAIL_ACC_DENIED_VLM_DFN
            );
        }
        catch (DatabaseException sqlExc)
        {
            throw new ApiDatabaseException(sqlExc);
        }

    }

    private boolean hasDeployedVolumes(VolumeDefinition vlmDfn)
    {
        boolean hasVolumes;
        try
        {
            hasVolumes = vlmDfn.iterateVolumes(peerAccCtx.get()).hasNext();
        }
        catch (AccessDeniedException accDeniedExc)
        {
            throw new ApiAccessDeniedException(
                accDeniedExc,
                "access volume definition",
                ApiConsts.FAIL_ACC_DENIED_VLM_DFN
            );
        }
        return hasVolumes;
    }

    private Iterator<Volume> iterateVolumes(VolumeDefinition vlmDfn)
    {
        Iterator<Volume> volumeIterator;
        try
        {
            volumeIterator = vlmDfn.iterateVolumes(peerAccCtx.get());
        }
        catch (AccessDeniedException accDeniedExc)
        {
            throw new ApiAccessDeniedException(
                accDeniedExc,
                "iterate volumes",
                ApiConsts.FAIL_ACC_DENIED_VLM_DFN
            );
        }
        return volumeIterator;
    }

    private Stream<Volume> streamVolumesPrivileged(VolumeDefinition vlmDfn)
    {
        Stream<Volume> volumeStream;
        try
        {
            volumeStream = vlmDfn.streamVolumes(apiCtx);
        }
        catch (AccessDeniedException accDeniedExc)
        {
            throw new ImplementationError(accDeniedExc);
        }
        return volumeStream;
    }

    private boolean isFlagSet(VolumeDefinition vlmDfnRef, Flags flag)
    {
        boolean isFlagSet;
        try
        {
            isFlagSet = vlmDfnRef.getFlags().isSet(peerAccCtx.get(), flag);
        }
        catch (AccessDeniedException accDeniedExc)
        {
            throw new ApiAccessDeniedException(
                accDeniedExc,
                "check if a flag is set",
                ApiConsts.FAIL_ACC_DENIED_VLM_DFN
            );
        }
        return isFlagSet;
    }

    private boolean isFlagSet(Flags[] vlmDfnFlagsRef, Flags flag)
    {
        boolean isFlagSet = false;
        for (Flags setFlag : vlmDfnFlagsRef)
        {
            if (setFlag.equals(flag))
            {
                isFlagSet = true;
            }
        }
        return isFlagSet;
    }

    private void unsetFlag(VolumeDefinition vlmDfnRef, Flags flag)
    {
        try
        {
            vlmDfnRef.getFlags().disableFlags(peerAccCtx.get(), flag);
        }
        catch (AccessDeniedException accDeniedExc)
        {
            throw new ApiAccessDeniedException(
                accDeniedExc,
                "disabling flag",
                ApiConsts.FAIL_ACC_DENIED_VLM_DFN
            );
        }
        catch (DatabaseException dbExc)
        {
            throw new ApiDatabaseException(dbExc);
        }
    }

    private void setFlag(VolumeDefinition vlmDfnRef, Flags flag)
    {
        try
        {
            vlmDfnRef.getFlags().enableFlags(peerAccCtx.get(), flag);
        }
        catch (AccessDeniedException accDeniedExc)
        {
            throw new ApiAccessDeniedException(
                accDeniedExc,
                "enabling flag",
                ApiConsts.FAIL_ACC_DENIED_VLM_DFN
            );
        }
        catch (DatabaseException dbExc)
        {
            throw new ApiDatabaseException(dbExc);
        }
    }

    private static Set<NodeName> getNodeNames(Optional<Volume> drbdResizeVlm)
    {
        return drbdResizeVlm.isPresent() ?
            Collections.singleton(drbdResizeVlm.get().getAbsResource().getNode().getName()) :
            Collections.emptySet();
    }
}
