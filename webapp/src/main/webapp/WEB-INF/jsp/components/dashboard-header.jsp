<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="dashboard-header">
  <div class="doctor-info">
    <div class="doctor-avatar">
      <c:choose>
        <c:when test="${isDoctor == true && user.imageId != null}">
          <img src='<c:url value="/image/${user.imageId}"/>' onerror="this.src='/img/default_picture.png'" alt="${user.name}" class="doctor-avatar-small" />
        </c:when>
        <c:otherwise>
          <img src="/img/default_picture.png" alt="default"  class="doctor-avatar"/>
        </c:otherwise>
      </c:choose>

    </div>
    <div class="doctor-details">
      <h1 class="doctor-name"><c:out value="${user.name}" /> <c:out value="${user.lastName}" /></h1>
      <div class="doctor-meta">
        <div class="doctor-meta-item">
          <i class="fas fa-envelope"></i>
          <span><c:out value="${user.email}" /></span>
        </div>
        <div class="doctor-meta-item">
          <i class="fas fa-phone"></i>
          <span><c:out value="${user.phone}" /></span>
        </div>
      </div>
      <c:if test="${isDoctor}">
        <c:if test="${user.ratingCount > 0}">
          <div class="doctor-rating">
            <div class="rating-stars">
              <c:forEach begin="1" end="5" var="i">
                <c:choose>
                  <c:when test="${user.rating >= i}">
                    <i class="fas fa-star"></i>
                  </c:when>
                  <c:when test="${user.rating >= i - 0.5}">
                    <i class="fas fa-star-half-alt"></i>
                  </c:when>
                  <c:otherwise>
                    <i class="far fa-star"></i>
                  </c:otherwise>
                </c:choose>
              </c:forEach>
            </div>
            <div class="rating-value">
              <fmt:formatNumber value="${user.rating}" pattern="#.#" /> <span class="rating-count">(${user.ratingCount})</span>
            </div>
          </div>
        </c:if>
        <div class="doctor-specialties">
          <c:forEach items="${user.specialtyList}" var="specialty" varStatus="status">
                        <span class="specialty-tag">
                            <c:choose>
                              <c:when test="${not empty specialty.key}">
                                <spring:message code="${specialty.key}" />
                              </c:when>
                              <c:otherwise>
                                <spring:message code="${specialty.key}" />
                              </c:otherwise>
                            </c:choose>
                        </span>
          </c:forEach>
        </div>
      </c:if>
    </div>
  </div>
  <c:if test="${isDoctor}">
    <div class="dashboard-stats">
      <div class="stat-item">
        <div class="stat-value">${user.specialtyList.size()}</div>
        <div class="stat-label"><spring:message code="dashboard.stats.specialties" /></div>
      </div>
    </div>
  </c:if>
</div>

<!-- Dashboard Navigation Tabs -->
<div class="dashboard-nav">
  <a href="<c:url value='/${isDoctor ? "doctor" : "patient"}/dashboard/upcoming'/>" class="nav-tab ${activeTab == 'upcoming' ? 'active' : ''}">
    <i class="fas fa-calendar-alt"></i>
    <span><spring:message code="dashboard.tab.upcoming" /></span>
  </a>
  <a href="<c:url value='/${isDoctor ? "doctor" : "patient"}/dashboard/history'/>" class="nav-tab ${activeTab == 'history' ? 'active' : ''}">
    <i class="fas fa-history"></i>
    <span><spring:message code="dashboard.tab.history" /></span>
  </a>
  <c:if test="${!isDoctor}">
    <a href="<c:url value='/patient/dashboard/medical-history'/>" class="nav-tab ${activeTab == 'medicalHistory' ? 'active' : ''}">
      <i class="fas fa-notes-medical"></i>
      <span><spring:message code="dashboard.tab.medical.history" /></span>
    </a>
  </c:if>
  <c:if test="${isDoctor}">
    <a href="<c:url value='/doctor/dashboard/availability'/>" class="nav-tab ${activeTab == 'availability' ? 'active' : ''}">
      <i class="fas fa-calendar-check"></i>
      <span><spring:message code="dashboard.tab.availability" /></span>
    </a>
  </c:if>
  <c:if test="${isDoctor}">
    <a href="<c:url value='/doctor/dashboard/offices'/>" class="nav-tab ${activeTab == 'offices' ? 'active' : ''}">
      <i class="fas fa-clinic-medical"></i>
      <span><spring:message code="dashboard.tab.offices" /></span>
    </a>
  </c:if>
  <a href="<c:url value='/${isDoctor ? "doctor" : "patient"}/dashboard/profile'/>" class="nav-tab ${activeTab == 'profile' ? 'active' : ''}">
    <i class="fas fa-user"></i>
    <span><spring:message code="dashboard.tab.profile" /></span>
  </a>
</div>
