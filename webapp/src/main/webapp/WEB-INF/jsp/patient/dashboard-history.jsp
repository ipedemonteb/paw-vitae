<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="dashboard" tagdir="/WEB-INF/tags/components" %>

<dashboard:patient-dashboard>
    <!-- Appointment History Tab -->
    <div class="tab-content active" id="history-tab">
    <div class="tab-header">
        <h2><spring:message code="dashboard.history.title" /></h2>
        <div class="tab-actions">
            <div class="status-filter">
                <label for="history-status-filter"><spring:message code="dashboard.filter.status" />:</label>
                <select id="history-status-filter" class="filter-select">
                    <option value="<spring:message code="dashboard.filter.all" />" selected><spring:message code="dashboard.filter.all" /></option>
                    <option value="<spring:message code="appointment.status.completed" />"><spring:message code="appointment.status.completed" /></option>
                    <option value="<spring:message code="appointment.status.cancelled" />"><spring:message code="appointment.status.cancelled" /></option>
                    <option value="<spring:message code="appointment.status.noShow" />"><spring:message code="appointment.status.noShow" /></option>
                </select>
            </div>
            <div class="search-container">
                <button class="search-button">
                    <i class="fas fa-search"></i>
                </button>
                <input type="text" class="search-input" placeholder="<spring:message code="dashboard.search.placeholder" />" />
            </div>
        </div>
    </div>

    <c:choose>
        <c:when test="${not empty pastAppointments}">
            <div class="appointments-list">
            <c:forEach items="${pastAppointments}" var="appointment">
                <div class="appointment-card past" data-status="<spring:message code="${appointment.status}"/>" data-date="<c:out value="${appointment.date}"/>">
                    <div class="appointment-time">
                        <div class="appointment-date">
                                    <span class="day">
                                            <spring:message code="${appointment.date.dayOfWeek}"/>
                                    </span>
                            <span class="date-number">
                                            <c:out value="${appointment.date.dayOfMonth}"/>
                                    </span>
                            <span class="month">
                                             <spring:message code="${appointment.date.month}" />
                                    </span>
                        </div>
                        <div class="  />
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
                            <img src="<c:url value="/doctor/${appointment.doctor.id}/image"/>" alt="<c:out value="${patient.name} ${patient.lastName}"/>'">
                        </div>
                        <div>
                            <div class="patient-name">
                                <c:out value="${appointment.doctor.name}" /> <c:out value="${appointment.doctor.lastName}" />
                            </div>
                            <div class="patient-coverage">
                                <spring:message code="${appointment.specialty.key}" />
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
                </div>
                </div>
            </c:forEach>
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

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Status filter functionality for history appointments
            const historyStatusFilter = document.getElementById('history-status-filter');
            if (historyStatusFilter) {
                historyStatusFilter.addEventListener('change', function() {
                    const selectedStatus = this.value;
                    const appointmentCards = document.querySelectorAll('#history-tab .appointment-card');

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

            // Search functionality
            const searchInput = document.querySelector('.search-input');
            if (searchInput) {
                searchInput.addEventListener('input', function() {
                    // Filter appointments based on search input
                    const searchTerm = this.value.toLowerCase();
                    const appointmentCards = document.querySelectorAll('#history-tab .appointment-card');

                    appointmentCards.forEach(card => {
                        const doctorName = card.querySelector('.patient-name').textContent.toLowerCase();
                        const reasonText = card.querySelector('.reason-text')?.textContent.toLowerCase() || '';

                        if (doctorName.includes(searchTerm) || reasonText.includes(searchTerm)) {
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
