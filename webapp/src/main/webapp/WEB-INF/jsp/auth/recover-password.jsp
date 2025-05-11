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
    <title><spring:message code="recover.password.page.title" /></title>
    <link rel="stylesheet" href="<c:url value='/css/recover-password.css' />" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
<!-- Include the header -->
<jsp:include page="/WEB-INF/jsp/components/header.jsp" />

<!-- Main Content -->
<div class="main-content">
    <div class="container">
        <div class="recover-password-container">
            <div class="card">
                <div class="card-header">
                    <h1 class="card-title"><spring:message code="recover.password.title" /></h1>
                    <p class="card-subtitle"><spring:message code="recover.password.subtitle" /></p>
                </div>

                <div class="card-body">
                    <!-- Check for URL parameters -->
                    <c:choose>
                        <%-- Email sent confirmation --%>
                        <c:when test="${param.recover eq 'sent'}">
                            <div class="status-message success-message">
                                <div class="status-icon">
                                    <i class="fas fa-check-circle"></i>
                                </div>
                                <div class="status-content">
                                    <h3><spring:message code="recover.password.email.sent.title" /></h3>
                                    <p><spring:message code="recover.password.email.sent.message" /></p>
                                </div>
                            </div>

                            <div class="form-links">
                                <a href="${pageContext.request.contextPath}/login" class="link">
                                    <i class="fas fa-arrow-left"></i>
                                    <spring:message code="recover.password.back.to.login" />
                                </a>
                            </div>
                        </c:when>

                        <%-- Default: show the form --%>
                        <c:otherwise>
                            <!-- Success Message -->
                            <c:if test="${not empty successMessage}">
                                <div class="alert alert-success">
                                    <i class="fas fa-check-circle"></i>
                                    <span><c:out value="${successMessage}" /></span>
                                </div>
                            </c:if>

                            <!-- Error Message -->
                            <c:if test="${not empty errorMessage}">
                                <div class="alert alert-danger">
                                    <i class="fas fa-exclamation-circle"></i>
                                    <span><c:out value="${errorMessage}" /></span>
                                </div>
                            </c:if>

                            <form:form modelAttribute="recoverPasswordForm" method="post" action="${pageContext.request.contextPath}/recover-password" class="recover-form">
                                <div class="form-group form-floating">
                                    <form:input path="email" id="email" class="form-control" placeholder=" " required="true" />
                                    <label for="email" class="form-label"><spring:message code="recover.password.email.label" /></label>
                                    <div id="email-validation-message" class="validation-message"></div>
                                    <form:errors path="email" cssClass="error-message" />
                                </div>

                                <div class="form-group">
                                    <button type="submit" id="recoverButton" class="btn btn-primary btn-block">
                                        <span>
                                            <i class="fas fa-paper-plane"></i>
                                            <spring:message code="recover.password.submit" />
                                        </span>
                                    </button>
                                </div>
                            </form:form>

                            <div class="form-links">
                                <a href="${pageContext.request.contextPath}/login" class="link">
                                    <i class="fas fa-arrow-left"></i>
                                    <spring:message code="recover.password.back.to.login" />
                                </a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- JavaScript for validation -->
<script>
    document.addEventListener("DOMContentLoaded", function() {

        // Email validation
        const emailField = document.getElementById("email");
        const emailValidationMessage = document.getElementById("email-validation-message");
        const recoverButton = document.getElementById("recoverButton");

        if (emailField) {
            emailField.addEventListener("input", function() {
                validateEmail(this);
                updateButtonState();
            });
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
            if (!recoverButton) return;

            const isEmailValid = emailField && validateEmail(emailField);

            recoverButton.disabled = !isEmailValid;

            if (recoverButton.disabled) {
                recoverButton.classList.add("disabled");
            } else {
                recoverButton.classList.remove("disabled");
            }
        }

        // Initial validation
        if (emailField) {
            validateEmail(emailField);
            updateButtonState();
        }
    });
    function validateEmail(field) {
        if (!field) return false;

        const email = field.value;
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        if (!email && field.hasAttribute("required")) {
            setFieldError(field, emailValidationMessage, window.messages?.fieldRequired || "Email is required");
            return false;
        } else if (email && !emailPattern.test(email)) {
            setFieldError(field, emailValidationMessage, window.messages?.emailInvalid || "Please enter a valid email address");
            return false;
        } else if (email) {
            setFieldValid(field, emailValidationMessage);
            return true;
        } else {
            clearFieldValidation(field, emailValidationMessage);
            return true;
        }
    }
</script>
</body>
</html>