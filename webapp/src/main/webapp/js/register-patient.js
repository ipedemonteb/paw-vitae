let isFormSubmitting = false

document.addEventListener("DOMContentLoaded", () => {
    // Initialize password strength meter
    initPasswordStrength()
    // Check if there are form errors and show the appropriate section
    checkFormErrors()
    // Add validation icons to input fields
    addValidationIcons()

    const form = document.querySelector(".register-form")
    const submitButton = document.getElementById("registerButton")

    if (form && submitButton) {
        // Initially disable the button
        submitButton.disabled = true
        submitButton.classList.add("disabled")

        form.addEventListener("submit", () => {
            // Disable the button to prevent multiple submissions
            isFormSubmitting = true
            submitButton.disabled = true
        })
    }

    // Add event listeners
    document.getElementById("password").addEventListener("input", () => {
        removeServerError("password")
        checkPasswordStrength()
        validatePassword()
        updateSubmitButtonState()
    })

    document.getElementById("repeatPassword").addEventListener("input", () => {
        removeServerError("repeatPassword")
        checkPasswordMatch()
        updateSubmitButtonState()
    })
    document.getElementById("password").addEventListener("input", () => {
        removeServerError("password")
        checkPasswordMatch()
        updateSubmitButtonState()
    })

    document.getElementById("email").addEventListener("input", function () {
        removeServerError("email")
        validateEmail(this)
        updateSubmitButtonState()
    })

    document.getElementById("phone").addEventListener("input", function () {
        removeServerError("phone")
        validatePhone(this)
        updateSubmitButtonState()
    })

    // Add listeners for name and lastName
    document.getElementById("name").addEventListener("input", function () {
        removeServerError("name")
        validateName(this)
        updateSubmitButtonState()
    })

    document.getElementById("lastName").addEventListener("input", function () {
        removeServerError("lastName")
        validateName(this)
        updateSubmitButtonState()
    })

    restorePasswordValues()

    // Add listeners for coverage radio buttons
    document.querySelectorAll('input[name="coverage"]').forEach((radio) => {
        radio.addEventListener("change", () => {
            removeServerError("coverage")
            updateSubmitButtonState()
        })
    })

    // Add listener for terms checkbox
    document.getElementById("terms").addEventListener("change", () => {
        updateSubmitButtonState()
    })

    // Initial check of button state
    updateSubmitButtonState()
})

function removeServerError(name) {
    const errorDiv = document.querySelector(".server-error-" + name)
    if (errorDiv) {
        errorDiv.style.display = "none"
    }
}

// Replace the addValidationIcons function to use Font Awesome icons
function addValidationIcons() {
    const inputFields = document.querySelectorAll(".input-field")

    inputFields.forEach((field) => {
        const container = field.parentElement
        if (!container || !container.classList.contains("input-container")) return

        // Remove any existing validation icons first
        const existingIcons = container.querySelectorAll('.validation-icon')
        existingIcons.forEach(icon => icon.remove())

        // Create valid icon with Font Awesome
        const validIcon = document.createElement("div")
        validIcon.className = "validation-icon valid"
        validIcon.innerHTML = '<i class="fas fa-check-circle"></i>'

        // Create error icon with Font Awesome
        const errorIcon = document.createElement("div")
        errorIcon.className = "validation-icon error"
        errorIcon.innerHTML = '<i class="fas fa-exclamation-circle"></i>'

        // Add icons to container
        container.appendChild(validIcon)
        container.appendChild(errorIcon)
    })
}

// Keep the setFieldError and setFieldValid functions the same as they're working
function setFieldError(field, errorElement, message) {
    if (!field) return

    field.classList.add("input-error")
    field.classList.remove("valid")

    // Update validation icons
    const container = field.parentElement
    if (container) {
        const validIcon = container.querySelector('.validation-icon.valid')
        const errorIcon = container.querySelector('.validation-icon.error')

        if (validIcon) validIcon.style.opacity = "0"
        if (errorIcon) errorIcon.style.opacity = "1"
    }

    if (errorElement) {
        errorElement.textContent = message || ""
        errorElement.style.display = "block"
    }
}

function setFieldValid(field, errorElement) {
    if (!field) return

    field.classList.remove("input-error")
    field.classList.add("valid")

    // Update validation icons
    const container = field.parentElement
    if (container) {
        const validIcon = container.querySelector('.validation-icon.valid')
        const errorIcon = container.querySelector('.validation-icon.error')

        if (validIcon) validIcon.style.opacity = "1"
        if (errorIcon) errorIcon.style.opacity = "0"
    }

    if (errorElement) {
        errorElement.style.display = "none"
    }
}

// Validate name fields
function validateName(field) {

    const errorElement = document.getElementById(field.id + "-validation-message");
    if (!field) return false

    const value = field.value.trim()

    if (!value) {
        setFieldError(field)
        return false
    } else if (value.length < 2) {
        errorElement.style.display = "block"
        errorElement.textContent = window.messages.nameTooShort
        setFieldError(field)
        return false
    } else {
        errorElement.style.display = "none"
        setFieldValid(field)
        return true
    }
}

function restorePasswordValues() {
    const passwordValue = document.getElementById("passwordValue")
    const repeatPasswordValue = document.getElementById("repeatPasswordValue")
    const passwordField = document.getElementById("password")
    const repeatPasswordField = document.getElementById("repeatPassword")

    if (passwordValue && passwordValue.value) {
        passwordField.value = passwordValue.value
        checkPasswordStrength()
        validatePassword()
    }

    if (repeatPasswordValue && repeatPasswordValue.value) {
        repeatPasswordField.value = repeatPasswordValue.value
        checkPasswordMatch()
    }
}

function updateSubmitButtonState() {
    const submitButton = document.getElementById("registerButton")
    if (!submitButton || isFormSubmitting) return

    const isValid = validateAllFields()

    submitButton.disabled = !isValid

    // Add/remove disabled class for styling
    if (isValid) {
        submitButton.classList.remove("disabled")
    } else {
        submitButton.classList.add("disabled")
    }
}

function validateAllFields() {
    // Check all required text inputs
    const name = document.getElementById("name").value.trim()
    const lastName = document.getElementById("lastName").value.trim()
    const email = document.getElementById("email").value.trim()
    const phone = document.getElementById("phone").value.trim()
    const password = document.getElementById("password").value
    const repeatPassword = document.getElementById("repeatPassword").value

    // Check if any required field is empty
    if (!name || !lastName || !email || !phone || !password || !repeatPassword) {
        return false
    }

    // Check if coverage is selected
    const coverageSelected = document.querySelector('input[name="coverage"]:checked')
    if (!coverageSelected) {
        return false
    }

    // Check terms checkbox
    const termsChecked = document.getElementById("terms").checked
    if (!termsChecked) {
        return false
    }

    // Validate password with simplified requirements
    if (password.length < 8 || !/[A-Z]/.test(password) || !/[0-9]/.test(password)) {
        return false
    }

    // Validate passwords match
    if (password !== repeatPassword) {
        return false
    }

    // Validate email format
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
        return false
    }

    // Validate phone format
    if (!/\+?[0-9. ()-]{7,25}/.test(phone)) {
        return false
    }

    // If we got here, all validations passed
    return true
}

// Initialize password strength meter
function initPasswordStrength() {
    const password = document.getElementById("password")
    if (password && password.value) {
        checkPasswordStrength()
        validatePassword()
    }
}

// Check if there are form errors and show the appropriate section
function checkFormErrors() {
    if (window.hasErrors && window.errorSection) {
        showSection(window.errorSection)
    }
}

// Validate password
function validatePassword() {
    const password = document.getElementById("password").value
    const passwordField = document.getElementById("password")
    const lengthMessage = document.getElementById("password-length-message")

    // Check simplified requirements
    const hasLength = password.length >= 8
    const hasUppercase = /[A-Z]/.test(password)
    const hasNumber = /[0-9]/.test(password)

    if (password && (!hasLength || !hasUppercase || !hasNumber)) {
        // Password doesn't meet requirements
        setFieldError(passwordField)
        if (lengthMessage) {
            lengthMessage.textContent =
                window.messages?.passwordInvalid ||
                "Password must be at least 8 characters with one uppercase letter and one number"
            lengthMessage.style.display = "block"
        }
        return false
    } else if (password) {
        // Password meets requirements
        setFieldValid(passwordField)
        if (lengthMessage) {
            lengthMessage.style.display = "none"
        }
        return true
    } else {
        // No password entered
        passwordField.classList.remove("input-error")
        passwordField.classList.remove("valid")
        if (lengthMessage) {
            lengthMessage.style.display = "none"
        }
        return true
    }
}

// Password strength checker
function checkPasswordStrength() {
    const password = document.getElementById("password").value
    const strengthMeterFill = document.querySelector(".strength-fill")
    const strengthText = document.querySelector(".strength-text")

    // Remove all classes
    document.querySelector(".password-strength").className = "password-strength"

    if (!password) {
        // strengthMeter.style.width = '0';
        strengthText.textContent = ""
        return
    }

    // Calculate strength based on simplified requirements
    let strength = 0

    // Length check
    if (password.length >= 8) strength += 1
    if (password.length >= 12) strength += 1

    // Character variety check
    if (/[A-Z]/.test(password)) strength += 1
    if (/[0-9]/.test(password)) strength += 1
    if (/[^A-Za-z0-9]/.test(password)) strength += 1

    // Set strength level
    let strengthClass = ""
    let strengthLabel = ""

    if (strength < 3) {
        strengthClass = "strength-weak"
        strengthLabel = window.messages.passwordWeak
    } else if (strength < 5) {
        strengthClass = "strength-medium"
        strengthLabel = window.messages.passwordMedium
    } else {
        strengthClass = "strength-strong"
        strengthLabel = window.messages.passwordStrong
    }

    document.querySelector(".password-strength").classList.add(strengthClass)
    strengthText.textContent = strengthLabel
}

function validateEmail(field) {
    const email = field.value
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    const emailValidMessage = document.getElementById("email-validation-message")

    if (email && !emailPattern.test(email)) {
        setFieldError(field)
        emailValidMessage.style.display = "block"
        emailValidMessage.textContent = window.messages.emailInvalid
    } else if (email) {
        setFieldValid(field)
        emailValidMessage.style.display = "none"
    } else {
        field.classList.remove("input-error")
        field.classList.remove("valid")
        emailValidMessage.style.display = "none"
    }
}

function validatePhone(field) {
    const phone = field.value
    const phonePattern = /^\+?[0-9. ()-]{7,25}$/
    const phoneValidMessage = document.getElementById("phone-validation-message")

    if (phone && !phonePattern.test(phone)) {
        setFieldError(field)
        phoneValidMessage.style.display = "block"
        phoneValidMessage.textContent = window.messages.phoneInvalid
    } else if (phone) {
        setFieldValid(field)
        phoneValidMessage.style.display = "none"
    } else {
        field.classList.remove("input-error")
        field.classList.remove("valid")
        phoneValidMessage.style.display = "none"
    }
}

// Check if passwords match
function checkPasswordMatch() {
    const password = document.getElementById("password").value
    const repeatPassword = document.getElementById("repeatPassword").value
    const repeatPasswordField = document.getElementById("repeatPassword")
    const matchMessage = document.getElementById("password-match-message")

    if (password && repeatPassword) {
        if (password !== repeatPassword) {
            setFieldError(repeatPasswordField)
            matchMessage.textContent = window.messages.passwordsDoNotMatch
            matchMessage.style.display = "block"
        } else {
            setFieldValid(repeatPasswordField)
            matchMessage.style.display = "none"
        }
    } else if (!password && !repeatPassword) {
        repeatPasswordField.classList.remove("input-error")
        repeatPasswordField.classList.remove("valid")
        matchMessage.style.display = "none"
    }
}

// Form validation
function validateForm() {
    // Validate all fields
    validateName(document.getElementById("name"))
    validateName(document.getElementById("lastName"))
    validateEmail(document.getElementById("email"))
    validatePhone(document.getElementById("phone"))
    validatePassword()
    checkPasswordMatch()

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

    return validateAllFields()
}

function showSection(sectionId) {
    // Implementation of showSection function
    const section = document.getElementById(sectionId)
    if (section) {
        section.style.display = "block" // Or any other display style you want
    }
}
