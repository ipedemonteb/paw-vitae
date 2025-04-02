<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<body>
<h2><spring:message code="index.greeting" arguments="${doctor.email}" /></h2>
<h4><spring:message code="index.subtitle" arguments="${doctor.id}" /></h4>
</body>
</html>