package com.inmobi.pso.bobserver.services.v1;


import com.inmobi.pso.bob.core.AdGeneratorByTag;
import com.inmobi.pso.bob.factory.BobAdRequest;
import com.inmobi.pso.bobserver.factory.RMCastRequest;
import com.inmobi.pso.bobserver.helpers.AdParseWrapper;
import com.inmobi.pso.bobserver.helpers.GeneralUtils;
import com.inmobi.pso.bobserver.helpers.HtmlTemplates;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import com.sun.net.httpserver.HttpServer;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.File;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * Created by mukthar.ahmed on 11/27/15.
 * BobAdRespServer
 * - This is the parent service class
 * - each method is a service in itself
 * - "services/bob" the parent path
 * - "services/bob/adasjson - path to json based ad request and response service.
 * - "services/bob/rmcast - path to RMCreative Serve tool's service call
 */

@Path("/bob")
public class BobAssetsAdService {
    public static Logger LOG = LoggerFactory.getLogger(BobAssetsAdService.class.getName());

    private static final String FOLDER_PATH = "/opt/tomcat8/webapps/bob-static/banners/";

    @Context
    ServletContext context;

    ServletRequest request;

    HttpServer server;

    Response response;
    @Context
    UriInfo ui;

    /**
     * ########################################################################################
     */
    @POST
    @Produces("text/html")
    @Path("/rmcast")
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.MULTIPART_FORM_DATA})
    public Response adResponseForRMCast(
        @FormDataParam("file") InputStream fis,
        @FormDataParam("file") FormDataContentDisposition formDataContent,
        @FormDataParam("contentProvider") String inContentProvider,
        @FormDataParam("creativeType") String inCreativeType,
        @FormDataParam("slotId") String inSlotId,
        @FormDataParam("landingUrl") String inLandingUrl,
        @FormDataParam("adTag") String inAdTag,
        @FormDataParam("os") String inOs,
        @FormDataParam("sdkVersion") String inTargetSdk,
        @FormDataParam("clickTrackerUrl") String inClickTrackerUrl,
        @FormDataParam("bannerUrl") String inBannerUrl,
        @FormDataParam("adTemplateId") String inAdTemplateId,
        @FormDataParam("defaultTemplateId") String inDefaultAdTemplateId,
        @FormDataParam("metaJson") String inMetaJson,
        @FormDataParam("adTemplate") String inAdTemplate,
        @FormDataParam("isRewarded") String isRewardedAd,
        @FormDataParam("vastUrl") String vastUrl) {


        MDC.put("logFileName", "ads_rmcast_");
        LOG.debug("+ RM creative services, starting to build ad response.");

        // ToDo: Change the part of setting paths to a common method
        /** location to store file uploads */
        long currentEpoch = System.currentTimeMillis();
        String fileUniqId = String.valueOf(currentEpoch);

        LOG.debug("+ Context Path: " + context.getContext(File.separator));

        String bobHomeLocal = context.getRealPath(File.separator);
        String baseUri = ui.getBaseUri().toString();
        String adStaticWebappPath = "bob-static";
        String uploadedBannersDir = "banners";     // dir name where uploaded banners are available

        String adsHostServerApp = bobHomeLocal + "../" + adStaticWebappPath + "/";


        /** Get server paths of the files */
        String[] baseUriSplit = baseUri.split("/");
        String serverBaseUrl = baseUriSplit[0] + "//" + baseUriSplit[2] + "/" + adStaticWebappPath;


        /** Uploading the file to bob-static web path */
        String uploadedBannerFile = formDataContent.getFileName();

        if (!uploadedBannerFile.isEmpty()) {
            LOG.debug("+ File upload request will be processed....");

            /** Check if file upload was successful or not */
            if (!GeneralUtils.fileUploadUtils(FOLDER_PATH, fis, formDataContent)) {
                return Response
                    .status(Response.Status.OK)
                    .entity(HtmlTemplates.BannerFileUploadConfict(uploadedBannerFile))
                    .build();
            }

            /** set banner-url after uploading, if upload is successful */
            inBannerUrl = serverBaseUrl + "/" + uploadedBannersDir + "/" + uploadedBannerFile;
        }

        /** Rephrase banner_tpu to banner so as to avoid whole lot of change chain down stream */
        if (inCreativeType.equalsIgnoreCase("banner_tpbu")) {
            inCreativeType = "banner";
        }

        RMCastRequest rmCastRequest
            = new RMCastRequest(inContentProvider, inCreativeType, inSlotId,
            inLandingUrl, inAdTag, inOs, inTargetSdk, inClickTrackerUrl, inBannerUrl,
            inAdTemplateId, inDefaultAdTemplateId, inMetaJson, inAdTemplate, Boolean.valueOf
            (isRewardedAd), vastUrl);


        /** Parse input form parameters */
        LOG.debug("+ Input form parameters: " + rmCastRequest.toString());


        /** init bob-static app path with lib and images */
        GeneralUtils.initBobstaticApp(bobHomeLocal, adStaticWebappPath);

        File qrCodeWorkspace = new File(adsHostServerApp + "/" + "qrcodes");
        File adsWorkspace = new File(adsHostServerApp + "/" + "ads");

        if (!qrCodeWorkspace.exists()) {
            qrCodeWorkspace.mkdirs();
        }
        if (!adsWorkspace.exists()) {
            adsWorkspace.mkdirs();
        }


        String fileNameAdsUrlQR = "qrcode_" + fileUniqId + ".png";
        String fileNameSimpleQR = "qrcode_simple_apps_" + fileUniqId + ".png";

        String adsQRCodeFileLocal = qrCodeWorkspace + "/" + fileNameAdsUrlQR;
        String simpleAppsQRCodeFileLocal = qrCodeWorkspace + "/" + fileNameSimpleQR;

        /** Generate ad response */
        String resp = adResponseModerator(rmCastRequest);
        LOG.debug("Ad Response: " + resp);

        /** You would have got ad object by now, parse using wrapper class and get individuals */
        AdParseWrapper adParseWrapper = new AdParseWrapper(resp, rmCastRequest.getTargetSdk());

        /** write the ad response to a hosted html file */
        new GeneralUtils(LOG);
        String adsFileLocal, jsAdCodeLocalFile, jsAdAppUrl;

        /** final hosted ad response file varies based on sdk or mobile web js ad */
        if (rmCastRequest.getTargetSdk() == 0) {    /** for mobile web */
            jsAdCodeLocalFile = adsWorkspace + "/" + "js_ad_code_" + fileUniqId + ".html";
            jsAdAppUrl = serverBaseUrl + "/ads" + "/js_ad_code_" + fileUniqId + ".html";
            adsFileLocal = adsWorkspace + "/" + "mweb_file_" + fileUniqId + ".html";

            // ToDo: Check for its runtime errors
            GeneralUtils.writeToFile(adParseWrapper.getJsAd(), jsAdCodeLocalFile);


            int[] dimensions = BobAdRequest.slotToDimensions(rmCastRequest.getSlotId());

            GeneralUtils.writeToFile(
                HtmlTemplates.MobileWebAppBuilder(jsAdAppUrl, String.valueOf(dimensions[0]),
                    String.valueOf(dimensions[1])), adsFileLocal);

        } else {    /** for In-App ads */
            adsFileLocal = adsWorkspace + "/" + "file_" + fileUniqId + ".html";
            GeneralUtils.writeToFile(adParseWrapper.getAd(), adsFileLocal);

        }


        String adsHostedFile;
        if (rmCastRequest.getTargetSdk() == 0) {
            adsHostedFile = serverBaseUrl + "/ads" + "/mweb_file_" + fileUniqId + ".html";
        } else {
            adsHostedFile = serverBaseUrl + "/ads" + "/file_" + fileUniqId + ".html";
        }

        String qrCodeAdHostedFile = serverBaseUrl + "/qrcodes/" + fileNameAdsUrlQR;
        String qrCodeSimplifyHostedFile = serverBaseUrl + "/qrcodes/" + fileNameSimpleQR;

        /** Generate QR Code image file */
        GeneralUtils.QRCodeGenerator(adsHostedFile, adsQRCodeFileLocal);


        /** Simplyfy App Changes - Start */
        JSONObject simplifyAppDetails = new JSONObject();
        String simplifyAdType = rmCastRequest.getCreativeType();
        if (rmCastRequest.getCreativeType().equalsIgnoreCase("video") ||
            rmCastRequest.getCreativeType().equalsIgnoreCase("video_vast_url")) {
            simplifyAdType = "interstitial";
        }
        try {
            simplifyAppDetails.put("url", adsHostedFile);
            simplifyAppDetails.put("slotid", rmCastRequest.getSlotId());
            simplifyAppDetails.put("adType", simplifyAdType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        /** Generate QR Code image file */
        GeneralUtils.QRCodeGenerator(simplifyAppDetails, simpleAppsQRCodeFileLocal);

        /** Simplyfy App Changes - End */

        /** Bundle as a html response for RM creative server tool's final page */
        String adResponse;
        if (rmCastRequest.getTargetSdk() >= 500) {
            adResponse = HtmlTemplates.templateToRmcastResp(
                adsHostedFile,
                qrCodeAdHostedFile,
                qrCodeSimplifyHostedFile,
                adParseWrapper.getDecryptedAd());
        } else {
            adResponse = HtmlTemplates.templateToRmcastResp(
                adsHostedFile,
                qrCodeAdHostedFile,
                qrCodeSimplifyHostedFile,
                adParseWrapper.getAd());
        }


        /** Make sure the log appender for MDC is removed at the end else an exception might occur */
        MDC.remove("logFileName");

        return Response.status(Response.Status.OK).entity(adResponse).build();

    }   // end adResponseForRMCast()


// ToDo: Enable this later....
//    // ########################################################################################
//    @POST
//    @Path("/adasjson")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces("application/json")   // sends JSON
//    public Response adReqAsJson(String inputReqParams) throws JSONException {
//        MDC.put("logFileName", "ads_json");
//        LOG.debug("+ RM creative services, starting to build ad response.");
//
//        // Converting input string object to json object the service level:
//        JSONObject inputReqJson = new JSONObject(inputReqParams);
//
//
//    AdParseWrapper adWrapper = new AdParseWrapper(adResponseModerator(inputReqJson),
// Integer.parseInt(inputReqJson.get(keyTargetSdk).toString()));
//        AdParser adParser =
//                new AdParseWrapper(adResponseModerator(inputReqJson),
//                        Integer.parseInt(inputReqJson.get(keyTargetSdk).toString()));
//
//        JSONObject outAdResponseJson  = new JSONObject();
//        outAdResponseJson.put("ad", adParser.getAd());
//
//        return Response.status(200).entity(outAdResponseJson.toString()).build();
//    }


    // ########################################################################################
    public static String adResponseModerator(RMCastRequest inRMCastRequest) {
        // retaining it blank in all the cases, can be change when requried
        String inBannerAltText = inRMCastRequest.getBannerAltText();

        String inClickTracker = inRMCastRequest.getClickTrackerUrl();
        String inBannerTpbu = inRMCastRequest.getBannerUrl();
        String inContentProvider = inRMCastRequest.getContentProvider();
        String inCreativeType = inRMCastRequest.getCreativeType();
        String inSlotId = String.valueOf(inRMCastRequest.getSlotId());

        /** Set blank ad-tag string */
        String inAdTag;
        if (inCreativeType.equalsIgnoreCase("banner")
            || inCreativeType.equalsIgnoreCase("banner_tpbu")) {
            LOG.debug("+ Generating ad response out of Banner/TPBU.");
            inAdTag = "";
        } else {
            LOG.debug("+ Generating ad response out of Interstitial.");
            inAdTag = inRMCastRequest.getAdTag();
        }

        /** landing url */
        String inLandingUrl = inRMCastRequest.getLandingUrl();

        /** sdk */
        int inTargetSdk = inRMCastRequest.getTargetSdk();

        /** Content provider */
        BobAdRequest.ContentProviderNames contentProvider = null;
        if (inContentProvider.equalsIgnoreCase("sprout")) {
            contentProvider = BobAdRequest.ContentProviderNames.SPROUT;
        } else {
            contentProvider = BobAdRequest.ContentProviderNames.SELF;
        }

        /** Device OS */
        String inOs = inRMCastRequest.getOs();
        BobAdRequest.DeviceOsTypes requestedDeviceOs = null;
        if (inOs.equalsIgnoreCase("android")) {
            requestedDeviceOs = BobAdRequest.DeviceOsTypes.ANDROID;
        } else {
            requestedDeviceOs = BobAdRequest.DeviceOsTypes.IOS;
        }

        AdGeneratorByTag adGeneratorByTag = new AdGeneratorByTag(LOG);
        String adResponse = adGeneratorByTag.generateAdResponse(inCreativeType,
            Integer.parseInt(inSlotId),
            inTargetSdk,
            requestedDeviceOs,
            inLandingUrl,
            inClickTracker,
            inAdTag,
            inBannerTpbu,
            inBannerAltText,
            inRMCastRequest.getMetaJson(),
            inRMCastRequest.getAdTemplateId(),
            inRMCastRequest.getAdTemplateContent(),
            inRMCastRequest.getIsVideoRewarded(),
            contentProvider);

        return adResponse;

    }


    // ########################################################################################
    // Global variables:
    static String videoMetaAdTag = "{\"ad\":{\"images\":[{\"size\":35939,\"assetId\":865373,\"width\":480,\"url\":\"http://r.edge.inmobicdn.net/FileData/528b5b46-cc3b-48d6-a26e-7ac64477942e.jpeg\",\"height\":320}],\"ad_type\":[\"interstitial\",\"native\"],\"creative_type\":\"video\",\"videos\":[{\"duration\":30,\"original\":{\"size\":19369871,\"assetId\":865312,\"width\":1920,\"url\":\"http://adtools.inmobicdn.net/videoads/prod/cf8ec0ce92ae46c6bc66665370cd8016/videos/d605e636-7c14-4718-a4a9-bd551481d0bf/original-bd9490a3-26da-438b-8a4b-486a923b8d96\",\"height\":1080},\"transcoded\":[{\"size\":4180397,\"assetId\":865371,\"width\":640,\"thumbnails\":[{\"width\":640,\"url\":\"http://adtools.inmobicdn.net/videoads/prod/cf8ec0ce92ae46c6bc66665370cd8016/videos/d605e636-7c14-4718-a4a9-bd551481d0bf/video.360p.mp4_thumbnail_00009.jpg\",\"height\":360},{\"width\":640,\"url\":\"http://adtools.inmobicdn.net/videoads/prod/cf8ec0ce92ae46c6bc66665370cd8016/videos/d605e636-7c14-4718-a4a9-bd551481d0bf/video.360p.mp4_thumbnail_00008.jpg\",\"height\":360},{\"width\":640,\"url\":\"http://adtools.inmobicdn.net/videoads/prod/cf8ec0ce92ae46c6bc66665370cd8016/videos/d605e636-7c14-4718-a4a9-bd551481d0bf/video.360p.mp4_thumbnail_00007.jpg\",\"height\":360},{\"width\":640,\"url\":\"http://adtools.inmobicdn.net/videoads/prod/cf8ec0ce92ae46c6bc66665370cd8016/videos/d605e636-7c14-4718-a4a9-bd551481d0bf/video.360p.mp4_thumbnail_00006.jpg\",\"height\":360},{\"width\":640,\"url\":\"http://adtools.inmobicdn.net/videoads/prod/cf8ec0ce92ae46c6bc66665370cd8016/videos/d605e636-7c14-4718-a4a9-bd551481d0bf/video.360p.mp4_thumbnail_00005.jpg\",\"height\":360},{\"width\":640,\"url\":\"http://adtools.inmobicdn.net/videoads/prod/cf8ec0ce92ae46c6bc66665370cd8016/videos/d605e636-7c14-4718-a4a9-bd551481d0bf/video.360p.mp4_thumbnail_00004.jpg\",\"height\":360},{\"width\":640,\"url\":\"http://adtools.inmobicdn.net/videoads/prod/cf8ec0ce92ae46c6bc66665370cd8016/videos/d605e636-7c14-4718-a4a9-bd551481d0bf/video.360p.mp4_thumbnail_00003.jpg\",\"height\":360},{\"width\":640,\"url\":\"http://adtools.inmobicdn.net/videoads/prod/cf8ec0ce92ae46c6bc66665370cd8016/videos/d605e636-7c14-4718-a4a9-bd551481d0bf/video.360p.mp4_thumbnail_00002.jpg\",\"height\":360},{\"width\":640,\"url\":\"http://adtools.inmobicdn.net/videoads/prod/cf8ec0ce92ae46c6bc66665370cd8016/videos/d605e636-7c14-4718-a4a9-bd551481d0bf/video.360p.mp4_thumbnail_00001.jpg\",\"height\":360}],\"url\":\"http://adtools.inmobicdn.net/videoads/prod/cf8ec0ce92ae46c6bc66665370cd8016/videos/d605e636-7c14-4718-a4a9-bd551481d0bf/video.360p.mp4\",\"height\":360},{\"size\":4401229,\"assetId\":865372,\"width\":1280,\"thumbnails\":[{\"width\":1280,\"url\":\"http://adtools.inmobicdn.net/videoads/prod/cf8ec0ce92ae46c6bc66665370cd8016/videos/d605e636-7c14-4718-a4a9-bd551481d0bf/video.720p.mp4_thumbnail_00009.png\",\"height\":720},{\"width\":1280,\"url\":\"http://adtools.inmobicdn.net/videoads/prod/cf8ec0ce92ae46c6bc66665370cd8016/videos/d605e636-7c14-4718-a4a9-bd551481d0bf/video.720p.mp4_thumbnail_00008.png\",\"height\":720},{\"width\":1280,\"url\":\"http://adtools.inmobicdn.net/videoads/prod/cf8ec0ce92ae46c6bc66665370cd8016/videos/d605e636-7c14-4718-a4a9-bd551481d0bf/video.720p.mp4_thumbnail_00007.png\",\"height\":720},{\"width\":1280,\"url\":\"http://adtools.inmobicdn.net/videoads/prod/cf8ec0ce92ae46c6bc66665370cd8016/videos/d605e636-7c14-4718-a4a9-bd551481d0bf/video.720p.mp4_thumbnail_00006.png\",\"height\":720},{\"width\":1280,\"url\":\"http://adtools.inmobicdn.net/videoads/prod/cf8ec0ce92ae46c6bc66665370cd8016/videos/d605e636-7c14-4718-a4a9-bd551481d0bf/video.720p.mp4_thumbnail_00005.png\",\"height\":720},{\"width\":1280,\"url\":\"http://adtools.inmobicdn.net/videoads/prod/cf8ec0ce92ae46c6bc66665370cd8016/videos/d605e636-7c14-4718-a4a9-bd551481d0bf/video.720p.mp4_thumbnail_00004.png\",\"height\":720},{\"width\":1280,\"url\":\"http://adtools.inmobicdn.net/videoads/prod/cf8ec0ce92ae46c6bc66665370cd8016/videos/d605e636-7c14-4718-a4a9-bd551481d0bf/video.720p.mp4_thumbnail_00003.png\",\"height\":720},{\"width\":1280,\"url\":\"http://adtools.inmobicdn.net/videoads/prod/cf8ec0ce92ae46c6bc66665370cd8016/videos/d605e636-7c14-4718-a4a9-bd551481d0bf/video.720p.mp4_thumbnail_00002.png\",\"height\":720},{\"width\":1280,\"url\":\"http://adtools.inmobicdn.net/videoads/prod/cf8ec0ce92ae46c6bc66665370cd8016/videos/d605e636-7c14-4718-a4a9-bd551481d0bf/video.720p.mp4_thumbnail_00001.png\",\"height\":720}],\"url\":\"http://adtools.inmobicdn.net/videoads/prod/cf8ec0ce92ae46c6bc66665370cd8016/videos/d605e636-7c14-4718-a4a9-bd551481d0bf/video.720p.mp4\",\"height\":720},{\"size\":4282854,\"assetId\":865368,\"width\":854,\"thumbnails\":[{\"width\":854,\"url\":\"http://adtools.inmobicdn.net/videoads/prod/cf8ec0ce92ae46c6bc66665370cd8016/videos/d605e636-7c14-4718-a4a9-bd551481d0bf/video.480p.mp4_thumbnail_00009.jpg\",\"height\":480},{\"width\":854,\"url\":\"http://adtools.inmobicdn.net/videoads/prod/cf8ec0ce92ae46c6bc66665370cd8016/videos/d605e636-7c14-4718-a4a9-bd551481d0bf/video.480p.mp4_thumbnail_00008.jpg\",\"height\":480},{\"width\":854,\"url\":\"http://adtools.inmobicdn.net/videoads/prod/cf8ec0ce92ae46c6bc66665370cd8016/videos/d605e636-7c14-4718-a4a9-bd551481d0bf/video.480p.mp4_thumbnail_00007.jpg\",\"height\":480},{\"width\":854,\"url\":\"http://adtools.inmobicdn.net/videoads/prod/cf8ec0ce92ae46c6bc66665370cd8016/videos/d605e636-7c14-4718-a4a9-bd551481d0bf/video.480p.mp4_thumbnail_00006.jpg\",\"height\":480},{\"width\":854,\"url\":\"http://adtools.inmobicdn.net/videoads/prod/cf8ec0ce92ae46c6bc66665370cd8016/videos/d605e636-7c14-4718-a4a9-bd551481d0bf/video.480p.mp4_thumbnail_00005.jpg\",\"height\":480},{\"width\":854,\"url\":\"http://adtools.inmobicdn.net/videoads/prod/cf8ec0ce92ae46c6bc66665370cd8016/videos/d605e636-7c14-4718-a4a9-bd551481d0bf/video.480p.mp4_thumbnail_00004.jpg\",\"height\":480},{\"width\":854,\"url\":\"http://adtools.inmobicdn.net/videoads/prod/cf8ec0ce92ae46c6bc66665370cd8016/videos/d605e636-7c14-4718-a4a9-bd551481d0bf/video.480p.mp4_thumbnail_00003.jpg\",\"height\":480},{\"width\":854,\"url\":\"http://adtools.inmobicdn.net/videoads/prod/cf8ec0ce92ae46c6bc66665370cd8016/videos/d605e636-7c14-4718-a4a9-bd551481d0bf/video.480p.mp4_thumbnail_00002.jpg\",\"height\":480},{\"width\":854,\"url\":\"http://adtools.inmobicdn.net/videoads/prod/cf8ec0ce92ae46c6bc66665370cd8016/videos/d605e636-7c14-4718-a4a9-bd551481d0bf/video.480p.mp4_thumbnail_00001.jpg\",\"height\":480}],\"url\":\"http://adtools.inmobicdn.net/videoads/prod/cf8ec0ce92ae46c6bc66665370cd8016/videos/d605e636-7c14-4718-a4a9-bd551481d0bf/video.480p.mp4\",\"height\":480}]}]},\"supplyTags\":{\"non_rewarded\":true},\"inmTag\":{\"a15\":true,\"a17\":true}}\n";

    static String videoCustomAdTemplate = "#set ($hash='#') ${first.prefix}\n" +
        "## Protocol is http by default\n" +
        "#set($PROTOCOL = \"http\")\n" +
        "#if($ad.secure)\n" +
        "\t#set($PROTOCOL = \"https\")\n" +
        "#end\n" +
        "<style type=\"text/css\" id=\"${first.ns}adStyles\">${hash}${first.ns}sa{background:rgba(0,0,0,.8);width:100%;height:100%;position:relative;overflow:hidden;display:none}</style><div id=\"${first.ns}sa\"></div><script>var _Sprout=_Sprout||{};_Sprout.impressionTracker=\"PUT_IMPRESSION_TRACKER_HERE\",_Sprout.clickTracker=\"PUT_CLICK_TRACKER_HERE\",_Sprout.publisherLabel=\"PUT_PUBLISHER_LABEL_HERE\",_Sprout.xvars={_incentCBJson:JSON.stringify($tool.evalJsonPath($ad.sitePreferencesJson,\"$.incentiveJSON\"))},function(){var a=function(a){var b=\"$PROTOCOL\",c=/^https?/;return a.replace(c,b)},b=function(){var a=\"$ad.sdkVersion\";return parseInt(a.substr(1),10)||0},c=function(){var a=b(),c=a>=450,d=window.${first.ns}fireAdFailed;c&&void 0!==typeof d&&(D(99,{action:\"ad-failed\"}),d())},d=window,e=d.mraid,f=d.imraid,g=document.body.style,h=document.documentElement.style,i=navigator.userAgent,j=\"${first.ns}\",k=i.match(/iPhone|iPad|iPod/i)?\"ios\":i.match(/Android/i)?\"android\":\"\",l=0,m=!1,n=\"100\"+String.fromCharCode(37),o=[\"83dLOElOZFWmfvf2\",\"oxCtLtdsDdBwgxDR\",\"uqCmrRjcWv5FHoiY\",\"72n5fQslpBdy0o9k\"],p=\"LKtL9P0xb5TwlwPC\",q=480,r=320,s=\"$tool.evalJsonPath($ad.requestJson, '$.networkType')\",t=\"html\",u=\"preload\"+(new Date).getTime(),v=!1,w=b(),x=\"blocking\",y=!0,z=o[0],A=!0,B=!1,C=window.${first.ns}recordEvent,D=function(a,b){var c={spid:z||\"\",src:\"sprout\",rl:_Sprout.publisherLabel,n:\"inmobi\",at:0,__t:(new Date).getTime()};if(99===a)for(var d in b)b.hasOwnProperty(d)&&(c[d]=b[d]);C&&\"function\"==typeof C&&C(a,c)},E=window.${first.ns}disableAutoFireAdReady,F=window.${first.ns}fireAdReady,l=0,G=w>=450;\"blocking\"===x&&G&&E&&E();var H=function(a){var b;return a>=0&&1>=a?b=\"0_1\":a>1&&2>=a?b=\"1_2\":a>2&&3>=a?b=\"2_3\":a>3&&5>=a?b=\"3_5\":a>5&&10>=a?b=\"5_10\":a>10&&20>=a?b=\"10_20\":a>20&&60>=a?b=\"20_60\":a>60&&(b=\"60p\"),b},I=function(){if(\"loading\"!==e.getState()){if(\"ios\"===k&&G&&F)return void F();var a,b,d,g=\"\";B&&_Sprout.xvars.video_url?(a=(new Date).getTime(),f.saveContent(u,_Sprout.xvars.video_url,function(f,h){if(b=(new Date).getTime(),d=parseFloat((b-a)/1e3),g=H(d),\"success\"!=f)return G&&\"android\"===k&&\"undefined\"!=typeof y&&y&&l++<3?setTimeout(I,1e3):(D(99,{action:\"cache_error_\"+h.reason}),G&&\"undefined\"!=typeof y&&y&&D(99,{action:\"cache_failure_retry_\"+(l-1),label:h.reason}),void c());if(D(99,{action:\"cache_time_\"+g}),\"function\"==typeof e.isViewable&&!e.isViewable()){var i='{\"action\" : \"setXvar\", \"sproutID\" : \"'+z+'\", \"params\" : {\"video_url\": \"'+h.saved_url+'\"}}',j=document.getElementById(\"Sprout_\"+z);if(j){var m=j.contentWindow;m.postMessage(i,\"*\")}else _Sprout.xvars.video_url=h.saved_url}l>0&&D(99,{action:\"cache_success_retry_\"+l}),v=!0,\"blocking\"===x&&G&&F&&F()})):\"blocking\"===x&&G&&F&&F()}else e.addEventListener(\"ready\",I)},J=function(a){return document.getElementById(a)},K=J(j+\"sa\"),L=function(){var a=i.match(/Android\\s+([\\d\\.]+)/i);return a?a[1]:!1},M=function(a,b){var c=a?a:\"auto\",d=b?b:\"auto\";h.width=c,h.height=d,g.width=c,g.height=d},N=function(a,b){a.style.display=b?\"block\":\"none\"},O=function(){\"ios\"!==k&&B&&!v&&(f.cancelSaveContent(u),D(99,{action:\"preBuffer_cancelled\"})),M(n,n),N(K,!0)},P=function(){\"function\"==typeof e.useCustomClose&&e.useCustomClose(!0),f&&\"function\"==typeof f.setOrientationProperties?f.setOrientationProperties({allowOrientationChange:!1,forceOrientation:\"landscape\",direction:\"right\"}):e.setOrientationProperties({allowOrientationChange:!1,forceOrientation:\"landscape\"}),I(),\"function\"==typeof e.isViewable&&e.isViewable()?O():e.addEventListener(\"viewableChange\",function(a){a&&(e.removeEventListener(\"viewableChange\",arguments.callee),O())}),w>=450&&f&&\"function\"==typeof f.disableCloseRegion&&f.disableCloseRegion(!0),f&&\"function\"==typeof f.disableBackButton&&f.disableBackButton(!0),\"ios\"===k&&f&&\"function\"==typeof f.hideStatusBar&&f.hideStatusBar()},Q=function(){l++,4!==l?void 0!==typeof e?(m=!0,\"loading\"===e.getState()?e.addEventListener(\"ready\",P):P()):setTimeout(Q,500):O()},R=function(b,c){var d=b[0].transcoded,e={},f=[];0===c?d.sort(function(a,b){return a.size-b.size}):d.sort(function(a,b){return b.size-a.size});var g=d[0].thumbnails;return f.push(g[1]?a(g[1].url):\"\"),f.push(g[3]?a(g[3].url):\"\"),f.push(g[5]?a(g[5].url):\"\"),e={url:a(d[0].url),screenshots:f}},S=function(b,c){var d,e=q/r,f=[],g=\"\";1.775==e&&(e=1.5);for(var h=0;h<b.length;h++)d=b[h],d.width/d.height===e&&f.push(d);return 1===f.length?g=f[0].url:f.length>1&&(0===c?f.sort(function(a,b){return a.size-b.size}):f.sort(function(a,b){return b.size-a.size}),g=f[0].url),g=a(g)},T=function(){var a,b=$tool.evalJsonPath($first.jsonObject,\"ad\"),c=b?b.videos:[],d=b?b.images:[],e=0;e=s?\"WIFI\"==s?1:0:A?0:1,a=R(c,e),_Sprout.xvars.video_url=a.url;for(var f=0,g=a.screenshots.length;g>f;f++)_Sprout.xvars[\"background_\"+(f+1)]=a.screenshots[f];_Sprout.xvars.fallbackImage=S(d,e)},U=function(b){for(var c=300,d=300,e=\"\",f=0,g=b.length;g>f;f++)if(b[f].width===c&&b[f].height===d){e=b[f].iconUrl;break}return e=a(e)},V=function(a){var b=18,c=/[^\\u0000-\\u007F]+/g;return c.test(a)&&(b=10),a.length>b?a.substring(0,b)+\"...\":a},W=function(){var a=[],b={},c=\"en\",e=\"zh\",f=d.navigator.language||c,g=-1!==f.toLowerCase().indexOf(e)?e:c,h=f.length>=5?f.substr(3,2).toLowerCase():\"\";try{b='$!{tool.javascript($!{tool.evalJsonPath($first.appMetaJson,\"$.countryData\")})}';var i,j=\"$!{tool.javascript($!{tool.evalJsonPath($ad.requestJson,'$.countryCode')})}\".toLowerCase(),k=\"$!{tool.javascript($!{tool.evalJsonPath($first.appMetaJson,'$.mainCategory')})}\";b=\"\"!==b?JSON.parse(b):{},b&&(b[j]?i=b[j]:\"\"!==h&&b[h]&&(i=b[h])),a=i&&i.appIcons||[],k=k.replace(/^Games_/,\"\").replace(/_/g,\" \"),_Sprout.xvars.app_name=V(i&&i.appTitle&&i.appTitle+\"\"||\"\"),_Sprout.xvars.appIcon=U(a),_Sprout.xvars.starRating=i&&i.starRating&&i.starRating+\"\"||\"\",_Sprout.xvars.no_ratings=i&&i.ratingCount&&i.ratingCount+\"\"||\"\",_Sprout.xvars.developer=V(i&&i.developerName&&i.developerName+\"\"||\"\"),_Sprout.xvars.category=V(k&&\"null\"!==k?k:\"\")}catch(l){D(99,{action:\"json-parse-error\"})}return g},X=function(){var a,b=\"$tool.evalJsonPath($ad.sitePreferencesJson, '$.video.preBuffer')\";parseFloat(L());(\"ALWAYS\"===b||\"WIFI\"===b&&\"WIFI\"===s)&&\"function\"==typeof f.saveContent&&(\"ios\"===k||\"android\"===k&&w>=450)&&(B=!0),a=B?\"android\"===k?\"Android_InstantPlay\":\"iOS_InstantPlay\":\"android\"===k?\"Android_Regular\":\"iOS_Regular\",_Sprout.publisherLabel=a},Y=function(b){var c={en:[\"Get it for free!\"],zh:[\"免费获取!\"]},d={android:{en:\"http://adtools.inmobicdn.net/c/MNVy1ONMXp3ddm2ZS/o.png\",zh:\"http://adtools.inmobicdn.net/c/N677algoKjCvFVmpw/o.png\"},ios:{en:\"http://adtools.inmobicdn.net/c/R0che4sQTQoBlinSQ/o.png\",zh:\"http://adtools.inmobicdn.net/c/OYK5BA8XwokeEsIW5/o.png\"}},e=c[b];_Sprout.xvars.cta_text=e[0];var f=d[k]?d[k][b]:\"\";_Sprout.xvars.storeIcon=a(f)},Z=function(){var a='<div id=\"Sprout_'+z+'_div\" data-creativeId=\"'+z+'\"></div>',b=document.createElement(\"div\");b.innerHTML=a,K.appendChild(b.firstChild)},$=function(){var a;r>q&&(a=q,q=r,r=a);var b=q/r,c=parseFloat(L());switch(\"android\"===k&&4.1===c&&(t=\"mraid\"),b){case 1.5:z=o[0];break;case 1.775:z=\"mraid\"===t?p:o[1];break;case 1024/768:z=o[2],A=!1;break;case 1.6:z=o[3],A=!1}};$(),Z();var _=W();Y(_),X(),T(),Q();var aa=function(){var _sproutReadyEvt,a,b,_Sprout_load;_Sprout._inMobiAdTagTracking={st:(new Date).getTime(),rr:0},_Sprout[z]={querystring:{__im_curl:\"$tool.jsInline($first.beaconUrl)\",__im_sdk:\"$ad.sdkVersion\",click:\"$tool.jsInline($first.clickServerUrl)\",adFormat:\"interstitial\",__im_recordEventFun:\"${first.ns}recordEvent\",__geo_lat:\"$GEO_LAT\",__geo_lng:\"$GEO_LNG\",__geo_cc:\"$GEO_CC\",__geo_zip:\"$GEO_ZIP\",__js_esc_geo_city:\"$JS_ESC_GEO_CITY\",openLandingPage:\"${first.ns}openLandingPage\",__use_secure:\"false\"!==\"$ad.secure\"?\"true\":\"false\",__site_pref:\"$\"===\"$tool.javascript($ad.sitePreferencesJson)\".charAt(0)?{}:JSON.parse(\"$tool.javascript($ad.sitePreferencesJson)\")}};var _sproutReadyEvt=document.createEvent(\"Event\");_sproutReadyEvt.initEvent(\"sproutReady\",!0,!0),window.dispatchEvent(_sproutReadyEvt);var a,b=\"/load/\"+z+\".inmobi.html.js\",_Sprout_load=function(){var c=document.getElementsByTagName(\"script\"),c=c[c.length-1],d=document.createElement(\"script\");d.async=!0,d.type=\"text/javascript\",a=\"true\"===window._Sprout[z].querystring.__use_secure?\"https://studio.inmobicdn.net\":\"http://studio.inmobicdn.net\",d.src=a+b,c.parentNode.insertBefore(d,c.nextSibling)};\"0\"===window._Sprout[z].querystring.__im_sdk||\"complete\"===document.readyState?_Sprout_load():window.addEventListener(\"load\",_Sprout_load,!1)};aa()}();</script>${first.suffix}\n";

    private final String othersAdTag = "<meta name='viewport' content='initial-scale=1," +
        "width=device-width," +
        "user-scalable=no'><script>window.im_2245_tst=(new Date).getTime()" +
        "</script><script src='mraid.js'></script><style " +
        "type='text/css'>#im_2245_a{position:relative}#im_2245_a{position:relative;" +
        "width:100%;height:100%;margin:auto}#im_2245_p{position:relative;" +
        "text-align:center;display:none;width:100%;height:100%}#im_2245_c{width:25px;" +
        "height:25px;position:absolute;top:0px;right:10px;display:none;z-index:10}" +
        ".im_2245_i{border:0}body{padding:0;margin:0;background-color:#fff;width:100%;" +
        "height:100%}</style><div id='im_2245_ad'><div id='im_2245_a' align='center'><div" +
        " id='im_2245_p' align='center'><img id=\"im_2245_i\" class=\"im_2245_i\" " +
        "src='http://adtools-a.akamaihd.net/c/sbIJBtMTIq13Ra7kw/o.jpg' " +
        "alt='image'/></div><div id='im_2245_c'><img id=\"im_2245_ci\" " +
        "class=\"im_2245_i\" alt=\"close\"/></div></div></div><script>(function()" +
        "{function z(a,b){a.style.display=b?\"block\":\"none\"}function l(a){u&&F++;" +
        "return A.getElementById(a)}function n(){F++;return(new Date).getTime()}function " +
        "f(a,b){var d=L;999==a&&(d=\"http://ec2-23-20-116-88.compute-1.amazonaws" +
        ".com/beacon/999\");d+=\"?v=5&spid=hw-sbIJBtMTIq13Ra7kw&rl=inmobi&at=0&n=inmobi" +
        "&src=handwritten&m=\"+a+\"&sessID=\"+M+\"&__t=\"+n()+\"_001\";" +
        "8==a?d+=\"&cta=exit&ctaid=click&it=1&as=1.161.244&fi=true\":3==a&&" +
        "(d+=\"&secs=\"+Math.round((n()-v)/1E3));b&&(d+=\"&p=\"+b);var c=B-N,e=v-G," +
        "f=v-B-e;1==a?d+=\"&lt=\"+c:18==a&&(d=d+(\"&rt=\"+e)+(\"&wt=\"+f));(new Image)" +
        ".src=d}function C(a,b,d){for(var c=e.length-1;0<=c;c--)d?a.addEventListener" +
        "(e[c],b,!1):a.removeEventListener(e[c],b,!1)}function g(a){a=\"object\"===typeof" +
        " a?a:l(a);c=this;c.element=a;c.moved=!1;c.startX=0;c.startY=0;c.hteo=!1;a" +
        ".addEventListener(h[0],c,!1);a.addEventListener(h[1],c," +
        "!1)}function w(){G=n();var a;a=l(m+\"p\");z(a," +
        "!0);if(p){l(m+\"ci\").src=\"http://adtools-a.akamaihd.net/a/c.1.png\";a=l" +
        "(m+\"c\");z(a,!0);var c=!q||r;!D&&c&&(new g(a),a.addEventListener(\"tap\"," +
        "function(a){q||(a=l(m+\"ad\"),a.innerHTML=\"\",z(a," +
        "!1));r?x||f(3):f(3);r&&b.close()},!1))}D||(D=!0,v=n()," +
        "f(18))}function H(){\"function\"==typeof b.isViewable&&b.isViewable()?w():b" +
        ".addEventListener(I,function(a){a&&(b.removeEventListener(I,arguments.callee)," +
        "w())});b.addEventListener(\"stateChange\",function(a){\"hidden\"==a&&!x&&(r||f" +
        "(3))});b.addEventListener(\"error\",function(a,b){(!q||!(y&&350==k))&&f(999," +
        "E(\"m:\"+a+\",a:\"+b))});try{var a={useCustomClose:p,lockOrientation:!0," +
        "orientation:\"portrait\"};\"function\"==typeof b.setExpandProperties?b" +
        ".setExpandProperties(a):(p=!1,b.expand(null,a),y&&w())}catch(c){f(999," +
        "E(\"e:\"+c))}}function J(){K++;2!=K?\"undefined\"!==typeof window.mraid?" +
        "(b=window.mraid,q=!0,s=\"function\"==typeof b.getInMobiAIVersion?b" +
        ".getInMobiAIVersion():\"1.0\",\"1.0\"==s?k=300:\"1.1\"==s?k=350:\"1" +
        ".2\"==s?k=360:\"2.0\"==s&&(k=370),O&&(p=!1)," +
        "\"function\"==typeof b.useCustomClose&&b.useCustomClose(p),r=y&&300==k," +
        "\"loading\"===b.getState()?b.addEventListener(\"ready\",H):H()):setTimeout(J," +
        "500):w()}var b=window.mraid,A=window.document,E=encodeURIComponent," +
        "I=\"viewableChange\",q=!1,x=!1,D=!1,F=0,K=0,M=Math.random().toString(36).substr" +
        "(2,9),L=\"$JS_ESC_BEACON_URL\",u=\"$JS_ESC_CLICK_URL\",m=\"im_2245_\",p=!0," +
        "t=navigator.userAgent,y=t.match(/iPhone|iPad|iPod/i)?!0:!1," +
        "O=t.match(/Android/i)?!0:!1,N=window[m+\"tst\"],k=!1,c,r,s,B,v," +
        "G;window.onerror=function(a,b,c){f(999,E(\"m:\"+a+\",u:\"+b+\"," +
        "l:\"+c))};var e=[\"touchmove\",\"touchend\",\"touchcancel\",\"mousemove\"," +
        "\"mouseup\"],h=[\"touchstart\",\"mousedown\"];g.prototype.start=function(a){var " +
        "b=this.element;a.type===h[0]&&(this.hteo=!0);this.moved=!1;this.startX=a" +
        ".type===h[0]?a.touches[0].clientX:a.clientX;this.startY=a.type===h[0]?a" +
        ".touches[0].clientY:a.clientY;C(b,this,!0)};g.prototype.move=function(a){var b=a" +
        ".type===e[0]?a.touches[0].clientY:a.clientY;if(10<Math.abs((a.type===e[0]?a" +
        ".touches[0].clientX:a.clientX)-this.startX)||10<Math.abs(b-this.startY))this" +
        ".moved=!0};g.prototype.end=function(a){var b=this.element," +
        "c;this.hteo&&a.type===e[4]?(a.preventDefault(),a.stopPropagation()," +
        "this.hteo=!1):(this.moved||(\"function\"===typeof CustomEvent?c=new CustomEvent" +
        "(\"tap\",{bubbles:!0,cancelable:!0}):(c=A.createEvent(\"Event\")," +
        "c.initEvent(\"tap\",!0,!0)),a.target.dispatchEvent(c)),C(b,this," +
        "!1))};g.prototype.cancel=function(a){a=this.element;this.moved=!1;this" +
        ".startY=this.startX=0;C(a,this,!1)};g.prototype.handleEvent=function(a){c=this;" +
        "switch(a.type){case h[0]:c.start(a);break;case e[0]:c.move(a);break;case e[1]:c" +
        ".end(a);break;case e[2]:c.cancel(a);break;case h[1]:c.start(a);break;case e[3]:c" +
        ".move(a);break;case e[4]:c.end(a)}};A.addEventListener(e[0]," +
        "function(a){a.preventDefault()});t=l(m+\"p\");new g(t);t.addEventListener" +
        "(\"tap\",function(a){x||(x=!0,f(8),q?(\"function\"==typeof b.openExternal?b" +
        ".openExternal(u):b.open(u),y?350!=k&&b.close():setTimeout(b.close," +
        "4E3)):window.location.href=u)},!1);B=n();f(1);J()})();</script>";

    private final String sproutAdTag = "<script src=\"mraid.js\"></script>\n" +
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


    @Path("/health")
    public static class HealthCheckService {

        @GET
        @Produces(MediaType.TEXT_PLAIN)
        public Response getMsg(@PathParam("param") String name) {
            //remember remove this
            MDC.remove("logFileName");
            String output;
            if (name == null || name.isEmpty()) {
                output = "Hi, " + name + ", We look healthy today, please go ahead.";
            }
            else {
                output = "Hi, ???, We look healthy today, please go ahead.";
            }
            return Response.status(200).entity(output).build();
        }
    }
}   // end of class
