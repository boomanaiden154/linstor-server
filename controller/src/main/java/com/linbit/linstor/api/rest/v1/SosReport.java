package com.linbit.linstor.api.rest.v1;

import com.linbit.linstor.api.ApiCallRcImpl;
import com.linbit.linstor.api.ApiConsts;
import com.linbit.linstor.api.rest.v1.utils.ApiCallRcRestUtils;
import com.linbit.linstor.core.apicallhandler.controller.CtrlSosReportApiCallHandler;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.google.common.io.ByteStreams;
import org.glassfish.grizzly.http.server.Request;
import reactor.core.publisher.Mono;

@Path("v1/sos-report")
@Produces(MediaType.APPLICATION_JSON)
public class SosReport
{
    private final RequestHelper requestHelper;
    private final CtrlSosReportApiCallHandler ctrlSosReportApiCallHandler;
    private static final long DAY_IN_MS = 1000 * 60 * 60 * 24;

    @Inject
    public SosReport(
        RequestHelper reqestHelperRef,
        CtrlSosReportApiCallHandler ctrlSosReportApiCallHandlerRef
    )
    {
        requestHelper = reqestHelperRef;
        ctrlSosReportApiCallHandler = ctrlSosReportApiCallHandlerRef;
    }

    @GET
    @Path("/download")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public void downloadSosReport(
        @Context Request request,
        @QueryParam("node") String nodeName,
        @QueryParam("since") Long since,
        @Suspended final AsyncResponse asyncResponse
    )
    {
        Set<String> filterNodes = new HashSet<>();
        if (nodeName != null)
        {
            filterNodes.add(nodeName);
        }
        final Date sinceDate = since != null ? new Date(since) : new Date(System.currentTimeMillis() - (7 * DAY_IN_MS));

        Mono<Response> flux = ctrlSosReportApiCallHandler.getSosReport(filterNodes, sinceDate)
            .subscriberContext(requestHelper.createContext(ApiConsts.API_REQ_SOS_REPORT, request))
            .flatMap(sosReport ->
            {
                Response resp;
                resp = Response
                    .ok((StreamingOutput) output ->
                    {
                        try
                        {
                            java.nio.file.Path path = Paths.get(sosReport);
                            FileInputStream input = new FileInputStream(path.toFile());
                            ByteStreams.copy(input, output);
                            output.flush();
                            input.close();
                        }
                        catch (Exception exc)
                        {
                            throw new WebApplicationException("File Not Found !!");
                        }
                    }, MediaType.APPLICATION_OCTET_STREAM)
                    .header("content-disposition", "attachment; filename = " + Paths.get(sosReport).getFileName().toString())
                    .build();
                return Mono.just(resp);
            })
            .next();

        requestHelper.doFlux(asyncResponse, flux);
    }

    @GET
    public void getSosReport(
        @Context
        Request request,
        @QueryParam("node") String nodeName,
        @QueryParam("since") Long since,
        @Suspended final AsyncResponse asyncResponse
    )
    {
        Set<String> filterNodes = new HashSet<>();
        if (nodeName != null)
        {
            filterNodes.add(nodeName);
        }
        final Date sinceDate = since != null ? new Date(since) : new Date(System.currentTimeMillis() - (7 * DAY_IN_MS));

        Mono<Response> flux = ctrlSosReportApiCallHandler.getSosReport(filterNodes, sinceDate)
            .subscriberContext(requestHelper.createContext(ApiConsts.API_REQ_SOS_REPORT, request))
            .flatMap(sosReport ->
            {
                ApiCallRcImpl apiCallRc = ApiCallRcImpl.singletonApiCallRc(
                    ApiCallRcImpl.entryBuilder(
                        ApiConsts.CREATED | ApiConsts.MASK_SUCCESS,
                        "SOS Report created on Controller: " + sosReport
                        ).putObjRef("path", sosReport).build());
                return Mono.just(ApiCallRcRestUtils.toResponse(apiCallRc, Response.Status.CREATED));
            })
            .next();

        requestHelper.doFlux(asyncResponse, flux);
    }
}
