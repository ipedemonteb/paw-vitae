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
    <link rel="stylesheet" href="<c:url value='/css/profile-image-upload.css' />" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
<!-- Include the header -->
<jsp:include page="/WEB-INF/jsp/components/header.jsp">
    <jsp:param name="id" value="${doctor.imageId}" />
</jsp:include>

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

<main class="dashboard-container">
    <!-- Doctor Profile Header -->
    <div class="dashboard-header">
        <div class="doctor-info">
            <div class="doctor-avatar">
                <img id="doctor-profile-image" src="<c:url value="/image/${doctor.imageId}"/>" alt="<c:out value="${doctor.name} ${doctor.lastName}"/>"/>
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
                <c:if test="${doctor.ratingCount > 0}">
                    <div class="doctor-rating">
                        <div class="rating-stars">
                            <c:forEach begin="1" end="5" var="i">
                                <c:choose>
                                    <c:when test="${doctor.rating >= i}">
                                        <i class="fas fa-star"></i>
                                    </c:when>
                                    <c:when test="${doctor.rating >= i - 0.5}">
                                        <i class="fas fa-star-half-alt"></i>
                                    </c:when>
                                    <c:otherwise>
                                        <i class="far fa-star"></i>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </div>
                        <div class="rating-value">
                            <fmt:formatNumber value="${doctor.rating}" pattern="#.#" /> <span class="rating-count">(${doctor.ratingCount})</span>
                        </div>
                    </div>
                </c:if>
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
        <a href="<c:url value='/doctor/dashboard/upcoming'/>" class="nav-tab ">
            <i class="fas fa-calendar-alt"></i>
            <span><spring:message code="dashboard.tab.upcoming" /></span>
        </a>
        <a href="<c:url value='/doctor/dashboard/history'/>" class="nav-tab ">
            <i class="fas fa-history"></i>
            <span><spring:message code="dashboard.tab.history" /></span>
        </a>
        <a href="<c:url value='/doctor/dashboard/profile'/>" class="nav-tab active ">
            <i class="fas fa-user-md"></i>
            <span><spring:message code="dashboard.tab.profile" /></span>
        </a>
        <a href="<c:url value='/doctor/dashboard/availability'/>" class="nav-tab ">
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

                    <form:form id="updateDoctorForm" modelAttribute="updateDoctorForm" method="post" action="${pageContext.request.contextPath}/doctor/dashboard/update" cssClass="edit-profile-form" enctype="multipart/form-data">
                        <!-- Imagen de perfil -->
                    <div class="form-group">
                        <label class="form-label"><spring:message code="register.profileImage" /></label>
                        <div class="image-upload-container">
                            <div class="image-upload-area" id="image-upload-area">
                                <i class="fas fa-cloud-upload-alt"></i>
                                <span><spring:message code="register.chooseImage" /></span>
                            </div>
                            <form:input path="image" id="image" type="file" class="file-input" accept="image/jpeg,image/png,image/jpg" />
                            <div id="image-preview-container" class="image-preview-container" style="display: none;">
                                <div class="image-preview-content">
                                    <div class="image-preview-thumbnail">
                                        <img id="image-preview" src="/placeholder.svg" alt="Vista previa">
                                    </div>
                                    <div class="image-preview-info">
                                        <div id="image-preview-name" class="image-preview-name"></div>
                                        <div id="image-preview-details" class="image-preview-details"></div>
                                    </div>
                                    <button type="button" id="remove-image" class="remove-image-btn" aria-label="Eliminar imagen">
                                        <i class="fas fa-times"></i>
                                    </button>
                                </div>
                            </div>
                            <form:errors path="image" cssClass="error-message" id="image-error" />
                        </div>
                        <div class="text-muted">
                            <spring:message code="register.imageRequirements" />
                        </div>
                    </div>




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
                                <form:hidden path="coverages" id="coverages-input" />
                            </div>
                            <div class="error-container">
                                <form:errors path="coverages" cssClass="error-message" element="div" />
                            </div>
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
                                    <c:forEach items="${specialtyList}" var="specialty">
                                        <div class="custom-multi-select-option"
                                             data-value="${specialty.id}"
                                             data-name='<spring:message code="${specialty.key}" />'
                                             onclick="toggleSpecialty(this)">
                                            <div class="option-checkbox"></div>
                                            <div class="option-text">
                                                <spring:message code="${specialty.key}" />
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                                <form:hidden path="specialties" id="specialties-input" />
                            </div>
                            <div class="error-container">
                                <form:errors path="specialties" cssClass="error-message" element="div" />
                            </div>
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
</main>

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
<script src="<c:url value='/js/profile-image-upload.js' />"></script>

<script>
    document.addEventListener('DOMContentLoaded', function() {
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

        initCoveragesDropdown();

        preSelectCurrentCoverages();

        initSpecialtiesDropdown();
        preSelectCurrentSpecialties();

    });

    function initCoveragesDropdown() {
        const coverageOptions = document.querySelectorAll('#coverages-options .custom-multi-select-option');
        const coveragesInput = document.getElementById('coverages-input');
        updateSelectedCoverages();
    }

    function toggleCoverage(optionElement) {
        optionElement.classList.toggle('selected');
        updateSelectedCoverages();
    }

    function updateSelectedCoverages() {
        const selectedOptions = document.querySelectorAll('#coverages-options .custom-multi-select-option.selected');
        const coveragesInput = document.getElementById('coverages-input');
        const selectedCoveragesDisplay = document.getElementById('selected-coverages-display');

        // Remove any existing hidden inputs for coverages
        document.querySelectorAll('input[name^="coverages["]').forEach(el => el.remove());

        // Create an array of selected coverage IDs
        const selectedIds = Array.from(selectedOptions).map(option => option.getAttribute('data-value'));

        selectedIds.forEach((id, index) => {
            const hiddenInput = document.createElement('input');
            hiddenInput.type = 'hidden';
            hiddenInput.name = 'coverages[' + index + ']';
            hiddenInput.value = id;
            document.getElementById('updateDoctorForm').appendChild(hiddenInput);
        });

        coveragesInput.value = '';

        const selectedNames = Array.from(selectedOptions).map(option => option.getAttribute('data-name'));
        selectedCoveragesDisplay.innerHTML = selectedNames.length > 0
            ? selectedNames.join(', ')
            : '';
    }

    function preSelectCurrentCoverages() {

        const currentCoverages = document.querySelectorAll('.coverages-list .coverage-item');
        const coverageNames = Array.from(currentCoverages).map(item => item.textContent.trim());

        const options = document.querySelectorAll('#coverages-options .custom-multi-select-option');

        options.forEach(option => {
            const name = option.getAttribute('data-name');
            if (coverageNames.includes(name)) {
                option.classList.add('selected');
            }
        });
        updateSelectedCoverages();
    }

    function initSpecialtiesDropdown() {
        const specialtyOptions = document.querySelectorAll('#specialties-options .custom-multi-select-option');
        const specialtiesInput = document.getElementById('specialties-input');
        updateSelectedSpecialties();
    }

    function toggleSpecialty(optionElement) {
        optionElement.classList.toggle('selected');
        updateSelectedSpecialties();
    }

    function updateSelectedSpecialties() {
        const selectedOptions = document.querySelectorAll('#specialties-options .custom-multi-select-option.selected');
        const specialtiesInput = document.getElementById('specialties-input');
        const selectedSpecialtiesDisplay = document.getElementById('selected-specialties-display');
        document.querySelectorAll('input[name^="specialties["]').forEach(el => el.remove());
        const selectedIds = Array.from(selectedOptions).map(option => option.getAttribute('data-value'));
        selectedIds.forEach((id, index) => {
            const hiddenInput = document.createElement('input');
            hiddenInput.type = 'hidden';
            hiddenInput.name = 'specialties[' + index + ']';
            hiddenInput.value = id;
            document.getElementById('updateDoctorForm').appendChild(hiddenInput);
        });


        specialtiesInput.value = '';

        const selectedNames = Array.from(selectedOptions).map(option => option.getAttribute('data-name'));
        selectedSpecialtiesDisplay.innerHTML = selectedNames.length > 0
            ? selectedNames.join(', ')
            : '';
    }

    function preSelectCurrentSpecialties() {
        const currentSpecialties = document.querySelectorAll('.specialties-list .specialty-item');
        const specialtyNames = Array.from(currentSpecialties).map(item => item.textContent.trim());

        const options = document.querySelectorAll('#specialties-options .custom-multi-select-option');
        options.forEach(option => {
            const name = option.getAttribute('data-name');
            if (specialtyNames.includes(name)) {
                option.classList.add('selected');
            }
        });
        updateSelectedSpecialties();
    }
</script>
</body>
</html>
