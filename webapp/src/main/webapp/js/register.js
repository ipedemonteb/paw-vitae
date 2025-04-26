// Global variables
const slotCounter = 0
const timeSlots = []
let isFormSubmitting = false
const validationTimeout = null

document.addEventListener("DOMContentLoaded", () => {
    // Initialize components
    initFileUpload()
    initPasswordStrength()
    initMultiSelect()
    checkFormErrors()

    const form = document.querySelector(".register-form")
    const submitButton = document.getElementById("registerButton")

    // Replace the form submission event listener in the DOMContentLoaded function with this improved version:
    if (form && submitButton) {
        form.addEventListener("submit", (event) => {
            // Always prevent default to handle form submission manually
            event.preventDefault()

            // Validate the form
            if (!validateForm()) {
                return false
            }

            // Set loading state
            isFormSubmitting = true
            submitButton.disabled = true
            submitButton.innerHTML =
                '<span class="loading-spinner"></span> ' + (window.messages?.submitting || "Submitting...")

            // Submit the form
            form.submit()
        })
    }

    // Add event listeners
    const addSlotBtn = document.getElementById("add-slot-btn")
    if (addSlotBtn) {
        // addSlotBtn.addEventListener("click", addTimeSlotRow) // Replaced with new implementation
    }

    // Enhanced password validation - make it more responsive
    const passwordField = document.getElementById("password")
    if (passwordField) {
        passwordField.addEventListener("input", function () {
            checkPasswordStrength()
            validatePasswordRequirements(this.value)
            updateNextButtonState(1)
        })
    }

    const repeatPasswordField = document.getElementById("repeatPassword")
    if (repeatPasswordField) {
        repeatPasswordField.addEventListener("input", () => {
            checkPasswordMatch()
            updateNextButtonState(1)
        })
    }

    if (passwordField) {
        passwordField.addEventListener("input", () => {
            checkPasswordMatch()
            updateNextButtonState(1)
        })
    }

    // Enhanced email validation with real-time feedback
    const emailField = document.getElementById("email")
    if (emailField) {
        emailField.addEventListener("input", function () {
            validateEmail(this)
            updateNextButtonState(1)
        })

        emailField.addEventListener("blur", function () {
            validateEmail(this)
            updateNextButtonState(1)
        })
    }

    // Enhanced phone validation with real-time feedback
    const phoneField = document.getElementById("phone")
    if (phoneField) {
        phoneField.addEventListener("input", function () {
            validatePhone(this)
            updateNextButtonState(1)
        })

        phoneField.addEventListener("blur", function () {
            validatePhone(this)
            updateNextButtonState(1)
        })
    }

    // Add validation for name and lastName
    const nameField = document.getElementById("name")
    const lastNameField = document.getElementById("lastName")

    if (nameField) {
        nameField.addEventListener("input", function () {
            validateName(this)
            updateNextButtonState(1)
        })

        nameField.addEventListener("blur", function () {
            validateName(this)
        })
    }

    if (lastNameField) {
        lastNameField.addEventListener("input", function () {
            validateName(this)
            updateNextButtonState(1)
        })

        lastNameField.addEventListener("blur", function () {
            validateName(this)
        })
    }

    restorePasswordValues()
    addInputValidationListeners()
    setupFloatingLabels()
    addValidationIcons()

    // Update all validation states at the end
    updateAllValidationStates()
})

// Add validation icons to input fields
function addValidationIcons() {
    const inputFields = document.querySelectorAll(".form-control")

    inputFields.forEach((field) => {
        // Skip select elements and hidden fields
        if (field.tagName === "SELECT" || field.type === "hidden") return

        // Get the parent element
        const parent = field.parentElement
        if (!parent) return

        // Create wrapper if it doesn't exist
        if (!parent.classList.contains("input-wrapper")) {
            const wrapper = document.createElement("div")
            wrapper.className = "input-wrapper"

            // Insert wrapper before the field
            parent.insertBefore(wrapper, field)

            // Move field into wrapper
            wrapper.appendChild(field)

            // Create validation icons
            const validIcon = document.createElement("div")
            validIcon.className = "validation-icon valid"
            validIcon.innerHTML =
                '<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>'

            const errorIcon = document.createElement("div")
            errorIcon.className = "validation-icon error"
            errorIcon.innerHTML =
                '<svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><line x1="12" y1="8" x2="12" y2="12"></line><line x1="12" y1="16" x2="12.01" y2="16"></line></svg>'

            wrapper.appendChild(validIcon)
            wrapper.appendChild(errorIcon)
        }
    })
}

// Setup floating labels
function setupFloatingLabels() {
    const floatingFields = document.querySelectorAll(".form-floating .form-control")

    floatingFields.forEach((field) => {
        if (!field) return

        // Set placeholder to space to trigger CSS selector
        field.setAttribute("placeholder", " ")

        // Add focus and blur events
        field.addEventListener("focus", function () {
            const parent = this.parentElement
            if (parent) {
                parent.classList.add("focused")
            }
        })

        field.addEventListener("blur", function () {
            const parent = this.parentElement
            if (parent) {
                parent.classList.remove("focused")
            }
        })
    })
}

// Validate name fields
function validateName(field) {
    if (!field) return false

    const value = field.value.trim()
    const errorElement = document.getElementById(field.id + "-validation-message")
    const parent = field.closest(".input-wrapper")

    if (!value && field.hasAttribute("required")) {
        setFieldError(field, errorElement, window.messages?.fieldRequired || "This field is required")
        return false
    } else if (value && value.length < 2) {
        setFieldError(field, errorElement, window.messages?.nameTooShort || "Name must be at least 2 characters")
        return false
    } else if (value && !/^[A-Za-zÀ-ÖØ-öø-ÿ\s'-]+$/.test(value)) {
        setFieldError(field, errorElement, window.messages?.nameInvalid || "Name contains invalid characters")
        return false
    } else if (value) {
        setFieldValid(field, errorElement)
        return true
    } else {
        clearFieldValidation(field, errorElement)
        return true
    }
}

// Initialize file upload preview with enhanced validation
// Replace the initFileUpload function with this improved version:

function initFileUpload() {
    const fileInput = document.getElementById("image-upload")
    const fileName = document.getElementById("file-name")
    const fileLabel = document.querySelector(".file-upload-label")

    if (fileInput && fileName && fileLabel) {
        fileInput.addEventListener("change", function () {
            const errorElement = document.getElementById("image-error")
            if (!errorElement) return

            if (this.files && this.files[0]) {
                const file = this.files[0]

                // Validate file size
                if (file.size > 2 * 1024 * 1024) {
                    errorElement.textContent = window.messages?.fileSizeError || "File size exceeds the 2MB limit"
                    errorElement.style.display = "block"
                    fileLabel.classList.add("error")
                    return
                }

                // Validate file type
                const validTypes = ["image/jpeg", "image/png", "image/gif", "image/webp"]
                if (!validTypes.includes(file.type)) {
                    errorElement.textContent =
                        window.messages?.fileTypeError || "Please upload a valid image file (JPEG, PNG, GIF, WEBP)"
                    errorElement.style.display = "block"
                    fileLabel.classList.add("error")
                    return
                }

                // Clear error and show file name
                errorElement.textContent = ""
                errorElement.style.display = "none"
                fileLabel.classList.remove("error")
                fileLabel.classList.add("valid")

                // Format file size for display
                const fileSize = formatFileSize(file.size)

                // Get file extension
                const fileExtension = file.name.split(".").pop().toUpperCase()

                // Preview image with improved UI
                const reader = new FileReader()
                reader.onload = (e) => {
                    // Remove existing preview if any
                    const existingPreview = document.querySelector(".file-preview-container")
                    if (existingPreview) {
                        existingPreview.remove()
                    }

                    // Create improved preview container with new design
                    const previewContainer = document.createElement("div")
                    previewContainer.className = "file-preview-container"

                    // Create image preview
                    const imagePreview = document.createElement("div")
                    imagePreview.className = "file-preview"
                    imagePreview.innerHTML = `<img src="${e.target.result}" alt="Preview">`

                    // Create file info section
                    const fileInfo = document.createElement("div")
                    fileInfo.className = "file-info"

                    // Add file name
                    const fileNameDisplay = document.createElement("div")
                    fileNameDisplay.className = "file-name-display"
                    fileNameDisplay.textContent = file.name

                    // Add file type and size
                    const fileTypeSize = document.createElement("div")
                    fileTypeSize.className = "file-type-size"
                    fileTypeSize.textContent = `${fileExtension} • ${fileSize}`

                    fileInfo.appendChild(fileNameDisplay)
                    fileInfo.appendChild(fileTypeSize)

                    // Create remove button
                    const removeButton = document.createElement("button")
                    removeButton.type = "button"
                    removeButton.className = "remove-image-btn"
                    removeButton.innerHTML = "×"
                    removeButton.setAttribute("aria-label", "Remove image")
                    removeButton.onclick = () => {
                        // Clear the file input
                        fileInput.value = ""
                        // Remove the preview
                        previewContainer.remove()
                        // Reset the file name
                        fileName.textContent = ""
                        // Reset the label
                        fileLabel.classList.remove("valid")
                    }

                    // Add elements to container
                    previewContainer.appendChild(imagePreview)
                    previewContainer.appendChild(fileInfo)
                    previewContainer.appendChild(removeButton)

                    // Add preview container after file name
                    fileName.parentNode.insertBefore(previewContainer, fileName.nextSibling)
                }
                reader.readAsDataURL(file)
            }
        })
    }
}

// Format file size for display
function formatFileSize(bytes) {
    if (bytes < 1024) return bytes + " bytes"
    else if (bytes < 1048576) return (bytes / 1024).toFixed(1) + " KB"
    else return (bytes / 1048576).toFixed(1) + " MB"
}

function restorePasswordValues() {
    const passwordValue = document.getElementById("passwordValue")
    const repeatPasswordValue = document.getElementById("repeatPasswordValue")
    const passwordField = document.getElementById("password")
    const repeatPasswordField = document.getElementById("repeatPassword")

    if (passwordValue && passwordValue.value && passwordField) {
        passwordField.value = passwordValue.value
        checkPasswordStrength()
        validatePasswordRequirements(passwordField.value)
    }

    if (repeatPasswordValue && repeatPasswordValue.value && repeatPasswordField) {
        repeatPasswordField.value = repeatPasswordValue.value
        checkPasswordMatch()
    }
}

function addInputValidationListeners() {
    // Section 1 fields
    const section1 = document.querySelector('.form-section[data-section="1"]')
    if (!section1) return

    const requiredFields1 = section1.querySelectorAll("input[required]")
    requiredFields1.forEach((field) => {
        field.addEventListener("input", () => {
            updateNextButtonState(1)
        })

        // Add blur event for validation
        field.addEventListener("blur", function () {
            validateField(this)
        })
    })

    // Section 2 fields
    const section2 = document.querySelector('.form-section[data-section="2"]')
    if (!section2) return

    // Add custom event listeners for multi-select components
    const specialtiesOptions = document.querySelectorAll("#specialties-options .custom-multi-select-option")
    specialtiesOptions.forEach((option) => {
        option.addEventListener("click", () => {
            setTimeout(() => {
                updateNextButtonState(2)
                validateMultiSelect("specialties")
            }, 100)
        })
    })

    const coveragesOptions = document.querySelectorAll("#coverages-options .custom-multi-select-option")
    coveragesOptions.forEach((option) => {
        option.addEventListener("click", () => {
            setTimeout(() => {
                updateNextButtonState(2)
                validateMultiSelect("coverages")
            }, 100)
        })
    })

    // Section 3 fields
    const section3 = document.querySelector('.form-section[data-section="3"]')
    if (!section3) return

    const termsCheckbox = document.getElementById("terms")
    if (termsCheckbox) {
        termsCheckbox.addEventListener("change", function () {
            updateSubmitButtonState()
            validateCheckbox(this)
        })
    }
}

// Validate a checkbox
function validateCheckbox(checkbox) {
    if (!checkbox) return false

    const label = document.querySelector(`label[for="${checkbox.id}"]`)
    if (!label) return false

    if (checkbox.required && !checkbox.checked) {
        label.classList.add("error")
        return false
    } else {
        label.classList.remove("error")
        return true
    }
}

// Validate multi-select
function validateMultiSelect(selectId) {
    const select = document.getElementById(selectId)
    if (!select) return false

    const container = document.getElementById(selectId + "-container")
    if (!container) return false

    const errorElement = container.querySelector(".error-message")

    if (!select.options || select.selectedOptions.length === 0) {
        container.classList.add("error")
        if (errorElement) {
            errorElement.textContent = window.messages?.[selectId + "Required"] || "Please select at least one option"
            errorElement.classList.add("visible")
        }
        return false
    } else {
        container.classList.remove("error")
        if (errorElement) {
            errorElement.classList.remove("visible")
        }
        return true
    }
}

// Generic field validation
function validateField(field) {
    if (!field) return false

    if (field.id === "email") {
        return validateEmail(field)
    } else if (field.id === "phone") {
        return validatePhone(field)
    } else if (field.id === "password") {
        return validatePasswordRequirements(field.value)
    } else if (field.id === "repeatPassword") {
        return checkPasswordMatch()
    } else if (field.id === "name" || field.id === "lastName") {
        return validateName(field)
    } else if (field.required && !field.value.trim()) {
        const errorElement = document.getElementById(field.id + "-validation-message")
        setFieldError(field, errorElement, window.messages?.fieldRequired || "This field is required")
        return false
    } else {
        const errorElement = document.getElementById(field.id + "-validation-message")
        if (field.value.trim()) {
            setFieldValid(field, errorElement)
        } else {
            clearFieldValidation(field, errorElement)
        }
        return true
    }
}

// Set field error state
function setFieldError(field, errorElement, message) {
    if (!field) return

    field.classList.add("error")
    field.classList.remove("valid")

    // Update validation icons
    const wrapper = field.closest(".input-wrapper")
    if (wrapper) {
        const validIcon = wrapper.querySelector(".validation-icon.valid")
        const errorIcon = wrapper.querySelector(".validation-icon.error")

        if (validIcon) validIcon.style.opacity = "0"
        if (errorIcon) errorIcon.style.opacity = "1"
    }

    if (errorElement) {
        errorElement.textContent = message
        errorElement.classList.add("visible")
    }
}

// Set field valid state
function setFieldValid(field, errorElement) {
    if (!field) return

    field.classList.remove("error")
    field.classList.add("valid")

    // Update validation icons
    const wrapper = field.closest(".input-wrapper")
    if (wrapper) {
        const validIcon = wrapper.querySelector(".validation-icon.valid")
        const errorIcon = wrapper.querySelector(".validation-icon.error")

        if (validIcon) validIcon.style.opacity = "1"
        if (errorIcon) errorIcon.style.opacity = "0"
    }

    if (errorElement) {
        errorElement.classList.remove("visible")
    }
}

// Clear field validation state
function clearFieldValidation(field, errorElement) {
    if (!field) return

    field.classList.remove("error")
    field.classList.remove("valid")

    // Update validation icons
    const wrapper = field.closest(".input-wrapper")
    if (wrapper) {
        const validIcon = wrapper.querySelector(".validation-icon.valid")
        const errorIcon = wrapper.querySelector(".validation-icon.error")

        if (validIcon) validIcon.style.opacity = "0"
        if (errorIcon) errorIcon.style.opacity = "0"
    }

    if (errorElement) {
        errorElement.classList.remove("visible")
    }
}

function updateAllButtonStates() {
    updateNextButtonState(1)
    updateNextButtonState(2)
    updateSubmitButtonState()
}

// Update next button state based on section validation
function updateNextButtonState(sectionNumber) {
    const nextButton = document.querySelector(`.form-section[data-section="${sectionNumber}"] .btn-next`)
    if (!nextButton) return

    const isValid = validateSectionWithoutUI(sectionNumber)

    // Additional check for time slots in section 3
    if (sectionNumber === 3) {
        // const hasTimeSlots = timeSlots.length > 0 // Replaced with new implementation
        // const hasTimeSlotErrors = document.querySelectorAll(".slot-error").length > 0 // Replaced with new implementation
        // nextButton.disabled = !isValid || !hasTimeSlots || hasTimeSlotErrors // Replaced with new implementation
    } else {
        nextButton.disabled = !isValid
    }

    // Add/remove disabled class for styling
    if (nextButton.disabled) {
        nextButton.classList.add("disabled")
    } else {
        nextButton.classList.remove("disabled")
    }
}

// Validate section without updating UI
function validateSectionWithoutUI(sectionNumber) {
    let isValid = true
    const section = document.querySelector(`.form-section[data-section="${sectionNumber}"]`)
    if (!section) return false

    // Check required fields in the current section
    const requiredFields = section.querySelectorAll("input[required], select[required]")
    requiredFields.forEach((field) => {
        if (!field.value) {
            isValid = false
        }
    })

    // Section-specific validations
    if (sectionNumber === 1) {
        // Validate password match
        const password = document.getElementById("password")
        const repeatPassword = document.getElementById("repeatPassword")

        if (!password || !repeatPassword) return false

        if (password.value.length < 8) {
            isValid = false
        }

        if (password.value && repeatPassword.value && password.value !== repeatPassword.value) {
            isValid = false
        }

        // Validate email format
        const email = document.getElementById("email")
        if (email && email.value && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value)) {
            isValid = false
        }

        // Validate phone format
        const phone = document.getElementById("phone")
        if (phone && phone.value && !/\+?[0-9. ()-]{7,25}/.test(phone.value)) {
            isValid = false
        }
    } else if (sectionNumber === 2) {
        // Validate specialties and coverages
        const specialties = document.getElementById("specialties")
        const coverages = document.getElementById("coverages")

        if (!specialties || !coverages) return false

        if (!specialties.options || specialties.selectedOptions.length === 0) {
            isValid = false
        }

        if (!coverages.options || coverages.selectedOptions.length === 0) {
            isValid = false
        }
    }

    return isValid
}

// Initialize password strength meter
function initPasswordStrength() {
    const password = document.getElementById("password")
    if (password && password.value) {
        checkPasswordStrength()
    }
}

// Validate password requirements
function validatePasswordRequirements(password) {
    // Get or create requirements list
    let requirementsList = document.getElementById("password-requirements")
    if (!requirementsList) {
        const passwordField = document.getElementById("password")
        if (!passwordField) return false

        const passwordContainer = passwordField.parentElement
        if (!passwordContainer) return false

        requirementsList = document.createElement("ul")
        requirementsList.id = "password-requirements"
        requirementsList.className = "requirements-list"

        // Add after password strength meter
        const strengthMeter = passwordContainer.querySelector(".password-strength")
        if (strengthMeter) {
            passwordContainer.insertBefore(requirementsList, strengthMeter.nextSibling)
        } else {
            passwordContainer.appendChild(requirementsList)
        }

        // Create requirement items
        const requirements = [
            { id: "length", text: window.messages?.passwordReqLength || "At least 8 characters" },
            { id: "uppercase", text: window.messages?.passwordReqUppercase || "At least one uppercase letter" },
            { id: "lowercase", text: window.messages?.passwordReqLowercase || "At least one lowercase letter" },
            { id: "number", text: window.messages?.passwordReqNumber || "At least one number" },
            { id: "special", text: window.messages?.passwordReqSpecial || "At least one special character" },
        ]

        requirements.forEach((req) => {
            const item = document.createElement("li")
            item.id = `req-${req.id}`
            item.className = "requirement-item"
            item.textContent = req.text
            requirementsList.appendChild(item)
        })
    }

    // Validate each requirement
    const lengthReq = document.getElementById("req-length")
    const uppercaseReq = document.getElementById("req-uppercase")
    const lowercaseReq = document.getElementById("req-lowercase")
    const numberReq = document.getElementById("req-number")
    const specialReq = document.getElementById("req-special")

    // Check requirements
    const hasLength = password && password.length >= 8
    const hasUppercase = password && /[A-Z]/.test(password)
    const hasLowercase = password && /[a-z]/.test(password)
    const hasNumber = password && /[0-9]/.test(password)
    const hasSpecial = password && /[^A-Za-z0-9]/.test(password)

    // Update UI - with null checks
    if (lengthReq) updateRequirement(lengthReq, hasLength)
    if (uppercaseReq) updateRequirement(uppercaseReq, hasUppercase)
    if (lowercaseReq) updateRequirement(lowercaseReq, hasLowercase)
    if (numberReq) updateRequirement(numberReq, hasNumber)
    if (specialReq) updateRequirement(specialReq, hasSpecial)

    // Update field validation state
    const passwordField = document.getElementById("password")
    if (!passwordField) return false

    const errorElement = document.getElementById("password-length-message")

    if (password) {
        // if (hasLength && (hasUppercase || hasLowercase) && (hasNumber || hasSpecial))
        if (hasLength && hasUppercase && hasLowercase && hasNumber && hasSpecial)
        {
            setFieldValid(passwordField, errorElement)
            return true
        } else {
            setFieldError(
                passwordField,
                errorElement,
                window.messages?.passwordInvalid || "Password does not meet requirements",
            )
            return false
        }
    } else {
        clearFieldValidation(passwordField, errorElement)
        return true
    }
}

// Update requirement item state
function updateRequirement(element, isValid) {
    if (!element) return

    element.classList.remove("valid", "invalid")
    if (isValid) {
        element.classList.add("valid")
    } else {
        element.classList.add("invalid")
    }
}

// Initialize multi-select components
function initMultiSelect() {
    updateSelectedDisplay("specialties")
    updateSelectedDisplay("coverages")
}


// Replace the validateForm function with this improved version:
// Form validation
function validateForm() {

    // Validate all sections
    for (let i = 1; i <= 3; i++) {
        if (!validateSection(i)) {
            showSection(i)
            return false
        }
    }

    // Check terms checkbox
    const termsCheckbox = document.getElementById("terms")
    if (termsCheckbox && !termsCheckbox.checked) {
        showSection(3)
        return false
    }

    // ✅ Validate time slots
    if (!areTimeSlotsValid()) {
        return false
    }


    // Store password values in hidden fields before submission
    const passwordField = document.getElementById("password")
    const repeatPasswordField = document.getElementById("repeatPassword")
    const passwordValueField = document.getElementById("passwordValue")
    const repeatPasswordValueField = document.getElementById("repeatPasswordValue")

    if (passwordField && passwordValueField) {
        passwordValueField.value = passwordField.value
    }

    if (repeatPasswordField && repeatPasswordValueField) {
        repeatPasswordValueField.value = repeatPasswordField.value
    }

    return true
}

// Check if there are form errors and show the appropriate section
function checkFormErrors() {
    if (window.hasErrors && window.errorSection) {
        showSection(window.errorSection)
    }
}

// Generate hours for select options (8 AM to 8 PM)
function getHourOptions() {
    const hours = []
    for (let i = 8; i <= 20; i++) {
        const hour = i % 12 || 12
        const ampm = i < 12 ? "AM" : "PM"
        const value = (i < 10 ? "0" + i : i) + ":00"
        const display = hour + ":00 " + ampm
        hours.push({ value: value, display: display })
    }
    return hours
}

// Form navigation functions
function nextSection(currentSection) {
    // Validate current section
    if (!validateSection(currentSection)) {
        return
    }

    // Hide current section
    const currentSectionElement = document.querySelector(`.form-section[data-section="${currentSection}"]`)
    if (currentSectionElement) {
        currentSectionElement.classList.remove("active")
    }

    // Show next section
    const nextSectionNum = currentSection + 1
    const nextSectionElement = document.querySelector(`.form-section[data-section="${nextSectionNum}"]`)
    if (nextSectionElement) {
        nextSectionElement.classList.add("active")
    }

    // Update progress indicator
    updateProgressIndicator(nextSectionNum)

    // Update button states for the new section
    if (nextSectionNum === 2) {
        updateNextButtonState(2)
    } else if (nextSectionNum === 3) {
        updateSubmitButtonState()
    }

    // Scroll to top of form
    const cardBody = document.querySelector(".card-body")
    if (cardBody) {
        cardBody.scrollTop = 0
    }

    // Add entrance animation
    if (nextSectionElement) {
        nextSectionElement.style.animation = "fadeIn 0.5s ease-in-out"
        setTimeout(() => {
            nextSectionElement.style.animation = ""
        }, 500)
    }
}

function prevSection(currentSection) {
    // Hide current section
    const currentSectionElement = document.querySelector(`.form-section[data-section="${currentSection}"]`)
    if (currentSectionElement) {
        currentSectionElement.classList.remove("active")
    }

    // Show previous section
    const prevSectionNum = currentSection - 1
    const prevSectionElement = document.querySelector(`.form-section[data-section="${prevSectionNum}"]`)
    if (prevSectionElement) {
        prevSectionElement.classList.add("active")
    }

    // Update progress indicator
    updateProgressIndicator(prevSectionNum)

    // Scroll to top of form
    const cardBody = document.querySelector(".card-body")
    if (cardBody) {
        cardBody.scrollTop = 0
    }

    // Add entrance animation
    if (prevSectionElement) {
        prevSectionElement.style.animation = "fadeIn 0.5s ease-in-out"
        setTimeout(() => {
            prevSectionElement.style.animation = ""
        }, 500)
    }
}

function showSection(sectionNumber) {
    // Hide all sections
    document.querySelectorAll(".form-section").forEach((section) => {
        section.classList.remove("active")
    })

    // Show requested section
    const sectionToShow = document.querySelector(`.form-section[data-section="${sectionNumber}"]`)
    if (sectionToShow) {
        sectionToShow.classList.add("active")
    }

    // Update progress indicator
    updateProgressIndicator(sectionNumber)
}

function updateProgressIndicator(currentStep) {
    // Reset all steps
    document.querySelectorAll(".progress-step").forEach((step) => {
        step.classList.remove("active", "completed")
    })

    // Mark current step as active
    const currentStepElement = document.querySelector(`.progress-step[data-step="${currentStep}"]`)
    if (currentStepElement) {
        currentStepElement.classList.add("active")
    }

    // Mark previous steps as completed
    for (let i = 1; i < currentStep; i++) {
        const prevStepElement = document.querySelector(`.progress-step[data-step="${i}"]`)
        if (prevStepElement) {
            prevStepElement.classList.add("completed")
        }
    }
}

function validateSection(sectionNumber) {
    let isValid = true
    const section = document.querySelector(`.form-section[data-section="${sectionNumber}"]`)
    if (!section) return false

    // Check required fields in the current section
    const requiredFields = section.querySelectorAll("input[required], select[required]")
    requiredFields.forEach((field) => {
        if (!field.value) {
            field.classList.add("error")

            // Show error message
            const errorElement = document.getElementById(field.id + "-validation-message")
            if (errorElement) {
                errorElement.textContent = window.messages?.fieldRequired || "This field is required"
                errorElement.classList.add("visible")
            }

            isValid = false
        } else {
            field.classList.remove("error")

            // Hide error message
            const errorElement = document.getElementById(field.id + "-validation-message")
            if (errorElement) {
                errorElement.classList.remove("visible")
            }
        }
    })

    // Section-specific validations
    if (sectionNumber === 1) {
        // Validate each field
        const fields = ["name", "lastName", "email", "phone", "password", "repeatPassword"]
        fields.forEach((fieldId) => {
            const field = document.getElementById(fieldId)
            if (field) {
                const isFieldValid = validateField(field)
                isValid = isValid && isFieldValid
            }
        })
    } else if (sectionNumber === 2) {
        // Validate specialties and coverages
        const specialtiesValid = validateMultiSelect("specialties")
        const coveragesValid = validateMultiSelect("coverages")

        isValid = isValid && specialtiesValid && coveragesValid
    }

    return isValid
}

// Check password strength
function checkPasswordStrength() {
    const password = document.getElementById("password")?.value || ""
    const strengthMeterFill = document.querySelector(".strength-meter-fill")
    const strengthText = document.querySelector(".strength-text")
    const passwordStrength = document.querySelector(".password-strength")

    // Exit if elements don't exist
    if (!passwordStrength || !strengthText) return

    // Remove all classes
    passwordStrength.className = "password-strength"

    if (!password) {
        strengthText.textContent = ""
        return
    }

    // Calculate strength
    let strength = 0

    // Length check
    if (password.length >= 8) strength += 1
    if (password.length >= 12) strength += 1

    // Character variety check
    if (/[A-Z]/.test(password)) strength += 1
    if (/[a-z]/.test(password)) strength += 1
    if (/[0-9]/.test(password)) strength += 1
    if (/[^A-Za-z0-9]/.test(password)) strength += 1

    // Set strength level
    let strengthClass = ""
    let strengthLabel = ""
    let strengthIcon = ""

    if (strength < 3) {
        strengthClass = "strength-weak"
        strengthLabel = window.messages?.passwordWeak || "Weak"
        strengthIcon =
            '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 9v4"></path><path d="M12 17h.01"></path><circle cx="12" cy="12" r="10"></circle></svg>'
    } else if (strength < 4) {
        strengthClass = "strength-medium"
        strengthLabel = window.messages?.passwordMedium || "Medium"
        strengthIcon =
            '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 9v4"></path><path d="M12 17h.01"></path><circle cx="12" cy="12" r="10"></circle></svg>'
    } else if (strength < 6) {
        strengthClass = "strength-strong"
        strengthLabel = window.messages?.passwordStrong || "Strong"
        strengthIcon =
            '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M20 6L9 17l-5-5"></path></svg>'
    } else {
        strengthClass = "strength-very-strong"
        strengthLabel = window.messages?.passwordVeryStrong || "Very Strong"
        strengthIcon =
            '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M20 6L9 17l-5-5"></path></svg>'
    }

    passwordStrength.classList.add(strengthClass)
    strengthText.innerHTML = strengthIcon + " " + strengthLabel
}

// Validate email
function validateEmail(field) {
    if (!field) return false

    const email = field.value
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    const errorElement = document.getElementById("email-validation-message")

    if (!email && field.hasAttribute("required")) {
        setFieldError(field, errorElement, window.messages?.fieldRequired || "Email is required")
        return false
    } else if (email && !emailPattern.test(email)) {
        setFieldError(field, errorElement, window.messages?.emailInvalid || "Please enter a valid email address")
        return false
    } else if (email) {
        setFieldValid(field, errorElement)
        return true
    } else {
        clearFieldValidation(field, errorElement)
        return true
    }
}

// Validate phone
function validatePhone(field) {
    if (!field) return false

    const phone = field.value
    const phonePattern = /^\+?[0-9. ()-]{7,25}$/
    const errorElement = document.getElementById("phone-validation-message")

    if (!phone && field.hasAttribute("required")) {
        setFieldError(field, errorElement, window.messages?.fieldRequired || "Phone is required")
        return false
    } else if (phone && !phonePattern.test(phone)) {
        setFieldError(field, errorElement, window.messages?.phoneInvalid || "Please enter a valid phone number")
        return false
    } else if (phone) {
        setFieldValid(field, errorElement)
        return true
    } else {
        clearFieldValidation(field, errorElement)
        return true
    }
}

// Check if passwords match
function checkPasswordMatch() {
    const password = document.getElementById("password")?.value || ""
    const repeatPassword = document.getElementById("repeatPassword")?.value || ""
    const matchMessage = document.getElementById("password-match-message")
    const repeatPasswordField = document.getElementById("repeatPassword")

    if (!repeatPasswordField) return false

    if (!repeatPassword && repeatPasswordField.hasAttribute("required")) {
        setFieldError(repeatPasswordField, matchMessage, window.messages?.fieldRequired || "Confirm password is required")
        return false
    } else if (password && repeatPassword) {
        if (password !== repeatPassword) {
            setFieldError(repeatPasswordField, matchMessage, window.messages?.passwordsDoNotMatch || "Passwords do not match")
            return false
        } else {
            setFieldValid(repeatPasswordField, matchMessage)
            return true
        }
    } else {
        clearFieldValidation(repeatPasswordField, matchMessage)
        return true
    }
}

// Global function to toggle option selection
function toggleOption(optionElement, selectId) {
    // Get the value of the clicked option
    const value = optionElement.getAttribute("data-value")

    // Toggle selected class
    optionElement.classList.toggle("selected")

    // Get the select element
    const select = document.getElementById(selectId)

    if (select) {
        // Find the corresponding option in the select element
        for (let i = 0; i < select.options.length; i++) {
            if (select.options[i].value === value) {
                // Toggle selection
                select.options[i].selected = optionElement.classList.contains("selected")
                break
            }
        }

        // Update the selected options display
        updateSelectedDisplay(selectId)

        // Remove error class if options are selected
        if (select.selectedOptions.length > 0) {
            const container = document.getElementById(selectId + "-container")
            if (container) {
                container.classList.remove("error")
            }

            // Hide error message
            const errorElement = document.querySelector(`#${selectId}-container .error-message`)
            if (errorElement) {
                errorElement.classList.remove("visible")
            }
        }

        // Update next button state after selection changes
        updateNextButtonState(2)
    }
}

// Improved updateSelectedDisplay function to update button states
function updateSelectedDisplay(selectId) {
    const select = document.getElementById(selectId)
    const selectedContainer = document.getElementById(selectId + "-selected")
    const options = document.querySelectorAll("#" + selectId + "-options .custom-multi-select-option")

    if (!select || !selectedContainer) return

    // Clear the selected container
    selectedContainer.innerHTML = ""

    // Get all selected options from the select element
    const selectedOptions = []
    for (let i = 0; i < select.options.length; i++) {
        if (select.options[i].selected) {
            selectedOptions.push(select.options[i])
        }
    }

    // Update the custom options UI to match the select element
    options.forEach((option) => {
        const value = option.getAttribute("data-value")
        let isSelected = false

        for (let i = 0; i < selectedOptions.length; i++) {
            if (selectedOptions[i].value === value) {
                isSelected = true
                break
            }
        }

        if (isSelected) {
            option.classList.add("selected")
        } else {
            option.classList.remove("selected")
        }
    })

    // Create badges for selected options
    selectedOptions.forEach((option) => {
        const badge = document.createElement("div")
        badge.className = "selected-option-badge"
        badge.innerHTML =
            option.text +
            '<span class="remove-option" data-value="' +
            option.value +
            '" onclick="removeOption(this, \'' +
            selectId +
            "')\">×</span>"

        selectedContainer.appendChild(badge)
    })
}

// Improved removeOption function to update button states
function removeOption(removeButton, selectId) {
    const value = removeButton.getAttribute("data-value")

    // Get the select element
    const select = document.getElementById(selectId)

    if (select) {
        // Find and deselect the option in the select element
        for (let i = 0; i < select.options.length; i++) {
            if (select.options[i].value === value) {
                select.options[i].selected = false
                break
            }
        }

        // Find and deselect the option in the custom UI
        const customOption = document.querySelector(
            "#" + selectId + '-options .custom-multi-select-option[data-value="' + value + '"]',
        )
        if (customOption) {
            customOption.classList.remove("selected")
        }

        // Update the selected options display
        updateSelectedDisplay(selectId)

        // Validate the multi-select
        validateMultiSelect(selectId)

        // Update next button state after removing an option
        updateNextButtonState(2)
    }
}

// Update all validation states
function updateAllValidationStates() {
    // Update button states
    updateAllButtonStates()

    // Validate all fields in all sections
    for (let i = 1; i <= 3; i++) {
        validateSectionWithoutUI(i)
    }
}

// Replace all time slot related code with this completely new implementation

// Global variables for time slots
let timeSlotCounter = 0
let availabilitySlots = []

// Initialize time slots when the page loads
document.addEventListener("DOMContentLoaded", () => {
    // Initialize time slots
    initializeTimeSlots()

    // Add event listener for the add slot button
    const addSlotButton = document.getElementById("add-slot-btn")
    if (addSlotButton) {
        addSlotButton.addEventListener("click", addNewTimeSlot)
    }

    // Handle form submission
    const form = document.querySelector(".register-form")
    if (form) {
        form.addEventListener("submit", function (event) {
            // Prevent default form submission
            event.preventDefault()

            // Validate the form including time slots
            if (!validateForm()) {
                return false
            }

            // Create hidden inputs for time slots
            createTimeSlotHiddenInputs()

            // Submit the form
            this.submit()
        })
    }
})

// Initialize time slots from existing data or create a default one
function initializeTimeSlots() {
    // Clear the existing slots array
    availabilitySlots = []

    // Check if we have existing slots from the server
    if (window.existingSlots && window.existingSlots.length > 0) {
        // Process existing slots
        window.existingSlots.forEach((slot) => {
            if (isValidSlotData(slot)) {
                // Add to our array with clean data
                availabilitySlots.push({
                    id: slot.index,
                    day: Number.parseInt(slot.day, 10),
                    startTime: slot.startTime,
                    endTime: slot.endTime,
                })

                // Update counter to be higher than any existing ID
                if (slot.index >= timeSlotCounter) {
                    timeSlotCounter = slot.index + 1
                }
            }
        })

        // Render all slots
        renderAllTimeSlots()
    } else {
        // Add a default time slot if none exist
        addNewTimeSlot()
    }

    // Update UI
    updateNoSlotsMessage()
}

// Validate slot data
function isValidSlotData(slot) {
    return slot && typeof slot.day !== "undefined" && slot.startTime && slot.endTime
}

// Render all time slots
function renderAllTimeSlots() {
    // Clear the container first
    const container = document.getElementById("time-slot-inputs")
    if (container) {
        container.innerHTML = ""
    }

    // Render each slot
    availabilitySlots.forEach((slot) => {
        renderTimeSlot(slot)
    })
    validateTimeSlots();
}

// Add a new time slot
function addNewTimeSlot() {
    // Create new slot with default values
    const newSlot = {
        id: timeSlotCounter++,
        day: 0, // Monday
        startTime: "09:00",
        endTime: "17:00",
    }

    // Add to array
    availabilitySlots.push(newSlot)

    // Render the new slot
    renderTimeSlot(newSlot)

    // Update UI
    updateNoSlotsMessage()

    // Check for overlaps
    validateTimeSlots()
}

// Render a single time slot
function renderTimeSlot(slot) {
    const container = document.getElementById("time-slot-inputs")
    if (!container) return

    // Create row
    const row = document.createElement("div")
    row.className = "time-slot-row"
    row.id = `time-slot-${slot.id}`
    row.dataset.slotId = slot.id

    // Create day select
    const dayContainer = document.createElement("div")
    dayContainer.className = "day-select"

    const dayLabel = document.createElement("label")
    dayLabel.className = "time-label"
    dayLabel.textContent = window.messages?.dayOfWeek || "Day of Week"
    dayContainer.appendChild(dayLabel)

    const daySelect = document.createElement("select")
    daySelect.className = "form-control slot-day"
    daySelect.dataset.slotId = slot.id

    const days = [
        { value: 0, text: window.messages?.monday || "Monday" },
        { value: 1, text: window.messages?.tuesday || "Tuesday" },
        { value: 2, text: window.messages?.wednesday || "Wednesday" },
        { value: 3, text: window.messages?.thursday || "Thursday" },
        { value: 4, text: window.messages?.friday || "Friday" },
        { value: 5, text: window.messages?.saturday || "Saturday" },
        { value: 6, text: window.messages?.sunday || "Sunday" },
    ]

    days.forEach((day) => {
        const option = document.createElement("option")
        option.value = day.value
        option.textContent = day.text
        daySelect.appendChild(option)
    })

    // Set selected day
    daySelect.value = slot.day

    // Add change event
    daySelect.addEventListener("change", function () {
        updateTimeSlot(slot.id, "day", Number.parseInt(this.value, 10))
    })

    dayContainer.appendChild(daySelect)

    // Create start time select
    const startContainer = document.createElement("div")
    startContainer.className = "time-select"

    const startLabel = document.createElement("label")
    startLabel.className = "time-label"
    startLabel.textContent = window.messages?.startTime || "Start Time"
    startContainer.appendChild(startLabel)

    const startSelect = document.createElement("select")
    startSelect.className = "form-control slot-start"
    startSelect.dataset.slotId = slot.id

    // Create end time select
    const endContainer = document.createElement("div")
    endContainer.className = "time-select"

    const endLabel = document.createElement("label")
    endLabel.className = "time-label"
    endLabel.textContent = window.messages?.endTime || "End Time"
    endContainer.appendChild(endLabel)

    const endSelect = document.createElement("select")
    endSelect.className = "form-control slot-end"
    endSelect.dataset.slotId = slot.id

    // Add time options to both selects
    const timeOptions = getTimeOptions()

    timeOptions.forEach((time) => {
        const startOption = document.createElement("option")
        startOption.value = time.value
        startOption.textContent = time.display
        startSelect.appendChild(startOption)

        const endOption = document.createElement("option")
        endOption.value = time.value
        endOption.textContent = time.display
        endSelect.appendChild(endOption)
    })

    // Set selected times
    startSelect.value = slot.startTime
    endSelect.value = slot.endTime

    // Add change events
    startSelect.addEventListener("change", function () {
        updateTimeSlot(slot.id, "startTime", this.value)
    })

    endSelect.addEventListener("change", function () {
        updateTimeSlot(slot.id, "endTime", this.value)
    })

    startContainer.appendChild(startSelect)
    endContainer.appendChild(endSelect)

    // Create remove button
    const removeBtn = document.createElement("button")
    removeBtn.type = "button"
    removeBtn.className = "btn-remove"
    removeBtn.textContent = "×"
    removeBtn.setAttribute("aria-label", "Remove time slot")
    removeBtn.dataset.slotId = slot.id

    removeBtn.addEventListener("click", () => {
        removeTimeSlot(slot.id)
    })

    // Add all elements to row
    row.appendChild(dayContainer)
    row.appendChild(startContainer)
    row.appendChild(endContainer)
    row.appendChild(removeBtn)

    // Add row to container with animation
    row.style.opacity = "0"
    row.style.transform = "translateY(10px)"
    container.appendChild(row)

    // Trigger animation
    setTimeout(() => {
        row.style.opacity = "1"
        row.style.transform = "translateY(0)"
    }, 10)
}

// Get time options for selects (8 AM to 8 PM)
function getTimeOptions() {
    const options = []

    for (let hour = 8; hour <= 20; hour++) {
        const hourFormatted = hour < 10 ? `0${hour}` : `${hour}`
        const hourDisplay = hour % 12 || 12
        const ampm = hour < 12 ? "AM" : "PM"

        options.push({
            value: `${hourFormatted}:00`,
            display: `${hourDisplay}:00 ${ampm}`,
        })
    }

    return options
}

// Update a time slot property
function updateTimeSlot(slotId, property, value) {
    // Find the slot
    const slotIndex = availabilitySlots.findIndex((slot) => slot.id === slotId)

    if (slotIndex !== -1) {
        // Update the property
        availabilitySlots[slotIndex][property] = value

        // Validate time slots
        validateTimeSlots()
    }
}

// Remove a time slot
function removeTimeSlot(slotId) {
    // Find the slot
    const slotIndex = availabilitySlots.findIndex((slot) => slot.id === slotId)

    if (slotIndex !== -1) {
        // Get the row element
        const row = document.getElementById(`time-slot-${slotId}`)

        if (row) {
            // Animate removal
            row.style.opacity = "0"
            row.style.transform = "translateY(-10px)"

            setTimeout(() => {
                // Remove from DOM
                if (row.parentNode) {
                    row.parentNode.removeChild(row)
                }

                // Remove from array
                availabilitySlots.splice(slotIndex, 1)

                // Update UI
                updateNoSlotsMessage()

                // Validate remaining slots
                validateTimeSlots()
            }, 300)
        }
    }
}

// Validate all time slots for overlaps and time order
function validateTimeSlots() {
    // Clear previous errors
    clearTimeSlotErrors()

    // Check each slot
    availabilitySlots.forEach((slot) => {
        const row = document.getElementById(`time-slot-${slot.id}`)
        if (!row) return

        // Check if end time is after start time
        if (slot.startTime >= slot.endTime) {
            markSlotError(row)
            showTimeSlotError(window.messages?.timeSlotInvalidTime || "End time must be after start time")
            return
        }

        // Check for overlaps with other slots
        const overlaps = availabilitySlots.filter((otherSlot) => {
            // Skip comparing with self
            if (otherSlot.id === slot.id) return false

            // Only check slots on the same day
            if (otherSlot.day !== slot.day) return false

            // Check for time overlap
            return !(slot.endTime <= otherSlot.startTime || slot.startTime >= otherSlot.endTime)
        })

        if (overlaps.length > 0) {
            // Mark this slot as error
            markSlotError(row)

            // Mark overlapping slots as error
            overlaps.forEach((overlapSlot) => {
                const overlapRow = document.getElementById(`time-slot-${overlapSlot.id}`)
                if (overlapRow) {
                    markSlotError(overlapRow)
                }
            })

            showTimeSlotError(window.messages?.timeSlotOverlap || "Time slots cannot overlap")
        }
    })

    // Update submit button state
    updateSubmitButtonState()
}

// Modify the showTimeSlotError function to make the error more visible:
function showTimeSlotError(message) {
    const errorElement = document.getElementById("time-slot-error")
    if (errorElement) {
        errorElement.textContent = message
        errorElement.style.display = "block"
        errorElement.classList.add("visible"); // Add visible class for better visibility

        // Also show the server-side error if it exists
        // const serverErrorElement = document.querySelector("form .error-message[data-path='availabilitySlots']");
        // if (serverErrorElement && serverErrorElement.textContent.trim()) {
        //     errorElement.textContent += " " + serverErrorElement.textContent;
        // }

        // Scroll to the error message
        errorElement.scrollIntoView({ behavior: 'smooth', block: 'center' });
    }
}

// Mark a slot row as having an error
function markSlotError(row) {
    row.classList.add("slot-error")
}

// Clear all time slot errors
function clearTimeSlotErrors() {
    // Clear error message
    const errorElement = document.getElementById("time-slot-error")
    if (errorElement) {
        errorElement.style.display = "none"
    }

    // Clear error class from all rows
    const errorRows = document.querySelectorAll(".slot-error")
    errorRows.forEach((row) => {
        row.classList.remove("slot-error")
    })
}

// Update the no slots message visibility
function updateNoSlotsMessage() {
    const noSlotsMessage = document.getElementById("no-slots-message")
    if (noSlotsMessage) {
        noSlotsMessage.style.display = availabilitySlots.length === 0 ? "block" : "none"
    }

    // Update submit button state
    updateSubmitButtonState()
}

// Create hidden inputs for time slots before form submission
function createTimeSlotHiddenInputs() {
    const form = document.querySelector(".register-form")
    if (!form) return

    // Remove any existing hidden inputs
    const existingInputs = document.querySelectorAll('input[name^="availabilitySlots["]')
    existingInputs.forEach((input) => {
        input.parentNode.removeChild(input)
    })

    // Create new hidden inputs for each slot
    availabilitySlots.forEach((slot, index) => {
        // Create day input
        const dayInput = document.createElement("input")
        dayInput.type = "hidden"
        dayInput.name = `availabilitySlots[${index}].dayOfWeek`
        dayInput.value = slot.day
        form.appendChild(dayInput)

        // Create start time input
        const startInput = document.createElement("input")
        startInput.type = "hidden"
        startInput.name = `availabilitySlots[${index}].startTime`
        startInput.value = slot.startTime
        form.appendChild(startInput)

        // Create end time input
        const endInput = document.createElement("input")
        endInput.type = "hidden"
        endInput.name = `availabilitySlots[${index}].endTime`
        endInput.value = slot.endTime
        form.appendChild(endInput)
    })
}

// Check if time slots are valid for form submission
function areTimeSlotsValid() {
    // Must have at least one time slot
    if (availabilitySlots.length === 0) {
        showTimeSlotError(window.messages?.timeSlotRequired || "At least one time slot is required")
        return false
    }

    // No slots should have errors
    const errorSlots = document.querySelectorAll(".slot-error")
    return errorSlots.length === 0
}

// Update submit button state based on time slot validation
function updateSubmitButtonState() {
    const submitButton = document.querySelector(".btn-submit-doctor")
    if (!submitButton) return

    const termsChecked = document.getElementById("terms")?.checked || false
    const timeSlotsValid = availabilitySlots.length > 0 && document.querySelectorAll(".slot-error").length === 0

    // Combine with other validation as needed
    const isValid = termsChecked && timeSlotsValid

    submitButton.disabled = !isValid
    submitButton.classList.toggle("disabled", !isValid)
}
