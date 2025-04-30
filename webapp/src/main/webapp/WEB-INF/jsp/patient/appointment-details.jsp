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
    <link rel="stylesheet" href="<c:url value='/css/appointment-details.css' />" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
<!-- Include the header -->
<jsp:include page="/WEB-INF/jsp/components/header.jsp" />

<!-- Main Content -->
<main class="main-content">
    <div class="container">
        <!-- Back button to My Appointments -->

        <div class="appointment-container">
            <div class="appointment-header">
                <h1 class="appointment-title"><spring:message code="appointment.details.page.title" /></h1>
                <p class="appointment-subtitle"><spring:message code="appointment.subtitle" /></p>
            </div>

            <div class="appointment-body">
                <c:set var="doctor" value="${appointment.doctor}" />
                <!-- Display doctor information if available -->
                <c:if test="${not empty doctor}">
                    <div class="doctor-info">
                        <div class="doctor-image">
                            <img src="<c:url value='/doctor/${doctor.id}/image'/>" alt="<c:out value="${doctor.name}"/> <c:out value="${doctor.lastName}"/>" class="doctor-avatar">
                        </div>
                        <div class="doctor-details">
                            <h3 class="doctor-name"><c:out value="${doctor.name}"/> <c:out value="${doctor.lastName}"/></h3>
                            <div class="card-specialty-list">
                                <c:forEach var="specialty" items="${doctor.specialtyList}" varStatus="status">
                                    <p class="doctor-specialty"><spring:message code="${specialty.key}" /></p>
                                    <c:if test="${!status.last}">
                                        <span style="white-space: pre">, </span>
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

                <!-- Status field - NEW -->
                <div class="specialty-card-appointment status-card">
                    <div class="specialty-icon-appointment">
                        <i class="fas fa-clipboard-check"></i>
                    </div>
                    <div class="specialty-content">
                        <span class="specialty-label-appointment"><spring:message code="appointment.details.info.status" text="Status" />:</span>
                        <div class="status-display">
                            <span class="specialty-value-appointment">
                                <spring:message code="appointment.status.${appointment.status}" text="${appointment.status}" />
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
                        <span class="specialty-label-appointment"><spring:message code="appointment.form.date" text="Date" />:</span>
                        <div class="enhanced-date-display">
                            <div class="date-time-info">
                                <div class="date-info">
                                    <i class="fas fa-calendar-alt"></i>
                                    <c:out value="${appointment.date.toLocalDate()}"/>                                </div>
                                <div class="time-info">
                                    <i class="fas fa-clock"></i>
                                    <c:out value="${appointment.date.toLocalTime()}"/>                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="specialty-card-appointment">
                    <div class="specialty-icon-appointment">
                        <i class="fas fa-stethoscope"></i>
                    </div>
                    <div class="specialty-content">
                        <span class="specialty-label-appointment"><spring:message code="appointment.form.specialty" />:</span>
                        <span class="specialty-value-appointment"><spring:message code="${appointment.specialty.key}" /></span>
                    </div>
                </div>

                <div class="specialty-card-appointment">
                    <div class="specialty-icon-appointment">
                        <i class="fas fa-comment"></i>
                    </div>
                    <div class="specialty-content">
                        <span class="specialty-label-appointment"><spring:message code="appointment.form.reason" />:</span>
                        <span class="specialty-value-appointment"><c:out value="${appointment.reason}"/> </span>
                    </div>
                </div>

                <div class="files-list">
                    <lable class="files-lable"><spring:message code="appointment.details.patient.files.title"/></lable>
                    <c:forEach var="file" items="${patientFiles}">
                        <c:if test="${file.uploader_role == 'patient'}">
                            <div class="file-item">
                                <div class="file-icon">
                                    <i class="far fa-file-pdf"></i>
                                </div>
                                <div class="file-info">
                                    <div class="file-name"><c:out value="${file.fileName}" /></div>
                                </div>
                                <a href="<c:url value='/appointment/${appointment.id}/file/${file.id}'/>" class="file-download" download>
                                    <i class="fas fa-download"></i>
                                </a>
                            </div>
                        </c:if>
                    </c:forEach>
                </div>
                <div class="back-button-container">
                    <a href="${pageContext.request.contextPath}/patient/dashboard" class="back-button">
                        <i class="fas fa-arrow-left"></i>
                        <span><spring:message code="appointment.details.back" text="Back to My Appointments" /></span>
                    </a>
                </div>
            </div>
        </div>
    </div>
</main>

<!-- Add message translations for JavaScript -->
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
        }<c:if test="${!status.last}">,</c:if>
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
        }<c:if test="${!status.last}">,</c:if>
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
            } else if(acc[date].includes(hour)) {
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
</script>

<!-- Include the external JavaScript file -->
<script src="<c:url value='/js/date-time-picker.js'/>"></script>
<script src="<c:url value='/js/file-upload.js'/>"></script>
</body>
</html>
