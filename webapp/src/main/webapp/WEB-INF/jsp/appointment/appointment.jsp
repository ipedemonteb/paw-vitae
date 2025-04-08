<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="comp" tagdir="/WEB-INF/tags/components" %>

<layout:page title="appointment.page.title">
    <!-- Add Flatpickr CSS in the head section -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">

    <!-- Custom CSS for time slots -->
    <style>
        .time-slots-container {
            margin-top: 15px;
        }

        .time-slots-grid {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 10px;
            margin-top: 10px;
        }

        .time-slot-btn {
            padding: 10px;
            border: 1px solid #dee2e6;
            border-radius: 4px;
            background-color: #f8f9fa;
            cursor: pointer;
            text-align: center;
            transition: all 0.2s;
        }

        .time-slot-btn:hover {
            background-color: #e9ecef;
        }

        .time-slot-btn.active {
            background-color: #007bff;
            color: white;
            border-color: #007bff;
        }

        .appointment-summary {
            margin-top: 15px;
            padding: 10px;
            background-color: #f8f9fa;
            border-radius: 4px;
            border-left: 4px solid #007bff;
        }

        .appointment-summary.hidden {
            display: none;
        }
    </style>

    <div class="card">
        <div class="card-header">
            <h1 class="card-title"><spring:message code="appointment.title" /></h1>
            <p class="card-subtitle"><spring:message code="appointment.subtitle" /></p>
        </div>

        <div class="card-body">
            <c:if test="${not empty doctor}">
                <comp:doctor-info doctor="${doctor}" />
            </c:if>

            <form:form modelAttribute="appointmentForm" method="post" action="${pageContext.request.contextPath}/appointment">
                <!-- Hidden field for doctor ID -->
                <form:hidden path="doctorId" />
                <form:hidden path="specialtyId" />
                <form:hidden path="clientId" />

                <div class="form-row">
                    <comp:form-group path="name" label="appointment.form.firstName" />
                    <comp:form-group path="lastName" label="appointment.form.lastName" />
                </div>

                <div class="form-row">
                    <comp:form-group path="email" label="appointment.form.email" type="email" />
                    <comp:form-group path="phone" label="appointment.form.phone" />
                </div>

                <div class="form-group">
                    <label for="specialty"><spring:message code="appointment.form.specialty" /></label>
                    <input type="text" id="specialty" name="specialty" class="form-control bg-light"
                           value="<spring:message code='${specialty.key}' />" readonly />
                </div>

                <div class="form-group">
                    <label for="coverageId"><spring:message code="appointment.form.coverage" /></label>
                    <form:select path="coverageId" id="coverageId" class="form-control">
                        <option value=""><spring:message code="appointment.placeholder.coverage" /></option>
                        <c:forEach var="coverage" items="${coverages}">
                            <option value="${coverage.id}">${coverage.name}</option>
                        </c:forEach>
                    </form:select>
                    <form:errors path="coverageId" cssClass="error-message" />
                </div>

                <div class="form-group">
                    <label for="appointmentDatePicker"><spring:message code="appointment.form.datetime"/></label>
                    <input type="text" id="appointmentDatePicker" class="form-control" placeholder="<spring:message code="appointment.placeholder.selectDate"/>" readonly>

                    <input type="hidden" id="appointmentDate" name="appointmentDate">
                    <input type="hidden" id="appointmentHour" name="appointmentHour">

                    <div id="timeSlotsContainer" class="time-slots-container" style="display: none;">
                        <h6><spring:message code="appointment.form.selectTime"/></h6>
                        <div id="timeSlots" class="time-slots-grid"></div>
                    </div>

                    <div id="appointmentSummary" class="appointment-summary hidden">
                        <p class="mb-1"><strong><spring:message code="appointment.summary.title"/></strong></p>
                        <p id="appointmentSummaryText" class="mb-0"></p>
                    </div>
                </div>

                <comp:form-group path="reason" label="appointment.form.reason" type="textarea" />

                <div class="form-group">
                    <button type="submit" class="btn btn-primary btn-block">
                        <spring:message code="appointment.form.submit" />
                    </button>
                </div>
            </form:form>
        </div>
    </div>


    <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const datePickerInput = document.getElementById('appointmentDatePicker');
            const dateInput = document.getElementById('appointmentDate');
            const hourInput = document.getElementById('appointmentHour');
            const timeSlotsContainer = document.getElementById('timeSlotsContainer');
            const timeSlotsGrid = document.getElementById('timeSlots');
            const appointmentSummary = document.getElementById('appointmentSummary');
            const appointmentSummaryText = document.getElementById('appointmentSummaryText');
            const doctorId = document.getElementById('doctorId').value;

            const dayNames = [
                '<spring:message code="calendar.day.sunday"/>',
                '<spring:message code="calendar.day.monday"/>',
                '<spring:message code="calendar.day.tuesday"/>',
                '<spring:message code="calendar.day.wednesday"/>',
                '<spring:message code="calendar.day.thursday"/>',
                '<spring:message code="calendar.day.friday"/>',
                '<spring:message code="calendar.day.saturday"/>'
            ];

            const monthNames = [
                '<spring:message code="calendar.month.january"/>',
                '<spring:message code="calendar.month.february"/>',
                '<spring:message code="calendar.month.march"/>',
                '<spring:message code="calendar.month.april"/>',
                '<spring:message code="calendar.month.may"/>',
                '<spring:message code="calendar.month.june"/>',
                '<spring:message code="calendar.month.july"/>',
                '<spring:message code="calendar.month.august"/>',
                '<spring:message code="calendar.month.september"/>',
                '<spring:message code="calendar.month.october"/>',
                '<spring:message code="calendar.month.november"/>',
                '<spring:message code="calendar.month.december"/>'
            ];

            const flatpickrInstance = flatpickr(datePickerInput, {
                minDate: "today",
                maxDate: new Date().fp_incr(30),
                dateFormat: "Y-m-d",
                disableMobile: true,
                locale: {
                    firstDayOfWeek: 1,
                    weekdays: {
                        shorthand: dayNames.map(day => day.substring(0, 3)),
                        longhand: dayNames
                    },
                    months: {
                        shorthand: monthNames.map(month => month.substring(0, 3)),
                        longhand: monthNames
                    }
                },
                onChange: function(selectedDates, dateStr) {
                    if (selectedDates.length > 0) {
                        dateInput.value = dateStr;
                        hourInput.value = '';
                        fetchAvailableHours(dateStr);
                    } else {
                        timeSlotsContainer.style.display = 'none';
                        appointmentSummary.classList.add('hidden');
                    }
                }
            });

            function fetchAvailableHours(selectedDate) {

                timeSlotsGrid.innerHTML = '<div class="text-center w-100"><div class="spinner-border text-primary" role="status"><span class="sr-only">Loading...</span></div></div>';
                timeSlotsContainer.style.display = 'block';

                fetch(`${pageContext.request.contextPath}/appointment/available-hours?doctorId=${doctor.id}&date=` + selectedDate)
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('HTTP error! status: ' + response.status);
                        }
                        return response.json();
                    })
                    .then(data => {
                        renderTimeSlots(data.bookedHours, selectedDate);
                    })
                    .catch(error => {
                        console.error('Error fetching available hours:', error);
                        timeSlotsGrid.innerHTML = '<div class="alert alert-danger"><spring:message code="appointment.error.fetchHours"/></div>';
                    });
            }

            function renderTimeSlots(bookedHours, selectedDate) {

                timeSlotsGrid.innerHTML = '';
                const allHours = Array.from({ length: 11 }, (_, i) => i + 8);
                const availableHours = allHours.filter(hour => !bookedHours.includes(hour.toString()));

                if (availableHours.length === 0) {
                    // No available hours
                    timeSlotsGrid.innerHTML = '<div class="alert alert-info w-100 text-center"><spring:message code="appointment.noAvailableHours"/></div>';
                    return;
                }

                availableHours.forEach(hour => {
                    const timeButton = document.createElement('button');
                    timeButton.type = 'button';
                    timeButton.className = 'time-slot-btn';
                    timeButton.textContent = hour + `:00`;
                    timeButton.dataset.hour = hour;

                    timeButton.addEventListener('click', function() {
                        document.querySelectorAll('.time-slot-btn').forEach(btn => {
                            btn.classList.remove('active');
                        });
                        this.classList.add('active');
                        hourInput.value = hour;
                        updateAppointmentSummary(selectedDate, hour);
                    });

                    if (hourInput.value && parseInt(hourInput.value) === hour) {
                        timeButton.classList.add('active');
                    }

                    timeSlotsGrid.appendChild(timeButton);
                });
            }

            function updateAppointmentSummary(dateStr, hour) {
                if (!dateStr || !hour) {
                    appointmentSummary.classList.add('hidden');
                    return;
                }

                const date = new Date(dateStr);
                const dayName = dayNames[date.getDay()];
                const monthName = monthNames[date.getMonth()];
                const dayOfMonth = date.getDate();
                const year = date.getFullYear();

                const formattedDateTime = dayName + `, ` + monthName + ` ` + dayOfMonth + `, ` + year + <spring:message code="appointment.at"/> + hour + `:00`;
                appointmentSummaryText.innerHTML = formattedDateTime;
                appointmentSummary.classList.remove('hidden');
            }

            if (dateInput.value) {
                flatpickrInstance.setDate(dateInput.value);
                fetchAvailableHours(dateInput.value);
                if (hourInput.value) {
                    updateAppointmentSummary(dateInput.value, hourInput.value);
                }
            }
        });
    </script>
</layout:page>