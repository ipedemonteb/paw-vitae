<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="icon" type="image/png" href="https://upload.wikimedia.org/wikipedia/commons/thumb/a/aa/V-logo.svg/2048px-V-logo.svg.png" />
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="change.password.page.title" /></title>
    <link rel="stylesheet" href="<c:url value='/css/change-password.css' />" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
<!-- Include the header -->
<jsp:include page="/WEB-INF/jsp/components/header.jsp" />

<!-- Main Content -->
<main class="main-content">
    <div class="container">
        <div class="change-password-container">
            <div class="card">
                <div class="card-header">
                    <h1 class="card-title"><spring:message code="change.password.title" /></h1>
                    <p class="card-subtitle"><spring:message code="change.password.subtitle" /></p>
                </div>

                <div class="card-body">
                    <form:form modelAttribute="ChangePasswordForm" method="post" action="${pageContext.request.contextPath}/change-password?token=${token}" class="change-password-form">
                        <!-- Password Field -->
                        <div class="form-group">
                            <label for="password" class="form-label"><spring:message code="change.password.new.password" /></label>
                            <div class="password-input-wrapper">
                                <form:password path="password" id="password" class="form-control" required="true" />
                                <button type="button" class="toggle-password" aria-label="Toggle password visibility">
                                    <i class="fas fa-eye"></i>
                                </button>
                            </div>
                            <div id="password-length-message" class="validation-message"></div>
                            <form:errors path="password" cssClass="error-message" />

                            <!-- Password Strength Meter -->
                            <div id="pass-validations" class="password-validations">
                                <div class="password-strength">
                                    <div class="strength-meter">
                                        <div class="strength-meter-fill"></div>
                                    </div>
                                    <span class="strength-text"></span>
                                </div>
                            </div>
                        </div>

                        <!-- Repeat Password Field -->
                        <div class="form-group">
                            <label for="repeatPassword" class="form-label"><spring:message code="change.password.confirm.password" /></label>
                            <div class="password-input-wrapper">
                                <form:password path="repeatPassword" id="repeatPassword" class="form-control" required="true" />
                                <button type="button" class="toggle-password" aria-label="Toggle password visibility">
                                    <i class="fas fa-eye"></i>
                                </button>
                            </div>
                            <div id="password-match-message" class="validation-message"></div>
                            <form:errors path="repeatPassword" cssClass="error-message" />
                        </div>

                        <div class="form-group">
                            <button type="submit" id="changePasswordButton" class="btn btn-primary btn-block">
                                <span>
                                    <i class="fas fa-lock"></i>
                                    <spring:message code="change.password.submit" />
                                </span>
                            </button>
                        </div>
                    </form:form>

                    <div class="form-links">
                        <a href="${pageContext.request.contextPath}/login" class="link">
                            <i class="fas fa-arrow-left"></i>
                            <spring:message code="change.password.back.to.login" />
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<!-- JavaScript for validation -->
<script>
    document.addEventListener("DOMContentLoaded", function() {
        // Fixed header adjustment
        const fixedHeader = document.querySelector(".main-header");
        const mainContent = document.querySelector("main");

        if (fixedHeader && mainContent) {
            const adjustContentMargin = () => {
                const headerHeight = fixedHeader.offsetHeight;
                mainContent.style.marginTop = (headerHeight * 1.25) + `px`;
            };

            // Adjust on page load
            adjustContentMargin();

            // Adjust on window resize
            window.addEventListener("resize", adjustContentMargin);
        }

        // Set up messages object for internationalization
        window.messages = {
            passwordWeak: '<spring:message code="password.weak" />',
            passwordMedium: '<spring:message code="password.medium" />',
            passwordStrong: '<spring:message code="password.strong" />',
            passwordVeryStrong: '<spring:message code="password.very.strong" />',
            passwordReqLength: '<spring:message code="password.req.length" />',
            passwordReqUppercase: '<spring:message code="password.req.uppercase" />',
            passwordReqLowercase: '<spring:message code="password.req.lowercase" />',
            passwordReqNumber: '<spring:message code="password.req.number" />',
            passwordReqSpecial: '<spring:message code="password.req.special" />',
            passwordInvalid: '<spring:message code="password.invalid" />',
            passwordsDoNotMatch: '<spring:message code="passwords.do.not.match" />',
            fieldRequired: '<spring:message code="field.required" />'
        };

        // Password toggle visibility
        const togglePasswordButtons = document.querySelectorAll('.toggle-password');
        togglePasswordButtons.forEach(button => {
            button.addEventListener('click', function() {
                const passwordInput = this.previousElementSibling;
                const icon = this.querySelector('i');

                if (passwordInput.type === 'password') {
                    passwordInput.type = 'text';
                    icon.classList.remove('fa-eye');
                    icon.classList.add('fa-eye-slash');
                } else {
                    passwordInput.type = 'password';
                    icon.classList.remove('fa-eye-slash');
                    icon.classList.add('fa-eye');
                }
            });
        });

        // Password validation
        const passwordField = document.getElementById("password");
        const repeatPasswordField = document.getElementById("repeatPassword");
        const passwordLengthMessage = document.getElementById("password-length-message");
        const passwordMatchMessage = document.getElementById("password-match-message");
        const changePasswordButton = document.getElementById("changePasswordButton");

        // Create requirements list on page load
        createRequirementsList();

        if (passwordField) {
            passwordField.addEventListener("input", function() {
                checkPasswordStrength();
                validatePasswordRequirements(this.value);
                checkPasswordMatch();
                updateButtonState();
            });
        }

        if (repeatPasswordField) {
            repeatPasswordField.addEventListener("input", function() {
                checkPasswordMatch();
                updateButtonState();
            });
        }

        function createRequirementsList() {
            const passwordContainer = document.getElementById("pass-validations");
            if (!passwordContainer) return;

            const requirementsList = document.createElement("ul");
            requirementsList.id = "password-requirements";
            requirementsList.className = "requirements-list";

            // Add after password strength meter
            const strengthMeter = passwordContainer.querySelector(".password-strength");
            if (strengthMeter) {
                passwordContainer.insertBefore(requirementsList, strengthMeter.nextSibling);
            } else {
                passwordContainer.appendChild(requirementsList);
            }

            // Create requirement items
            const requirements = [
                { id: "length", text: window.messages?.passwordReqLength || "At least 8 characters" },
                { id: "uppercase", text: window.messages?.passwordReqUppercase || "At least one uppercase letter" },
                { id: "lowercase", text: window.messages?.passwordReqLowercase || "At least one lowercase letter" },
                { id: "number", text: window.messages?.passwordReqNumber || "At least one number" },
                { id: "special", text: window.messages?.passwordReqSpecial || "At least one special character" },
            ];

            requirements.forEach((req) => {
                const item = document.createElement("li");
                item.id = `req-${req.id}`;
                item.className = "requirement-item";

                // Create a span for the check icon
                const checkIcon = document.createElement("span");
                checkIcon.className = "req-icon";
                checkIcon.innerHTML = '<i class="fas fa-check-circle"></i>';

                // Create a span for the text
                const textSpan = document.createElement("span");
                textSpan.className = "req-text";
                textSpan.textContent = req.text;

                // Append both to the list item
                item.appendChild(checkIcon);
                item.appendChild(textSpan);

                requirementsList.appendChild(item);
            });
        }

        function checkPasswordStrength() {
            const password = passwordField?.value || "";
            const strengthMeterFill = document.querySelector(".strength-meter-fill");
            const strengthText = document.querySelector(".strength-text");
            const passwordStrength = document.querySelector(".password-strength");

            // Exit if elements don't exist
            if (!passwordStrength || !strengthText || !strengthMeterFill) return;

            // Remove all classes
            passwordStrength.className = "password-strength";

            if (!password) {
                strengthText.textContent = "";
                strengthMeterFill.style.width = "0";
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
            let strengthClass = "";
            let strengthLabel = "";
            let strengthIcon = "";
            let strengthPercentage = 0;

            if (strength < 3) {
                strengthClass = "strength-weak";
                strengthLabel = window.messages?.passwordWeak || "Weak";
                strengthIcon = '<i class="fas fa-exclamation-circle"></i>';
                strengthPercentage = 25;
            } else if (strength < 4) {
                strengthClass = "strength-medium";
                strengthLabel = window.messages?.passwordMedium || "Medium";
                strengthIcon = '<i class="fas fa-info-circle"></i>';
                strengthPercentage = 50;
            } else if (strength < 6) {
                strengthClass = "strength-strong";
                strengthLabel = window.messages?.passwordStrong || "Strong";
                strengthIcon = '<i class="fas fa-check-circle"></i>';
                strengthPercentage = 75;
            } else {
                strengthClass = "strength-very-strong";
                strengthLabel = window.messages?.passwordVeryStrong || "Very Strong";
                strengthIcon = '<i class="fas fa-shield-alt"></i>';
                strengthPercentage = 100;
            }

            passwordStrength.classList.add(strengthClass);
            strengthText.innerHTML = strengthIcon + " " + strengthLabel;
            strengthMeterFill.style.width = strengthPercentage + "%";
        }

        function validatePasswordRequirements(password) {
            // Validate each requirement
            const lengthReq = document.getElementById("req-length");
            const uppercaseReq = document.getElementById("req-uppercase");
            const lowercaseReq = document.getElementById("req-lowercase");
            const numberReq = document.getElementById("req-number");
            const specialReq = document.getElementById("req-special");

            // Check requirements
            const hasLength = password && password.length >= 8;
            const hasUppercase = password && /[A-Z]/.test(password);
            const hasLowercase = password && /[a-z]/.test(password);
            const hasNumber = password && /[0-9]/.test(password);
            const hasSpecial = password && /[^A-Za-z0-9]/.test(password);

            // Update UI - with null checks
            if (lengthReq) updateRequirement(lengthReq, hasLength);
            if (uppercaseReq) updateRequirement(uppercaseReq, hasUppercase);
            if (lowercaseReq) updateRequirement(lowercaseReq, hasLowercase);
            if (numberReq) updateRequirement(numberReq, hasNumber);
            if (specialReq) updateRequirement(specialReq, hasSpecial);

            if (password) {
                if (hasLength && hasUppercase && hasLowercase && hasNumber && hasSpecial) {
                    setFieldValid(passwordField, passwordLengthMessage);
                    return true;
                } else {
                    setFieldError(
                        passwordField,
                        passwordLengthMessage,
                        window.messages?.passwordInvalid || "Password does not meet requirements"
                    );
                    return false;
                }
            } else {
                clearFieldValidation(passwordField, passwordLengthMessage);
                return true;
            }
        }

        function updateRequirement(element, isValid) {
            if (!element) return;

            if (isValid) {
                element.classList.add("valid");
                element.classList.remove("invalid");
            } else {
                element.classList.add("invalid");
                element.classList.remove("valid");
            }
        }

        function checkPasswordMatch() {
            const password = passwordField?.value || "";
            const repeatPassword = repeatPasswordField?.value || "";

            if (!repeatPasswordField) return false;

            if (!repeatPassword && repeatPasswordField.hasAttribute("required")) {
                setFieldError(repeatPasswordField, passwordMatchMessage, window.messages?.fieldRequired || "Confirm password is required");
                return false;
            } else if (password && repeatPassword) {
                if (password !== repeatPassword) {
                    setFieldError(repeatPasswordField, passwordMatchMessage, window.messages?.passwordsDoNotMatch || "Passwords do not match");
                    return false;
                } else {
                    setFieldValid(repeatPasswordField, passwordMatchMessage);
                    return true;
                }
            } else {
                clearFieldValidation(repeatPasswordField, passwordMatchMessage);
                return true;
            }
        }

        function setFieldError(field, errorElement, message) {
            if (!field) return;

            field.classList.add("error");
            field.classList.remove("valid");

            if (errorElement) {
                errorElement.textContent = message;
                errorElement.classList.add("visible");
            }
        }

        function setFieldValid(field, errorElement) {
            if (!field) return;

            field.classList.remove("error");
            field.classList.add("valid");

            if (errorElement) {
                errorElement.classList.remove("visible");
            }
        }

        function clearFieldValidation(field, errorElement) {
            if (!field) return;

            field.classList.remove("error");
            field.classList.remove("valid");

            if (errorElement) {
                errorElement.classList.remove("visible");
            }
        }

        function updateButtonState() {
            if (!changePasswordButton) return;

            const isPasswordValid = passwordField && validatePasswordRequirements(passwordField.value);
            const isPasswordMatch = checkPasswordMatch();

            changePasswordButton.disabled = !isPasswordValid || !isPasswordMatch;

            if (changePasswordButton.disabled) {
                changePasswordButton.classList.add("disabled");
            } else {
                changePasswordButton.classList.remove("disabled");
            }
        }

        // Initial validation
        if (passwordField) {
            checkPasswordStrength();
            validatePasswordRequirements(passwordField.value);
        }

        if (repeatPasswordField) {
            checkPasswordMatch();
        }

        updateButtonState();
    });
</script>
</body>
</html>