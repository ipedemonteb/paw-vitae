<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="icon" type="image/png" href="https://upload.wikimedia.org/wikipedia/commons/thumb/a/aa/V-logo.svg/2048px-V-logo.svg.png" />
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="appointment.page.title" /></title>
    <link rel="stylesheet" href="<c:url value='/css/appointment.css' />" />
    <link rel="stylesheet" href="<c:url value='/css/toast-notification.css' />" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
<!-- Include the header -->
<jsp:include page="/WEB-INF/jsp/components/header.jsp" />

<div id="successToast" class="success-toast">
    <div class="success-toast-icon">
        <i class="fas fa-check"></i>
    </div>
    <div class="success-toast-content">
        <div class="success-toast-title"><spring:message code="file.upload.fileAdded"/></div>
        <div class="success-toast-message"><spring:message code="file.upload.message"/></div>
    </div>
    <button class="success-toast-close" onclick="hideSuccessToast()">
        <i class="fas fa-times"></i>
    </button>
</div>


<!-- Main Content -->
<main class="main-content">
    <div class="container">
        <div class="appointment-container">
            <div class="appointment-header">
                <h1 class="appointment-title"><spring:message code="appointment.title" /></h1>
                <p class="appointment-subtitle"><spring:message code="appointment.subtitle" /></p>
            </div>

            <div class="appointment-body">
                <!-- Display doctor information if available -->
                <c:if test="${not empty doctor}">
                    <div class="doctor-info">
                        <div class="doctor-image">
                            <c:choose>
                                <c:when test="${doctor.imageId != null}">
                                    <img src='<c:url value="/image/${doctor.imageId}"/>' onerror="this.src='/img/default_picture.png'" alt="${doctor.name}" class="doctor-avatar" class="doctor-avatar" />
                                </c:when>
                                <c:otherwise>
                                    <img src="/img/default_picture.png" alt="default" class="doctor-avatar" />
                                </c:otherwise>
                            </c:choose>                              </div>
                        <div class="doctor-details">
                            <h3 class="doctor-name"><c:out value="${doctor.name}"/> <c:out value="${doctor.lastName}"/></h3>
                            <div class="card-specialty-list">
                                <c:set var="maxLength" value="80" />
                                <c:set var="currentLength" value="0" />
                                <c:set var="stopLoop" value="false" />

                                <c:forEach var="specialty" items="${doctor.specialtyList}" varStatus="status">
                                    <c:if test="${!stopLoop}">
                                        <c:if test="${currentLength le maxLength}">
                                            <p class="doctor-specialty">
                                                <spring:message code="${specialty.key}" />
                                            </p>
                                            <c:set var="currentLength" value="${currentLength + fn:length(specialty.key)}" />
                                            <c:if test="${!status.last && currentLength le maxLength}">
                                                <span style="white-space: pre">, </span>
                                            </c:if>
                                        </c:if>
                                        <c:if test="${currentLength gt maxLength}">
                                            <span>...</span>
                                            <c:set var="stopLoop" value="true" />
                                        </c:if>
                                    </c:if>
                                </c:forEach>
                            </div>
                            <div class="doctor-contact">
                                <div class="contact-item">
                                    <span class="contact-icon"><i class="fas fa-envelope"></i></span>
                                    <span class="contact-text"><c:out value="${doctor.email}"/></span>
                                </div>
                                <div class="contact-item">
                                    <span class="contact-icon"><i class="fas fa-phone"></i></span>
                                    <span class="contact-text"><c:out value="${doctor.phone}"/></span>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>

                <form:form modelAttribute="appointmentForm" method="post" action="${pageContext.request.contextPath}/appointment" id="appointmentForm" class="appointment-form" enctype="multipart/form-data" onsubmit="return submitOnce(this);">
                    <!-- Replace the existing specialty card section with this enhanced version -->
                    <div class="specialty-card-appointment">
                        <div class="specialty-icon-appointment">
                            <i class="fas fa-stethoscope"></i>
                        </div>
                        <div class="specialty-content">
                            <span class="specialty-label-appointment"><spring:message code="appointment.form.specialty" />:</span>
                            <div class="specialty-select-container">
                                <form:select path="specialtyId" id="specialtySelect" class="specialty-select">
                                    <c:forEach var="doctorSpecialty" items="${doctor.specialtyList}">
                                        <form:option value="${doctorSpecialty.id}">
                                            <spring:message code="${doctorSpecialty.key}" />
                                        </form:option>
                                    </c:forEach>
                                </form:select>
                            </div>
                            <form:errors path="specialtyId" cssClass="error-message" />
                        </div>
                    </div>

                    <!-- Add Office Selection Card -->
                    <div class="office-card-appointment">
                        <div class="office-icon-appointment">
                            <i class="fas fa-building"></i>
                        </div>
                        <div class="office-content">
                            <span class="office-label-appointment"><spring:message code="appointment.form.office" />:</span>
                            <div class="office-select-container">
                                <select id="officeSelect" class="office-select" name="officeId" required>
                                    <option value=""><spring:message code="appointment.form.selectOffice" /></option>
                                    <!-- Options will be populated by JavaScript -->
                                </select>
                            </div>
                            <div class="office-details" id="officeDetails" style="display: none;">
                                <div class="office-info">
                                    <span class="office-name" id="selectedOfficeName"></span>
                                    <span class="office-neighborhood" id="selectedOfficeNeighborhood"></span>
                                </div>
                            </div>
                            <form:errors path="officeId" cssClass="error-message" />
                        </div>
                    </div>

                    <!-- Hidden fields for office data -->
                    <form:hidden path="officeId" id="officeId" />
                    <form:hidden path="specialtyId" id="specialtyId" />
                    <input type="hidden" name="doctorId" value="${doctor.id}" />

                    <div class="form-group">
                        <label for="datePickerInput" class="required-field"><spring:message code="appointment.form.datetime"/></label>
                        <div class="date-picker-container">
                            <input type="text" id="datePickerInput" class="form-control date-picker-input"
                                   placeholder="<spring:message code='appointment.placeholder.selectDate'/>" readonly>

                            <!-- Custom Calendar -->
                            <div id="datePickerCalendar" class="date-picker-calendar">
                                <div class="date-picker-header">
                                    <button type="button" id="prevMonthBtn" class="date-picker-nav">&lsaquo;</button>
                                    <div id="currentMonthYear" class="date-picker-month-year"></div>
                                    <button type="button" id="nextMonthBtn" class="date-picker-nav">&rsaquo;</button>
                                </div>
                                <div class="date-picker-weekdays" id="calendarWeekdays"></div>
                                <div class="date-picker-days" id="calendarDays"></div>
                            </div>
                        </div>

                        <!-- Hidden fields to store actual values -->
                        <form:hidden path="appointmentDate" id="appointmentDate" />
                        <form:hidden path="appointmentHour" id="appointmentHour" />
                        <form:hidden path="patientId" id="patientId"/>
                        <form:hidden path="doctorId" id="doctorId"/>

                        <!-- Error messages for date and hour -->


                        <!-- Time slots container -->
                        <div id="timeSlotsContainer" class="time-slots-container" style="display: none;">
                            <label><spring:message code="appointment.form.selectTime"/></label>
                            <div id="timeSlots" class="time-slots-grid"></div>
                        </div>

                        <!-- Appointment summary -->
                        <div id="appointmentSummary" class="appointment-summary hidden">
                            <p class="mb-1"><strong><spring:message code="appointment.summary.title"/></strong></p>
                            <p id="appointmentSummaryText" class="mb-0"></p>
                        </div>
                        <form:errors path="appointmentDate" cssClass="error-message" />
                        <form:errors path="appointmentHour" cssClass="error-message" />
                    </div>

                    <!-- Reason for appointment -->
                    <div class="form-group">
                        <label for="reason"><spring:message code="appointment.form.reason"/></label>
                        <form:textarea path="reason" id="reason" class="form-control" rows="4" />
                        <form:errors path="reason" cssClass="error-message" />
                    </div>

                    <!-- File Upload Section -->
                    <div class="form-group">
                        <label for="files">
                            <spring:message code="appointment.form.files"  />
                        </label>
                        <div class="file-upload-container">
                            <div class="file-upload-dropzone" id="dropZone">
                                <div class="file-upload-icon">
                                    <i class="fas fa-cloud-upload-alt"></i>
                                </div>
                                <div class="file-upload-text">
                                    <p class="file-upload-primary"><spring:message code="file.upload.dragHere"/></p>
                                    <p class="file-upload-secondary"><spring:message code="file.upload.onlyPdf"/></p>
                                </div>
                                <form:input type="file" path="files" id="files" multiple="true" accept=".pdf" class="file-upload-input-hidden" />
                            </div>
                            <div id="filePreview" class="file-upload-preview"></div>
                            <form:errors path="files" cssClass="error-message" />
                        </div>
                    </div>
                <div class="form-group">
                    <label class="flex items-center space-x-2">
                        <form:checkbox path="allowFullHistory" />
                        <span><spring:message code="allow.full.history" /></span>
                    </label>
                </div>

                    <div class="form-group">
                        <button type="submit" class="btn btn-primary btn-block" >
                            <span>
                                <i class="fas fa-calendar-check"></i>
                                <spring:message code="appointment.form.submit" />
                            </span>
                        </button>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</main>

<script src="<c:url value="/js/toast-notification.js"/>"></script>
<!-- Include the external JavaScript file -->
<script src="<c:url value='/js/date-time-picker.js'/>"></script>
<script src="<c:url value='/js/file-upload.js'/>"></script>

<script>
    // Create a messages object to be used by the JavaScript
    window.appointmentMessages = {
        months: [
            '<spring:message code="calendar.month.january" />',
            '<spring:message code="calendar.month.february" />',
            '<spring:message code="calendar.month.march" />',
            '<spring:message code="calendar.month.april" />',
            '<spring:message code="calendar.month.may" />',
            '<spring:message code="calendar.month.june" />',
            '<spring:message code="calendar.month.july" />',
            '<spring:message code="calendar.month.august" />',
            '<spring:message code="calendar.month.september" />',
            '<spring:message code="calendar.month.october" />',
            '<spring:message code="calendar.month.november" />',
            '<spring:message code="calendar.month.december" />'
        ],
        weekdays: [
            '<spring:message code="calendar.day.sunday" />',
            '<spring:message code="calendar.day.monday" />',
            '<spring:message code="calendar.day.tuesday" />',
            '<spring:message code="calendar.day.wednesday" />',
            '<spring:message code="calendar.day.thursday" />',
            '<spring:message code="calendar.day.friday" />',
            '<spring:message code="calendar.day.saturday" />'
        ],
        weekdaysShort: [
            '<spring:message code="calendar.day.short.sun" />',
            '<spring:message code="calendar.day.short.mon" />',
            '<spring:message code="calendar.day.short.tue" />',
            '<spring:message code="calendar.day.short.wed" />',
            '<spring:message code="calendar.day.short.thu" />',
            '<spring:message code="calendar.day.short.fri" />',
            '<spring:message code="calendar.day.short.sat" />'
        ],
        appointmentAt: '<spring:message code="appointment.at" />',
        noAvailableSlots: '<spring:message code="appointment.noAvailableHours" />',
        fileUpload: {
            dragHere: '<spring:message code="file.upload.dragHere"  />',
            onlyPdf: '<spring:message code="file.upload.onlyPdf"  />',
            tooManyFiles: '<spring:message code="file.upload.tooManyFiles"  />',
            invalidType: '<spring:message code="file.upload.invalidType"  />',
            fileAdded: '<spring:message code="file.upload.fileAdded"  />',
            fileRemoved: '<spring:message code="file.upload.fileRemoved" />',
            fileTooLarge: '<spring:message code="file.upload.fileTooLarge" />'
        },
        selectOffice: '<spring:message code="appointment.form.selectOffice" javaScriptEscape="true" />',
        officeRequired: '<spring:message code="appointment.form.officeRequired" javaScriptEscape="true" />'
    };
    const contextPath = "${pageContext.request.contextPath}";
    const doctorId = "${doctor.id}";
    let currentOfficeId = "0";


    const unavailabilitySlots = [
        <c:forEach var="slot" items="${unavailabilitySlots}" varStatus="status">
        {
            startDate: "${slot.startDate}",
            endDate: "${slot.endDate}"
        }<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    ];

    const availabilitySlots = [
        <c:forEach var="slot" items="${doctor.availabilitySlots}" varStatus="status">
        {
            dayOfWeek: ${slot.dayOfWeek},
            startTime: ${slot.startTime.hour},
            endTime: ${slot.endTime.hour},
            slots: ${slot.endTime.hour - slot.startTime.hour + 1}
        }<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    ];

    const argDate = new Date().toLocaleString("en-US", {
        timeZone: "America/Argentina/Buenos_Aires",
    });
    const today = new Date(argDate);

    const FutureAppointments = [
        <c:forEach items="${appointmentsByDate}" var="appointment" varStatus="status">
        {
            date: "${appointment.key}",
            hours: ${appointment.value}
        }<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    ];

    const doctorOffices = [
        <c:forEach items="${doctorOffices}" var="office" varStatus="status">
        {
            id: ${office.id},
            neighborhoodId: ${office.neighborhood.id},
            neighborhoodName: "${office.neighborhood.name}",
            officeName: "${office.officeName}",
            specialtyIds: [<c:forEach items="${office.specialties}" var="specialty" varStatus="specStatus">${specialty.id}<c:if test="${!specStatus.last}">,</c:if></c:forEach>]
        }<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    ];

    // Handle specialty selection
    document.addEventListener('DOMContentLoaded', function() {
        const specialtySelect = document.getElementById('specialtySelect');
        const specialtyId = document.getElementById('specialtyId');
        const officeSelect = document.getElementById('officeSelect');
        const appointmentForm = document.getElementById('appointmentForm');

        // Set initial value
        if (specialtySelect && specialtyId) {
            specialtyId.value = specialtySelect.value;
            updateOfficeOptions(specialtySelect.value);
        }

        // Update hidden field and form action when specialty changes
        if (specialtySelect) {
            specialtySelect.addEventListener('change', function() {
                if (specialtyId) {
                    specialtyId.value = this.value;
                }

                // Update office options based on selected specialty
                updateOfficeOptions(this.value);

                // Clear office selection
                clearOfficeSelection();

                // Update form action with selected specialty
                if (appointmentForm) {
                    const doctorId = ${doctor.id};
                    appointmentForm.action = contextPath + `/appointment?doctorId=` + doctorId;
                }
            });
        }

        // Handle office selection
        if (officeSelect) {
            officeSelect.addEventListener('change', function() {
                currentOfficeId = this.value;
                clearDateTimePicker();
                handleOfficeSelection(this.value);
            });
        }

        // Set initial form action
        if (appointmentForm && specialtySelect) {
            const doctorId = ${doctor.id};
            appointmentForm.action = contextPath + `/appointment?doctorId=`+ doctorId;
        }
    });

    function clearDateTimePicker() {
        const datePickerInput = document.getElementById('datePickerInput');
        const appointmentDate = document.getElementById('appointmentDate');
        const appointmentHour = document.getElementById('appointmentHour');
        const timeSlotsContainer = document.getElementById('timeSlotsContainer');
        const timeSlots = document.getElementById('timeSlots');
        const appointmentSummary = document.getElementById('appointmentSummary');
        const appointmentSummaryText = document.getElementById('appointmentSummaryText');

        if (datePickerInput) {
            datePickerInput.value = '';
        }
        if (appointmentDate) {
            appointmentDate.value = '';
        }
        if (appointmentHour) {
            appointmentHour.value = '';
        }
        if (timeSlotsContainer) {
            timeSlotsContainer.style.display = 'none';
        }
        if (timeSlots) {
            timeSlots.innerHTML = '';
        }
        if (appointmentSummary) {
            appointmentSummary.classList.add('hidden');
            appointmentSummaryText.textContent = '';
        }
    }
    function submitOnce(form) {
        if (form.getAttribute('data-submitting') === 'true') {
            return false;
        }

        // Validate office selection
        const officeSelect = document.getElementById('officeSelect');
        const officeCard = document.querySelector('.office-card-appointment');

        if (officeCard && officeCard.style.display !== 'none' && officeSelect && !officeSelect.value) {
            alert(window.appointmentMessages?.officeRequired || 'Please select an office');
            officeSelect.focus();
            return false;
        }

        form.setAttribute('data-submitting', 'true');

        const submitButton = form.querySelector('button[type="submit"]');
        if (submitButton) {
            setTimeout(function() {
                submitButton.disabled = true;
            }, 10);
        }
        return true;
    }

    // Function to update office options based on selected specialty
    function updateOfficeOptions(selectedSpecialtyId) {
        const officeSelect = document.getElementById('officeSelect');
        if (!officeSelect) return;

        // Clear existing options except the first one
        officeSelect.innerHTML = '<option value="">' +
            (window.appointmentMessages?.selectOffice || 'Select an office') + '</option>';

        // Filter offices that handle the selected specialty
        const filteredOffices = doctorOffices.filter(office =>
            office.specialtyIds.includes(parseInt(selectedSpecialtyId))
        );

        // Add filtered offices to the select
        filteredOffices.forEach(office => {
            const option = document.createElement('option');
            option.value = office.id; // Use the office ID as the value
            option.textContent = office.officeName + ` - ` + office.neighborhoodName;
            option.dataset.neighborhoodId = office.neighborhoodId;
            option.dataset.neighborhoodName = office.neighborhoodName;
            option.dataset.officeName = office.officeName;
            officeSelect.appendChild(option);
        });

        // Show/hide office select based on available options
        const officeCard = document.querySelector('.office-card-appointment');
        if (officeCard) {
            if (filteredOffices.length > 0) {
                officeCard.style.display = 'flex';
                officeSelect.required = true;
            } else {
                officeCard.style.display = 'none';
                officeSelect.required = false;
            }
        }
    }

    // Function to handle office selection
    // Function to handle office selection
    function handleOfficeSelection(selectedOfficeId) {
        const officeDetails = document.getElementById('officeDetails');
        const selectedOfficeName = document.getElementById('selectedOfficeName');
        const selectedOfficeNeighborhood = document.getElementById('selectedOfficeNeighborhood');
        const officeSelect = document.getElementById('officeSelect');

        if (selectedOfficeId) {
            // Find the selected option to get the data attributes
            const selectedOption = officeSelect.querySelector(`option[value="` + selectedOfficeId + `"]`);

            if (selectedOption) {
                const officeName = selectedOption.dataset.officeName;
                const neighborhoodName = selectedOption.dataset.neighborhoodName;

                // Update display
                if (selectedOfficeName) {
                    selectedOfficeName.textContent = officeName;
                }
                if (selectedOfficeNeighborhood) {
                    selectedOfficeNeighborhood.textContent = neighborhoodName;
                }
                if (officeDetails) {
                    officeDetails.style.display = 'block';
                }
            }
        } else {
            clearOfficeSelection();
        }
    }

    // Function to clear office selection
    // Function to clear office selection
    function clearOfficeSelection() {
        const officeDetails = document.getElementById('officeDetails');
        const officeSelect = document.getElementById('officeSelect');

        if (officeDetails) {
            officeDetails.style.display = 'none';
        }
        if (officeSelect) {
            officeSelect.value = '';
        }
        currentOfficeId = "0";
        clearDateTimePicker();
    }

</script>
</body>
</html>