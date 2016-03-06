package com.inmobi.pso.bobserver.client;



import com.inmobi.pso.bob.factory.BobAdRequest;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;

/*
  Created by: mukthar.ahmed@inmobi.com
  Date: 26/04/14.
 */
public class AdRequestClient {
    private static final String HOSTNAME = "bob-pso.app.uj1.inmobi.com";
    private static final String PORT = "8080";
//    private static final String HOSTNAME = "10.14.123.122";
//    private static final String PORT = "8081";
    private static final String APP_PATH = "bob";
    private static final String SERVICE_PATH = "services/v1/bob/adasjson";


    private static final String someAdTag = "<script src=\"mraid.js\"></script>\n" +
            "<div id=\"Sprout_LWWnQ5cR2KmXRH2e_div\" data-creativeId=\"LWWnQ5cR2KmXRH2e\"></div>\n" +
            "<script type=\"text/javascript\">\n" +
            "var _Sprout = _Sprout || {};\n" +
            "\n" +
            "/* 3rd Party Impression Tracker: a tracking pixel URL for tracking 3rd party impressions */\n" +
            "_Sprout.impressionTracker = \"PUT_IMPRESSION_TRACKER_HERE\";\n" +
            "\n" +
            "/* 3rd Party Click Tracker: A URL or Macro like %c for third party exit tracking */\n" +
            "_Sprout.clickTracker = \"PUT_CLICK_TRACKER_HERE\";\n" +
            "\n" +
            "/* Publisher Label: What you want to call this line-item in Studio reports */\n" +
            "_Sprout.publisherLabel = \"PUT_PUBLISHER_LABEL_HERE\";\n" +
            "\n" +
            "/* External variables: parameters for dynamic ads */\n" +
            "_Sprout.xvars = {\n" +
            "  \"abcd\" : \"$USER_ID_SHA1_HASHED\"\n" +
            "};\n" +
            "\n" +
            "\n" +
            "_Sprout._inMobiAdTagTracking={st:new Date().getTime(),rr:0};_Sprout[\"LWWnQ5cR2KmXRH2e\"]={querystring:{__im_curl:\"$JS_ESC_BEACON_URL\",__im_sdk:\"$SDK_VERSION_ID\",click:\"$JS_ESC_CLICK_URL\",adFormat:\"interstitial\",__im_recordEventFun:\"$RECORD_EVENT_FUN\",__geo_lat:\"$GEO_LAT\",__geo_lng:\"$GEO_LNG\",__geo_cc:\"$GEO_CC\",__geo_zip:\"$GEO_ZIP\",__js_esc_geo_city:\"$JS_ESC_GEO_CITY\",openLandingPage:\"$OPEN_LP_FUN\"}};var _sproutReadyEvt=document.createEvent(\"Event\");_sproutReadyEvt.initEvent(\"sproutReady\",true,true);window.dispatchEvent(_sproutReadyEvt);var _Sprout_load=function(){var e=document.getElementsByTagName(\"script\"),e=e[e.length-1],t=document.createElement(\"script\");t.async=!0;t.type=\"text/javascript\";t.src=\"http://edgy.sproutbuilder.com/load/LWWnQ5cR2KmXRH2e.inmobi.html.js\";e.parentNode.insertBefore(t,e.nextSibling)};\"0\"===window[\"_Sprout\"][\"LWWnQ5cR2KmXRH2e\"][\"querystring\"][\"__im_sdk\"]||\"complete\"===document.readyState?_Sprout_load():window.addEventListener(\"load\",_Sprout_load,!1)</script>";

    public static String keyTargetSdk = "SDK_VERSION";
    public static String keyContentProvider = "CONTENT_PROVIDER";
    public static String keyCreativeType = "CREATIVE_TYPE";
    public static String keySlotId = "SLOT_ID";
    public static String keyOs = "OS";
    public static String keyLandingUrl = "LANDING_URL";
    public static String keyAdTag = "AD_TAG";

    // ##########################################################################################
    public static void main(String[] args) throws URISyntaxException, IOException {
        JSONObject inRequestJson = new JSONObject();

        System.out.println("+ Ad request Json client started...");

        String sdkVersion = String.valueOf(BobAdRequest.Sdk500);
        String contentProvider = String.valueOf(BobAdRequest.ContentProviderNames.SPROUT);
        String creativeType = String.valueOf(BobAdRequest.ReqCreativeTypes.INTERSTITIAL);
        String slotId = String.valueOf(BobAdRequest.SlotId_14);
        String os = String.valueOf(BobAdRequest.DeviceOsTypes.ANDROID);
        String landingUrl = "";
        inRequestJson.put(keyTargetSdk, sdkVersion);
        inRequestJson.put(keyContentProvider, contentProvider);
        inRequestJson.put(keyCreativeType, creativeType);
        inRequestJson.put(keySlotId, slotId);
        inRequestJson.put(keyOs, os);
        inRequestJson.put(keyLandingUrl, landingUrl);
        inRequestJson.put(keyAdTag, someAdTag);

        System.out.println("+++ Returned: " + adServiceInit(inRequestJson).toString() );

    }   // end main()


    // ##########################################################################################
    //  throws URISyntaxException, IOException
    public static JSONObject adServiceInit(JSONObject inputReqJson) {
        JSONObject outJsonObj = new JSONObject();

        try {
            ClientConfig config = new DefaultClientConfig();
            config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

            Client client = Client.create(config);
            WebResource webResource = client.resource(getAdsServiceURI());

            ClientResponse response = webResource.accept("application/json")
                    .type("application/json").post(ClientResponse.class, inputReqJson.toString());

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }

            String output = response.getEntity(String.class);
            outJsonObj = new JSONObject(output);
            System.out.println("Server output response: " + outJsonObj.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return outJsonObj;

    }   // end CABUrlBuilder()


    // ##########################################################################################

    private static String getAdsServiceURI() {
        String sbUrl = "http://" + HOSTNAME + ":" + PORT + "/" + APP_PATH + "/" + SERVICE_PATH;
        System.out.println("Bob Ads Url: " + sbUrl);
        return sbUrl;
    }


} // end class

// ##############################################################################################
