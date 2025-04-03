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

        <form:form modelAttribute="registerForm" method="post" action="${pageContext.request.contextPath}/register" enctype="multipart/form-data">
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

            <div class="form-row">
                <div class="form-group">
                    <label for="password">Password</label>
                    <form:password path="password" id="password" class="form-control" />
                    <form:errors path="password" cssClass="error-message" />
                </div>
                <div class="form-group">
                    <label for="repeatPassword">Confirm Password</label>
                    <form:password path="repeatPassword" id="repeatPassword" class="form-control" />
                    <form:errors path="repeatPassword" cssClass="error-message" />
                </div>
            </div>

            <div class="form-group">
                <label for="specialty">Specialties</label>
                <div class="specialty-select-container">
                    <input type="checkbox" id="dropdown-toggle" class="dropdown-toggle">
                    <label for="dropdown-toggle" class="specialty-select-trigger">Select specialties</label>

                    <div class="specialty-select">
                        <c:forEach items="${specialtyList}" var="specialty2">
                            <div class="specialty-option">
                                <input type="checkbox" id="specialty-${specialty2}" name="specialty" value="${specialty2}" class="specialty-checkbox">
                                <label for="specialty-${specialty2}" class="specialty-label">${specialty2}</label>
                            </div>
                        </c:forEach>
                    </div>

                    <!-- Select oculto para mantener la compatibilidad con el backend -->
                    <form:select path="specialty" id="specialty" class="hidden-select" multiple="true">
                        <c:forEach items="${specialtyList}" var="specialty2">
                            <option value="${specialty2}">${specialty2}</option>
                        </c:forEach>
                    </form:select>
                </div>
                <form:errors path="specialty" cssClass="error-message" />
            </div>

            <div class="form-group">
                <label for="coverages">Coverage</label>
                <div class="specialty-select-container">
                    <input type="checkbox" id="coverage-dropdown-toggle" class="dropdown-toggle">
                    <label for="coverage-dropdown-toggle" class="specialty-select-trigger">Select your coverage</label>

                    <!-- Lista de opciones personalizada -->
                    <div class="specialty-select">
                        <c:forEach items="${coverageList}" var="coverage">
                            <div class="specialty-option">
                                <input type="checkbox" id="coverage-${coverage.id}" name="coverages" value="${coverage.id}" class="specialty-checkbox">
                                <label for="coverage-${coverage.id}" class="specialty-label">${coverage.name}</label>
                            </div>
                        </c:forEach>
                    </div>

                    <!-- Select oculto para mantener la compatibilidad con el backend -->
                    <form:select path="coverages" id="coverages" class="hidden-select" multiple="true">
                        <c:forEach items="${coverageList}" var="coverage">
                            <option value="${coverage.id}">${coverage.name}</option>
                        </c:forEach>
                    </form:select>
                </div>
                <form:errors path="coverages" cssClass="error-message" />
                <div class="form-group">
                    <label for="image">Subir imagen:</label>
                    <form:input type="file" path="image" id="image" accept="image/*" />
                    <form:errors path="image" cssClass="error-message" />
                </div>
            </div>

            <div class="form-group">
                <button type="submit" class="btn-primary">Register</button>
            </div>
        </form:form>
    </div>
</div>
</body>
</html>

