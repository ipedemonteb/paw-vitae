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

<!-- Remove Office Confirmation Modal -->
<div id="removeOfficeModal" class="confirmation-modal" style="display: none;">
    <div class="modal-overlay" onclick="hideRemoveConfirmation()"></div>
    <div class="modal-content">
        <div class="modal-header">
            <h3><i class="fas fa-exclamation-triangle text-warning"></i> <spring:message code="offices.remove.confirm.title" /></h3>
        </div>
        <div class="modal-body">
            <p><spring:message code="offices.remove.confirm.message" /></p>
            <div class="office-info-preview">
                <strong id="officeNamePreview"></strong>
            </div>
            <div class="warning-note">
                <i class="fas fa-info-circle"></i>
                <span><spring:message code="offices.remove.confirm.warning" /></span>
            </div>
        </div>
        <div class="modal-actions">
            <button type="button" class="btn btn-secondary" onclick="hideRemoveConfirmation()">
                <i class="fas fa-times"></i>
                <spring:message code="logout.confirmation.cancel" />
            </button>
            <button type="button" class="btn btn-danger" onclick="confirmRemoveOffice()">
                <i class="fas fa-trash"></i>
                <spring:message code="offices.remove.confirm.delete" />
            </button>
        </div>
    </div>
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
                                <c:if test="${office.active}">
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
                                </c:if>
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

                <!-- Office Filter Tabs -->
                <div class="office-filter-tabs">
                    <button class="filter-tab" data-filter="active" onclick="filterOffices('active')">
                        <i class="fas fa-check-circle"></i>
                        <spring:message code="offices.filter.active" />
                        <span class="tab-count" id="count-active">0</span>
                    </button>
<%--                    <button class="filter-tab" data-filter="disabled" onclick="filterOffices('disabled')">--%>
<%--                        <i class="fas fa-pause-circle"></i>--%>
<%--                        <spring:message code="offices.filter.disabled" />--%>
<%--                        <span class="tab-count" id="count-disabled">0</span>--%>
<%--                    </button>--%>
                </div>

                <form:form id="officesForm" modelAttribute="doctorOfficeForm" method="post"
                           action="${pageContext.request.contextPath}/doctor/dashboard/offices/update"
                           cssClass="edit-profile-form">

                    <form:hidden path="doctorId" id="doctorId"/>

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
    let currentFilter = 'active';
    let pendingRemoveIndex = null;

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
            id: ${office.id},
            name: "<c:out value='${office.officeName}'/>",
            neighborhoodId: ${office.neighborhood.id},
            active: ${office.active},
            removed: false,
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

    // Modal functions
    function showRemoveConfirmation(index) {
        const office = doctorOffices.find(o => o.index === parseInt(index));
        if (!office) return;

        pendingRemoveIndex = index;

        if (office.id == null) {
            confirmRemoveOffice();
            return;
        }

        document.getElementById('officeNamePreview').textContent = office.name || 'Unnamed Office';
        document.getElementById('removeOfficeModal').style.display = 'flex';
    }

    function hideRemoveConfirmation() {
        pendingRemoveIndex = null;
        document.getElementById('removeOfficeModal').style.display = 'none';
    }

    function confirmRemoveOffice() {
        if (pendingRemoveIndex !== null) {
            const office = doctorOffices.find(o => o.index === parseInt(pendingRemoveIndex));
            if (!office) return;

            if (office.id) {
                // Existing office - mark as removed for backend processing
                office.removed = true;
                office.active = false;

                // Hide the office from view
                const officeEntry = document.querySelector('.office-entry[data-index="' + pendingRemoveIndex + '"]');
                if (officeEntry) {
                    officeEntry.style.display = 'none';
                }
            } else {
                // New office - completely remove from DOM and array
                const officeEntry = document.querySelector('.office-entry[data-index="' + pendingRemoveIndex + '"]');
                if (officeEntry) {
                    officeEntry.parentNode.removeChild(officeEntry);
                }

                const officeArrayIndex = doctorOffices.findIndex(o => o.index === parseInt(pendingRemoveIndex));
                if (officeArrayIndex !== -1) {
                    doctorOffices.splice(officeArrayIndex, 1);
                }
            }

            updateFilterCounts();
            applyCurrentFilter();
            hideRemoveConfirmation();
        }
    }

    // Simple functions using onclick
    function showOfficesForm() {
        document.getElementById('offices-display').style.display = 'none';
        document.getElementById('offices-form-container').style.display = 'block';
        document.querySelector('.tab-header').style.display = 'none';

        if (typeof existingOffices !== 'undefined' && existingOffices.length > 0) {
            renderExistingOffices();
        } else {
            addNewOffice();
        }
        filterOffices('active');
        updateFilterCounts();
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
            id: null,
            name: "",
            neighborhoodId: "",
            specialties: [],
            active: true,
            removed: false
        });

        // container.insertBefore(officeEntry, container.firstChild);
        container.appendChild(officeEntry);
        initializeOffice(officeCounter);
        updateOfficeStatus(officeCounter);
        updateFilterCounts();
        officeCounter++;
    }

    function createOfficeHTML(index, name, neighborhoodId, id, active) {
        name = name || '';
        id = id || '';
        neighborhoodId = neighborhoodId || '';
        active = active !== undefined ? active : true;

        let neighborhoodName = '';
        if (neighborhoodId) {
            for (let i = 0; i < neighborhoods.length; i++) {
                if (neighborhoods[i].id.toString() === neighborhoodId.toString()) {
                    neighborhoodName = neighborhoods[i].name;
                    break;
                }
            }
        }

        let html = '';

        // Status indicator
        <%--html += '<div class="office-status-indicator" id="status-indicator-' + index + '">';--%>
        <%--html += '<i class="fas fa-circle"></i>';--%>
        <%--html += '<span id="status-text-' + index + '"><spring:message code='offices.status.active' /></span>';--%>
        <%--html += '</div>';--%>

        // Action buttons
        // html += '<div class="office-actions">';
        <%--html += '<button type="button" class="office-action-btn btn-toggle" id="toggle-btn-' + index + '" ';--%>
        <%--html += 'onclick="toggleOfficeStatus(' + index + ')" data-tooltip="<spring:message code='offices.disable' />">';--%>
        <%--html += '<i class="fas fa-pause"></i>';--%>
        <%--html += '</button>';--%>
        <%--html += '<button type="button" class="office-action-btn btn-remove-office" id="remove-btn-' + index + '" ';--%>
        <%--html += 'onclick="showRemoveConfirmation(' + index + ')" data-tooltip="<spring:message code='offices.remove' />">';--%>
        <%--html += '<i class="fas fa-trash"></i>';--%>
        <%--html += '</button>';--%>
        // html += '</div>';

        html += '<div class="form-row">';

        // Office name with validation
        html += '<div class="form-group">';
        html += '<label for="office-name-' + index + '" class="form-label required-field"><spring:message code='register.officeName'/></label>';
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
        html += '<label for="office-neighborhood-search-' + index + '" class="form-label required-field"><spring:message code='register.neighborhood'/></label>';
        html += '<div class="input-wrapper neighborhood-search-wrapper">';
        html += '<input type="text" id="office-neighborhood-search-' + index + '" class="form-control office-neighborhood-search" ';
        html += 'placeholder="<spring:message code='neighborhood.placeholder'/>" value="' + neighborhoodName + '" autocomplete="off" required/>';
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
        html += '<label class="form-label required-field"><spring:message code='register.officeSpecialties'/></label>';
        html += '<div class="custom-multi-select" id="office-specialties-container-' + index + '">';
        html += generateSpecialtyOptions(index);
        html += '</div>';
        html += '<div class="selected-options" id="office-specialties-selected-' + index + '"></div>';
        html += '<small class="form-text text-muted"><spring:message code='register.selectOfficeSpecialties'/></small>';
        html += '<div id="office-specialties-' + index + '-validation-message" class="error-message"></div>';
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

    function toggleOfficeStatus(index) {
        const office = doctorOffices.find(o => o.index === parseInt(index));
        if (!office || office.removed) return;

        office.active = !office.active;
        updateOfficeStatus(index);
        updateFilterCounts();
        applyCurrentFilter();
    }

    function updateOfficeStatus(index) {
        const office = doctorOffices.find(o => o.index === parseInt(index));
        if (!office) return;

        const officeEntry = document.querySelector('.office-entry[data-index="' + index + '"]');
        const statusIndicator = document.getElementById('status-indicator-' + index);
        const statusText = document.getElementById('status-text-' + index);
        const toggleBtn = document.getElementById('toggle-btn-' + index);

        if (!officeEntry || !statusIndicator || !statusText || !toggleBtn) return;

        // Remove all status classes
        officeEntry.classList.remove('disabled');
        statusIndicator.classList.remove('active', 'disabled');
        toggleBtn.classList.remove('disabled');

        if (!office.active) {
            // Disabled state
            officeEntry.classList.add('disabled');
            statusIndicator.classList.add('disabled');
            statusText.textContent = "<spring:message code='offices.status.disabled' />";

            toggleBtn.classList.add('disabled');
            toggleBtn.innerHTML = '<i class="fas fa-play"></i>';
            toggleBtn.setAttribute('data-tooltip', '<spring:message code='offices.enable' />');
        } else {
            // Active state
            statusIndicator.classList.add('active');
            statusText.textContent = "<spring:message code='offices.status.active' />";

            toggleBtn.innerHTML = '<i class="fas fa-pause"></i>';
            toggleBtn.setAttribute('data-tooltip', '<spring:message code='offices.disable' />');
        }
    }

    function filterOffices(filter) {
        currentFilter = filter;

        // Update active tab
        document.querySelectorAll('.filter-tab').forEach(tab => {
            tab.classList.remove('active');
        });
        document.querySelector('.filter-tab[data-filter="' + filter + '"]').classList.add('active');

        applyCurrentFilter();
    }

    function applyCurrentFilter() {
        const offices = document.querySelectorAll('.office-entry');
        const addButton = document.getElementById('add-office-btn');

        offices.forEach(officeElement => {
            const index = parseInt(officeElement.getAttribute('data-index'));
            const office = doctorOffices.find(o => o.index === index);

            if (!office || office.removed) {
                officeElement.style.display = 'none';
                return;
            }

            let shouldShow = false;

            switch (currentFilter) {
                case 'active':
                    shouldShow = office.active;
                    addButton.style.display = 'block';
                    break;
                case 'disabled':
                    shouldShow = !office.active;
                    addButton.style.display = 'none';
                    break;
            }

            officeElement.style.display = shouldShow ? 'block' : 'none';
        });
    }

    function updateFilterCounts() {
        const counts = {
            active: 0,
            disabled: 0
        };

        doctorOffices.forEach(office => {
            if (!office.removed) {
                if (office.active) {
                    counts.active++;
                } else {
                    counts.disabled++;
                }
            }
        });

        document.getElementById('count-active').textContent = counts.active;
        document.getElementById('count-disabled').textContent = counts.disabled;
    }

    function toggleOfficeSpecialtyOption(element, officeIndex) {
        const office = doctorOffices.find(o => o.index === parseInt(officeIndex));
        if (!office || office.removed) return;

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
                setFieldError(nameField, errorElement, "<spring:message code='offices.invalid.name'/>");
            } else {
                clearFieldValidation(nameField, errorElement);
            }
            return false;
        } else if (value.length < 2) {
            if (officeNameTouched[index]) {
                setFieldError(nameField, errorElement, "<spring:message code='offices.invalid.name.length'/>");
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
                setFieldError(searchField, errorElement, "<spring:message code='offices.invalid.neighborhood'/>");
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
                setContainerError(container, errorElement, "<spring:message code='offices.invalid.specialties.length'/>");
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

    function renderExistingOffices() {
        const container = document.getElementById("doctor-offices-container");
        if (!container) return;

        container.innerHTML = "";
        doctorOffices = [];

        for (let i = 0; i < existingOffices.length; i++) {
            const office = existingOffices[i];

            const specialties = [];
            for (let j = 0; j < office.specialties.length; j++) {
                specialties.push(office.specialties[j].id.toString());
            }

            doctorOffices.push({
                index: officeCounter,
                id: office.id,
                name: office.name,
                neighborhoodId: office.neighborhoodId.toString(),
                specialties: specialties,
                active: office.active,
                removed: office.removed
            });

            const officeEntry = document.createElement("div");
            officeEntry.className = "office-entry";
            officeEntry.setAttribute('data-index', officeCounter.toString());

            officeEntry.innerHTML = createOfficeHTML(officeCounter, office.name, office.neighborhoodId, office.id, office.active);

            container.appendChild(officeEntry);
            initializeOffice(officeCounter);
            updateOfficeStatus(officeCounter);

            (function(currentCounter) {
                setTimeout(function() {
                    updateOfficeSpecialtiesDisplay(currentCounter);
                }, 50);
            })(officeCounter);

            officeCounter++;
        }
    }

    // Form submission with validation
    document.addEventListener('DOMContentLoaded', function() {
        const form = document.getElementById('officesForm');

        if (form) {
            form.addEventListener('submit', function(e) {
                // Validate all non-removed offices before submission
                let isValid = true;
                for (let i = 0; i < doctorOffices.length; i++) {
                    const office = doctorOffices[i];
                    if (office.removed) continue; // Skip validation for removed offices

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
                let formIndex = 0;
                for (let i = 0; i < doctorOffices.length; i++) {
                    const office = doctorOffices[i];
                    if (!office.name || !office.neighborhoodId || !office.specialties) continue;

                    // Office ID (for existing offices)
                    const idInput = document.createElement("input");
                    idInput.type = "hidden";
                    idInput.name = "doctorOfficeForm[" + formIndex + "].id";
                    idInput.value = office.id || '';
                    form.appendChild(idInput);

                    // Office name
                    const nameInput = document.createElement("input");
                    nameInput.type = "hidden";
                    nameInput.name = "doctorOfficeForm[" + formIndex + "].officeName";
                    nameInput.value = office.name;
                    form.appendChild(nameInput);

                    // Neighborhood ID
                    const neighborhoodInput = document.createElement("input");
                    neighborhoodInput.type = "hidden";
                    neighborhoodInput.name = "doctorOfficeForm[" + formIndex + "].neighborhoodId";
                    neighborhoodInput.value = office.neighborhoodId;
                    form.appendChild(neighborhoodInput);

                    // Active status
                    const activeInput = document.createElement("input");
                    activeInput.type = "hidden";
                    activeInput.name = "doctorOfficeForm[" + formIndex + "].active";
                    activeInput.value = office.active;
                    form.appendChild(activeInput);

                    // Removed status
                    const removedInput = document.createElement("input");
                    removedInput.type = "hidden";
                    removedInput.name = "doctorOfficeForm[" + formIndex + "].removed";
                    removedInput.value = office.removed;
                    form.appendChild(removedInput);

                    // Specialty IDs
                    for (let j = 0; j < office.specialties.length; j++) {
                        const specialtyInput = document.createElement("input");
                        specialtyInput.type = "hidden";
                        specialtyInput.name = "doctorOfficeForm[" + formIndex + "].specialtyIds[" + j + "]";
                        specialtyInput.value = office.specialties[j];
                        form.appendChild(specialtyInput);
                    }

                    formIndex++;
                }

                // Submit the form
                this.submit();
            });
        }

        // Handle ESC key to close modal
        document.addEventListener('keydown', function(e) {
            if (e.key === 'Escape') {
                hideRemoveConfirmation();
            }
        });
    });

    document.addEventListener('DOMContentLoaded', function() {
        const urlParams = new URLSearchParams(window.location.search);
        if (urlParams.get('updated') === 'true') {
            showSuccessToast();
        }
    });
</script>

<style>
    /* Confirmation Modal Styles */
    .confirmation-modal {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        z-index: 10000;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .modal-overlay {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.5);
        backdrop-filter: blur(4px);
    }

    .modal-content {
        position: relative;
        background: white;
        border-radius: 12px;
        box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
        max-width: 500px;
        width: 90%;
        max-height: 90vh;
        overflow-y: auto;
        animation: modalSlideIn 0.3s ease;
    }

    @keyframes modalSlideIn {
        from {
            opacity: 0;
            transform: translateY(-20px);
        }
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }

    .modal-header {
        padding: 1.5rem 1.5rem 0;
        border-bottom: 1px solid #e5e7eb;
    }

    .modal-header h3 {
        margin: 0 0 1rem;
        color: #1f2937;
        font-size: 1.25rem;
        font-weight: 600;
        display: flex;
        align-items: center;
        gap: 0.5rem;
    }

    .modal-body {
        padding: 1.5rem;
    }

    .modal-body p {
        margin: 0 0 1rem;
        color: #6b7280;
        line-height: 1.6;
    }

    .office-info-preview {
        background: #f9fafb;
        border: 1px solid #e5e7eb;
        border-radius: 8px;
        padding: 0.75rem;
        margin: 1rem 0;
        font-family: monospace;
    }

    .warning-note {
        background: #fef3c7;
        border: 1px solid #f59e0b;
        border-radius: 6px;
        padding: 0.75rem;
        margin-top: 1rem;
        display: flex;
        align-items: flex-start;
        gap: 0.5rem;
        font-size: 0.875rem;
    }

    .warning-note i {
        color: #d97706;
        margin-top: 2px;
        flex-shrink: 0;
    }

    .modal-actions {
        padding: 1rem 1.5rem 1.5rem;
        border-top: 1px solid #e5e7eb;
        display: flex;
        gap: 0.75rem;
        justify-content: flex-end;
    }

    .text-warning {
        color: #f59e0b;
    }

    /* Responsive modal */
    @media (max-width: 640px) {
        .modal-content {
            margin: 1rem;
            width: calc(100% - 2rem);
        }

        .modal-actions {
            flex-direction: column;
        }

        .modal-actions .btn {
            width: 100%;
            justify-content: center;
        }
    }
</style>
</body>
</html>
