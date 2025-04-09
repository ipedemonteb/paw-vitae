<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="registration.success.page.title"/></title>
    <link rel="stylesheet" href="<c:url value='/css/doctor.css' />">
    <link rel="stylesheet" href="<c:url value='/css/base.css' />">
</head>
<body>
<div class="container">
    <div class="success-card">
        <div class="success-header">
            <div class="success-icon">&#10004;</div>
            <h1><spring:message code="registration.success.title"/></h1>
            <p class="thank-you"><spring:message code="registration.success.message"/> ${doctor.lastName}</p>
        </div>

        <div class="doctor-profile">
            <div class="doctor-image">
                <img src="<c:url value="/doctor/${doctor.id}/image"/>" alt="${doctor.name} ${doctor.lastName}">
            </div>

            <div class="profile-details">
                <div class="detail-item">
                    <span class="label"><spring:message code="registration.success.name"/></span>
                    <span class="value">${doctor.name} ${doctor.lastName}</span>
                </div>

                <div class="detail-item">
                    <span class="label"><spring:message code="registration.success.email"/></span>
                    <span class="value">${doctor.email}</span>
                </div>

                <c:if test="${not empty doctor.phone}">
                    <div class="detail-item">
                        <span class="label"><spring:message code="registration.success.phone"/></span>
                        <span class="value">${doctor.phone}</span>
                    </div>
                </c:if>
            </div>
        </div>
        <div class="action-button">
            <a href="<c:url value='/' />"><spring:message code="appointment.confirmation.backToHome"/></a>
        </div>
    </div>
</div>
</body>
</html>