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
			
			<s:if test="hasFlash('success')">
				<div class="alert alert-success">
					<s:property value="%{getFlash('success')}"/>
				</div>
			</s:if>
			
			<div class="row">
				<s:iterator value="userPhotos" var="photo">
					<figure class="col-sm-6 col-md-4 col-lg-3 mb-4">
						<img src="<s:url action="preview/og/%{#photo.id}" namespace="/"/>" class="img-fluid border">
						
						<figcaption>
							<s:if test="%{#photo.authorName != null && #photo.authorName != ''}">
							<dl class="row no-gutters mb-0">
								<dt class="col-3">Author:</dt>
								<dd class="col-9 mb-0">
									<s:if test="%{#photo.authorURL != null && #photo.authorURL != ''}">
									<a href="<s:property value="#photo.authorURL"/>" target="_blank">
									</s:if>
										<s:property value="#photo.authorName"/>
									<s:if test="%{#photo.authorURL != null && #photo.authorURL != ''}">
									</a>
									</s:if>
								</dd>
							</dl>
							</s:if>
							
							<s:if test="%{#photo.sourceName != null && #photo.sourceName != ''}">
							<dl class="row no-gutters mb-0">
								<dt class="col-3">Source:</dt>
								<dd class="col-9 mb-0">
									<s:if test="%{#photo.sourceURL != null && #photo.sourceURL != ''}">
									<a href="<s:property value="#photo.sourceURL"/>" target="_blank">
									</s:if>
										<s:property value="#photo.sourceName"/>
									<s:if test="%{#photo.sourceURL != null && #photo.sourceURL != ''}">
									</a>
									</s:if>
								</dd>
							</dl>
							</s:if>
							
							<s:a action="show/%{#photo.id}" namespace="/" target="_blank">Public Link</s:a>
						</figcaption>
					</figure>
				</s:iterator>
			</div>
		</div>
    </body>
</html>
