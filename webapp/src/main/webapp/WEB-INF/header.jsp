<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<fmt:setLocale value="${pageContext.response.locale}" />
<fmt:setBundle basename="messages" var="msg" />

<link rel="stylesheet" href="<c:url value='/css/header.css' />" />

<style>
    /* Custom Modal Styles */
    .modal-overlay {
        display: none;
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background-color: rgba(0, 0, 0, 0.5);
        z-index: 1000;
        align-items: center;
        justify-content: center;
    }

    .modal-overlay.show {
        display: flex;
    }

    .modal-container {
        background-color: #fff;
        border-radius: 8px;
        box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
        width: 100%;
        max-width: 400px;
        padding: 0;
        animation: modalFadeIn 0.3s ease-out;
    }

    @keyframes modalFadeIn {
        from { opacity: 0; transform: translateY(-20px); }
        to { opacity: 1; transform: translateY(0); }
    }

    .modal-header {
        padding: 16px 24px;
        border-bottom: 1px solid #e9ecef;
        display: flex;
        align-items: center;
    }

    .modal-title {
        font-size: 1.25rem;
        font-weight: 600;
        color: #333;
        margin: 0;
    }

    .modal-icon {
        margin-right: 12px;
        width: 24px;
        height: 24px;
        background-color: #f8d7da;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .modal-icon svg {
        width: 16px;
        height: 16px;
        fill: #dc3545;
    }

    .modal-body {
        padding: 24px;
    }

    .modal-message {
        margin: 0 0 16px 0;
        color: #495057;
        line-height: 1.5;
    }

    .modal-footer {
        padding: 16px 24px;
        border-top: 1px solid #e9ecef;
        display: flex;
        justify-content: flex-end;
        gap: 12px;
    }

    .btn-modal {
        padding: 8px 16px;
        border-radius: 4px;
        font-weight: 500;
        cursor: pointer;
        transition: all 0.2s;
        border: none;
    }

    .btn-cancel {
        background-color: #f8f9fa;
        color: #495057;
    }

    .btn-cancel:hover {
        background-color: #e9ecef;
    }

    .btn-logout {
        background-color: #dc3545;
        color: white;
    }

    .btn-logout:hover {
        background-color: #c82333;
    }
</style>

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
                    <a href="<c:url value='/register-patient' />" class="btn btn-primary">
                        <spring:message code="header.button.access.2" />
                    </a>
                    <a href="<c:url value='/login' />" class="btn btn-primary">
                        <spring:message code="header.button.access" />
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
                                <a href="<c:url value='/doctor/dashboard' />" class="dropdown-item">
                                    <span class="icon doctor-icon"></span>
                                    <spring:message code="header.dropdown.doctorDashboard" />
                                </a>
                            </sec:authorize>

                            <div class="dropdown-divider"></div>
                            <a href="javascript:void(0);" class="dropdown-item" onclick="showLogoutModal();">
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

<!-- Custom Logout Confirmation Modal -->
<div id="logoutModal" class="modal-overlay">
    <div class="modal-container">
        <div class="modal-header">
            <div class="modal-icon">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
                    <path d="M12 22C6.477 22 2 17.523 2 12S6.477 2 12 2s10 4.477 10 10-4.477 10-10 10zm0-2a8 8 0 1 0 0-16 8 8 0 0 0 0 16zm0-9a1 1 0 0 1 1 1v4a1 1 0 0 1-2 0v-4a1 1 0 0 1 1-1zm0-4a1 1 0 1 1 0 2 1 1 0 0 1 0-2z"></path>
                </svg>
            </div>
            <h3 class="modal-title"><spring:message code="logout.confirmation.title" /></h3>
        </div>
        <div class="modal-body">
            <p class="modal-message"><spring:message code="logout.confirmation.message" /></p>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn-modal btn-cancel" onclick="hideLogoutModal();">
                <spring:message code="logout.confirmation.cancel"/>
            </button>
            <a href="<c:url value='/logout' />" class="btn-modal btn-logout">
                <spring:message code="logout.confirmation.confirm"  />
            </a>
        </div>
    </div>
</div>

<script>
    function showLogoutModal() {
        document.getElementById('logoutModal').classList.add('show');
        // Close dropdown when modal opens
        document.querySelector('.dropdown-menu').classList.remove('show');
    }

    function hideLogoutModal() {
        document.getElementById('logoutModal').classList.remove('show');
    }

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

        // Close modal when clicking outside
        const modal = document.getElementById('logoutModal');
        if (modal) {
            modal.addEventListener('click', function(e) {
                if (e.target === modal) {
                    hideLogoutModal();
                }
            });

            // Close modal on escape key
            document.addEventListener('keydown', function(e) {
                if (e.key === 'Escape' && modal.classList.contains('show')) {
                    hideLogoutModal();
                }
            });
        }
    });
</script>