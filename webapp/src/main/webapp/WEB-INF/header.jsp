<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<fmt:setLocale value="es_ES"/>
<fmt:setBundle basename="messages" var="msg"/>

<link rel="stylesheet" href="<c:url value='/css/header.css' />">

<header class="site-header">
    <div class="container">
        <div class="header-wrapper">
            <!-- Logo and site name -->
            <div class="logo-container">
                <a href="<c:url value='/' />" class="logo-link">
<%--                    <img src="<c:url value='/images/logo.jpg' />" alt="Vitae Logo" class="logo-image">--%>
                    <span class="site-name"><spring:message code = "header.site.title"/></span>
                </a>
            </div>

            <!-- Navigation menu -->
            <nav class="main-nav">
                <button class="mobile-menu-toggle" aria-label="Toggle menu">
                    <span class="bar"></span>
                    <span class="bar"></span>
                    <span class="bar"></span>
                </button>
                <ul class="nav-list">
                    <li class="nav-item">
                        <a class="nav-link"><spring:message code = "header.nav.howItWorks"/></a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link"><spring:message code = "header.nav.specialties"/></a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link"><spring:message code = "header.nav.opinions"/></a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link"><spring:message code = "header.nav.help"/></a>
                    </li>
                </ul>
            </nav>

            <!-- Auth buttons -->
            <div class="auth-buttons">
                <a href="<c:url value='/login' />" class="btn btn-login"><spring:message code = "header.button.login"/></a>
                <a href="<c:url value='/register' />" class="btn btn-register"><spring:message code = "header.button.register"/></a>
            </div>
        </div>
    </div>
<%--</header>--%>

<%--<script>--%>
<%--    document.addEventListener('DOMContentLoaded', function() {--%>
<%--        const mobileMenuToggle = document.querySelector('.mobile-menu-toggle');--%>
<%--        const navList = document.querySelector('.nav-list');--%>

<%--        mobileMenuToggle.addEventListener('click', function() {--%>
<%--            navList.classList.toggle('active');--%>
<%--            mobileMenuToggle.classList.toggle('active');--%>
<%--        });--%>
<%--    });--%>
<%--</script>--%>

