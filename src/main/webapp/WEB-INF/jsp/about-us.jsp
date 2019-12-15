<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<html>
	<head>
		<meta name="decorator" content="dialog"/>
	</head>
	
    <body>
		<content tag="modalID">about-us-modal</content>
		<content tag="header">About PhotoHosting</content>
		<content tag="main">
			<p>This is a simple proof-of-concept website showcasing some
				technologies for hosting fairly high-quality photographic
				images.</p>
			
			<div class="alert alert-danger" role="alert">
				Since the site is only intended as a proof-of-concept, the
				site's content is entirely unmonitored and unmoderated.
			</div>
			
			<p>We offer no mechanism for reporting copyright violations or
				otherwise offensive content.</p>
		</content>
    </body>
</html>
