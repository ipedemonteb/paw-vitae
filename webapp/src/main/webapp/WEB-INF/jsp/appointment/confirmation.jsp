<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="comp" tagdir="/WEB-INF/tags/components" %>

<link rel="stylesheet" href="<c:url value='/css/components/confirmation.css' />">

<layout:page title="appointment.confirmation.title">
<div class="card">
    <div class="card-header">
        <h1 class="card-title"><spring:message code="appointment.confirmation.title"/></h1>
    </div>

    <div class="card-body">
        <div class="confirmation-details">
            <div class="confirmation-item">
                <div class="confirmation-label"><spring:message code="appointment.form.reason"/></div>
                <div class="confirmation-value"><c:out value="${appointment.reason}"/></div>
            </div>

            <div class="confirmation-item">
                <div class="confirmation-label"><spring:message code="appointment.selectedDoctor"/></div>
                <div class="confirmation-value"><c:out value="${appointment.doctor.name} ${appointment.doctor.lastName}"/></div>
            </div>

            <div class="confirmation-item">
                <div class="confirmation-label"><spring:message code="appointment.form.specialty"/></div>
                <div class="confirmation-value"><spring:message code='${specialty.key}' /></div>
            </div>

            <div class="confirmation-item">
                <div class="confirmation-label"><spring:message code="appointment.form.date"/></div>
                <div class="confirmation-value"><c:out value="${appointment.date.toLocalDate()}"/></div>
            </div>

            <div class="confirmation-item">
                <div class="confirmation-label"><spring:message code="appointment.form.time"/></div>
                <div class="confirmation-value"><c:out value="${appointment.date.toLocalTime()}"/></div>
            </div>
        </div>

        <c:if test="${not empty appointment.doctor}">
            <comp:doctor-info doctor="${appointment.doctor}" />
        </c:if>
    </div>

    <div class="card-footer">
        <a href="<c:url value="/patient/dashboard"/>" class="btn btn-primary">
            <spring:message code="appointment.confirmation.backToHome"/>
        </a>
    </div>
</div>
</layout:page>