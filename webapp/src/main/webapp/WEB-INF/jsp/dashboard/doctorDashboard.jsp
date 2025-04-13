<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>

<link rel="stylesheet" href="<c:url value='/css/components/doctor-dashboard.css' />" />

<layout:page title="dashboard.doctor.title">
    <div class="dashboard-container">
        <div class="dashboard-header">
            <div class="doctor-info">
                <div class="doctor-avatar">
                    <c:choose>
                        <c:when test="${not empty doctor.imageId}">
                            <img src="<c:url value='/images/${doctor.imageId}' />" alt="<c:out value='${doctor.name}' /> <c:out value='${doctor.lastName}' />" />
                        </c:when>
                        <c:otherwise>
                            <div class="avatar-placeholder">
                                <span>${doctor.name.charAt(0)}${doctor.lastName.charAt(0)}</span>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="doctor-details">
                    <h1 class="doctor-name"><c:out value="${doctor.name}" /> <c:out value="${doctor.lastName}" /></h1>
                    <div class="doctor-specialties">
                        <c:forEach items="${doctor.specialties}" var="specialty" varStatus="status">
                            <span class="specialty-tag">
                                <c:choose>
                                    <c:when test="${not empty specialty.key}">
                                        <spring:message code="${specialty.key}" />
                                    </c:when>
                                    <c:otherwise>
                                        <c:out value="${specialty.name}" />
                                    </c:otherwise>
                                </c:choose>
                            </span>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>

        <div class="dashboard-nav">
            <button class="nav-tab active" data-tab="upcoming">
                <span class="tab-icon calendar-icon"></span>
                <spring:message code="dashboard.tab.upcoming" />
            </button>
            <button class="nav-tab" data-tab="history">
                <span class="tab-icon history-icon"></span>
                <spring:message code="dashboard.tab.history" />
            </button>
            <button class="nav-tab" data-tab="profile">
                <span class="tab-icon profile-icon"></span>
                <spring:message code="dashboard.tab.profile" />
            </button>
<%--            <button class="nav-tab" data-tab="settings">--%>
<%--                <span class="tab-icon settings-icon"></span>--%>
<%--                <spring:message code="dashboard.tab.settings" />--%>
<%--            </button>--%>
        </div>

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
                                            <fmt:formatDate value="${appointment.date}" pattern="EEE" />
                                            <span class="date-number"><fmt:formatDate value="${appointment.date}" pattern="d" /></span>
                                            <fmt:formatDate value="${appointment.date}" pattern="MMM" />
                                        </div>
                                        <div class="appointment-hour">
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
                                                        <spring:message code="appointment.type.virtual" />
                                                    </span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="appointment-badge in-person">
                                                        <spring:message code="appointment.type.inPerson" />
                                                    </span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        <div class="appointment-actions">
                                            <c:if test="${appointment.virtual}">
                                                <a href="<c:url value='/appointments/${appointment.id}/join' />" class="btn btn-primary">
                                                    <spring:message code="appointment.action.join" />
                                                </a>
                                            </c:if>
                                            <a href="<c:url value='/appointments/${appointment.id}' />" class="btn btn-secondary">
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
                            <div class="empty-icon calendar-empty"></div>
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
                            <input type="text" class="search-input" placeholder="<spring:message code="dashboard.search.placeholder" />" />
                            <button class="search-button">
                                <span class="search-icon"></span>
                            </button>
                        </div>
                    </div>
                </div>

                <c:choose>
                    <c:when test="${not empty appointmentHistory}">
                        <div class="appointments-list history-list">
                            <c:forEach items="${appointmentHistory}" var="appointment">
                                <div class="appointment-card">
                                    <div class="appointment-time">
                                        <div class="appointment-date">
                                            <fmt:formatDate value="${appointment.date}" pattern="EEE" />
                                            <span class="date-number"><fmt:formatDate value="${appointment.date}" pattern="d" /></span>
                                            <fmt:formatDate value="${appointment.date}" pattern="MMM" />
                                        </div>
                                        <div class="appointment-hour">
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
                                        <div class="appointment-status">
                                            <c:choose>
                                                <c:when test="${appointment.status eq 'COMPLETED'}">
                                                    <span class="status-badge completed">
                                                        <spring:message code="appointment.status.completed" />
                                                    </span>
                                                </c:when>
                                                <c:when test="${appointment.status eq 'CANCELLED'}">
                                                    <span class="status-badge cancelled">
                                                        <spring:message code="appointment.status.cancelled" />
                                                    </span>
                                                </c:when>
                                                <c:when test="${appointment.status eq 'NO_SHOW'}">
                                                    <span class="status-badge no-show">
                                                        <spring:message code="appointment.status.noShow" />
                                                    </span>
                                                </c:when>
                                            </c:choose>
                                        </div>
                                        <div class="appointment-actions">
                                            <a href="<c:url value='/appointments/${appointment.id}/medical-record' />" class="btn btn-secondary">
                                                <spring:message code="appointment.action.viewRecord" />
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="empty-state">
                            <div class="empty-icon history-empty"></div>
                            <h3><spring:message code="dashboard.history.empty.title" /></h3>
                            <p><spring:message code="dashboard.history.empty.message" /></p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Profile Tab -->
            <div class="tab-content" id="profile-tab">
                <div class="tab-header">
                    <h2><spring:message code="dashboard.profile.title" /></h2>
                    <div class="tab-actions">
<%--                        <a href="<c:url value='/doctor/profile/edit' />" class="btn btn-primary">--%>
                        <a class="btn btn-primary">
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
                                <div class="info-value"><c:out value="${doctor.email}" /></div>
                            </div>
                            <div class="info-item">
                                <div class="info-label"><spring:message code="dashboard.profile.phone" /></div>
                                <div class="info-value"><c:out value="${doctor.phone}" /></div>
                            </div>
                            <div class="info-item">
                                <div class="info-label"><spring:message code="dashboard.profile.memberSince" /></div>
                                <div class="info-value"><fmt:formatDate value="${doctor.createdAt}" pattern="MMMM yyyy" /></div>
                            </div>
                        </div>
                    </div>

                    <div class="profile-section">
                        <h3 class="section-title"><spring:message code="dashboard.profile.specialties" /></h3>
                        <div class="specialties-list">
                            <c:forEach items="${doctor.specialties}" var="specialty">
                                <div class="specialty-item">
                                    <c:choose>
                                        <c:when test="${not empty specialty.key}">
                                            <spring:message code="${specialty.key}" />
                                        </c:when>
                                        <c:otherwise>
                                            <c:out value="${specialty.name}" />
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </c:forEach>
                        </div>
                    </div>

                    <div class="profile-section">
                        <h3 class="section-title"><spring:message code="dashboard.profile.coverages" /></h3>
                        <div class="coverages-list">
                            <c:forEach items="${doctor.coverages}" var="coverage">
                                <div class="coverage-item">
                                    <c:out value="${coverage.name}" />
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>


<%--            <!-- Settings Tab -->--%>
<%--            <div class="tab-content" id="settings-tab">--%>
<%--                <div class="tab-header">--%>
<%--                    <h2><spring:message code="dashboard.settings.title" /></h2>--%>
<%--                </div>--%>
<%--                    <div class="settings-section">--%>
<%--                        <h3 class="section-title"><spring:message code="dashboard.settings.account" /></h3>--%>
<%--                        <div class="account-settings">--%>
<%--&lt;%&ndash;                            <a href="<c:url value='/doctor/password/change' />" class="btn btn-secondary">&ndash;%&gt;--%>
<%--                            <a class="btn btn-secondary">--%>
<%--                                <spring:message code="dashboard.settings.changePassword" />--%>
<%--                            </a>--%>
<%--                            <button class="btn btn-danger" id="deactivate-account">--%>
<%--                                <spring:message code="dashboard.settings.deactivateAccount" />--%>
<%--                            </button>--%>
<%--                        </div>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--            </div>--%>
        </div>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Tab switching functionality
            const tabs = document.querySelectorAll('.nav-tab');
            const tabContents = document.querySelectorAll('.tab-content');

            tabs.forEach(tab => {
                tab.addEventListener('click', () => {
                    // Remove active class from all tabs and contents
                    tabs.forEach(t => t.classList.remove('active'));
                    tabContents.forEach(c => c.classList.remove('active'));

                    // Add active class to clicked tab
                    tab.classList.add('active');

                    // Show corresponding content
                    const tabId = tab.getAttribute('data-tab');
                    document.getElementById(`${tabId}-tab`).classList.add('active');
                });
            });

            // Date filter functionality
            const dateFilter = document.getElementById('date-range');
            if (dateFilter) {
                dateFilter.addEventListener('change', function() {
                    // In a real application, this would filter appointments based on the selected date range
                    console.log('Filter appointments by:', this.value);
                    // You would typically make an AJAX call here to fetch filtered data
                });
            }

            // Search functionality
            const searchInput = document.querySelector('.search-input');
            if (searchInput) {
                searchInput.addEventListener('input', function() {
                    // In a real application, this would search through appointments
                    console.log('Search term:', this.value);
                    // You would typically implement a search function here
                });
            }

            // Availability edit functionality
            const editButtons = document.querySelectorAll('.edit-availability');
            editButtons.forEach(button => {
                button.addEventListener('click', function() {
                    const day = this.getAttribute('data-day');
                    // In a real application, this would open a modal to edit availability
                    console.log('Edit availability for:', day);
                    // You would typically show a modal or form here
                });
            });

            // Account deactivation confirmation
            const deactivateButton = document.getElementById('deactivate-account');
            if (deactivateButton) {
                deactivateButton.addEventListener('click', function() {
                    if (confirm('<spring:message code="dashboard.settings.deactivateConfirm" />')) {
                        // In a real application, this would submit a request to deactivate the account
                        console.log('Account deactivation confirmed');
                        // You would typically make an AJAX call or submit a form here
                    }
                });
            }

            // Save notification settings
            const saveNotificationsButton = document.querySelector('.save-notifications');
            if (saveNotificationsButton) {
                saveNotificationsButton.addEventListener('click', function() {
                    const emailNotifications = document.getElementById('email-notifications').checked;
                    const smsNotifications = document.getElementById('sms-notifications').checked;
                    const appointmentReminders = document.getElementById('appointment-reminders').checked;

                    // In a real application, this would save the notification settings
                    console.log('Save notification settings:', {
                        emailNotifications,
                        smsNotifications,
                        appointmentReminders
                    });
                    // You would typically make an AJAX call or submit a form here

                    // Show a success message
                    alert('<spring:message code="dashboard.settings.saveSuccess" />');
                });
            }
        });
    </script>
</layout:page>