<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>

    <title>Bob - To inmobi production Ads</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="lib/bootstrap/css/bootstrap.css" rel="stylesheet">
    <link href="lib/bootstrap/css/bootstrap-responsive.css" rel="stylesheet">
    <link rel="shortcut icon" href="./images/Bob.ico">

    <style type="text/css">
        #header {
            height: 40px;
            margin-bottom: 20px;
            background: url(./images/logo.png) 20px center no-repeat #000;
            padding-left: 10px;
        }
    </style>
</head>


<body>
<div id="header">
</div>
<!--   <form action="display" method="get">   -->
<%--<form enctype='application/json' method="post" action="services/hello/asjson">--%>

<form action="services/v1/prod/adsbyguid" method="get" enctype="multipart/form-data">
    <div class="container">
        <div class="row">

            <!-- MUKS: this is the main code generating UI objects within the page -->
            <form class="form-horizontal" id="registerHere" method='post' action=''>
                <legend>Ad Builder - By Creative GUID</legend>
                <p>
                <span id="spanCreativeGuid"
                      style="font-family:helvetica;" title="Ad Creative GUID.">Creative GUID:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                  <textarea name="creativeGuid" id="creativeGuid"
                            style="margin: 2px;width:360px; height: 28px;"></textarea>
                </span>
                </p>

                <p>
                    <span id="spanos" style="font-family:helvetica;" title="Define the target Operating System">Operating System:&nbsp;</span>
                    <select name="os">
                        <option value="android">Android</option>
                        <option selected="selected" value="iOS">iOS</option>
                    </select>
                </p>

                <p>
          <span id="spanslotid" style="font-family:helvetica;" title="Slot Id of the Sdk ad request">Slot size:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

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
                            style="font-family:helvetica;"
                            title="Define the target SDK version to obtain a xhtml/imai response for below/above SDK v.370 resp.">SDK Version:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
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


</body>
</html>