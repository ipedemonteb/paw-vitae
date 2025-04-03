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
    <title><spring:message code="register.title" /></title>
    <link rel="stylesheet" href="<c:url value="/css/register.css" />">
</head>
<body>
<div class="container">
    <div class="appointment-card">
        <h1 class="title"><spring:message code="register.title" /></h1>
        <p class="subtitle"><spring:message code="register.subtitle" /></p>

        <form:form modelAttribute="registerForm" method="post" action="${pageContext.request.contextPath}/register" enctype="multipart/form-data">
            <div class="form-row">
                <div class="form-group">
                    <label for="name"><spring:message code="register.firstName" /></label>
                    <form:input path="name" id="name" class="form-control" />
                    <form:errors path="name" cssClass="error-message" />
                </div>
                <div class="form-group">
                    <label for="lastName"><spring:message code="register.lastName" /></label>
                    <form:input path="lastName" id="lastName" class="form-control" />
                    <form:errors path="lastName" cssClass="error-message" />
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="email"><spring:message code="register.email" /></label>
                    <form:input path="email" id="email" type="email" class="form-control" />
                    <form:errors path="email" cssClass="error-message" />
                </div>
                <div class="form-group">
                    <label for="phone"><spring:message code="register.phone" /></label>
                    <form:input path="phone" id="phone" class="form-control" />
                    <form:errors path="phone" cssClass="error-message" />
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="password"><spring:message code="register.password" /></label>
                    <form:password path="password" id="password" class="form-control" />
                    <form:errors path="password" cssClass="error-message" />
                </div>
                <div class="form-group">
                    <label for="repeatPassword"><spring:message code="register.confirmPassword" /></label>
                    <form:password path="repeatPassword" id="repeatPassword" class="form-control" />
                    <form:errors path="repeatPassword" cssClass="error-message" />
                </div>
            </div>

            <div class="form-group">
                <label for="specialty"><spring:message code="register.specialties" /></label>
                <div class="specialty-select-container">
                    <input type="checkbox" id="dropdown-toggle" class="dropdown-toggle">
                    <label for="dropdown-toggle" class="specialty-select-trigger"><spring:message code="register.selectSpecialties" /></label>

                    <div class="specialty-select">
                        <c:forEach items="${specialtyList}" var="specialty2">
                            <div class="specialty-option">
                                <input type="checkbox" id="specialty-${specialty2}" name="specialty" value="${specialty2}" class="specialty-checkbox">
                                <label for="specialty-${specialty2}" class="specialty-label">${specialty2}</label>
                            </div>
                        </c:forEach>
                    </div>


                    <form:select path="specialty" id="specialty" class="hidden-select" multiple="true">
                        <c:forEach items="${specialtyList}" var="specialty2">
                            <option value="${specialty2}">${specialty2}</option>
                        </c:forEach>
                    </form:select>
                </div>
                <form:errors path="specialty" cssClass="error-message" />
            </div>

            <div class="form-group">
                <label for="coverages"><spring:message code="register.coverage" /></label>
                <div class="specialty-select-container">
                    <input type="checkbox" id="coverage-dropdown-toggle" class="dropdown-toggle">
                    <label for="coverage-dropdown-toggle" class="specialty-select-trigger"><spring:message code="register.selectCoverage" /></label>

                    <div class="specialty-select">
                        <c:forEach items="${coverageList}" var="coverage">
                            <div class="specialty-option">
                                <input type="checkbox" id="coverage-${coverage.id}" name="coverages" value="${coverage.id}" class="specialty-checkbox">
                                <label for="coverage-${coverage.id}" class="specialty-label">${coverage.name}</label>
                            </div>
                        </c:forEach>
                    </div>

                    <form:select path="coverages" id="coverages" class="hidden-select" multiple="true">
                        <c:forEach items="${coverageList}" var="coverage">
                            <option value="${coverage.id}">${coverage.name}</option>
                        </c:forEach>
                    </form:select>
                </div>
                <form:errors path="coverages" cssClass="error-message" />
                <div class="form-group">
                    <label for="image"><spring:message code="register.uploadImage" />:</label>
                    <form:input type="file" path="image" id="image" accept="image/*" />
                    <form:errors path="image" cssClass="error-message" />
                </div>
            </div>

            <div class="form-group">
                <button type="submit" class="btn-primary"><spring:message code="register.button" /></button>
            </div>
        </form:form>
    </div>
</div>
</body>
</html>