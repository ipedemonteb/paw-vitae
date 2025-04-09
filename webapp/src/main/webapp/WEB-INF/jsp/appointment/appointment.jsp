<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="comp" tagdir="/WEB-INF/tags/components" %>

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

                <!-- Custom Date and Time Picker -->
                <div class="form-group">
                    <label for="datePickerInput"><spring:message code="appointment.form.datetime"/></label>
                    <div class="date-picker-container">
                        <input type="text" id="datePickerInput" class="form-control date-picker-input"
                               placeholder="<spring:message code="appointment.placeholder.selectDate"/>" readonly>

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
                        <h6><spring:message code="appointment.form.selectTime"/></h6>
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
                    <button type="submit" class="btn btn-primary btn-block">
                        <spring:message code="appointment.form.submit" />
                    </button>
                </div>
            </form:form>
        </div>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Elements
            const datePickerInput = document.getElementById('datePickerInput');
            const datePickerCalendar = document.getElementById('datePickerCalendar');
            const currentMonthYear = document.getElementById('currentMonthYear');
            const calendarWeekdays = document.getElementById('calendarWeekdays');
            const calendarDays = document.getElementById('calendarDays');
            const prevMonthBtn = document.getElementById('prevMonthBtn');
            const nextMonthBtn = document.getElementById('nextMonthBtn');
            const dateInput = document.getElementById('appointmentDate');
            const hourInput = document.getElementById('appointmentHour');
            const timeSlotsContainer = document.getElementById('timeSlotsContainer');
            const timeSlotsGrid = document.getElementById('timeSlots');
            const appointmentSummary = document.getElementById('appointmentSummary');
            const appointmentSummaryText = document.getElementById('appointmentSummaryText');
            const doctorId = document.getElementById('doctorId').value;

            // Calendar state
            let currentDate = new Date();
            let selectedDate = null;
            let isCalendarOpen = false;

            // Day and month names for localization
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

            // Initialize weekday headers
            function initWeekdays() {
                calendarWeekdays.innerHTML = '';
                for (let i = 0; i < 7; i++) {
                    const dayIndex = (i + 1) % 7; // Start with Monday (1) as first day of week
                    const dayElement = document.createElement('div');
                    dayElement.textContent = dayNames[dayIndex].substring(0, 3);
                    calendarWeekdays.appendChild(dayElement);
                }
            }

            // Generate calendar for a given month and year
            function generateCalendar(month, year) {
                // Clear previous days
                calendarDays.innerHTML = '';

                // Update header
                currentMonthYear.textContent = monthNames[month] + ' ' + year;

                // Get first day of month and number of days
                const firstDay = new Date(year, month, 1);
                const lastDay = new Date(year, month + 1, 0);
                const daysInMonth = lastDay.getDate();

                // Get day of week for first day (0 = Sunday, 1 = Monday, etc.)
                let firstDayOfWeek = firstDay.getDay();
                // Adjust for Monday as first day of week
                firstDayOfWeek = firstDayOfWeek === 0 ? 6 : firstDayOfWeek - 1;

                // Get today's date for highlighting
                const today = new Date();
                const todayDate = today.getDate();
                const todayMonth = today.getMonth();
                const todayYear = today.getFullYear();

                // Get days from previous month to fill first row
                const prevMonth = month === 0 ? 11 : month - 1;
                const prevYear = month === 0 ? year - 1 : year;
                const daysInPrevMonth = new Date(prevYear, prevMonth + 1, 0).getDate();

                // Add days from previous month
                for (let i = 0; i < firstDayOfWeek; i++) {
                    const dayElement = document.createElement('div');
                    const dayNumber = daysInPrevMonth - firstDayOfWeek + i + 1;
                    dayElement.textContent = dayNumber;
                    dayElement.className = 'date-picker-day outside-month disabled';
                    calendarDays.appendChild(dayElement);
                }

                // Add days of current month
                const minDate = new Date(todayYear, todayMonth, todayDate);
                const maxDate = new Date(todayYear, todayMonth, todayDate + 30); // 30 days from today

                for (let i = 1; i <= daysInMonth; i++) {
                    const dayElement = document.createElement('div');
                    dayElement.textContent = i;
                    dayElement.className = 'date-picker-day';

                    // Check if this day is today
                    if (i === todayDate && month === todayMonth && year === todayYear) {
                        dayElement.classList.add('today');
                    }

                    // Check if this day is selected
                    if (selectedDate && i === selectedDate.getDate() &&
                        month === selectedDate.getMonth() &&
                        year === selectedDate.getFullYear()) {
                        dayElement.classList.add('selected');
                    }

                    // Check if this day is in the allowed range (today to 30 days from now)
                    const currentDay = new Date(year, month, i);
                    if (currentDay < minDate || currentDay > maxDate) {
                        dayElement.classList.add('disabled');
                    } else {
                        // Add click event for selectable days
                        dayElement.addEventListener('click', function() {
                            selectDate(new Date(year, month, i));
                        });
                    }

                    calendarDays.appendChild(dayElement);
                }

                // Fill remaining slots with days from next month
                const totalDaysDisplayed = firstDayOfWeek + daysInMonth;
                const remainingSlots = 42 - totalDaysDisplayed; // 6 rows of 7 days

                for (let i = 1; i <= remainingSlots; i++) {
                    const dayElement = document.createElement('div');
                    dayElement.textContent = i;
                    dayElement.className = 'date-picker-day outside-month disabled';
                    calendarDays.appendChild(dayElement);
                }
            }

            // Handle date selection
            function selectDate(date) {
                selectedDate = date;

                // Format date for display and hidden input
                const year = date.getFullYear();
                const month = (date.getMonth() + 1).toString().padStart(2, '0');
                const day = date.getDate().toString().padStart(2, '0');
                const formattedDate = year + '-' + month + '-' + day;

                // Update input fields
                datePickerInput.value = day + ' ' + monthNames[date.getMonth()].substring(0, 3) + ' ' + year;
                dateInput.value = formattedDate;

                // Close calendar
                closeCalendar();

                // Clear previously selected time
                hourInput.value = '';

                // Fetch available hours for the selected date
                fetchAvailableHours(formattedDate);

                // Update calendar UI (in case it's reopened)
                generateCalendar(date.getMonth(), date.getFullYear());
            }

            // Toggle calendar visibility
            function toggleCalendar() {
                if (isCalendarOpen) {
                    closeCalendar();
                } else {
                    openCalendar();
                }
            }

            function openCalendar() {
                datePickerCalendar.classList.add('show');
                isCalendarOpen = true;

                // If a date is already selected, show that month
                if (selectedDate) {
                    generateCalendar(selectedDate.getMonth(), selectedDate.getFullYear());
                } else {
                    generateCalendar(currentDate.getMonth(), currentDate.getFullYear());
                }
            }

            function closeCalendar() {
                datePickerCalendar.classList.remove('show');
                isCalendarOpen = false;
            }

            // Function to fetch available hours from the server
            function fetchAvailableHours(selectedDate) {
                // Show loading indicator
                timeSlotsGrid.innerHTML = '<div class="date-time-picker-loading"><div class="spinner"></div></div>';
                timeSlotsContainer.style.display = 'block';

                // Fetch available hours from the server
                fetch('${pageContext.request.contextPath}/appointment/available-hours?doctorId=' + doctorId + '&date=' + selectedDate)
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
                        timeSlotsGrid.innerHTML = '<div class="date-time-picker-alert error"><spring:message code="appointment.error.fetchHours"/></div>';
                    });
            }

            // Function to render time slots
            function renderTimeSlots(bookedHours, selectedDate) {
                // Clear previous time slots
                timeSlotsGrid.innerHTML = '';

                // Generate all possible hours (8 AM to 6 PM)
                const allHours = Array.from({ length: 11 }, (_, i) => i + 8);

                // Filter out booked hours
                const availableHours = allHours.filter(hour => !bookedHours.includes(hour.toString()));

                if (availableHours.length === 0) {
                    // No available hours
                    timeSlotsGrid.innerHTML = '<div class="date-time-picker-alert info"><spring:message code="appointment.noAvailableHours"/></div>';
                    return;
                }

                // Create buttons for each available hour
                availableHours.forEach(hour => {
                    const timeButton = document.createElement('button');
                    timeButton.type = 'button';
                    timeButton.className = 'time-slot-btn';
                    timeButton.textContent = hour + ':00';
                    timeButton.dataset.hour = hour;

                    // Add click event to select time
                    timeButton.addEventListener('click', function() {
                        // Remove active class from all buttons
                        document.querySelectorAll('.time-slot-btn').forEach(btn => {
                            btn.classList.remove('active');
                        });

                        // Add active class to this button
                        this.classList.add('active');

                        // Store the selected hour
                        hourInput.value = hour;

                        // Update appointment summary
                        updateAppointmentSummary(selectedDate, hour);
                    });

                    // If this hour was previously selected, mark it as active
                    if (hourInput.value && parseInt(hourInput.value) === hour) {
                        timeButton.classList.add('active');
                    }

                    timeSlotsGrid.appendChild(timeButton);
                });
            }

            // Function to update appointment summary
            function updateAppointmentSummary(dateStr, hour) {
                if (!dateStr || !hour) {
                    appointmentSummary.classList.add('hidden');
                    return;
                }

                // Parse the date
                const parts = dateStr.split('-');
                const year = parseInt(parts[0], 10);
                const month = parseInt(parts[1], 10) - 1;
                const day = parseInt(parts[2], 10);

                const date = new Date(year, month, day);
                const dayName = dayNames[date.getDay()];
                const monthName = monthNames[date.getMonth()];

                // Format the date and time for display
                const formattedDateTime = dayName + ', ' + monthName + ' ' + day + ', ' + year + ' <spring:message code="appointment.at"/> ' + hour + ':00';

                // Update the summary text
                appointmentSummaryText.innerHTML = formattedDateTime;

                // Show the summary
                appointmentSummary.classList.remove('hidden');
            }

            // Set up event listeners
            datePickerInput.addEventListener('click', toggleCalendar);

            prevMonthBtn.addEventListener('click', function(e) {
                e.stopPropagation();
                currentDate.setMonth(currentDate.getMonth() - 1);
                generateCalendar(currentDate.getMonth(), currentDate.getFullYear());
            });

            nextMonthBtn.addEventListener('click', function(e) {
                e.stopPropagation();
                currentDate.setMonth(currentDate.getMonth() + 1);
                generateCalendar(currentDate.getMonth(), currentDate.getFullYear());
            });

            // Close calendar when clicking outside
            document.addEventListener('click', function(event) {
                if (isCalendarOpen && !datePickerInput.contains(event.target) &&
                    !datePickerCalendar.contains(event.target)) {
                    closeCalendar();
                }
            });

            // Initialize the calendar
            initWeekdays();

            // If date and hour are already set (e.g., when returning to the form after validation error),
            // initialize the UI accordingly
            if (dateInput.value) {
                const parts = dateInput.value.split('-');
                if (parts.length === 3) {
                    const year = parseInt(parts[0], 10);
                    const month = parseInt(parts[1], 10) - 1;
                    const day = parseInt(parts[2], 10);

                    selectedDate = new Date(year, month, day);

                    // Update the input display
                    datePickerInput.value = day + ' ' + monthNames[month].substring(0, 3) + ' ' + year;

                    // Fetch available hours
                    fetchAvailableHours(dateInput.value);

                    // Update summary if hour is also set
                    if (hourInput.value) {
                        updateAppointmentSummary(dateInput.value, hourInput.value);
                    }
                }
            }
        });
    </script>
</layout:page>