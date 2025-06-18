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
<jsp:include page="/WEB-INF/jsp/components/header.jsp" />

<div class="main-content">
    <div class="container">
        <div class="change-password-container">
            <div class="card">
                <div class="card-header">
                    <h1 class="card-title"><spring:message code="change.password.title" /></h1>
                    <p class="card-subtitle"><spring:message code="change.password.subtitle" /></p>
                </div>

                <div class="card-body">
                    <form:form modelAttribute="ChangePasswordForm" method="post" action="${pageContext.request.contextPath}/change-password?token=${token}" class="change-password-form">

                        <div class="form-group">
                            <label for="password" class="form-label"><spring:message code="change.password.new.password" /></label>
                            <div class="password-input-wrapper">
                                <form:password path="password" id="password" class="form-control" required="true" />
                                <div class="validation-icon valid">
                                    <i class="fas fa-check-circle"></i>
                                </div>
                                <div class="validation-icon error">
                                    <i class="fas fa-exclamation-circle"></i>
                                </div>
                                <button type="button" class="toggle-password" aria-label="Toggle password visibility">
                                    <i class="fas fa-eye"></i>
                                </button>
                            </div>
                            <div id="pass-validations" class="password-validations">
                                <div class="password-strength">
                                    <div class="strength-meter">
                                        <div class="strength-meter-fill"></div>
                                    </div>
                                    <span class="strength-text"></span>
                                </div>
                            </div>
                            <div id="password-length-message" class="validation-message"></div>
                            <form:errors path="password" cssClass="error-message" />
                        </div>
                        <div class="form-group">
                            <label for="repeatPassword" class="form-label"><spring:message code="change.password.confirm.password" /></label>
                            <div class="password-input-wrapper">
                                <form:password path="repeatPassword" id="repeatPassword" class="form-control" required="true" />
                                <div class="validation-icon valid">
                                    <i class="fas fa-check-circle"></i>
                                </div>
                                <div class="validation-icon error">
                                    <i class="fas fa-exclamation-circle"></i>
                                </div>
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
</div>

<script>
    document.addEventListener("DOMContentLoaded", function() {

        window.messages = {
            passwordWeak: '<spring:message code="password.weak" javaScriptEscape="true" />',
            passwordMedium: '<spring:message code="password.medium" javaScriptEscape="true" />',
            passwordStrong: '<spring:message code="password.strong" javaScriptEscape="true" />',
            passwordVeryStrong: '<spring:message code="password.very.strong" javaScriptEscape="true" />',
            passwordInvalid: '<spring:message code="password.invalid" javaScriptEscape="true" />',
            passwordsDoNotMatch: '<spring:message code="passwords.do.not.match" javaScriptEscape="true" />',
            fieldRequired: '<spring:message code="field.required" javaScriptEscape="true" />'
        };

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

        const passwordField = document.getElementById("password");
        const repeatPasswordField = document.getElementById("repeatPassword");
        const passwordLengthMessage = document.getElementById("password-length-message");
        const passwordMatchMessage = document.getElementById("password-match-message");
        const changePasswordButton = document.getElementById("changePasswordButton");

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

        function checkPasswordStrength() {
            const password = passwordField?.value || "";
            const strengthMeterFill = document.querySelector(".strength-meter-fill");
            const strengthText = document.querySelector(".strength-text");
            const passwordStrength = document.querySelector(".password-strength");

            if (!passwordStrength || !strengthText || !strengthMeterFill) return;

            passwordStrength.className = "password-strength";

            if (!password) {
                strengthText.textContent = "";
                strengthMeterFill.style.width = "0";
                return;
            }

            let strength = 0;

            if (password.length >= 8) strength += 1
            if (password.length >= 12) strength += 1

            if (/[A-Z]/.test(password)) strength += 1
            if (/[0-9]/.test(password)) strength += 1
            if (/[^A-Za-z0-9]/.test(password)) strength += 1

            let strengthClass = "";
            let strengthLabel = "";
            let strengthIcon = "";
            let strengthPercentage = 0;

            if (strength < 3) {
                strengthClass = "strength-weak";
                strengthLabel = window.messages?.passwordWeak || "Weak";

                strengthPercentage = 30;
            } else if (strength < 5) {
                strengthClass = "strength-medium";
                strengthLabel = window.messages?.passwordMedium || "Medium";
                strengthPercentage = 60;
            } else {
                strengthClass = "strength-strong";
                strengthLabel = window.messages?.passwordStrong || "Strong";
                strengthPercentage = 100;
            }

            passwordStrength.classList.add(strengthClass);
            strengthText.innerHTML = strengthLabel;
            strengthMeterFill.style.width = strengthPercentage + "%";
        }

        function validatePasswordRequirements(password) {
            const hasLength = password && password.length >= 8;
            const hasUppercase = /[A-Z]/.test(password);

            if (password) {
                if (hasLength && hasUppercase ) {
                    setFieldValid(passwordField, passwordLengthMessage);
                    return true;
                } else {
                    let errorMessage = window.messages.passwordInvalid;
                    setFieldError(
                        passwordField,
                        passwordLengthMessage,
                        window.messages?.passwordInvalid || errorMessage
                    );
                    return false;
                }
            } else {
                clearFieldValidation(passwordField, passwordLengthMessage);
                return true;
            }
        }

        function updateRequirement(reqId, isValid) {
            const reqElement = document.getElementById(reqId);
            if (!reqElement) return;

            if (isValid) {
                reqElement.classList.add("valid");
            } else {
                reqElement.classList.remove("valid");
            }
        }

        function checkPasswordMatch() {
            const password = passwordField?.value || "";
            const repeatPassword = repeatPasswordField?.value || "";

            if(!repeatPassword && !password) {
                clearFieldValidation(repeatPasswordField, passwordMatchMessage);
                return true;
            }

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

            const wrapper = field.closest(".password-input-wrapper")
            if (wrapper) {
                const validIcon = wrapper.querySelector(".validation-icon.valid")
                const errorIcon = wrapper.querySelector(".validation-icon.error")

                if (validIcon) validIcon.style.opacity = "0"
                if (errorIcon) errorIcon.style.opacity = "1"
            }

            if (errorElement) {
                errorElement.textContent = message;
                errorElement.classList.add("visible");
            }
        }

        function setFieldValid(field, errorElement) {
            if (!field) return;

            field.classList.remove("error");
            field.classList.add("valid");
            const wrapper = field.closest(".password-input-wrapper")

            if(wrapper) {
                const validIcon = wrapper.querySelector(".validation-icon.valid")
                const errorIcon = wrapper.querySelector(".validation-icon.error")

                if (validIcon) validIcon.style.opacity = "1"
                if (errorIcon) errorIcon.style.opacity = "0"            }
            if (errorElement) {
                errorElement.classList.remove("visible");
            }
        }

        function clearFieldValidation(field, errorElement) {
            if (!field) return;

            const wrapper = field.closest(".password-input-wrapper")
            if (wrapper) {
                const validIcon = wrapper.querySelector(".validation-icon.valid")
                const errorIcon = wrapper.querySelector(".validation-icon.error")

                if (validIcon) validIcon.style.opacity = "0"
                if (errorIcon) errorIcon.style.opacity = "0"
            }

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