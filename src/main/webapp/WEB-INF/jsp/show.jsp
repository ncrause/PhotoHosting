<%@taglib prefix="s" uri="/struts-tags" %>

<html>
	<head>
		<title><s:text name="show.title"/></title>
		<meta name="decorator" content="publicNav"/>
		
		<s:head />
		<meta property="og:image" content="<s:url action="preview/og/%{id}" forceAddSchemeHostAndPort="true"/>">
		<link rel="image_src" href="<s:url action="preview/reddit/%{id}" forceAddSchemeHostAndPort="true"/>">
		<meta name="twitter:image" content="<s:url action="preview/twitter/%{id}" forceAddSchemeHostAndPort="true"/>">
	</head>
	
	<body>
		<div class="container-fluid mt-4">
			<div class="row">
				<div class="col-md-8">
					<figure itemscope itemtype="https://schema.org/Photograph">
						<picture>
							<img src="<s:url action="show/%{id}/%{nonce}"/>" alt="<s:property value="filename"/>" class="img-fluid" oncontextmenu="return false" ondrag="return false" ondragstart="return false">
						</picture>
						
						<meta itemProp="encodingFormat" content="image/jpeg">
						
						<figcaption>
							<s:if test="%{photo.authorURL != null && photo.authorName != null}">
								<div itemprop="author" itemscope itemtype="http://schema.org/Person">
									Author: <a href="<s:property value="photo.authorURL"/>" target="_blank" itemprop="url">
										<span itemprop="name"><s:property value="photo.authorName"/></span>
									</a>
								</div>
							</s:if>
							
							<s:if test="%{photo.sourceURL != null && photo.sourceName != null}">
								<div itemprop="publisher" itemscope itemtype="https://schema.org/Organization">
									Publisher: <a href="<s:property value="photo.sourceURL"/>" target="_blank" itemprop="url">
										<span itemprop="name"><s:property value="photo.sourceName"/></span>
									</a>
								</div>
							</s:if>
						</figcaption>
					</figure>
				</div>

				<div class="col-md-4">
					<h3 class="py-4 border text-center">
						Ad Space Here
					</h3>
				</div>
			</div>
		</div>
	</body>
</html>