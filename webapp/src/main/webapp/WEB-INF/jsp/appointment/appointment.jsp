<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="comp" tagdir="/WEB-INF/tags/components" %>

<layout:page title="appointment.page.title">
<div class="card">
    <div class="card-header">
        <h1 class="card-title"><spring:message code="appointment.title" /></h1>
        <p class="card-subtitle"><spring:message code="appointment.subtitle" /></p>
    </div>

    <div class="card-body">
        <!-- Display doctor information if available -->
        <c:if test="${not empty doctor}">
            <comp:doctor-info doctor="${doctor}" />
        </c:if>

        <form:form modelAttribute="appointmentForm" method="post" action="${pageContext.request.contextPath}/appointment">
            <!-- Hidden field for doctor ID -->
            <form:hidden path="doctorId" />

            <div class="form-row">
                <comp:form-group path="name" label="appointment.form.firstName" />
                <comp:form-group path="lastName" label="appointment.form.lastName" />
            </div>

            <div class="form-row">
                <comp:form-group path="email" label="appointment.form.email" type="email" />
                <comp:form-group path="phone" label="appointment.form.phone" />
            </div>

            <div class="form-group">
                <label for="coverageId"><spring:message code="appointment.form.coverage" /></label>
                <form:select path="coverageId" id="coverageId" class="form-control">
                    <option value=""><spring:message code="appointment.placeholder.coverage" /></option>
                    <c:forEach var="coverage" items="${coverages}">
                        <option value="${coverage.id}">${coverage.name}</option>
                    </c:forEach>
                </form:select>
                <form:errors path="coverageId" cssClass="error-message" />
            </div>

            <div class="form-group">
                <label for="appointmentDate"><spring:message code="appointment.form.date"/></label>
                <input type="date" id="appointmentDate" name="appointmentDate" class="form-control" />
            </div>

            <div class="form-group">
                <label for="appointmentHour"><spring:message code="appointment.form.time"/></label>
                <select id="appointmentHour" name="appointmentHour" class="form-control">
                    <c:forEach var="hour" begin="0" end="23">
                        <option value="${hour}">${hour}:00</option>
                    </c:forEach>
                </select>
            </div>

            <comp:form-group path="reason" label="appointment.form.reason" type="textarea" />

            <div class="form-group">
                <button type="submit" class="btn btn-primary btn-block">
                    <spring:message code="appointment.form.submit" />
                </button>
            </div>
        </form:form>
    </div>
</div>
</layout:page>