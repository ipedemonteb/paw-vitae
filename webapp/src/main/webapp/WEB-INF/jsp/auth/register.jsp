<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="comp" tagdir="/WEB-INF/tags/components" %>

<layout:page title="register.doctor.title">
    <!-- Include CSS and JS files -->
    <link rel="stylesheet" href="<c:url value='/css/components/register-doctor.css'/> ">
    <script src="<c:url value='/js/register.js' />" defer></script>

    <div class="register-container">
        <div class="register-card">
            <div class="card-header">
                <h1 class="card-title-register"><spring:message code="register.doctor.title" /></h1>
                <p class="card-subtitle-register"><spring:message code="register.doctor.subtitle" /></p>
            </div>

            <div class="card-body">
                <!-- Progress indicator -->
                <div class="form-progress">
                    <div class="progress-step active" data-step="1">
                        <div class="step-indicator">1</div>
                        <div class="step-label"><spring:message code="register.step.personal" /></div>
                    </div>
                    <div class="progress-step" data-step="2">
                        <div class="step-indicator">2</div>
                        <div class="step-label"><spring:message code="register.step.professional" /></div>
                    </div>
                    <div class="progress-step" data-step="3">
                        <div class="step-indicator">3</div>
                        <div class="step-label"><spring:message code="register.step.availability" /></div>
                    </div>
                </div>

                <form:form modelAttribute="registerForm" method="post" action="${pageContext.request.contextPath}/register" enctype="multipart/form-data" cssClass="register-form" onsubmit="return validateForm()">
                    <input type="hidden" id="passwordValue" name="passwordValue" value="${registerForm.password}" />
                    <input type="hidden" id="repeatPasswordValue" name="repeatPasswordValue" value="${registerForm.repeatPassword}" />
                    <!-- Section 1: Personal Information -->
                    <div class="form-section active" data-section="1">
                        <h2><spring:message code="register.personalInfo" /></h2>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="name" class="form-label required-field"><spring:message code="register.firstName" /></label>
                                <form:input required="true" path="name" id="name" cssClass="form-control ${status.error ? 'error' : ''}" />
                                <form:errors path="name" cssClass="error-message" />
                            </div>

                            <div class="form-group">
                                <label for="lastName" class="form-label required-field"><spring:message code="register.lastName" /></label>
                                <form:input required="true" path="lastName" id="lastName" cssClass="form-control ${status.error ? 'error' : ''}" />
                                <form:errors path="lastName" cssClass="error-message" />
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="email" class="form-label required-field"><spring:message code="register.email" /></label>
                                <form:input required="true" path="email" id="email" cssClass="form-control ${status.error ? 'error' : ''}" placeholder="email@vitae.com" />
                                <div id="email-validation-message" class="error-message" style="display: none;"></div>
                                <form:errors path="email" cssClass="error-message" />
                            </div>

                            <div class="form-group">
                                <label for="phone" class="form-label required-field"><spring:message code="register.phone" /></label>
                                <form:input required="true" path="phone" id="phone" cssClass="form-control ${status.error ? 'error' : ''}" placeholder="+1 (123) 456-7890" />
                                <div id="phone-validation-message" class="error-message" style="display: none;"></div>
                                <form:errors path="phone" cssClass="error-message" />
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label for="password" class="form-label required-field">
                                    <spring:message code="register.password" />
                                    <span class="tooltip">
                                        <span class="tooltip-icon">?</span>
                                        <span class="tooltip-text"><spring:message code="register.passwordTooltip" /></span>
                                     </span>
                                </label>
                                <form:password required="true" path="password" id="password" cssClass="form-control ${status.error ? 'error' : ''}" />
                                <div class="password-strength">
                                    <div class="strength-meter">
                                        <div class="strength-meter-fill"></div>
                                    </div>
                                    <div class="strength-text"></div>
                                </div>
                                <div id="password-length-message" class="error-message" style="display: none;"></div>
                                <form:errors path="password" cssClass="error-message" />
                            </div>

                            <div class="form-group">
                                <label for="repeatPassword" class="form-label required-field"><spring:message code="register.confirmPassword" /></label>
                                <form:password required="true" path="repeatPassword" id="repeatPassword" cssClass="form-control ${status.error ? 'error' : ''}" />
                                <div id="password-match-message" class="error-message" style="display: none;"></div>
                                <form:errors path="repeatPassword" cssClass="error-message" />
                            </div>
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
                            <div id="image-error" class="error-message" style="display: none; margin-bottom: 10px;"></div>
                            <form:errors path="image" cssClass="error-message" />
                        </div>

                        <div class="form-navigation">
                            <div></div> <!-- Empty div for spacing -->
                            <button type="button" class="btn-next" onclick="nextSection(1)">
                                <spring:message code="register.next" /> →
                            </button>
                        </div>
                    </div>

                    <!-- Section 2: Professional Information -->
                    <div class="form-section" data-section="2">
                        <h2><spring:message code="register.professionalInfo" /></h2>

                        <div class="form-group">
                            <label for="specialties" class="form-label required-field"><spring:message code="register.specialties" /></label>
                            <div class="multi-select-container" id="specialties-container">
                                <div class="custom-multi-select" id="specialties-options">
                                    <c:forEach items="${specialtyList}" var="specialty">
                                        <div class="custom-multi-select-option" data-value="${specialty.id}" onclick="toggleOption(this, 'specialties')">
                                            <div class="option-checkbox"></div>
                                            <div class="option-text">
                                                <spring:message code="${specialty.key}"/>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                                <div class="selected-options" id="specialties-selected"></div>
                                <form:select path="specialties" id="specialties" cssClass="form-control" multiple="true" style="display: none;">
                                    <c:forEach items="${specialtyList}" var="specialty">
                                        <form:option value="${specialty.id}">
                                            <spring:message code="${specialty.key}"/>
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
                                <form:select path="coverages" id="coverages" cssClass="form-control" multiple="true" style="display: none;">
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

                        <div class="form-navigation">
                            <button type="button" class="btn-prev" onclick="prevSection(2)">
                                ← <spring:message code="register.previous" />
                            </button>
                            <button type="button" class="btn-next" onclick="nextSection(2)">
                                <spring:message code="register.next" /> →
                            </button>
                        </div>
                    </div>

                    <!-- Section 3: Availability -->
                    <div class="form-section" data-section="3">
                        <h2><spring:message code="register.availability" /></h2>

                        <div class="form-group">
                            <label class="form-label required-field">
                                <spring:message code="register.availabilitySlots" />
                                <span class="tooltip">
                                    <span class="tooltip-icon">?</span>
                                    <span class="tooltip-text"><spring:message code="register.timeSlotHelp" /></span>
                                </span>
                            </label>
                            <div id="timeslots-container" class="timeslots-container">
                                <div id="no-slots-message" class="no-slots-message">
                                    <p><spring:message code="register.noTimeSlots" /></p>
                                </div>

                                <div id="time-slot-inputs">
                                    <!-- Time slot inputs will be added here -->
                                    <c:if test="${not empty registerForm.availabilitySlots}">
                                        <c:forEach items="${registerForm.availabilitySlots}" var="slot" varStatus="status">
                                            <div class="time-slot-row" id="slot-row-${status.index}" data-index="${status.index}">
                                                <!-- This will be populated by JavaScript on page load -->
                                            </div>
                                        </c:forEach>
                                    </c:if>
                                </div>

                                <div id="time-slot-error" class="error-message" style="display: none; margin-bottom: 10px;"></div>

                                <button type="button" class="btn-add-slot" id="add-slot-btn">
                                    + <spring:message code="register.addTimeSlot" />
                                </button>
                            </div>
                            <form:errors path="availabilitySlots" cssClass="error-message" />
                        </div>

                        <div class="form-group terms-checkbox">
                            <input type="checkbox" id="terms" class="custom-checkbox" required />
                            <label for="terms" class="checkbox-label required-field">
                                <spring:message code="register.agreeTerms" />
                                <a class="terms-link"><spring:message code="register.termsLink" /></a>
                            </label>
                        </div>

                        <div class="form-navigation">
                            <button type="button" class="btn-prev" onclick="prevSection(3)">
                                ← <spring:message code="register.previous" />
                            </button>
                            <button type="submit" class="btn-submit-doctor" id="registerButton">
                                <spring:message code="register.button" />
                            </button>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </div>

    <!-- Store message translations as JavaScript variables for use in JS file -->
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
            fileSizeError: '<spring:message code="fileSizeError" javaScriptEscape="true" />',
        };

        // Store existing availability slots data if present
        <c:if test="${not empty registerForm.availabilitySlots}">
        window.existingSlots = [
            <c:forEach items="${registerForm.availabilitySlots}" var="slot" varStatus="status">
            {
                index: ${status.index},
                day: ${slot.dayOfWeek},
                startTime: '${slot.startTime}',
                endTime: '${slot.endTime}'
            }<c:if test="${!status.last}">,</c:if>
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
</layout:page>