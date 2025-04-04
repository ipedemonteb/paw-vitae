<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ attribute name="title" required="true" type="java.lang.String" %>
<%@ attribute name="description" required="true" type="java.lang.String" %>
<%@ attribute name="buttonText" required="true" type="java.lang.String" %>
<%@ attribute name="buttonUrl" required="true" type="java.lang.String" %>

<section class="hero-section">
  <div class="container">
    <div class="hero-content">
      <h1 class="hero-title"><spring:message code="${title}" /></h1>
      <p class="hero-description"><spring:message code="${description}" /></p>
      <a href="<c:url value='${buttonUrl}' />" class="hero-button"><spring:message code="${buttonText}" /></a>
    </div>
  </div>
</section>