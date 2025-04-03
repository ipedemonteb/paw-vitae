<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title>Doctor Profile</title>
    <style>
        .doctor-profile {
            display: flex;
            flex-direction: column;
            align-items: center;
            margin: 20px;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 8px;
        }
        .doctor-image {
            width: 200px;
            height: 200px;
            border-radius: 50%;
            overflow: hidden;
            margin-bottom: 20px;
        }
        .doctor-image img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }
        .doctor-info {
            text-align: center;
        }
    </style>
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