<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<fmt:setLocale value="${pageContext.response.locale}" />
<fmt:setBundle basename="messages" var="msg" />

<link rel="stylesheet" href="<c:url value='/css/header.css' />" />

<header class="site-header">
    <div class="container">
        <div class="header-wrapper">
            <!-- Logo and site name -->
            <div class="logo-container">
                <a href="<c:url value='/' />" class="logo-link">
                    <span class="site-name"><spring:message code="header.site.title" /></span>
                </a>
            </div>

            <!-- Navigation menu -->
<%--            <nav class="main-nav">--%>
<%--                <button class="mobile-menu-toggle" aria-label="<spring:message code="header.toggle.menu" />">--%>
<%--                    <span class="bar"></span>--%>
<%--                    <span class="bar"></span>--%>
<%--                    <span class="bar"></span>--%>
<%--                </button>--%>
<%--                <ul class="nav-list">--%>
<%--                    <li class="nav-item">--%>
<%--                        <a href="<c:url value='/how-it-works' />" class="nav-link"><spring:message code="header.nav.howItWorks" /></a>--%>
<%--                    </li>--%>
<%--                    <li class="nav-item">--%>
<%--                        <a href="<c:url value='/specialties' />" class="nav-link"><spring:message code="header.nav.specialties" /></a>--%>
<%--                    </li>--%>
<%--                    <li class="nav-item">--%>
<%--                        <a href="<c:url value='/opinions' />" class="nav-link"><spring:message code="header.nav.opinions" /></a>--%>
<%--                    </li>--%>
<%--                    <li class="nav-item">--%>
<%--                        <a href="<c:url value='/help' />" class="nav-link"><spring:message code="header.nav.help" /></a>--%>
<%--                    </li>--%>
<%--                </ul>--%>
<%--            </nav>--%>

            <!-- Auth section - changes based on authentication status -->
            <div class="auth-section">
                <!-- For non-authenticated users -->
                <sec:authorize access="!isAuthenticated()">
                    <a href="<c:url value='/register' />" class="btn btn-doctor">
                        <spring:message code="header.button.areYouDoctor" />
                    </a>
                    <a href="<c:url value='/login' />" class="btn btn-primary">
                        <spring:message code="header.button.access" />
                    </a>
                    <a href="<c:url value='/register-patient' />" class="btn btn-primary">
                        <spring:message code="header.button.access.2" />
                    </a>
                </sec:authorize>

                <!-- For authenticated users -->
                <sec:authorize access="isAuthenticated()">
                    <div class="user-dropdown">
                        <button class="dropdown-toggle-header">
                            <span class="user-icon"></span>
                            <span class="user-name">
                                <c:out value="${pageContext.request.userPrincipal.name}" />
                            </span>
                            <span class="arrow-down"></span>
                        </button>
                        <div class="dropdown-menu">
                            <a href="<c:url value='/' />" class="dropdown-item">
                                <span class="icon calendar-icon"></span>
                                <spring:message code="header.dropdown.appointments" />
                            </a>
                            <a href="<c:url value='/' />" class="dropdown-item">
                                <span class="icon history-icon"></span>
                                <spring:message code="header.dropdown.history" />
                            </a>
                            <a href="<c:url value='/' />" class="dropdown-item">
                                <span class="icon profile-icon"></span>
                                <spring:message code="header.dropdown.profile" />
                            </a>

                            <!-- Role-specific menu items -->
                            <sec:authorize access="hasRole('ROLE_DOCTOR')">
                                <div class="dropdown-divider"></div>
                                <a href="<c:url value='/' />" class="dropdown-item">
                                    <span class="icon doctor-icon"></span>
                                    <spring:message code="header.dropdown.doctorDashboard" />
                                </a>
                            </sec:authorize>

                            <div class="dropdown-divider"></div>
                            <a href="<c:url value='/' />" class="dropdown-item">
                                <span class="icon logout-icon"></span>
                                <spring:message code="header.dropdown.logout" />
                            </a>
                        </div>
                    </div>
                </sec:authorize>
            </div>
        </div>
    </div>
</header>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Mobile menu toggle
        const mobileMenuToggle = document.querySelector('.mobile-menu-toggle');
        const navList = document.querySelector('.nav-list');

        if (mobileMenuToggle && navList) {
            mobileMenuToggle.addEventListener('click', function() {
                navList.classList.toggle('active');
                mobileMenuToggle.classList.toggle('active');
            });
        }

        // User dropdown toggle
        const dropdownToggle = document.querySelector('.dropdown-toggle-header');
        const dropdownMenu = document.querySelector('.dropdown-menu');

        if (dropdownToggle && dropdownMenu) {
            dropdownToggle.addEventListener('click', function(e) {
                e.preventDefault();
                dropdownMenu.classList.toggle('show');
            });

            // Close dropdown when clicking outside
            document.addEventListener('click', function(e) {
                if (!dropdownToggle.contains(e.target) && !dropdownMenu.contains(e.target)) {
                    dropdownMenu.classList.remove('show');
                }
            });
        }
    });
</script>