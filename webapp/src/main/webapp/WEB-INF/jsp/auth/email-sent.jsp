<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="icon" type="image/png"
          href="https://upload.wikimedia.org/wikipedia/commons/thumb/a/aa/V-logo.svg/2048px-V-logo.svg.png"/>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="verification.email.sent.title"/></title>
    <link rel="stylesheet" href="<c:url value='/css/register.css'/> ">
    <link rel="stylesheet" href="<c:url value='/css/email-sent.css'/> ">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/components/header.jsp"/>

<div class="register-container">
    <div class="register-card">
        <div class="card-header">
            <h1 class="card-title-register"><spring:message code="verification.email.sent.title"/></h1>
            <p class="card-subtitle-register"><spring:message code="verification.email.sent.subtitle"/></p>
        </div>

        <div class="card-body">
            <div class="email-sent-container">
                <div class="email-icon">
                    <i class="fas fa-envelope-open-text"></i>
                </div>
                <h2 class="email-sent-title"><spring:message code="verification.email.sent.title"/></h2>
                <p class="email-sent-message"><spring:message code="verification.email.sent.message"/></p>

                <a href="${pageContext.request.contextPath}/" class="back-to-home">
                    <i class="fas fa-home"></i>
                    <spring:message code="verification.back.to.home"/>
                </a>
            </div>
        </div>
    </div>
</div>

</body>
</html>
