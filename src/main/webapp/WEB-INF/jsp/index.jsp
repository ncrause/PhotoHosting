<%@taglib prefix="s" uri="/struts-tags" %>

<html>
	<head>
		<title><s:text name="index.title"/></title>
		<meta name="decorator" content="slideshow"/>
		
		<s:head />
	</head>
	
	<body>
		<%-- <s:form action="helloWorld" theme="bootstrap">
			<s:textfield label="What is your name?" name="name" />
			<s:textfield label="What is the date?" name="dateNow" />
			<s:submit cssClass="btn btn-primary" />
		</s:form> --%>
		
		<div class="container">
			<div class="row min-vh-100 align-items-center justify-content-center">
				<div class="col-md-5">
					<main class="card">
						<h4 class="card-header">
							<s:text name="index.welcome.message">
								<s:param><s:url action="about-us"/></s:param>
							</s:text>
						</h4>
						
						<div class="card-body">
							<s:if test="hasActionMessages()">
								<s:actionmessage theme="bootstrap" />
							</s:if>
							<s:if test="hasActionErrors()">
								<s:actionerror theme="bootstrap" />
							</s:if>
							<div class="row">
								<div class="col-md-6">
									<p><s:text name="index.login.message"/></p>

									<s:form action="login" theme="bootstrap">
										<s:textfield key="global.login.email.label" name="emailAddress"/>
										<s:password key="global.login.password.label" name="password"/>

										<s:submit cssClass="btn btn-primary" key="global.login.label"/>
									</s:form>
								</div>
									
								<div class="col-md-6">
									<p><s:text name="index.signup.message"/></p>

									<s:form action="signup" theme="bootstrap">
										<s:textfield key="global.login.email.label" name="emailAddress" type="email"/>

										<s:submit cssClass="btn btn-secondary" key="index.signup.label" />
									</s:form>
								</div>
							</div>
						</div>
					</main>
				</div>
			</div>
		</div>
	</body>
</html>
	