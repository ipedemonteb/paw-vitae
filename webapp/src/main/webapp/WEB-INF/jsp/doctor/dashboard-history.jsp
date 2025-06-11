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
    <title><spring:message code="dashboard.history.title" /></title>
    <link rel="stylesheet" href="<c:url value='/css/doctor-dashboard.css' />" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
<!-- Include the header -->
<jsp:include page="/WEB-INF/jsp/components/header.jsp">
    <jsp:param name="id" value="${doctor.imageId}" />
    <jsp:param name="doctorId" value="${doctor.id}" />
</jsp:include>

<main class="dashboard-container">

    <c:set var="activeTab" value="history" scope="request" />
    <c:set var="user" value="${doctor}" scope="request"/>
    <c:set var="isDoctor" value="${true}" scope="request"/>
    <jsp:include page="/WEB-INF/jsp/components/dashboard-header.jsp"/>

    <!-- Dashboard Content Area -->
    <div class="dashboard-content">
        <!-- Appointment History Tab -->
        <div class="tab-content active" id="history-tab">
            <div class="tab-header">
                <h2><spring:message code="dashboard.history.title" /></h2>
                <div class="tab-actions">
                    <div class="status-filter">
                        <label for="history-status-filter"><spring:message code="dashboard.filter.status" />:</label>
                        <select id="history-status-filter" class="filter-select" onchange="applyStatusFilter(this.value)">
                            <option value="all" ${param.status == 'all' || empty param.status ? 'selected' : ''}><spring:message code="dashboard.history.all" /></option>
                            <option value="completed" ${param.status == 'completed' ? 'selected' : ''}><spring:message code="appointment.status.completed" /></option>
                            <option value="cancelled" ${param.status == 'cancelled' ? 'selected' : ''}><spring:message code="appointment.status.cancelled" /></option>
                        </select>
                    </div>
<%--                    <div class="search-container">--%>
<%--                        <button class="search-button">--%>
<%--                            <i class="fas fa-search"></i>--%>
<%--                        </button>--%>
<%--                        <input type="text" class="search-input" placeholder="<spring:message code="dashboard.search.placeholder" />" />--%>
<%--                    </div>--%>
                </div>
            </div>

            <c:choose>
                <c:when test="${not empty pastAppointments}">
                    <div class="appointments-list">
                        <c:forEach items="${pastAppointments}" var="appointment">
                            <div class="appointment-card past" data-id="${appointment.id}" data-status="<spring:message code='${appointment.status}'/>" data-date="${appointment.date}">
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
                                            ${appointment.date.hour}:00
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
                                    <c:if test="${not empty appointment.reason}">
                                        <div class="appointment-reason">
                                            <div class="reason-label"><spring:message code="appointment.reason" />:</div>
                                            <div class="reason-text"><c:out value="${appointment.reason}" /></div>
                                        </div>
                                    </c:if>
                                    <c:if test="${empty appointment.reason}">
                                        <div class="appointment-reason">
                                            <div class="reason-text"><spring:message code="appointment.no.reason" /></div>
                                        </div>
                                    </c:if>
                                    <div class="appointment-specialty">
                                        <span class="specialty-badge">
                                            <spring:message code="${appointment.specialty.key}" />
                                        </span>
                                        <div class="appointment-actions">
                                            <div class="appointment-actions">
                                                <a class="btn btn-primary view-appointment" href="<c:url value='/doctor/dashboard/appointment-details/${appointment.id}' />">
                                                    <i class="fas fa-eye"></i>
                                                    <span><spring:message code="appointment.details" /></span>
                                                </a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                        <!-- Pagination -->
                        <c:if test="${totalPages > 1}">
                            <div class="pagination">
                                <c:if test="${currentPage > 1}">
                                    <a href="<c:url value='/doctor/dashboard/history?page=${currentPage - 1}&status=${param.status != null ? param.status : "all"}' />" class="pagination-btn">
                                        <i class="fas fa-chevron-left"></i>
                                    </a>
                                </c:if>

                                <div class="pagination-numbers">
                                    <c:set var="startPage" value="${Math.max(1, currentPage - 2)}" />
                                    <c:set var="endPage" value="${Math.min(totalPages, startPage + 4)}" />
                                    <c:if test="${startPage > 1}">
                                        <a href="<c:url value='/doctor/dashboard/history?page=1&status=${param.status != null ? param.status : "all"}' />" class="pagination-number">1</a>
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
                                                <a href="<c:url value='/doctor/dashboard/history?page=${i}&status=${param.status != null ? param.status : "all"}' />" class="pagination-number">${i}</a>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>

                                    <c:if test="${endPage < totalPages}">
                                        <c:if test="${endPage < totalPages - 1}">
                                            <span class="pagination-ellipsis">...</span>
                                        </c:if>
                                        <a href="<c:url value='/doctor/dashboard/history?page=${totalPages}&status=${param.status != null ? param.status : "all"}' />" class="pagination-number">${totalPages}</a>
                                    </c:if>
                                </div>

                                <c:if test="${currentPage < totalPages}">
                                    <a href="<c:url value='/doctor/dashboard/history?page=${currentPage + 1}&status=${param.status != null ? param.status : "all"}' />" class="pagination-btn">
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
                            <i class="fas fa-history fa-3x"></i>
                        </div>
                        <h3><spring:message code="dashboard.history.empty.title" /></h3>
                        <p><spring:message code="dashboard.history.empty.message" /></p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</main>

<!-- Modales comunes -->
<div id="confirmAppointmentModal" class="remove-modal-overlay">
    <div class="modal-container">
        <div class="remove-modal-header">
            <div class="modal-icon">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
                    <path d="M12 22C6.477 22 2 17.523 2 12S6.477 2 12 2s10 4.477 10 10-4.477 10-10 10zm0-2a8 8 0 1 0 0-16 8 8 0 0 0 0 16zm0-9a1 1 0 0 1 1 1v4a1 1 0 0 1-2 0v-4a1 1 0 0 1 1-1zm0-4a1 1 0 1 1 0 2 1 1 0 0 1 0-2z"></path>
                </svg>
            </div>
            <h3 class="modal-title"><spring:message code="appointment.confirm.title" /></h3>
        </div>
        <div class="remove-modal-body">
            <p class="modal-message"><spring:message code="appointment.confirm.message" /></p>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn-modal btn-cancel" onclick="hideConfirmModal();">
                <spring:message code="logout.confirmation.cancel"/>
            </button>
            <button type="button" class="btn-modal btn-confirm" id="confirmAppointmentBtn">
                <spring:message code="appointment.action.confirm"/>
            </button>
        </div>
    </div>
</div>

<div id="cancelAppointmentModal" class="remove-modal-overlay">
    <div class="modal-container">
        <div class="remove-modal-header">
            <div class="modal-icon">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
                    <path d="M12 22C6.477 22 2 17.523 2 12S6.477 2 12 2s10 4.477 10 10-4.477 10-10 10zm0-2a8 8 0 1 0 0-16 8 8 0 0 0 0 16zm0-9a1 1 0 0 1 1 1v4a1 1 0 0 1-2 0v-4a1 1 0 0 1 1-1zm0-4a1 1 0 1 1 0 2 1 1 0 0 1 0-2z"></path>
                </svg>
            </div>
            <h3 class="modal-title"><spring:message code="appointment.cancel.title" /></h3>
        </div>
        <div class="remove-modal-body">
            <p class="modal-message"><spring:message code="appointment.cancel.message" /></p>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn-modal btn-cancel" onclick="hideCancelModal();">
                <spring:message code="logout.confirmation.cancel"/>
            </button>
            <button type="button" class="btn-modal btn-danger" id="cancelAppointmentBtn">
                <spring:message code="appointment.action.cancel"/>
            </button>
        </div>
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Apply status filter function
        window.applyStatusFilter = function(value) {
            window.location.href = '${pageContext.request.contextPath}/doctor/dashboard/history?status=' + value;
        };

        // Search functionality
        const searchInput = document.querySelector('.search-input');
        if (searchInput) {
            searchInput.addEventListener('input', function() {
                // Filter appointments based on search input
                const searchTerm = this.value.toLowerCase();
                const appointmentCards = document.querySelectorAll('#history-tab .appointment-card');

                appointmentCards.forEach(card => {
                    const patientName = card.querySelector('.patient-name').textContent.toLowerCase();
                    const reasonText = card.querySelector('.reason-text')?.textContent.toLowerCase() || '';

                    if (patientName.includes(searchTerm) || reasonText.includes(searchTerm)) {
                        card.style.display = 'flex';
                    } else {
                        card.style.display = 'none';
                    }
                });
            });
        }

        // Función para inicializar filtros y eventos en las citas cargadas
        function initializeFiltersAndEvents() {
            // Aplicar la búsqueda actual a las nuevas citas
            const searchInput = document.querySelector('.search-input');
            if (searchInput && searchInput.value.trim() !== '') {
                const searchTerm = searchInput.value.toLowerCase();
                const appointmentCards = document.querySelectorAll('#history-tab .appointment-card');

                appointmentCards.forEach(card => {
                    const patientName = card.querySelector('.patient-name').textContent.toLowerCase();
                    const reasonText = card.querySelector('.reason-text')?.textContent.toLowerCase() || '';

                    if (patientName.includes(searchTerm) || reasonText.includes(searchTerm)) {
                        card.style.display = 'flex';
                    } else {
                        card.style.display = 'none';
                    }
                });
            }
        }
    });
</script>
</body>
</html>
