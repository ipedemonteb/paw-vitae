<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ attribute name="title" required="true" type="java.lang.String" %>
<%@ attribute name="description" required="true" type="java.lang.String" %>

<div class="feature-item">
  <h3><spring:message code="${fn:escapeXml(title)}" /></h3>
  <p><spring:message code="${fn:escapeXml(description)}" /></p>
</div>