<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<link rel="stylesheet" href="<c:url value='/css/components/doctor-dashboard.css' />">
<link rel="stylesheet" href="<c:url value='/css/components/modal.css' />">
<layout:page title="dashboard.doctor.title">
    <div class="dashboard-container">
        <!-- Doctor Profile Header -->
        <div class="dashboard-header">
            <div class="doctor-info">
                <div class="doctor-avatar">
                    <img src="<c:url value="/doctor/${doctor.id}/image"/>" alt="<c:out value="${doctor.name} ${doctor.lastName}"/>"/>
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
                    <div class="stat-value">${upcomingAppointments.size()}</div>
                    <div class="stat-label"><spring:message code="dashboard.stats.upcoming" /></div>
                </div>
                <div class="stat-item">
                    <div class="stat-value">${pastAppointments.size()}</div>
                    <div class="stat-label"><spring:message code="dashboard.stats.past" /></div>
                </div>
                <div class="stat-item">
                    <div class="stat-value">${doctor.specialtyList.size()}</div>
                    <div class="stat-label"><spring:message code="dashboard.stats.specialties" /></div>
                </div>
            </div>
        </div>

        <!-- Dashboard Navigation Tabs -->
        <div class="dashboard-nav">
            <a href="<c:url value='/doctor/dashboard/upcoming'/>" class="nav-tab ${activeTab == 'upcoming' ? 'active' : ''}">
                <i class="fas fa-calendar-alt"></i>
                <span><spring:message code="dashboard.tab.upcoming" /></span>
            </a>
            <a href="<c:url value='/doctor/dashboard/history'/>" class="nav-tab ${activeTab == 'history' ? 'active' : ''}">
                <i class="fas fa-history"></i>
                <span><spring:message code="dashboard.tab.history" /></span>
            </a>
            <a href="<c:url value='/doctor/dashboard/profile'/>" class="nav-tab ${activeTab == 'profile' ? 'active' : ''}">
                <i class="fas fa-user-md"></i>
                <span><spring:message code="dashboard.tab.profile" /></span>
            </a>
            <a href="<c:url value='/doctor/dashboard/availability'/>" class="nav-tab ${activeTab == 'availability' ? 'active' : ''}">
                <i class="fas fa-calendar-check"></i>
                <span><spring:message code="dashboard.tab.availability" /></span>
            </a>
        </div>

        <!-- Dashboard Content Area -->
        <div class="dashboard-content">
            <!-- Contenido específico de cada página -->
            <jsp:doBody />
        </div>
    </div>

    <!-- Modales comunes -->
    <div id="confirmAppointmentModal" class="modal-overlay">
        <div class="modal-container">
            <div class="modal-header">
                <div class="modal-icon">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
                        <path d="M12 22C6.477 22 2 17.523 2 12S6.477 2 12 2s10 4.477 10 10-4.477 10-10 10zm0-2a8 8 0 1 0 0-16 8 8 0 0 0 0 16zm0-9a1 1 0 0 1 1 1v4a1 1 0 0 1-2 0v-4a1 1 0 0 1 1-1zm0-4a1 1 0 1 1 0 2 1 1 0 0 1 0-2z"></path>
                    </svg>
                </div>
                <h3 class="modal-title"><spring:message code="appointment.confirm.title" /></h3>
            </div>
            <div class="modal-body">
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

    <div id="cancelAppointmentModal" class="modal-overlay">
        <div class="modal-container">
            <div class="modal-header">
                <div class="modal-icon">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
                        <path d="M12 22C6.477 22 2 17.523 2 12S6.477 2 12 2s10 4.477 10 10-4.477 10-10 10zm0-2a8 8 0 1 0 0-16 8 8 0 0 0 0 16zm0-9a1 1 0 0 1 1 1v4a1 1 0 0 1-2 0v-4a1 1 0 0 1 1-1zm0-4a1 1 0 1 1 0 2 1 1 0 0 1 0-2z"></path>
                    </svg>
                </div>
                <h3 class="modal-title"><spring:message code="appointment.cancel.title" /></h3>
            </div>
            <div class="modal-body">
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

    <link rel="stylesheet" href="<c:url value='/css/components/doctor-dashboard.css' />" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />

    <!-- Estilos comunes -->
    <style>
        .text-center {
            text-align: center;
        }

        .profile-content {
            display: flex;
            flex-direction: column;
            align-items: center;
            width: 100%;
        }

        .profile-section {
            width: 100%;
            max-width: 800px;
            margin: 0 auto 1.5rem;
            background-color: #fff;
            border-radius: 0.5rem;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
            padding: 1.5rem;
        }

        #edit-profile-form {
            width: 100%;
            max-width: 800px;
            margin: 0 auto;
        }

        .info-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 1rem;
        }

        .info-item {
            padding: 0.75rem;
            border-radius: 0.375rem;
            background-color: #f9fafb;
        }

        .info-label {
            font-size: 0.875rem;
            color: #6b7280;
            margin-bottom: 0.25rem;
        }

        .info-value {
            font-size: 1rem;
            font-weight: 500;
            color: #1f2937;
        }

        .specialties-list, .coverages-list {
            display: flex;
            flex-wrap: wrap;
            gap: 0.5rem;
        }

        .specialty-item, .coverage-item {
            background-color: #e1effe;
            color: #2a5caa;
            padding: 0.375rem 0.75rem;
            border-radius: 0.375rem;
            font-size: 0.875rem;
            font-weight: 500;
        }

        .selected-coverage {
            display: inline-block;
            margin-right: 0.25rem;
        }

        .current-coverages {
            margin-bottom: 0.5rem;
        }

        #selected-coverages-display {
            display: inline;
            font-weight: 500;
        }

        /* Time slots styles */
        .timeslots-container {
            border: 1px solid #ddd;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 15px;
            background-color: #f9f9f9;
        }

        .no-slots-message {
            text-align: center;
            color: #666;
            padding: 20px 0;
        }

        .time-slot-row {
            display: flex;
            align-items: center;
            margin-bottom: 15px;
            padding: 15px;
            background-color: white;
            border-radius: 8px;
            border: 1px solid #eee;
            box-shadow: 0 1px 3px rgba(0,0,0,0.05);
        }

        .time-slot-row select {
            height: 40px;
            padding: 0 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            background-color: white;
            font-size: 14px;
        }

        .day-select {
            flex: 2;
            margin-right: 10px;
        }

        .time-select {
            flex: 1;
            margin-right: 10px;
        }

        .time-label {
            display: block;
            font-size: 12px;
            color: #666;
            margin-bottom: 5px;
        }

        .btn-remove {
            background: none;
            border: none;
            color: #e25c5c;
            cursor: pointer;
            font-size: 20px;
            width: 30px;
            height: 30px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 50%;
            transition: all 0.2s;
        }

        .btn-remove:hover {
            background-color: #ffeeee;
        }

        .btn-add-slot {
            background-color: #f5f5f5;
            border: 1px dashed #ccc;
            color: #333;
            width: 100%;
            padding: 12px;
            text-align: center;
            border-radius: 6px;
            cursor: pointer;
            font-weight: 500;
            transition: all 0.2s;
        }

        .btn-add-slot:hover {
            background-color: #e9e9e9;
        }

        .slot-error {
            border: 1px solid #e25c5c !important;
            background-color: #fff8f8 !important;
        }

        .btn-disabled {
            opacity: 0.6;
            cursor: not-allowed;
            pointer-events: none;
        }

        /* Modificar los estilos de navegación para usar enlaces en lugar de botones */
        .dashboard-nav a.nav-tab {
            display: flex;
            align-items: center;
            padding: 0.75rem 1.25rem;
            border-radius: 0.375rem;
            font-weight: 500;
            color: #4b5563;
            text-decoration: none;
            transition: all 0.2s;
        }

        .dashboard-nav a.nav-tab:hover {
            background-color: #f3f4f6;
            color: #111827;
        }

        .dashboard-nav a.nav-tab.active {
            background-color: #f3f4f6;
            color: #111827;
            font-weight: 600;
        }

        .dashboard-nav a.nav-tab i {
            margin-right: 0.5rem;
        }
    </style>

    <!-- Scripts comunes -->
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            let currentAppointmentId = null;

            // Funciones para mostrar/ocultar modales
            window.showConfirmModal = function(appointmentId) {
                currentAppointmentId = appointmentId;
                document.getElementById('confirmAppointmentModal').classList.add('show');
            }

            window.hideConfirmModal = function() {
                document.getElementById('confirmAppointmentModal').classList.remove('show');
            }

            window.showCancelModal = function(appointmentId) {
                currentAppointmentId = appointmentId;
                document.getElementById('cancelAppointmentModal').classList.add('show');
            }

            window.hideCancelModal = function() {
                document.getElementById('cancelAppointmentModal').classList.remove('show');
            }

            // Confirm appointment functionality
            const confirmButtons = document.querySelectorAll('.confirm-appointment');
            confirmButtons.forEach(button => {
                button.addEventListener('click', function(e) {
                    e.preventDefault();
                    const appointmentId = this.getAttribute('data-id');
                    showConfirmModal(appointmentId);
                });
            });

            // Confirm button in modal
            document.getElementById('confirmAppointmentBtn').addEventListener('click', function() {
                if (currentAppointmentId) {
                    fetch(`${pageContext.request.contextPath}/doctor/dashboard/appointment/accept`, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        body: new URLSearchParams({ appointmentId: currentAppointmentId })
                    })
                        .then(response => response.json())
                        .then(data => {
                            if (data.success) {
                                window.location.reload();
                            }
                        });
                }
                hideConfirmModal();
            });

            // Cancel appointment functionality
            const cancelButtons = document.querySelectorAll('.cancel-appointment');
            cancelButtons.forEach(button => {
                button.addEventListener('click', function(e) {
                    e.preventDefault();
                    const appointmentId = this.getAttribute('data-id');
                    showCancelModal(appointmentId);
                });
            });

            // Cancel button in modal
            document.getElementById('cancelAppointmentBtn').addEventListener('click', function() {
                if (currentAppointmentId) {
                    fetch(`${pageContext.request.contextPath}/doctor/dashboard/appointment/cancel`, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        body: new URLSearchParams({ appointmentId: currentAppointmentId })
                    })
                        .then(response => response.json())
                        .then(data => {
                            if (data.success) {
                                window.location.reload();
                            }
                        });
                }
                hideCancelModal();
            });

            // Cerrar modales al hacer clic fuera
            document.querySelectorAll('.modal-overlay').forEach(modal => {
                modal.addEventListener('click', function(e) {
                    if (e.target === this) {
                        this.classList.remove('show');
                    }
                });
            });

            // Cerrar modales con tecla Escape
            document.addEventListener('keydown', function(e) {
                if (e.key === 'Escape') {
                    document.querySelectorAll('.modal-overlay').forEach(modal => {
                        modal.classList.remove('show');
                    });
                }
            });
        });
    </script>
</layout:page>
