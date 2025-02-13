package com.linbit.linstor.dbdrivers.interfaces;

import com.linbit.linstor.core.identifier.NodeName;
import com.linbit.linstor.core.identifier.StorPoolName;
import com.linbit.linstor.core.objects.AbsResource;
import com.linbit.linstor.core.objects.StorPool;
import com.linbit.linstor.dbdrivers.DatabaseException;
import com.linbit.linstor.storage.data.adapter.cache.CacheRscData;
import com.linbit.linstor.storage.data.adapter.cache.CacheVlmData;
import com.linbit.linstor.storage.interfaces.categories.resource.AbsRscLayerObject;
import com.linbit.utils.Pair;

import java.util.Map;
import java.util.Set;

public interface CacheLayerCtrlDatabaseDriver extends CacheLayerDatabaseDriver
{
    <RSC extends AbsResource<RSC>> Pair<? extends CacheRscData<RSC>, Set<AbsRscLayerObject<RSC>>>
    load(
        RSC rscRef,
        int idRef,
        String rscSuffixRef,
        AbsRscLayerObject<RSC> parentRef,
        Map<Pair<NodeName, StorPoolName>, Pair<StorPool, StorPool.InitMaps>> tmpStorPoolMapRef
    )
        throws DatabaseException;

    /*
     * Implement these methods to increase loading performance by for example fetching all data with one single request
     * instead of creating new requests for each object.
     *
     * If the underlying DBEngine is performant enough (like SQL), there is no need to implement these methods
     */
    default void fetchForLoadAll()
    {
    }

    default void clearLoadAllCache()
    {
    }

    default String getId(CacheRscData<?> cacheRscData)
    {
        return "(LayerRscId=" + cacheRscData.getRscLayerId() +
            ", SuffResName=" + cacheRscData.getSuffixedResourceName() +
            ")";
    }

    default String getId(CacheVlmData<?> cacheVlmData)
    {
        return "(LayerRscId=" + cacheVlmData.getRscLayerId() +
            ", SuffResName=" + cacheVlmData.getRscLayerObject().getSuffixedResourceName() +
            ", VlmNr=" + cacheVlmData.getVlmNr().value +
            ")";
    }
}
