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
    var officeCounter = 0;
    var doctorOffices = [];

    // Initialize data from JSP
    var neighborhoods = [
        <c:forEach items="${neighborhoodList}" var="neighborhood" varStatus="status">
        {
            id: "${neighborhood.id}",
            name: "${neighborhood.name}"
        }<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    ];

    var specialtyList = [
        <c:forEach items="${specialtyList}" var="specialty" varStatus="status">
        {
            id: ${specialty.id},
            name: "<spring:message code="${specialty.key}" />"
        }<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    ];

    // Existing offices data
    <c:if test="${not empty doctorOffices}">
    var existingOffices = [
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
        var container = document.getElementById("doctor-offices-container");
        if (!container) return;

        var officeEntry = document.createElement("div");
        officeEntry.className = "office-entry";
        officeEntry.setAttribute('data-index', officeCounter);

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

    function createOfficeHTML(index, name, neighborhoodId) {
        name = name || '';
        neighborhoodId = neighborhoodId || '';

        var neighborhoodName = '';
        if (neighborhoodId) {
            for (var i = 0; i < neighborhoods.length; i++) {
                if (neighborhoods[i].id.toString() === neighborhoodId.toString()) {
                    neighborhoodName = neighborhoods[i].name;
                    break;
                }
            }
        }

        var html = '<div class="form-row">';

        html += '<div class="form-group">';
        html += '<label for="office-name-' + index + '" class="form-label required-field">Nombre del Consultorio</label>';
        html += '<div class="input-wrapper">';
        html += '<input type="text" id="office-name-' + index + '" class="form-control office-name" ';
        html += 'placeholder="Ingrese el nombre del consultorio" value="' + name + '" required/>';
        html += '</div>';
        html += '</div>';

        // Neighborhood
        html += '<div class="form-group">';
        html += '<label for="office-neighborhood-search-' + index + '" class="form-label required-field">Barrio</label>';
        html += '<div class="input-wrapper neighborhood-search-wrapper">';
        html += '<input type="text" id="office-neighborhood-search-' + index + '" class="form-control office-neighborhood-search" ';
        html += 'placeholder="Buscar barrio" value="' + neighborhoodName + '" autocomplete="off" required/>';
        html += '<input type="hidden" id="office-neighborhood-id-' + index + '" class="office-neighborhood-id" value="' + neighborhoodId + '"/>';
        html += '</div>';
        html += '<div id="office-neighborhood-results-' + index + '" class="search-results-container"></div>';
        html += '</div>';

        html += '</div>';

        // Specialties
        html += '<div class="form-group">';
        html += '<label class="form-label required-field">Especialidades del Consultorio</label>';
        html += '<div class="multi-select-container" id="office-specialties-container-' + index + '">';
        html += generateSpecialtyOptions(index);
        html += '</div>';
        html += '<div class="selected-options" id="office-specialties-selected-' + index + '"></div>';
        html += '</div>';

        html += '<div class="office-actions">';
        html += '<button type="button" class="btn-remove office-remove" onclick="removeOffice(' + index + ')">';
        html += '<i class="fas fa-times"></i>';
        html += '</button>';
        html += '</div>';

        return html;
    }

    function generateSpecialtyOptions(officeIndex) {
        var html = '<div class="custom-multi-select" id="office-specialties-options-' + officeIndex + '">';

        for (var i = 0; i < specialtyList.length; i++) {
            var specialty = specialtyList[i];
            var isSelected = false;

            // Check if this specialty is selected for this office
            var officeArrayIndex = -1;
            for (var j = 0; j < doctorOffices.length; j++) {
                if (doctorOffices[j].index === parseInt(officeIndex)) {
                    officeArrayIndex = j;
                    break;
                }
            }

            if (officeArrayIndex !== -1 && doctorOffices[officeArrayIndex].specialties) {
                for (var k = 0; k < doctorOffices[officeArrayIndex].specialties.length; k++) {
                    if (doctorOffices[officeArrayIndex].specialties[k].toString() === specialty.id.toString()) {
                        isSelected = true;
                        break;
                    }
                }
            }

            html += '<label class="custom-checkbox">';
            html += '<input type="checkbox" value="' + specialty.id + '" ' + (isSelected ? 'checked' : '') + ' onchange="toggleOfficeSpecialty(this, ' + officeIndex + ')">';
            html += '<span class="checkmark"></span>';
            html += '<span class="checkbox-label">' + specialty.name + '</span>';
            html += '</label>';
        }

        html += '</div>';
        return html;
    }

    function initializeOffice(index) {
        var searchInput = document.getElementById('office-neighborhood-search-' + index);
        if (searchInput) {
            searchInput.addEventListener("input", function() {
                searchOfficeNeighborhoods(index, this.value);
            });
            searchInput.addEventListener("focus", function() {
                searchOfficeNeighborhoods(index, this.value);
            });
            searchInput.addEventListener("blur", function() {
                setTimeout(function() {
                    var resultsContainer = document.getElementById('office-neighborhood-results-' + index);
                    if (resultsContainer) {
                        resultsContainer.style.display = "none";
                    }
                }, 200);
            });
        }

        var nameInput = document.getElementById('office-name-' + index);
        if (nameInput) {
            nameInput.addEventListener("input", function() {
                for (var i = 0; i < doctorOffices.length; i++) {
                    if (doctorOffices[i].index === parseInt(index)) {
                        doctorOffices[i].name = this.value;
                        break;
                    }
                }
            });
        }
    }

    function searchOfficeNeighborhoods(index, query) {
        var resultsContainer = document.getElementById('office-neighborhood-results-' + index);
        if (!resultsContainer) return;

        resultsContainer.innerHTML = "";
        resultsContainer.style.display = "block";

        var searchQuery = query.toLowerCase();

        for (var i = 0; i < neighborhoods.length; i++) {
            var neighborhood = neighborhoods[i];
            if (query.trim().length === 0 || neighborhood.name.toLowerCase().indexOf(searchQuery) !== -1) {
                var item = document.createElement("div");
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
        var searchField = document.getElementById('office-neighborhood-search-' + index);
        var idField = document.getElementById('office-neighborhood-id-' + index);
        var resultsContainer = document.getElementById('office-neighborhood-results-' + index);

        if (searchField && idField) {
            searchField.value = name;
            idField.value = id;

            for (var i = 0; i < doctorOffices.length; i++) {
                if (doctorOffices[i].index === parseInt(index)) {
                    doctorOffices[i].neighborhoodId = id;
                    break;
                }
            }
        }

        if (resultsContainer) {
            resultsContainer.style.display = "none";
        }
    }

    function toggleOfficeSpecialty(checkbox, officeIndex) {
        var specialtyId = checkbox.value;

        for (var i = 0; i < doctorOffices.length; i++) {
            if (doctorOffices[i].index === parseInt(officeIndex)) {
                if (checkbox.checked) {
                    var found = false;
                    for (var j = 0; j < doctorOffices[i].specialties.length; j++) {
                        if (doctorOffices[i].specialties[j] === specialtyId) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        doctorOffices[i].specialties.push(specialtyId);
                    }
                } else {
                    for (var j = 0; j < doctorOffices[i].specialties.length; j++) {
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
    }

    function updateOfficeSpecialtiesDisplay(officeIndex) {
        var selectedContainer = document.getElementById('office-specialties-selected-' + officeIndex);
        if (!selectedContainer) return;

        selectedContainer.innerHTML = "";

        var officeArrayIndex = -1;
        for (var i = 0; i < doctorOffices.length; i++) {
            if (doctorOffices[i].index.toString() === officeIndex.toString()) {
                officeArrayIndex = i;
                break;
            }
        }

        if (officeArrayIndex === -1) return;

        var selectedSpecialties = doctorOffices[officeArrayIndex].specialties || [];

        for (var i = 0; i < selectedSpecialties.length; i++) {
            var specialtyId = selectedSpecialties[i];
            var specialtyName = 'Specialty ' + specialtyId;

            for (var j = 0; j < specialtyList.length; j++) {
                if (specialtyList[j].id.toString() === specialtyId.toString()) {
                    specialtyName = specialtyList[j].name;
                    break;
                }
            }

            var badge = document.createElement("span");
            badge.className = "specialty-tag";
            badge.textContent = specialtyName;
            selectedContainer.appendChild(badge);
        }
    }

    function removeOffice(index) {
        var officeEntry = document.querySelector('.office-entry[data-index="' + index + '"]');
        if (!officeEntry) return;

        officeEntry.parentNode.removeChild(officeEntry);

        for (var i = 0; i < doctorOffices.length; i++) {
            if (doctorOffices[i].index === parseInt(index)) {
                doctorOffices.splice(i, 1);
                break;
            }
        }

        updateRemoveButtons();
    }

    function updateRemoveButtons() {
        var offices = document.querySelectorAll(".office-entry");
        var removeButtons = document.querySelectorAll(".office-remove");

        for (var i = 0; i < removeButtons.length; i++) {
            removeButtons[i].style.display = offices.length > 1 ? "block" : "none";
        }
    }

    function renderExistingOffices() {
        var container = document.getElementById("doctor-offices-container");
        if (!container) return;

        container.innerHTML = "";

        for (var i = 0; i < existingOffices.length; i++) {
            var office = existingOffices[i];

            var specialties = [];
            for (var j = 0; j < office.specialties.length; j++) {
                specialties.push(office.specialties[j].id.toString());
            }

            doctorOffices.push({
                index: officeCounter,
                name: office.name,
                neighborhoodId: office.neighborhoodId.toString(),
                specialties: specialties
            });

            var officeEntry = document.createElement("div");
            officeEntry.className = "office-entry";
            officeEntry.setAttribute('data-index', officeCounter);

            officeEntry.innerHTML = createOfficeHTML(officeCounter, office.name, office.neighborhoodId);

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

    // Form submission
    document.addEventListener('DOMContentLoaded', function() {
        var form = document.getElementById('officesForm');
        if (form) {
            form.addEventListener('submit', function(e) {
                e.preventDefault();

                // Create hidden inputs
                var existingInputs = document.querySelectorAll('input[name^="doctorOfficeForm["]');
                for (var i = 0; i < existingInputs.length; i++) {
                    existingInputs[i].parentNode.removeChild(existingInputs[i]);
                }

                for (var i = 0; i < doctorOffices.length; i++) {
                    var office = doctorOffices[i];
                    if (!office.name || !office.neighborhoodId) continue;

                    var nameInput = document.createElement("input");
                    nameInput.type = "hidden";
                    nameInput.name = "doctorOfficeForm[" + i + "].officeName";
                    nameInput.value = office.name;
                    form.appendChild(nameInput);

                    var neighborhoodInput = document.createElement("input");
                    neighborhoodInput.type = "hidden";
                    neighborhoodInput.name = "doctorOfficeForm[" + i + "].neighborhoodId";
                    neighborhoodInput.value = office.neighborhoodId;
                    form.appendChild(neighborhoodInput);

                    for (var j = 0; j < office.specialties.length; j++) {
                        var specialtyInput = document.createElement("input");
                        specialtyInput.type = "hidden";
                        specialtyInput.name = "doctorOfficeForm[" + i + "].specialtyIds[" + j + "]";
                        specialtyInput.value = office.specialties[j];
                        form.appendChild(specialtyInput);
                    }
                }

                this.submit();
            });
        }
    });


    document.addEventListener('DOMContentLoaded', function() {
        var urlParams = new URLSearchParams(window.location.search);
        if (urlParams.get('updated') === 'true') {
            showSuccessToast();
        }
    });
</script>
</body>
</html>
