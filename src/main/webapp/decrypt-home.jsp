
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>

  <title>Decrypt Tool</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link href="lib/bootstrap/css/bootstrap.css" rel="stylesheet">
  <link href="lib/bootstrap/css/bootstrap-responsive.css" rel="stylesheet">
  <link rel="shortcut icon" href="./images/Splasher.jpg">

  <style type="text/css">
    #header{
      height: 40px;
      margin-bottom: 20px;
      background: url(./images/logo.png) 20px center no-repeat #000;
      padding-left: 10px;
    }
  </style>

  <style type="text/css">
    span.InputText {padding:0 3px;border:#ccc 1px solid}
  </style>

</head>

<body>
<div id="header">
</div>
<!--   <form action="display" method="get">   -->
<form method="post" action="services/v1/secure/decrypt">
  <div class="container">
    <div class="row">

      <!-- MUKS: this is the main code generating UI objects within the page -->
      <form class="form-horizontal" id="registerHere" method='post' action=''>
        <legend>Decryption Helper</legend>
        <p>
          <span style="font-family:comic sans ms,cursive;"
                title="InMobi SDK version">Is SDK-5.x.x:&nbsp;&nbsp;&nbsp;&nbsp;</span><select
                name="isSdk5xx" id="isSdk5xx" onChange="myFunction()">
          <option value="true">True</option>
          <option selected="selected" value="false">False</option>
        </select></p>
        <p><span style="font-family:comic sans ms,cursive;" title="AES key">AES key:&nbsp;<textarea name="aes" id ="aes" style="margin: 2px;width:152px; height:30px;" required>abcdefghijklmnop</textarea></span>
        </p>

        <p><span style="font-family:comic sans ms,cursive;"title="IV key">IV key:&nbsp;<textarea name="iv" id ="iv" style="margin: 2px;width:152px; height: 30px;" required>abcdefghijklmnop</textarea></span>
        </p>

        <p><span style="font-family:comic sans ms,cursive;"title="Encrypted ad response">Encrypted Ad Response:&nbsp;<textarea name="encryptedtxt" id ="encryptedtxt" style="margin: 2px; width: 550px; height:150px;" required></textarea></span>
        </p>
        <p>
          <br/> <br/>


          <input type="submit" class="btn btn-success"
                 rel="popover"
                 title="Submit the query" data-container="body"
                 value="Decrypt">

      </form>

    </div>
  </div>
  </div>
</form>
</body>
</html>