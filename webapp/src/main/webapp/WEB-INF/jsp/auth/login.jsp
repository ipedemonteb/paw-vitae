<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="comp" tagdir="/WEB-INF/tags/components" %>

<layout:page title="login.title">
  <div class="card">
    <div class="card-header">
      <h1 class="card-title"><spring:message code="login.title" /></h1>
      <p class="card-subtitle"><spring:message code="login.subtitle" /></p>
    </div>

    <div class="card-body">
      <c:if test="${param.error != null}">
        <div class="alert alert-danger">
          <spring:message code="login.error" />
        </div>
      </c:if>
      <c:if test="${param.logout != null}">
        <div class="alert alert-success">
          <spring:message code="login.logout.success" />
        </div>
      </c:if>

      <c:url value="/login" var="loginUrl" />
      <form action="${loginUrl}" method="post" enctype="application/x-www-form-urlencoded">
        <div class="form-group">
          <label for="email"><spring:message code="login.email" /></label>
          <input id="email" name="j_email" type="email" class="form-control" required />
        </div>

        <div class="form-group">
          <div class="d-flex justify-content-between align-items-center">
            <label for="password"><spring:message code="login.password" /></label>
            <a href="#" class="text-sm">
              <spring:message code="login.forgotPassword" />
            </a>
          </div>
          <input id="password" name="j_password" type="password" class="form-control" required />
        </div>

        <div class="form-group">
          <div class="checkbox">
            <label>
              <input name="j_rememberme" type="checkbox" />
              <spring:message code="login.rememberMe" />
            </label>
          </div>
        </div>

        <div class="form-group">
          <button type="submit" class="btn btn-primary btn-block">
            <spring:message code="login.button" />
          </button>
        </div>
      </form>
    </div>

    <div class="card-footer text-center">
      <p>
        <spring:message code="login.noAccount" />
        <a href="<c:url value='/register' />">
          <spring:message code="login.register" />
        </a>
      </p>
    </div>
  </div>
</layout:page>
