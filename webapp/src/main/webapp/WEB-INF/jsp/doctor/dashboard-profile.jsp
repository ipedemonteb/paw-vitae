<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="icon" type="image/png" href="https://upload.wikimedia.org/wikipedia/commons/thumb/a/aa/V-logo.svg/2048px-V-logo.svg.png" />
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="dashboard.profile.title" /></title>
    <link rel="stylesheet" href="<c:url value='/css/doctor-dashboard.css' />" />
    <link rel="stylesheet" href="<c:url value='/css/toast-notification.css' />" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
<!-- Include the header -->
<jsp:include page="/WEB-INF/jsp/components/header.jsp" />

<!-- Success Notification Toast -->
<div id="successToast" class="success-toast">
    <div class="success-toast-icon">
        <i class="fas fa-check"></i>
    </div>
    <div class="success-toast-content">
        <div class="success-toast-title"><spring:message code="profile.update.success.title" /></div>
        <div class="success-toast-message"><spring:message code="profile.update.success.message" /></div>
    </div>
    <button class="success-toast-close" onclick="hideSuccessToast()">
        <i class="fas fa-times"></i>
    </button>
</div>

<div class="dashboard-container">
    <!-- Doctor Profile Header -->
    <div class="dashboard-header">
        <div class="doctor-info">
            <div class="doctor-avatar">
                <img src="<c:url value="/image/${doctor.imageId}"/>" alt="<c:out value="${doctor.name} ${doctor.lastName}"/>"/>
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
                                    <spring:message code="${specialty.key}" />
                                </c:otherwise>
                            </c:choose>
                        </span>
                    </c:forEach>
                </div>
            </div>
        </div>
        <div class="dashboard-stats">
            <c:if test="${not empty upcomingAppointments}">
                <div class="stat-item">
                    <div class="stat-value">${upcomingAppointments.size()}</div>
                    <div class="stat-label"><spring:message code="dashboard.stats.upcoming" /></div>
                </div>
            </c:if>
            <c:if test="${not empty pastAppointments}">
                <div class="stat-item">
                    <div class="stat-value">${pastAppointments.size()}</div>
                    <div class="stat-label"><spring:message code="dashboard.stats.past" /></div>
                </div>
            </c:if>
            <div class="stat-item">
                <div class="stat-value">${doctor.specialtyList.size()}</div>
                <div class="stat-label"><spring:message code="dashboard.stats.specialties" /></div>
            </div>
        </div>
    </div>

    <!-- Dashboard Navigation Tabs -->
    <div class="dashboard-nav">
        <a href="<c:url value='/doctor/dashboard/upcoming'/>" class="nav-tab ${activeTab == 'upcoming' ? 'active' : ''}">
            <i class="fas fa-calendar-alt"></i>
            <span><spring:message code="dashboard.tab.upcoming" /></span>
        </a>
        <a href="<c:url value='/doctor/dashboard/history'/>" class="nav-tab ${activeTab == 'history' ? 'active' : ''}">
            <i class="fas fa-history"></i>
            <span><spring:message code="dashboard.tab.history" /></span>
        </a>
        <a href="<c:url value='/doctor/dashboard/profile'/>" class="nav-tab ${activeTab == 'profile' ? 'active' : ''}">
            <i class="fas fa-user-md"></i>
            <span><spring:message code="dashboard.tab.profile" /></span>
        </a>
        <a href="<c:url value='/doctor/dashboard/availability'/>" class="nav-tab ${activeTab == 'availability' ? 'active' : ''}">
            <i class="fas fa-calendar-check"></i>
            <span><spring:message code="dashboard.tab.availability" /></span>
        </a>
    </div>

    <!-- Dashboard Content Area -->
    <div class="dashboard-content">
        <!-- Profile Tab -->
        <div class="tab-content active" id="profile-tab">
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
                <div id="profile-view" class="profile-section" style="display: <c:choose>
                <c:when test="${display == 'none'}">block</c:when>
                <c:otherwise>none</c:otherwise>
                </c:choose>;">
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

                <div id="specialties-section" class="profile-section" style="display: <c:choose>
                <c:when test="${display == 'none'}">block</c:when>
                <c:otherwise>none</c:otherwise>
                </c:choose>;">
                    <h3 class="section-title">
                        <i class="fas fa-stethoscope"></i>
                        <spring:message code="dashboard.profile.specialties" />
                    </h3>
                    <div class="specialties-list" >
                        <c:forEach items="${doctor.specialtyList}" var="specialty">
                            <div class="specialty-item">
                                <c:choose>
                                    <c:when test="${not empty specialty.key}">
                                        <spring:message code="${specialty.key}" />
                                    </c:when>
                                    <c:otherwise>
                                        <spring:message code="${specialty.key}" />
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </c:forEach>
                    </div>
                </div>

                <div id="coverages-section" class="profile-section" style="display: <c:choose>
                <c:when test="${display == 'none'}">block</c:when>
                <c:otherwise>none</c:otherwise>
                </c:choose>;">
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

                <!-- Edit Profile Form (Hidden by default) -->
                <div id="edit-profile-form" class="profile-section" style="display: ${display};">
                    <h3 class="section-title text-center">
                        <i class="fas fa-user-edit"></i>
                        <spring:message code="dashboard.profile.edit" />
                    </h3>

                    <form:form id="updateDoctorForm" modelAttribute="updateDoctorForm" method="post" action="${pageContext.request.contextPath}/doctor/dashboard/update" cssClass="edit-profile-form">
                        <div class="form-row">
                            <div class="form-group">
                                <form:label path="name"><spring:message code="register.firstName" /></form:label>
                                <form:input path="name" cssClass="form-control" value="${doctor.name}" />
                                <form:errors path="name" cssClass="error-message" />
                            </div>

                            <div class="form-group">
                                <form:label path="lastName"><spring:message code="register.lastName" /></form:label>
                                <form:input path="lastName" cssClass="form-control" value="${doctor.lastName}" />
                                <form:errors path="lastName" cssClass="error-message" />
                            </div>
                        </div>

                        <div class="form-group">
                            <form:label path="phone"><spring:message code="register.phone" /></form:label>
                            <form:input path="phone" cssClass="form-control" value="${doctor.phone}" />
                            <form:errors path="phone" cssClass="error-message" />
                        </div>

                        <div class="form-group">
                            <form:label path="coverages"><spring:message code="register.coverage" /></form:label>
                            <div class="current-coverages">
                                <spring:message code="register.coveragesSelected" />:
                                <div id="selected-coverages-display">
                                    <c:forEach items="${doctor.coverageList}" var="coverage" varStatus="status">
                                        <span class="selected-coverage">${coverage.name}${!status.last ? ', ' : ''}</span>
                                    </c:forEach>
                                </div>
                            </div>
                            <div class="multi-select-container" id="coverages-container">
                                <div class="custom-multi-select" id="coverages-options">
                                    <input type="text" id="coverages-search" class="search-box" placeholder="<spring:message code="register.search" />" />
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
                                <!-- Hidden input field for selected coverages -->
                                <form:input path="coverages" id="coverages-input" cssClass="form-control" style="display: none;" />
                            </div>
                            <form:errors path="coverages" cssClass="error-message" />
                        </div>

                        <div class="form-group">
                            <form:label path="specialties"><spring:message code="register.specialties" /></form:label>
                            <div class="current-coverages">
                                <spring:message code="register.selectSpecialties" />:
                                <div id="selected-specialties-display">
                                    <c:forEach items="${doctor.specialtyList}" var="specialty" varStatus="status">
                                        <span class="selected-coverage"><spring:message code="${specialty.key}" />${!status.last ? ', ' : ''}</span>
                                    </c:forEach>
                                </div>
                            </div>
                            <div class="multi-select-container" id="specialties-container">
                                <div class="custom-multi-select" id="specialties-options">
                                    <input type="text" id="specialties-search" class="search-box" placeholder="<spring:message code="register.search" />" />
                                    <c:forEach items="${specialtyList}" var="specialty">
                                        <div class="custom-multi-select-option"
                                             data-value="${specialty.id}"
                                             data-name='<spring:message code="${specialty.key}" />'
                                             Onclick=toggleSpecialty(this)>
                                            <div class="option-checkbox"></div>
                                            <div class="option-text">
                                                <spring:message code="${specialty.key}" />
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                                <!-- Hidden input field for selected coverages -->
                                <form:input path="specialties" id="specialties-input" cssClass="form-control" style="display: none;" />
                            </div>
                            <form:errors path="specialties" cssClass="error-message" />
                        </div>

                        <div class="form-actions">
                            <button type="button" id="cancel-edit-btn" class="btn btn-secondary">
                                <i class="fas fa-times"></i>
                                <spring:message code="logout.confirmation.cancel" />
                            </button>
                            <button type="submit" class="btn btn-primary" onclick="this.disabled = true ; this.form.submit();">
                                <i class="fas fa-save"></i>
                                <spring:message code="appointment.form.save" />
                            </button>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </div>
</div>

<div id="cancelAppointmentModal" class="modal-overlay">
    <div class="modal-container">
        <div class="modal-header">
            <div class="modal-icon">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
                    <path d="M12 22C6.477 22 2 17.523 2 12S6.477 2 12 2s10 4.477 10 10-4.477 10-10 10zm0-2a8 8 0 1 0 0-16 8 8 0 0 0 0 16zm0-9a1 1 0 0 1 1 1v4a1 1 0 0 1-2 0v-4a1 1 0 0 1 1-1zm0-4a1 1 0 1 1 0 2 1 1 0 0 1 0-2z"></path>
                </svg>
            </div>
            <h3 class="modal-title"><spring:message code="appointment.cancel.title" /></h3>
        </div>
        <div class="modal-body">
            <p class="modal-message"><spring:message code="appointment.cancel.message" /></p>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn-modal btn-cancel" onclick="hideCancelModal();">
                <spring:message code="logout.confirmation.cancel"/>
            </button>
            <button type="button" class="btn-modal btn-danger" id="cancelAppointmentBtn">
                <spring:message code="appointment.action.cancel"/>
            </button>
        </div>
    </div>
</div>

<script src="<c:url value='/js/toast-notification.js' />"></script>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Edit Profile Functionality
        const editProfileBtn = document.getElementById('edit-profile-btn');
        const cancelEditBtn = document.getElementById('cancel-edit-btn');
        const profileView = document.getElementById('profile-view');
        const specialtiesSection = document.getElementById('specialties-section');
        const coveragesSection = document.getElementById('coverages-section');
        const editProfileForm = document.getElementById('edit-profile-form');

        updateSelectedCoverages();
        updateSelectedSpecialties();

        if (editProfileBtn) {
            editProfileBtn.addEventListener('click', function() {
                profileView.style.display = 'none';
                specialtiesSection.style.display = 'none';
                coveragesSection.style.display = 'none';
                editProfileForm.style.display = 'block';
            });
        }

        if (cancelEditBtn) {
            cancelEditBtn.addEventListener('click', function() {
                profileView.style.display = 'block';
                specialtiesSection.style.display = 'block';
                coveragesSection.style.display = 'block';
                editProfileForm.style.display = 'none';
            });
        }

        // Initialize coverages dropdown
        initCoveragesDropdown();

        // Initialize search functionality for coverages
        initCoveragesSearch();

        // Pre-select the current coverages
        preSelectCurrentCoverages();

        // Initialize specialties dropdown
        initSpecialtiesDropdown();

        // Initialize search functionality for specialties
        initSpecialtiesSearch();

        // Pre-select the current specialties
        preSelectCurrentSpecialties();

        // Modal functionality
        let currentAppointmentId = null;

        window.showCancelModal = function(appointmentId) {
            currentAppointmentId = appointmentId;
            document.getElementById('cancelAppointmentModal').classList.add('show');
        }

        window.hideCancelModal = function() {
            document.getElementById('cancelAppointmentModal').classList.remove('show');
        }

        // Cancel appointment functionality
        const cancelButtons = document.querySelectorAll('.cancel-appointment');
        cancelButtons.forEach(button => {
            button.addEventListener('click', function(e) {
                e.preventDefault();
                const appointmentId = this.getAttribute('data-id');
                showCancelModal(appointmentId);
            });
        });

        // Cancel button in modal
        const cancelAppointmentBtn = document.getElementById('cancelAppointmentBtn');
        if (cancelAppointmentBtn) {
            cancelAppointmentBtn.addEventListener('click', function() {
                if (currentAppointmentId) {
                    fetch(`${pageContext.request.contextPath}/doctor/dashboard/appointment/cancel`, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        body: new URLSearchParams({ appointmentId: currentAppointmentId })
                    })
                        .then(response => response.json())
                        .then(data => {
                            if (data.success) {
                                window.location.reload();
                            }
                        });
                }
                hideCancelModal();
            });
        }

        // Cerrar modales al hacer clic fuera
        document.querySelectorAll('.modal-overlay').forEach(modal => {
            modal.addEventListener('click', function(e) {
                if (e.target === this) {
                    this.classList.remove('show');
                }
            });
        });

        // Cerrar modales con tecla Escape
        document.addEventListener('keydown', function(e) {
            if (e.key === 'Escape') {
                document.querySelectorAll('.modal-overlay').forEach(modal => {
                    modal.classList.remove('show');
                });
            }
        });
    });

    // Funciones para el manejo de coverages
    function initCoveragesDropdown() {
        const coverageOptions = document.querySelectorAll('#coverages-options .custom-multi-select-option');
        const coveragesInput = document.getElementById('coverages-input');

        // Set initial values from the input
        updateSelectedCoverages();
    }

    function toggleCoverage(optionElement) {
        // Toggle selection state
        optionElement.classList.toggle('selected');

        // Update the hidden input with all selected coverage IDs
        updateSelectedCoverages();
    }

    function updateSelectedCoverages() {
        const selectedOptions = document.querySelectorAll('#coverages-options .custom-multi-select-option.selected');
        const coveragesInput = document.getElementById('coverages-input');
        const selectedCoveragesDisplay = document.getElementById('selected-coverages-display');

        // Create an array of selected coverage IDs
        const selectedIds = Array.from(selectedOptions).map(option => option.getAttribute('data-value'));

        // Update the hidden input value with comma-separated IDs
        coveragesInput.value = selectedIds.join(',');

        // Update the display of selected coverages
        const selectedNames = Array.from(selectedOptions).map(option => option.getAttribute('data-name'));
        selectedCoveragesDisplay.innerHTML = selectedNames.length > 0
            ? selectedNames.join(', ')
            : '<em>None selected</em>';
    }

    function initCoveragesSearch() {
        const searchBox = document.getElementById('coverages-search');
        const options = document.querySelectorAll('#coverages-options .custom-multi-select-option');

        if (searchBox) {
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
    }

    function preSelectCurrentCoverages() {
        // Get the current doctor's coverages from the display
        const currentCoverages = document.querySelectorAll('.coverages-list .coverage-item');
        const coverageNames = Array.from(currentCoverages).map(item => item.textContent.trim());

        // Find and select matching options in the dropdown
        const options = document.querySelectorAll('#coverages-options .custom-multi-select-option');

        options.forEach(option => {
            const name = option.getAttribute('data-name');
            if (coverageNames.includes(name)) {
                option.classList.add('selected');
            }
        });

        // Update the hidden input
        updateSelectedCoverages();
    }

    // Funciones para el manejo de specialties
    function initSpecialtiesDropdown() {
        const specialtyOptions = document.querySelectorAll('#specialties-options .custom-multi-select-option');
        const specialtiesInput = document.getElementById('specialties-input');

        // Set initial values from the input
        updateSelectedSpecialties();
    }

    function toggleSpecialty(optionElement) {
        // Toggle selection state
        optionElement.classList.toggle('selected');

        // Update the hidden input with all selected specialty IDs
        updateSelectedSpecialties();
    }

    function updateSelectedSpecialties() {
        const selectedOptions = document.querySelectorAll('#specialties-options .custom-multi-select-option.selected');
        const specialtiesInput = document.getElementById('specialties-input');
        const selectedSpecialtiesDisplay = document.getElementById('selected-specialties-display');

        // Create an array of selected specialty IDs
        const selectedIds = Array.from(selectedOptions).map(option => option.getAttribute('data-value'));

        // Update the hidden input value with comma-separated IDs
        specialtiesInput.value = selectedIds.join(',');

        // Update the display of selected specialties
        const selectedNames = Array.from(selectedOptions).map(option => option.getAttribute('data-name'));
        selectedSpecialtiesDisplay.innerHTML = selectedNames.length > 0
            ? selectedNames.join(', ')
            : '<em>None selected</em>';
    }

    function initSpecialtiesSearch() {
        const searchBox = document.getElementById('specialties-search');
        const options = document.querySelectorAll('#specialties-options .custom-multi-select-option');

        if (searchBox) {
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
    }

    const fixedHeader = document.querySelector(".main-header");
    const mainContent = document.querySelector(".dashboard-container");

    if (fixedHeader && mainContent) {
        const adjustContentMargin = () => {
            const headerHeight = fixedHeader.offsetHeight;
            mainContent.style.marginTop = (headerHeight * 1.25) + `px`;
        };

        // Adjust on page load
        adjustContentMargin();

        // Adjust on window resize
        window.addEventListener("resize", adjustContentMargin);
    }

    function preSelectCurrentSpecialties() {
        // Get the current doctor's specialties from the display
        const currentSpecialties = document.querySelectorAll('.specialties-list .specialty-item');
        const specialtyNames = Array.from(currentSpecialties).map(item => item.textContent.trim());

        // Find and select matching options in the dropdown
        const options = document.querySelectorAll('#specialties-options .custom-multi-select-option');

        options.forEach(option => {
            const name = option.getAttribute('data-name');
            if (specialtyNames.includes(name)) {
                option.classList.add('selected');
            }
        });

        // Update the hidden input
        updateSelectedSpecialties();
    }
</script>
</body>
</html>