<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vitae - Login</title>
    <link rel="stylesheet" href="<c:url value='/css/login.css' />">
    <link rel="stylesheet" href="<c:url value='/css/header.css' />">
</head>
<body>


<main class="main-content">
    <div class="logo-container">
        <a href="<c:url value='/' />" class="logo-link">
            <%--                    <img src="<c:url value='/images/logo.jpg' />" alt="Vitae Logo" class="logo-image">--%>
            <span class="site-name"><spring:message code = "header.site.title"/></span>
        </a>
    </div>
    <h1>Felicitaciones, apretaste login</h1>
</main>
</body>
</html>

