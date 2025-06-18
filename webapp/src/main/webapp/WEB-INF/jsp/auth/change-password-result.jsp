<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="icon" type="image/png" href="https://upload.wikimedia.org/wikipedia/commons/thumb/a/aa/V-logo.svg/2048px-V-logo.svg.png" />
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
     <link rel="stylesheet" href="<c:url value='/css/change-password-result.css' />" />

    <title>
        <c:choose>
            <c:when test="${param.success == 'true'}">
                <spring:message code="change.password.result.success.title" />
            </c:when>
            <c:otherwise>
                <spring:message code="change.password.result.error.title" />
            </c:otherwise>
        </c:choose>
    </title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/components/header.jsp" />

<div class="main-content">
    <div class="container">
        <div class="result-container">
            <div class="card">
                <div class="card-header ${param.success == 'true' ? 'success-header' : 'error-header'}">
                    <c:choose>
                        <c:when test="${param.success == 'true'}">
                            <h1><spring:message code="change.password.result.success.header" /></h1>
                        </c:when>
                        <c:otherwise>
                            <h1><spring:message code="change.password.result.error.header" /></h1>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${param.success == 'true'}">
                            <div class="result-icon success-icon">
                                <i class="fas fa-check-circle"></i>
                            </div>
                            <h2 class="result-title"><spring:message code="change.password.result.success.title" /></h2>
                            <p class="result-message"><spring:message code="change.password.result.success.message" /></p>
                            <a href="${pageContext.request.contextPath}/login" class="btn btn-success">
                                <i class="fas fa-sign-in-alt"></i>
                                <spring:message code="change.password.result.success.button" />
                            </a>
                        </c:when>
                        <c:otherwise>
                            <div class="result-icon error-icon">
                                <i class="fas fa-exclamation-circle"></i>
                            </div>
                            <h2 class="result-title"><spring:message code="change.password.result.error.title" /></h2>
                            <p class="result-message"><spring:message code="change.password.result.error.message" /></p>
                            <a href="${pageContext.request.contextPath}/recover-password" class="btn btn-danger">
                                <i class="fas fa-redo"></i>
                                <spring:message code="change.password.result.error.button" />
                            </a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>