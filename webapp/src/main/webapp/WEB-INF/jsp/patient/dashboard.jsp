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
                    <h1 class="doctor-name">
                        <c:out value="${patient.name}" /> <c:out value="${patient.lastName}" />
                    </h1>
                    <h4 class="doctor-specialties">
                        <c:out value="${patient.coverage.name}" />
                    </h4>
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
                    <c:when test="${not empty patient.appointments}">
                        <div class="appointments-list">
                            <c:forEach items="${patient.appointments}" var="appointment">
                                <div class="appointment-card">
                                    <div class="appointment-time">
                                        <div class="appointment-date">
                                            <span class="day">${appointment.date.dayOfWeek}</span>
                                            <span class="date-number">${appointment.date.dayOfMonth}</span>
                                            <span class="month">${appointment.date.month}</span>
                                        </div>
                                        <div class="appointment-hour">
                                                ${appointment.date.hour < 10 ? '0' : ''}${appointment.date.hour}:${appointment.date.minute < 10 ? '0' : ''}${appointment.date.minute}
                                        </div>
                                    </div>
                                    <div class="appointment-details">
                                        <div class="doctor-info">
                                            <div class="doctor-avatar">
                                                <img src="<c:url value="/doctor/${appointment.doctorId}/image"/>" alt="<c:out value="${appointment.doctorId} ${appointment.doctorId}"/>'">
                                            </div>
                                            <div class="doctor-details">
                                                <h1 class="doctor-name">
                                                    <c:out value="${appointment.doctor.name}" /> <c:out value="${appointment.doctor.lastName}" />
                                                </h1>
                                                <p class="doctor-specialty">
                                                    <spring:message code="${appointment.specialty.key}" />
                                                </p>
                                            </div>
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
                        <button id="edit-profile-btn" class="btn btn-primary">
                            <i class="icon icon-edit"></i>
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
                                    Guardar
                                </button>
                            </div>
                        </form:form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <link rel="stylesheet" href="<c:url value='/css/components/doctor-dashboard.css' />" />
    <link rel="stylesheet" href="<c:url value='/css/components/forms.css' />" />
    <link rel="stylesheet" href="<c:url value='/css/components/inline-form.css' />" />
    <link rel="stylesheet" href="<c:url value='/css/components/multi-select.css' />" />
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

        // Inicializar con la cobertura actual (si existe)
        function initCoverageSelection() {
            const coverageInput = document.getElementById('coverage-input');
            const currentCoverageId = coverageInput.value;

            if (currentCoverageId) {
                const option = document.querySelector(`.custom-multi-select-option[data-value="${currentCoverageId}"]`);
                if (option) {
                    option.classList.add('selected');
                    const coverageName = option.getAttribute('data-name');
                    document.getElementById('current-coverage-name').textContent = coverageName;
                }
            }
        }

        // Llamar al inicializador cuando la página cargue
        document.addEventListener('DOMContentLoaded', function() {
            initCoverageSelection();
            // Resto del código...
        });

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
                const currentCoverageName = document.getElementById('current-coverage-name').textContent;
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
                const currentCoverageName = document.getElementById('current-coverage-name').textContent;
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
            }}
    </script>
</layout:page>
