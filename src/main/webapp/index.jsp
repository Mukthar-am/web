<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
		 pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<title>Bob - The response builder</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link href="lib/bootstrap/css/bootstrap.css" rel="stylesheet">
	<link href="lib/bootstrap/css/bootstrap-responsive.css" rel="stylesheet">
	<link rel="shortcut icon" href="images/Bob.ico">


	<script type="text/javascript" src="http://code.jquery.com/jquery.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			$("input[type='button']").click(function(){
				var radioValue = $("input[name='toolRadios']:checked").val();
				if(radioValue){
					window.location.replace(radioValue);
					<!-- alert("Your are a - " + radioValue); -->
				}
			});

		});
	</script>

	<style type="text/css">
		#header{
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

<div class="container pull-left">
	<div class="row">
		<div class="span8">

			<!-- MUKS: this is the main code generating UI objects within the page -->
			<form class="form-horizontal" id="registerHere" method='post' action=''>
				<fieldset>
					<legend>Bob Server</legend>

					<div class="control-group">
						<label class="control-label">Tools</label>

						<div class="controls">
							<label class="radio">
								<input type="radio" name="toolRadios"
									   id="rmResponseServer"
									   value="bob.jsp">
								Ad Builder (Generate Ad Response from a Ad tag/assets)</label>

							<label class="radio">
								<input type="radio" name="toolRadios"
									   id="decryptServer"
									   value="decrypt-home.jsp">
								Ad Decrypt Utility (InMobi Ad decryption helper)</label>

							<div class="control-group">
								<label class="control-label"></label>
								<div class="controls">
									<p>
										<input type="button" class="btn btn-success"
											   rel="popover"
											   title="Tool launch trigger" data-container="body"
											   data-toggle="popover" value="Launch Tool" ></p>
								</div>
							</div>

						</div>
					</div>

				</fieldset>
			</form>

		</div>
	</div>
</div>
</body>
</html>
