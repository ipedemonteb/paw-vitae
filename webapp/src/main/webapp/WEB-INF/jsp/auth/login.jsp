<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="comp" tagdir="/WEB-INF/tags/components" %>

<layout:page title="login.title">
  <div class="login-page">
    <div class="login-visual">
      <div class="visual-content">
        <div class="brand-logo">
          <div class="logo-shape"></div>
          <div class="logo-shape"></div>
        </div>
        <h2 class="visual-title"><spring:message code="login.welcome" /></h2>
        <p class="visual-text"><spring:message code="login.welcomeMessage" /></p>
      </div>
      <div class="shape-container">
        <div class="shape shape-1"></div>
        <div class="shape shape-2"></div>
        <div class="shape shape-3"></div>
      </div>
    </div>

    <div class="login-content">
      <div class="login-header">
        <h1 class="login-title"><spring:message code="login.title" /></h1>
        <p class="login-subtitle"><spring:message code="login.subtitle" /></p>
      </div>

      <c:if test="${param.error != null}">
        <div class="notification notification-error" role="alert">
          <spring:message code="login.error" />
        </div>
      </c:if>
      <c:if test="${param.logout != null}">
        <div class="notification notification-success" role="alert">
          <spring:message code="login.logout.success" />
        </div>
      </c:if>

      <c:url value="/login" var="loginUrl" />
      <form id="loginForm" action="${loginUrl}" method="post" enctype="application/x-www-form-urlencoded" class="login-form">
        <div class="form-group">
          <label for="email" class="form-label"><spring:message code="login.email" /></label>
          <div class="input-wrapper">
            <input id="email" name="j_email" type="email" class="form-input" required
                   aria-label="<spring:message code="login.email" />" />
            <div class="input-line"></div>
          </div>
          <div class="form-error" id="email-error"></div>
        </div>

        <div class="form-group">
          <div class="label-row">
            <label for="password" class="form-label"><spring:message code="login.password" /></label>
            <a href="#" class="forgot-link">
              <spring:message code="login.forgotPassword" />
            </a>
          </div>
          <div class="input-wrapper">
            <input id="password" name="j_password" type="password" class="form-input" required
                   aria-label="<spring:message code="login.password" />" />
            <div class="input-line"></div>
            <button type="button" class="toggle-password" aria-label="Toggle password visibility">
              <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                <circle cx="12" cy="12" r="3"></circle>
              </svg>
            </button>
          </div>
          <div class="form-error" id="password-error"></div>
        </div>

        <div class="form-group checkbox-group">
          <label class="checkbox-label">
            <input type="checkbox" name="j_rememberme" class="checkbox-input" />
            <span class="checkbox-custom"></span>
            <span class="checkbox-text"><spring:message code="login.rememberMe" /></span>
          </label>
        </div>

        <div class="form-group">
          <button type="submit" class="login-button" onclick="this.disabled = true ; this.form.submit();">
            <span class="button-text"><spring:message code="login.button" /></span>
            <span class="button-loader"></span>
          </button>
        </div>
      </form>

      <div class="register-prompt">
        <span><spring:message code="login.noAccount" /></span>
        <a href="<c:url value='/register' />" class="register-link">
          <spring:message code="login.register" />
        </a>
      </div>
    </div>
  </div>

  <script>
    document.addEventListener('DOMContentLoaded', function() {
      // Password visibility toggle
      const togglePassword = document.querySelector('.toggle-password');
      const passwordInput = document.getElementById('password');

      if (togglePassword && passwordInput) {
        togglePassword.addEventListener('click', function() {
          const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
          passwordInput.setAttribute('type', type);
          this.classList.toggle('show-password');
        });
      }

      // Form validation
      const loginForm = document.getElementById('loginForm');
      const emailInput = document.getElementById('email');
      const emailError = document.getElementById('email-error');
      const passwordError = document.getElementById('password-error');

      if (loginForm) {
        loginForm.addEventListener('submit', function(e) {
          let isValid = true;

          // Validate email
          if (!emailInput.value.trim()) {
            emailError.textContent = 'Email is required';
            emailInput.classList.add('input-error');
            isValid = false;
          } else if (!/^\S+@\S+\.\S+$/.test(emailInput.value.trim())) {
            emailError.textContent = 'Please enter a valid email address';
            emailInput.classList.add('input-error');
            isValid = false;
          } else {
            emailError.textContent = '';
            emailInput.classList.remove('input-error');
          }

          // Validate password
          if (!passwordInput.value) {
            passwordError.textContent = 'Password is required';
            passwordInput.classList.add('input-error');
            isValid = false;
          } else {
            passwordError.textContent = '';
            passwordInput.classList.remove('input-error');
          }

          if (!isValid) {
            e.preventDefault();
          } else {
            // Show loading state
            document.querySelector('.login-button').classList.add('is-loading');
          }
        });
      }

      // Input focus effects
      const inputs = document.querySelectorAll('.form-input');

      inputs.forEach(input => {
        // Initial check for pre-filled inputs
        if (input.value) {
          input.classList.add('has-value');
        }

        input.addEventListener('focus', function() {
          this.parentElement.classList.add('input-focused');
        });

        input.addEventListener('blur', function() {
          this.parentElement.classList.remove('input-focused');
          if (this.value) {
            this.classList.add('has-value');
          } else {
            this.classList.remove('has-value');
          }
        });

        input.addEventListener('input', function() {
          if (this.classList.contains('input-error')) {
            this.classList.remove('input-error');
            const errorElement = this.parentElement.nextElementSibling;
            if (errorElement && errorElement.classList.contains('form-error')) {
              errorElement.textContent = '';
            }
          }
        });
      });

      // Auto-dismiss notifications
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
</layout:page>
