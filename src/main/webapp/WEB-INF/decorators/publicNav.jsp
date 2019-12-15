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
			<title><decorator:title default="Public"/></title>
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
							
						<s:form action="search" cssClass="">

						</s:form>
					</div>
				</nav>
			</header>
			
			<main>
				<decorator:body />
			</main>
			
			<footer class="container-fluid bg-light py-4 mt-4 border-top text-center">
				<div itemprop="author" itemscope itemtype="https://schema.org/Person">
					&copy; <%= java.time.Year.now().getValue() %> <a href="http://nathan.crause.name" target="_blank" itemprop="url"><span itemprop="givenName">Nathan</span> <span itemprop="familyname">Crause</span></a>
					<meta itemprop="email" content="nathan@crause.name" />
					
				</div>
				
				<div>
					<a href="https://github.com/ncrause/Photohosting" target="_blank">This software</a> is released under the <a href="https://www.gnu.org/licenses/gpl-3.0.html" target="_blank" itemprop="license">GPLv3 license</a>.
				</div>
					
				<div id="powered-by">
					<h5>Powered By</h5>

					<div class="row align-items-center justify-content-center">
						<div class="col-6 col-sm-3 col-md-2">
							<a href="https://struts.apache.org/" target="_blank" class="clean" title="Apache Struts 2 Framework" data-toggle="tooltip">
								<img src="<c:url context="/images" value="/struts.svg" />" class="w-100">
							</a>
						</div>
							
						<div class="col-6 col-sm-3 col-md-2">
							<a href="https://hbase.apache.org/" target="_blank" class="clean" title="Apache HBase" data-toggle="tooltip">
								<img src="<c:url context="/images" value="/hbase-1.svg" />" class="w-100">
							</a>
						</div>

						<div class="col-6 col-sm-3 col-md-2">
							<a href="http://tomcat.apache.org" target="_blank" class="clean" title="Apache Tomcat Java Servlet and HTTP Server" data-toggle="tooltip">
								<%--
								I've included "rounded-circle" below so the white
								background of the logo isn't so jarring.
								--%>
								<img src="<c:url context="/images" value="/pb-tomcat.jpg" />" class="w-100 rounded-circle">
							</a>
						</div>
					</div>
				</div>
			</footer>
		</body>
	</html>
</page:applyDecorator>
