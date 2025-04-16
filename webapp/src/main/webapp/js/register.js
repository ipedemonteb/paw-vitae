// Global variables
let slotCounter = 0;
let timeSlots = [];

document.addEventListener('DOMContentLoaded', function() {
    // Initialize file upload preview
    initFileUpload();

    // Initialize password strength meter
    initPasswordStrength();

    // Initialize multi-select components
    initMultiSelect();

    // Initialize time slots
    initTimeSlots();

    // Check if there are form errors and show the appropriate section
    checkFormErrors();

    // Add event listeners
    document.getElementById('add-slot-btn').addEventListener('click', addTimeSlotRow);
    document.getElementById('password').addEventListener('keyup', function () {
        checkPasswordStrength();
        updateNextButtonState(1);
    });
    document.getElementById('repeatPassword').addEventListener('keyup', function () {
        checkPasswordMatch();
        updateNextButtonState(1);
    });

    restorePasswordValues();

    addInputValidationListeners();

    updateAllButtonStates();
});

// Initialize file upload preview
function initFileUpload() {
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
}

function restorePasswordValues() {
    const passwordValue = document.getElementById('passwordValue');
    const repeatPasswordValue = document.getElementById('repeatPasswordValue');
    const passwordField = document.getElementById('password');
    const repeatPasswordField = document.getElementById('repeatPassword');

    if (passwordValue && passwordValue.value) {
        passwordField.value = passwordValue.value;
        checkPasswordStrength();
    }

    if (repeatPasswordValue && repeatPasswordValue.value) {
        repeatPasswordField.value = repeatPasswordValue.value;
        checkPasswordMatch();
    }
}

function addInputValidationListeners() {
    // Section 1 fields
    const section1 = document.querySelector('.form-section[data-section="1"]');
    const requiredFields1 = section1.querySelectorAll('input[required]');
    requiredFields1.forEach(field => {
        field.addEventListener('input', function() {
            updateNextButtonState(1);
        });
    });

    // Section 2 fields
    const section2 = document.querySelector('.form-section[data-section="2"]');
    const specialtiesContainer = document.getElementById('specialties-container');
    const coveragesContainer = document.getElementById('coverages-container');

    // Add custom event listeners for multi-select components
    const specialtiesOptions = document.querySelectorAll('#specialties-options .custom-multi-select-option');
    specialtiesOptions.forEach(option => {
        option.addEventListener('click', function() {
            setTimeout(() => updateNextButtonState(2), 100);
        });
    });

    const coveragesOptions = document.querySelectorAll('#coverages-options .custom-multi-select-option');
    coveragesOptions.forEach(option => {
        option.addEventListener('click', function() {
            setTimeout(() => updateNextButtonState(2), 100);
        });
    });

    // Section 3 fields
    const section3 = document.querySelector('.form-section[data-section="3"]');
    const termsCheckbox = document.getElementById('terms');
    termsCheckbox.addEventListener('change', function() {
        updateSubmitButtonState();
    });
}

function updateSubmitButtonState() {
    const submitButton = document.querySelector('.btn-submit-doctor');
    if (!submitButton) return;

    const termsChecked = document.getElementById('terms').checked;
    const hasTimeSlots = timeSlots.length > 0;
    const isValid = termsChecked && hasTimeSlots;

    submitButton.disabled = !isValid;

    // Add/remove disabled class for styling
    if (isValid) {
        submitButton.classList.remove('disabled');
    } else {
        submitButton.classList.add('disabled');
    }
}

function updateAllButtonStates() {
    updateNextButtonState(1);
    updateNextButtonState(2);
    updateSubmitButtonState();
}


function updateNextButtonState(sectionNumber) {
    const nextButton = document.querySelector(`.form-section[data-section="${sectionNumber}"] .btn-next`);
    if (!nextButton) return;

    const isValid = validateSectionWithoutUI(sectionNumber);
    nextButton.disabled = !isValid;

    // Add/remove disabled class for styling
    if (isValid) {
        nextButton.classList.remove('disabled');
    } else {
        nextButton.classList.add('disabled');
    }
}

function validateSectionWithoutUI(sectionNumber) {
    let isValid = true;
    const section = document.querySelector(`.form-section[data-section="${sectionNumber}"]`);

    // Check required fields in the current section
    const requiredFields = section.querySelectorAll('input[required], select[required]');
    requiredFields.forEach(field => {
        if (!field.value) {
            isValid = false;
        }
    });

    // Section-specific validations
    if (sectionNumber === 1) {
        // Validate password match
        const password = document.getElementById('password');
        const repeatPassword = document.getElementById('repeatPassword');

        if (password.value && repeatPassword.value && password.value !== repeatPassword.value) {
            isValid = false;
        }

        // Validate email format
        const email = document.getElementById('email');
        if (email.value && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value)) {
            isValid = false;
        }

        // Validate phone format
        const phone = document.getElementById('phone');
        if (phone.value && !/\+?[0-9. ()-]{7,25}/.test(phone.value)) {
            isValid = false;
        }
    } else if (sectionNumber === 2) {
        // Validate specialties and coverages
        const specialties = document.getElementById('specialties');
        const coverages = document.getElementById('coverages');

        if (!specialties.options || specialties.selectedOptions.length === 0) {
            isValid = false;
        }

        if (!coverages.options || coverages.selectedOptions.length === 0) {
            isValid = false;
        }
    }

    return isValid;
}

// Initialize password strength meter
function initPasswordStrength() {
    const password = document.getElementById('password');
    if (password && password.value) {
        checkPasswordStrength();
    }
}

// Initialize multi-select components
function initMultiSelect() {
    updateSelectedDisplay('specialties');
    updateSelectedDisplay('coverages');
}

// Initialize time slots
function initTimeSlots() {
    // Check if we have existing slots from the server
    if (window.existingSlots && window.existingSlots.length > 0) {
        // Use the existing slots
        timeSlots = window.existingSlots;
        slotCounter = timeSlots.length;

        // Render the existing slots
        timeSlots.forEach(slot => {
            renderTimeSlotRow(slot);
        });
    } else {
        // Add at least one time slot if none exist
        addTimeSlotRow();
    }

    // Update no slots message
    updateNoSlotsMessage();
}

// Render a time slot row with existing data
function renderTimeSlotRow(slot) {
    const container = document.getElementById('time-slot-inputs');
    const existingRow = document.getElementById('slot-row-' + slot.index);

    if (existingRow) {
        // Clear the existing row
        existingRow.innerHTML = '';
    } else {
        // Create a new row
        const row = document.createElement('div');
        row.className = 'time-slot-row';
        row.id = 'slot-row-' + slot.index;
        container.appendChild(row);
    }

    const row = document.getElementById('slot-row-' + slot.index);

    // Create day select container with label
    const dayContainer = document.createElement('div');
    dayContainer.className = 'day-select';

    const dayLabel = document.createElement('label');
    dayLabel.className = 'time-label';
    dayLabel.textContent = window.messages.dayOfWeek;
    dayContainer.appendChild(dayLabel);

    // Day select
    const daySelect = document.createElement('select');
    daySelect.name = 'availabilitySlots[' + slot.index + '].dayOfWeek';
    daySelect.className = 'form-control';
    daySelect.setAttribute('data-index', slot.index);
    daySelect.onchange = function() { checkOverlap(this); };

    const days = [
        { value: '0', text: window.messages.monday },
        { value: '1', text: window.messages.tuesday },
        { value: '2', text: window.messages.wednesday },
        { value: '3', text: window.messages.thursday },
        { value: '4', text: window.messages.friday },
        { value: '5', text: window.messages.saturday },
        { value: '6', text: window.messages.sunday }
    ];

    days.forEach(function(day) {
        const option = document.createElement('option');
        option.value = day.value;
        option.textContent = day.text;
        daySelect.appendChild(option);
    });

    // Set the selected day
    daySelect.value = slot.day.toString();

    dayContainer.appendChild(daySelect);

    // Start time container with label
    const startContainer = document.createElement('div');
    startContainer.className = 'time-select';

    const startLabel = document.createElement('label');
    startLabel.className = 'time-label';
    startLabel.textContent = window.messages.startTime;
    startContainer.appendChild(startLabel);

    // Start time select
    const startSelect = document.createElement('select');
    startSelect.name = 'availabilitySlots[' + slot.index + '].startTime';
    startSelect.className = 'form-control';
    startSelect.setAttribute('data-index', slot.index);
    startSelect.onchange = function() { checkOverlap(this); };

    // End time container with label
    const endContainer = document.createElement('div');
    endContainer.className = 'time-select';

    const endLabel = document.createElement('label');
    endLabel.className = 'time-label';
    endLabel.textContent = window.messages.endTime;
    endContainer.appendChild(endLabel);

    // End time select
    const endSelect = document.createElement('select');
    endSelect.name = 'availabilitySlots[' + slot.index + '].endTime';
    endSelect.className = 'form-control';
    endSelect.setAttribute('data-index', slot.index);
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

    // Set the selected times
    startSelect.value = slot.startTime;
    endSelect.value = slot.endTime;

    startContainer.appendChild(startSelect);
    endContainer.appendChild(endSelect);

    // Remove button
    const removeBtn = document.createElement('button');
    removeBtn.type = 'button';
    removeBtn.className = 'btn-remove';
    removeBtn.textContent = '×';
    removeBtn.setAttribute('data-index', slot.index);
    removeBtn.setAttribute('aria-label', 'Remove time slot');
    removeBtn.onclick = function() {
        const index = parseInt(this.getAttribute('data-index'));
        removeTimeSlotRow(index);
    };

    // Add elements to row
    row.appendChild(dayContainer);
    row.appendChild(startContainer);
    row.appendChild(endContainer);
    row.appendChild(removeBtn);
}

// Check if there are form errors and show the appropriate section
function checkFormErrors() {
    if (window.hasErrors && window.errorSection) {
        console.log("IN HERE");
        showSection(window.errorSection);
    }
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

// Add a new time slot row
function addTimeSlotRow() {
    // Create a new slot object
    const newSlot = {
        index: slotCounter,
        day: 0,
        startTime: '09:00',
        endTime: '17:00'
    };

    // Add to slots array
    timeSlots.push(newSlot);

    // Render the new slot
    renderTimeSlotRow(newSlot);

    // Check for overlaps
    const daySelect = document.querySelector('select[data-index="' + slotCounter + '"]');
    checkOverlap(daySelect);

    // Update no slots message
    updateNoSlotsMessage();

    // Increment counter
    slotCounter++;
    updateTimeSlotValidation();
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
        showSlotError(window.messages.timeSlotInvalidTime);
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

        showSlotError(window.messages.timeSlotOverlap);
        return false;
    }

    setTimeout(updateTimeSlotValidation, 100);

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
        // Add fade out animation
        row.style.opacity = '0';
        row.style.transform = 'translateY(-10px)';

        setTimeout(() => {
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
            updateTimeSlotValidation();
        }, 300);
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

// Form navigation functions
function nextSection(currentSection) {
    // Validate current section
    if (!validateSection(currentSection)) {
        return;
    }

    // Hide current section
    document.querySelector(`.form-section[data-section="${currentSection}"]`).classList.remove('active');

    // Show next section
    const nextSection = currentSection + 1;
    document.querySelector(`.form-section[data-section="${nextSection}"]`).classList.add('active');

    // Update progress indicator
    updateProgressIndicator(nextSection);

    // Update button states for the new section
    if (nextSection === 2) {
        updateNextButtonState(2);
    } else if (nextSection === 3) {
        updateSubmitButtonState();
    }
}

function prevSection(currentSection) {
    // Hide current section
    document.querySelector(`.form-section[data-section="${currentSection}"]`).classList.remove('active');

    // Show previous section
    const prevSection = currentSection - 1;
    document.querySelector(`.form-section[data-section="${prevSection}"]`).classList.add('active');

    // Update progress indicator
    updateProgressIndicator(prevSection);
}

function showSection(sectionNumber) {
    // Hide all sections
    document.querySelectorAll('.form-section').forEach(section => {
        section.classList.remove('active');
    });

    // Show requested section
    document.querySelector(`.form-section[data-section="${sectionNumber}"]`).classList.add('active');

    // Update progress indicator
    updateProgressIndicator(sectionNumber);
}

function updateProgressIndicator(currentStep) {
    // Reset all steps
    document.querySelectorAll('.progress-step').forEach(step => {
        step.classList.remove('active', 'completed');
    });

    // Mark current step as active
    document.querySelector(`.progress-step[data-step="${currentStep}"]`).classList.add('active');

    // Mark previous steps as completed
    for (let i = 1; i < currentStep; i++) {
        document.querySelector(`.progress-step[data-step="${i}"]`).classList.add('completed');
    }
}

function validateSection(sectionNumber) {
    let isValid = true;
    const section = document.querySelector(`.form-section[data-section="${sectionNumber}"]`);

    // Check required fields in the current section
    const requiredFields = section.querySelectorAll('input[required], select[required]');
    requiredFields.forEach(field => {
        if (!field.value) {
            field.classList.add('error');
            isValid = false;
        } else {
            field.classList.remove('error');
        }
    });

    // Section-specific validations
    if (sectionNumber === 1) {
        // Validate password match
        const password = document.getElementById('password');
        const repeatPassword = document.getElementById('repeatPassword');

        if (password.value && repeatPassword.value && password.value !== repeatPassword.value) {
            document.getElementById('password-match-message').textContent = window.messages.passwordsDoNotMatch;
            document.getElementById('password-match-message').style.display = 'block';
            repeatPassword.classList.add('error');
            isValid = false;
        }

        // Validate email format
        const email = document.getElementById('email');
        if (email.value && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value)) {
            email.classList.add('error');
            isValid = false;
        }

        // Validate phone format
        const phone = document.getElementById('phone');
        if (phone.value && !/\+?[0-9. ()-]{7,25}/.test(phone.value)) {
            phone.classList.add('error');
            isValid = false;
        }
    } else if (sectionNumber === 2) {
        // Validate specialties and coverages
        const specialties = document.getElementById('specialties');
        const coverages = document.getElementById('coverages');

        if (!specialties.options || specialties.selectedOptions.length === 0) {
            document.getElementById('specialties-container').classList.add('error');
            isValid = false;
        }

        if (!coverages.options || coverages.selectedOptions.length === 0) {
            document.getElementById('coverages-container').classList.add('error');
            isValid = false;
        }
    }

    return isValid;
}

// Password strength checker
function checkPasswordStrength() {
    const password = document.getElementById('password').value;
    const strengthMeter = document.querySelector('.strength-meter-fill');
    const strengthText = document.querySelector('.strength-text');

    // Remove all classes
    document.querySelector('.password-strength').className = 'password-strength';

    if (!password) {
        strengthMeter.style.width = '0';
        strengthText.textContent = '';
        return;
    }

    // Calculate strength
    let strength = 0;

    // Length check
    if (password.length >= 8) strength += 1;
    if (password.length >= 12) strength += 1;

    // Character variety check
    if (/[A-Z]/.test(password)) strength += 1;
    if (/[a-z]/.test(password)) strength += 1;
    if (/[0-9]/.test(password)) strength += 1;
    if (/[^A-Za-z0-9]/.test(password)) strength += 1;

    // Set strength level
    let strengthClass = '';
    let strengthLabel = '';

    if (strength < 3) {
        strengthClass = 'strength-weak';
        strengthLabel = window.messages.passwordWeak;
    } else if (strength < 4) {
        strengthClass = 'strength-medium';
        strengthLabel = window.messages.passwordMedium;
    } else if (strength < 6) {
        strengthClass = 'strength-strong';
        strengthLabel = window.messages.passwordStrong;
    } else {
        strengthClass = 'strength-very-strong';
        strengthLabel = window.messages.passwordVeryStrong;
    }

    document.querySelector('.password-strength').classList.add(strengthClass);
    strengthText.textContent = strengthLabel;
}

// Check if passwords match
function checkPasswordMatch() {
    const password = document.getElementById('password').value;
    const repeatPassword = document.getElementById('repeatPassword').value;
    const matchMessage = document.getElementById('password-match-message');

    if (password && repeatPassword) {
        if (password !== repeatPassword) {
            matchMessage.textContent = window.messages.passwordsDoNotMatch;
            matchMessage.style.display = 'block';
            document.getElementById('repeatPassword').classList.add('error');
        } else {
            matchMessage.style.display = 'none';
            document.getElementById('repeatPassword').classList.remove('error');
        }
    }
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

        // Remove error class if options are selected
        if (select.selectedOptions.length > 0) {
            document.getElementById(selectId + '-container').classList.remove('error');
        }
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

function updateTimeSlotValidation() {
    const hasValidTimeSlots = timeSlots.length > 0 && timeSlots.every(slot => {
        const element = document.querySelector('select[data-index="' + slot.index + '"]');
        return checkOverlap(element);
    });

    updateSubmitButtonState();
}

// Form validation
function validateForm() {
    // Check if we have at least one time slot
    if (timeSlots.length === 0) {
        showSlotError(window.messages.timeSlotRequired);
        showSection(3); // Show the availability section
        return false;
    }

    // Check for any overlapping slots
    for (let i = 0; i < timeSlots.length; i++) {
        const slot = timeSlots[i];
        const element = document.querySelector('select[data-index="' + slot.index + '"]');
        if (!checkOverlap(element)) {
            showSection(3); // Show the availability section
            return false;
        }
    }

    // Validate all sections
    for (let i = 1; i <= 3; i++) {
        if (!validateSection(i)) {
            showSection(i);
            return false;
        }
    }

    // Store password values in hidden fields before submission
    const passwordField = document.getElementById('password');
    const repeatPasswordField = document.getElementById('repeatPassword');
    const passwordValueField = document.getElementById('passwordValue');
    const repeatPasswordValueField = document.getElementById('repeatPasswordValue');

    if (passwordField && passwordValueField) {
        passwordValueField.value = passwordField.value;
    }

    if (repeatPasswordField && repeatPasswordValueField) {
        repeatPasswordValueField.value = repeatPasswordField.value;
    }

    return true;
}