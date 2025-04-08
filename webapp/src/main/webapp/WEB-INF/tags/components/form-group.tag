<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ attribute name="path" required="true" type="java.lang.String" %>
<%@ attribute name="label" required="true" type="java.lang.String" %>
<%@ attribute name="type" required="false" type="java.lang.String"%>
<%@ attribute name="placeholder" required="false" type="java.lang.String" %>

<div class="form-group">
    <label for="${fn:escapeXml(path)}"><spring:message code="${fn:escapeXml(label)}" /></label>
    <c:choose>
        <c:when test="${type eq 'textarea'}">
            <form:textarea
                    path="${fn:escapeXml(path)}"
                    id="${fn:escapeXml(path)}"
                    class="form-control"
                    placeholder="${fn:escapeXml(placeholder)}" />
        </c:when>
        <c:otherwise>
            <form:input
                    path="${fn:escapeXml(path)}"
                    id="${fn:escapeXml(path)}"
                    type="${fn:escapeXml(type)}"
                    class="form-control"
                    placeholder="${fn:escapeXml(placeholder)}" />
        </c:otherwise>
    </c:choose>
    <form:errors path="${fn:escapeXml(path)}" cssClass="error-message" />
</div>