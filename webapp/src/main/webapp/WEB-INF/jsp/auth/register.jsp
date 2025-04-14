<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="comp" tagdir="/WEB-INF/tags/components" %>

<link rel="stylesheet" href="<c:url value='/css/components/forms.css' />" />
<link rel="stylesheet" href="<c:url value='/css/components/register.css' />" />

<style>
    /* Simplified time slots styles */
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

<layout:page title="register.doctor.title">
    <div class="register-container">
        <div class="register-card">
            <div class="card-header">
                <h1 class="card-title-register"><spring:message code="register.doctor.title" /></h1>
                <p class="card-subtitle-register"><spring:message code="register.doctor.subtitle" /></p>
            </div>

            <div class="card-body">
                <form:form modelAttribute="registerForm" method="post" action="${pageContext.request.contextPath}/register" enctype="multipart/form-data" cssClass="register-form" onsubmit="return validateForm()">
                    <div class="form-row">
                        <div class="form-group">
                            <label for="name" class="form-label required-field"><spring:message code="register.firstName" /></label>
                            <form:input path="name" id="name" cssClass="form-control" />
                            <form:errors path="name" cssClass="error-message" />
                        </div>

                        <div class="form-group">
                            <label for="lastName" class="form-label required-field"><spring:message code="register.lastName" /></label>
                            <form:input path="lastName" id="lastName" cssClass="form-control" />
                            <form:errors path="lastName" cssClass="error-message" />
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="email" class="form-label required-field"><spring:message code="register.email" /></label>
                            <form:input path="email" id="email" cssClass="form-control" type="email" />
                            <form:errors path="email" cssClass="error-message" />
                        </div>

                        <div class="form-group">
                            <label for="phone" class="form-label required-field"><spring:message code="register.phone" /></label>
                            <form:input path="phone" id="phone" cssClass="form-control" />
                            <form:errors path="phone" cssClass="error-message" />
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="password" class="form-label required-field"><spring:message code="register.password" /></label>
                            <form:password path="password" id="password" cssClass="form-control" />
                            <form:errors path="password" cssClass="error-message" />
                        </div>

                        <div class="form-group">
                            <label for="repeatPassword" class="form-label required-field"><spring:message code="register.confirmPassword" /></label>
                            <form:password path="repeatPassword" id="repeatPassword" cssClass="form-control" />
                            <form:errors path="repeatPassword" cssClass="error-message" />
                        </div>
                    </div>

                    <!-- Time Slots Section -->
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

                    <div class="form-group">
                        <label for="specialties" class="form-label required-field"><spring:message code="register.specialties" /></label>
                        <div class="multi-select-container" id="specialties-container">
                            <div class="custom-multi-select" id="specialties-options">
                                <c:forEach items="${specialtyList}" var="specialty">
                                    <div class="custom-multi-select-option" data-value="${specialty.id}" onclick="toggleOption(this, 'specialties')">
                                        <div class="option-checkbox"></div>
                                        <div class="option-text">
                                            <c:choose>
                                                <c:when test="${not empty specialty.key}">
                                                    <spring:message code="${specialty.key}"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:out value="${specialty.name}"/>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                            <div class="selected-options" id="specialties-selected"></div>
                            <form:select path="specialties" id="specialties" cssClass="form-control" multiple="true">
                                <c:forEach items="${specialtyList}" var="specialty">
                                    <form:option value="${specialty.id}">
                                        <c:choose>
                                            <c:when test="${not empty specialty.key}">
                                                <spring:message code="${specialty.key}"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="${specialty.name}"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </form:option>
                                </c:forEach>
                            </form:select>
                        </div>
                        <small class="form-text text-muted">
                            <spring:message code="register.selectSpecialties" />
                        </small>
                        <form:errors path="specialties" cssClass="error-message" />
                    </div>

                    <div class="form-group">
                        <label for="coverages" class="form-label required-field"><spring:message code="register.coverage" /></label>
                        <div class="multi-select-container" id="coverages-container">
                            <div class="custom-multi-select" id="coverages-options">
                                <c:forEach items="${coverageList}" var="coverage">
                                    <div class="custom-multi-select-option" data-value="${coverage.id}" onclick="toggleOption(this, 'coverages')">
                                        <div class="option-checkbox"></div>
                                        <div class="option-text">
                                            <c:out value="${coverage.name}"/>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                            <div class="selected-options" id="coverages-selected"></div>
                            <form:select path="coverages" id="coverages" cssClass="form-control" multiple="true">
                                <c:forEach items="${coverageList}" var="coverage">
                                    <form:option value="${coverage.id}"><c:out value="${coverage.name}"/></form:option>
                                </c:forEach>
                            </form:select>
                        </div>
                        <small class="form-text text-muted">
                            <spring:message code="register.selectCoverage" />
                        </small>
                        <form:errors path="coverages" cssClass="error-message" />
                    </div>

                    <div class="form-group">
                        <label class="form-label"><spring:message code="register.uploadImage" /></label>
                        <div class="file-upload">
                            <label class="file-upload-label">
                                <span class="file-upload-text"><spring:message code="register.uploadImage" /></span>
                                <form:input type="file" path="image" id="image-upload" accept="image/*" cssClass="file-input" />
                            </label>
                        </div>
                        <div id="file-name" class="file-name"></div>
                        <form:errors path="image" cssClass="error-message" />
                    </div>

                    <div class="form-group terms-checkbox">
                        <input type="checkbox" id="terms" class="custom-checkbox" required />
                        <label for="terms" class="checkbox-label required-field">
                            <spring:message code="register.agreeTerms" />
                            <a class="terms-link"><spring:message code="register.termsLink" /></a>
                        </label>
                    </div>

                    <div class="form-group">
                        <button type="submit" class="btn-submit">
                            <spring:message code="register.button" />
                        </button>
                    </div>
                </form:form>
            </div>
        </div>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Simple file upload preview
            const fileInput = document.getElementById('image-upload');
            const fileName = document.getElementById('file-name');

            if (fileInput) {
                fileInput.addEventListener('change', function() {
                    if (this.files && this.files[0]) {
                        const file = this.files[0];
                        fileName.textContent = file.name;
                    }
                });
            }

            // Initialize selected options display
            updateSelectedDisplay('specialties');
            updateSelectedDisplay('coverages');

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

        // Validate the form before submission
        function validateForm() {
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

            // Create hidden inputs for the time slots
            const form = document.querySelector('.register-form');
            const slotsContainer = document.createElement('div');
            slotsContainer.style.display = 'none';

            timeSlots.forEach((slot, i) => {
                // Create hidden input for day of week
                const dayInput = document.createElement('input');
                dayInput.type = 'hidden';
                dayInput.name = 'availabilitySlots[' + i + '].dayOfWeek';
                dayInput.value = slot.day;
                slotsContainer.appendChild(dayInput);

                // Create hidden input for start time
                const startInput = document.createElement('input');
                startInput.type = 'hidden';
                startInput.name = 'availabilitySlots[' + i + '].startTime';
                startInput.value = slot.startTime;
                slotsContainer.appendChild(startInput);

                // Create hidden input for end time
                const endInput = document.createElement('input');
                endInput.type = 'hidden';
                endInput.name = 'availabilitySlots[' + i + '].endTime';
                endInput.value = slot.endTime;
                slotsContainer.appendChild(endInput);
            });

            form.appendChild(slotsContainer);

            return true;
        }

        // Global function to toggle option selection
        function toggleOption(optionElement, selectId) {
            // Get the value of the clicked option
            const value = optionElement.getAttribute('data-value');

            // Toggle selected class
            optionElement.classList.toggle('selected');

            // Get the select element
            const select = document.getElementById(selectId);

            if (select) {
                // Find the corresponding option in the select element
                for (let i = 0; i < select.options.length; i++) {
                    if (select.options[i].value === value) {
                        // Toggle selection
                        select.options[i].selected = optionElement.classList.contains('selected');
                        break;
                    }
                }

                // Update the selected options display
                updateSelectedDisplay(selectId);
            }
        }

        function updateSelectedDisplay(selectId) {
            const select = document.getElementById(selectId);
            const selectedContainer = document.getElementById(selectId + '-selected');
            const options = document.querySelectorAll('#' + selectId + '-options .custom-multi-select-option');

            if (!select || !selectedContainer) return;

            // Clear the selected container
            selectedContainer.innerHTML = '';

            // Get all selected options from the select element
            const selectedOptions = [];
            for (let i = 0; i < select.options.length; i++) {
                if (select.options[i].selected) {
                    selectedOptions.push(select.options[i]);
                }
            }

            // Update the custom options UI to match the select element
            options.forEach(function(option) {
                const value = option.getAttribute('data-value');
                let isSelected = false;

                for (let i = 0; i < selectedOptions.length; i++) {
                    if (selectedOptions[i].value === value) {
                        isSelected = true;
                        break;
                    }
                }

                if (isSelected) {
                    option.classList.add('selected');
                } else {
                    option.classList.remove('selected');
                }
            });

            // Create badges for selected options
            selectedOptions.forEach(function(option) {
                const badge = document.createElement('div');
                badge.className = 'selected-option-badge';
                badge.innerHTML = option.text +
                    '<span class="remove-option" data-value="' + option.value + '" onclick="removeOption(this, \'' + selectId + '\')">×</span>';

                selectedContainer.appendChild(badge);
            });
        }

        function removeOption(removeButton, selectId) {
            const value = removeButton.getAttribute('data-value');

            // Get the select element
            const select = document.getElementById(selectId);

            if (select) {
                // Find and deselect the option in the select element
                for (let i = 0; i < select.options.length; i++) {
                    if (select.options[i].value === value) {
                        select.options[i].selected = false;
                        break;
                    }
                }

                // Find and deselect the option in the custom UI
                const customOption = document.querySelector('#' + selectId + '-options .custom-multi-select-option[data-value="' + value + '"]');
                if (customOption) {
                    customOption.classList.remove('selected');
                }

                // Update the selected options display
                updateSelectedDisplay(selectId);
            }
        }
    </script>
</layout:page>