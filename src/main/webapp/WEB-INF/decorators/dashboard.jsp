<%-- 
    Document   : publicNa
    Created on : 16-Jan-2020, 8:34:31 PM
    Author     : Nathan Crause <nathan@crause.name>
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@taglib prefix="page" uri="http://www.opensymphony.com/sitemesh/page" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<page:applyDecorator name="main">
	<html lang="en">
		<head>
			<title>Dashboard - <decorator:title default="Your Account Dashboard"/></title>
			<decorator:head/>
		</head>
		
		<body>
			<header class="sticky-top">
				<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
					<s:a action="index" cssClass="navbar-brand">PhotoHosting</s:a>
					<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#mainMenu" aria-controls="mainMenu" aria-expanded="false" aria-label="Toggle navigation">
						<span class="navbar-toggler-icon"></span>
					</button>
					
					<div class="collapse navbar-collapse" id="mainMenu">
						<ul class="navbar-nav mr-auto">
							<li class="nav-item">
								<s:a action="index" cssClass="nav-link">Home</s:a>
							</li>
						</ul>
							
						<span class="navbar-text">
							<s:property value="#session.authentication" />&ensp;
							<s:a action="logout" title="Logout"><i class="fas fa-sign-out-alt"></i></s:a>
						</span>
					</div>
				</nav>
			</header>
			
			<main>
				<decorator:body />
			</main>
		</body>
	</html>
</page:applyDecorator>
