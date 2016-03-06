package com.inmobi.pso.bobserver.factory;

import com.inmobi.pso.bob.factory.BobAdRequest;

/**
 * Created by mukthar.ahmed on 1/22/16.
 * Note:
 * This code is not enabled for banner-alt-text which is part of baner/tpbu ads responses
 */

public class RMCastRequest {
  /* all string input parameters */
  private String CONTENT_PROVIDER, CREATIVE_TYPE, LANDING_URL, OS,
      CLICK_TRACKER_URL, BANNER_URL, VAST_URL = null;

  private String BANNER_ALT_TEXT = "";
  private String AD_TAG, META_JSON, AD_TEMPLATE_CONTENT = "";
  private boolean IS_REWARDED = false;
  private long AD_TEMPLATE_ID = 8770247689732829490L;     // defaulted to a random ad-template-id

  /* better to have them as int parameters */
  private int SLOT_ID, TARGET_SDK;

  public RMCastRequest(String provider,
                       String creativeType,
                       String slotId,
                       String landingUrl,
                       String adTag,
                       String os,
                       String sdk,
                       String clickTrackerUrl,
                       String bannerUrl,
                       String videoAdTemplateId,
                       String defaultVideoAdTemplateId,
                       String videoMetaJson,
                       String inAdTemplate,
                       boolean isRewarded,
                       String vastUrl) {

    this.CONTENT_PROVIDER = provider;


        /* setting isRewared by default and later changing it to its appropriate value */
    this.IS_REWARDED = isRewarded;

    /** VAST VIDEO Url */
    this.VAST_URL = vastUrl;

    /** Set params based on creative-type provided "" */
    if (creativeType.equalsIgnoreCase("video_vast_url_default")) {
      this.CREATIVE_TYPE = String.valueOf(BobAdRequest.ReqCreativeTypes.VIDEO_VAST_URL).toLowerCase();

      this.handleVideoDefaultValues(defaultVideoAdTemplateId);

      /** Setting metajson using vast-url */
      videoMetaJson = vastUrlToMetajson(vastUrl);  // setting metajson for vast with vast url

    }
    else if (creativeType.equalsIgnoreCase("video_vast_url_userdef")) {
      this.CREATIVE_TYPE = String.valueOf(BobAdRequest.ReqCreativeTypes.VIDEO_VAST_URL).toLowerCase();
      this.AD_TEMPLATE_ID = Long.valueOf(videoAdTemplateId);
      videoMetaJson = vastUrlToMetajson(vastUrl);  // setting metajson for vast with vast url

    }
    else if (creativeType.equalsIgnoreCase("video_default")) {
      this.CREATIVE_TYPE = String.valueOf(BobAdRequest.ReqCreativeTypes.VIDEO).toLowerCase();

      /** grab custom template Id */
      this.handleVideoDefaultValues(defaultVideoAdTemplateId);

    }
    else if (creativeType.equalsIgnoreCase("video_userdefined")) {
      this.CREATIVE_TYPE = String.valueOf(BobAdRequest.ReqCreativeTypes.VIDEO).toLowerCase();
      this.AD_TEMPLATE_ID = Long.valueOf(videoAdTemplateId);

    }
    else {
      this.CREATIVE_TYPE = creativeType;

    }

    this.SLOT_ID = Integer.parseInt(slotId);
    this.OS = os;
    this.TARGET_SDK = Integer.parseInt(sdk);

    /** Handle request parameters which are null */
    if (landingUrl.isEmpty()) {
      this.LANDING_URL = "http://www.inmobi.com";
    } else {
      this.LANDING_URL = landingUrl;
    }

    if (adTag.isEmpty()) {
      this.AD_TAG = "";
    } else {
      this.AD_TAG = adTag;
    }

    if (clickTrackerUrl.isEmpty()) {
      this.CLICK_TRACKER_URL = "";
    } else {
      this.CLICK_TRACKER_URL = clickTrackerUrl;
    }

    if (bannerUrl.isEmpty()) {
      this.BANNER_URL = "";
    } else {
      this.BANNER_URL = bannerUrl;
    }

        /* for video ads, rewarded and non-rewarded */
    if (!inAdTemplate.isEmpty()) {
      this.AD_TEMPLATE_CONTENT = inAdTemplate;
    }


    /** Set metajson value if its not video-vast-url and is not empty */
    if (!videoMetaJson.isEmpty()) {
      this.META_JSON = videoMetaJson;
    } else {
      this.META_JSON = "";    /** Set it to an empty string if not found */
    }

  }   // end constructor()


  /** ===========================================================================================
   * Handling default values for vast-video and video ad requests
   * @param defaultVideoAdTemplateId
   */
  private void handleVideoDefaultValues(String defaultVideoAdTemplateId) {
    /** grab custom template Id */
    this.AD_TEMPLATE_ID = Long.valueOf(defaultVideoAdTemplateId.split("_")[0]);

    /** set if rewareded or not */
    if (defaultVideoAdTemplateId.split("_")[1].equalsIgnoreCase("rw")) {
      this.IS_REWARDED = true;
    } else {
      this.IS_REWARDED = false;
    }
  }

  /** ===========================================================================================
   * @param inVastUrl
   * @return metajson string
   */
  private static String vastUrlToMetajson(String inVastUrl) {
//    inVastUrl = "https://bs.serving-sys.com/BurstingPipe/adServer.bs?cn=is&c=23&pl=VAST&pli=16381256&PluID=0&pos=1908&ord=$IMP_CB&cim=1";
    String baseMetaJson = "{\"ctatext\":\"Know More\"," +
        "\"supplyTags\":" + "{\"rewarded\":true,\"non_rewarded\":true}," +
        "\"vast\":{\"version\":\"2.0\"," +
        "\"url\":\"" +
        inVastUrl +
        "\"},\"inmTag\":{\"a177\":true}}";

    return baseMetaJson;
  }

  /** ===========================================================================================
   * overridding toString method of this object class
   * @return  string
   */
  public String toString() {
    StringBuilder castRequest = new StringBuilder();
    castRequest.append("[ ");
    castRequest.append("ContentProvider = " + CONTENT_PROVIDER + ", ");
    castRequest.append("CreativeType = " + CREATIVE_TYPE + ", ");
    castRequest.append("SlotId = " + SLOT_ID + ", ");
    castRequest.append("LandingURL = " + LANDING_URL + ", ");
    castRequest.append("Ad Tag = " + AD_TAG + ", ");
    castRequest.append("OS = " + OS + ", ");
    castRequest.append("TargetSDK = " + TARGET_SDK + ", ");

    if (CLICK_TRACKER_URL == null || CLICK_TRACKER_URL.isEmpty()) {
      castRequest.append("ClickTrackerUrl = NULL, ");
    } else {
      castRequest.append("ClickTrackerUrl = " + CLICK_TRACKER_URL + ", ");
    }
    if (BANNER_URL == null || BANNER_URL.isEmpty()) {
      castRequest.append("BannerURL = NULL , ");
    } else {
      castRequest.append("BannerURL = " + BANNER_URL + ", ");
    }

    castRequest.append("BannerAltText = NULL(Not Enabled)" + ", ");
    castRequest.append("MetaJson = " + META_JSON + ", ");
    castRequest.append("Ad Template Id = " + AD_TEMPLATE_ID + ", ");
    castRequest.append("Ad Template Content = " + AD_TEMPLATE_CONTENT + ", ");
    castRequest.append("Vast Url = " + VAST_URL + ", ");
    castRequest.append("Is Rewarded = " + IS_REWARDED + ", ");
    castRequest.append(" ]");

    return castRequest.toString();
  }


  /** All getters for easy access of parameters */
  public String getContentProvider() {
    return this.CONTENT_PROVIDER;
  }

  public String getCreativeType() {
    return this.CREATIVE_TYPE;
  }

  public int getSlotId() {
    return this.SLOT_ID;
  }

  public String getLandingUrl() {
    return this.LANDING_URL;
  }

  public String getAdTag() {
    return this.AD_TAG;
  }

  public String getOs() {
    return this.OS;
  }

  public int getTargetSdk() {
    return this.TARGET_SDK;
  }

  public String getClickTrackerUrl() {
    return this.CLICK_TRACKER_URL;
  }

  public String getBannerUrl() {
    return this.BANNER_URL;
  }

  public String getBannerAltText() {
    return this.BANNER_ALT_TEXT;
  }

  public boolean getIsVideoRewarded() {
    return this.IS_REWARDED;
  }

  public long getAdTemplateId() {
    return this.AD_TEMPLATE_ID;
  }

  public String getMetaJson() {
    return this.META_JSON;
  }

  public String getAdTemplateContent() {
    return this.AD_TEMPLATE_CONTENT;
  }

  public String getVastUrl() {
    return this.VAST_URL;
  }

}
