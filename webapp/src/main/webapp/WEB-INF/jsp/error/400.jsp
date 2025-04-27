<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isErrorPage="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="${pageContext.response.locale}">
<head>
  <link rel="icon" type="image/png" href="https://upload.wikimedia.org/wikipedia/commons/thumb/a/aa/V-logo.svg/2048px-V-logo.svg.png" />
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title><spring:message code="error.400.title" /> - Medical Appointments</title>
  <link rel="stylesheet" href="<c:url value='/css/400.css'/> "/>
</head>
<body>
<div class="error-container">
  <div class="error-icon">
    <svg xmlns="http://www.w3.org/2000/svg" width="100" height="100" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
      <circle cx="12" cy="12" r="10"></circle>
      <path d="M12 8v4"></path>
      <path d="M12 16h.01"></path>
    </svg>
  </div>
  <div class="error-code"><spring:message code="error.400.code" /></div>
  <h1 class="error-title"><spring:message code="error.400.title" /></h1>
  <p class="error-message">
    <spring:message code="error.400.message" />
  </p>

  <div class="action-buttons">
    <a href="javascript:history.back();" class="btn">
      <spring:message code="common.try.again" />
    </a>
    <a href="${pageContext.request.contextPath}/" class="btn btn-outline">
      <spring:message code="common.return.home" />
    </a>
  </div>

  <div class="help-sections">
    <div class="help-section">
      <h3><spring:message code="error.400.common.issues.title" /></h3>
      <ul>
        <li><spring:message code="error.400.common.issue.1" /></li>
        <li><spring:message code="error.400.common.issue.2" /></li>
        <li><spring:message code="error.400.common.issue.3" /></li>
        <li><spring:message code="error.400.common.issue.4" /></li>
      </ul>
    </div>
    <div class="help-section">
      <h3><spring:message code="error.400.what.to.do.title" /></h3>
      <ul>
        <li><spring:message code="error.400.what.to.do.1" /></li>
        <li><spring:message code="error.400.what.to.do.2" /></li>
        <li><spring:message code="error.400.what.to.do.3" /></li>
        <li><spring:message code="error.400.what.to.do.4" /></li>
      </ul>
    </div>
  </div>
</div>

<footer>
  <spring:message code="common.copyright" arguments="<%= new java.util.Date().getYear() + 1900 %>" />
</footer>
</body>
</html>