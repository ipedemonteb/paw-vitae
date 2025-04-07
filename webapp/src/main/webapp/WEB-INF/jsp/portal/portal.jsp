<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="comp" tagdir="/WEB-INF/tags/components" %>


<layout:page title="portal.title">
<comp:headerPortal title="portal.heading"/>

<div class="card">
    <div class="card-header">
        <h2 class="card-title text-center"><spring:message code="portal.selectSpecialty" /></h2>
    </div>

    <div class="card-body">
        <div class="grid grid-cols-1 grid-cols-2 grid-cols-3">
            <c:forEach items="${specialties}" var="specialty">
                <a href="<c:url value='/search?specialty=${specialty.id}' />" class="specialty-card">
                    <div class="specialty-name">
                        <spring:message code="${specialty.key}"/>
                    </div>
                </a>
            </c:forEach>
        </div>
    </div>
</div>
</layout:page>