<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isErrorPage="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <link rel="icon" type="image/png" href="https://upload.wikimedia.org/wikipedia/commons/thumb/a/aa/V-logo.svg/2048px-V-logo.svg.png" />
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Access Denied - Medical Appointments</title>
  <link rel="stylesheet" href="<c:url value='/css/403.css'/> ">
</head>
<body>
<div class="error-container">
  <div class="error-icon">
    <svg xmlns="http://www.w3.org/2000/svg" width="100" height="100" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
      <rect x="3" y="11" width="18" height="11" rx="2" ry="2"></rect>
      <path d="M7 11V7a5 5 0 0 1 10 0v4"></path>
      <circle cx="12" cy="16" r="1"></circle>
    </svg>
  </div>
  <div class="error-code"><spring:message code="error.403.code" /></div>
  <h1 class="error-title"><spring:message code="error.403.title" /></h1>
  <p class="error-message">
    <spring:message code="error.403.message" />
  </p>

  <div class="action-buttons">
    <a href="${pageContext.request.contextPath}/" class="btn">
      <spring:message code="common.return.home" />
    </a>
  </div>

  <div class="suggestions">
    <h3><spring:message code="error.403.why.title" /></h3>
    <ul>
      <li><spring:message code="error.403.why.1" /></li>
      <li><spring:message code="error.403.why.2" /></li>
      <li><spring:message code="error.403.why.3" /></li>
      <li><spring:message code="error.403.why.4" /></li>
    </ul>
  </div>
</div>

<footer>
  <spring:message code="common.copyright" arguments="<%= new java.util.Date().getYear() + 1900 %>" />
</footer>
</body>
</html>