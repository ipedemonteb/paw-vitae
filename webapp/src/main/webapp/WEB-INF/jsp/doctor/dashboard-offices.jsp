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
    <title><spring:message code="dashboard.tab.offices" /></title>
    <link rel="stylesheet" href="<c:url value='/css/doctor-dashboard.css' />" />
    <link rel="stylesheet" href="<c:url value='/css/dashboard-offices.css' />" />
    <link rel="stylesheet" href="<c:url value='/css/toast-notification.css' />" />
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
        <div class="success-toast-title"><spring:message code="offices.update.success.title" /></div>
        <div class="success-toast-message"><spring:message code="offices.update.success.message" /></div>
    </div>
    <button class="success-toast-close" onclick="hideSuccessToast()">
        <i class="fas fa-times"></i>
    </button>
</div>

<main class="dashboard-container">
    <c:set var="activeTab" value="offices" scope="request" />
    <c:set var="user" value="${doctor}" scope="request"/>
    <c:set var="isDoctor" value="${true}" scope="request"/>
    <jsp:include page="/WEB-INF/jsp/components/dashboard-header.jsp"/>

    <!-- Dashboard Content Area -->
    <div class="dashboard-content">
        <!-- Offices Tab -->
        <div class="tab-content active" id="offices-tab">
            <div class="tab-header">
                <h2>
                    <i class="fas fa-building"></i>
                    <spring:message code="dashboard.tab.offices" />
                </h2>
                <div class="tab-actions">
                    <button id="edit-offices-btn" class="btn btn-primary" onclick="showOfficesForm()">
                        <i class="fas fa-edit"></i>
                        <spring:message code="offices.manage" />
                    </button>
                </div>
            </div>

            <!-- Offices Display -->
            <div id="offices-display" class="offices-content">
                <c:choose>
                    <c:when test="${empty doctorOffices}">
                        <div class="empty-state">
                            <div class="empty-icon">
                                <i class="fas fa-building" style="font-size: 4rem;"></i>
                            </div>
                            <h3><spring:message code="offices.empty.title" /></h3>
                            <p><spring:message code="offices.empty.message" /></p>
                            <button class="btn btn-primary" onclick="showOfficesForm()" style="margin-top: 1rem;">
                                <i class="fas fa-plus"></i>
                                <spring:message code="offices.add.first" />
                            </button>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="offices-grid">
                            <c:forEach items="${doctorOffices}" var="office" varStatus="status">
                                <div class="office-card">
                                    <div class="office-card-header">
                                        <div class="office-info">
                                            <h3 class="office-name">
                                                <i class="fas fa-building"></i>
                                                <c:out value="${office.officeName}" />
                                            </h3>
                                            <div class="office-location">
                                                <i class="fas fa-map-marker-alt"></i>
                                                <c:out value="${office.neighborhood.name}" />
                                            </div>
                                        </div>
                                    </div>
                                    <div class="office-specialties">
                                        <div class="specialties-label">
                                            <i class="fas fa-stethoscope"></i>
                                            <spring:message code="offices.specialties" />:
                                        </div>
                                        <div class="specialties-list">
                                            <c:forEach items="${office.specialties}" var="specialty">
                                                <span class="specialty-tag">
                                                    <spring:message code="${specialty.key}" />
                                                </span>
                                            </c:forEach>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Offices Management Form -->
            <div id="offices-form-container" class="profile-section" style="display: none;">
                <h3 class="section-title text-center">
                    <i class="fas fa-building"></i>
                    <spring:message code="offices.manage.title" />
                </h3>

                <form:form id="officesForm" modelAttribute="doctorOfficeForm" method="post"
                           action="${pageContext.request.contextPath}/doctor/dashboard/offices/update"
                           cssClass="edit-profile-form">

                    <div class="form-group">
                        <label class="form-label required-field">
                            <spring:message code="register.doctorOffices" />
                        </label>
                        <div id="doctor-offices-container">
                            <!-- Offices will be rendered here by JavaScript -->
                        </div>

                        <form:errors path="doctorOfficeForm" cssClass="error-message visible"/>

                        <button type="button" class="btn-add-slot" id="add-office-btn" onclick="addNewOffice()">
                            <i class="fas fa-plus"></i>
                            <spring:message code="register.addOffice"/>
                        </button>
                    </div>

                    <div class="form-actions">
                        <button type="button" id="cancel-offices-btn" class="btn btn-secondary" onclick="hideOfficesForm()">
                            <i class="fas fa-times"></i>
                            <spring:message code="logout.confirmation.cancel" />
                        </button>
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save"></i>
                            <spring:message code="appointment.form.save" />
                        </button>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</main>

<script src="<c:url value='/js/toast-notification.js' />"></script>

<script type="text/javascript">
    // Global variables
    let officeCounter = 0;
    let doctorOffices = [];

    // Track if fields have been touched for validation
    const officeNameTouched = {};
    const officeNeighborhoodTouched = {};
    const officeSpecialtiesTouched = {};

    // Initialize data from JSP
    const neighborhoods = [
        <c:forEach items="${neighborhoodList}" var="neighborhood" varStatus="status">
        {
            id: "${neighborhood.id}",
            name: "${neighborhood.name}"
        }<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    ];

    const specialtyList = [
        <c:forEach items="${specialtyList}" var="specialty" varStatus="status">
        {
            id: ${specialty.id},
            name: "<spring:message code="${specialty.key}" />"
        }<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    ];

    // Get doctor's specialties to filter office specialties
    const doctorSpecialties = [
        <c:forEach items="${doctor.specialtyList}" var="specialty" varStatus="status">
        ${specialty.id}<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    ];

    // Existing offices data
    <c:if test="${not empty doctorOffices}">
    const existingOffices = [
        <c:forEach items="${doctorOffices}" var="office" varStatus="status">
        {
            index: ${status.index},
            name: "<c:out value='${office.officeName}'/>",
            neighborhoodId: ${office.neighborhood.id},
            specialties: [
                <c:forEach items="${office.specialties}" var="specialty" varStatus="sidStatus">
                {
                    id: ${specialty.id}
                }
                <c:if test="${!sidStatus.last}">,</c:if>
                </c:forEach>
            ]
        }<c:if test="${!status.last}">, </c:if>
        </c:forEach>
    ];
    </c:if>

    // Simple functions using onclick
    function showOfficesForm() {
        document.getElementById('offices-display').style.display = 'none';
        document.getElementById('offices-form-container').style.display = 'block';
        document.querySelector('.tab-header').style.display = 'none';

        // Initialize offices if not done yet
        if (document.getElementById('doctor-offices-container').children.length === 0) {
            if (typeof existingOffices !== 'undefined' && existingOffices.length > 0) {
                renderExistingOffices();
            } else {
                addNewOffice();
            }
        }
    }

    function hideOfficesForm() {
        document.getElementById('offices-display').style.display = 'block';
        document.getElementById('offices-form-container').style.display = 'none';
        document.querySelector('.tab-header').style.display = 'flex';
    }

    function addNewOffice() {
        const container = document.getElementById("doctor-offices-container");
        if (!container) return;

        const officeEntry = document.createElement("div");
        officeEntry.className = "office-entry";
        officeEntry.setAttribute('data-index', officeCounter.toString());

        officeEntry.innerHTML = createOfficeHTML(officeCounter);

        doctorOffices.push({
            index: officeCounter,
            name: "",
            neighborhoodId: "",
            specialties: []
        });

        container.appendChild(officeEntry);
        initializeOffice(officeCounter);
        updateRemoveButtons();
        officeCounter++;
    }

    function createOfficeHTML(index, name, neighborhoodId, id) {
        name = name || '';
        id = id || '';
        neighborhoodId = neighborhoodId || '';

        let neighborhoodName = '';
        if (neighborhoodId) {
            for (let i = 0; i < neighborhoods.length; i++) {
                if (neighborhoods[i].id.toString() === neighborhoodId.toString()) {
                    neighborhoodName = neighborhoods[i].name;
                    break;
                }
            }
        }

        let html = '<div class="form-row">';

        // Office name with validation
        html += '<div class="form-group">';
        html += '<label for="office-name-' + index + '" class="form-label required-field">Nombre del Consultorio</label>';
        html += '<div class="input-wrapper">';
        html += '<input type="text" id="office-name-' + index + '" class="form-control office-name" ';
        html += 'placeholder="Ingrese el nombre del consultorio" value="' + name + '" required/>';
        html += '<div class="validation-icon valid"><i class="fas fa-check-circle"></i></div>';
        html += '<div class="validation-icon error"><i class="fas fa-exclamation-circle"></i></div>';
        html += '</div>';
        html += '<div id="office-name-' + index + '-validation-message" class="error-message"></div>';
        html += '</div>';

        // Neighborhood with validation
        html += '<div class="form-group">';
        html += '<label for="office-neighborhood-search-' + index + '" class="form-label required-field">Barrio</label>';
        html += '<div class="input-wrapper neighborhood-search-wrapper">';
        html += '<input type="text" id="office-neighborhood-search-' + index + '" class="form-control office-neighborhood-search" ';
        html += 'placeholder="Buscar barrio" value="' + neighborhoodName + '" autocomplete="off" required/>';
        html += '<input type="hidden" id="office-neighborhood-id-' + index + '" class="office-neighborhood-id" value="' + neighborhoodId + '"/>';
        html += '<div class="validation-icon valid"><i class="fas fa-check-circle"></i></div>';
        html += '<div class="validation-icon error"><i class="fas fa-exclamation-circle"></i></div>';
        html += '</div>';
        html += '<div id="office-neighborhood-results-' + index + '" class="search-results-container"></div>';
        html += '<div id="office-neighborhood-' + index + '-validation-message" class="error-message"></div>';
        html += '</div>';

        html += '</div>';

        // Specialties with validation - only show doctor's specialties
        html += '<div class="form-group">';
        html += '<label class="form-label required-field">Especialidades del Consultorio</label>';
        html += '<div class="custom-multi-select" id="office-specialties-container-' + index + '">';
        html += generateSpecialtyOptions(index);
        html += '</div>';
        html += '<div class="selected-options" id="office-specialties-selected-' + index + '"></div>';
        html += '<small class="form-text text-muted">Seleccione las especialidades para este consultorio</small>';
        html += '<div id="office-specialties-' + index + '-validation-message" class="error-message"></div>';
        html += '</div>';

        html += '<div class="office-actions">';
        html += '<button type="button" class="btn-remove office-remove" onclick="removeOffice(' + index + ')">';
        html += '<i class="fas fa-times"></i>';
        html += '</button>';
        html += '</div>';

        return html;
    }

    function generateSpecialtyOptions(officeIndex) {
        let html = '';

        // Filter specialties to only show doctor's specialties
        const filteredSpecialties = specialtyList.filter(specialty =>
            doctorSpecialties.includes(specialty.id)
        );

        for (let i = 0; i < filteredSpecialties.length; i++) {
            const specialty = filteredSpecialties[i];
            let isSelected = false;

            // Check if this specialty is selected for this office
            let officeArrayIndex = -1;
            for (let j = 0; j < doctorOffices.length; j++) {
                if (doctorOffices[j].index === parseInt(officeIndex)) {
                    officeArrayIndex = j;
                    break;
                }
            }

            if (officeArrayIndex !== -1 && doctorOffices[officeArrayIndex].specialties) {
                for (let k = 0; k < doctorOffices[officeArrayIndex].specialties.length; k++) {
                    if (doctorOffices[officeArrayIndex].specialties[k].toString() === specialty.id.toString()) {
                        isSelected = true;
                        break;
                    }
                }
            }

            html += '<div class="custom-multi-select-option' + (isSelected ? ' selected' : '') + '" data-value="' + specialty.id + '" onclick="toggleOfficeSpecialtyOption(this, ' + officeIndex + ')">';
            html += '<div class="option-checkbox"></div>';
            html += '<span class="option-text">' + specialty.name + '</span>';
            html += '</div>';
        }

        return html;
    }

    function toggleOfficeSpecialtyOption(element, officeIndex) {
        const specialtyId = element.getAttribute('data-value');
        const isSelected = element.classList.contains('selected');

        if (!officeSpecialtiesTouched[officeIndex]) {
            officeSpecialtiesTouched[officeIndex] = true;
        }

        // Toggle selection
        if (isSelected) {
            element.classList.remove('selected');
        } else {
            element.classList.add('selected');
        }

        // Update doctorOffices array
        for (let i = 0; i < doctorOffices.length; i++) {
            if (doctorOffices[i].index === parseInt(officeIndex)) {
                if (!isSelected) {
                    // Add specialty
                    let found = false;
                    for (let j = 0; j < doctorOffices[i].specialties.length; j++) {
                        if (doctorOffices[i].specialties[j] === specialtyId) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        doctorOffices[i].specialties.push(specialtyId);
                    }
                } else {
                    // Remove specialty
                    for (let j = 0; j < doctorOffices[i].specialties.length; j++) {
                        if (doctorOffices[i].specialties[j] === specialtyId) {
                            doctorOffices[i].specialties.splice(j, 1);
                            break;
                        }
                    }
                }
                break;
            }
        }

        updateOfficeSpecialtiesDisplay(officeIndex);
        validateOfficeSpecialties(officeIndex);
    }

    function initializeOffice(index) {
        const searchInput = document.getElementById('office-neighborhood-search-' + index);
        if (searchInput) {
            searchInput.addEventListener("input", function() {
                if (!officeNeighborhoodTouched[index]) officeNeighborhoodTouched[index] = true;
                searchOfficeNeighborhoods(index, this.value);
                validateOfficeNeighborhood(index);
            });
            searchInput.addEventListener("focus", function() {
                searchOfficeNeighborhoods(index, this.value);
            });
            searchInput.addEventListener("blur", function() {
                setTimeout(function() {
                    const resultsContainer = document.getElementById('office-neighborhood-results-' + index);
                    if (resultsContainer) {
                        resultsContainer.style.display = "none";
                    }
                    validateOfficeNeighborhood(index);
                }, 200);
            });
        }

        const nameInput = document.getElementById('office-name-' + index);
        if (nameInput) {
            nameInput.addEventListener("input", function() {
                if (!officeNameTouched[index]) officeNameTouched[index] = true;
                for (let i = 0; i < doctorOffices.length; i++) {
                    if (doctorOffices[i].index === parseInt(index)) {
                        doctorOffices[i].name = this.value;
                        break;
                    }
                }
                validateOfficeName(index);
            });
            nameInput.addEventListener("blur", function() {
                validateOfficeName(index);
            });
        }
    }

    // Validation functions
    function validateOfficeName(index) {
        const nameField = document.getElementById('office-name-' + index);
        const errorElement = document.getElementById('office-name-' + index + '-validation-message');

        if (!nameField) return false;

        const value = nameField.value.trim();

        if (!value) {
            if (officeNameTouched[index]) {
                setFieldError(nameField, errorElement, "Office name is required");
            } else {
                clearFieldValidation(nameField, errorElement);
            }
            return false;
        } else if (value.length < 2) {
            if (officeNameTouched[index]) {
                setFieldError(nameField, errorElement, "Name must be at least 2 characters");
            } else {
                clearFieldValidation(nameField, errorElement);
            }
            return false;
        } else {
            setFieldValid(nameField, errorElement);
            return true;
        }
    }

    function validateOfficeNeighborhood(index) {
        const searchField = document.getElementById('office-neighborhood-search-' + index);
        const idField = document.getElementById('office-neighborhood-id-' + index);
        const errorElement = document.getElementById('office-neighborhood-' + index + '-validation-message');

        if (!searchField || !idField) return false;

        const neighborhood = neighborhoods.find(function(n) {
            return n.name.toLowerCase() === searchField.value.toLowerCase();
        });

        if (!neighborhood) {
            if (officeNeighborhoodTouched[index]) {
                setFieldError(searchField, errorElement, "Please select a valid neighborhood");
            } else {
                clearFieldValidation(searchField, errorElement);
            }
            return false;
        } else {
            idField.value = neighborhood.id;
            for (let i = 0; i < doctorOffices.length; i++) {
                if (doctorOffices[i].index === parseInt(index)) {
                    doctorOffices[i].neighborhoodId = neighborhood.id;
                    break;
                }
            }
            setFieldValid(searchField, errorElement);
            return true;
        }
    }

    function validateOfficeSpecialties(index) {
        const container = document.getElementById('office-specialties-container-' + index);
        const errorElement = document.getElementById('office-specialties-' + index + '-validation-message');

        let officeArrayIndex = -1;
        for (let i = 0; i < doctorOffices.length; i++) {
            if (doctorOffices[i].index === parseInt(index)) {
                officeArrayIndex = i;
                break;
            }
        }

        if (officeArrayIndex === -1) return false;

        const selectedSpecialties = doctorOffices[officeArrayIndex].specialties || [];

        if (selectedSpecialties.length === 0) {
            if (officeSpecialtiesTouched[index]) {
                setContainerError(container, errorElement, "Please select at least one specialty for this office");
            } else {
                clearContainerValidation(container, errorElement);
            }
            return false;
        } else {
            setContainerValid(container, errorElement);
            return true;
        }
    }

    // Validation helper functions
    function setFieldError(field, errorElement, message) {
        if (!field) return;
        field.classList.add("error");
        field.classList.remove("valid");
        if (errorElement) {
            errorElement.textContent = message;
            errorElement.classList.add("visible");
        }
    }

    function setFieldValid(field, errorElement) {
        if (!field) return;
        field.classList.remove("error");
        field.classList.add("valid");
        if (errorElement) {
            errorElement.classList.remove("visible");
        }
    }

    function clearFieldValidation(field, errorElement) {
        if (!field) return;
        field.classList.remove("error");
        field.classList.remove("valid");
        if (errorElement) {
            errorElement.classList.remove("visible");
        }
    }

    function setContainerError(container, errorElement, message) {
        if (!container) return;
        container.classList.add("error");
        container.classList.remove("valid");
        if (errorElement) {
            errorElement.textContent = message;
            errorElement.classList.add("visible");
        }
    }

    function setContainerValid(container, errorElement) {
        if (!container) return;
        container.classList.remove("error");
        container.classList.add("valid");
        if (errorElement) {
            errorElement.classList.remove("visible");
        }
    }

    function clearContainerValidation(container, errorElement) {
        if (!container) return;
        container.classList.remove("error");
        container.classList.remove("valid");
        if (errorElement) {
            errorElement.classList.remove("visible");
        }
    }

    function searchOfficeNeighborhoods(index, query) {
        const resultsContainer = document.getElementById('office-neighborhood-results-' + index);
        if (!resultsContainer) return;

        resultsContainer.innerHTML = "";
        resultsContainer.style.display = "block";

        const searchQuery = query.toLowerCase();

        for (let i = 0; i < neighborhoods.length; i++) {
            const neighborhood = neighborhoods[i];
            if (query.trim().length === 0 || neighborhood.name.toLowerCase().indexOf(searchQuery) !== -1) {
                const item = document.createElement("div");
                item.className = "search-result-item";
                item.innerHTML = '<i class="fas fa-map-marker-alt"></i>' + neighborhood.name;
                item.setAttribute('data-id', neighborhood.id);

                (function(neighId, neighName) {
                    item.addEventListener("click", function() {
                        selectOfficeNeighborhood(index, neighId, neighName);
                    });
                })(neighborhood.id, neighborhood.name);

                resultsContainer.appendChild(item);
            }
        }
    }

    function selectOfficeNeighborhood(index, id, name) {
        const searchField = document.getElementById('office-neighborhood-search-' + index);
        const idField = document.getElementById('office-neighborhood-id-' + index);
        const resultsContainer = document.getElementById('office-neighborhood-results-' + index);

        if (searchField && idField) {
            searchField.value = name;
            idField.value = id;

            for (let i = 0; i < doctorOffices.length; i++) {
                if (doctorOffices[i].index === parseInt(index)) {
                    doctorOffices[i].neighborhoodId = id;
                    break;
                }
            }
        }

        if (resultsContainer) {
            resultsContainer.style.display = "none";
        }

        validateOfficeNeighborhood(index);
    }

    function updateOfficeSpecialtiesDisplay(officeIndex) {
        const selectedContainer = document.getElementById('office-specialties-selected-' + officeIndex);
        if (!selectedContainer) return;

        selectedContainer.innerHTML = "";

        let officeArrayIndex = -1;
        for (let i = 0; i < doctorOffices.length; i++) {
            if (doctorOffices[i].index.toString() === officeIndex.toString()) {
                officeArrayIndex = i;
                break;
            }
        }

        if (officeArrayIndex === -1) return;

        const selectedSpecialties = doctorOffices[officeArrayIndex].specialties || [];

        for (let i = 0; i < selectedSpecialties.length; i++) {
            const specialtyId = selectedSpecialties[i];
            let specialtyName = 'Specialty ' + specialtyId;

            for (let j = 0; j < specialtyList.length; j++) {
                if (specialtyList[j].id.toString() === specialtyId.toString()) {
                    specialtyName = specialtyList[j].name;
                    break;
                }
            }

            const badge = document.createElement("span");
            badge.className = "specialty-tag";
            badge.textContent = specialtyName;
            selectedContainer.appendChild(badge);
        }
    }

    function removeOffice(index) {
        const officeEntry = document.querySelector('.office-entry[data-index="' + index + '"]');
        if (!officeEntry) return;

        officeEntry.parentNode.removeChild(officeEntry);

        for (let i = 0; i < doctorOffices.length; i++) {
            if (doctorOffices[i].index === parseInt(index)) {
                doctorOffices.splice(i, 1);
                break;
            }
        }

        updateRemoveButtons();
    }

    function updateRemoveButtons() {
        const offices = document.querySelectorAll(".office-entry");
        const removeButtons = document.querySelectorAll(".office-remove");

        for (let i = 0; i < removeButtons.length; i++) {
            removeButtons[i].style.display = offices.length > 1 ? "block" : "none";
        }
    }

    function renderExistingOffices() {
        const container = document.getElementById("doctor-offices-container");
        if (!container) return;

        container.innerHTML = "";

        for (let i = 0; i < existingOffices.length; i++) {
            const office = existingOffices[i];

            const specialties = [];
            for (let j = 0; j < office.specialties.length; j++) {
                specialties.push(office.specialties[j].id.toString());
            }

            doctorOffices.push({
                index: officeCounter,
                name: office.name,
                neighborhoodId: office.neighborhoodId.toString(),
                specialties: specialties
            });

            const officeEntry = document.createElement("div");
            officeEntry.className = "office-entry";
            officeEntry.setAttribute('data-index', officeCounter.toString());

            officeEntry.innerHTML = createOfficeHTML(officeCounter, office.name, office.neighborhoodId, office.id);

            container.appendChild(officeEntry);
            initializeOffice(officeCounter);

            (function(currentCounter) {
                setTimeout(function() {
                    updateOfficeSpecialtiesDisplay(currentCounter);
                }, 50);
            })(officeCounter);

            officeCounter++;
        }

        updateRemoveButtons();
    }

    // Form submission with validation
    document.addEventListener('DOMContentLoaded', function() {
        const form = document.getElementById('officesForm');
        if (form) {
            form.addEventListener('submit', function(e) {
                // Validate all offices before submission
                let isValid = true;
                for (let i = 0; i < doctorOffices.length; i++) {
                    const office = doctorOffices[i];
                    const nameValid = validateOfficeName(office.index);
                    const neighborhoodValid = validateOfficeNeighborhood(office.index);
                    const specialtiesValid = validateOfficeSpecialties(office.index);

                    if (!nameValid || !neighborhoodValid || !specialtiesValid) {
                        isValid = false;
                    }
                }

                if (!isValid) {
                    e.preventDefault();
                    // Scroll to first error
                    const firstError = document.querySelector('.error-message.visible, .custom-multi-select.error');
                    if (firstError) {
                        firstError.scrollIntoView({ behavior: 'smooth', block: 'center' });
                    }
                    return false;
                }

                e.preventDefault();

                // Remove existing hidden inputs
                const existingInputs = document.querySelectorAll('input[name^="doctorOfficeForm["]');
                for (let i = 0; i < existingInputs.length; i++) {
                    existingInputs[i].parentNode.removeChild(existingInputs[i]);
                }

                // Create hidden inputs matching OfficeForm structure
                for (let i = 0; i < doctorOffices.length; i++) {
                    const office = doctorOffices[i];
                    if (!office.name || !office.neighborhoodId || !office.specialties) continue;

                    // Office name
                    const nameInput = document.createElement("input");
                    nameInput.type = "hidden";
                    nameInput.name = "doctorOfficeForm[" + i + "].officeName";
                    nameInput.value = office.name;
                    form.appendChild(nameInput);

                    // Neighborhood ID
                    const neighborhoodInput = document.createElement("input");
                    neighborhoodInput.type = "hidden";
                    neighborhoodInput.name = "doctorOfficeForm[" + i + "].neighborhoodId";
                    neighborhoodInput.value = office.neighborhoodId;
                    form.appendChild(neighborhoodInput);

                    // Specialty IDs
                    for (let j = 0; j < office.specialties.length; j++) {
                        const specialtyInput = document.createElement("input");
                        specialtyInput.type = "hidden";
                        specialtyInput.name = "doctorOfficeForm[" + i + "].specialtyIds[" + j + "]";
                        specialtyInput.value = office.specialties[j];
                        form.appendChild(specialtyInput);
                    }
                }

                // Submit the form
                this.submit();
            });
        }
    });

    document.addEventListener('DOMContentLoaded', function() {
        const urlParams = new URLSearchParams(window.location.search);
        if (urlParams.get('updated') === 'true') {
            showSuccessToast();
        }
    });
</script>
</body>
</html>
