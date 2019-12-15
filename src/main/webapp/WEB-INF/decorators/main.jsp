<!DOCTYPE html>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@taglib prefix="s" uri="/struts-tags" %>

<html lang="en">
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
		
		<title><decorator:title default="Page"/> - <s:text name="global.title.suffix"/></title>
		
		<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.11.2/css/all.min.css">
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css">
		<link href="https://stackpath.bootstrapcdn.com/bootswatch/4.4.1/materia/bootstrap.min.css" rel="stylesheet">
		<link href="<s:url value="/styles/main.css"/>" rel="stylesheet" type="text/css" media="all">
		
		<%-- Although technically a "blocking" resource, we still place it in
		the head because we'd have no capacity to place jQuery-based scripts
		in the bodies of pages --%>
		<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
		
		<decorator:head/>
	</head>
	
	<body itemscope itemtype="https://schema.org/WebApplication">
		<meta itemprop="browserRequirements" content="HTML5+CSS3" />
		<meta itemprop="applicationCategory" content="2 megapixel, photograph, hosting" />
		<meta itemprop="operatingSystem" content="Any GUI-enabled operating system with an HTML5 capable browser." />
		
		<decorator:body/>
		
		<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"></script>
		<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>
		<script src="<s:url value="/scripts/dialog.js"/>"></script>
		<script src="<s:url value="/scripts/app.js"/>"></script>
	</body>
</html>
