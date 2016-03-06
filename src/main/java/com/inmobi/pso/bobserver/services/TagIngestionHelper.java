package com.inmobi.pso.bobserver.services;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/*
 * Created by mukthar.ahmed on 23/12/14.
 * Description: Helper service which will ingest the form data of partner app to DB.
 * URL: /services/ingestion/tags
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Path("/ingestion")
public class TagIngestionHelper {

    private static Logger LOG = LoggerFactory.getLogger(TagIngestionHelper.class);
    private static String PSO_RESOURCE_PKG = "/com/inmobi/pso";
    private static String CONFIGFILE = PSO_RESOURCE_PKG + "/configs/services.properties";



    @POST
    @Path("/tags")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")   // sends JSON
    public Response consumePartnerTags(String inputReqParams) throws JSONException {
        // Converting input string object to json object the service level:
        JSONObject inputReqJson = new JSONObject(inputReqParams);



        // Bind the json object to a string and return with http-200 status code:
        return Response.status(200).entity(inputReqJson.toString()).build();
    }
}
