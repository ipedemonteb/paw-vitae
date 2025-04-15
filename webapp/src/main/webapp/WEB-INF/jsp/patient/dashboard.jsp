<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<layout:page title="dashboard.patient.title">
    <div class="dashboard-container">
        <!-- Patient Profile Header -->
        <div class="dashboard-header">
            <div class="doctor-info">
                <div class="doctor-avatar">
                    <img src="<c:url value="/doctor/${patient.id}/image"/>" alt="<c:out value="${patient.name} ${patient.lastName}"/>'">
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
            <div class="dashboard-stats">
                <div class="stat-item">
                    <div class="stat-value">${upcomingAppointments.size()}</div>
                    <div class="stat-label"><spring:message code="dashboard.stats.upcoming" /></div>
                </div>
                <div class="stat-item">
                    <div class="stat-value">${pastAppointments.size()}</div>
                    <div class="stat-label"><spring:message code="dashboard.stats.past" /></div>
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
                <i class="fas fa-user"></i>
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
                            <!-- CORREGIDO: Valores del filtro actualizados para coincidir con el enum -->
                            <select id="status-filter" class="filter-select">
                                <option value="<spring:message code="dashboard.filter.all" />" selected><spring:message code="dashboard.filter.all" /></option>
                                <option value="<spring:message code="appointment.status.pending" />"><spring:message code="appointment.status.pending" /></option>
                                <option value="<spring:message code="appointment.status.confirmed" />"><spring:message code="appointment.status.confirmed" /></option>
                                <option value="<spring:message code="appointment.status.cancelled" />"><spring:message code="appointment.status.cancelled" /></option>
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
                                <!-- CORREGIDO: Atributos data-status y data-date con comillas -->
                                <div class="appointment-card" data-status="<spring:message code="${appointment.key.status}"/>" data-date="<c:out value="${appointment.key.date}"/>">
                                    <div class="appointment-time">
                                        <div class="appointment-date">
                                            <span class="day">
                                                    <spring:message code="${appointment.key.date.dayOfWeek}" />
                                            </span>
                                            <span class="date-number">
                                                    <c:out value="${appointment.key.date.dayOfMonth}"/>
                                            </span>
                                            <span class="month">
                                                     <spring:message code="${appointment.key.date.month}" />
                                            </span>
                                        </div>
                                        <div class="appointment-hour">
                                            <i class="fas fa-clock"></i>
                                            <c:out value="${appointment.key.date.hour}"/>:00
                                        </div>
                                        <div class="appointment-status-indicator">
                                            <span class="status-badge <c:out value="${appointment.key.status}"/>">
                                                <spring:message code='${appointment.key.status}'/>
                                            </span>
                                        </div>
                                    </div>
                                    <div class="appointment-details">
                                        <div class="patient-info">
                                            <div class="patient-avatar">
                                                <img src="<c:url value="/doctor/${appointment.value.id}/image"/>" alt="<c:out value="${appointment.value.name} ${appointment.value.lastName}"/>"/>
                                            </div>
                                            <div>
                                                <div class="patient-name">
                                                    <c:out value="${appointment.value.name}" /> <c:out value="${appointment.value.lastName}" />
                                                </div>
                                                <div class="patient-coverage">
                                                    <c:out value="${appointment.value.specialtyList[0].key}" />
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
                                            <c:set var="status" >
                                                <spring:message code="${appointment.key.status}" />
                                            </c:set>
                                            <c:set var="pending">
                                            <spring:message code="appointment.status.pending" />
                                            </c:set>
                                            <c:set var="confirmed">
                                            <spring:message code="appointment.status.confirmed" />
                                            </c:set>
                                            <c:set var="all">
                                                <spring:message code="dashboard.filter.all" />
                                            </c:set>
                                            <c:if test="${status eq pending|| status eq confirmed}">
                                                <button class="btn btn-danger cancel-appointment" data-id="<c:out value="${appointment.key.id}"/>">
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
                                <option value="<spring:message code="dashboard.filter.all" />" selected><spring:message code="dashboard.filter.all" /></option>
                                <option value="<spring:message code="appointment.status.completed" />"><spring:message code="appointment.status.completed" /></option>
                                <option value="<spring:message code="appointment.status.cancelled" />"><spring:message code="appointment.status.cancelled" /></option>
                                <option value="<spring:message code="appointment.status.noShow" />"><spring:message code="appointment.status.noShow" /></option>
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
                                <!-- CORREGIDO: Atributos data-status y data-date con comillas -->
                                <div class="appointment-card past" data-status="<spring:message code="${appointment.key.status}"/>" data-date="<c:out value="${appointment.key.date}"/>">
                                    <div class="appointment-time">
                                        <div class="appointment-date">
                                            <span class="day">
                                                    <spring:message code="${appointment.key.date.dayOfWeek}"/>
                                            </span>
                                            <span class="date-number">
                                                    <c:out value="${appointment.key.date.dayOfMonth}"/>
                                            </span>
                                            <span class="month">
                                                     <spring:message code="${appointment.key.date.month}" />
                                            </span>
                                        </div>
                                        <div class="appointment-hour">
                                            <i class="fas fa-clock"></i>
                                            <c:out value="${appointment.key.date.hour}"/>:00
                                        </div>
                                        <div class="appointment-status-indicator">
                                            <span class="status-badge <c:out value="${appointment.key.status}"/>">
                                                <spring:message code='${appointment.key.status}'/>
                                            </span>
                                        </div>
                                    </div>
                                    <div class="appointment-details">
                                        <div class="patient-info">
                                            <div class="patient-avatar">
                                                <img src="<c:url value="/doctor/${appointment.value.id}/image"/>" alt="<c:out value="${patient.name} ${patient.lastName}"/>'">
                                            </div>
                                            <div>
                                                <div class="patient-name">
                                                    <c:out value="${appointment.value.name}" /> <c:out value="${appointment.value.lastName}" />
                                                </div>
                                                <div class="patient-coverage">
                                                    <c:out value="${appointment.value.specialtyList[0].key}" />
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
<%--                                        <div class="appointment-actions">--%>
<%--                                            <a href="<c:url value='/appointments/${appointment.key.id}/records' />" class="btn btn-secondary">--%>
<%--                                                <i class="fas fa-file-medical-alt"></i>--%>
<%--                                                <span><spring:message code="appointment.action.records" /></span>--%>
<%--                                            </a>--%>
<%--                                        </div>--%>
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
                        <button id="edit-profile-btn" class="btn btn-primary">
                            <i class="fas fa-edit"></i>
                            <spring:message code="dashboard.profile.edit" />
                        </button>
                    </div>
                </div>

                <div class="profile-content">
                    <!-- Visible Profile Information -->
                    <div id="profile-view" class="profile-section">
                        <h3 class="section-title"><spring:message code="dashboard.profile.personalInfo" /></h3>
                        <div class="info-grid">
                            <div class="info-item">
                                <div class="info-label"><spring:message code="dashboard.profile.email" /></div>
                                <div class="info-value"><c:out value="${patient.email}" /></div>
                            </div>
                            <div class="info-item">
                                <div class="info-label"><spring:message code="register.firstName" /></div>
                                <div class="info-value"><c:out value="${patient.name}" /></div>
                            </div>
                            <div class="info-item">
                                <div class="info-label"><spring:message code="register.lastName" /></div>
                                <div class="info-value"><c:out value="${patient.lastName}" /></div>
                            </div>
                            <div class="info-item">
                                <div class="info-label"><spring:message code="dashboard.profile.phone" /></div>
                                <div class="info-value"><c:out value="${patient.phone}" /></div>
                            </div>
                        </div>
                    </div>

                    <div id="coverage-section" class="profile-section">
                        <h3 class="section-title"><spring:message code="dashboard.profile.coverages" /></h3>
                        <div class="coverages-list">
                            <div class="coverage-item">
                                <c:out value="${patient.coverage.name}" />
                            </div>
                        </div>
                    </div>

                    <!-- Edit Profile Form (Hidden by default) -->
                    <div id="edit-profile-form" class="profile-section" style="display: none;">
                        <h3 class="section-title text-center"><spring:message code="dashboard.profile.edit" /></h3>

                        <form:form id="updatePatientForm" modelAttribute="updatePatientForm" method="post" action="${pageContext.request.contextPath}/patient/dashboard/update" cssClass="edit-profile-form">
                            <div class="form-row">
                                <div class="form-group">
                                    <form:label path="name"><spring:message code="register.firstName" /></form:label>
                                    <form:input path="name" cssClass="form-control" value="${patient.name}" />
                                    <form:errors path="name" cssClass="error-message" />
                                </div>

                                <div class="form-group">
                                    <form:label path="lastName"><spring:message code="register.lastName" /></form:label>
                                    <form:input path="lastName" cssClass="form-control" value="${patient.lastName}" />
                                    <form:errors path="lastName" cssClass="error-message" />
                                </div>
                            </div>

                            <div class="form-group">
                                <form:label path="phone"><spring:message code="register.phone" /></form:label>
                                <form:input path="phone" cssClass="form-control" value="${patient.phone}" />
                                <form:errors path="phone" cssClass="error-message" />
                            </div>

                            <div class="form-group">
                                <form:label path="coverage"><spring:message code="register.coverage" /></form:label>
                                <div class="current-coverage">
                                    <spring:message code="register.selectCoverage" />:
                                    <span id="current-coverage-name">
                                        <c:out value="${not empty patient.coverage ? patient.coverage.name : 'None'}" />
                                    </span>
                                </div>
                                <div class="multi-select-container" id="coverage-container">
                                    <div class="custom-multi-select" id="coverage-options">
                                        <input type="text" id="coverage-search" class="search-box" placeholder="<spring:message code="register.search" />" />
                                        <c:forEach items="${coverageList}" var="coverage">
                                            <div class="custom-multi-select-option"
                                                 data-value="${coverage.id}"
                                                 data-name="${coverage.name}"
                                                 onclick="toggleCoverage(this)">
                                                <div class="option-checkbox"></div>
                                                <div class="option-text">
                                                    <c:out value="${coverage.name}"/>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                    <!-- Asegurarse de que el campo oculto tenga el ID (no el nombre) -->
                                    <form:input path="coverage" id="coverage-input"
                                                value="${not empty patient.coverage ? patient.coverage.id : ''}"
                                                cssClass="form-control" style="display: none;" />
                                </div>
                                <form:errors path="coverage" cssClass="error-message" />
                            </div>

                            <div class="form-actions">
                                <button type="button" id="cancel-edit-btn" class="btn btn-secondary">
                                    <spring:message code="logout.confirmation.cancel" />
                                </button>
                                <button type="submit" class="btn btn-primary">
                                    <spring:message code="appointment.form.save" />
                                </button>
                            </div>
                        </form:form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <link rel="stylesheet" href="<c:url value='/css/components/doctor-dashboard.css' />" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />

    <style>
        .text-center {
            text-align: center;
        }

        .profile-content {
            display: flex;
            flex-direction: column;
            align-items: center;
            width: 100%;
        }

        .profile-section {
            width: 100%;
            max-width: 800px;
            margin: 0 auto 1.5rem;
            background-color: #fff;
            border-radius: 0.5rem;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
            padding: 1.5rem;
        }

        #edit-profile-form {
            width: 100%;
            max-width: 800px;
            margin: 0 auto;
        }

        .info-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 1rem;
        }

        .info-item {
            padding: 0.75rem;
            border-radius: 0.375rem;
            background-color: #f9fafb;
        }

        .info-label {
            font-size: 0.875rem;
            color: #6b7280;
            margin-bottom: 0.25rem;
        }

        .info-value {
            font-size: 1rem;
            font-weight: 500;
            color: #1f2937;
        }

        .coverages-list {
            display: flex;
            flex-wrap: wrap;
            gap: 0.5rem;
        }

        .coverage-item {
            background-color: #e1effe;
            color: #2a5caa;
            padding: 0.375rem 0.75rem;
            border-radius: 0.375rem;
            font-size: 0.875rem;
            font-weight: 500;
        }
    </style>

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
                        console.log(selectedStatus)
                        if (selectedStatus === '${all}' || cardStatus === selectedStatus) {
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
                        const cardStatus = card.getAttribute('data-status');

                        if (selectedStatus === '${all}' || cardStatus === selectedStatus) {
                            card.style.display = 'flex';
                        } else {
                            card.style.display = 'none';
                        }
                    });
                });
            }

            const dateFilter = document.getElementById('date-range');
            if (dateFilter) {
                dateFilter.addEventListener('change', function() {
                    const selectedDateRange = this.value;
                    const appointmentCards = document.querySelectorAll('#upcoming-tab .appointment-card');
                    const today = new Date();
                    const startOfWeek = new Date();
                    startOfWeek.setDate(today.getDate() - today.getDay());
                    const startOfMonth = new Date(today.getFullYear(), today.getMonth(), 1);
                    const endOfMonth = new Date(today.getFullYear(), today.getMonth() + 1, 0);
                    const startOfToday = new Date(today.getFullYear(), today.getMonth(), today.getDate());
                    const endOfToday = new Date(today.getFullYear(), today.getMonth(), today.getDate() + 1);
                    const startOfAll = new Date(0);
                    const endOfAll = new Date(9999, 11, 31);
                    let startDate, endDate;
                    switch (selectedDateRange) {
                        case 'today':
                            startDate = startOfToday;
                            endDate = endOfToday;
                            break;
                        case 'week':
                            startDate = startOfWeek;
                            endDate = new Date(startOfWeek);
                            endDate.setDate(endDate.getDate() + 7);
                            break;
                        case 'month':
                            startDate = startOfMonth;
                            endDate = endOfMonth;
                            break;
                        case 'all':
                            startDate = startOfAll;
                            endDate = endOfAll;
                            break;
                        default:
                            startDate = startOfAll;
                            endDate = endOfAll;
                    }
                    appointmentCards.forEach(card => {
                        const cardDate = new Date(card.getAttribute('data-date'));
                        if (cardDate >= startDate && cardDate <= endDate) {
                            card.style.display = 'flex';
                        } else {
                            card.style.display = 'none';
                        }
                    });
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
                        const doctorName = card.querySelector('.patient-name').textContent.toLowerCase();
                        const reasonText = card.querySelector('.reason-text')?.textContent.toLowerCase() || '';

                        if (doctorName.includes(searchTerm) || reasonText.includes(searchTerm)) {
                            card.style.display = 'flex';
                        } else {
                            card.style.display = 'none';
                        }
                    });
                });
            }

            // Cancel appointment functionality
            const cancelButtons = document.querySelectorAll('.cancel-appointment');
            cancelButtons.forEach(button => {
                button.addEventListener('click', function(e) {
                    e.preventDefault();
                    const appointmentId = this.getAttribute('data-id');
                    if (confirm('¿Estás seguro de que querés cancelar este turno?')) {
                        fetch(`/patient/dashboard/appointment/cancel`, {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/x-www-form-urlencoded'
                            },
                            body: new URLSearchParams({ appointmentId })
                        })
                            .then(response => response.json())
                            .then(data => {
                                if (data.success) {
                                    window.location.reload();
                                }
                            })
                    }
                });
            });

            // Edit Profile Functionality
            const editProfileBtn = document.getElementById('edit-profile-btn');
            const cancelEditBtn = document.getElementById('cancel-edit-btn');
            const profileView = document.getElementById('profile-view');
            const coverageSection = document.getElementById('coverage-section');
            const editProfileForm = document.getElementById('edit-profile-form');

            if (editProfileBtn) {
                editProfileBtn.addEventListener('click', function() {
                    profileView.style.display = 'none';
                    coverageSection.style.display = 'none';
                    editProfileForm.style.display = 'block';
                });
            }

            if (cancelEditBtn) {
                cancelEditBtn.addEventListener('click', function() {
                    profileView.style.display = 'block';
                    coverageSection.style.display = 'block';
                    editProfileForm.style.display = 'none';
                });
            }

            // Initialize coverage dropdown
            initCoverageDropdown();

            // Initialize search functionality for coverage
            initCoverageSearch();

            // Pre-select the current coverage
            preSelectCurrentCoverage();
        });

        function initCoverageDropdown() {
            const coverageOptions = document.querySelectorAll('#coverage-options .custom-multi-select-option');
            const coverageInput = document.getElementById('coverage-input');

            // Set initial value from the input
            updateSelectedCoverage();
        }

        function toggleCoverage(optionElement) {
            // Deseleccionar todas las opciones primero
            const allOptions = document.querySelectorAll('#coverage-options .custom-multi-select-option');
            allOptions.forEach(option => {
                option.classList.remove('selected');
            });

            // Seleccionar la opción clickeada
            optionElement.classList.add('selected');

            // Obtener el ID y nombre de la cobertura
            const coverageId = optionElement.getAttribute('data-value');
            const coverageName = optionElement.getAttribute('data-name');

            // Actualizar el input oculto con el ID (no el nombre)
            const coverageInput = document.getElementById('coverage-input');
            coverageInput.value = coverageId;

            // Actualizar el nombre mostrado en pantalla
            const currentCoverageDisplay = document.getElementById('current-coverage-name');
            if (currentCoverageDisplay) {
                currentCoverageDisplay.textContent = coverageName;
            }
        }

        function updateSelectedCoverage() {
            const coverageInput = document.getElementById('coverage-input');
            const currentCoverageId = coverageInput.value;

            // Find the option with this coverage ID
            if (currentCoverageId) {
                const options = document.querySelectorAll('#coverage-options .custom-multi-select-option');

                options.forEach(option => {
                    const id = option.getAttribute('data-value');
                    if (id === currentCoverageId) {
                        option.classList.add('selected');

                        // Update the current coverage display with the name
                        const coverageName = option.getAttribute('data-name');
                        const currentCoverageDisplay = document.getElementById('current-coverage-name');
                        if (currentCoverageDisplay) {
                            currentCoverageDisplay.textContent = coverageName;
                        }
                    } else {
                        option.classList.remove('selected');
                    }
                });
            } else {
                // If no ID is set, try to match by name (for backward compatibility)
                const currentCoverageDisplay = document.getElementById('current-coverage-name');
                if (currentCoverageDisplay) {
                    const currentCoverageName = currentCoverageDisplay.textContent;
                    if (currentCoverageName) {
                        const options = document.querySelectorAll('#coverage-options .custom-multi-select-option');

                        options.forEach(option => {
                            const name = option.getAttribute('data-name');
                            if (name === currentCoverageName) {
                                option.classList.add('selected');

                                // Update the input with the ID
                                const id = option.getAttribute('data-value');
                                coverageInput.value = id;
                            }
                        });
                    }
                }
            }
        }

        function initCoverageSearch() {
            const searchBox = document.getElementById('coverage-search');
            const options = document.querySelectorAll('#coverage-options .custom-multi-select-option');

            searchBox.addEventListener('input', function() {
                const searchTerm = this.value.toLowerCase();

                options.forEach(option => {
                    const text = option.querySelector('.option-text').textContent.toLowerCase();
                    if (text.includes(searchTerm)) {
                        option.style.display = 'flex';
                    } else {
                        option.style.display = 'none';
                    }
                });
            });
        }

        function preSelectCurrentCoverage() {
            const coverageInput = document.getElementById('coverage-input');
            const currentCoverageId = coverageInput.value;

            if (currentCoverageId) {
                const options = document.querySelectorAll('#coverage-options .custom-multi-select-option');

                options.forEach(option => {
                    const id = option.getAttribute('data-value');
                    if (id === currentCoverageId) {
                        option.classList.add('selected');

                        // Update the current coverage display
                        const coverageName = option.getAttribute('data-name');
                        const currentCoverageDisplay = document.getElementById('current-coverage-name');
                        if (currentCoverageDisplay) {
                            currentCoverageDisplay.textContent = coverageName;
                        }
                    }
                });
            } else {
                // Fallback: Match by name if ID is not set
                const currentCoverageDisplay = document.getElementById('current-coverage-name');
                if (currentCoverageDisplay) {
                    const currentCoverageName = currentCoverageDisplay.textContent;
                    if (currentCoverageName) {
                        const options = document.querySelectorAll('#coverage-options .custom-multi-select-option');

                        options.forEach(option => {
                            const name = option.getAttribute('data-name');
                            if (name === currentCoverageName) {
                                option.classList.add('selected');

                                // Update the input with the ID
                                const id = option.getAttribute('data-value');
                                coverageInput.value = id;
                            }
                        });
                    }
                }
            }
        }
    </script>

    <link rel="stylesheet" href="<c:url value='/css/components/forms.css' />" />
    <link rel="stylesheet" href="<c:url value='/css/components/inline-form.css' />" />
</layout:page>