package com.linbit.linstor.api.rest.v1;

import com.linbit.linstor.api.ApiConsts;
import com.linbit.linstor.api.rest.v1.serializer.Json;
import com.linbit.linstor.api.rest.v1.serializer.JsonGenTypes;
import com.linbit.linstor.core.apicallhandler.controller.CtrlApiCallHandler;
import com.linbit.linstor.core.apicallhandler.controller.CtrlStorPoolListApiCallHandler;
import com.linbit.linstor.core.apicallhandler.controller.CtrlVlmListApiCallHandler;
import com.linbit.linstor.core.apicallhandler.controller.helpers.ResourceList;
import com.linbit.linstor.core.apis.ResourceApi;
import com.linbit.linstor.core.apis.SnapshotDefinitionListItemApi;
import com.linbit.linstor.core.apis.SnapshotShippingListItemApi;
import com.linbit.linstor.core.apis.StorPoolApi;

import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.grizzly.http.server.Request;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Path("v1/view")
@Produces(MediaType.APPLICATION_JSON)
public class View
{
    private final RequestHelper requestHelper;
    private final CtrlApiCallHandler ctrlApiCallHandler;
    private final CtrlVlmListApiCallHandler ctrlVlmListApiCallHandler;
    private final CtrlStorPoolListApiCallHandler ctrlStorPoolListApiCallHandler;
    private final ObjectMapper objectMapper;

    @Inject
    View(
        RequestHelper requestHelperRef,
        CtrlApiCallHandler ctrlApiCallHandlerRef,
        CtrlVlmListApiCallHandler ctrlVlmListApiCallHandlerRef,
        CtrlStorPoolListApiCallHandler ctrlStorPoolListApiCallHandlerRef
    )
    {
        requestHelper = requestHelperRef;
        ctrlApiCallHandler = ctrlApiCallHandlerRef;
        ctrlVlmListApiCallHandler = ctrlVlmListApiCallHandlerRef;
        ctrlStorPoolListApiCallHandler = ctrlStorPoolListApiCallHandlerRef;
        objectMapper = new ObjectMapper();
    }


    @GET
    @Path("resources")
    public void viewResources(
        @Context Request request,
        @Suspended AsyncResponse asyncResponse,
        @QueryParam("nodes") List<String> nodes,
        @QueryParam("resources") List<String> resources,
        @QueryParam("storage_pools") List<String> storagePools,
        @QueryParam("props") List<String> propFilters,
        @DefaultValue("0") @QueryParam("limit") int limit,
        @DefaultValue("0") @QueryParam("offset") int offset
    )
    {
        List<String> nodesFilter = nodes != null ? nodes : Collections.emptyList();
        List<String> storagePoolsFilter = storagePools != null ? storagePools : Collections.emptyList();
        List<String> resourcesFilter = resources != null ? resources : Collections.emptyList();

        RequestHelper.safeAsyncResponse(asyncResponse, () ->
        {
            Flux<ResourceList> flux = ctrlVlmListApiCallHandler.listVlms(
                nodesFilter, storagePoolsFilter, resourcesFilter, propFilters)
                .subscriberContext(requestHelper.createContext(ApiConsts.API_LST_VLM, request));

            requestHelper.doFlux(
                asyncResponse,
                listVolumesApiCallRcWithToResponse(flux, limit, offset)
            );
        });
    }

    private Mono<Response> listVolumesApiCallRcWithToResponse(
        Flux<ResourceList> resourceListFlux,
        int limit,
        int offset
    )
    {
        return resourceListFlux.flatMap(resourceList ->
        {
            Response resp;

            Stream<ResourceApi> rscApiStream = resourceList.getResources().stream();

            if (limit > 0)
            {
                rscApiStream = rscApiStream.skip(offset).limit(limit);
            }

            final List<JsonGenTypes.Resource> rscs = rscApiStream
                .map(rscApi -> Json.apiToResourceWithVolumes(rscApi, resourceList.getSatelliteStates(), true))
                .collect(Collectors.toList());

            try
            {
                resp = Response
                    .status(Response.Status.OK)
                    .entity(objectMapper.writeValueAsString(rscs))
                    .build();
            }
            catch (JsonProcessingException exc)
            {
                exc.printStackTrace();
                resp = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }

            return Mono.just(resp);
        }).next();
    }

    @GET
    @Path("storage-pools")
    public void viewStoragePools(
        @Context Request request,
        @Suspended AsyncResponse asyncResponse,
        @QueryParam("nodes") List<String> nodes,
        @QueryParam("storage_pools") List<String> storagePools,
        @QueryParam("props") List<String> propFilters,
        @DefaultValue("0") @QueryParam("limit") int limit,
        @DefaultValue("0") @QueryParam("offset") int offset,
        @DefaultValue("false") @QueryParam("cached") boolean fromCache
    )
    {
        List<String> nodesFilter = nodes != null ? nodes : Collections.emptyList();
        List<String> storagePoolsFilter = storagePools != null ? storagePools : Collections.emptyList();

        RequestHelper.safeAsyncResponse(asyncResponse, () ->
        {
            Flux<List<StorPoolApi>> flux = ctrlStorPoolListApiCallHandler
                .listStorPools(nodesFilter, storagePoolsFilter, propFilters, fromCache)
                .subscriberContext(requestHelper.createContext(ApiConsts.API_LST_STOR_POOL, request));

            requestHelper.doFlux(asyncResponse, storPoolListToResponse(flux, limit, offset));
        });
    }

    private Mono<Response> storPoolListToResponse(
        Flux<List<StorPoolApi>> storPoolListFlux,
        int limit,
        int offset
    )
    {
        return storPoolListFlux.flatMap(storPoolList ->
        {
            Response resp;
            Stream<StorPoolApi> storPoolApiStream = storPoolList.stream();
            if (limit > 0)
            {
                storPoolApiStream = storPoolApiStream.skip(offset).limit(limit);
            }
            List<JsonGenTypes.StoragePool> storPoolDataList = storPoolApiStream
                .map(Json::storPoolApiToStoragePool)
                .collect(Collectors.toList());

            try
            {
                resp = Response
                    .status(Response.Status.OK)
                    .entity(objectMapper.writeValueAsString(storPoolDataList))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
            }
            catch (JsonProcessingException exc)
            {
                exc.printStackTrace();
                resp = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }

            return Mono.just(resp);
        }).next();
    }

    @GET
    @Path("snapshots")
    public Response listSnapshots(
        @Context Request request,
        @QueryParam("nodes") List<String> nodes,
        @QueryParam("resources") List<String> resources,
        @DefaultValue("0") @QueryParam("limit") int limit,
        @DefaultValue("0") @QueryParam("offset") int offset
    )
    {
        return requestHelper.doInScope(ApiConsts.API_LST_SNAPSHOT_DFN, request, () ->
        {
            List<String> nodesFilter = nodes != null ? nodes : Collections.emptyList();
            List<String> resourcesFilter = resources != null ?
                resources.parallelStream().map(String::toLowerCase).collect(Collectors.toList()) :
                Collections.emptyList();

            Response response;

            Stream<SnapshotDefinitionListItemApi> snapsStream =
                ctrlApiCallHandler.listSnapshotDefinition(nodesFilter, resourcesFilter).stream();

            if (limit > 0)
            {
                snapsStream = snapsStream.skip(offset).limit(limit);
            }

            List<JsonGenTypes.Snapshot> snapshot = snapsStream
                .map(Json::apiToSnapshot)
                .collect(Collectors.toList());

            response = RequestHelper.queryRequestResponse(
                objectMapper, ApiConsts.FAIL_NOT_FOUND_SNAPSHOT, "Snapshot", null, snapshot
            );

            return response;
        }, false);
    }

    @GET
    @Path("snapshot-shippings")
    public Response listSnapshotShippings(
        @Context Request request,
        @QueryParam("nodes") List<String> nodes,
        @QueryParam("resources") List<String> resources,
        @QueryParam("snapshots") List<String> snapshots,
        @QueryParam("status") List<String> status,
        @DefaultValue("0") @QueryParam("limit") int limit,
        @DefaultValue("0") @QueryParam("offset") int offset
    )
    {
        return requestHelper.doInScope(ApiConsts.API_LST_SNAPSHOT_SHIPPINGS, request, () ->
        {
            List<String> nodesFilter = nodes != null ? nodes : Collections.emptyList();
            List<String> resourcesFilter = resources != null ?
                resources.parallelStream().map(String::toLowerCase).collect(Collectors.toList()) :
                Collections.emptyList();
            List<String> statusFilter = status != null ? status : Collections.emptyList();

            Response response;

            Stream<SnapshotShippingListItemApi> snapsStream = ctrlApiCallHandler
                .listSnapshotShippings(nodesFilter, resourcesFilter, snapshots, statusFilter).stream();

            if (limit > 0)
            {
                snapsStream = snapsStream.skip(offset).limit(limit);
            }

            List<JsonGenTypes.SnapshotShippingStatus> snapshot = snapsStream
                .map(Json::apiToSnapshotShipping)
                .collect(Collectors.toList());

            response = RequestHelper.queryRequestResponse(
                objectMapper,
                ApiConsts.FAIL_NOT_FOUND_SNAPSHOT,
                "Snapshot shippments",
                null,
                snapshot
            );

            return response;
        }, false);
    }
}
