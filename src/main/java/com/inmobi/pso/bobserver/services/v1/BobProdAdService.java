package com.inmobi.pso.bobserver.services.v1;

/**
 * Created by mukthar.ahmed on 2/27/16.
 * Service which generates ads using ad-creative GUID
 */


import com.inmobi.pso.bob.core.AdGenerator;
import com.inmobi.pso.bob.factory.BobAdRequest;
import com.sun.jersey.multipart.FormDataMultiPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.*;


@Path("/prod")
public class BobProdAdService {
    private static Logger LOG = LoggerFactory.getLogger(BobProdAdService.class.getName());
    private static String PSO_RESOURCE_PKG = "/com/inmobi/pso/configs";


    /**
     * Description  - Method mapped to fetch Ad response generated out of creative GUID
     * param    -
     *          - creativeGuid
     *
     * return      - Html response
     */
    @GET
    @Path("/adsbyguid")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getAdsByCreativeGuid(@Context UriInfo ui) {
        MultivaluedMap<String, String> queryParams = ui.getQueryParameters();

        return processAdRequest(queryParams);
    }   // end ()


    /**
     * param       -
     * return      -
     */
    @POST
    @Path("/adsbyguid")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response postAdsByCreativeGuid(FormDataMultiPart multiPartData) {
        LOG.debug("+ Creative GUID: " + multiPartData.getField("creativeGuid").getValue() );

        return Response.status(200).entity("All is well").build();
    }   // end ()


    /**
     * param       -
     * return      - Html response
     */
    private static Response processAdRequest(MultivaluedMap<String, String> queryParams) {
        LOG.debug("+ Query params = " + queryParams.toString());

        /** loading config files */
        String bobConfigFile = PSO_RESOURCE_PKG + "/bob.properties";
        LOG.debug("+ Using bob-config file: " + bobConfigFile);
        String testConfigFile = AdGenerator.class.getResource(bobConfigFile).getFile();


        BobAdRequest.DeviceOsTypes reqOs;
        if ( queryParamFormatter(queryParams.get("os")).equalsIgnoreCase("ios") ) {
            reqOs = BobAdRequest.DeviceOsTypes.IOS;
        } else {
            reqOs = BobAdRequest.DeviceOsTypes.ANDROID;
        }

        BobAdRequest.ReqCreativeTypes reqCreativeType = BobAdRequest.ReqCreativeTypes.VIDEO_VAST_URL;
        //long defaultCustomTemplateId = 7235800134520062013l;
        long defaultCustomTemplateId = 0L;

        // Create bob-request object
        BobAdRequest bobAdRequest = new BobAdRequest(queryParamFormatter(queryParams.get("creativeGuid")),
                                                    Integer.parseInt(queryParamFormatter(queryParams.get("sdkVersion"))),
                                                    Integer.parseInt(queryParamFormatter(queryParams.get("slotId"))),
                                                    reqOs,
                                                    defaultCustomTemplateId);

        // call ad generating method
        new AdGenerator(LOG);
        String adResponse = AdGenerator.generateAdResponse(bobAdRequest, testConfigFile);

        return Response.status(200).entity(adResponse).build();
    }   // end ()

    /** Query param formatter util */
    private static String queryParamFormatter(Object rawCreativeGuid) {
        return rawCreativeGuid.toString().substring(1, rawCreativeGuid.toString().length()-1);
    }



    /**
     * Health check utility for the current service.
     * param       - "name"  Some entry string
     * return      - Html response
     */
    @GET
    @Path("/health")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getMsg(@Context UriInfo ui) {
        MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
        String output;
        if (queryParams.containsKey("name")) {
            output = "Hi, " + queryParams.get("name") + ", We look healthy today, please go ahead.";
        } else {
            output = "Hi, ???, We look healthy today, please go ahead.";
        }
        return Response.status(200).entity(output).build();
    }
}
