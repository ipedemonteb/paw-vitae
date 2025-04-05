<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ attribute name="doctor" required="true" type="ar.edu.itba.paw.models.Doctor" %>

<div class="doctor-info">
    <div class="doctor-info-content">
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
    <div class="doctor-image">
        <img src="<c:url value="/doctor/${doctor.id}/image"/>" alt="doctor_image"/>
    </div>
</div>

