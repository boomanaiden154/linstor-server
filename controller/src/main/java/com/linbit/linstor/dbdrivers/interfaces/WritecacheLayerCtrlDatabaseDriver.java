package com.linbit.linstor.dbdrivers.interfaces;

import com.linbit.linstor.core.identifier.NodeName;
import com.linbit.linstor.core.identifier.StorPoolName;
import com.linbit.linstor.core.objects.AbsResource;
import com.linbit.linstor.core.objects.StorPool;
import com.linbit.linstor.dbdrivers.DatabaseException;
import com.linbit.linstor.storage.data.adapter.writecache.WritecacheRscData;
import com.linbit.linstor.storage.data.adapter.writecache.WritecacheVlmData;
import com.linbit.linstor.storage.interfaces.categories.resource.AbsRscLayerObject;
import com.linbit.utils.Pair;

import java.util.Map;
import java.util.Set;

public interface WritecacheLayerCtrlDatabaseDriver extends WritecacheLayerDatabaseDriver
{
    <RSC extends AbsResource<RSC>> Pair<? extends WritecacheRscData<RSC>, Set<AbsRscLayerObject<RSC>>> load(
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

    default String getId(WritecacheRscData<?> writecacheRscData)
    {
        return "(LayerRscId=" + writecacheRscData.getRscLayerId() +
            ", SuffResName=" + writecacheRscData.getSuffixedResourceName() +
            ")";
    }

    default String getId(WritecacheVlmData<?> writecacheVlmData)
    {
        return "(LayerRscId=" + writecacheVlmData.getRscLayerId() +
            ", SuffResName=" + writecacheVlmData.getRscLayerObject().getSuffixedResourceName() +
            ", VlmNr=" + writecacheVlmData.getVlmNr().value +
            ")";
    }
}
