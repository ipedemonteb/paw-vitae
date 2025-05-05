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
    <title><spring:message code="error.500.title" /> - Medical Appointments</title>
    <link rel="stylesheet" href="<c:url value='/css/400.css'/> "/>
</head>
<body>
<div class="error-container">
    <div class="error-icon">
        <svg xmlns="http://www.w3.org/2000/svg" width="100" height="100" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <circle cx="12" cy="12" r="10"></circle>
            <line x1="12" y1="8" x2="12" y2="12"></line>
            <line x1="12" y1="16" x2="12.01" y2="16"></line>
        </svg>
    </div>
    <div class="error-code"><spring:message code="error.500.code" /></div>
    <h1 class="error-title"><spring:message code="error.500.title" /></h1>
    <p class="error-message">
        <c:choose>
            <c:when test="${not empty message}">
                ${message}
            </c:when>
            <c:otherwise>
                <spring:message code="error.500.message" />
            </c:otherwise>
        </c:choose>
    </p>

    <div class="action-buttons">
        <a href="javascript:history.back();" class="btn">
            <spring:message code="common.try.again" />
        </a>
        <a href="${pageContext.request.contextPath}/" class="btn btn-outline">
            <spring:message code="common.return.home" />
        </a>
    </div>



<footer>
    <spring:message code="common.copyright" arguments="<%= new java.util.Date().getYear() + 1900 %>" />
</footer>
</body>
</html>
