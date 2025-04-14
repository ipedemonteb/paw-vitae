<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>

<layout:page title="dashboard.profile.edit">
    <div class="form-container">
        <div class="form-header">
            <h2><spring:message code="dashboard.profile.edit" /></h2>
            <p><spring:message code="dashboard.profile.personalInfo" /></p>
        </div>

        <form:form modelAttribute="updatePatientForm" method="post" action="${pageContext.request.contextPath}/client?update" cssClass="edit-profile-form">
            <div class="form-row">
                <div class="form-group">
                    <form:label path="name"><spring:message code="register.firstName" /></form:label>
                    <form:input path="name" cssClass="form-control" />
                    <form:errors path="name" cssClass="error-message" />
                </div>

                <div class="form-group">
                    <form:label path="lastName"><spring:message code="register.lastName" /></form:label>
                    <form:input path="lastName" cssClass="form-control" />
                    <form:errors path="lastName" cssClass="error-message" />
                </div>
            </div>

            <div class="form-group">
                <form:label path="phone"><spring:message code="register.phone" /></form:label>
                <form:input path="phone" cssClass="form-control" />
                <form:errors path="phone" cssClass="error-message" />
            </div>

            <div class="form-group">
                <form:label path="coverage"><spring:message code="register.coverage" /></form:label>
                <form:input path="coverage" cssClass="form-control" />
                <form:errors path="coverage" cssClass="error-message" />
            </div>

            <div class="form-actions">
                <a href="${pageContext.request.contextPath}/patient/dashboard" class="btn btn-secondary">
                    <spring:message code="logout.confirmation.cancel" />
                </a>
                <button type="submit" class="btn btn-primary">
                    <spring:message code="appointment.form.submit" />
                </button>
            </div>
        </form:form>
    </div>

    <link rel="stylesheet" href="<c:url value='/css/components/forms.css' />" />
</layout:page>
