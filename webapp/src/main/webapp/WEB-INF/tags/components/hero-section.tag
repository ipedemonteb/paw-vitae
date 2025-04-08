<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ attribute name="title" required="true" type="java.lang.String" %>
<%@ attribute name="description" required="true" type="java.lang.String" %>
<%@ attribute name="buttonText" required="true" type="java.lang.String" %>
<%@ attribute name="buttonUrl" required="true" type="java.lang.String" %>

<section class="hero-section">
  <div class="container">
    <div class="hero-content">
      <h1 class="hero-title"><spring:message code="${fn:escapeXml(title)}" /></h1>
      <p class="hero-description"><spring:message code="${fn:escapeXml(description)}" /></p>
      <a href="<c:url value='${buttonUrl}' />" class="hero-button"><spring:message code="${fn:escapeXml(buttonText)}" /></a>
    </div>
  </div>
</section>