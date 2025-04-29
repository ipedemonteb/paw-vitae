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
    <link rel="stylesheet" href="<c:url value='/css/dashboard-patient.css' />" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
<!-- Include the header -->
<jsp:include page="/WEB-INF/jsp/components/header.jsp" />

<div class="dashboard-container">
    <!-- Include the dashboard header component -->
    <c:set var="activeTab" value="history" scope="request" />
    <jsp:include page="/WEB-INF/jsp/components/dashboard-header.jsp" />

    <!-- Dashboard Content Area -->
    <div class="dashboard-content">
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
                            <div class="appointment-card past" data-id="${appointment.id}" data-status="<spring:message code="${appointment.status}"/>" data-date="<c:out value="${appointment.date}"/>">
                                <div class="appointment-left">
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
                                    <div class="appointment-footer">
                                        <div class="appointment-specialty">
                                                <span class="specialty-badge">
                                                    <spring:message code="${appointment.specialty.key}" />
                                                </span>
                                        </div>
                                        <div class="appointment-actions">
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
                                <button id="loadMoreHistory" class="btn-load-more" data-current-page="${currentPage}" data-total-pages="${totalPages}">
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
                            <i class="fas fa-history fa-3x"></i>
                        </div>
                        <h3><spring:message code="dashboard.history.empty.title" /></h3>
                        <p><spring:message code="dashboard.history.empty.message" /></p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
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

        // Función para inicializar filtros y eventos en las citas cargadas
        function initializeFiltersAndEvents() {
            // Aplicar el filtro actual a las nuevas citas
            const historyStatusFilter = document.getElementById('history-status-filter');
            if (historyStatusFilter) {
                const selectedStatus = historyStatusFilter.value;
                const appointmentCards = document.querySelectorAll('#history-tab .appointment-card');

                appointmentCards.forEach(card => {
                    const cardStatus = card.getAttribute('data-status');

                    if (selectedStatus === '<spring:message code="dashboard.filter.all" />' || cardStatus === selectedStatus) {
                        card.style.display = 'flex';
                    } else {
                        card.style.display = 'none';
                    }
                });
            }

            // Aplicar la búsqueda actual a las nuevas citas
            const searchInput = document.querySelector('.search-input');
            if (searchInput && searchInput.value.trim() !== '') {
                const searchTerm = searchInput.value.toLowerCase();
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
            }
        }

        // Cargar más citas históricas
        const loadMoreHistoryBtn = document.getElementById('loadMoreHistory');
        if (loadMoreHistoryBtn) {
            loadMoreHistoryBtn.addEventListener('click', function() {
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
                const url = new URL(`${pageContext.request.contextPath}/patient/dashboard/history`, window.location.origin);
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
                            this.innerHTML = '<i class="fas fa-sync-alt"></i> <span><spring:message code="dashboard.loadMore"/></span>';
                            this.disabled = false;
                        }

                        // Reinicializar los filtros y eventos para las nuevas citas
                        initializeFiltersAndEvents();
                    })
                    .catch(error => {
                        console.error('Error al cargar más citas:', error);
                        this.innerHTML = '<i class="fas fa-exclamation-circle"></i> ';
                        setTimeout(() => {
                            this.innerHTML = '<i class="fas fa-sync-alt"></i> <span><spring:message code="dashboard.loadMore" /></span>';
                            this.disabled = false;
                        }, 2000);
                    });
            });
        }
    });
</script>
</body>
</html>