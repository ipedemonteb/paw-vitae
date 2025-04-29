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
    <link rel="stylesheet" href="<c:url value='/css/dashboard-patient.css' />" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/components/header.jsp" />

<!-- Cancel Appointment Modal -->
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

<section class="dashboard-container">
    <!-- Include the dashboard header component -->
    <c:set var="activeTab" value="upcoming" scope="request" />
    <jsp:include page="/WEB-INF/jsp/components/dashboard-header.jsp" />

    <!-- Dashboard Content Area -->
    <div class="dashboard-content">
        <!-- Upcoming Appointments Tab -->
        <div class="tab-content active" id="upcoming-tab">
            <div class="tab-header">
                <h2><spring:message code="dashboard.upcoming.title" /></h2>
                <div class="tab-actions">
                    <div class="date-filter">
                        <label for="date-range"><spring:message code="dashboard.filter.dateRange" />:</label>
                        <select id="date-range" class="filter-select">
                            <option value="today"><spring:message code="dashboard.filter.today" /></option>
                            <option value="week" ><spring:message code="dashboard.filter.thisWeek" /></option>
                            <option value="month"><spring:message code="dashboard.filter.thisMonth" /></option>
                            <option value="all" selected><spring:message code="dashboard.history.all" /></option>
                        </select>
                    </div>
                </div>
            </div>

            <c:choose>
                <c:when test="${not empty upcomingAppointments}">
                    <div class="appointments-list">
                        <c:forEach items="${upcomingAppointments}" var="appointment">
                            <div class="appointment-card" data-id="${appointment.id}" data-status="<spring:message code="${appointment.status}"/>" data-date="<c:out value="${appointment.date}"/>">
                                <div class="appointment-left">
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
                                    <span class="status-badge <c:out value="${appointment.status}"/>">
                                            <spring:message code='${appointment.status}'/>
                                        </span>
                                </div>
                                <div class="appointment-right">
                                    <div class="patient-info">
                                        <div class="patient-avatar">
                                            <img src="<c:url value="/doctor/${appointment.doctor.id}/image"/>" alt="<c:out value="${appointment.doctor.name} ${appointment.doctor.lastName}"/>"/>
                                        </div>
                                        <div>
                                            <div class="patient-name">
                                                <c:out value="${appointment.doctor.name}" /> <c:out value="${appointment.doctor.lastName}" />
                                            </div>
                                            <div class="patient-coverage">
                                                <c:out value="${patient.coverage.name}"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="appointment-reason">
                                        <div class="reason-label"><spring:message code="appointment.reason" />:</div>
                                        <div class="reason-text"><c:out value="${appointment.reason}" /></div>
                                    </div>
                                    <div class="appointment-footer">
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
                                            <div class="appointment-actions">
                                                <button class="btn btn-primary view-appointment" onclick="window.location.href='/appointment/${appointment.id}'">
                                                    <i class="fas fa-eye"></i>
                                                    <span>View Appointment</span>
                                                </button>
                                            </div>
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
</section>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Status filter functionality for upcoming appointments


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

        // Cancel appointment functionality
        let currentAppointmentId = null;

        // Funciones para mostrar/ocultar modales
        window.showCancelModal = function(appointmentId) {
            currentAppointmentId = appointmentId;
            const modal = document.getElementById('cancelAppointmentModal');
            if (modal) {
                modal.classList.add('show');
            }
        };

        window.hideCancelModal = function() {
            const modal = document.getElementById('cancelAppointmentModal');
            if (modal) {
                modal.classList.remove('show');
            }
        };

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
        const cancelAppointmentBtn = document.getElementById('cancelAppointmentBtn');
        if (cancelAppointmentBtn) {
            cancelAppointmentBtn.addEventListener('click', function() {
                if (currentAppointmentId) {
                    fetch(`${pageContext.request.contextPath}/patient/dashboard/appointment/cancel`, {
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
                        })
                }
                hideCancelModal();
            });
        }

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

        // Función para inicializar eventos en las nuevas citas
        function initializeEvents() {
            // Inicializar los botones de cancelar para las nuevas citas
            document.querySelectorAll('.cancel-appointment').forEach(button => {
                if (!button.hasAttribute('data-initialized')) {
                    button.setAttribute('data-initialized', 'true');
                    button.addEventListener('click', function(e) {
                        e.preventDefault();
                        const appointmentId = this.getAttribute('data-id');
                        showCancelModal(appointmentId);
                    });
                }
            });

            // Aplicar los filtros actuales a las nuevas citas


            // Aplicar el filtro de fecha a las nuevas citas
            if (dateFilter) {
                const selectedDateRange = dateFilter.value;
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

                const newCards = document.querySelectorAll('#upcoming-tab .appointment-card:not([data-date-filtered])');

                newCards.forEach(card => {
                    card.setAttribute('data-date-filtered', 'true');
                    const cardDate = new Date(card.getAttribute('data-date'));
                    if (!(cardDate >= startDate && cardDate <= endDate)) {
                        card.style.display = 'none';
                    }
                });
            }
        }

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
                this.innerHTML = '<i class="fas fa-spinner fa-spin"></i> ';
                this.disabled = true;

                // Construir la URL con el parámetro de página y asegurarse de que sea reconocida como AJAX
                const url = new URL(`${pageContext.request.contextPath}/patient/dashboard/upcoming`, window.location.origin);
                url.searchParams.append('page', nextPage);
                url.searchParams.append('ajax', 'true'); // Añadir un parámetro para indicar que es una solicitud AJAX

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
                        this.innerHTML = '<i class="fas fa-sync-alt"></i> <span><spring:message code="dashboard.loadMore" /></span>';
                        this.disabled = false;
                    }

                    // Inicializar eventos para las nuevas citas
                    initializeEvents();
                })
                .catch(error => {
                    console.error('Error al cargar más citas:', error);
                    this.innerHTML = '<i class="fas fa-exclamation-circle"></i> ';
                    setTimeout(() => {
                        this.innerHTML = '<i class="fas fa-sync-alt"></i> <span><spring:message code="dashboard.loadMore"  /></span>';
                        this.disabled = false;
                    }, 2000);
                });
            });
        }
    });
</script>
</body>
</html>