<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Corporate Social Network</title>
<!-- <link rel="stylesheet" href="resources/css/screen.css" type="text/css"
	media="screen, projection"></link> -->
<style>
body {
	margin: 20px;
}
.clearfix:after { 
   content: " ";
   display: block; 
   height: 0; 
   clear: both;
}
</style>
<script src="/csn/resources/jquery.min.js"></script>
</head>
<body>
	<div align="center">
		<!-- Header -->
		<tiles:insertAttribute name="header" />
		<!-- Menu Page -->
		<div>
			<tiles:insertAttribute name="menu" />
		</div>
		<!-- Body Page -->
		<div style="min-height:450px; padding: 20px;">
			<tiles:insertAttribute name="body" />
		</div>
		<!-- Footer Page -->
		<div>
		<tiles:insertAttribute name="footer" />
		</div>
	</div>
</body>
</html>
