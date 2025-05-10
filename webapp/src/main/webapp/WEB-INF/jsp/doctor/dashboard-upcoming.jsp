<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="icon" type="image/png" href="https://upload.wikimedia.org/wikipedia/commons/thumb/a/aa/V-logo.svg/2048px-V-logo.svg.png" />
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="dashboard.upcoming.title" /></title>
    <link rel="stylesheet" href="<c:url value='/css/doctor-dashboard.css' />" />
    <link rel="stylesheet" href="<c:url value='/css/toast-notification.css' />" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
<!-- Include the header -->
<jsp:include page="/WEB-INF/jsp/components/header.jsp">
    <jsp:param name="id" value="${doctor.imageId}" />
</jsp:include>

<c:set var="validRanges" value="today,week,month,all" />
<c:choose>
    <c:when test="${fn:contains(validRanges, param.dateRange)}">
        <c:set var="selectedDateRange" value="${param.dateRange}" />
    </c:when>
    <c:otherwise>
        <c:set var="selectedDateRange" value="all" />
    </c:otherwise>
</c:choose>


<div id="successToast" class="success-toast">
    <div class="success-toast-icon">
        <i class="fas fa-check"></i>
    </div>
    <div class="success-toast-content">
        <div class="success-toast-title"><spring:message code="appointment.cancel.success.title"/></div>
        <div class="success-toast-message"><spring:message code="appointment.cancel.success.message"/></div>
    </div>
    <button class="success-toast-close" onclick="hideSuccessToast()">
        <i class="fas fa-times"></i>
    </button>
</div>


<div id="errorToast" class="error-toast">
    <div class="error-toast-icon">
        <i class="fas fa-check"></i>
    </div>
    <div class="error-toast-content">
        <div class="error-toast-title"><spring:message code="appointment.cancel.error.title"/></div>
        <div class="error-toast-message"><spring:message code="appointment.cancel.error.message"/></div>
    </div>
    <button class="error-toast-close" onclick="hideErrorToast()">
        <i class="fas fa-times"></i>
    </button>
</div>


<div class="dashboard-container">
    <!-- Doctor Profile Header -->
    <div class="dashboard-header">
        <div class="doctor-info">
            <div class="doctor-avatar">
                <img src="<c:url value="/image/${doctor.imageId}"/>" alt="<c:out value="${doctor.name} ${doctor.lastName}"/>"/>
            </div>
            <div class="doctor-details">
                <h1 class="doctor-name"><c:out value="${doctor.name}" /> <c:out value="${doctor.lastName}" /></h1>
                <div class="doctor-meta">
                    <div class="doctor-meta-item">
                        <i class="fas fa-envelope"></i>
                        <span><c:out value="${doctor.email}" /></span>
                    </div>
                    <div class="doctor-meta-item">
                        <i class="fas fa-phone"></i>
                        <span><c:out value="${doctor.phone}" /></span>
                    </div>
                </div>
                <c:if test="${doctor.ratingCount > 0}">
                    <div class="doctor-rating">
                        <div class="rating-stars">
                            <c:forEach begin="1" end="5" var="i">
                                <c:choose>
                                    <c:when test="${doctor.rating >= i}">
                                        <i class="fas fa-star"></i>
                                    </c:when>
                                    <c:when test="${doctor.rating >= i - 0.5}">
                                        <i class="fas fa-star-half-alt"></i>
                                    </c:when>
                                    <c:otherwise>
                                        <i class="far fa-star"></i>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </div>
                        <div class="rating-value">
                            <fmt:formatNumber value="${doctor.rating}" pattern="#.#" /> <span class="rating-count">(${doctor.ratingCount})</span>
                        </div>
                    </div>
                </c:if>
                <div class="doctor-specialties">
                    <c:forEach items="${doctor.specialtyList}" var="specialty" varStatus="status">
                        <span class="specialty-tag">
                            <c:choose>
                                <c:when test="${not empty specialty.key}">
                                    <spring:message code="${specialty.key}" />
                                </c:when>
                                <c:otherwise>
                                    <spring:message code="${specialty.key}" />
                                </c:otherwise>
                            </c:choose>
                        </span>
                    </c:forEach>
                </div>
            </div>
        </div>
        <div class="dashboard-stats">
            <div class="stat-item">
                <div class="stat-value">${doctor.specialtyList.size()}</div>
                <div class="stat-label"><spring:message code="dashboard.stats.specialties" /></div>
            </div>
        </div>
    </div>

    <!-- Dashboard Navigation Tabs -->
    <div class="dashboard-nav">
        <a href="<c:url value='/doctor/dashboard/upcoming'/>" class="nav-tab active">
            <i class="fas fa-calendar-alt"></i>
            <span><spring:message code="dashboard.tab.upcoming" /></span>
        </a>
        <a href="<c:url value='/doctor/dashboard/history'/>" class="nav-tab ">
            <i class="fas fa-history"></i>
            <span><spring:message code="dashboard.tab.history" /></span>
        </a>
        <a href="<c:url value='/doctor/dashboard/profile'/>" class="nav-tab">
            <i class="fas fa-user-md"></i>
            <span><spring:message code="dashboard.tab.profile" /></span>
        </a>
        <a href="<c:url value='/doctor/dashboard/availability'/>" class="nav-tab ">
            <i class="fas fa-calendar-check"></i>
            <span><spring:message code="dashboard.tab.availability" /></span>
        </a>
    </div>

    <!-- Dashboard Content Area -->
    <div class="dashboard-content">
        <!-- Upcoming Appointments Tab -->
        <div class="tab-content active" id="upcoming-tab">
            <div class="tab-header">
                <h2><spring:message code="dashboard.upcoming.title" /></h2>
                <div class="tab-actions">
                    <div class="date-filter">
                        <label for="date-range"><spring:message code="dashboard.filter.dateRange" />:</label>
                        <select id="date-range" class="filter-select" onchange="applyDateFilter(this.value)">
                            <option value="today" ${selectedDateRange == 'today' ? 'selected' : ''}><spring:message code="dashboard.filter.today" /></option>
                            <option value="week" ${selectedDateRange == 'week' ? 'selected' : ''}><spring:message code="dashboard.filter.thisWeek" /></option>
                            <option value="month" ${selectedDateRange == 'month' ? 'selected' : ''}><spring:message code="dashboard.filter.thisMonth" /></option>
                            <option value="all" ${selectedDateRange == null || selectedDateRange == 'all' ? 'selected' : ''}><spring:message code="dashboard.filter.all" /></option>
                        </select>
                    </div>
                </div>
            </div>

            <c:choose>
                <c:when test="${not empty upcomingAppointments}">
                    <div class="appointments-list">
                        <c:forEach items="${upcomingAppointments}" var="appointment">
                            <div class="appointment-card" data-id="${appointment.id}" data-status="<spring:message code='${appointment.status}'/>" data-date="<c:out value="${appointment.date}"/>">
                                <div class="appointment-time">
                                    <div class="appointment-date">
                                        <span class="day">
                                            <spring:message code="${appointment.date.dayOfWeek}" />
                                        </span>
                                        <span class="date-number">
                                            <c:out value="${appointment.date.dayOfMonth}"/>
                                        </span>
                                        <span class="month">
                                            <spring:message code="${appointment.date.month}" />
                                        </span>
                                    </div>
                                    <div class="appointment-hour">
                                        <i class="fas fa-clock"></i>
                                        <c:out value="${appointment.date.hour}"/>:00
                                    </div>
                                    <div class="appointment-status-indicator">
                                        <span class="status-badge ${appointment.status}">
                                            <spring:message code='${appointment.status}'/>
                                        </span>
                                    </div>
                                </div>
                                <div class="appointment-details">
                                    <div class="patient-info">
                                        <div class="patient-avatar">
                                            <div class="avatar-placeholder small">
                                                <c:out value="${fn:substring(appointment.patient.name, 0, 1)}${fn:substring(appointment.patient.lastName, 0, 1)}"/>
                                            </div>
                                        </div>
                                        <div>
                                            <div class="patient-name">
                                                <c:out value="${appointment.patient.name}" /> <c:out value="${appointment.patient.lastName}" />
                                            </div>
                                            <div class="patient-coverage">
                                                <c:out value="${appointment.patient.coverage.name}" />
                                            </div>
                                        </div>
                                    </div>
                                    <div class="appointment-reason">
                                        <div class="reason-label"><spring:message code="appointment.reason" />:</div>
                                        <div class="reason-text"><c:out value="${appointment.reason}" /></div>
                                    </div>
                                    <div class="appointment-specialty">
                                        <span class="specialty-badge">
                                            <spring:message code="${appointment.specialty.key}" />
                                        </span>
                                        <c:set var="status" >
                                            <spring:message code="${appointment.status}" />
                                        </c:set>
                                        <c:set var="pending">
                                            <spring:message code="appointment.status.pending" />
                                        </c:set>
                                        <c:set var="confirmed">
                                            <spring:message code="appointment.status.confirmed" />
                                        </c:set>
                                        <c:set var="all">
                                            <spring:message code="dashboard.filter.all" />
                                        </c:set>

                                        <div class="appointment-actions">

                                            <c:if test="${status eq confirmed && appointment.cancellable}">
                                                <button class="btn btn-danger cancel-appointment" data-id="${appointment.id}">
                                                    <i class="fas fa-times-circle"></i>
                                                    <span><spring:message code="appointment.action.cancel" /></span>
                                                </button>
                                            </c:if>

                                            <button class="btn btn-primary" onclick="window.location.href='/doctor/dashboard/appointment-details/${appointment.id}'">
                                                <i class="fas fa-eye"></i>
                                                <span><spring:message code="appointment.details" /></span>
                                            </button>
                                        </div>

                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                        <!-- Pagination -->
                        <c:if test="${totalPages > 1}">
                            <div class="pagination">
                                <c:if test="${currentPage > 1}">
                                    <a href="<c:url value='/doctor/dashboard/upcoming?page=${currentPage - 1}&dateRange=${param.dateRange != null ? param.dateRange : "all"}' />" class="pagination-btn">
                                        <i class="fas fa-chevron-left"></i>
                                    </a>
                                </c:if>

                                <div class="pagination-numbers">
                                    <c:set var="startPage" value="${Math.max(1, currentPage - 2)}" />
                                    <c:set var="endPage" value="${Math.min(totalPages, startPage + 4)}" />
                                    <c:if test="${startPage > 1}">
                                        <a href="<c:url value='/doctor/dashboard/upcoming?page=1&dateRange=${param.dateRange != null ? param.dateRange : "all"}' />" class="pagination-number">1</a>
                                        <c:if test="${startPage > 2}">
                                            <span class="pagination-ellipsis">...</span>
                                        </c:if>
                                    </c:if>

                                    <c:forEach begin="${startPage}" end="${endPage}" var="i">
                                        <c:choose>
                                            <c:when test="${i == currentPage}">
                                                <span class="pagination-number active">${i}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="<c:url value='/doctor/dashboard/upcoming?page=${i}&dateRange=${param.dateRange != null ? param.dateRange : "all"}' />" class="pagination-number">${i}</a>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>

                                    <c:if test="${endPage < totalPages}">
                                        <c:if test="${endPage < totalPages - 1}">
                                            <span class="pagination-ellipsis">...</span>
                                        </c:if>
                                        <a href="<c:url value='/doctor/dashboard/upcoming?page=${totalPages}&dateRange=${param.dateRange != null ? param.dateRange : "all"}' />" class="pagination-number">${totalPages}</a>
                                    </c:if>
                                </div>

                                <c:if test="${currentPage < totalPages}">
                                    <a href="<c:url value='/doctor/dashboard/upcoming?page=${currentPage + 1}&dateRange=${param.dateRange != null ? param.dateRange : "all"}' />" class="pagination-btn">
                                        <i class="fas fa-chevron-right"></i>
                                    </a>
                                </c:if>
                            </div>
                        </c:if>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="empty-state">
                        <div class="empty-icon">
                            <i class="fas fa-calendar-day fa-3x"></i>
                        </div>
                        <h3><spring:message code="dashboard.upcoming.empty.title" /></h3>
                        <p><spring:message code="dashboard.upcoming.empty.message" /></p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/components/modal.jsp">
    <jsp:param name="id" value="cancelAppointmentModal"/>
    <jsp:param name="confirm" value="appointment.action.cancel"/>
    <jsp:param name="title" value="appointment.cancel.title"/>
    <jsp:param name="message" value="appointment.cancel.message"/>
    <jsp:param name="actionPath" value="${pageContext.request.contextPath}/doctor/dashboard/appointment/cancel"/>
    <jsp:param name="divId" value="cancelModal"/>
    <jsp:param name="formId" value="cancelForm"/>
    <jsp:param name="buttonId" value="cancelAppointmentBtn"/>
    <jsp:param name="buttonClass" value="cancel-appointment"/>
</jsp:include>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Apply date filter function
        window.applyDateFilter = function(value) {
            window.location.href = '${pageContext.request.contextPath}/doctor/dashboard/upcoming?dateRange=' + value;
        };

        const fixedHeader = document.querySelector(".main-header");
        const mainContent = document.querySelector(".dashboard-container");

        if (fixedHeader && mainContent) {
            const adjustContentMargin = () => {
                const headerHeight = fixedHeader.offsetHeight;
                mainContent.style.marginTop = (headerHeight * 1.25) + `px`;
            };

            // Adjust on page load
            adjustContentMargin();

            // Adjust on window resize
            window.addEventListener("resize", adjustContentMargin);
        }
        const urlParams = new URLSearchParams(window.location.search);
        if (urlParams.get('cancelled') === 'true') {
            showSuccessToast();

            // Remove the query parameter without refreshing the page
            const newUrl = window.location.pathname;
            window.history.replaceState({}, document.title, newUrl);
        }
        if(urlParams.get('cancelled')=== 'false'){
            showErrorToast();
            const newUrl = window.location.pathname;
            window.history.replaceState({}, document.title, newUrl);
        }
    });
</script>

<script src="<c:url value="/js/toast-notification.js"/> "></script>
</body>
</html>
