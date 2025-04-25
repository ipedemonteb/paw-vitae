<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="comp" tagdir="/WEB-INF/tags/components" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link rel="stylesheet" href="<c:url value='/css/components/forms.css' />" />
<layout:page title="appointment.page.title">
    <div class="card">
        <div class="card-header">
            <h1 class="card-title"><spring:message code="appointment.title" /></h1>
            <p class="card-subtitle"><spring:message code="appointment.subtitle" /></p>
        </div>

        <div class="card-body">
            <!-- Display doctor information if available -->
            <c:if test="${not empty doctor}">
                <comp:doctor-info doctor="${doctor}" />
            </c:if>

            <form:form modelAttribute="appointmentForm" method="post" action="${pageContext.request.contextPath}/appointment?specialtyId=${specialty.id}&doctorId=${doctor.id}" class="appointment-form">
                <div class="specialty-card-appointment">
                    <div class="specialty-icon-appointment">
                        &#9877;
                    </div>
                    <div class="specialty-content">
                        <span class="specialty-label-appointment"><spring:message code="appointment.form.specialty" />:</span>
                        <span class="specialty-value-appointment"><spring:message code="${specialty.key}" /></span>
                    </div>
                </div>

                <!-- Custom Date and Time Picker -->
                <div class="form-group">
                    <label for="datePickerInput" class="required-field"><spring:message code="appointment.form.datetime"/></label>
                    <div class="date-picker-container">
                        <div class="date-picker-container">
                            <input type="text" id="datePickerInput" class="form-control date-picker-input"
                                   placeholder="<spring:message code='appointment.placeholder.selectDate'/>" readonly>
                            <form:errors path="appointmentDate" cssClass="error-message" />
                            <form:errors path="appointmentHour" cssClass="error-message" />
                        </div>

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

                        <!-- Hidden fields to store actual values -->
                        <input type="hidden" id="appointmentDate" name="appointmentDate">
                        <input type="hidden" id="appointmentHour" name="appointmentHour">
                    </div>

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
                </div>

                <comp:form-group path="reason" label="appointment.form.reason" type="textarea" />

                <div class="form-group">
                    <button type="submit" class="btn btn-primary btn-block" onclick="this.disabled=true; this.form.submit();">
                        <spring:message code="appointment.form.submit" />
                    </button>
                </div>
            </form:form>
        </div>
    </div>

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
            noAvailableSlots: '<spring:message code="appointment.noAvailableHours" />'
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

        console.log(FutureAppointments)
    </script>

    <!-- Include the external JavaScript file -->
    <script src="<c:url value='/js/date-time-picker.js'/>"></script>
</layout:page>
