<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="portal.title" /></title>
    <link rel="stylesheet" href="<c:url value='/css/portal.css' />">
</head>
<body>
<div class="container">
    <header class="header">
        <h1><spring:message code="portal.heading" /></h1>
    </header>

    <div class="search-section">
        <h2><spring:message code="portal.selectSpecialty" /></h2>

        <div class="specialty-grid">
            <c:forEach items="${specialties}" var="specialty">
                <a href="<c:url value='/search?specialty=${specialty}' />" class="specialty-card">
<%--                    <div class="specialty-icon">--%>
<%--                        <img src="<c:url value='/resources/images/specialty-icon.png' />" alt="${specialty}">--%>
<%--                    </div>--%>
                    <div class="specialty-name">
                        <spring:message code="specialty.${specialty}" />
                    </div>
                </a>
            </c:forEach>
        </div>
    </div>
</div>
</body>
</html>