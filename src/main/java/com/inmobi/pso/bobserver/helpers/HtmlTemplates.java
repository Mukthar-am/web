package com.inmobi.pso.bobserver.helpers;

/*
 * Created by mukthar.ahmed on 12/1/15.
 * Description:
 *          Final response generate page for RMCreativeServe Tool.
 */

public class HtmlTemplates {

    public static String templateToRmcastResp(String hostedRespLink,
                                              String qrCodeFileLink,
                                              String qrCodeSimplifyApp,
                                              String adResponse) {

        // one can send null object as well in place of qrCodeSimplifyApp, handled below;
        String qrCodeForSimplifyApp;
        if (qrCodeSimplifyApp != null) {
            qrCodeForSimplifyApp = qrCodeSimplifyApp;
        } else {
            qrCodeForSimplifyApp = "QRCode for simplify apps, NOT found.";
        }


        StringBuilder htmlFormatted = new StringBuilder();
        htmlFormatted.append("<html>\n" +
                "   <head>\n" +
                "      <title>RMCreative Serve Tool</title>\n" +
                "      <meta charset=\"utf-8\">\n" +
                "      <meta charset=\"utf-8\">\n" +
                "      <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "      <link href=\"./../../../lib/bootstrap/css/bootstrap.css\" " +
                "rel=\"stylesheet\">\n" +
                "      <link href=\"./../../../lib/bootstrap/css/bootstrap-responsive.css\" " +
                "rel=\"stylesheet\">\n" +
                "      <link rel=\"shortcut icon\" href=\"./../../../images/InMobiLogo.jpg\">\n" +
                "      <style type=\"text/css\">\n" +
                "         #header{\n" +
                "         height: 40px;\n" +
                "         margin-bottom: 20px;\n" +
                "         background: url(./../../../images/logo.png) 20px center no-repeat #000;\n" +
                "         padding-left: 10px;\n" +
                "         }\n" +
                "         .main_block {\n" +
                "         width: 800px;\n" +
                "         border: 2px black solid;\n" +
                "         }\n" +
                "         .main_block: before, .main_block: after {\n" +
                "         overflow: hidden;\n" +
                "         content: \"\";\n" +
                "         display: table;\n" +
                "         }\n" +
                "         .main_block: after {\n" +
                "         clear: both;\n" +
                "         }\n" +
                "         .inner_block {\n" +
                "         display: inline-block;\n" +
                "         float: left;\n" +
                "         width: 50%;\n" +
                "         }\n" +
                "         .inner_block img {\n" +
                "         width: 100%;\n" +
                "         height: auto;\n" +
                "         vertical-align: middle;\n" +
                "         }\n" +
                "      </style>\n" +
                "   </head>\n" +
                "   <body>\n" +
                "      <div id=\"header\"></div>\n" +
                "      <div class=\"container\">\n" +
                "         <div class=\"row\">\n" +

                "                <H4>Ad response decrypted</H4>\n" +
                "               <input type=\"button\" value=\"Back - Bob Home\" " +
                "onclick=\"location" +
                ".href='../../../index.jsp';\" style=\"float: right;\">" +
                "               <input type=\"button\" value=\"Back - Ad Builder Home\" " +
                "onclick=\"location" +
                ".href='../../../bob.jsp';\" style=\"float: right;\"><br><br>" +
                "<textarea rows=\"\\&quot;30\\&quot;\" cols=\"\\&quot;120\\&quot;\" margin:=\"\" 0px;=\"\" width:=\"\" 800px;=\"\" height:=\"\" 1150px;=\"\" style=\"height: 130; width: 1200;\">" +
                "                \"" + adResponse + "\"\n" +
                "                </textarea><br />" +

                "            <h4><b>RM Creative Serve Tool:</b></h4>\n" +
                "            <b>Generated Ad reponse and is hosted </b><a href=\"" +
                hostedRespLink +
                "\"> here</a> (Right click -> \"Copy link address\")\n" +
                "            <b>or use the complete URL: </b>\n" +
                "            <font color=\"blue\">" +
                hostedRespLink +
                "\n" +
                "            <br><br>\n" +
                "            <div class=\"main_block\">\n" +
                "               <div class=\"inner_block\">\n" +
                "               <br><br>\n" +
                "                  <b><font color=\"black\">Scan link from below</font></b><br><img src=\"" +
                qrCodeFileLink +
                "\" alt=\"Mountain View\" style=\"width:200px;height:180px;\">\n" +
                "               </div>\n" +
                "               <div class=\"inner_block\">\n" +
                "               <br><br>\n" +
                "                  <b><font color=\"black\">Simplify App: Scan link from below</font><br><img src=\"" +
                qrCodeForSimplifyApp +
                "\" alt=\"Mountain View\" style=\"width:210px;height:180px;\">\n" +
                "               </div>\n" +
                "            </div>\n" +
                "            <br><br>\n" +
                "            <br><br>  \n" +
                "         </div>\n" +
                "      </div>\n" +
                "      </div>\n" +
                "      </form>\n" +
                "   </body>\n" +
                "</html>"
        );

        return htmlFormatted.toString();
    }


    // ####################################################################################
    /*
        Date: 18-Dec-2015
        Author: shiraz.ansari@inmobi.com / mukthar.ahmed@inmobi.com
        Description:
            This method consumes Api ad response string (raw),  generated out of bob-server via
            api and added to the body of html app template.

            return value - complete html app with ad embedded into it.

     */
    public static String MobileWebAppBuilder(String hostedJsAd, String width, String height) {
        StringBuilder mWebAppWithAd = new StringBuilder();

        mWebAppWithAd.append("<!DOCTYPE html>\n" +
                "<html>\n" +
                "   <head>\n" +
                "   <meta name=\"robots\" content=\"noindex\">\n" +
                "   <meta name=\"apple-mobile-web-app-status-bar-style\" content=\"black\">\n" +
                "   <meta name=\"format-detection\" content=\"telephone=no\">\n" +
                "   <meta name=\"viewport\" content=\"width = 640, initial-scale = 1.0, user-scalable = no\">" +
                "      <title>InMobi - Mobile Web Creative Testing</title>\n" +
                "      <link rel=\"shortcut icon\" href=\"./../images/InMobiLogo.jpg\">\n" +
                "   </head>\n" +
                "   <body bgcolor=\"#FFFFFF\" style=\"width:640px;font-family:Verdana,Tahoma,Arial;font-size:11\">\n" +
                "      <h4>InMobi - Webify App</h4>\n" +
                "      <div style=\"display: inline-block; " +
                "width: "+ width + "px; " +
                "height: " + height + "px;\">\n" +
                "         <iframe scrolling=\"no\" class=\"inmobi-ad\" " +
                "id=\"inmobi-iframe-UyVbQp\" style=\"border: none; overflow: hidden;" +
                "width: " + width + "px; " +
                "height: " + height + "px; " +
                "display: block; background-color: white;\" " +
                "src=\"" +
                hostedJsAd +
                "\">\n" +
                "         </iframe>\n" +
                "      </div>\n" +
                "<p>This page demonstrates how a creative would show in a <strong>web</strong> " +
                "environment.</p>\n" +
                "<p>The method of delivery is production-identical and is therefore a true " +
                "representation of how the creative would be delivered on a mobile web site by " +
                "the InMobi network.</p>\n" +
                "<p>If the creative is from a third party, it may accrue statistics with that " +
                "third party if shown on this page. However, it will accrue no statistics on the " +
                "InMobi network.</p>\n" +
                "<p>Please contact your InMobi account manager with any questions.</p>\n" +
                "   </body>\n" +
                "</html>");

        return mWebAppWithAd.toString();
    }


    // ##########################################################################################
    /*  Author: mukthar.ahmed@inmobi.com
        Date: 21-Dec'2015

        Description:
            This is for mobile web app ads.
            ad response built out of bob-server/api should be html wrapped for prod equivalent
     */
    public static StringBuilder MobileWebAdBuilder(String jsAdCode) {
        StringBuilder mobileWebAd = new StringBuilder();
        mobileWebAd.append("<html>\n" +
                "<head>" +
                "      <title></title>\n" +
                "      <style type=\"text/css\"> body {margin: 0; overflow: hidden; background-color: transparent}\n" +
                "      </style>\n" +
                "  <script>var tvt = tvt || {}; \n" +
                "  tvt.captureVariables = function (a){var b=new Date,c={},d;for(d in a||{})if(a.hasOwnProperty(d)&&\"undefined\"!=typeof a[d])try{var e=\n" +
                "[];e.forEach(function(a,b){e[b]=void 0});c[d]=JSON.stringify(a[d],function(a,b){try{if(\"function\"!==typeof b){if(\"object\"===typeof b&&null!==b){if(b instanceof HTMLElement||b instanceof Node||-1!=e.indexOf(b))return;e.push(b)}return b}}catch(c){}})}catch(f){}a=document.createEvent(\"CustomEvent\");a.initCustomEvent(\"TvtRetrievedVariablesEvent\",!0,!0,{variables:c,date:b});window.dispatchEvent(a)};window.setTimeout(function() {\n" +
                "\ttvt.captureVariables({'dataLayer': window['dataLayer']})}, 2000);\n" +
                "\t</script>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "\t" + jsAdCode +
                "</body>\n" +
                "\n" +
                "</html>");

        return mobileWebAd;
    }


    /*  ========================================================================================
        Html template for building uploaded banner image file conflict error
     */
    public static String BannerFileUploadConfict(String conflictingFileName) {

        StringBuilder errorHtmlCode = new StringBuilder();
        errorHtmlCode.append("<html>\n" +
                "   <head>\n" +
                "      <title>RMCreative Serve Tool</title>\n" +
                "      <meta charset=\"utf-8\">\n" +
                "      <meta charset=\"utf-8\">\n" +
                "      <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "      <link href=\"./../../../lib/bootstrap/css/bootstrap.css\" rel=\"stylesheet\">\n" +
                "      <link href=\"./../../../lib/bootstrap/css/bootstrap-responsive.css\" rel=\"stylesheet\">\n" +
                "      <link rel=\"shortcut icon\" href=\"./../../../images/InMobiLogo.jpg\">\n" +
                "      <style type=\"text/css\">\n" +
                "         #header{\n" +
                "         height: 40px;\n" +
                "         margin-bottom: 20px;\n" +
                "         background: url(./../../../images/logo.png) 20px center no-repeat #000;\n" +
                "         padding-left: 10px;\n" +
                "         }\n" +
                "         .main_block {\n" +
                "         width: 800px;\n" +
                "         border: 2px black solid;\n" +
                "         }\n" +
                "         .main_block: before, .main_block: after {\n" +
                "         overflow: hidden;\n" +
                "         content: \"\";\n" +
                "         display: table;\n" +
                "         }\n" +
                "         .main_block: after {\n" +
                "         clear: both;\n" +
                "         }\n" +
                "         .inner_block {\n" +
                "         display: inline-block;\n" +
                "         float: left;\n" +
                "         width: 50%;\n" +
                "         }\n" +
                "         .inner_block img {\n" +
                "         width: 100%;\n" +
                "         height: auto;\n" +
                "         vertical-align: middle;\n" +
                "         }\n" +
                "      </style>\n" +
                "   </head>\n" +
                "   <body>\n" +
                "      <div id=\"header\"></div>\n" +
                "      <div class=\"container\">\n" +
                "      <div class=\"row\">\n" +
                "      <input type=\"button\" value=\"Back - Bob Home\" onclick=\"location.href='../../../index.jsp';\" style=\"float: right;\">               <input type=\"button\" value=\"Back - Creative Serve Home\" onclick=\"location.href='../../../bob.jsp';\" style=\"float: right;\"><br><br>\n" +
                "      <font color=\"red\">Error! Uploaded banner image file name " +
                "\"" + conflictingFileName + "\" " +
                "conflicts with existing file name. <br>Resolution: Change the file name " +
                "and retry.</font> \n" +
                "   </body>\n" +
                "</html>\n");

        return errorHtmlCode.toString();
    }

}
