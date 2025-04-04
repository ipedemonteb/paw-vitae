<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ attribute name="title" required="true" type="java.lang.String" %>
<%@ attribute name="cssFiles" required="false" type="java.lang.String" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="${title}" /></title>
    <link rel="stylesheet" href="<c:url value='/css/main.css' />">
    <c:if test="${not empty cssFiles}">
        <c:forEach var="cssFile" items="${cssFiles}">
            <link rel="stylesheet" href="<c:url value='/css/${cssFile}' />">
        </c:forEach>
    </c:if>
</head>
<body>
    <div class="container">
        <jsp:doBody />
    </div>
</body>
</html>