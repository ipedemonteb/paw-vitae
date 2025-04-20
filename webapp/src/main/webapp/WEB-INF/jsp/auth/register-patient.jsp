<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="comp" tagdir="/WEB-INF/tags/components" %>

<link rel="stylesheet" href="<c:url value='/css/components/forms.css' />" />
<link rel="stylesheet" href="<c:url value='/css/components/register.css' />" />
<link rel="stylesheet" href="<c:url value='/css/components/register-doctor.css' />" />
<script src="<c:url value='/js/register-patient.js' />" defer></script>

<layout:page title="register.title">
  <div class="register-container">
    <div class="register-card">
      <div class="card-header">
        <h1 class="card-title-register"><spring:message code="register.title" /></h1>
        <p class="card-subtitle-register"><spring:message code="register.subtitle" /></p>
      </div>

      <div class="card-body">
        <form:form modelAttribute="patientForm" method="post" action="${pageContext.request.contextPath}/register-patient" enctype="multipart/form-data" cssClass="register-form">
          <input type="hidden" id="passwordValue" name="passwordValue" value="${patientForm.password}" />
          <input type="hidden" id="repeatPasswordValue" name="repeatPasswordValue" value="${patientForm.repeatPassword}" />

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

<%--          <div class="form-row">--%>
<%--            <div class="form-group">--%>
<%--              <label for="password" class="form-label required-field"><spring:message code="register.password" /></label>--%>
<%--              <form:password path="password" id="password" cssClass="form-control" />--%>
<%--              <form:errors path="password" cssClass="error-message" />--%>
<%--            </div>--%>

<%--            <div class="form-group">--%>
<%--              <label for="repeatPassword" class="form-label required-field"><spring:message code="register.confirmPassword" /></label>--%>
<%--              <form:password path="repeatPassword" id="repeatPassword" cssClass="form-control" />--%>
<%--              <form:errors path="repeatPassword" cssClass="error-message" />--%>
<%--            </div>--%>
<%--          </div>--%>

          <div class="form-group">
            <label class="form-label required-field"><spring:message code="register.coverage" /></label>
            <div class="coverage-options">
              <c:forEach items="${coverageList}" var="coverage" varStatus="status">
                <div class="coverage-option">
                  <form:radiobutton path="coverage" id="coverage-${coverage.id}" value="${coverage.id}" cssClass="coverage-radio" />
                  <label for="coverage-${coverage.id}" class="coverage-label">
                    <span class="radio-circle"></span>
                    <span class="coverage-name"><c:out value="${coverage.name}"/></span>
                  </label>
                </div>
              </c:forEach>
            </div>
            <form:errors path="coverage" cssClass="error-message" />
          </div>

          <div class="form-group terms-checkbox">
            <input type="checkbox" id="terms" class="custom-checkbox" required />
            <label for="terms" class="checkbox-label required-field">
              <spring:message code="register.agreeTerms" />
              <a class="terms-link"><spring:message code="register.termsLink" /></a>
            </label>
          </div>

          <div class="form-group">
            <button type="submit" class="btn-submit" id="registerButton" onclick="this.disabled = true ; this.form.submit();">
              <spring:message code="register.button" />
            </button>
          </div>

          <div class="login-link">
            <spring:message code="register.alreadyHaveAccount" />
            <a href="<c:url value='/login' />" class="terms-link"><spring:message code="register.loginLink" /></a>
          </div>
        </form:form>
      </div>
    </div>
  </div>

<%--  <script>--%>
<%--    document.addEventListener('DOMContentLoaded', function() {--%>
<%--      // File upload preview--%>
<%--      const fileInput = document.getElementById('image-upload');--%>
<%--      const fileUploadLabel = document.querySelector('.file-upload-label');--%>
<%--      const fileName = document.getElementById('file-name');--%>

<%--      if (fileInput) {--%>
<%--        fileInput.addEventListener('change', function() {--%>
<%--          if (this.files && this.files[0]) {--%>
<%--            const file = this.files[0];--%>
<%--            const fileNameElement = document.createElement('div');--%>
<%--            fileNameElement.textContent = file.name;--%>
<%--            fileNameElement.style.marginTop = '0.5rem';--%>
<%--            fileNameElement.style.padding = '0.5rem';--%>
<%--            fileNameElement.style.backgroundColor = '#e6f0ff';--%>
<%--            fileNameElement.style.borderRadius = '0.25rem';--%>
<%--            fileNameElement.style.color = '#2a5caa';--%>
<%--            fileNameElement.style.fontSize = '0.875rem';--%>

<%--            if (fileName.firstChild) {--%>
<%--              fileName.removeChild(fileName.firstChild);--%>
<%--            }--%>
<%--            fileName.appendChild(fileNameElement);--%>
<%--          }--%>
<%--        });--%>
<%--      }--%>

<%--      // Drag and drop functionality--%>
<%--      if (fileUploadLabel) {--%>
<%--        ['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => {--%>
<%--          fileUploadLabel.addEventListener(eventName, preventDefaults, false);--%>
<%--        });--%>

<%--        function preventDefaults(e) {--%>
<%--          e.preventDefault();--%>
<%--          e.stopPropagation();--%>
<%--        }--%>

<%--        ['dragenter', 'dragover'].forEach(eventName => {--%>
<%--          fileUploadLabel.addEventListener(eventName, highlight, false);--%>
<%--        });--%>

<%--        ['dragleave', 'drop'].forEach(eventName => {--%>
<%--          fileUploadLabel.addEventListener(eventName, unhighlight, false);--%>
<%--        });--%>

<%--        function highlight() {--%>
<%--          fileUploadLabel.style.borderColor = '#2a5caa';--%>
<%--          fileUploadLabel.style.backgroundColor = '#e6f0ff';--%>
<%--        }--%>

<%--        function unhighlight() {--%>
<%--          fileUploadLabel.style.borderColor = '#e0e0e0';--%>
<%--          fileUploadLabel.style.backgroundColor = '#f8f9fa';--%>
<%--        }--%>

<%--        fileUploadLabel.addEventListener('drop', handleDrop, false);--%>

<%--        function handleDrop(e) {--%>
<%--          const dt = e.dataTransfer;--%>
<%--          const files = dt.files;--%>
<%--          fileInput.files = files;--%>

<%--          if (files && files[0]) {--%>
<%--            const file = files[0];--%>
<%--            const fileNameElement = document.createElement('div');--%>
<%--            fileNameElement.textContent = file.name;--%>
<%--            fileNameElement.style.marginTop = '0.5rem';--%>
<%--            fileNameElement.style.padding = '0.5rem';--%>
<%--            fileNameElement.style.backgroundColor = '#e6f0ff';--%>
<%--            fileNameElement.style.borderRadius = '0.25rem';--%>
<%--            fileNameElement.style.color = '#2a5caa';--%>
<%--            fileNameElement.style.fontSize = '0.875rem';--%>

<%--            if (fileName.firstChild) {--%>
<%--              fileName.removeChild(fileName.firstChild);--%>
<%--            }--%>
<%--            fileName.appendChild(fileNameElement);--%>
<%--          }--%>
<%--        }--%>
<%--      }--%>
<%--    });--%>
<%--  </script>--%>

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