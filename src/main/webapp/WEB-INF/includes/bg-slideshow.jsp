<%-- 
    Document   : bg-slideshow
    Created on : 17-Dec-2019, 1:43:14 PM
    Author     : Nathan Crause <nathan@crause.name>
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div id="background-slideshow">
	<s:iterator value="%{slideshow}" var="entry" status="status">
		<figure class="background-slide" data-id="<s:property value="#entry.id" />" data-index="<s:property value="#status.index" />" style="background-image: url('<s:url action="show/%{#entry.id}/%{nonces[#entry.id]}"/>');<s:if test="%{#status.index == 0}"> display: block;</s:if>" itemscope itemtype="https://schema.org/Photograph">
			<link href="<s:url action="show/%{#entry.id}/%{nonces[#entry.id]}"/>" itemprop="url" />
			
			<span itemprop="author" itemscope itemtype="http://schema.org/Person">
				<meta content="<s:property value="#entry.authorName" />" itemprop="name">
				<link href="<s:property value="#entry.authorURL" />" itemprop="url">
			</span>
			
			<span itemprop="publisher" itemscope itemtype="https://schema.org/Organization">
				<meta content="<s:property value="#entry.sourceName" />" itemprop="name">
				<link href="<s:property value="#entry.sourceURL" />" itemprop="url">
			</span>
		</figure>
	</s:iterator>
</div>