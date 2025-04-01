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
    <title><spring:message code="appointment.page.title" /></title>
    <link rel="stylesheet" href="<c:url value="/css/appointment.css" />">
</head>
<body>
<div class="container">
    <div class="appointment-card">
        <h1 class="title"><spring:message code="appointment.title" /></h1>
        <p class="subtitle"><spring:message code="appointment.subtitle" /></p>

        <form:form modelAttribute="appointmentForm" method="post" action="${pageContext.request.contextPath}/appointment">
            <div class="form-row">
                <div class="form-group">
                    <label for="name"><spring:message code="appointment.form.firstName" /></label>
                    <form:input path="name" id="name" class="form-control" />
                    <form:errors path="name" cssClass="error-message" />
                </div>
                <div class="form-group">
                    <label for="lastName"><spring:message code="appointment.form.lastName" /></label>
                    <form:input path="lastName" id="lastName" class="form-control" />
                    <form:errors path="lastName" cssClass="error-message" />
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="email"><spring:message code="appointment.form.email" /></label>
                    <form:input path="email" id="email" type="email" class="form-control" />
                    <form:errors path="email" cssClass="error-message" />
                </div>
                <div class="form-group">
                    <label for="phone"><spring:message code="appointment.form.phone" /></label>
                    <form:input path="phone" id="phone" class="form-control" />
                    <form:errors path="phone" cssClass="error-message" />
                </div>
            </div>

            <div class="form-group">
                <label for="coverageId"><spring:message code="appointment.form.coverage" /></label>
                <form:select path="coverageId" id="coverageId" class="form-control">
                    <option value="0"><spring:message code="appointment.placeholder.coverage" /></option>
                    <option value="1"><spring:message code="appointment.placeholder.coverage" />!!!</option>
                </form:select>
                <form:errors path="coverageId" cssClass="error-message" />
            </div>

<%--            <div class="form-group">--%>
<%--                <label for="appointmentDateTime">--%>
<%--                    <spring:message code="appointment.form.dateTime" />--%>
<%--                </label>--%>
<%--                <form:input path="appointmentDateTime" id="appointmentDateTime" type="datetime-local" step="3600" class="form-control" />--%>
<%--                <form:errors path="appointmentDateTime" cssClass="error-message" />--%>
<%--            </div>--%>
            <div class="form-group">
                <label for="appointmentDate">Date</label>
                <input type="date" id="appointmentDate" name="appointmentDate" class="form-control" />
            </div>

            <div class="form-group">
                <label for="appointmentHour">Hour</label>
                <select id="appointmentHour" name="appointmentHour" class="form-control">
                    <!-- Options for hours (0 to 23) -->
                    <c:forEach var="hour" begin="0" end="23">
                        <option value="${hour}">${hour}:00</option>
                    </c:forEach>
                </select>
            </div>



            <div class="form-group">
                <label for="reason"><spring:message code="appointment.form.reason" /></label>
                <form:textarea path="reason" id="reason" class="form-control" rows="3" />
                <form:errors path="reason" cssClass="error-message" />
            </div>

            <div class="form-group">
                <button type="submit" class="btn-primary"><spring:message code="appointment.form.submit" /></button>
            </div>
        </form:form>
    </div>
</div>
</body>
</html>