<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<spring:message code="placeholder.name" var="namePlaceholder"/>
<spring:message code="placeholder.lastname" var="lastNamePlaceholder"/>
<spring:message code="placeholder.email" var="emailPlaceholder"/>
<spring:message code="placeholder.password" var="passwordPlaceholder"/>


<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="icon" type="image/png"
          href="https://upload.wikimedia.org/wikipedia/commons/thumb/a/aa/V-logo.svg/2048px-V-logo.svg.png"/>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="register.title"/></title>
    <link rel="stylesheet" href="<c:url value='/css/register-patient.css' />"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <script src="<c:url value='/js/register-patient.js' />" defer></script>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/components/header.jsp"/>

<div class="page-container">
    <div class="register-container">
        <div class="register-header">
            <h1 class="register-title"><spring:message code="register.title"/></h1>
            <p class="register-subtitle"><spring:message code="register.subtitle"/></p>
        </div>
        <c:choose>
        <c:when test="${param.recover eq 'sent'}">
            <div class="status-message success-message">
                <div class="status-icon">
                    <i class="fas fa-check-circle"></i>
                </div>
                <div class="status-content">
                    <h3><spring:message code="verification.email.sent.title"/></h3>
                    <p><spring:message code="verification.email.sent.message"/></p>
                </div>
            </div>

            <div class="form-links">
                <a href="${pageContext.request.contextPath}/login" class="link">
                    <i class="fas fa-arrow-left"></i>
                    <spring:message code="recover.password.back.to.login"/>
                </a>
            </div>
        </c:when>
        <c:otherwise>

        <div class="register-content">
            <form:form modelAttribute="patientForm" method="post"
                       action="${pageContext.request.contextPath}/register-patient" enctype="multipart/form-data"
                       id="patientRegistrationForm">
                <!-- Hidden fields for password values -->
                <input type="hidden" id="passwordValue" name="passwordValue" value="${patientForm.password}"/>
                <input type="hidden" id="repeatPasswordValue" name="repeatPasswordValue"
                       value="${patientForm.repeatPassword}"/>

                <!-- Personal Information Section -->
                <div class="form-section">
                    <div class="section-header">
                        <h2 class="section-title"><spring:message code="register.personalInfo"/></h2>
                    </div>

                    <div class="form-grid">
                        <div class="form-field">
                            <label for="name" class="field-label">
                                <spring:message code="register.firstName"/>
                                <span class="required-mark">*</span>
                            </label>
                            <div class="input-container">
                                <form:input path="name" id="name" required="true"
                                            class="input-field ${status.error ? 'input-error' : ''}"
                                            placeholder="${namePlaceholder}"
                                />
                            </div>
                            <div id="name-validation-message" class="error-text" style="display: none;"></div>
                            <div class="server-error-name">
                                <form:errors path="name" class="error-text"/>
                            </div>
                        </div>

                        <div class="form-field">
                            <label for="lastName" class="field-label">
                                <spring:message code="register.lastName"/>
                                <span class="required-mark">*</span>
                            </label>
                            <div class="input-container">
                                <form:input path="lastName" id="lastName" required="true"
                                            class="input-field ${status.error ? 'input-error' : ''}"
                                            placeholder="${lastNamePlaceholder}"
                                />
                            </div>
                            <div id="lastName-validation-message" class="error-text" style="display: none;"></div>
                            <div class="server-error-lastName">
                                <form:errors path="lastName" class="error-text"/>
                            </div>
                        </div>

                        <div class="form-field">
                            <label for="email" class="field-label">
                                <spring:message code="register.email"/>
                                <span class="required-mark">*</span>
                            </label>
                            <div class="input-container">
                                <form:input path="email" id="email" required="true"
                                            class="input-field ${status.error ? 'input-error' : ''}"
                                            placeholder="${emailPlaceholder}"
                                />
                            </div>
                            <div id="email-validation-message" class="error-text" style="display: none;"></div>
                            <div class="server-error-email">
                                <form:errors path="email" class="error-text"/>
                            </div>
                        </div>

                        <div class="form-field">
                            <label for="phone" class="field-label">
                                <spring:message code="register.phone"/>
                                <span class="required-mark">*</span>
                            </label>
                            <div class="input-container">
                                <form:input path="phone" id="phone" required="true"
                                            class="input-field ${status.error ? 'input-error' : ''}"
                                            placeholder="+1 (123) 456-7890"
                                />
                            </div>
                            <div id="phone-validation-message" class="error-text" style="display: none;"></div>
                            <div class="server-error-phone">
                                <form:errors path="phone" class="error-text"/>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Security Section -->
                <div class="form-section">
                    <div class="section-header">
                        <h2 class="section-title"><spring:message code="register.security"/></h2>
                    </div>

                    <div class="form-grid">
                        <div class="form-field">
                            <label for="password" class="field-label">
                                <spring:message code="register.password"/>
                                <span class="required-mark">*</span>
                                <div class="tooltip">
                                    <i class="fas fa-info-circle tooltip-icon"></i>
                                    <span class="tooltip-content"><spring:message
                                            code="register.passwordTooltip"/></span>
                                </div>
                            </label>
                            <div class="input-container">
                                <form:password path="password" id="password" required="true"
                                               class="input-field ${status.error ? 'input-error' : ''}"
                                               placeholder="${passwordPlaceholder}"
                                />
                            </div>
                            <div class="password-strength">
                                <div class="strength-bar">
                                    <div class="strength-fill"></div>
                                </div>
                                <span class="strength-text"></span>
                            </div>
                            <div id="password-length-message" class="error-text" style="display: none;"></div>
                            <div class="server-error-password">
                                <form:errors path="password" class="error-text"/>
                            </div>
                        </div>

                        <div class="form-field">
                            <label for="repeatPassword" class="field-label">
                                <spring:message code="register.confirmPassword"/>
                                <span class="required-mark">*</span>
                            </label>
                            <div class="input-container">
                                <form:password path="repeatPassword" id="repeatPassword" required="true"
                                               class="input-field ${status.error ? 'input-error' : ''}"
                                               placeholder="${passwordPlaceholder}"
                                />
                            </div>
                            <div id="password-match-message" class="error-text" style="display: none;"></div>
                            <div class="server-error-repeatPassword">
                                <form:errors path="repeatPassword" class="error-text"/>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Coverage Section -->
                <div class="form-section">
                    <div class="section-header">
                        <h2 class="section-title"><spring:message code="register.coverage"/></h2>
                    </div>

                    <div class="coverage-grid">
                        <c:forEach items="${coverageList}" var="coverage" varStatus="status">
                            <div class="coverage-item">
                                <form:radiobutton path="coverage" id="coverage-${coverage.id}" value="${coverage.id}"
                                                  class="coverage-input"/>
                                <label for="coverage-${coverage.id}" class="coverage-label">
                                    <span class="radio-indicator"></span>
                                    <span class="coverage-text"><c:out value="${coverage.name}"/></span>
                                </label>
                            </div>
                        </c:forEach>
                    </div>
                    <div class="server-error-coverage">
                        <form:errors path="coverage" class="error-text"/>
                    </div>
                </div>

                <!-- Terms and Conditions -->
                <div class="terms-section">
                    <div class="checkbox-container">
                        <input type="checkbox" id="terms" class="checkbox-input" required/>
                        <label for="terms" class="checkbox-label">
                            <span class="checkbox-indicator"></span>
                            <span class="checkbox-text">
                                    <spring:message code="register.agreeTerms"/>
                                    <a href="#" class="terms-link"><spring:message code="register.termsLink"/></a>
                                </span>
                        </label>
                    </div>
                </div>

                <!-- Form Actions -->
                <div class="form-actions">
                    <button type="submit" id="registerButton" class="submit-button"
                            onclick="this.disabled = true ; this.form.submit();">
                        <i class="fas fa-user-plus button-icon"></i>
                        <span class="button-text"><spring:message code="register.button"/></span>
                    </button>

                    <div class="login-redirect">
                        <spring:message code="register.alreadyHaveAccount"/>
                        <a href="<c:url value='/login' />" class="login-link"><spring:message
                                code="register.loginLink"/></a>
                    </div>
                </div>
            </form:form>
            </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<script>
    // Create a global object to store message translations
    window.messages = {
        dayOfWeek: '<spring:message code="register.dayOfWeek" javaScriptEscape="true" />',
        startTime: '<spring:message code="register.startTime" javaScriptEscape="true" />',
        endTime: '<spring:message code="register.endTime" javaScriptEscape="true" />',
        monday: '<spring:message code="register.monday" javaScriptEscape="true" />',
        tuesday: '<spring:message code="register.tuesday" javaScriptEscape="true" />',
        wednesday: '<spring:message code="register.wednesday" javaScriptEscape="true" />',
        thursday: '<spring:message code="register.thursday" javaScriptEscape="true" />',
        friday: '<spring:message code="register.friday" javaScriptEscape="true" />',
        saturday: '<spring:message code="register.saturday" javaScriptEscape="true" />',
        sunday: '<spring:message code="register.sunday" javaScriptEscape="true" />',
        timeSlotInvalidTime: '<spring:message code="register.timeSlotInvalidTime" javaScriptEscape="true" />',
        timeSlotOverlap: '<spring:message code="register.timeSlotOverlap" javaScriptEscape="true" />',
        timeSlotRequired: '<spring:message code="register.timeSlotRequired" javaScriptEscape="true" />',
        passwordWeak: '<spring:message code="register.passwordWeak" javaScriptEscape="true" />',
        passwordMedium: '<spring:message code="register.passwordMedium" javaScriptEscape="true" />',
        passwordStrong: '<spring:message code="register.passwordStrong" javaScriptEscape="true" />',
        passwordVeryStrong: '<spring:message code="register.passwordVeryStrong" javaScriptEscape="true" />',
        passwordsDoNotMatch: '<spring:message code="register.passwordsDoNotMatch" javaScriptEscape="true" />',
        passwordLength: '<spring:message code="register.passwordLength" javaScriptEscape="true" />',
        emailInvalid: '<spring:message code="appointment.validation.email" javaScriptEscape="true" />',
        phoneInvalid: '<spring:message code="appointment.validation.phone" javaScriptEscape="true" />',
        fieldRequired: '<spring:message code="register.fieldRequired" javaScriptEscape="true" />',
        nameTooShort: '<spring:message code="register.nameTooShort" javaScriptEscape="true" />',
        nameInvalid: '<spring:message code="register.nameInvalid" javaScriptEscape="true" />',
        passwordInvalid: '<spring:message code="register.passwordInvalid" javaScriptEscape="true" />'
    };

    const fixedHeader = document.querySelector(".main-header");
    const mainContent = document.querySelector(".page-container");

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

    // Store existing availability slots data if present
    <c:if test="${not empty registerForm.availabilitySlots}">
    window.existingSlots = [
        <c:forEach items="${registerForm.availabilitySlots}" var="slot" varStatus="status">
        {
            index: ${status.index},
            day: ${slot.dayOfWeek},
            startTime: '${slot.startTime}',
            endTime: '${slot.endTime}'
        }<c:if test="${!status.last}">, </c:if>
        </c:forEach>
    ];
    </c:if>

    // Check if there are any form errors and determine which section to show
    <c:if test="${status.error}">
    window.hasErrors = true;
    <c:if test="${not empty status.errorMessages['name'] || not empty status.errorMessages['lastName'] ||
                not empty status.errorMessages['email'] || not empty status.errorMessages['phone'] ||
                not empty status.errorMessages['password'] || not empty status.errorMessages['repeatPassword'] ||
                not empty status.errorMessages['image']}">
    window.errorSection = 1;
    </c:if>
    <c:if test="${not empty status.errorMessages['specialties'] || not empty status.errorMessages['coverages']}">
    window.errorSection = 2;
    </c:if>
    <c:if test="${not empty status.errorMessages['availabilitySlots']}">
    window.errorSection = 3;
    </c:if>
    </c:if>
</script>
</body>
</html>
