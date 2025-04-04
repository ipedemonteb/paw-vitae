<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="appointment.confirmation.title"/></title>
    <link rel="stylesheet" href="<c:url value="/css/confirmation.css" />">
</head>
<body>
<div class="container">
    <div class="confirmation-card">
        <h1 class="title"><spring:message code="appointment.confirmation.title"/></h1>

        <div class="confirmation-details">
            <div class="confirmation-item">
                <div class="confirmation-label"><spring:message code="appointment.form.reason"/></div>
                <div class="confirmation-value">${appointment.reason}</div>
            </div>

            <div class="confirmation-item">
                <div class="confirmation-label"><spring:message code="appointment.selectedDoctor"/></div>
                <div class="confirmation-value">${doctor.name} ${doctor.lastName}</div>
            </div>

            <div class="confirmation-item">
                <div class="confirmation-label"><spring:message code="appointment.form.date"/></div>
                <div class="confirmation-value">${appointment.date.toLocalDate()}</div>
            </div>

            <div class="confirmation-item">
                <div class="confirmation-label"><spring:message code="appointment.form.time"/></div>
                <div class="confirmation-value">${appointment.date.toLocalTime()}</div>
            </div>
        </div>

        <c:if test="${not empty doctor}">
            <div class="doctor-info">
                <h3><spring:message code="appointment.selectedDoctor" /></h3>
                <p><strong>${doctor.name} ${doctor.lastName}</strong></p>
                <c:if test="${not empty doctor.specialty}">
                    <p class="doctor-specialty">${doctor.specialty}</p>
                </c:if>
                <c:if test="${not empty doctor.email}">
                    <p><spring:message code="appointment.doctor.email" />: ${doctor.email}</p>
                </c:if>
                <c:if test="${not empty doctor.phone}">
                    <p><spring:message code="appointment.doctor.phone" />: ${doctor.phone}</p>
                </c:if>
            </div>
        </c:if>

        <div class="actions">
            <a href="<c:url value="/portal"/>" class="btn-primary">
                <spring:message code="appointment.confirmation.backToHome"/>
            </a>
        </div>
    </div>
</div>
</body>
</html>