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
<jsp:include page="/WEB-INF/jsp/components/header.jsp" />

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
        <!-- Upcoming Appointments Tab -->
        <div class="tab-content active" id="upcoming-tab">
            <div class="tab-header">
                <h2><spring:message code="dashboard.upcoming.title" /></h2>
                <div class="tab-actions">
                    <div class="date-filter">
                        <label for="date-range"><spring:message code="dashboard.filter.dateRange" />:</label>
                        <select id="date-range" class="filter-select" onchange="applyDateFilter(this.value)">
                            <option value="today" ${param.dateRange == 'today' ? 'selected' : ''}><spring:message code="dashboard.filter.today" /></option>
                            <option value="week" ${param.dateRange == 'week' ? 'selected' : ''}><spring:message code="dashboard.filter.thisWeek" /></option>
                            <option value="month" ${param.dateRange == 'month' ? 'selected' : ''}><spring:message code="dashboard.filter.thisMonth" /></option>
                            <option value="all" ${param.dateRange == 'all' || empty param.dateRange ? 'selected' : ''}><spring:message code="dashboard.filter.all" /></option>
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

                                            <c:if test="${status eq confirmed}">
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
                        <c:if test="${hasMore}">
                            <div class="load-more-container">
                                <button id="loadMoreUpcoming" class="btn-load-more" data-current-page="${currentPage}" data-total-pages="${totalPages}">
                                    <i class="fas fa-sync-alt"></i>
                                    <span><spring:message code="dashboard.loadMore"  /></span>
                                </button>
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
        <div class="modal-footer" id="cancelModal">
            <form id="cancelForm" action="${pageContext.request.contextPath}/doctor/dashboard/appointment/cancel" method="post">
                <input type="hidden" name="appointmentId" value="" />
                <button type="button" class="btn-modal btn-cancel" onclick="hideCancelModal();">
                    <spring:message code="logout.confirmation.cancel"/>
                </button>
                <button type="submit" class="btn-modal btn-primary" id="cancelAppointmentBtn">
                    <spring:message code="appointment.action.cancel"/>
                </button>
            </form>
        </div>
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Apply date filter function
        window.applyDateFilter = function(value) {
            window.location.href = '${pageContext.request.contextPath}/doctor/dashboard/upcoming?dateRange=' + value;
        };

        // Modal functionality
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

        const urlParams = new URLSearchParams(window.location.search);
        if (urlParams.get('cancelled') === 'true') {
            showSuccessToast();

            // Remove the query parameter without refreshing the page
            const newUrl = window.location.pathname;
            window.history.replaceState({}, document.title, newUrl);
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


        // Cancel appointment functionality
        const cancelButtons = document.querySelectorAll('.cancel-appointment');
        cancelButtons.forEach(button => {
            button.addEventListener('click', function(e) {
                e.preventDefault();
                const appointmentId = this.getAttribute('data-id');
                showCancelModal(appointmentId);
            });
        });


        // Cerrar modales al hacer clic fuera
        document.querySelectorAll('.modal-overlay').forEach(modal => {
            modal.addEventListener('click', function(e) {
                if (e.target === this) {
                    this.classList.remove('show');
                }
            });
        });

        document.querySelectorAll('.cancel-appointment').forEach(button => {
            button.addEventListener('click', () => {
                const apptId = button.getAttribute('data-id');
                document
                    .querySelector('#cancelForm input[name="appointmentId"]')
                    .value = apptId;
            });
        });

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

        // Cerrar modales con tecla Escape
        document.addEventListener('keydown', function(e) {
            if (e.key === 'Escape') {
                document.querySelectorAll('.modal-overlay').forEach(modal => {
                    modal.classList.remove('show');
                });
            }
        });

        // Cargar más citas próximas
        const loadMoreUpcomingBtn = document.getElementById('loadMoreUpcoming');
        if (loadMoreUpcomingBtn) {
            loadMoreUpcomingBtn.addEventListener('click', function() {
                const currentPage = parseInt(this.getAttribute('data-current-page'));
                const nextPage = currentPage + 1;
                const totalPages = parseInt(this.getAttribute('data-total-pages'));

                // Verificar si ya estamos en la última página
                if (nextPage > totalPages) {
                    this.parentNode.remove();
                    return;
                }

                // Mostrar indicador de carga
                this.innerHTML = '<i class="fas fa-spinner fa-spin"></i>';
                this.disabled = true;

                // Construir la URL con el parámetro de página y asegurarse de que sea reconocida como AJAX
                const url = new URL(`${pageContext.request.contextPath}/doctor/dashboard/upcoming`, window.location.origin);
                url.searchParams.append('page', nextPage);
                url.searchParams.append('ajax', 'true'); // Añadir un parámetro para indicar que es una solicitud AJAX
                url.searchParams.append('dateRange', '${param.dateRange != null ? param.dateRange : "all"}');

                // Realizar la petición AJAX
                fetch(url.toString(), {
                    method: 'GET',
                    headers: {
                        'X-Requested-With': 'XMLHttpRequest', // Cabecera estándar para solicitudes AJAX
                        'Accept': 'text/html' // Especificar que esperamos HTML
                    }
                })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Error en la respuesta del servidor: ' + response.status);
                        }
                        return response.text();
                    })
                    .then(html => {
                        // Crear un elemento temporal para parsear el HTML
                        const parser = new DOMParser();
                        const doc = parser.parseFromString(html, 'text/html');

                        // Extraer las nuevas citas
                        const newAppointments = doc.querySelectorAll('.appointment-card');

                        console.log(`Se encontraron ${newAppointments.length} citas en la respuesta`);

                        if (newAppointments.length === 0) {
                            console.log('No se encontraron citas en la respuesta');
                            // Si no hay más citas, eliminar el botón
                            this.parentNode.remove();
                            return;
                        }

                        // Obtener los IDs de las citas actuales
                        const currentAppointmentIds = Array.from(
                            document.querySelectorAll('.appointment-card')
                        ).map(card => card.getAttribute('data-id'));

                        // Agregar las nuevas citas
                        const appointmentsList = document.querySelector('.appointments-list');
                        const loadMoreContainer = document.querySelector('.load-more-container');
                        let addedCount = 0;

                        newAppointments.forEach(appointment => {
                            const appointmentId = appointment.getAttribute('data-id');

                            // Verificar si la cita ya existe
                            if (!currentAppointmentIds.includes(appointmentId)) {
                                // Clonar el nodo para agregarlo a nuestro DOM
                                const appointmentNode = document.importNode(appointment, true);
                                appointmentsList.insertBefore(appointmentNode, loadMoreContainer);

                                // Inicializar los botones de confirmar y cancelar para las nuevas citas
                                const confirmBtn = appointmentNode.querySelector('.confirm-appointment');
                                if (confirmBtn) {
                                    confirmBtn.addEventListener('click', function(e) {
                                        e.preventDefault();
                                        const appointmentId = this.getAttribute('data-id');
                                        showConfirmModal(appointmentId);
                                    });
                                }

                                const cancelBtn = appointmentNode.querySelector('.cancel-appointment');
                                if (cancelBtn) {
                                    cancelBtn.addEventListener('click', function(e) {
                                        e.preventDefault();
                                        const appointmentId = this.getAttribute('data-id');
                                        showCancelModal(appointmentId);
                                    });
                                }

                                addedCount++;
                            }
                        });

                        console.log(`Se agregaron ${addedCount} citas nuevas`);

                        if (addedCount === 0) {
                            // Si no se agregaron citas nuevas, podría ser que estemos en la última página
                            if (nextPage >= totalPages) {
                                this.parentNode.remove();
                            } else {
                                // O podría ser que todas las citas ya estaban cargadas
                                // Intentar con la siguiente página
                                this.setAttribute('data-current-page', nextPage);
                                setTimeout(() => {
                                    this.click();
                                }, 500);
                            }
                            return;
                        }

                        // Actualizar el botón con la nueva página
                        this.setAttribute('data-current-page', nextPage);

                        // Verificar si hay más páginas
                        if (nextPage >= totalPages) {
                            this.parentNode.remove(); // Eliminar el botón si no hay más páginas
                        } else {
                            // Restaurar el botón
                            this.innerHTML = '<i class="fas fa-sync-alt"></i> <span><spring:message code="dashboard.loadMore"  /></span>';
                            this.disabled = false;
                        }
                    })
                    .catch(error => {

                        this.innerHTML = '<i class="fas fa-exclamation-circle"></i>';
                        setTimeout(() => {
                            this.innerHTML = '<i class="fas fa-sync-alt"></i> <span><spring:message code="dashboard.loadMore"  /></span>';
                            this.disabled = false;
                        }, 2000);
                    });
            });
        }
    });
</script>

<script src="<c:url value="/js/toast-notification.js"/> "></script>
</body>
</html>
