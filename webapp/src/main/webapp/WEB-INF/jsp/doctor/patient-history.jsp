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
    <title><spring:message code="dashboard.medicalHistory.title" /></title>
    <link rel="stylesheet" href="<c:url value='/css/dashboard-patient.css' />" />
    <link rel="stylesheet" href="<c:url value='/css/medical-history.css' />" />
    <link rel="stylesheet" href="<c:url value='/css/toast-notification.css' />" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>

<jsp:include page="/WEB-INF/jsp/components/header.jsp">
    <jsp:param name="id" value="${doctor.imageId}" />
    <jsp:param name="doctorId" value="${doctor.id}" />
</jsp:include>

<!-- Success Notification Toast -->
<div id="successToast" class="success-toast">
    <div class="success-toast-icon">
        <i class="fas fa-check"></i>
    </div>
    <div class="success-toast-content">
        <div class="success-toast-title"><spring:message code="medicalHistory.download.success.title" /></div>
        <div class="success-toast-message"><spring:message code="medicalHistory.download.success.message" /></div>
    </div>
    <button class="success-toast-close" onclick="hideSuccessToast()">
        <i class="fas fa-times"></i>
    </button>
</div>
<main class="dashboard-container">

    <div class="dashboard-content">
        <!-- Medical History Tab -->
        <div class="tab-content active" id="medical-history-tab">
            <div class="tab-header">
                <div class="header-content">
                    <div class="header-info">
                        <h2><spring:message code="dashboard.medicalHistory.title" /></h2>
                        <p class="header-subtitle"><spring:message code="dashboard.doctor.medicalHistory.subtitle" /></p>
                    </div>
                    <div class="header-stats">
                        <div class="stat-card">
                            <div class="stat-icon">
                                <i class="fas fa-file-medical"></i>
                            </div>
                            <div class="stat-content">
                                <!-- Calculate total files from grouped entries -->
                                <c:set var="totalFiles" value="0" />
                                <c:forEach items="${appointmentFiles}" var="appointmentEntry">
                                    <c:set var="totalFiles" value="${totalFiles + fn:length(appointmentEntry.value)}" />
                                </c:forEach>
                                <div class="stat-number">${totalFiles}</div>
                                <div class="stat-label"><spring:message code="dashboard.medicalHistory.totalFiles" /></div>
                            </div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-icon">
                                <i class="fas fa-calendar-check"></i>
                            </div>
                            <div class="stat-content">
                                <div class="stat-number">${fn:length(appointmentFiles)}</div>
                                <div class="stat-label"><spring:message code="dashboard.medicalHistory.totalAppointments" /></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Search and Filter Section -->
            <div class="search-filter-section">
                <div class="search-container">
                    <div class="search-input-wrapper">
                        <i class="fas fa-search search-icon"></i>
                        <input type="text" id="searchInput" class="search-input"
                               placeholder="<spring:message code='dashboard.medicalHistory.search.placeholder' />" />
                        <button type="button" id="clearSearch" class="clear-search" style="display: none;">
                            <i class="fas fa-times"></i>
                        </button>
                    </div>
                </div>

                <div class="filter-container">
                    <div class="filter-group">
                        <label for="dateFilter" class="filter-label">
                            <i class="fas fa-calendar-alt"></i>
                            <spring:message code="dashboard.medicalHistory.filter.date" />
                        </label>
                        <select id="dateFilter" class="filter-select">
                            <option value="all"><spring:message code="dashboard.medicalHistory.filter.allDates" /></option>
                            <option value="thisMonth"><spring:message code="dashboard.medicalHistory.filter.thisMonth" /></option>
                            <option value="lastMonth"><spring:message code="dashboard.medicalHistory.filter.lastMonth" /></option>
                            <option value="last3Months"><spring:message code="dashboard.medicalHistory.filter.last3Months" /></option>
                            <option value="thisYear"><spring:message code="dashboard.medicalHistory.filter.thisYear" /></option>
                        </select>
                    </div>
                </div>
            </div>

            <!-- Medical History Content -->
            <div class="medical-history-content">
                <!-- Empty State -->
                <div id="emptyState" class="empty-state" style="display: none;">
                    <div class="empty-state-icon">
                        <i class="fas fa-folder-open"></i>
                    </div>
                    <h3 class="empty-state-title"><spring:message code="dashboard.medicalHistory.empty.title" /></h3>
                    <p class="empty-state-message"><spring:message code="dashboard.medicalHistory.empty.message" /></p>
                </div>

                <!-- No Search Results -->
                <div id="noResults" class="no-results" style="display: none;">
                    <div class="no-results-icon">
                        <i class="fas fa-search"></i>
                    </div>
                    <h3 class="no-results-title"><spring:message code="dashboard.medicalHistory.noResults.title" /></h3>
                    <p class="no-results-message"><spring:message code="dashboard.medicalHistory.noResults.message" /></p>
                </div>

                <!-- Appointments with Files -->
                <div id="appointmentsContainer" class="appointments-container">
                    <c:choose>
                        <c:when test="${empty appointmentFiles}">
                            <script>
                                document.addEventListener('DOMContentLoaded', function() {
                                    document.getElementById('emptyState').style.display = 'block';
                                });
                            </script>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${appointmentFiles}" var="appointmentEntry" varStatus="appointmentStatus">
                                <div class="appointment-card" data-appointment-id="${appointmentEntry.key.id}"
                                    <%--                                     data-date="<fmt:formatDate value='${appointmentEntry.key.date}' pattern='yyyy-MM-dd' />"--%>
                                     data-date="${appointmentEntry.key.date}"
                                     data-doctor="<c:out value='${appointmentEntry.key.doctor.name} ${appointmentEntry.key.doctor.lastName}' />">

                                    <!-- Appointment Header -->
                                    <div class="appointment-header">
                                        <div class="appointment-info">
                                            <div class="appointment-title">
                                                <i class="fas fa-calendar-check"></i>
                                                <h3><spring:message code="dashboard.medicalHistory.appointment" /> #${appointmentEntry.key.id}</h3>
                                            </div>
                                            <div class="appointment-details">
                                                <div class="detail-item">
                                                    <i class="fas fa-calendar-day"></i>
                                                    <span class="detail-label"><spring:message code="dashboard.medicalHistory.date" />:</span>
                                                    <span class="detail-value">
<%--                                                        <fmt:formatDate value="${appointmentEntry.key.date}" pattern="dd/MM/yyyy HH:mm" />--%>
                                                        ${appointmentEntry.key.date}
                                                    </span>
                                                </div>
                                                <div class="detail-item">
                                                    <i class="fas fa-user-md"></i>
                                                    <span class="detail-label"><spring:message code="dashboard.medicalHistory.doctor" />:</span>
                                                    <span class="detail-value">
                                                        <c:out value="${appointmentEntry.key.doctor.name} ${appointmentEntry.key.doctor.lastName}" />
                                                    </span>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="appointment-actions">
                                            <button type="button" class="btn-toggle-files" onclick="toggleFiles(${appointmentEntry.key.id})">
                                                <span class="toggle-text"><spring:message code="dashboard.medicalHistory.showFiles" /></span>
                                                <i class="fas fa-chevron-down toggle-icon"></i>
                                            </button>
                                            <div class="files-count">
                                                <i class="fas fa-paperclip"></i>
                                                <span>${fn:length(appointmentEntry.value)} <spring:message code="dashboard.medicalHistory.files" /></span>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- Files Section -->
                                    <div class="files-section" id="files-${appointmentEntry.key.id}" style="display: none;">
                                        <div class="files-header">
                                            <h4><spring:message code="dashboard.medicalHistory.attachedFiles" /></h4>
                                            <div class="files-summary">
                                                <span class="files-total">${fn:length(appointmentEntry.value)} <spring:message code="dashboard.medicalHistory.totalFiles" /></span>
                                            </div>
                                        </div>

                                        <div class="files-grid">
                                            <c:choose>
                                                <c:when test="${empty appointmentEntry.value}">
                                                    <div class="no-files-message">
                                                        <i class="fas fa-folder-open"></i>
                                                        <span><spring:message code="dashboard.medicalHistory.noFiles" /></span>
                                                    </div>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach items="${appointmentEntry.value}" var="file" varStatus="fileStatus">
                                                        <div class="file-card" data-filename="<c:out value='${file.fileName}' />">
                                                            <div class="file-icon">
                                                                <c:choose>
                                                                    <c:when test="${fn:endsWith(fn:toLowerCase(file.fileName), '.pdf')}">
                                                                        <i class="fas fa-file-pdf"></i>
                                                                    </c:when>
                                                                    <c:when test="${fn:endsWith(fn:toLowerCase(file.fileName), '.jpg') ||
                                                                                  fn:endsWith(fn:toLowerCase(file.fileName), '.jpeg') ||
                                                                                  fn:endsWith(fn:toLowerCase(file.fileName), '.png') ||
                                                                                  fn:endsWith(fn:toLowerCase(file.fileName), '.gif')}">
                                                                        <i class="fas fa-file-image"></i>
                                                                    </c:when>
                                                                    <c:when test="${fn:endsWith(fn:toLowerCase(file.fileName), '.doc') ||
                                                                                  fn:endsWith(fn:toLowerCase(file.fileName), '.docx')}">
                                                                        <i class="fas fa-file-word"></i>
                                                                    </c:when>
                                                                    <c:when test="${fn:endsWith(fn:toLowerCase(file.fileName), '.xls') ||
                                                                                  fn:endsWith(fn:toLowerCase(file.fileName), '.xlsx')}">
                                                                        <i class="fas fa-file-excel"></i>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <i class="fas fa-file"></i>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </div>

                                                            <div class="file-info">
                                                                <div class="file-name" title="<c:out value='${file.fileName}' />">
                                                                    <c:out value="${file.fileName}" />
                                                                </div>
                                                                <div class="file-meta">
                                                                    <span class="uploader-badge uploader-${fn:toLowerCase(file.uploaderRole)}">
                                                                        <i class="fas fa-user"></i>
                                                                        <spring:message code="dashboard.medicalHistory.uploadedBy.${fn:toLowerCase(file.uploaderRole)}" />
                                                                    </span>
                                                                </div>
                                                            </div>

                                                            <div class="file-actions">
                                                                <a href="<c:url value='/file/${file.id}' />"
                                                                   class="btn-view-file"
                                                                   target="_blank"
                                                                   title="<spring:message code='dashboard.medicalHistory.viewFile' />">
                                                                    <i class="fas fa-eye"></i>
                                                                    <span><spring:message code="dashboard.medicalHistory.view" /></span>
                                                                </a>
                                                                <a href="<c:url value='/appointment/${appointmentEntry.key.id}/file/${file.id}' />"
                                                                   class="btn-download-file"
                                                                   title="<spring:message code='dashboard.medicalHistory.downloadFile' />">
                                                                    <i class="fas fa-download"></i>
                                                                </a>
                                                            </div>
                                                        </div>
                                                    </c:forEach>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Pagination -->
                <c:if test="${totalPages > 1}">
                    <div class="pagination-container">
                        <div class="pagination">
                            <c:if test="${currentPage > 1}">
                                <a href="<c:url value='/doctor/appointments/${patientId}/history?page=${currentPage - 1}' />"
                                   class="pagination-btn pagination-prev">
                                    <i class="fas fa-chevron-left"></i>
                                        <%--                                    <span><spring:message code="pagination.previous" /></span>--%>
                                </a>
                            </c:if>

                            <div class="pagination-info">
                                    <%--                                <span><spring:message code="pagination.page" /> ${currentPage} <spring:message code="pagination.of" /> ${totalPages}</span>--%>
                                <span>${currentPage} - ${totalPages}</span>
                            </div>

                            <c:if test="${currentPage < totalPages}">
                                <a href="<c:url value='/doctor/appointments/${patientId}/history?page=${currentPage + 1}' />"
                                   class="pagination-btn pagination-next">
                                        <%--                                    <span><spring:message code="pagination.next" /></span>--%>
                                    <i class="fas fa-chevron-right"></i>
                                </a>
                            </c:if>
                        </div>
                    </div>
                </c:if>
                <div class="back-button-container">
                    <a href="<c:url value='${pageContext.request.contextPath}/doctor/dashboard/'/>" class="back-button">
                        <i class="fas fa-arrow-left"></i>
                        <span><spring:message code="appointment.details.back"/></span>
                    </a>
                </div>
            </div>
        </div>
    </div>
</main>

<!-- Include the toast notification script -->
<script src="<c:url value='/js/toast-notification.js' />"></script>
<script src="<c:url value='/js/medical-history.js' />"></script>

</body>
</html>
