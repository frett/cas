<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<!-- $Id$ -->
	
		<!-- DOCUMENT TITLE: CHANGE TO NEW TITLE -->
		<title>JA-SIG  Central  Authentication  Service (CAS)</title>
		<meta http-equiv="content-type" content="text/html; charset=iso-8859-1" />
	
		<!-- KEYWORDS AND DESCRIPTIONS GO INTO THIS SECTION -->
	
		<meta name="keywords" content="Central Authentication Service,JA-SIG,CAS" />
	
		<!-- THIS CODE PROVIDES THE FORMATTING FOR THE TEXT - PLEASE LEAVE INTACT -->
		<link rel="stylesheet" href="<spring:theme code="css" />" type="text/css" media="all" />
		<script src="js/common.js" type="text/javascript"></script>
	</head>


  <body onload="init();">
	<!-- HEADER -->
	<div id="header">
		<a id="top">Java Architecture Special Interest Group</a>
		<h1>JA-SIG Central  Authentication  Service</h1>
	</div>
	<!-- END HEADER -->

	<!-- CONTENT -->
	<!-- $Id$ -->
	<div id="content">

		<div class="dataset clear" style="position: relative;">
			<h2 style="margin-bottom:0;">Log In Successful</h2>

			<p style="margin-top:-.5em;border:1px solid #ccc;background-color:#ffc;color:#000;padding:5px;">
				You have successfully logged into the Central Authentication Service.<br /><br />
			  For security reasons, please Log Out and Exit your web browser when you are done accessing services that require authentication!
			</p>
		</div>
	</div><!-- END CONTENT -->

	<!-- FOOTER -->
   	<div id="footer">
		<hr />
		<p style="margin-top:1em;">
			Copyright &copy; 2004 JA-SIG.  All rights reserved.
		</p>
	</div>
	<!-- END FOOTER -->

</body>
</html>

