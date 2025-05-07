<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<link rel="stylesheet" href="<c:url value='/css/header.css'/> "/>
<link rel="stylesheet" href="<c:url value='/css/modal.css'/> "/>

<header class="main-header">
  <div class="container">
    <div class="logo">
      <a href="<c:url value='/' />">
        <h1><spring:message code="app.name" /></h1>
      </a>
    </div>

    <button class="mobile-menu-toggle" aria-label="<spring:message code="nav.toggle.menu" />">
      <span></span>
      <span></span>
      <span></span>
    </button>

    <nav class="main-nav">
      <ul class="nav-links">
        <li><a href="<c:url value='/' />" class="nav-link"><spring:message code="nav.home" /></a></li>
        <sec:authorize access="isAnonymous() or hasRole('ROLE_PATIENT')">
          <li><a href="<c:url value='/search' />" class="nav-link"><spring:message code="nav.doctors" /></a></li>
        </sec:authorize>
<%--        <li><a href="<c:url value='#' />" class="nav-link"><spring:message code="nav.services" /></a></li>--%>
        <li><a href="<c:url value='/about-us' />" class="nav-link"><spring:message code="nav.about" /></a></li>

        <!-- Mobile-only auth buttons -->
        <sec:authorize access="isAnonymous()">
          <li class="mobile-auth-item">
            <a href="<c:url value='/login' />" class="nav-link mobile-auth-link">
              <i class="fas fa-sign-in-alt"></i>
              <spring:message code="nav.login" />
            </a>
          </li>
          <li class="mobile-auth-item">
            <a href="<c:url value='/register-patient' />" class="nav-link mobile-auth-link">
              <i class="fas fa-user"></i>
              <spring:message code="nav.register.patient" />
            </a>
          </li>
          <li class="mobile-auth-item">
            <a href="<c:url value='/register' />" class="nav-link mobile-auth-link">
              <i class="fas fa-user-md"></i>
              <spring:message code="nav.register.doctor" />
            </a>
          </li>
        </sec:authorize>

        <sec:authorize access="isAuthenticated()">
          <li class="mobile-auth-item">
            <sec:authentication property="principal.username" var="username" />
            <span class="mobile-username">
              <span class="mobile-avatar">${username.charAt(0)}</span>
              ${username}
            </span>
          </li>
          <sec:authorize access="hasRole('ROLE_DOCTOR')">
            <li class="mobile-auth-item">
              <a href="<c:url value='/doctor/dashboard' />" class="nav-link mobile-auth-link">
                <i class="fas fa-stethoscope"></i>
                <spring:message code="header.dropdown.doctorDashboard" />
              </a>
            </li>
          </sec:authorize>
          <sec:authorize access="hasRole('ROLE_PATIENT')">
            <li class="mobile-auth-item">
              <a href="<c:url value='/patient/dashboard' />" class="nav-link mobile-auth-link">
                <i class="fas fa-tachometer-alt"></i>
                <spring:message code="header.dropdown.patientDashboard" />
              </a>
            </li>
          </sec:authorize>
          <li class="mobile-auth-item">
            <button class="mobile-logout-btn" type="button">
              <i class="fas fa-sign-out-alt"></i>
              <spring:message code="nav.logout" />
            </button>
          </li>
        </sec:authorize>
      </ul>
    </nav>

    <div class="auth-buttons desktop-only">
      <sec:authorize access="isAnonymous()">
        <a href="<c:url value='/login' />" class="btn btn-outline"><spring:message code="nav.login" /></a>
        <div class="register-dropdown">
          <button class="btn btn-primary register-dropdown-toggle" aria-expanded="false" aria-haspopup="true">
            <span><spring:message code="nav.register" /></span>
            <i class="fas fa-chevron-down"></i>
          </button>
          <div class="register-dropdown-menu">
            <a href="<c:url value='/register-patient' />" class="dropdown-item">
              <i class="fas fa-user"></i>
              <spring:message code="nav.register.patient" />
            </a>
            <a href="<c:url value='/register' />" class="dropdown-item">
              <i class="fas fa-user-md"></i>
              <spring:message code="nav.register.doctor" />
            </a>
          </div>
        </div>
      </sec:authorize>
      <sec:authorize access="isAuthenticated()">
        <div class="user-dropdown">
          <button class="user-dropdown-toggle" aria-expanded="false" aria-haspopup="true">

        <sec:authorize access="hasRole('ROLE_PATIENT')">
          <span class="user-avatar">
          <sec:authentication property="principal.username" var="username" />
          <span>${username.charAt(0)}</span>
            </span>
        </sec:authorize>
        <sec:authorize access="hasRole('ROLE_DOCTOR')">
          <img src="<c:url value='/image/${param.id}' />" alt="Doctor Avatar" class="doctor-avatar-small" />
        </sec:authorize>
            <span class="user-name">
        <sec:authentication property="principal.username" var="username" />
        ${username}
      </span>
            <i class="fas fa-chevron-down"></i>
          </button>
          <div class="user-dropdown-menu">
            <sec:authorize access="hasRole('ROLE_DOCTOR')">
              <a href="<c:url value='/doctor/dashboard' />" class="dropdown-item">
                <i class="fas fa-stethoscope"></i>
                <spring:message code="header.dropdown.doctorDashboard" />
              </a>
            </sec:authorize>
            <sec:authorize access="hasRole('ROLE_PATIENT')">
              <a href="<c:url value='/patient/dashboard' />" class="dropdown-item">
                <i class="fas fa-tachometer-alt"></i>
                <spring:message code="header.dropdown.patientDashboard" />
              </a>
            </sec:authorize>
            <div class="dropdown-divider"></div>
            <button class="dropdown-item logout-btn" type="button">
              <i class="fas fa-sign-out-alt"></i>
              <spring:message code="nav.logout" />
            </button>
          </div>
        </div>
      </sec:authorize>
    </div>
  </div>
</header>

<jsp:include page="/WEB-INF/jsp/components/modal.jsp">
  <jsp:param name="confirm" value="logout.confirm.yes"/>
  <jsp:param name="id" value="logoutModal"/>
  <jsp:param name="title" value="logout.confirm.title"/>
  <jsp:param name="message" value="logout.confirm.message"/>
  <jsp:param name="actionPath" value="/logout"/>
  <jsp:param name="divId" value=""/>
  <jsp:param name="formId" value=""/>
  <jsp:param name="buttonId" value=""/>
  <jsp:param name="buttonClass" value="logout-btn"/>
</jsp:include>


<script>
  document.addEventListener('DOMContentLoaded', function() {
    // Mobile menu toggle functionality
    const mobileMenuToggle = document.querySelector('.mobile-menu-toggle');
    const mainNav = document.querySelector('.main-nav');
    const body = document.body;

    if (mobileMenuToggle && mainNav) {
      mobileMenuToggle.addEventListener('click', function() {
        mainNav.classList.toggle('active');
        this.classList.toggle('active');
        document.body.classList.toggle('menu-open');

        // Close any open dropdowns when toggling mobile menu
        const dropdowns = document.querySelectorAll('.user-dropdown-menu, .register-dropdown-menu');
        dropdowns.forEach(dropdown => dropdown.classList.remove('show'));

        const toggles = document.querySelectorAll('.user-dropdown-toggle, .register-dropdown-toggle');
        toggles.forEach(toggle => toggle.setAttribute('aria-expanded', 'false'));
      });
    }

    // User dropdown functionality
    const setupDropdown = function(toggleSelector, menuSelector) {
      const toggle = document.querySelector(toggleSelector);
      const menu = document.querySelector(menuSelector);

      if (toggle && menu) {
        toggle.addEventListener('click', function(e) {
          e.stopPropagation();
          const expanded = this.getAttribute('aria-expanded') === 'true';

          // Close all other dropdowns first
          const allToggles = document.querySelectorAll('.user-dropdown-toggle, .register-dropdown-toggle');
          const allMenus = document.querySelectorAll('.user-dropdown-menu, .register-dropdown-menu');

          allToggles.forEach(t => {
            if (t !== this) t.setAttribute('aria-expanded', 'false');
          });

          allMenus.forEach(m => {
            if (m !== menu) m.classList.remove('show');
          });

          // Toggle current dropdown
          this.setAttribute('aria-expanded', !expanded);
          menu.classList.toggle('show');
        });
      }
    };

    // Setup both dropdowns
    setupDropdown('.user-dropdown-toggle', '.user-dropdown-menu');
    setupDropdown('.register-dropdown-toggle', '.register-dropdown-menu');

    // Close dropdowns when clicking outside
    document.addEventListener('click', function(e) {
      if (!e.target.closest('.user-dropdown') && !e.target.closest('.register-dropdown')) {
        const toggles = document.querySelectorAll('.user-dropdown-toggle, .register-dropdown-toggle');
        const menus = document.querySelectorAll('.user-dropdown-menu, .register-dropdown-menu');

        toggles.forEach(toggle => toggle.setAttribute('aria-expanded', 'false'));
        menus.forEach(menu => menu.classList.remove('show'));
      }
    });

    // Add active class to current nav link
    const currentPath = window.location.pathname;
    const context = '${pageContext.request.contextPath}';
    console.log(context);
    const navLinks = document.querySelectorAll('.nav-link');

    navLinks.forEach(link => {
      const href = link.getAttribute('href').split('?')[0]; // Remove query params for comparison
      if (currentPath === href || (href !== context + '/' && currentPath.includes(href))) {
        link.classList.add('active');
      }
    });

    // Handle window resize for responsive behavior
    const handleResize = function() {
      if (window.innerWidth > 768) {
        if (mainNav.classList.contains('active')) {
          mainNav.classList.remove('active');
          mobileMenuToggle.classList.remove('active');
          body.classList.remove('menu-open');
        }
      }
    };

    window.addEventListener('resize', handleResize);

    // Initial call to handle page load state
    handleResize();
  });
</script>
<style>
  .doctor-avatar-small {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    overflow: hidden;
    border: 3px solid var(--primary-light);
    box-shadow: var(--shadow-sm);
    flex-shrink: 0;
    transition: var(--transition);
  }
</style>