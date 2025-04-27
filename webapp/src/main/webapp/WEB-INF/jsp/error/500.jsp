<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <link rel="icon" type="image/png" href="https://upload.wikimedia.org/wikipedia/commons/thumb/a/aa/V-logo.svg/2048px-V-logo.svg.png" />
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Server Error - Medical Appointments</title>
  <link rel="stylesheet" href="<c:url value='/css/500.css'/> "/>
</head>
<body>
<div class="error-container">
  <div class="error-icon">
    <svg xmlns="http://www.w3.org/2000/svg" width="100" height="100" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
      <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"></path>
      <line x1="12" y1="9" x2="12" y2="13"></line>
      <line x1="12" y1="17" x2="12.01" y2="17"></line>
    </svg>
  </div>
  <div class="error-code"><spring:message code="error.500.code" /></div>
  <h1 class="error-title"><spring:message code="error.500.title" /></h1>
  <p class="error-message">
    <spring:message code="error.500.message" />
  </p>

  <div class="action-buttons">
    <a href="javascript:window.location.reload();" class="btn">
      <spring:message code="common.try.again" />
    </a>
    <a href="${pageContext.request.contextPath}/" class="btn btn-outline">
      <spring:message code="common.return.home" />
    </a>
  </div>

  <div class="error-details">
    <h3><spring:message code="error.500.technical.title" /></h3>
    <p>
      <spring:message code="error.500.technical.message" />
    </p>
    <div class="error-id">
      <spring:message code="error.500.error.id" arguments="<%= System.currentTimeMillis() %>" />
    </div>
    <div class="contact-support">
      <spring:message code="error.500.contact.message" />
      <br>
      <strong><spring:message code="error.500.support.email" /></strong>
      or call <strong><spring:message code="error.500.support.phone" /></strong>
    </div>
  </div>
</div>

<footer>
  <spring:message code="common.copyright" arguments="<%= new java.util.Date().getYear() + 1900 %>" />
</footer>
</body>
</html>