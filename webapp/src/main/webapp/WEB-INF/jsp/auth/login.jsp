<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!DOCTYPE html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title><spring:message code="login.title" /></title>
  <link rel="stylesheet" href="<c:url value='/css/login.css' />" />
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/components/header.jsp" />

<section class="login-page">
  <div class="login-container">
    <div class="login-welcome">
      <div class="welcome-content">
        <div class="welcome-icon">
          <i class="fas fa-stethoscope"></i>
        </div>
        <h1 class="welcome-title"><spring:message code="login.welcome" /></h1>
        <p class="welcome-text"><spring:message code="login.welcomeMessage" /></p>

        <div class="welcome-features">
          <div class="feature-item">
            <div class="feature-icon">
              <i class="fas fa-user-md"></i>
            </div>
            <div class="feature-text">
              <h3><spring:message code="login.feature.doctors.title" /></h3>
              <p><spring:message code="login.feature.doctors.description" /></p>
            </div>
          </div>

          <div class="feature-item">
            <div class="feature-icon">
              <i class="fas fa-calendar-check"></i>
            </div>
            <div class="feature-text">
              <h3><spring:message code="login.feature.appointments.title" /></h3>
              <p><spring:message code="login.feature.appointments.description" /></p>
            </div>
          </div>

          <div class="feature-item">
            <div class="feature-icon">
              <i class="fas fa-shield-alt"></i>
            </div>
            <div class="feature-text">
              <h3><spring:message code="login.feature.security.title" /></h3>
              <p><spring:message code="login.feature.security.description" /></p>
            </div>
          </div>
        </div>

        <div class="decoration-circles">
          <div class="circle circle-1"></div>
          <div class="circle circle-2"></div>
          <div class="circle circle-3"></div>
          <div class="circle circle-4"></div>
        </div>
      </div>
    </div>

    <div class="login-form-wrapper">
      <div class="form-container">
        <div class="form-header">
          <h2><spring:message code="login.title" /></h2>
          <p><spring:message code="login.subtitle" /></p>
        </div>

        <div class="notification-container">
          <c:if test="${param.error != null}">
            <div class="notification error-notification">
              <div class="notification-icon">
                <i class="fas fa-exclamation-circle"></i>
              </div>
              <div class="notification-content">
                <p><spring:message code="login.error" /></p>
              </div>
              <button class="notification-close" aria-label="<spring:message code="general.close" />">
                <i class="fas fa-times"></i>
              </button>
            </div>
          </c:if>

          <c:if test="${param.logout != null}">
            <div class="notification success-notification">
              <div class="notification-icon">
                <i class="fas fa-check-circle"></i>
              </div>
              <div class="notification-content">
                <p><spring:message code="login.logout.success" /></p>
              </div>
              <button class="notification-close" aria-label="<spring:message code="general.close" />">
                <i class="fas fa-times"></i>
              </button>
            </div>
          </c:if>
        </div>

        <c:url value="/login" var="loginUrl" />
        <form id="loginForm" action="${loginUrl}" method="post" class="form">
          <div class="form-group">
            <div class="input-container">
              <div class="input-icon">
                <i class="fas fa-envelope"></i>
              </div>
              <div class="input-field">
                <input
                        id="email"
                        name="j_email"
                        type="email"
                        required
                        autocomplete="email"
                        placeholder="<spring:message code="login.email.placeholder" />"
                />
                <label for="email" class="floating-label"><spring:message code="login.email" /></label>
              </div>
            </div>
            <div class="error-message" id="email-error"></div>
          </div>

          <div class="form-group">
            <div class="input-container">
              <div class="input-icon">
                <i class="fas fa-lock"></i>
              </div>
              <div class="input-field">
                <input
                        id="password"
                        name="j_password"
                        type="password"
                        required
                        autocomplete="current-password"
                        placeholder="<spring:message code="login.password.placeholder" />"
                />
                <label for="password" class="floating-label"><spring:message code="login.password" /></label>
                <button type="button" class="password-toggle" aria-label="<spring:message code="login.password.toggle" />">
                  <i class="fas fa-eye"></i>
                </button>
              </div>
            </div>
            <div class="error-message" id="password-error"></div>
          </div>

          <div class="form-options">
            <div class="remember-me">
              <label class="checkbox-container">
                <input type="checkbox" name="j_rememberme" id="remember-me">
                <span class="checkmark"></span>
                <span class="label-text"><spring:message code="login.rememberMe" /></span>
              </label>
            </div>

<%--            <a href="<c:url value='/forgot-password' />" class="forgot-password">--%>
            <a href="<c:url value='/recover-password' />" class="forgot-password">
              <spring:message code="login.forgotPassword" />
            </a>
          </div>

          <button type="submit" class="submit-btn" id="login-button">
            <span class="btn-text"><spring:message code="login.button" /></span>
            <span class="btn-loader">
              <i class="fas fa-circle-notch fa-spin"></i>
            </span>
          </button>
        </form>

        <div class="form-footer">
          <p><spring:message code="login.noAccount" /> <a href="<c:url value='/register' />"><spring:message code="login.register" /></a></p>
        </div>
      </div>
    </div>
  </div>
</section>

<script>
  document.addEventListener('DOMContentLoaded', function() {
    // Input animations
    const inputs = document.querySelectorAll('.input-field input');

    inputs.forEach(input => {
      // Check if input has value on load
      if (input.value !== '') {
        input.classList.add('has-value');
      }

      // Add event listeners
      input.addEventListener('focus', function() {
        this.parentElement.classList.add('focused');
      });

      input.addEventListener('blur', function() {
        this.parentElement.classList.remove('focused');
        if (this.value !== '') {
          this.classList.add('has-value');
        } else {
          this.classList.remove('has-value');
        }
      });

      input.addEventListener('input', function() {
        if (this.classList.contains('error')) {
          this.classList.remove('error');
          const errorElement = this.parentElement.parentElement.nextElementSibling;
          if (errorElement) {
            errorElement.textContent = '';
          }
        }
      });
    });

    // Password visibility toggle
    const passwordToggle = document.querySelector('.password-toggle');
    const passwordInput = document.getElementById('password');

    if (passwordToggle && passwordInput) {
      passwordToggle.addEventListener('click', function() {
        const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
        passwordInput.setAttribute('type', type);

        // Toggle icon
        const icon = this.querySelector('i');
        if (type === 'password') {
          icon.classList.remove('fa-eye-slash');
          icon.classList.add('fa-eye');
        } else {
          icon.classList.remove('fa-eye');
          icon.classList.add('fa-eye-slash');
        }
      });
    }

    // Form validation
    const loginForm = document.getElementById('loginForm');
    const emailInput = document.getElementById('email');
    const emailError = document.getElementById('email-error');
    const passwordError = document.getElementById('password-error');
    const loginButton = document.getElementById('login-button');

    if (loginForm) {
      loginForm.addEventListener('submit', function(e) {
        let isValid = true;

        // Validate email
        if (!emailInput.value.trim()) {
          emailError.textContent = '<spring:message code="login.validation.email.required" javaScriptEscape="true" />';
          emailInput.classList.add('error');
          emailInput.parentElement.classList.add('error');
          isValid = false;
        } else if (!/^\S+@\S+\.\S+$/.test(emailInput.value.trim())) {
          emailError.textContent = '<spring:message code="login.validation.email.invalid" javaScriptEscape="true" />';
          emailInput.classList.add('error');
          emailInput.parentElement.classList.add('error');
          isValid = false;
        } else {
          emailError.textContent = '';
          emailInput.classList.remove('error');
          emailInput.parentElement.classList.remove('error');
        }

        // Validate password
        if (!passwordInput.value) {
          passwordError.textContent = '<spring:message code="login.validation.password.required" javaScriptEscape="true" />';
          passwordInput.classList.add('error');
          passwordInput.parentElement.classList.add('error');
          isValid = false;
        } else {
          passwordError.textContent = '';
          passwordInput.classList.remove('error');
          passwordInput.parentElement.classList.remove('error');
        }

        if (!isValid) {
          e.preventDefault();
        } else {
          // Show loading state
          loginButton.classList.add('loading');
          loginButton.disabled = true;
        }
      });
    }

    // Close notification
    const notificationCloseButtons = document.querySelectorAll('.notification-close');

    notificationCloseButtons.forEach(button => {
      button.addEventListener('click', function() {
        const notification = this.closest('.notification');
        notification.classList.add('fade-out');

        setTimeout(() => {
          notification.style.display = 'none';
        }, 300);
      });
    });

    const fixedHeader = document.querySelector(".main-header");
    const mainContent = document.querySelector(".login-page");

    if (fixedHeader && mainContent) {
      const adjustContentMargin = () => {
        const headerHeight = fixedHeader.offsetHeight;
        mainContent.style.paddingTop = (headerHeight * 1.25) + `px`;
      };

      // Adjust on page load
      adjustContentMargin();

      // Adjust on window resize
      window.addEventListener("resize", adjustContentMargin);
    }

    // Auto-dismiss notifications after 5 seconds
    const notifications = document.querySelectorAll('.notification');

    notifications.forEach(notification => {
      setTimeout(() => {
        notification.classList.add('fade-out');
        setTimeout(() => {
          notification.style.display = 'none';
        }, 300);
      }, 5000);
    });
  });
</script>
</body>
