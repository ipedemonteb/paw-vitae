<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="dashboard" tagdir="/WEB-INF/tags/components" %>

<dashboard:patient-dashboard>
    <!-- Upcoming Appointments Tab -->
    <div class="tab-content active" id="upcoming-tab">
        <div class="tab-header">
            <h2><spring:message code="dashboard.upcoming.title" /></h2>
            <div class="tab-actions">
                <div class="status-filter">
                    <label for="status-filter"><spring:message code="dashboard.filter.status" />:</label>
                    <select id="status-filter" class="filter-select">
                        <option value="<spring:message code="dashboard.filter.all" />" selected><spring:message code="dashboard.filter.all" /></option>
                        <option value="<spring:message code="appointment.status.pending" />"><spring:message code="appointment.status.pending" /></option>
                        <option value="<spring:message code="appointment.status.confirmed" />"><spring:message code="appointment.status.confirmed" /></option>
                        <option value="<spring:message code="appointment.status.cancelled" />"><spring:message code="appointment.status.cancelled" /></option>
                    </select>
                </div>
                <div class="date-filter">
                    <label for="date-range"><spring:message code="dashboard.filter.dateRange" />:</label>
                    <select id="date-range" class="filter-select">
                        <option value="today"><spring:message code="dashboard.filter.today" /></option>
                        <option value="week" selected><spring:message code="dashboard.filter.thisWeek" /></option>
                        <option value="month"><spring:message code="dashboard.filter.thisMonth" /></option>
                        <option value="all"><spring:message code="dashboard.filter.all" /></option>
                    </select>
                </div>
            </div>
        </div>

        <c:choose>
            <c:when test="${not empty upcomingAppointments}">
                <div class="appointments-list">
                    <c:forEach items="${upcomingAppointments}" var="appointment">
                        <div class="appointment-card" data-status="<spring:message code="${appointment.status}"/>" data-date="<c:out value="${appointment.date}"/>">
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
                                    <span class="status-badge <c:out value="${appointment.status}"/>">
                                        <spring:message code='${appointment.status}'/>
                                    </span>
                                </div>
                            </div>
                            <div class="appointment-details">
                                <div class="patient-info">
                                    <div class="patient-avatar">
                                        <img src="<c:url value="/doctor/${appointment.doctor.id}/image"/>" alt="<c:out value="${appointment.doctor.name} ${appointment.doctor.lastName}"/>"/>
                                    </div>
                                    <div>
                                        <div class="patient-name">
                                            <c:out value="${appointment.doctor.name}" /> <c:out value="${appointment.doctor.lastName}" />
                                        </div>
                                        <div class="patient-coverage">
                                            <spring:message code="${appointment.doctor.specialtyList[0].key}" />
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
                                </div>
                                <div class="appointment-actions">
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
                                    <c:if test="${status eq pending|| status eq confirmed}">
                                        <button class="btn btn-danger cancel-appointment" data-id="<c:out value="${appointment.id}"/>" id="cancel-appointment">
                                            <i class="fas fa-times-circle"></i>
                                            <span><spring:message code="appointment.action.cancel" /></span>
                                        </button>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
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

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Status filter functionality for upcoming appointments
            const statusFilter = document.getElementById('status-filter');
            if (statusFilter) {
                statusFilter.addEventListener('change', function() {
                    const selectedStatus = this.value;
                    const appointmentCards = document.querySelectorAll('#upcoming-tab .appointment-card');

                    appointmentCards.forEach(card => {
                        const cardStatus = card.getAttribute('data-status');
                        if (selectedStatus === '<spring:message code="dashboard.filter.all" />' || cardStatus === selectedStatus) {
                            card.style.display = 'flex';
                        } else {
                            card.style.display = 'none';
                        }
                    });
                });
            }

            // Date filter functionality
            const dateFilter = document.getElementById('date-range');
            if (dateFilter) {
                dateFilter.addEventListener('change', function() {
                    const selectedDateRange = this.value;
                    const appointmentCards = document.querySelectorAll('#upcoming-tab .appointment-card');
                    const today = new Date();
                    const startOfWeek = new Date();
                    startOfWeek.setDate(today.getDate() - today.getDay());
                    const startOfMonth = new Date(today.getFullYear(), today.getMonth(), 1);
                    const endOfMonth = new Date(today.getFullYear(), today.getMonth() + 1, 0);
                    const startOfToday = new Date(today.getFullYear(), today.getMonth(), today.getDate());
                    const endOfToday = new Date(today.getFullYear(), today.getMonth(), today.getDate() + 1);
                    const startOfAll = new Date(0);
                    const endOfAll = new Date(9999, 11, 31);
                    let startDate, endDate;
                    switch (selectedDateRange) {
                        case 'today':
                            startDate = startOfToday;
                            endDate = endOfToday;
                            break;
                        case 'week':
                            startDate = startOfWeek;
                            endDate = new Date(startOfWeek);
                            endDate.setDate(endDate.getDate() + 7);
                            break;
                        case 'month':
                            startDate = startOfMonth;
                            endDate = endOfMonth;
                            break;
                        case 'all':
                            startDate = startOfAll;
                            endDate = endOfAll;
                            break;
                        default:
                            startDate = startOfAll;
                            endDate = endOfAll;
                    }
                    appointmentCards.forEach(card => {
                        const cardDate = new Date(card.getAttribute('data-date'));
                        if (cardDate >= startDate && cardDate <= endDate) {
                            card.style.display = 'flex';
                        } else {
                            card.style.display = 'none';
                        }
                    });
                });
            }
        });
    </script>
</dashboard:patient-dashboard>
