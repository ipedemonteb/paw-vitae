<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <link rel="stylesheet" href="<c:url value='/css/doctor.css' />">
    <title>Doctor Profile</title>
</head>
<body>
<div class="doctor-profile">
    <div class="doctor-image">
        <img src="<c:url value="/doctor/${doctor.id}/image"/>" alt="${doctor.name} ${doctor.lastName}"/>'">
    </div>
    <div class="doctor-info">
        <h2><spring:message code="index.greeting" arguments="${doctor.email}" /></h2>
        <h4><spring:message code="index.subtitle" arguments="${doctor.id}" /></h4>
        <!-- Add more doctor information here if needed -->
    </div>
</div>
</body>
</html>