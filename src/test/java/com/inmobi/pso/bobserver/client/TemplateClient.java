package com.inmobi.pso.bobserver.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * Created by mukthar.ahmed on 12/10/15.
 */
public class TemplateClient {

    public static void main(String[] args) {

        try {

            Client client = Client.create();
            WebResource webResource = client.resource("http://adtemplate-service.vip.uj1.inmobi.com:10170/adtemplate/service");
            ClientResponse response = webResource.accept("application/xml").get(ClientResponse.class);
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }

            String output = response.getEntity(String.class);
            System.out.println("============getCtoFResponse============");
            System.out.println(output);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
