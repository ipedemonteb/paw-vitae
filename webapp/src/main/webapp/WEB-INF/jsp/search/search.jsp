<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags/layouts" %>

<t:page title="search.page.title">
<div class="card">
  <div class="card-header">
    <h1 class="card-title"><spring:message code="search.title" /></h1>
    <p class="card-subtitle"><spring:message code="search.doctors.specialty" arguments="${specialty}" /></p>
  </div>

  <div class="card-body">
    <c:choose>
      <c:when test="${empty doctors}">
        <div class="empty-message">
          <spring:message code="search.no.doctors.available" />
        </div>
      </c:when>
      <c:otherwise>
        <div class="table-responsive">
          <table class="table">
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
        </div>
      </c:otherwise>
    </c:choose>
  </div>

  <div class="card-footer">
    <a href="<c:url value='/portal' />" class="btn btn-outline">
      <spring:message code="search.link.back.home" />
    </a>
  </div>
</div>
</t:page>