<%@ page import="java.util.TimeZone" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script src="<c:url value='/js/file-upload.js'/>"></script>

<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="icon" type="image/png"
          href="https://upload.wikimedia.org/wikipedia/commons/thumb/a/aa/V-logo.svg/2048px-V-logo.svg.png"/>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="appointment.details.page.title"/></title>
    <link rel="stylesheet" href="<c:url value='/css/appointment-details.css' />"/>
    <link rel="stylesheet" href="<c:url value='/css/toast-notification.css' />"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap"
          rel="stylesheet">
</head>
<body>
<!-- Include the header -->
<jsp:include page="/WEB-INF/jsp/components/header.jsp"/>

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
                <h1 class="appointment-title"><spring:message code="appointment.details.page.title"/></h1>
                <p class="appointment-subtitle"><spring:message code="appointment.details.subtitle"/></p>
            </div>

            <div class="appointment-body">
                <!-- Display doctor information if available -->
                <c:if test="${not empty appointment.patient}">
                    <div class="doctor-info">
                        <div class="doctor-image">
                            <img src="<c:url value='/image/-1'/>"
                                 alt="<c:out value="${doctor.name}"/> <c:out value="${doctor.lastName}"/>"
                                 class="doctor-avatar">
                        </div>
                        <div class="doctor-details">
                            <h3 class="doctor-name"><c:out value="${appointment.patient.name}"/> <c:out
                                    value="${appointment.patient.lastName}"/></h3>
                            <div class="doctor-contact">
                                <div class="contact-item">
                                    <span class="contact-icon"><i class="fas fa-envelope"></i></span>
                                    <span class="contact-text"><c:out value="${appointment.patient.email}"/></span>
                                </div>
                                <div class="contact-item">
                                    <span class="contact-icon"><i class="fas fa-phone"></i></span>
                                    <span class="contact-text"><c:out value="${appointment.patient.phone}"/></span>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>

                <!-- Status field - NEW -->
                <div class="specialty-card-appointment status-card">
                    <div class="specialty-icon-appointment">
                        <i class="fas fa-clipboard-check"></i>
                    </div>
                    <div class="specialty-content">
                        <span class="specialty-label-appointment">
                            <spring:message code="appointment.details.info.status" text="Status"/>:
                        </span>
                        <div class="status-display">
                        <span class="specialty-value-appointment status-${appointment.status}">
                            <spring:message code="appointment.status.${appointment.status}"
                                            text="${appointment.status}"/>
                        </span>
                        </div>
                    </div>
                </div>

                <!-- Improved date display -->
                <div class="specialty-card-appointment">
                    <div class="specialty-icon-appointment">
                        <i class="fas fa-calendar"></i>
                    </div>
                    <div class="specialty-content">
                        <span class="specialty-label-appointment"><spring:message code="appointment.form.date"
                                                                                  text="Date"/>:</span>
                        <div class="enhanced-date-display">
                            <div class="date-time-info">
                                <div class="date-info">
                                    <i class="fas fa-calendar-alt"></i>
                                    <c:out value="${appointment.date.toLocalDate()}"/></div>
                                <div class="time-info">
                                    <i class="fas fa-clock"></i>
                                    <c:out value="${appointment.date.toLocalTime()}"/></div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="specialty-card-appointment">
                    <div class="specialty-icon-appointment">
                        <i class="fas fa-stethoscope"></i>
                    </div>
                    <div class="specialty-content">
                        <span class="specialty-label-appointment"><spring:message
                                code="appointment.form.specialty"/>:</span>
                        <span class="specialty-value-appointment"><spring:message
                                code="${appointment.specialty.key}"/></span>
                    </div>
                </div>

                <div class="specialty-card-appointment">
                    <div class="specialty-icon-appointment">
                        <i class="fas fa-comment"></i>
                    </div>
                    <div class="specialty-content">
                        <span class="specialty-label-appointment"><spring:message
                                code="appointment.form.reason"/>:</span>
                        <span class="specialty-value-appointment"><c:out value="${appointment.reason}"/> </span>
                    </div>
                </div>


                <div class="files-section">
                    <h2 class="files-title">
                        <i class="fas fa-file-medical-alt"></i>
                        <spring:message code="appointment.details.patient.files.title"/>
                    </h2>

                    <div class="files-list">
                        <c:set var="hasPatientFiles" value="false"/>
                        <c:forEach var="file" items="${patientFiles}">
                            <c:if test="${file.uploader_role == 'patient'}">
                                <c:set var="hasPatientFiles" value="true"/>
                                <div class="file-item">
                                    <div class="file-icon">
                                        <i class="far fa-file-pdf"></i>
                                    </div>
                                    <div class="file-info">
                                        <div class="file-name"><c:out value="${file.fileName}"/></div>
                                    </div>
                                    <a href="<c:url value='/appointment/${appointment.id}/file/${file.id}'/>"
                                       class="file-download" download>
                                        <i class="fas fa-download"></i>
                                    </a>
                                </div>
                            </c:if>
                        </c:forEach>

                        <c:if test="${not hasPatientFiles}">
                            <div class="no-files-message">
                                <div class="no-files-content">
                                    <i class="fas fa-info-circle"></i>
                                    <p><spring:message code="appointment.details.patient.nofiles"/></p>
                                </div>
                            </div>
                        </c:if>
                    </div>
                </div>

                <c:if test="${appointment.status == 'completo'}">

                    <div class="rating-section">
                        <h2 class="rating-title">
                            <i class="fas fa-star"></i>
                            <spring:message code="appointment.details.review.title.doctor"/>
                        </h2>
                            <%-- Replace the existing review section with this updated version --%>
                        <c:choose>
                            <c:when test="${not empty existingRating}">
                                <!-- Display existing rating with title -->
                                <div class="review-card">
                                    <div class="review-card-content">
                                        <div class="review-header">
                                            <div class="review-stars">
                                                <c:forEach begin="1" end="5" var="star">
                                                    <i class="fa${star <= existingRating.rating ? 's' : 'r'} fa-star"
                                                       aria-hidden="true"></i>
                                                </c:forEach>
                                                <span class="rating-value"><c:out value="${existingRating.rating}"/></span>
                                            </div>
                                        </div>
                                        <div class="review-comment">
                                            <c:out value="${existingRating.comment}"/>
                                        </div>
                                    </div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <!-- Styled No Reviews Message -->
                                <div class="review-card">
                                    <div class="no-reviews-message"
                                         style="border: none; box-shadow: none; margin-bottom: 0;">
                                        <div class="no-reviews-icon">
                                            <i class="far fa-star"></i>
                                        </div>
                                        <h3 class="no-reviews-title"><spring:message code="appointment.details.review.none"
                                                                                     text="No Reviews Yet"/></h3>
                                        <p class="no-reviews-text"><spring:message
                                                code="appointment.details.review.none.message"
                                                text="This doctor hasn't received any reviews for this appointment."/></p>
                                    </div>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <form:form modelAttribute="doctorFileForm" method="post"
                               action="${pageContext.request.contextPath}/doctor/dashboard/appointment-details/${appointment.id}"
                               class="appointment-form" enctype="multipart/form-data">

                        <div class="files-section">
                            <h2 class="files-title">
                                <i class="fas fa-file-medical-alt"></i>
                                <spring:message code="appointment.details.doctor.files.title"/>
                            </h2>

                            <!-- Report textarea -->
                            <c:choose>
                                <c:when test="${empty appointment.report}">
                                    <div class="form-group">
                                        <label for="report"><spring:message code="appointment.details.report"/></label>
                                        <form:textarea path="report" id="report" class="form-control" rows="4"/>
                                        <form:errors path="report" cssClass="error-message"/>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="specialty-card-appointment">
                                        <div class="specialty-icon-appointment">
                                            <i class="fas fa-asterisk"></i>
                                        </div>
                                        <div class="specialty-content">
                                            <span class="specialty-value-appointment"><c:out value="${appointment.report}"/> </span>
                                        </div>
                                    </div>
                                </c:otherwise>
                            </c:choose>

                            <div class="files-list">
                                <c:set var="hasDoctorFiles" value="false"/>
                                <c:forEach var="file" items="${doctorFiles}">
                                    <c:if test="${file.uploader_role == 'doctor'}">
                                        <c:set var="hasDoctorFiles" value="true"/>
                                        <div class="file-item">
                                            <div class="file-icon">
                                                <i class="far fa-file-pdf"></i>
                                            </div>
                                            <div class="file-info">
                                                <div class="file-name"><c:out value="${file.fileName}"/></div>
                                            </div>
                                            <a href="<c:url value='/appointment/${appointment.id}/file/${file.id}'/>"
                                               class="file-download" download>
                                                <i class="fas fa-download"></i>
                                            </a>
                                        </div>
                                    </c:if>
                                </c:forEach>

                                <c:if test="${not hasDoctorFiles}">
                                    <div class="no-files-message">
                                        <div class="no-files-content">
                                            <i class="fas fa-info-circle"></i>
                                            <p><spring:message code="appointment.details.you.nofiles"/></p>
                                        </div>
                                    </div>
                                </c:if>
                            </div>
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
                            <button id="submitButton" type="submit" class="btn btn-primary btn-block">
                            <span>
                                <spring:message code="appointment.details.doctor.submit"/>
                            </span>
                            </button>
                        </div>
                    </form:form>
                </c:if>
                <div class="back-button-container">
                    <a href="${pageContext.request.contextPath}/doctor/dashboard" class="back-button">
                        <i class="fas fa-arrow-left"></i>
                        <span><spring:message code="appointment.details.back"/></span>
                    </a>
                </div>
            </div>
        </div>
    </div>
</main>

<script src="<c:url value="/js/toast-notification.js"/>"></script>

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
        }
    };
    contextPath = "${pageContext.request.contextPath}";

    const availabilitySlots = [
        <c:forEach var="slot" items="${doctor.availabilitySlots}" varStatus="status">
        {
            dayOfWeek: ${slot.dayOfWeek},
            startTime: ${slot.startTime.hour},
            endTime: ${slot.endTime.hour},
            slots: ${slot.endTime.hour - slot.startTime.hour + 1}
        }<c:if test="${!status.last}">, </c:if>
        </c:forEach>
    ];


    const argDate = new Date().toLocaleString("en-US", {
        timeZone: "America/Argentina/Buenos_Aires",
    });
    const today = new Date(argDate);


    const appointments = [
        <c:forEach var="app" varStatus="status" items="${doctor.appointments}">
        {
            date: "${app.date}",
            hour: ${app.date.hour},
        }<c:if test="${!status.last}">, </c:if>
        </c:forEach>
    ].filter(app => {
        const appointmentDate = new Date(app.date);
        return appointmentDate > today || (appointmentDate === today && appointmentDate.getHours() > today.getHours()); // Filter out past appointments
    })

    const futureAppointments = Object.entries(
        appointments.reduce((acc, appointment) => {
            const date = new Date(appointment.date).toISOString().split('T')[0];  // Extract local date
            const hour = appointment.hour; // Extract hour
            if (!acc[date]) {
                acc[date] = [];
            } else if (acc[date].includes(hour)) {
                return acc;
            }
            acc[date].push(hour);
            return acc;
        }, {})
    );
    const FutureAppointments = futureAppointments.map(([date, hours]) => {
        return {
            date: date,
            hours: hours
        };
    });

    const fixedHeader = document.querySelector(".main-header");
    const mainContent = document.querySelector("main");

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
    document.addEventListener('DOMContentLoaded', function() {

        const fileInput = document.getElementById('files');

        const form = document.getElementById('doctorFileForm');
        const submitButton = document.getElementById('submitButton');

        if (form && submitButton) {
            form.addEventListener('submit', function () {
                submitButton.disabled = true;
            });
        }

        fileInput.addEventListener('change', function(event) {
            if (event.target.files.length > 0) {
                showSuccessToast(); // Muestra la notificación de éxito
            }
        });

        const urlParams = new URLSearchParams(window.location.search);
        if (urlParams.get('updated') === 'true') {
            showSuccessToast();

            // Remove the query parameter without refreshing the page
            const newUrl = window.location.pathname;
            window.history.replaceState({}, document.title, newUrl);
        }
        document.addEventListener('keydown', function(e) {
            if (e.key === 'Escape') {
                document.querySelectorAll('.modal-overlay').forEach(modal => {
                    modal.classList.remove('show');
                });
                hideSuccessToast();
            }
        });
    });

</script>

</body>
</html>
