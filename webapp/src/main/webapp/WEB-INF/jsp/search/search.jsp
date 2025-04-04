<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="<c:url value='/css/search.css'/>">
  <title><spring:message code="search.page.title" /></title>
</head>
<body>
<div class="container">
  <div class="search-card">
    <h1 class="title"><spring:message code="search.title" /></h1>
    <h2 class="subtitle"><spring:message code="search.doctors.specialty" arguments="${specialty}" /></h2>

    <c:choose>
      <c:when test="${empty doctors}">
        <div class="empty-message">
          <spring:message code="search.no.doctors.available" />
        </div>
      </c:when>
      <c:otherwise>
        <table class="doctors-table">
          <tr>
            <th><spring:message code="search.table.name" /></th>
            <th><spring:message code="search.table.lastName" /></th>
            <th><spring:message code="search.table.email" /></th>
            <th><spring:message code="search.table.phone" /></th>
            <th><spring:message code="search.table.action" /></th>
          </tr>
          <c:forEach var="doctor" items="${doctors}">
            <tr>
              <td>${doctor.name}</td>
              <td>${doctor.lastName}</td>
              <td>${doctor.email}</td>
              <td>${doctor.phone}</td>
              <td>
                <a href="<c:url value='/appointment?doctorId=${doctor.id}' />" class="btn-appointment">
                  <spring:message code="search.button.schedule" />
                </a>
              </td>
            </tr>
          </c:forEach>
        </table>
      </c:otherwise>
    </c:choose>

    <div class="actions">
      <a href="<c:url value='/portal' />" class="back-link">
        <spring:message code="search.link.back.home" />
      </a>
    </div>
  </div>
</div>
</body>
</html>