<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>

<layout:page title="dashboard.doctor.title">
    <div class="dashboard-container">
        <!-- Doctor Profile Header -->
        <div class="dashboard-header">
            <div class="doctor-info">
                <div class="doctor-avatar">
                    <img src="<c:url value="/doctor/${patient.id}/image"/>" alt="<c:out value="${patient.name} ${patient.lastName}"/>'">
                </div>
                <div class="doctor-details">
                    <h1 class="doctor-name"><c:out value="${patient.name}" /> <c:out value="${patient.lastName}" /></h1>
                </div>
            </div>
            <div class="dashboard-actions">
                <div class="dropdown">
                    <button class="btn btn-outline dropdown-toggle">
                        <i class="icon-settings"></i>
                        <span class="sr-only"><spring:message code="dashboard.settings.title" /></span>
                    </button>
                </div>
            </div>
        </div>

        <!-- Dashboard Navigation Tabs -->
        <div class="dashboard-nav">
            <button class="nav-tab active" data-tab="upcoming">
                <i class="icon icon-calendar"></i>
                <span><spring:message code="dashboard.tab.upcoming" /></span>
            </button>
            <button class="nav-tab" data-tab="history">
                <i class="icon icon-history"></i>
                <span><spring:message code="dashboard.tab.history" /></span>
            </button>
            <button class="nav-tab" data-tab="profile">
                <i class="icon icon-user"></i>
                <span><spring:message code="dashboard.tab.profile" /></span>
            </button>
        </div>

        <!-- Dashboard Content Area -->
        <div class="dashboard-content">
            <!-- Upcoming Appointments Tab -->
            <div class="tab-content active" id="upcoming-tab">
                <div class="tab-header">
                    <h2><spring:message code="dashboard.upcoming.title" /></h2>
                    <div class="tab-actions">
                        <div class="date-filter">
                            <label for="date-range"><spring:message code="dashboard.filter.dateRange" />:</label>
                            <select id="date-range" class="filter-select">
                                <option value="today"><spring:message code="dashboard.filter.today" /></option>
                                <option value="week" selected><spring:message code="dashboard.filter.thisWeek" /></option>
                                <option value="month"><spring:message code="dashboard.filter.thisMonth" /></option>
                                <option value="all"><spring:message code="dashboard.filter.all" /></option>
                            </select>
                        </div>
                    </div>
                </div>

                <c:choose>
                    <c:when test="${not empty upcomingAppointments}">
                        <div class="appointments-list">
                            <c:forEach items="${upcomingAppointments}" var="appointment">
                                <div class="appointment-card">
                                    <div class="appointment-time">
                                        <div class="appointment-date">
                                            <span class="day"><fmt:formatDate value="${appointment.date}" pattern="EEE" /></span>
                                            <span class="date-number"><fmt:formatDate value="${appointment.date}" pattern="d" /></span>
                                            <span class="month"><fmt:formatDate value="${appointment.date}" pattern="MMM" /></span>
                                        </div>
                                        <div class="appointment-hour">
                                            <i class="icon icon-clock"></i>
                                            <fmt:formatDate value="${appointment.date}" pattern="HH:mm" />
                                        </div>
                                    </div>
                                    <div class="appointment-details">
                                        <div class="patient-info">
                                            <div class="patient-avatar">
                                                <c:choose>
                                                    <c:when test="${not empty appointment.patient.imageId}">
                                                        <img src="<c:url value='/images/${appointment.patient.imageId}' />" alt="${appointment.patient.name}" />
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="avatar-placeholder small">
                                                            <span>${appointment.patient.name.charAt(0)}</span>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                            <div class="patient-name">
                                                <c:out value="${appointment.patient.name}" /> <c:out value="${appointment.patient.lastName}" />
                                            </div>
                                        </div>
                                        <div class="appointment-type">
                                            <c:choose>
                                                <c:when test="${appointment.virtual}">
                                                    <span class="appointment-badge virtual">
                                                        <i class="icon icon-video"></i>
                                                        <spring:message code="appointment.type.virtual" />
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="appointment-badge in-person">
                                                        <i class="icon icon-building"></i>
                                                        <spring:message code="appointment.type.inPerson" />
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        <div class="appointment-actions">
                                            <c:if test="${appointment.virtual}">
                                                <a href="<c:url value='/appointments/${appointment.id}/join' />" class="btn btn-primary">
                                                    <i class="icon icon-video"></i>
                                                    <spring:message code="appointment.action.join" />
                                                </a>
                                            </c:if>
                                            <a href="<c:url value='/appointments/${appointment.id}' />" class="btn btn-secondary">
                                                <i class="icon icon-info"></i>
                                                <spring:message code="appointment.action.details" />
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="empty-state">
                            <div class="empty-icon">
                                <i class="icon icon-calendar-empty"></i>
                            </div>
                            <h3><spring:message code="dashboard.upcoming.empty.title" /></h3>
                            <p><spring:message code="dashboard.upcoming.empty.message" /></p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Appointment History Tab -->
            <div class="tab-content" id="history-tab">
                <div class="tab-header">
                    <h2><spring:message code="dashboard.history.title" /></h2>
                    <div class="tab-actions">
                        <div class="search-container">
                            <label>
                                <input type="text" class="search-input" placeholder="<spring:message code="dashboard.search.placeholder" />" />
                            </label>
                            <button class="search-button">
                                <i class="icon icon-search"></i>
                            </button>
                        </div>
                    </div>
                </div>

                <div class="empty-state">
                    <div class="empty-icon">
                        <i class="icon icon-history-empty"></i>
                    </div>
                    <h3><spring:message code="dashboard.history.empty.title" /></h3>
                    <p><spring:message code="dashboard.history.empty.message" /></p>
                </div>
            </div>

            <!-- Profile Tab -->
            <div class="tab-content" id="profile-tab">
                <div class="tab-header">
                    <h2><spring:message code="dashboard.profile.title" /></h2>
                    <div class="tab-actions">
                        <a class="btn btn-primary">
                            <i class="icon icon-edit"></i>
                            <spring:message code="dashboard.profile.edit" />
                        </a>
                    </div>
                </div>

                <div class="profile-content">
                    <div class="profile-section">
                        <h3 class="section-title"><spring:message code="dashboard.profile.personalInfo" /></h3>
                        <div class="info-grid">
                            <div class="info-item">
                                <div class="info-label"><spring:message code="dashboard.profile.email" /></div>
                                <div class="info-value"><c:out value="${patient.email}" /></div>
                            </div>
                            <div class="info-item">
                                <div class="info-label"><spring:message code="dashboard.profile.phone" /></div>
                                <div class="info-value"><c:out value="${patient.phone}" /></div>
                            </div>
                        </div>
                    </div>

                    <div class="profile-section">
                        <h3 class="section-title"><spring:message code="dashboard.profile.coverages" /></h3>
                        <div class="coverages-list">
                                <div class="coverage-item">
                                    <c:out value="${patient.coverage.name}" />
                                </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <link rel="stylesheet" href="<c:url value='/css/components/doctor-dashboard.css' />" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const tabs = document.querySelectorAll('.nav-tab');
            const tabContents = document.querySelectorAll('.tab-content');

            tabs.forEach(tab => {
                tab.addEventListener('click', function() {
                    // Remove active class from all tabs and contents
                    tabs.forEach(t => t.classList.remove('active'));
                    tabContents.forEach(c => c.classList.remove('active'));

                    // Add active class to clicked tab
                    this.classList.add('active');

                    // Get the tab ID from the data-tab attribute
                    const tabId = this.getAttribute('data-tab');

                    // Find the corresponding content element
                    const activeContent = document.getElementById(tabId + '-tab');

                    // Add active class to the content if it exists
                    if (activeContent) {
                        activeContent.classList.add('active');
                    } else {
                        console.error('Tab content not found for: ' + tabId);
                    }
                });
            });
        });
    </script>
</layout:page>