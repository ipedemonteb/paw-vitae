<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<style>
    /* Time slots styles */
    .timeslots-container {
        border: 1px solid #ddd;
        border-radius: 8px;
        padding: 20px;
        margin-bottom: 15px;
        background-color: #f9f9f9;
    }

    .no-slots-message {
        text-align: center;
        color: #666;
        padding: 20px 0;
    }

    .time-slot-row {
        display: flex;
        align-items: center;
        margin-bottom: 15px;
        padding: 15px;
        background-color: white;
        border-radius: 8px;
        border: 1px solid #eee;
        box-shadow: 0 1px 3px rgba(0,0,0,0.05);
    }

    .time-slot-row select {
        height: 40px;
        padding: 0 10px;
        border: 1px solid #ddd;
        border-radius: 4px;
        background-color: white;
        font-size: 14px;
    }

    .day-select {
        flex: 2;
        margin-right: 10px;
    }

    .time-select {
        flex: 1;
        margin-right: 10px;
    }

    .time-label {
        display: block;
        font-size: 12px;
        color: #666;
        margin-bottom: 5px;
    }

    .btn-remove {
        background: none;
        border: none;
        color: #e25c5c;
        cursor: pointer;
        font-size: 20px;
        width: 30px;
        height: 30px;
        display: flex;
        align-items: center;
        justify-content: center;
        border-radius: 50%;
        transition: all 0.2s;
    }

    .btn-remove:hover {
        background-color: #ffeeee;
    }

    .btn-add-slot {
        background-color: #f5f5f5;
        border: 1px dashed #ccc;
        color: #333;
        width: 100%;
        padding: 12px;
        text-align: center;
        border-radius: 6px;
        cursor: pointer;
        font-weight: 500;
        transition: all 0.2s;
    }

    .btn-add-slot:hover {
        background-color: #e9e9e9;
    }

    .error-message {
        color: #e25c5c;
        font-size: 12px;
        margin-top: 5px;
    }

    .slot-error {
        border: 1px solid #e25c5c !important;
        background-color: #fff8f8 !important;
    }
</style>

<!-- Time Slots Component -->
<div class="form-group">
    <label class="form-label required-field"><spring:message code="register.availabilitySlots" /></label>
    <div id="timeslots-container" class="timeslots-container">
        <div id="no-slots-message" class="no-slots-message">
            <p><spring:message code="register.noTimeSlots" /></p>
        </div>

        <div id="time-slot-inputs">
            <!-- Time slot inputs will be added here -->
        </div>

        <div id="time-slot-error" class="error-message" style="display: none; margin-bottom: 10px;"></div>

        <button type="button" class="btn-add-slot" id="add-slot-btn" onclick="addTimeSlotRow()">
            + <spring:message code="register.addTimeSlot" />
        </button>
    </div>
    <small class="form-text text-muted">
        <spring:message code="register.timeSlotHelp" />
    </small>
    <form:errors path="availabilitySlots" cssClass="error-message" />
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Check if we need to show the no slots message
        updateNoSlotsMessage();
    });

    // Time slot counter and slots array
    let slotCounter = 0;
    let timeSlots = [];

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

    // Add a new time slot row
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
            return false;
        }

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
            // Remove from DOM
            row.parentNode.removeChild(row);

            // Remove from array
            const slotIndex = timeSlots.findIndex(slot => slot.index === index);
            if (slotIndex !== -1) {
                timeSlots.splice(slotIndex, 1);
            }

            // Check remaining slots for overlaps
            if (timeSlots.length > 0) {
                checkOverlap(document.querySelector('select[data-index="' + timeSlots[0].index + '"]'));
            } else {
                clearSlotErrors();
            }

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

    // Validate time slots (can be called from parent form's validation)
    function validateTimeSlots() {
        // Check if we have at least one time slot
        if (timeSlots.length === 0) {
            showSlotError('<spring:message code="register.timeSlotRequired" javaScriptEscape="true" />');
            return false;
        }

        // Check for any overlapping slots
        for (let i = 0; i < timeSlots.length; i++) {
            const slot = timeSlots[i];
            const element = document.querySelector('select[data-index="' + slot.index + '"]');
            if (!checkOverlap(element)) {
                return false;
            }
        }

        return true;
    }

    // Get all time slots data (can be used to get data programmatically)
    function getTimeSlotsData() {
        return timeSlots.map(slot => {
            return {
                dayOfWeek: slot.day,
                startTime: slot.startTime,
                endTime: slot.endTime
            };
        });
    }
</script>
