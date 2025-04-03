<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <link rel="stylesheet" href="<c:url value='/css/search.css' />">
  <title>Médicos Especialistas</title>
</head>
<body>

<h2>Médicos de la especialidad: ${specialty}</h2>

<c:choose>
  <c:when test="${empty doctors}">
    <p>No hay médicos disponibles para esta especialidad.</p>
  </c:when>
  <c:otherwise>
    <table>
      <tr>
        <th>Nombre</th>
        <th>Apellido</th>
        <th>Email</th>
        <th>Teléfono</th>
        <th>Acción</th>
      </tr>
      <c:forEach var="doctor" items="${doctors}">
        <tr>
          <td>${doctor.name}</td>
          <td>${doctor.lastName}</td>
          <td>${doctor.email}</td>
          <td>${doctor.phone}</td>
          <td>
            <a href="<c:url value='/appointment?doctorId=${doctor.id}' />" class="btn-appointment">Agendar Cita</a>
          </td>
        </tr>
      </c:forEach>
    </table>
  </c:otherwise>
</c:choose>

</body>
</html>