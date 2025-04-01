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
    <title>Register</title>
    <link rel="stylesheet" href="<c:url value="/css/register.css" />">
</head>
<body>
<div class="container">
    <div class="appointment-card">
        <h1 class="title">Register</h1>
        <p class="subtitle">Fill out the form below</p>

        <form:form modelAttribute="registerForm" method="post" action="${pageContext.request.contextPath}/register">
            <div class="form-row">
                <div class="form-group">
                    <label for="name">First Name</label>
                    <form:input path="name" id="name" class="form-control" />
                    <form:errors path="name" cssClass="error-message" />
                </div>
                <div class="form-group">
                    <label for="lastName">Last Name</label>
                    <form:input path="lastName" id="lastName" class="form-control" />
                    <form:errors path="lastName" cssClass="error-message" />
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="email">Email</label>
                    <form:input path="email" id="email" type="email" class="form-control" />
                    <form:errors path="email" cssClass="error-message" />
                </div>
                <div class="form-group">
                    <label for="phone">Phone</label>
                    <form:input path="phone" id="phone" class="form-control" />
                    <form:errors path="phone" cssClass="error-message" />
                </div>
            </div>

<%--            <div class="form-group">--%>
<%--                <label for="specialty">Specialty</label>--%>
<%--                <form:input path="specialty" id="specialty" class="form-control" />--%>
<%--                <form:errors path="specialty" cssClass="error-message" />--%>
<%--            </div>--%>

<%--            <div class="form-group">--%>
<%--                <label for="coverages">Coverage</label>--%>
<%--                <form:select path="coverages" id="coverages" class="form-control">--%>
<%--                    <option value="">Select your coverage</option>--%>
<%--                    <c:forEach items="${coverageList}" var="coverage">--%>
<%--                        <option value="${coverage.id}">${coverage.name}</option>--%>
<%--                    </c:forEach>--%>
<%--                </form:select>--%>
<%--                <form:errors path="coverages" cssClass="error-message" />--%>
<%--            </div>--%>

            <div class="form-group">
                <button type="submit" class="btn-primary">Register</button>
            </div>
        </form:form>
    </div>
</div>
</body>
</html>