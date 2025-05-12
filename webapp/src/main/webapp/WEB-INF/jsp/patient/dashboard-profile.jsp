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
    <link rel="stylesheet" href="<c:url value='/css/dashboard-patient.css' />" />
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

<main class="dashboard-container">
    <!-- Include the dashboard header component -->
    <c:set var="activeTab" value="profile" scope="request" />
    <c:set var="user" value="${patient}" scope="request"/>
    <c:set var="isDoctor" value="${false}" scope="request"/>
    <jsp:include page="/WEB-INF/jsp/components/dashboard-header.jsp"/>

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

                <div id="coverage-section" class="profile-section" style="display: <c:choose>
                <c:when test="${display == 'none'}">block</c:when>
                <c:otherwise>none</c:otherwise>
                </c:choose>;">
                    <h3 class="section-title"><spring:message code="dashboard.profile.coverages" /></h3>
                    <div class="coverages-list">
                        <a href="${pageContext.request.contextPath}/search?coverage=${patient.coverage.id}&page=1" class="coverage-item-link">
                            <div class="coverage-item">
                                <c:out value="${patient.coverage.name}" />
                                <i class="fas fa-search" style="margin-left: 8px;"></i>
                            </div>
                        </a>
                    </div>
                </div>

                <!-- Edit Profile Form (Hidden by default) -->
                <div id="edit-profile-form" class="profile-section" style="display: ${display};">
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
                            <button type="submit" class="btn btn-primary" onclick="this.disabled = true ; this.form.submit();">
                                <spring:message code="appointment.form.save" />
                            </button>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </div>
</main>

<!-- Include the toast notification script -->
<script src="<c:url value='/js/toast-notification.js' />"></script>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Edit Profile Functionality
        const editProfileBtn = document.getElementById('edit-profile-btn');
        const cancelEditBtn = document.getElementById('cancel-edit-btn');
        const profileView = document.getElementById('profile-view');
        const coverageSection = document.getElementById('coverage-section');
        const editProfileForm = document.getElementById('edit-profile-form');

        updateSelectedCoverage();

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
</body>
</html>