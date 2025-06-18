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
    <title><spring:message code="register.doctor.title"/></title>
    <link rel="stylesheet" href="<c:url value='/css/register.css'/> ">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap"
          rel="stylesheet">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/components/header.jsp"/>

<main class="register-container">
    <div class="register-card">

        <div class="card-header">
            <h1 class="card-title-register"><spring:message code="register.doctor.title"/></h1>
            <p class="card-subtitle-register"><spring:message code="register.doctor.subtitle"/></p>
        </div>

        <div class="card-body">
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
                    <div class="form-progress">
                        <div class="progress-step active" data-step="1">
                            <div class="step-indicator">1</div>
                            <div class="step-label"><spring:message code="register.step.personal"/></div>
                        </div>
                        <div class="progress-step" data-step="2">
                            <div class="step-indicator">2</div>
                            <div class="step-label"><spring:message code="register.step.professional"/></div>
                        </div>
                        <div class="progress-step" data-step="3">
                            <div class="step-indicator">3</div>
                            <div class="step-label"><spring:message code="register.step.availability"/></div>
                        </div>
                    </div>

                    <form:form modelAttribute="registerForm" method="post"
                               action="${pageContext.request.contextPath}/register" enctype="multipart/form-data"
                               cssClass="register-form" onsubmit="return validateForm()">
                        <input type="hidden" id="passwordValue" name="passwordValue" value="${registerForm.password}"/>
                        <input type="hidden" id="repeatPasswordValue" name="repeatPasswordValue"
                               value="${registerForm.repeatPassword}"/>
                        <div class="form-section active" data-section="1">
                            <h2><spring:message code="register.personalInfo"/></h2>

                            <div class="form-row">
                                <div class="form-group">
                                    <label for="name" class="form-label required-field"><spring:message
                                            code="register.firstName"/></label>
                                    <div class="input-wrapper">
                                        <form:input required="true" path="name" id="name"
                                                    cssClass="form-control"
                                                    placeholder="${namePlaceholder}"
                                        />
                                        <div class="validation-icon valid">
                                            <i class="fas fa-check-circle"></i>
                                        </div>
                                        <div class="validation-icon error">
                                            <i class="fas fa-exclamation-circle"></i>
                                        </div>
                                    </div>
                                    <div id="name-validation-message" class="error-message">
                                    </div>
                                    <div class="error-message-server-name">
                                        <form:errors path="name"/>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="lastName" class="form-label required-field"><spring:message
                                            code="register.lastName"/></label>
                                    <div class="input-wrapper">
                                        <form:input required="true" path="lastName" id="lastName"
                                                    cssClass="form-control"
                                                    placeholder="${lastNamePlaceholder}"
                                        />
                                        <div class="validation-icon valid">
                                            <i class="fas fa-check-circle"></i>
                                        </div>
                                        <div class="validation-icon error">
                                            <i class="fas fa-exclamation-circle"></i>
                                        </div>
                                    </div>
                                    <div id="lastName-validation-message" class="error-message">
                                    </div>
                                    <div class="error-message-server-lastName">
                                        <form:errors path="lastName"/>
                                    </div>
                                </div>
                            </div>

                            <div class="form-row">
                                <div class="form-group">
                                    <label for="email" class="form-label required-field"><spring:message
                                            code="register.email"/></label>
                                    <div class="input-wrapper">
                                        <form:input required="true" path="email" id="email"
                                                    cssClass="form-control"
                                                    placeholder="${emailPlaceholder}"/>
                                        <div class="validation-icon valid">
                                            <i class="fas fa-check-circle"></i>
                                        </div>
                                        <div class="validation-icon error">
                                            <i class="fas fa-exclamation-circle"></i>
                                        </div>
                                    </div>
                                    <div id="email-validation-message" class="error-message">
                                    </div>
                                    <div class="error-message-server-email">
                                        <form:errors path="email"/>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="phone" class="form-label required-field"><spring:message
                                            code="register.phone"/></label>
                                    <div class="input-wrapper">
                                        <form:input required="true" path="phone" id="phone"
                                                    cssClass="form-control"
                                                    placeholder="+1 (123) 456-7890"
                                        />
                                        <div class="validation-icon valid">
                                            <i class="fas fa-check-circle"></i>
                                        </div>
                                        <div class="validation-icon error">
                                            <i class="fas fa-exclamation-circle"></i>
                                        </div>
                                    </div>
                                    <div id="phone-validation-message" class="error-message">
                                    </div>
                                    <div class="error-message-server-phone">
                                        <form:errors path="phone"/>
                                    </div>
                                </div>
                            </div>

                            <div class="form-row">
                                <div class="form-group">
                                    <label for="password" class="form-label">
                                        <spring:message code="register.password"/>
                                        <span class="required-mark">*</span>
                                        <div class="tooltip">
                                            <i class="fas fa-info-circle tooltip-icon"></i>
                                            <span class="tooltip-content"><spring:message
                                                    code="register.passwordTooltip"/></span>
                                        </div>
                                    </label>
                                    <div class="input-wrapper">
                                        <form:password required="true" path="password" id="password"
                                                       cssClass="form-control"
                                                       placeholder="${passwordPlaceholder}"
                                        />
                                        <div class="validation-icon valid">
                                            <i class="fas fa-check-circle"></i>
                                        </div>
                                        <div class="validation-icon error">
                                            <i class="fas fa-exclamation-circle"></i>
                                        </div>
                                        <div class="password-toggle" onclick="togglePasswordVisibility('password')">
                                            <i class="fas fa-eye"></i>
                                        </div>
                                    </div>
                                    <div id="pass-validations">

                                    </div>
                                    <div class="password-strength">
                                        <div class="strength-meter">
                                            <div class="strength-meter-fill"></div>
                                        </div>
                                        <div class="strength-text"></div>
                                    </div>
                                    <div id="password-length-message" class="error-message">
                                    </div>
                                    <div class="error-message-server-password">
                                        <form:errors path="password"/>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="repeatPassword" class="form-label required-field"><spring:message
                                            code="register.confirmPassword"/></label>
                                    <div class="input-wrapper">
                                        <form:password required="true" path="repeatPassword" id="repeatPassword"
                                                       cssClass="form-control"
                                                       placeholder="${passwordPlaceholder}"
                                        />
                                        <div class="validation-icon valid">
                                            <i class="fas fa-check-circle"></i>
                                        </div>
                                        <div class="validation-icon error">
                                            <i class="fas fa-exclamation-circle"></i>
                                        </div>
                                        <div class="password-toggle" onclick="togglePasswordVisibility('repeatPassword')">
                                            <i class="fas fa-eye"></i>
                                        </div>
                                    </div>
                                    <div id="password-match-message" class="error-message">
                                    </div>
                                    <div  class="error-message-server-repeatPassword">
                                        <form:errors path="repeatPassword"/>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="form-label"><spring:message code="register.uploadImage"/></label>
                                <div class="file-upload">
                                    <label class="file-upload-label">
                                        <span class="file-upload-text"><spring:message
                                                code="register.uploadImage"/></span>
                                        <form:input type="file" path="image" id="image-upload" accept="image/*"
                                                    cssClass="file-input"/>
                                    </label>
                                </div>
                                <div id="file-name" class="file-name"></div>
                                <div id="image-error" class="error-message" style="display: none;"></div>
                                <div class="error-container">
                                </div>
                                <div class="error-container-server-image">
                                    <form:errors path="image" cssClass="error-message visible"/>
                                </div>
                            </div>

                            <div class="form-navigation">
                                <div></div>
                                <button type="button" class="btn-next" onclick="nextSection(1)">
                                    <spring:message code="register.next"/> <i class="fas fa-arrow-right"></i>
                                </button>
                            </div>
                        </div>

                        <div class="form-section" data-section="2">
                            <h2><spring:message code="register.professionalInfo"/></h2>

                            <div class="form-group">
                                <label for="specialties" class="form-label required-field"><spring:message
                                        code="register.specialties"/></label>
                                <div class="multi-select-container" id="specialties-container">
                                    <div class="custom-multi-select" id="specialties-options">
                                        <c:forEach items="${specialtyList}" var="specialty">
                                            <div class="custom-multi-select-option" data-value="${specialty.id}"
                                                 onclick="toggleOption(this, 'specialties')">
                                                <div class="option-checkbox"></div>
                                                <div class="option-text">
                                                    <spring:message code="${specialty.key}"/>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                    <div class="selected-options" id="specialties-selected"></div>
                                    <form:select path="specialties" id="specialties" cssClass="form-control"
                                                 multiple="true" style="display: none;">
                                        <c:forEach items="${specialtyList}" var="specialty">
                                            <form:option value="${specialty.id}">
                                                <spring:message code="${specialty.key}"/>
                                            </form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                                <small class="form-text text-muted">
                                    <spring:message code="register.selectSpecialties"/>
                                </small>
                                <div class="error-container">
                                </div>
                                <div class="error-container-server-specialties">
                                    <form:errors path="specialties" cssClass="error-message visible"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="coverages" class="form-label required-field"><spring:message
                                        code="register.coverage"/></label>
                                <div class="multi-select-container" id="coverages-container">
                                    <div class="custom-multi-select" id="coverages-options">
                                        <c:forEach items="${coverageList}" var="coverage">
                                            <div class="custom-multi-select-option" data-value="${coverage.id}"
                                                 onclick="toggleOption(this, 'coverages')">
                                                <div class="option-checkbox"></div>
                                                <div class="option-text">
                                                    <c:out value="${coverage.name}"/>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                    <div class="selected-options" id="coverages-selected"></div>
                                    <form:select path="coverages" id="coverages" cssClass="form-control" multiple="true"
                                                 style="display: none;">
                                        <c:forEach items="${coverageList}" var="coverage">
                                            <form:option value="${coverage.id}"><c:out
                                                    value="${coverage.name}"/></form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                                <small class="form-text text-muted">
                                    <spring:message code="register.selectCoverage"/>
                                </small>
                                <div class="error-container">
                                </div>
                                <div class="error-container-server-coverages">
                                    <form:errors path="coverages" cssClass="error-message visible"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="form-label required-field"><spring:message code="register.doctorOffices"/></label>
                                <div id="doctor-offices-container">
                                    <div class="office-entry" data-index="0">
                                        <div class="form-row">
                                            <div class="form-group">
                                                <label for="office-name-0" class="form-label required-field"><spring:message code="register.officeName"/></label>
                                                <div class="input-wrapper">
                                                    <input type="text" id="office-name-0" class="form-control office-name" placeholder="<spring:message code="placeholder.officeName"/>" required/>
                                                    <div class="validation-icon valid">
                                                        <i class="fas fa-check-circle"></i>
                                                    </div>
                                                    <div class="validation-icon error">
                                                        <i class="fas fa-exclamation-circle"></i>
                                                    </div>
                                                </div>
                                                <div id="office-name-0-validation-message" class="error-message"></div>
                                            </div>
                                            <div class="form-group">
                                                <label for="office-neighborhood-search-0" class="form-label required-field"><spring:message code="register.neighborhood"/></label>
                                                <div class="input-wrapper">
                                                    <input type="text" id="office-neighborhood-search-0" class="form-control office-neighborhood-search"
                                                           placeholder="<spring:message code="placeholder.neighborhood"/>" autocomplete="off" required/>
                                                    <input type="hidden" id="office-neighborhood-id-0" class="office-neighborhood-id"/>
                                                    <div class="validation-icon valid">
                                                        <i class="fas fa-check-circle"></i>
                                                    </div>
                                                    <div class="validation-icon error">
                                                        <i class="fas fa-exclamation-circle"></i>
                                                    </div>
                                                </div>
                                                <div id="office-neighborhood-results-0" class="search-results-container" style="display: none;">
                                                </div>
                                                <div id="office-neighborhood-0-validation-message" class="error-message"></div>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="form-label required-field"><spring:message code="register.officeSpecialties"/></label>
                                            <div class="multi-select-container" id="office-specialties-container-0">
                                                <div class="custom-multi-select" id="office-specialties-options-0">
                                                    <c:forEach items="${specialtyList}" var="specialty">
                                                        <div class="custom-multi-select-option" data-value="${specialty.id}"
                                                             onclick="toggleOfficeSpecialty(this, 0)">
                                                            <div class="option-checkbox"></div>
                                                            <div class="option-text">
                                                                <spring:message code="${specialty.key}"/>
                                                            </div>
                                                        </div>
                                                    </c:forEach>
                                                </div>
                                                <div class="selected-options" id="office-specialties-selected-0"></div>
                                            </div>
                                            <small class="form-text text-muted">
                                                <spring:message code="register.selectOfficeSpecialties"/>
                                            </small>
                                            <div id="office-specialties-0-validation-message" class="error-message"></div>
                                        </div>

                                        <div class="office-actions">
                                            <button type="button" class="btn-remove office-remove" onclick="removeOffice(0)" style="display: none;">
                                                <i class="fas fa-times"></i>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                                <div class="error-container-server-doctorOfficeForm">
                                    <form:errors path="doctorOfficeForm" cssClass="error-message visible"/>
                                </div>
                                <button type="button" class="btn-add-slot" id="add-office-btn">
                                    <i class="fas fa-plus"></i> <spring:message code="register.addOffice"/>
                                </button>
                            </div>

                            <div class="form-navigation">
                                <button type="button" class="btn-prev" onclick="prevSection(2)">
                                    <i class="fas fa-arrow-left"></i> <spring:message code="register.previous"/>
                                </button>
                                <button type="button" class="btn-next" onclick="nextSection(2)">
                                    <spring:message code="register.next"/> <i class="fas fa-arrow-right"></i>
                                </button>
                            </div>
                        </div>

                        <div class="form-section" data-section="3">
                            <h2><spring:message code="register.availability"/></h2>

                            <div class="form-group">
                                <label for="password" class="form-label">
                                    <spring:message code="register.availabilitySlots"/>
                                    <span class="required-mark">*</span>
                                    <div class="tooltip">
                                        <i class="fas fa-info-circle tooltip-icon"></i>
                                        <span class="tooltip-content"><spring:message
                                                code="register.timeSlotHelp"/></span>
                                    </div>
                                </label>
                                <div id="timeslots-container" class="timeslots-container">
                                    <div id="no-slots-message" class="no-slots-message">
                                        <p><spring:message code="register.noTimeSlots"/></p>
                                    </div>

                                    <div id="time-slot-inputs">
                                    </div>

                                    <div id="time-slot-error" class="error-message" style="display: none;"></div>
                                    <div class="error-container">
                                    </div>

                                    <div class="error-container-server-doctorOfficeFormAvailabilitySlots">
                                        <form:errors path="officeAvailabilitySlotForms" cssClass="error-message visible"/>
                                    </div>

                                    <button type="button" class="btn-add-slot" id="add-slot-btn">
                                        <i class="fas fa-plus"></i> <spring:message code="register.addTimeSlot"/>
                                    </button>
                                </div>
                            </div>

                            <div class="form-group terms-checkbox">
                                <input type="checkbox" id="terms" class="custom-checkbox" required/>
                                <label for="terms" class="checkbox-label required-field">
                                    <spring:message code="register.agreeTerms"/>
                                    <a class="terms-link"><spring:message code="register.termsLink"/></a>
                                </label>
                            </div>

                            <div class="form-navigation">
                                <button type="button" class="btn-prev" onclick="prevSection(3)">
                                    <i class="fas fa-arrow-left"></i> <spring:message code="register.previous"/>
                                </button>
                                <button type="submit" class="btn-submit-doctor" id="registerButton">
                                    <spring:message code="register.button"/>
                                </button>
                            </div>
                        </div>
                    </form:form>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</main>


<script>
    window.messages = {
        dayOfWeek: '<spring:message code="register.dayOfWeek" javaScriptEscape="true" />',
        startTime: '<spring:message code="register.startTime" javaScriptEscape="true" />',
        endTime: '<spring:message code="register.endTime" javaScriptEscape="true" />',
        office: '<spring:message code="register.office" javaScriptEscape="true" />',
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
        noOfficesForSlot: '<spring:message code="register.noOfficesForSlot" javaScriptEscape="true" />',
        passwordWeak: '<spring:message code="register.passwordWeak" javaScriptEscape="true" />',
        passwordMedium: '<spring:message code="register.passwordMedium" javaScriptEscape="true" />',
        passwordStrong: '<spring:message code="register.passwordStrong" javaScriptEscape="true" />',
        passwordVeryStrong: '<spring:message code="register.passwordVeryStrong" javaScriptEscape="true" />',
        passwordsDoNotMatch: '<spring:message code="register.passwordsDoNotMatch" javaScriptEscape="true" />',
        passwordLength: '<spring:message code="register.passwordLength" javaScriptEscape="true" />',
        emailInvalid: '<spring:message code="appointment.validation.email" javaScriptEscape="true" />',
        phoneInvalid: '<spring:message code="appointment.validation.phone" javaScriptEscape="true" />',
        fileSizeError: '<spring:message code="fileSizeError" javaScriptEscape="true" />',
        fileTypeError: '<spring:message code="fileTypeError" javaScriptEscape="true" />',
        fieldRequired: '<spring:message code="register.fieldRequired" javaScriptEscape="true" />',
        nameTooShort: '<spring:message code="register.nameTooShort" javaScriptEscape="true" />',
        nameInvalid: '<spring:message code="register.nameInvalid" javaScriptEscape="true" />',
        passwordInvalid: '<spring:message code="register.passwordInvalid" javaScriptEscape="true" />',
        passwordReqLength: '<spring:message code="register.passwordReqLength" javaScriptEscape="true" />',
        passwordReqUppercase: '<spring:message code="register.passwordReqUppercase" javaScriptEscape="true" />',
        passwordReqLowercase: '<spring:message code="register.passwordReqLowercase" javaScriptEscape="true" />',
        passwordReqNumber: '<spring:message code="register.passwordReqNumber" javaScriptEscape="true" />',
        passwordReqSpecial: '<spring:message code="register.passwordReqSpecial" javaScriptEscape="true" />',
        specialtiesRequired: '<spring:message code="register.specialtiesRequired" javaScriptEscape="true" />',
        coveragesRequired: '<spring:message code="register.coveragesRequired" javaScriptEscape="true" />',
        submitting: '<spring:message code="register.submitting" javaScriptEscape="true" />',
        officeName: '<spring:message code="register.officeName" javaScriptEscape="true" />',
        officeNamePlaceholder: '<spring:message code="placeholder.officeName" javaScriptEscape="true" />',
        neighborhood: '<spring:message code="register.neighborhood" javaScriptEscape="true" />',
        neighborhoodPlaceholder: '<spring:message code="placeholder.neighborhood" javaScriptEscape="true" />',
        neighborhoodRequired: '<spring:message code="register.neighborhoodRequired" javaScriptEscape="true" />',
        officeRequired: '<spring:message code="register.officeRequired" javaScriptEscape="true" />',
        officeSpecialties: '<spring:message code="register.officeSpecialties" javaScriptEscape="true" />',
        selectOfficeSpecialties: '<spring:message code="register.selectOfficeSpecialties" javaScriptEscape="true" />',
        officeSpecialtiesRequired: '<spring:message code="register.officeSpecialtiesRequired" javaScriptEscape="true" />'
    };

    <c:if test="${not empty registerForm.doctorOfficeForm}">
    window.existingSlots = [];
    <c:forEach items="${registerForm.doctorOfficeForm}" var="office" varStatus="officeStatus">
    <c:if test="${not empty office.officeAvailabilitySlotForms}">
    <c:forEach items="${office.officeAvailabilitySlotForms}" var="slot" varStatus="slotStatus">
    window.existingSlots.push({
        index: ${officeStatus.index * 1000 + slotStatus.index},
        day: ${slot.dayOfWeek},
        startTime: '${slot.startTime}',
        endTime: '${slot.endTime}',
        officeIndex: ${officeStatus.index}
    });
    </c:forEach>
    </c:if>
    </c:forEach>
    </c:if>

    window.specialtyList = [<c:forEach items="${specialtyList}" var="specialty" varStatus="status">
        {
            id: ${specialty.id},
            name: "<spring:message code="${specialty.key}"/>"
        }<c:if test="${!status.last}">,</c:if>
        </c:forEach>];

    <c:if test="${not empty registerForm.doctorOfficeForm}">
    window.existingOffices = [
        <c:forEach items="${registerForm.doctorOfficeForm}" var="office" varStatus="status">
        {
            index: ${status.index},
            name: "<c:out value='${office.officeName}'/>",
            neighborhoodId: ${office.neighborhoodId},
            specialties: [
                <c:forEach items="${office.specialtyIds}" var="sid" varStatus="sidStatus">
                {
                    id: ${sid}
                }
                <c:if test="${!sidStatus.last}">,</c:if>
                </c:forEach>
            ]
        }<c:if test="${!status.last}">, </c:if>
        </c:forEach>
    ];
    </c:if>

    const neighborhoods = [<c:forEach items="${neighborhoodList}" var="neighborhood" varStatus="status">
        {
            id: "${neighborhood.id}",
            name: "${neighborhood.name}"
        }<c:if test="${!status.last}">,</c:if>
        </c:forEach>]
</script>

<script src="<c:url value='/js/register.js' />" defer></script>
</body>
</html>

