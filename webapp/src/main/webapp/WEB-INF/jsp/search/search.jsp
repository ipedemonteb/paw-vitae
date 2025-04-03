<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>Médicos Especialistas</title>
  <style>
    body { font-family: Arial, sans-serif; margin: 20px; }
    h2 { color: #333; }
    table { width: 100%; border-collapse: collapse; margin-top: 20px; }
    th, td { border: 1px solid #ddd; padding: 10px; text-align: left; }
    th { background-color: #f2f2f2; }
  </style>
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
      </tr>
      <c:forEach var="doctor" items="${doctors}">
        <tr>
          <td>${doctor.name}</td>
          <td>${doctor.lastName}</td>
          <td>${doctor.email}</td>
          <td>${doctor.phone}</td>
        </tr>
      </c:forEach>
    </table>
  </c:otherwise>
</c:choose>

</body>
</html>