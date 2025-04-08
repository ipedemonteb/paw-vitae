<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ attribute name="title" required="true" type="java.lang.String" %>

<section class="features-section">
  <div class="container">
    <h2 class="section-title"><spring:message code="${fn:escapeXml(title)}"/></h2>
    <div class="features-grid">
      <jsp:doBody />
    </div>
  </div>
</section>