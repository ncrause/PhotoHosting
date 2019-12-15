<%-- 
    Document   : index
    Created on : 9-Apr-2020, 8:44:32 PM
    Author     : Nathan Crause <nathan@crause.name>
--%>
<%@taglib prefix="s" uri="/struts-tags" %>

<html>
	<head>
		<title><s:text name="dashboard.index.title"><s:param value="#session.authentication"/></s:text></title>
		<meta name="decorator" content="dashboard"/>
		
		<s:head />
	</head>
	
    <body>
		<div class="container-fluid mt-4">
			<div class="btn-toolbar mb-4" role="toolbar">
				<s:a action="add" cssClass="btn btn-success">Add</s:a>
			</div>
			
			<div class="row">
				<s:iterator value="userPhotos" var="photo">
					<figure class="col-sm-6 col-md-4 col-lg-3 mb-4">
						<img src="<s:url action="preview/og/%{#photo.id}" namespace="/"/>" class="img-fluid border">
						
						<figcaption>
							<dl class="row no-gutters mb-0">
								<dt class="col-3">Author:</dt>
								<dd class="col-9 mb-0"><a href="<s:property value="#photo.authorURL"/>" target="_blank"><s:property value="#photo.authorName"/></a></dd>
							</dl>
							
							<dl class="row no-gutters mb-0">
								<dt class="col-3">Source:</dt>
								<dd class="col-9 mb-0"><a href="<s:property value="#photo.sourceURL"/>" target="_blank"><s:property value="#photo.sourceName"/></a></dd>
							</dl>
							
							<s:a action="show/%{#photo.id}" namespace="/" target="_blank">Public Link</s:a>
						</figcaption>
					</figure>
				</s:iterator>
			</div>
		</div>
    </body>
</html>
