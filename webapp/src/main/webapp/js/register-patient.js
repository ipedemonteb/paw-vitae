let isFormSubmitting = false

document.addEventListener("DOMContentLoaded", () => {
    // Initialize password strength meter
    initPasswordStrength()
    // Check if there are form errors and show the appropriate section
    checkFormErrors()

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
        checkPasswordStrength()
        updateSubmitButtonState()
    })

    document.getElementById("repeatPassword").addEventListener("input", () => {
        checkPasswordMatch()
        updateSubmitButtonState()
    })
    document.getElementById("password").addEventListener("input", () => {
        checkPasswordMatch()
        updateSubmitButtonState()
    })

    document.getElementById("email").addEventListener("input", function () {
        validateEmail(this)
        updateSubmitButtonState()
    })

    document.getElementById("phone").addEventListener("input", function () {
        validatePhone(this)
        updateSubmitButtonState()
    })

    // Add listeners for name and lastName
    document.getElementById("name").addEventListener("input", () => {
        updateSubmitButtonState()
    })

    document.getElementById("lastName").addEventListener("input", () => {
        updateSubmitButtonState()
    })

    restorePasswordValues()

    // Add listeners for coverage radio buttons
    document.querySelectorAll('input[name="coverage"]').forEach((radio) => {
        radio.addEventListener("change", () => {
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

function restorePasswordValues() {
    const passwordValue = document.getElementById("passwordValue")
    const repeatPasswordValue = document.getElementById("repeatPasswordValue")
    const passwordField = document.getElementById("password")
    const repeatPasswordField = document.getElementById("repeatPassword")

    if (passwordValue && passwordValue.value) {
        passwordField.value = passwordValue.value
        checkPasswordStrength()
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

    // Validate password length
    if (password.length < 8) {
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
    }
}

// Check if there are form errors and show the appropriate section
function checkFormErrors() {
    if (window.hasErrors && window.errorSection) {
        console.log("IN HERE")
        showSection(window.errorSection)
    }
}

// Password strength checker
function checkPasswordStrength() {
    const password = document.getElementById("password").value
    const strengthMeterFill = document.querySelector(".strength-meter-fill")
    const strengthText = document.querySelector(".strength-text")

    // Remove all classes
    document.querySelector(".password-strength").className = "password-strength"

    if (!password) {
        // strengthMeter.style.width = '0';
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

    if (strength < 3) {
        strengthClass = "strength-weak"
        strengthLabel = window.messages.passwordWeak
    } else if (strength < 4) {
        strengthClass = "strength-medium"
        strengthLabel = window.messages.passwordMedium
    } else if (strength < 6) {
        strengthClass = "strength-strong"
        strengthLabel = window.messages.passwordStrong
    } else {
        strengthClass = "strength-very-strong"
        strengthLabel = window.messages.passwordVeryStrong
    }

    document.querySelector(".password-strength").classList.add(strengthClass)
    strengthText.textContent = strengthLabel
}

function validateEmail(field) {
    const email = field.value
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    const emailValidMessage = document.getElementById("email-validation-message")

    if (email && !emailPattern.test(email)) {
        field.classList.add("error")
        emailValidMessage.style.display = "block"
        emailValidMessage.textContent = window.messages.emailInvalid
    } else {
        emailValidMessage.style.display = "none"
        field.classList.remove("error")
    }
}

function validatePhone(field) {
    const phone = field.value
    const phonePattern = /^\+?[0-9. ()-]{7,25}$/
    const phoneValidMessage = document.getElementById("phone-validation-message")

    if (phone && !phonePattern.test(phone)) {
        field.classList.add("error")
        phoneValidMessage.style.display = "block"
        phoneValidMessage.textContent = window.messages.phoneInvalid
    } else {
        phoneValidMessage.style.display = "none"
        field.classList.remove("error")
    }
}

// Check if passwords match
function checkPasswordMatch() {
    const password = document.getElementById("password").value
    const repeatPassword = document.getElementById("repeatPassword").value
    const matchMessage = document.getElementById("password-match-message")
    const lengthMessage = document.getElementById("password-length-message")

    if (password && repeatPassword) {
        if (password !== repeatPassword) {
            matchMessage.textContent = window.messages.passwordsDoNotMatch
            matchMessage.style.display = "block"
            document.getElementById("repeatPassword").classList.add("error")
        } else {
            matchMessage.style.display = "none"
            document.getElementById("repeatPassword").classList.remove("error")
        }
    } else if (!password && !repeatPassword) {
        matchMessage.style.display = "none"
        document.getElementById("repeatPassword").classList.remove("error")
    }
    if (password) {
        if (password.length > 0 && password.length < 8) {
            lengthMessage.textContent = window.messages.passwordLength
            lengthMessage.style.display = "block"
            document.getElementById("password").classList.add("error")
        } else {
            lengthMessage.style.display = "none"
            document.getElementById("password").classList.remove("error")
        }
    } else {
        lengthMessage.style.display = "none"
        document.getElementById("password").classList.remove("error")
    }
}

// Form validation
function validateForm() {
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
