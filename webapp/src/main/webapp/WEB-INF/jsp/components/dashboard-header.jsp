<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!-- Patient Profile Header -->
<div class="dashboard-header">
  <div class="doctor-info">
    <div class="doctor-avatar">
      <img src="<c:url value="/image/${-1}"/>" alt="<c:out value="${patient.name} ${patient.lastName}"/>">
    </div>
    <div class="doctor-details">
      <h1 class="doctor-name"><c:out value="${patient.name}" /> <c:out value="${patient.lastName}" /></h1>
      <div class="doctor-meta">
        <div class="doctor-meta-item">
          <i class="fas fa-envelope"></i>
          <span><c:out value="${patient.email}" /></span>
        </div>
        <div class="doctor-meta-item">
          <i class="fas fa-phone"></i>
          <span><c:out value="${patient.phone}" /></span>
        </div>
      </div>
      <div class="doctor-specialties">
                <span class="specialty-tag">
                    <c:out value="${patient.coverage.name}" />
                </span>
      </div>
    </div>
  </div>
</div>

<!-- Dashboard Navigation Tabs -->
<div class="dashboard-nav">
  <a href="<c:url value='/patient/dashboard/upcoming'/>" class="nav-tab ${activeTab == 'upcoming' ? 'active' : ''}">
    <i class="fas fa-calendar-alt"></i>
    <span><spring:message code="dashboard.tab.upcoming" /></span>
  </a>
  <a href="<c:url value='/patient/dashboard/history'/>" class="nav-tab ${activeTab == 'history' ? 'active' : ''}">
    <i class="fas fa-history"></i>
    <span><spring:message code="dashboard.tab.history" /></span>
  </a>
  <a href="<c:url value='/patient/dashboard/profile'/>" class="nav-tab ${activeTab == 'profile' ? 'active' : ''}">
    <i class="fas fa-user"></i>
    <span><spring:message code="dashboard.tab.profile" /></span>
  </a>
</div>
