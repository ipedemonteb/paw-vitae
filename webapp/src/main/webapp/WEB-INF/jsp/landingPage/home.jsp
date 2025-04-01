<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vitae - Landing Page</title>
    <link rel="stylesheet" href="<c:url value='/css/header.css' />">
    <link rel="stylesheet" href="<c:url value='/css/landing.css' />">
</head>
<body>
<%@ include file="/WEB-INF/header.jsp" %>

<main class="main-content">
    <!-- Hero Section -->
    <section class="hero-section">
        <div class="container">
            <div class="hero-content">
                <h1 class="hero-title"><spring:message code = "landing.welcome"/></h1>
                <p class="hero-description"><spring:message code = "landing.welcome.description"/></p>
                <a href="<c:url value='/register' />" class="hero-button"><spring:message code = "startnow"/></a>
            </div>
        </div>
    </section>

    <!-- Features Section -->
    <section class="features-section">
        <div class="container">
            <h2 class="section-title"><spring:message code = "features.title"/></h2>
            <div class="features-grid">
                <div class="feature-item">
                    <h3><spring:message code = "features.header"/></h3>
                    <p><spring:message code = "features.description"/></p>
                </div>
                <div class="feature-item">
                    <h3><spring:message code = "features.header2"/></h3>
                    <p><spring:message code = "features.description2"/></p>
                </div>
                <div class="feature-item">
                    <h3><spring:message code = "features.header3"/></h3>
                    <p><spring:message code = "features.description3"/></p>
                </div>
            </div>
        </div>
    </section>
</main>

<footer class="site-footer">
    <div class="container">
        <p>&copy;<spring:message code = "copyright"/></p>
    </div>
</footer>
</body>
</html>

