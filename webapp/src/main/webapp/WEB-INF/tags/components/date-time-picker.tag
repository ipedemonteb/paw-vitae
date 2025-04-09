<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%-- Attributes --%>
<%@ attribute name="id" required="true" rtexprvalue="true" %>
<%@ attribute name="name" required="true" rtexprvalue="true" %>
<%@ attribute name="label" required="true" rtexprvalue="true" %>
<%@ attribute name="doctorId" required="true" rtexprvalue="true" %>

<div class="form-group">
    <label for="${id}Picker"><spring:message code="${label}"/></label>

    <%-- Date Picker Container --%>
    <div class="date-picker-container">
        <input type="text" id="${id}Picker" class="form-control date-picker-input"
               placeholder="<spring:message code="appointment.placeholder.selectDate"/>" readonly>

        <%-- Calendar Popup --%>
        <div id="${id}Calendar" class="date-picker-calendar">
            <div class="date-picker-header">
                <button type="button" class="date-picker-nav prev-month">&lt;</button>
                <div class="date-picker-month-year"></div>
                <button type="button" class="date-picker-nav next-month">&gt;</button>
            </div>
            <div class="date-picker-weekdays"></div>
            <div class="date-picker-days"></div>
        </div>
    </div>

    <%-- Hidden fields to store actual values --%>
    <input type="hidden" id="${id}" name="${name}">
    <input type="hidden" id="${id}Hour" name="${name}Hour">

    <%-- Time slots container --%>
    <div id="${id}TimeSlotsContainer" class="time-slots-container" style="display: none;">
        <h6><spring:message code="appointment.form.selectTime"/></h6>
        <div id="${id}TimeSlots" class="time-slots-grid"></div>
    </div>

    <%-- Appointment summary --%>
    <div id="${id}Summary" class="appointment-summary hidden">
        <p class="mb-1"><strong><spring:message code="appointment.summary.title"/></strong></p>
        <p id="${id}SummaryText" class="mb-0"></p>
    </div>
</div>