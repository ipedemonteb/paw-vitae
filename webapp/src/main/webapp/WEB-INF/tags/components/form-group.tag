<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ attribute name="path" required="true" type="java.lang.String" %>
<%@ attribute name="label" required="true" type="java.lang.String" %>
<%@ attribute name="type" required="false" type="java.lang.String"%>
<%@ attribute name="placeholder" required="false" type="java.lang.String" %>

<div class="form-group">
    <label for="${path}"><spring:message code="${label}" /></label>
    <c:choose>
        <c:when test="${type eq 'textarea'}">
            <form:textarea path="${path}" id="${path}" class="form-control" placeholder="${placeholder}" />
        </c:when>
        <c:otherwise>
            <form:input path="${path}" id="${path}" type="${type}" class="form-control" placeholder="${placeholder}" />
        </c:otherwise>
    </c:choose>
    <form:errors path="${path}" cssClass="error-message" />
</div>