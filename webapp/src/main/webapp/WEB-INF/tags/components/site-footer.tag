<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<footer class="site-footer">
  <div class="container">
    <div class="footer-links">
      <a href="#" class="footer-link"><spring:message code="footer.terms" /></a>
      <a href="#" class="footer-link"><spring:message code="footer.privacy" /></a>
      <a href="#" class="footer-link"><spring:message code="footer.contact" /></a>
    </div>
    <p>&copy; <spring:message code="copyright" /></p>
  </div>
</footer>