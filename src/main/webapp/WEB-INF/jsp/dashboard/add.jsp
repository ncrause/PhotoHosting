<%-- 
    Document   : add
    Created on : 10-Apr-2020, 8:56:26 PM
    Author     : Nathan Crause <nathan@crause.name>
--%>
<%@taglib prefix="s" uri="/struts-tags" %>

<html>
	<head>
		<title><s:text name="dashboard.add.title"/></title>
		<meta name="decorator" content="dashboard"/>
		
		<s:head />
	</head>
	
    <body>
		<div class="container-fluid mt-4">
			<nav aria-label="breadcrumb">
				<ol class="breadcrumb">
					<li class="breadcrumb-item"><s:a action="index">Dashboard</s:a></li>
					<li class="breadcrumb-item active">Add Photo</li>
				</ol>
			</nav>
			
			<div class="row justify-content-center">
				<div class="col-md-6">
					<s:form action="upload" enctype="multipart/form-data" method="post" theme="bootstrap" class="card mb-4">
						<h5 class="card-header">Upload A Photo</h5>
						
						<s:actionerror theme="bootstrap" />
						<s:fielderror theme="bootstrap" />
								
						<div class="card-body">
							<p class="card-text"><s:text name="dashboard.upload.message"/></p>

							<s:file name="photoSource" key="dashboard.upload.file.label" class="form-control"/>
							<%-- <div class="custom-file">
								<input name="photoSource" type="file" class="custom-file-input" id="photoSource">
								<label class="custom-file-label" for="photoSource"><s:text name="dashboard.upload.file.label"/></label>
							</div> --%>
							
							<p>
								You may optionally credit this photo to a 
								particular photographer/author.
							</p>
							
							<div class="form-row">
								<div class="form-group col-md-6">
									<label class="control-label">Author's Name</label>
									<input type="text" name="authorName" class="form-control" value="<s:property value="authorName"/>"/>
									<small class="form-text text-muted">
										Optional - if the URL is supplied, but no name, the name is default to the URL's host.
									</small>
									<s:fielderror theme="bootstrap" fieldName="authorName" />
								</div>
								
								<div class="form-group col-md-6">
									<label class="control-label">Author's URL</label>
									<input type="url" name="authorURL" class="form-control" value="<s:property value="authorURL"/>"/>
									<small class="form-text text-muted">
										Optional.
									</small>
									<s:fielderror theme="bootstrap" fieldName="authorURL" />
								</div>
							</div>
									
							<p>
								You may optionally credit this photo as being
								sourced from a third party website.
							</p>
							
							<div class="form-row">
								<div class="form-group col-md-6">
									<label class="control-label">Source's Name</label>
									<input type="text" name="sourceName" class="form-control" value="<s:property value="sourceName"/>"/>
									<small class="form-text text-muted">
										Optional - if the URL is supplied, but no name, the name is default to the URL's host.
									</small>
									<s:fielderror theme="bootstrap" fieldName="sourceName" />
								</div>
								
								<div class="form-group col-md-6">
									<label class="control-label">Source's URL</label>
									<input type="url" name="sourceURL" class="form-control" value="<s:property value="sourceURL"/>"/>
									<small class="form-text text-muted">
										Optional.
									</small>
									<s:fielderror theme="bootstrap" fieldName="sourceURL" />
								</div>
							</div>
						</div>
						<div class="card-footer text-center">
							<s:submit key="dashboard.upload.submit.button" cssClass="btn btn-primary"/>
						</div>
					</s:form>
				</div>
			</div>
		</div>
	</body>
</html>