
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>

    <title>Bob's RMCreative Serve Tool</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="lib/bootstrap/css/bootstrap.css" rel="stylesheet">
    <link href="lib/bootstrap/css/bootstrap-responsive.css" rel="stylesheet">
    <link rel="shortcut icon" href="./images/Bob.ico">

    <style type="text/css">
        #header{
            height: 40px;
            margin-bottom: 20px;
            background: url(./images/logo.png) 20px center no-repeat #000;
            padding-left: 10px;
        }
    </style>


    <script>
        function dynamicSlotId() {
            var cType = document.getElementById("creativeType").value;
            var sSize = document.getElementById("slotSizeId");
            if(cType == "interstitial") {
                document.getElementById("slotSizeId").options.length=0;

                var intOption1 = document.createElement("option");
                intOption1.text = "14 (320x480)";
                intOption1.value = "14";

                var intOption2 = document.createElement("option");
                intOption2.text = "16 (1024x768)";
                intOption2.value = "16";

                var intOption3 = document.createElement("option");
                intOption3.text = "17 (1280x800)";
                intOption3.value = "17";

                sSize.add(intOption1);
                sSize.add(intOption2);
                sSize.add(intOption3);

            } else if (cType == "expandable") {
                document.getElementById("slotSizeId").options.length=0;
                var expOption1 = document.createElement("option");
                expOption1.text = "9 (320x48)";
                expOption1.value = "9";

                var expOption2 = document.createElement("option");
                expOption2.text = "10 (300x250)";
                expOption2.value = "10";

                var expOption3 = document.createElement("option");
                expOption3.text = "11 (728x90)";
                expOption3.value = "11";

                var expOption4 = document.createElement("option");
                expOption4.text = "12 (468x60)";
                expOption4.value = "12";

                var expOption5 = document.createElement("option");
                expOption5.text = "13 (120x600)";
                expOption5.value = "13";

                var expOption6 = document.createElement("option");
                expOption6.text = "15 (320x50)";
                expOption6.value = "15";

                sSize.add(expOption6);
                sSize.add(expOption1);
                sSize.add(expOption2);
                sSize.add(expOption3);
                sSize.add(expOption4);
                sSize.add(expOption5);

            } else if (cType == "banner" || cType == "banner_tpbu") {
                document.getElementById("slotSizeId").options.length=0;
                var bannerOption1 = document.createElement("option");
                bannerOption1.text = "9 (320x48)";
                bannerOption1.value = "9";

                var bannerOption2 = document.createElement("option");
                bannerOption2.text = "10 (300x250)";
                bannerOption2.value = "10";

                var bannerOption3 = document.createElement("option");
                bannerOption3.text = "11 (728x90)";
                bannerOption3.value = "11";

                var bannerOption4 = document.createElement("option");
                bannerOption4.text = "12 (468x60)";
                bannerOption4.value = "12";

                var bannerOption5 = document.createElement("option");
                bannerOption5.text = "13 (120x600)";
                bannerOption5.value = "13";

                var bannerOption6 = document.createElement("option");
                bannerOption6.text = "15 (320x50)";
                bannerOption6.value = "15";


                var bannerOption7 = document.createElement("option");
                bannerOption7.text = "14 (320x480)";
                bannerOption7.value = "14";

                var bannerOption8 = document.createElement("option");
                bannerOption8.text = "16 (1024x768)";
                bannerOption8.value = "16";

                var bannerOption9 = document.createElement("option");
                bannerOption9.text = "17 (1280x800)";
                bannerOption9.value = "17";

                sSize.add(bannerOption6);
                sSize.add(bannerOption1);
                sSize.add(bannerOption2);
                sSize.add(bannerOption3);
                sSize.add(bannerOption4);
                sSize.add(bannerOption5);
                sSize.add(bannerOption7);
                sSize.add(bannerOption8);
                sSize.add(bannerOption9);
            }
        }

        function previewOnClick() {
            var previewContent = document.getElementById("adTag").value;
            //window.open(URL,name,specs,replace)
            if(previewContent.length < 1) {
                alert('Please enter the ad tag first!');
            } else {
                top.consoleRef=window.open('','previewConsole', 'width=350,height=500');
                top.consoleRef.document.writeln(previewContent);
                top.consoleRef.document.close();
            }
        }

    </script>

</head>



<body>
<div id="header">
</div>
<!--   <form action="display" method="get">   -->
<%--<form enctype='application/json' method="post" action="services/hello/asjson">--%>

<%-- for uploading files --%>
<%-- <form action="rest/test/upload" method="post" enctype="multipart/form-data"> --%>
<form action="services/v1/bob/rmcast" method="post" enctype="multipart/form-data">
    <div class="container">
        <div class="row">

            <!-- MUKS: this is the main code generating UI objects within the page -->
            <form class="form-horizontal" id="registerHere" method='post' action=''>
                <legend>Ad Builder</legend>
                <p>
                    <span style="font-family:helvetica;" title="Define the type of creative ie. Expandable, Interstitial">Type of Creative:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><select name="creativeType" id="creativeType" onChange="dynamicSlotId()">
                    <option value="banner">Static Banner/Interstitial</option>
                    <option value="banner_tpbu">Static Banner/Interstitial TPBU</option>
                    <option value="expandable">RM Rich Banner</option>
                    <option value="interstitial">RM Interstitial</option>
                    <option selected="selected" value="video_default">Video Ads (Use Default Values)</option>
                    <option value="video_userdefined">Video Ads (User Defined)</option>
                    <option value="video_vast_url_userdef">Vast Video Ads (User Defined Template ID)</option>
                    <option value="video_vast_url_default">Vast Video Ads (With defaults)</option>
                </select></p>

                <p>
                    <span id = "spanisrewarded"
                          style="font-family:helvetica;"title="Video type, rewarded or non-rewarded.">Is Rewarded:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <select name="isRewarded" id="isRewarded">
                        <option value="true">True</option>
                        <option selected="selected" value="false">False</option>
                    </select>
                        </span>
                </p>

                 <p>
                    <span id = "spandefaulttemplate"
                          style="font-family:helvetica;"title="Default template Id, rewarded or non-rewarded.">Template Id:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <select name="defaultTemplateId" id="defaultTemplateId">
                        <option value="7556614876763911359_nrw">7556614876763911359
                            (Non-Rewareded)</option>
                        <option selected="selected" value="8770247689732829490_rw">8770247689732829490
                            (Rewarded)</option>
                        <option value="3412864359958606495_rw">3412864359958606495 (Vast-Rewareded)</option>
                    </select>
                        </span>
                </p>

                <p>
            <span id = "spantemplateid"
                  style="font-family:helvetica;"title="Video Ad Template Id.">AdTemplate Id:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<textarea name="adTemplateId" id ="adTemplateId" style="margin: 2px;width:244px;
              height:35px;">0</textarea>
            </span>
                </p>

                <p>
                    <span id = "spancontentprovider"
                          style="font-family:helvetica;"title="Ci third party ad tag ie. Celtra, Flashtalking etc.">Content Provider:&nbsp;&nbsp;&nbsp;
                    <select name="contentProvider" id="contentProviderId">
                        <option value="sprout">Studio</option>
                        <option selected="selected" value="others">Others</option>
                    </select>
                        </span>
                </p>
                <p>
                    <span id = "spanos" style="font-family:helvetica;" title="Define the target Operating System">Operating System:&nbsp;</span>
                    <select name="os">
                        <option value="android">Android</option>
                        <option selected="selected" value="iOS">iOS</option>
                    </select>
                </p>

                <p>
          <span id = "spanslotid" style="font-family:helvetica;"title="Slot Id of the Sdk ad request">Slot size:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

                    <select name="slotId" id="slotSizeId">
                        <option value="9">9 (320x48)</option>
                        <option value="10">10 (300x250)</option>
                        <option value="11">11 (728x90)</option>
                        <option value="12">12 (468x60)</option>
                        <option value="13">13 (120x600)</option>
                        <option selected="selected" value="14">14 (320x480)</option>
                        <option value="15">15 (320x50)</option>
                        <option value="16">16 (1024x768)</option>
                        <option value="17">17 (1280x800)</option>
                    </select>
              </span>
                </p>
                <p>
                    <span
                            style="font-family:helvetica;"title="Define the target SDK version to obtain a xhtml/imai response for below/above SDK v.370 resp.">SDK Version/Integration Type:&nbsp;</span>
                    <select name="sdkVersion" id="sdkVersionId">
                        <option value="0">MobileWeb/JsAdCode</option>
                        <option value="365">Version 3.6.5</option>
                        <option value="370">Version 3.7.0</option>
                        <option value="400">Version 4.0.0</option>
                        <option value="410">Version 4.1.0</option>
                        <option value="411">Version 4.1.1</option>
                        <option value="430">Version 4.3.0</option>
                        <option value="440">Version 4.4.0</option>
                        <option value="441">Version 4.4.1</option>
                        <option value="442">Version 4.4.2</option>
                        <option value="443">Version 4.4.3</option>
                        <option value="450">Version 4.5.0</option>
                        <option value="451">Version 4.5.1</option>
                        <option value="452">Version 4.5.2</option>
                        <option value="453">Version 4.5.3</option>
                        <option value="454">Version 4.5.4</option>
                        <option value="456">Version 4.5.6</option>
                        <option value="500">Version 5.0.0</option>
                        <option value="502">Version 5.0.2</option>
                        <option selected="selected" value="510">Version 5.1.0</option>
                        <option value="511">Version 5.1.1</option>
                    </select>
                </p>

                <p>
            <span id = "spanvasturl"
                  style="font-family:helvetica;"title="Hosted vast video url.">Vast URL
                (For vast video ads):
              <textarea name="vastUrl" id ="vastUrl" style="margin: 2px;width:644px; height: 55px;"></textarea>
            </span>
                </p>

                <p>
            <span id = "spanbannerurl" style="font-family:helvetica;"title="Mention third party banner hosted banner url.">Banner URL (For TPBU):&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              <textarea name="bannerUrl" id ="bannerUrl" style="margin: 2px;width:644px; height: 55px;"></textarea>
            </span>
                </p>

                <p>
            <span id = "spanlandingurl" style="font-family:helvetica;"title="If not defined, www.inmobi.com will be the default value.">Landing URL (optional):&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              <textarea name="landingUrl" id ="landingUrl" style="margin: 2px;width:644px; height: 55px;"></textarea>
            </span>
                </p>

                <p>
            <span id = "spanclicktrackerurl"
                  style="font-family:helvetica;"title="Third party click tracking Url.">Click Beacon Url (optional):&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              <textarea name="clickTrackerUrl" id ="clickTrackerUrl" style="margin: 2px;width:644px;height: 55px;"></textarea>
            </span>
                </p>

                <p>
            <span id = "spanadtag" style="font-family:helvetica;"
                  title="Define the javascript ad tag of the creative">Ad Tag:&nbsp;&nbsp;&nbsp;&nbsp;
              <textarea name="adTag" id ="adTag" style="margin: 2px; width: 742px; height: 130px;"></textarea></span>
                </p>

                <p>
            <span id = "spanmetajson" style="font-family:helvetica;"
                  title="Video meta json string">Meta Json:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              <textarea name="metaJson" id ="metaJson" style="margin: 2px; width: 742px; height: 130px;"></textarea></span>
                </p>

                <p>
            <span id = "spantemplatecontent" style="font-family:helvetica;"
                  title="Ad Template Content">Ad Template Content:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              <textarea name="adTemplate" id ="adTemplate" style="margin: 2px; width: 742px; height: 130px;"></textarea></span>
                </p>

                <p>
                    <span id = "bannerImg" style="font-family:helvetica;"
                  title="Upload banner image file.">Banner upload :&nbsp;&nbsp;&nbsp;
                <input type="file" name="file"/>
                </span>
                </p>

                <p>
                    <br/>
                    <input type="submit" class="btn btn-success"
                           rel="popover"
                           title="Fetch Ad Response" data-container="body"
                           value="Fetch Ad">
            </form>

        </div>
    </div>
    </div>
</form>
<script>

    var showHideMap = {
        'video_vast_url_default'  : {
            'showList': ['spanlandingurl', 'spancontentprovider', 'spanvasturl', 'spandefaulttemplate'],
            'hideList': ['spanbannerurl', 'bannerImg', 'spanadtag', 'spanslotid', 'spanmetajson',
                'spantemplatecontent', 'spantemplateid', 'spanisrewarded']
        },
        'video_vast_url_userdef'  : {
            'showList': ['spanlandingurl', 'spancontentprovider', 'spantemplateid', 'spanisrewarded', 'spanvasturl'],
            'hideList': ['spanbannerurl', 'bannerImg', 'spanadtag', 'spanslotid', 'spandefaulttemplate',
                'spanmetajson', 'spantemplatecontent']
        },
        'video_userdefined'  : {
            'showList': ['spanmetajson', 'spanlandingurl', 'spancontentprovider', 'spantemplateid',
                         'spantemplatecontent', 'spanisrewarded'],
            'hideList': ['spanbannerurl', 'bannerImg', 'spanadtag', 'spanslotid', 'spandefaulttemplate', 'spanvasturl']
        },
        'video_default'  : {
            'showList': ['spanmetajson', 'spanlandingurl', 'spancontentprovider', 'spandefaulttemplate'],
            'hideList': ['spanbannerurl', 'bannerImg', 'spanadtag', 'spanslotid', 'spanvasturl',
                         'spanisrewarded', 'spantemplatecontent', 'spantemplateid']
        },
        'expandable'  : {
            'showList': ['spanadtag', 'spanlandingurl', 'spancontentprovider', 'spanslotid'],
            'hideList': ['spanbannerurl', 'bannerImg', 'spanmetajson', 'spanisrewarded', 'spantemplatecontent',
                'spantemplateid', 'spandefaulttemplate', 'spanvasturl',]
        },
        'interstitial' : {
            'showList': ['spanadtag', 'spanlandingurl', 'spancontentprovider', 'spanslotid'],
            'hideList': ['spanbannerurl', 'bannerImg', 'spanmetajson', 'spanisrewarded', 'spantemplatecontent',
                'spantemplateid', 'spanvasturl', 'spandefaulttemplate']
        },
        'banner': {
            'showList': ['bannerImg', 'spanslotid'],
            'hideList': ['spanadtag', 'spanbannerurl', 'spancontentprovider', 'spanmetajson', 'spanisrewarded',
                'spantemplatecontent', 'spantemplateid', 'spanvasturl', 'spandefaulttemplate']
        },
        'banner_tpbu': {
            'showList': ['spanbannerurl', 'spanlandingurl', 'spanslotid'],
            'hideList': ['spanadtag', 'bannerImg', 'spancontentprovider', 'spanmetajson', 'spanisrewarded',
                'spantemplatecontent', 'spantemplateid', 'spanvasturl', 'spandefaulttemplate']
        },

    };

    var dropDown = document.getElementById("creativeType");
    var selectedValue = dropDown.options[dropDown.selectedIndex].value;

    showHide(selectedValue);
    dropDown.addEventListener('change', function (event) {
        selectedValue = dropDown.options[dropDown.selectedIndex].value;
        showHide(selectedValue);
    }, false);


    function showHide (option) {
        var showList = showHideMap[option].showList;
        var hideList = showHideMap[option].hideList;
        var showListLength = showList.length;
        var hideListLength = hideList.length;
        var i=0;
        for (; i<showListLength; i++) {
            document.getElementById(showList[i]).style.display = 'block';
        }

        for (i=0; i<hideListLength; i++) {
            document.getElementById(hideList[i]).style.display = 'none';
        }
    }

    showHide(selectedValue);


</script>
</body>
</html>