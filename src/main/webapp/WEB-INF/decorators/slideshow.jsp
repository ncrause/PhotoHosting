<%-- 
    Document   : slideshow
    Created on : 17-Dec-2019, 12:09:31 PM
    Author     : Nathan Crause <nathan@crause.name>
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@taglib prefix="s" uri="/struts-tags" %>

<page:applyDecorator name="main">
	<html lang="en">
		<head>
			<title><decorator:title default="Slideshow"/></title>
			<decorator:head/>
			
			<link href="<s:url value="/styles/slideshow.css"/>" rel="stylesheet" type="text/css" media="all">
		</head>
		
		<body>
			<%@include file="/WEB-INF/includes/bg-slideshow.jsp" %>
			
			<main>
				<decorator:body />
			</main>
			
			<script src="<s:url value="/scripts/slideshow.js"/>"></script>
		</body>
	</html>
</page:applyDecorator>
