<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<layout:page title="dashboard.doctor.title">
    <div class="dashboard-container">
        <!-- Doctor Profile Header -->
        <div class="dashboard-header">
            <div class="doctor-info">
                <div class="doctor-avatar">
                    <img src="<c:url value="/doctor/${doctor.id}/image"/>" alt="<c:out value="${doctor.name} ${doctor.lastName}"/>"/>
                </div>
                <div class="doctor-details">
                    <h1 class="doctor-name"><c:out value="${doctor.name}" /> <c:out value="${doctor.lastName}" /></h1>
                    <div class="doctor-meta">
                        <div class="doctor-meta-item">
                            <i class="fas fa-envelope"></i>
                            <span><c:out value="${doctor.email}" /></span>
                        </div>
                        <div class="doctor-meta-item">
                            <i class="fas fa-phone"></i>
                            <span><c:out value="${doctor.phone}" /></span>
                        </div>
                    </div>
                    <div class="doctor-specialties">
                        <c:forEach items="${doctor.specialtyList}" var="specialty" varStatus="status">
                            <span class="specialty-tag">
                                <c:choose>
                                    <c:when test="${not empty specialty.key}">
                                        <spring:message code="${specialty.key}" />
                                    </c:when>
                                    <c:otherwise>
                                        <c:out value="${specialty.key}" />
                                    </c:otherwise>
                                </c:choose>
                            </span>
                        </c:forEach>
                    </div>
                </div>
            </div>
            <div class="dashboard-stats">
                <div class="stat-item">
                    <div class="stat-value">${upcomingAppointments.size()}</div>
                    <div class="stat-label"><spring:message code="dashboard.stats.upcoming" /></div>
                </div>
                <div class="stat-item">
                    <div class="stat-value">${pastAppointments.size()}</div>
                    <div class="stat-label"><spring:message code="dashboard.stats.past" /></div>
                </div>
                <div class="stat-item">
                    <div class="stat-value">${doctor.specialtyList.size()}</div>
                    <div class="stat-label"><spring:message code="dashboard.stats.specialties" /></div>
                </div>
            </div>
        </div>

        <!-- Dashboard Navigation Tabs -->
        <div class="dashboard-nav">
            <button class="nav-tab active" data-tab="upcoming">
                <i class="fas fa-calendar-alt"></i>
                <span><spring:message code="dashboard.tab.upcoming" /></span>
            </button>
            <button class="nav-tab" data-tab="history">
                <i class="fas fa-history"></i>
                <span><spring:message code="dashboard.tab.history" /></span>
            </button>
            <button class="nav-tab" data-tab="profile">
                <i class="fas fa-user-md"></i>
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
                        <div class="status-filter">
                            <label for="status-filter"><spring:message code="dashboard.filter.status" />:</label>
                            <select id="status-filter" class="filter-select">
                                <option value="all" selected><spring:message code="dashboard.filter.all" /></option>
                                <option value="pending"><spring:message code="appointment.status.pending" /></option>
                                <option value="confirmed"><spring:message code="appointment.status.confirmed" /></option>
                                <option value="cancelled"><spring:message code="appointment.status.cancelled" /></option>
                            </select>
                        </div>
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
                                <div class="appointment-card" data-status="<spring:message code='${appointment.key.status}'/>">
                                    <div class="appointment-time">
                                        <div class="appointment-date">
                                            <span class="day">
                                                    ${appointment.key.date.dayOfWeek}
                                            </span>
                                            <span class="date-number">
                                                    ${appointment.key.date.dayOfMonth}
                                            </span>
                                            <span class="month">
                                                    ${appointment.key.date.month}
                                            </span>
                                        </div>
                                        <div class="appointment-hour">
                                            <i class="fas fa-clock"></i>
                                                ${appointment.key.date.hour}:00
                                        </div>
                                        <div class="appointment-status-indicator">
                                            <span class="status-badge ${appointment.key.status}">
                                                <spring:message code='${appointment.key.status}'/>
                                            </span>
                                        </div>
                                    </div>
                                    <div class="appointment-details">
                                        <div class="patient-info">
                                            <div class="patient-avatar">
                                                <div class="avatar-placeholder small">
                                                        ${fn:substring(appointment.value.name, 0, 1)}${fn:substring(appointment.value.lastName, 0, 1)}
                                                </div>
                                            </div>
                                            <div>
                                                <div class="patient-name">
                                                    <c:out value="${appointment.value.name}" /> <c:out value="${appointment.value.lastName}" />
                                                </div>
                                                <div class="patient-coverage">
                                                    <c:out value="${appointment.value.coverage.name}" />
                                                </div>
                                            </div>
                                        </div>
                                        <div class="appointment-reason">
                                            <div class="reason-label"><spring:message code="appointment.reason" />:</div>
                                            <div class="reason-text"><c:out value="${appointment.key.reason}" /></div>
                                        </div>
                                        <div class="appointment-specialty">
                                            <span class="specialty-badge">
                                                <spring:message code="${appointment.key.specialty.key}" />
                                            </span>
                                        </div>
                                        <div class="appointment-actions">
                                            <c:set var="status" value="${appointment.key.status}" />

                                            <c:if test="${status eq 'pendiente'}">
                                                <button class="btn btn-confirm confirm-appointment" data-id="${appointment.key.id}">
                                                    <i class="fas fa-check-circle"></i>
                                                    <span><spring:message code="appointment.action.confirm" /></span>
                                                </button>
                                                <button class="btn btn-danger cancel-appointment" data-id="${appointment.key.id}">
                                                    <i class="fas fa-times-circle"></i>
                                                    <span><spring:message code="appointment.action.cancel" /></span>
                                                </button>
                                            </c:if>

                                            <c:if test="${status eq 'confirmado'}">
                                                <button class="btn btn-danger cancel-appointment" data-id="${appointment.key.id}">
                                                    <i class="fas fa-times-circle"></i>
                                                    <span><spring:message code="appointment.action.cancel" /></span>
                                                </button>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="empty-state">
                            <div class="empty-icon">
                                <i class="fas fa-calendar-day fa-3x"></i>
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
                        <div class="status-filter">
                            <label for="history-status-filter"><spring:message code="dashboard.filter.status" />:</label>
                            <select id="history-status-filter" class="filter-select">
                                <option value="all" selected><spring:message code="dashboard.filter.all" /></option>
                                <option value="completed"><spring:message code="appointment.status.completed" /></option>
                                <option value="cancelled"><spring:message code="appointment.status.cancelled" /></option>
                                <option value="no-show"><spring:message code="appointment.status.noShow" /></option>
                            </select>
                        </div>
                        <div class="search-container">
                            <button class="search-button">
                                <i class="fas fa-search"></i>
                            </button>
                            <input type="text" class="search-input" placeholder="<spring:message code="dashboard.search.placeholder" />" />
                        </div>
                    </div>
                </div>

                <c:choose>
                    <c:when test="${not empty pastAppointments}">
                        <div class="appointments-list">
                            <c:forEach items="${pastAppointments}" var="appointment">
                                <div class="appointment-card past" data-status="<spring:message code='${appointment.key.status}'/>">
                                    <div class="appointment-time">
                                        <div class="appointment-date">
                                            <span class="day">
                                                    ${appointment.key.date.dayOfWeek}
                                            </span>
                                            <span class="date-number">
                                                    ${appointment.key.date.dayOfMonth}
                                            </span>
                                            <span class="month">
                                                    ${appointment.key.date.month}
                                            </span>
                                        </div>
                                        <div class="appointment-hour">
                                            <i class="fas fa-clock"></i>
                                                ${appointment.key.date.hour}:00
                                        </div>
                                        <div class="appointment-status-indicator">
                                            <span class="status-badge ${appointment.key.status}">
                                                <spring:message code='${appointment.key.status}'/>
                                            </span>
                                        </div>
                                    </div>
                                    <div class="appointment-details">
                                        <div class="patient-info">
                                            <div class="patient-avatar">
                                                <div class="avatar-placeholder small">
                                                        ${fn:substring(appointment.value.name, 0, 1)}${fn:substring(appointment.value.lastName, 0, 1)}
                                                </div>
                                            </div>
                                            <div>
                                                <div class="patient-name">
                                                    <c:out value="${appointment.value.name}" /> <c:out value="${appointment.value.lastName}" />
                                                </div>
                                                <div class="patient-coverage">
                                                    <c:out value="${appointment.value.coverage.name}" />
                                                </div>
                                            </div>
                                        </div>
                                        <div class="appointment-reason">
                                            <div class="reason-label"><spring:message code="appointment.reason" />:</div>
                                            <div class="reason-text"><c:out value="${appointment.key.reason}" /></div>
                                        </div>
                                        <div class="appointment-specialty">
                                            <span class="specialty-badge">
                                                <spring:message code="${appointment.key.specialty.key}" />
                                            </span>
                                        </div>
                                        <div class="appointment-actions">
                                            <a href="<c:url value='/appointments/${appointment.key.id}/records' />" class="btn btn-secondary">
                                                <i class="fas fa-file-medical-alt"></i>
                                                <span><spring:message code="appointment.action.records" /></span>
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
                                <i class="fas fa-history fa-3x"></i>
                            </div>
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
                        <a href="<c:url value='#' />" class="btn btn-primary">
                            <i class="fas fa-edit"></i>
                            <spring:message code="dashboard.profile.edit" />
                        </a>
                    </div>
                </div>

                <div class="profile-content">
                    <div class="profile-section">
                        <h3 class="section-title">
                            <i class="fas fa-user-circle"></i>
                            <spring:message code="dashboard.profile.personalInfo" />
                        </h3>
                        <div class="info-grid">
                            <div class="info-item">
                                <div class="info-label">
                                    <i class="fas fa-user"></i>
                                    <spring:message code="dashboard.profile.name" />
                                </div>
                                <div class="info-value"><c:out value="${doctor.name} ${doctor.lastName}" /></div>
                            </div>
                            <div class="info-item">
                                <div class="info-label">
                                    <i class="fas fa-envelope"></i>
                                    <spring:message code="dashboard.profile.email" />
                                </div>
                                <div class="info-value"><c:out value="${doctor.email}" /></div>
                            </div>
                            <div class="info-item">
                                <div class="info-label">
                                    <i class="fas fa-phone"></i>
                                    <spring:message code="dashboard.profile.phone" />
                                </div>
                                <div class="info-value"><c:out value="${doctor.phone}" /></div>
                            </div>
                        </div>
                    </div>

                    <div class="profile-section">
                        <h3 class="section-title">
                            <i class="fas fa-stethoscope"></i>
                            <spring:message code="dashboard.profile.specialties" />
                        </h3>
                        <div class="specialties-list">
                            <c:forEach items="${doctor.specialtyList}" var="specialty">
                                <div class="specialty-item">
                                    <c:choose>
                                        <c:when test="${not empty specialty.key}">
                                            <spring:message code="${specialty.key}" />
                                        </c:when>
                                        <c:otherwise>
                                            <c:out value="${specialty.key}" />
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </c:forEach>
                        </div>
                    </div>

                    <div class="profile-section">
                        <h3 class="section-title">
                            <i class="fas fa-shield-alt"></i>
                            <spring:message code="dashboard.profile.coverages" />
                        </h3>
                        <div class="coverages-list">
                            <c:forEach items="${doctor.coverageList}" var="coverage">
                                <div class="coverage-item">
                                    <c:out value="${coverage.name}" />
                                </div>
                            </c:forEach>
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
            // Tab switching functionality
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
                    }
                });
            });

            // Status filter functionality for upcoming appointments
            const statusFilter = document.getElementById('status-filter');
            if (statusFilter) {
                statusFilter.addEventListener('change', function() {
                    const selectedStatus = this.value;
                    const appointmentCards = document.querySelectorAll('#upcoming-tab .appointment-card');

                    appointmentCards.forEach(card => {
                        const cardStatus = card.getAttribute('data-status');

                        if (selectedStatus === 'all' || cardStatus.toLowerCase() === selectedStatus) {
                            card.style.display = 'flex';
                        } else {
                            card.style.display = 'none';
                        }
                    });
                });
            }

            // Status filter functionality for history appointments
            const historyStatusFilter = document.getElementById('history-status-filter');
            if (historyStatusFilter) {
                historyStatusFilter.addEventListener('change', function() {
                    const selectedStatus = this.value;
                    const appointmentCards = document.querySelectorAll('#history-tab .appointment-card');

                    appointmentCards.forEach(card => {
                        const cardStatus = card.getAttribute('data-status').toLowerCase();

                        if (selectedStatus === 'all' || cardStatus === selectedStatus) {
                            card.style.display = 'flex';
                        } else {
                            card.style.display = 'none';
                        }
                    });
                });
            }

            // Date filter functionality
            const dateFilter = document.getElementById('date-range');
            if (dateFilter) {
                dateFilter.addEventListener('change', function() {
                    // Filter appointments based on selected date range
                    console.log('Date filter changed to:', this.value);
                    // This would typically involve an AJAX call or form submission
                });
            }

            // Search functionality
            const searchInput = document.querySelector('.search-input');
            if (searchInput) {
                searchInput.addEventListener('input', function() {
                    // Filter appointments based on search input
                    const searchTerm = this.value.toLowerCase();
                    const appointmentCards = document.querySelectorAll('#history-tab .appointment-card');

                    appointmentCards.forEach(card => {
                        const patientName = card.querySelector('.patient-name').textContent.toLowerCase();
                        const reasonText = card.querySelector('.reason-text')?.textContent.toLowerCase() || '';

                        if (patientName.includes(searchTerm) || reasonText.includes(searchTerm)) {
                            card.style.display = 'flex';
                        } else {
                            card.style.display = 'none';
                        }
                    });
                });
            }

            // Confirm appointment functionality
            const confirmButtons = document.querySelectorAll('.confirm-appointment');
            confirmButtons.forEach(button => {
                button.addEventListener('click', function(e) {
                    e.preventDefault();
                    const appointmentId = this.getAttribute('data-id');
                    if (confirm('Are you sure you want to confirm this appointment?')) {
                        // This would typically involve an AJAX call to update the appointment status
                        console.log('Confirming appointment:', appointmentId);
                        // Example AJAX call:
                        // fetch(`/appointments/${appointmentId}/confirm`, { method: 'POST' })
                        //     .then(response => response.json())
                        //     .then(data => {
                        //         if (data.success) {
                        //             window.location.reload();
                        //         }
                        //     });
                    }
                });
            });

            // Cancel appointment functionality
            const cancelButtons = document.querySelectorAll('.cancel-appointment');
            cancelButtons.forEach(button => {
                button.addEventListener('click', function(e) {
                    e.preventDefault();
                    const appointmentId = this.getAttribute('data-id');
                    if (confirm('Are you sure you want to cancel this appointment?')) {
                        // This would typically involve an AJAX call to update the appointment status
                        console.log('Cancelling appointment:', appointmentId);
                        // Example AJAX call:
                        // fetch(`/appointments/${appointmentId}/cancel`, { method: 'POST' })
                        //     .then(response => response.json())
                        //     .then(data => {
                        //         if (data.success) {
                        //             window.location.reload();
                        //         }
                        //     });
                    }
                });
            });
        });
    </script>
</layout:page>
