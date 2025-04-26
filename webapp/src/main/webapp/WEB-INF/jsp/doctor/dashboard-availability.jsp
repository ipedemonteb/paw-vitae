<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="dashboard" tagdir="/WEB-INF/tags/components" %>

<dashboard:layout>
    <div class="tab-content active" id="availability-tab">
        <div class="tab-header">
            <h2><spring:message code="dashboard.availability.title" /></h2>
        </div>

        <div class="profile-content">
            <div class="profile-section">
                <h3 class="section-title">
                    <i class="fas fa-calendar-alt"></i>
                    <spring:message code="dashboard.availability.currentSlots" />
                </h3>

                <form:form id="updateAvailabilityForm" modelAttribute="updateAvailabilityForm" method="post" action="${pageContext.request.contextPath}/doctor/dashboard/availability/update" cssClass="edit-profile-form">
                    <div id="timeslots-container" class="timeslots-container">
                        <div id="no-slots-message" class="no-slots-message" style="display: none;">
                            <p><spring:message code="dashboard.availability.noTimeSlots" /></p>
                        </div>

                        <div id="time-slot-inputs">
                            <c:forEach items="${doctor.availabilitySlots}" var="slot" varStatus="status">
                                <div class="time-slot-row" id="slot-row-${status.index}">
                                    <div class="day-select">
                                        <label class="time-label"><spring:message code="register.dayOfWeek" /></label>
                                        <select name="availabilitySlots[${status.index}].dayOfWeek" class="form-control" data-index="${status.index}" onchange="checkOverlap(this)">
                                            <option value="0" <c:if test="${slot.dayOfWeek == 0}">selected</c:if>><spring:message code="register.monday" /></option>
                                            <option value="1" <c:if test="${slot.dayOfWeek == 1}">selected</c:if>><spring:message code="register.tuesday" /></option>
                                            <option value="2" <c:if test="${slot.dayOfWeek == 2}">selected</c:if>><spring:message code="register.wednesday" /></option>
                                            <option value="3" <c:if test="${slot.dayOfWeek == 3}">selected</c:if>><spring:message code="register.thursday" /></option>
                                            <option value="4" <c:if test="${slot.dayOfWeek == 4}">selected</c:if>><spring:message code="register.friday" /></option>
                                            <option value="5" <c:if test="${slot.dayOfWeek == 5}">selected</c:if>><spring:message code="register.saturday" /></option>
                                            <option value="6" <c:if test="${slot.dayOfWeek == 6}">selected</c:if>><spring:message code="register.sunday" /></option>
                                        </select>
                                    </div>
                                    <div class="time-select">
                                        <label class="time-label"><spring:message code="register.startTime" /></label>
                                        <select name="availabilitySlots[${status.index}].startTime" class="form-control" data-index="${status.index}" onchange="checkOverlap(this)">
                                            <c:forEach var="hour" begin="8" end="20">
                                                <c:set var="formattedHour" value="${hour < 10 ? '0' : ''}${hour}:00" />
                                                <option value="${formattedHour}" <c:if test="${slot.startTime.hour == hour}">selected</c:if>>
                                                    <c:choose>
                                                        <c:when test="${hour > 12}">${hour-12}:00 PM</c:when>
                                                        <c:when test="${hour == 12}">12:00 PM</c:when>
                                                        <c:otherwise>${hour}:00 AM</c:otherwise>
                                                    </c:choose>
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="time-select">
                                        <label class="time-label"><spring:message code="register.endTime" /></label>
                                        <select name="availabilitySlots[${status.index}].endTime" class="form-control" data-index="${status.index}" onchange="checkOverlap(this)">
                                            <c:forEach var="hour" begin="8" end="20">
                                                <c:set var="formattedHour" value="${hour < 10 ? '0' : ''}${hour}:00" />
                                                <option value="${formattedHour}" <c:if test="${slot.endTime.hour == hour}">selected</c:if>>
                                                    <c:choose>
                                                        <c:when test="${hour > 12}">${hour-12}:00 PM</c:when>
                                                        <c:when test="${hour == 12}">12:00 PM</c:when>
                                                        <c:otherwise>${hour}:00 AM</c:otherwise>
                                                    </c:choose>
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <button type="button" class="btn-remove" data-index="${status.index}" onclick="removeTimeSlotRow(${status.index})">×</button>
                                </div>
                            </c:forEach>
                        </div>

                        <div id="time-slot-error" class="error-message" style="display: none; margin-bottom: 10px;"></div>

                        <button type="button" class="btn-add-slot" id="add-slot-btn" onclick="addTimeSlotRow()">
                            + <spring:message code="dashboard.availability.addTimeSlot" />
                        </button>
                    </div>
                        <div>
                            <form:errors path="availabilitySlots" cssClass="error-message" />
                        </div>
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary" onclick="this.disabled=true; this.form.submit();">
                            <spring:message code="dashboard.availability.saveChanges" />
                        </button>
                    </div>
                </form:form>
            </div>
        </div>
    </div>

    <script>
        let slotCounter = ${doctor.availabilitySlots.size()};
        let timeSlots = [];

        document.addEventListener('DOMContentLoaded', function() {
            initializeTimeSlots();

            // Initial validation of slots
            validateAllSlots();

            // Add form submission handler to prevent submission with errors
            const availabilityForm = document.getElementById('updateAvailabilityForm');
            if (availabilityForm) {
                availabilityForm.addEventListener('submit', function(e) {
                    if (!validateAllSlots()) {
                        e.preventDefault();
                        return false;
                    }
                    return true;
                });
            }
        });

        function initializeTimeSlots() {
            // Initialize the timeSlots array with existing slots
            <c:forEach items="${doctor.availabilitySlots}" var="slot" varStatus="status">
            timeSlots.push({
                index: ${status.index},
                day: ${slot.dayOfWeek},
                startTime: '${slot.startTime}',
                endTime: '${slot.endTime}'
            });
            </c:forEach>

            // Update no slots message
            updateNoSlotsMessage();
        }

        // Generate hours for select options (8 AM to 8 PM)
        function getHourOptions() {
            const hours = [];
            for (let i = 8; i <= 20; i++) {
                const hour = i % 12 || 12;
                const ampm = i < 12 ? 'AM' : 'PM';
                const value = (i < 10 ? '0' + i : i) + ':00';
                const display = hour + ':00 ' + ampm;
                hours.push({ value: value, display: display });
            }
            return hours;
        }

        function addTimeSlotRow() {
            const container = document.getElementById('time-slot-inputs');

            // Create a new row
            const row = document.createElement('div');
            row.className = 'time-slot-row';
            row.id = 'slot-row-' + slotCounter;

            // Create day select container with label
            const dayContainer = document.createElement('div');
            dayContainer.className = 'day-select';

            const dayLabel = document.createElement('label');
            dayLabel.className = 'time-label';
            dayLabel.textContent = '<spring:message code="register.dayOfWeek" javaScriptEscape="true" />';
            dayContainer.appendChild(dayLabel);

            // Day select
            const daySelect = document.createElement('select');
            daySelect.name = 'availabilitySlots[' + slotCounter + '].dayOfWeek';
            daySelect.className = 'form-control';
            daySelect.setAttribute('data-index', slotCounter);
            daySelect.onchange = function() { checkOverlap(this); };

            const days = [
                { value: '0', text: '<spring:message code="register.monday" javaScriptEscape="true" />' },
                { value: '1', text: '<spring:message code="register.tuesday" javaScriptEscape="true" />' },
                { value: '2', text: '<spring:message code="register.wednesday" javaScriptEscape="true" />' },
                { value: '3', text: '<spring:message code="register.thursday" javaScriptEscape="true" />' },
                { value: '4', text: '<spring:message code="register.friday" javaScriptEscape="true" />' },
                { value: '5', text: '<spring:message code="register.saturday" javaScriptEscape="true" />' },
                { value: '6', text: '<spring:message code="register.sunday" javaScriptEscape="true" />' }
            ];

            days.forEach(function(day) {
                const option = document.createElement('option');
                option.value = day.value;
                option.textContent = day.text;
                daySelect.appendChild(option);
            });

            dayContainer.appendChild(daySelect);

            // Start time container with label
            const startContainer = document.createElement('div');
            startContainer.className = 'time-select';

            const startLabel = document.createElement('label');
            startLabel.className = 'time-label';
            startLabel.textContent = '<spring:message code="register.startTime" javaScriptEscape="true" />';
            startContainer.appendChild(startLabel);

            // Start time select
            const startSelect = document.createElement('select');
            startSelect.name = 'availabilitySlots[' + slotCounter + '].startTime';
            startSelect.className = 'form-control';
            startSelect.setAttribute('data-index', slotCounter);
            startSelect.onchange = function() { checkOverlap(this); };

            // End time container with label
            const endContainer = document.createElement('div');
            endContainer.className = 'time-select';

            const endLabel = document.createElement('label');
            endLabel.className = 'time-label';
            endLabel.textContent = '<spring:message code="register.endTime" javaScriptEscape="true" />';
            endContainer.appendChild(endLabel);

            // End time select
            const endSelect = document.createElement('select');
            endSelect.name = 'availabilitySlots[' + slotCounter + '].endTime';
            endSelect.className = 'form-control';
            endSelect.setAttribute('data-index', slotCounter);
            endSelect.onchange = function() { checkOverlap(this); };

            // Add hour options
            const hours = getHourOptions();

            hours.forEach(function(hour) {
                const startOption = document.createElement('option');
                startOption.value = hour.value;
                startOption.textContent = hour.display;
                startSelect.appendChild(startOption);

                const endOption = document.createElement('option');
                endOption.value = hour.value;
                endOption.textContent = hour.display;
                endSelect.appendChild(endOption);
            });

            // Set default values (9 AM for start, 5 PM for end)
            startSelect.value = '09:00';
            endSelect.value = '17:00';

            startContainer.appendChild(startSelect);
            endContainer.appendChild(endSelect);

            // Remove button
            const removeBtn = document.createElement('button');
            removeBtn.type = 'button';
            removeBtn.className = 'btn-remove';
            removeBtn.textContent = '×';
            removeBtn.setAttribute('data-index', slotCounter);
            removeBtn.onclick = function() {
                const index = parseInt(this.getAttribute('data-index'));
                removeTimeSlotRow(index);
            };

            // Add elements to row
            row.appendChild(dayContainer);
            row.appendChild(startContainer);
            row.appendChild(endContainer);
            row.appendChild(removeBtn);

            // Add row to container
            container.appendChild(row);

            // Add to slots array
            timeSlots.push({
                index: slotCounter,
                day: 0,
                startTime: '09:00',
                endTime: '17:00'
            });

            // Check for overlaps
            checkOverlap(daySelect);

            // Update no slots message
            updateNoSlotsMessage();

            // Increment counter
            slotCounter++;
        }

        // Check for overlapping time slots
        function checkOverlap(changedElement) {
            // Clear previous errors
            clearSlotErrors();

            const index = parseInt(changedElement.getAttribute('data-index'));
            const row = document.getElementById('slot-row-' + index);

            // Get values from the row
            const daySelect = row.querySelector('select[name$=".dayOfWeek"]');
            const startSelect = row.querySelector('select[name$=".startTime"]');
            const endSelect = row.querySelector('select[name$=".endTime"]');

            const day = parseInt(daySelect.value);
            const startTime = startSelect.value;
            const endTime = endSelect.value;

            // Update the slot in our array
            const slotIndex = timeSlots.findIndex(slot => slot.index === index);
            if (slotIndex !== -1) {
                timeSlots[slotIndex].day = day;
                timeSlots[slotIndex].startTime = startTime;
                timeSlots[slotIndex].endTime = endTime;
            }

            // Check if end time is after start time
            if (startTime >= endTime) {
                row.classList.add('slot-error');
                showSlotError('<spring:message code="register.timeSlotInvalidTime" javaScriptEscape="true" />');
                updateSaveButtonState();
                return false;
            }

            // Check for overlaps with other slots
            const overlaps = timeSlots.filter((slot, i) => {
                if (slot.index === index) return false; // Skip the current slot

                // Only check slots on the same day
                if (slot.day !== day) return false;

                // Check for time overlap
                return !(endTime <= slot.startTime || startTime >= slot.endTime);
            });

            if (overlaps.length > 0) {
                // Mark overlapping slots with error
                row.classList.add('slot-error');
                overlaps.forEach(overlap => {
                    const overlapRow = document.getElementById('slot-row-' + overlap.index);
                    if (overlapRow) {
                        overlapRow.classList.add('slot-error');
                    }
                });

                showSlotError('<spring:message code="register.timeSlotOverlap" javaScriptEscape="true" />');
                updateSaveButtonState();
                return false;
            }

            // Validate all slots to ensure we catch any other overlaps
            validateAllSlots();
            return true;
        }

        // Show error message for time slots
        function showSlotError(message) {
            const errorElement = document.getElementById('time-slot-error');
            errorElement.textContent = message;
            errorElement.style.display = 'block';
        }

        // Clear all slot errors
        function clearSlotErrors() {
            const errorElement = document.getElementById('time-slot-error');
            errorElement.style.display = 'none';

            const errorRows = document.querySelectorAll('.slot-error');
            errorRows.forEach(row => {
                row.classList.remove('slot-error');
            });
        }

        // Remove a time slot row
        function removeTimeSlotRow(index) {
            const row = document.getElementById('slot-row-' + index);

            if (row) {
                // Create a hidden input to mark this slot for deletion
                const hiddenInput = document.createElement('input');
                hiddenInput.type = 'hidden';
                hiddenInput.name = 'deletedSlots';
                hiddenInput.value = index;
                document.getElementById('updateAvailabilityForm').appendChild(hiddenInput);

                // Remove from DOM
                row.parentNode.removeChild(row);

                // Remove from array
                const slotIndex = timeSlots.findIndex(slot => slot.index === index);
                if (slotIndex !== -1) {
                    timeSlots.splice(slotIndex, 1);
                }

                // Validate remaining slots
                validateAllSlots();

                // Update no slots message
                updateNoSlotsMessage();
            }
        }

        // Update the no slots message visibility
        function updateNoSlotsMessage() {
            const container = document.getElementById('time-slot-inputs');
            const noSlotsMessage = document.getElementById('no-slots-message');

            if (container.children.length === 0) {
                noSlotsMessage.style.display = 'block';
            } else {
                noSlotsMessage.style.display = 'none';
            }
        }

        // Add a function to validate all slots
        function validateAllSlots() {
            // Clear all errors first
            clearSlotErrors();

            let hasErrors = false;

            // Check each slot against all others
            for (let i = 0; i < timeSlots.length; i++) {
                const slot = timeSlots[i];
                const row = document.getElementById('slot-row-' + slot.index);

                if (!row) continue;

                // Check if end time is after start time
                if (slot.startTime >= slot.endTime) {
                    row.classList.add('slot-error');
                    showSlotError('<spring:message code="register.timeSlotInvalidTime" javaScriptEscape="true" />');
                    hasErrors = true;
                    continue;
                }

                // Check for overlaps with other slots
                for (let j = i + 1; j < timeSlots.length; j++) {
                    const otherSlot = timeSlots[j];

                    // Only check slots on the same day
                    if (slot.day !== otherSlot.day) continue;

                    // Check for time overlap
                    if (!(slot.endTime <= otherSlot.startTime || slot.startTime >= otherSlot.endTime)) {
                        const row1 = document.getElementById('slot-row-' + slot.index);
                        const row2 = document.getElementById('slot-row-' + otherSlot.index);

                        if (row1) row1.classList.add('slot-error');
                        if (row2) row2.classList.add('slot-error');

                        showSlotError('<spring:message code="register.timeSlotOverlap" javaScriptEscape="true" />');
                        hasErrors = true;
                    }
                }
            }

            updateSaveButtonState();
            return !hasErrors;
        }

        function updateSaveButtonState() {
            const saveButton = document.querySelector('#updateAvailabilityForm button[type="submit"]');
            const hasErrors = document.querySelectorAll('.slot-error').length > 0;

            if (saveButton) {
                saveButton.disabled = hasErrors;
                if (hasErrors) {
                    saveButton.classList.add('btn-disabled');
                } else {
                    saveButton.classList.remove('btn-disabled');
                }
            }
        }
    </script>
</dashboard:layout>
