<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="comp" tagdir="/WEB-INF/tags/components" %>

<layout:page title="register.title">
  <div class="card">
    <div class="card-header">
      <h1 class="card-title"><spring:message code="register.title" /></h1>
      <p class="card-subtitle"><spring:message code="register.subtitle" /></p>
    </div>

    <div class="card-body">
      <form:form modelAttribute="patientForm" method="post" action="${pageContext.request.contextPath}/register-patient" enctype="multipart/form-data">
        <div class="form-row">
          <comp:form-group path="name" label="register.firstName" />
          <comp:form-group path="lastName" label="register.lastName" />
        </div>

        <div class="form-row">
          <comp:form-group path="email" label="register.email" type="email" />
          <comp:form-group path="phone" label="register.phone" />
        </div>

        <div class="form-row">
          <comp:form-group path="password" label="register.password" type="password" />
          <comp:form-group path="repeatPassword" label="register.confirmPassword" type="password" />
        </div>

        <div class="form-group">
          <label><spring:message code="register.coverage" /></label>
          <div class="specialty-select-container">
            <input type="checkbox" id="coverage-dropdown-toggle" class="dropdown-toggle">
            <label for="coverage-dropdown-toggle" class="specialty-select-trigger">
              <spring:message code="register.selectCoverage" />
            </label>

            <div class="specialty-select">
              <c:forEach items="${coverageList}" var="coverage">
                <div class="specialty-option">
                  <form:radiobutton path="coverage" id="coverage-${coverage.id}" value="${coverage.id}" class="specialty-radio" />
                  <label for="coverage-${coverage.id}" class="specialty-label"><c:out value="${coverage.name}"/></label>
                </div>
              </c:forEach>
            </div>
          </div>
          <form:errors path="coverage" cssClass="error-message" />
        </div>

        <div class="form-group">
          <label for="image"><spring:message code="register.uploadImage" />:</label>
          <form:input type="file" path="image" id="image" accept="image/*" class="form-control" />
          <form:errors path="image" cssClass="error-message" />
        </div>

        <div class="form-group">
          <button type="submit" class="btn btn-primary btn-block">
            <spring:message code="register.button" />
          </button>
        </div>
      </form:form>
    </div>
  </div>
</layout:page>