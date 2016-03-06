package com.inmobi.pso.bobserver.services.v1;

import com.inmobi.pso.bob.adutils.decryptutils.DecryptHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

@Path("/secure")
public class SecurityService {
    private static Logger LOG = LoggerFactory.getLogger(SecurityService.class.getName());


    @POST
    @Produces("text/html")
    @Path("/decrypt")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response decryptService(MultivaluedMap<String, String> formParams) {

        String encryptedResponse = formParams.get("encryptedtxt").toString();
        String aes = formParams.get("aes").toString();
        String iv = formParams.get("iv").toString();
        String isSdk5xxVal = formParams.get("isSdk5xx").toString();

        aes = aes.substring(1, aes.length()-1);
        iv = iv.substring(1, iv.length()-1);
        boolean isSdk5xx = Boolean.valueOf(isSdk5xxVal.substring(1, isSdk5xxVal.length()-1));
        encryptedResponse = encryptedResponse.substring(1, encryptedResponse.length()-1);

        LOG.debug("");
        LOG.debug("");
        LOG.debug("+ Starting to decrypt +");
        LOG.debug("AES: \"" + aes + "\"");
        LOG.debug("iv: \"" + iv + "\"");
        LOG.debug("+ Found SDK-5.x.x response: " + isSdk5xx);

        String decryptedResponse;
        if (isSdk5xx) {
            decryptedResponse = DecryptHelper.decrypt(encryptedResponse, aes, iv, true);
        } else {
            decryptedResponse = DecryptHelper.decrypt(encryptedResponse, aes, iv, false);
        }

        StringBuilder adResponse = new StringBuilder("<HTML>\n" +
                "<HEAD>\n" +
                "  <TITLE>Decrypted inmobi Ad response</TITLE>\n" +
                "  <link rel=\"shortcut icon\" href=\"images/Bob.ico\">\n" +
                "</HEAD>\n");

        adResponse.append("<BODY>\n" +
                "<H4>Ad response decrypted</H4>\n" +
                "<textarea rows=\"30\" cols=\"120\" margin: 0px; width: 800px; height: 1150px;>" +
                decryptedResponse +
                "</textarea><br />" +
                "</BODY>\n" +
                "</HTML>\n");

        LOG.debug("+ Decrypted Ad Response: \n" + decryptedResponse);
        return Response.status(200).entity(adResponse.toString()).build();
    }


    // ##########################################################################################
    @GET
    @Produces("text/html")
    @Path("/decrypt")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response getDecryptService(@PathParam("param") String msg) {

        String en = "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";
        String key = "abcdefghijklmnop";
        DecryptHelper.decrypt(en, key, key, false);


        StringBuilder adResponse = new StringBuilder("<HTML>\n" +
                "<HEAD>\n" +
                "  <TITLE>Decrypted inmobi Ad response</TITLE>\n" +
                "  <link rel=\"shortcut icon\" href=\"images/Bob.ico\">\n" +
                "</HEAD>\n");


        adResponse.append("<BODY>\n" +
                "<H4>Ad response decrypted</H4>\n" +
                "<textarea rows=\"30\" cols=\"120\" margin: 0px; width: 800px; height: 1150px;" +
                DecryptHelper.decrypt(en, key, key, false) +
                "</textarea><br />" +
                "</BODY>\n" +
                "</HTML>\n");

        return Response.status(200).entity(adResponse.toString()).build();

    }

    // ########################################################################################
}
