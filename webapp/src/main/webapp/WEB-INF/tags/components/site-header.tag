<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<header class="site-header">
  <div class="container">
    <div class="header-wrapper">
      <div class="logo-container">
        <a href="<c:url value='/' />" class="logo-link">
<%--          <img src="<c:url value='/images/logo.png' />" alt="Vitae Logo" class="logo-image">--%>
          <span class="site-name">Vitae</span>
        </a>
      </div>

      <button class="mobile-menu-toggle" id="mobile-menu-toggle">
        <span class="bar"></span>
        <span class="bar"></span>
        <span class="bar"></span>
      </button>

      <nav class="main-nav">
        <ul class="nav-list" id="nav-list">
          <li><a href="<c:url value='/' />" class="nav-link"><spring:message code="nav.home" /></a></li>
          <li><a href="<c:url value='/portal' />" class="nav-link"><spring:message code="nav.doctors" /></a></li>
          <li><a href="#" class="nav-link"><spring:message code="nav.about" /></a></li>
          <li><a href="#" class="nav-link"><spring:message code="nav.contact" /></a></li>
        </ul>
      </nav>

      <div class="auth-buttons">
        <a href="<c:url value='/portal' />" class="btn btn-login"><spring:message code="nav.login" /></a>
        <a href="<c:url value='/register' />" class="btn btn-register"><spring:message code="nav.register" /></a>
      </div>
    </div>
  </div>
</header>

<script>
  document.addEventListener('DOMContentLoaded', function() {
    const mobileMenuToggle = document.getElementById('mobile-menu-toggle');
    const navList = document.getElementById('nav-list');
    
    mobileMenuToggle.addEventListener('click', function() {
      mobileMenuToggle.classList.toggle('active');
      navList.classList.toggle('active');
    });
  });
</script>