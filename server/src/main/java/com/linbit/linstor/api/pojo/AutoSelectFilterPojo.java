package com.linbit.linstor.api.pojo;

import com.linbit.linstor.api.interfaces.AutoSelectFilterApi;
import com.linbit.linstor.storage.kinds.DeviceLayerKind;
import com.linbit.linstor.storage.kinds.DeviceProviderKind;

import javax.annotation.Nullable;

import java.util.Collections;
import java.util.List;

public class AutoSelectFilterPojo implements AutoSelectFilterApi
{
    private final @Nullable Integer placeCount; // null only allowed for resource groupss
    private final @Nullable String storPoolNameStr;
    private final List<String> doNotPlaceWithRscList;
    private final @Nullable String doNotPlaceWithRegex;
    private final List<String> replicasOnSameList;
    private final List<String> replicasOnDifferentList;
    private final List<DeviceLayerKind> layerStackList;
    private final List<DeviceProviderKind> providerList;
    private final @Nullable Boolean disklessOnRemaining;

    public AutoSelectFilterPojo(
        @Nullable Integer placeCountRef,
        @Nullable String storPoolNameStrRef,
        List<String> doNotPlaceWithRscListRef,
        @Nullable String doNotPlaceWithRegexRef,
        List<String> replicasOnSameListRef,
        List<String> replicasOnDifferentListRef,
        List<DeviceLayerKind> layerStackListRef,
        List<DeviceProviderKind> deviceProviderKindsRef,
        @Nullable Boolean disklessOnRemainingRef
    )
    {
        placeCount = placeCountRef;
        storPoolNameStr = storPoolNameStrRef;
        doNotPlaceWithRscList = doNotPlaceWithRscListRef;
        doNotPlaceWithRegex = doNotPlaceWithRegexRef;
        replicasOnSameList = replicasOnSameListRef;
        replicasOnDifferentList = replicasOnDifferentListRef;
        disklessOnRemaining = disklessOnRemainingRef;
        layerStackList = layerStackListRef == null ? Collections.emptyList() : layerStackListRef;
        providerList = deviceProviderKindsRef == null ? Collections.emptyList() : deviceProviderKindsRef;
    }

    public static AutoSelectFilterPojo merge(
        AutoSelectFilterApi... cfgArr
    )
    {
        Integer placeCount = null;
        List<String> replicasOnDifferentList = null;
        List<String> replicasOnSameList = null;
        String notPlaceWithRscRegex = null;
        List<String> notPlaceWithRscList = null;
        String storPoolNameStr = null;
        List<DeviceLayerKind> layerStack = null;
        List<DeviceProviderKind> providerList = null;
        Boolean disklessOnRemaining = null;

        for (AutoSelectFilterApi cfgApi : cfgArr)
        {
            if (placeCount == null)
            {
                placeCount = cfgApi.getReplicaCount();
            }
            if (replicasOnDifferentList == null)
            {
                replicasOnDifferentList = cfgApi.getReplicasOnDifferentList();
            }
            if (replicasOnSameList == null)
            {
                replicasOnSameList = cfgApi.getReplicasOnSameList();
            }
            if (notPlaceWithRscList == null)
            {
                notPlaceWithRscList = cfgApi.getDoNotPlaceWithRscList();
            }
            if (notPlaceWithRscRegex == null)
            {
                notPlaceWithRscRegex = cfgApi.getDoNotPlaceWithRscRegex();
            }
            if (storPoolNameStr == null)
            {
                storPoolNameStr = cfgApi.getStorPoolNameStr();
            }
            if (layerStack == null)
            {
                layerStack = cfgApi.getLayerStackList();
            }
            if (providerList == null)
            {
                providerList = cfgApi.getProviderList();
            }
            if (disklessOnRemaining == null)
            {
                disklessOnRemaining = cfgApi.getDisklessOnRemaining();
            }
        }

        return new AutoSelectFilterPojo(
            placeCount,
            storPoolNameStr,
            notPlaceWithRscList,
            notPlaceWithRscRegex,
            replicasOnSameList,
            replicasOnDifferentList,
            layerStack,
            providerList,
            disklessOnRemaining
        );
    }

    @Override
    public Integer getReplicaCount()
    {
        return placeCount;
    }

    @Override
    public @Nullable String getStorPoolNameStr()
    {
        return storPoolNameStr;
    }

    @Override
    public List<String> getDoNotPlaceWithRscList()
    {
        return doNotPlaceWithRscList;
    }

    @Override
    public @Nullable String getDoNotPlaceWithRscRegex()
    {
        return doNotPlaceWithRegex;
    }

    @Override
    public List<String> getReplicasOnSameList()
    {
        return replicasOnSameList;
    }

    @Override
    public List<String> getReplicasOnDifferentList()
    {
        return replicasOnDifferentList;
    }

    @Override
    public List<DeviceLayerKind> getLayerStackList()
    {
        return layerStackList;
    }

    @Override
    public List<DeviceProviderKind> getProviderList()
    {
        return providerList;
    }

    @Override
    public Boolean getDisklessOnRemaining()
    {
        return disklessOnRemaining;
    }
}
