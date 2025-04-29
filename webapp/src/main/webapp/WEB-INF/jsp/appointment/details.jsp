<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="icon" type="image/png" href="https://upload.wikimedia.org/wikipedia/commons/thumb/a/aa/V-logo.svg/2048px-V-logo.svg.png" />
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="appointment.details.page.title" /></title>
    <link rel="stylesheet" href="<c:url value='/css/appointment-details.css' />" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
<!-- Include the header -->
<jsp:include page="/WEB-INF/jsp/components/header.jsp" />
<%--TODO: acces from dashboard/upcoming and dashboard/history--%>

<!-- Main Content -->
<main class="main-content">
    <div class="container">
        <div class="appointment-details-container">
            <div class="appointment-header">
                <h1 class="appointment-title"><spring:message code="appointment.details.title" /></h1>
                <p class="appointment-subtitle"><spring:message code="appointment.details.subtitle" /></p>

                <!-- Appointment Status Badge -->
                <div class="appointment-status">
                    <span class="status-badge <c:out value="${appointment.status}"/>">
                        <spring:message code='${appointment.status}'/>
                    </span>
                    <span class="appointment-date">
                        <i class="far fa-calendar-alt"></i>
                        <fmt:parseDate value="${appointment.date}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both" />
                        <fmt:formatDate value="${parsedDate}" pattern="EEEE, MMMM d, yyyy 'at' h:mm a" />
                    </span>

                    <!-- Check if appointment has passed -->
                    <c:set var="now" value="<%= new java.util.Date() %>" />
                    <fmt:parseDate value="${appointment.date}" pattern="yyyy-MM-dd'T'HH:mm" var="appointmentDate" type="both" />
                    <c:if test="${appointmentDate.time < now.time}">
                        <span class="appointment-past-indicator">
                            <i class="fas fa-history"></i>
                            <spring:message code="appointment.details.past" />
                        </span>
                    </c:if>
                </div>
            </div>

            <div class="appointment-body">
                <div class="details-grid">
                    <!-- Doctor Information Card -->
                    <div class="details-card doctor-card">
                        <div class="card-header">
                            <h2><spring:message code="appointment.details.doctor.title" /></h2>
                        </div>
                        <div class="card-body">
                            <div class="profile-section">
                                <div class="profile-image">
                                    <img src="<c:url value='/doctor/${appointment.doctor.id}/image'/>" alt="<c:out value="${appointment.doctor.name}"/> <c:out value="${appointment.doctor.lastName}"/>" class="avatar">
                                </div>
                                <div class="profile-info">
                                    <h3 class="profile-name"><c:out value="${appointment.doctor.name}"/> <c:out value="${appointment.doctor.lastName}"/></h3>
                                    <div class="rating-display">
                                        <div class="stars">
                                            <c:forEach begin="1" end="5" var="star">
                                                <i class="fa${star <= appointment.doctor.rating ? ' fa-star' : star <= appointment.doctor.rating + 0.5 ? ' fa-star-half-alt' : 'r fa-star'}" aria-hidden="true"></i>
                                            </c:forEach>
                                        </div>
                                        <span class="rating-value"><c:out value="${appointment.doctor.rating}"/></span>
                                        <span class="rating-count">(<c:out value="${appointment.doctor.ratingCount}"/> <spring:message code="ratings" />)</span>
                                    </div>
                                    <p class="specialty-tag"><spring:message code="${appointment.specialty.key}" /></p>
                                </div>
                            </div>
                            <div class="contact-info">
                                <div class="contact-item">
                                    <span class="contact-icon"><i class="fas fa-envelope"></i></span>
                                    <span class="contact-text"><c:out value="${appointment.doctor.email}"/></span>
                                </div>
                                <div class="contact-item">
                                    <span class="contact-icon"><i class="fas fa-phone"></i></span>
                                    <span class="contact-text"><c:out value="${appointment.doctor.phone}"/></span>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Patient Information Card -->
                    <div class="details-card patient-card">
                        <div class="card-header">
                            <h2><spring:message code="appointment.details.patient.title" /></h2>
                        </div>
                        <div class="card-body">
                            <div class="profile-section">
                                <div class="profile-image">
                                    <img src="<c:url value='/patient/${appointment.patient.id}/image'/>" alt="<c:out value="${appointment.patient.name}"/> <c:out value="${appointment.patient.lastName}"/>" class="avatar">
                                </div>
                                <div class="profile-info">
                                    <h3 class="profile-name"><c:out value="${appointment.patient.name}"/> <c:out value="${appointment.patient.lastName}"/></h3>
                                </div>
                            </div>
                            <div class="contact-info">
                                <div class="contact-item">
                                    <span class="contact-icon"><i class="fas fa-envelope"></i></span>
                                    <span class="contact-text"><c:out value="${appointment.patient.email}"/></span>
                                </div>
                                <div class="contact-item">
                                    <span class="contact-icon"><i class="fas fa-phone"></i></span>
                                    <span class="contact-text"><c:out value="${appointment.patient.phone}"/></span>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Appointment Details Card -->
                    <div class="details-card appointment-info-card">
                        <div class="card-header">
                            <h2><spring:message code="appointment.details.info.title" /></h2>
                        </div>
                        <div class="card-body">
                            <div class="info-item">
                                <span class="info-label"><spring:message code="appointment.details.info.id" />:</span>
                                <span class="info-value"><c:out value="${appointment.id}"/></span>
                            </div>
                            <div class="info-item">
                                <span class="info-label"><spring:message code="appointment.details.info.date" />:</span>
                                <span class="info-value">
                                    <fmt:parseDate value="${appointment.date}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both" />
                                    <fmt:formatDate value="${parsedDate}" pattern="EEEE, MMMM d, yyyy" />
                                </span>
                            </div>
                            <div class="info-item">
                                <span class="info-label"><spring:message code="appointment.details.info.time" />:</span>
                                <span class="info-value">
                                    <fmt:parseDate value="${appointment.date}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedTime" type="both" />
                                    <fmt:formatDate value="${parsedTime}" pattern="h:mm a" />
                                </span>
                            </div>
                            <div class="info-item">
                                <span class="info-label"><spring:message code="appointment.details.info.status" />:</span>
                                <span class="status-badge <c:out value="${appointment.status}"/>">
                                            <spring:message code='${appointment.status}'/>
                                </span>
                            </div>
                            <div class="info-item">
                                <span class="info-label"><spring:message code="appointment.details.info.specialty" />:</span>
                                <span class="info-value"><spring:message code="${appointment.specialty.key}" /></span>
                            </div>
                            <div class="info-item reason-item">
                                <span class="info-label"><spring:message code="appointment.details.info.reason" />:</span>
                                <div class="reason-text">
                                    <c:out value="${appointment.reason}" />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Patient Files Section -->
                <c:if test="${not empty patientFiles}">
                    <div class="details-card files-card">
                        <div class="card-header">
                            <h2><spring:message code="appointment.details.patient.files.title" /></h2>
                        </div>
                        <div class="card-body">
                            <div class="files-grid">
                                <c:forEach var="file" items="${patientFiles}">
                                    <c:if test="${file.uploader_role == 'patient'}">
                                        <div class="file-item">
                                            <div class="file-icon">
                                                <i class="far fa-file-pdf"></i>
                                            </div>
                                            <div class="file-info">
                                                <span class="file-name"><c:out value="${file.fileName}" /></span>
                                                <span class="file-date">
                                                    <fmt:formatDate value="${file.uploadDate}" pattern="MMM d, yyyy" />
                                                </span>
                                            </div>
                                            <a href="<c:url value='/appointment/${appointment.id}/file/${file.id}'/>" class="file-download" download>
                                                <i class="fas fa-download"></i>
                                            </a>
                                        </div>
                                    </c:if>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </c:if>

                <!-- Check if appointment has passed -->
                <c:set var="now" value="<%= new java.util.Date() %>" />
                <fmt:parseDate value="${appointment.date}" pattern="yyyy-MM-dd'T'HH:mm" var="appointmentDate" type="both" />
                <c:if test="${appointmentDate.time < now.time}">
                    <!-- Doctor Files Section (Only show if appointment has passed) -->
                    <sec:authorize access="hasRole('DOCTOR')">
                        <div class="details-card files-card doctor-files-card">
                            <div class="card-header">
                                <h2><spring:message code="appointment.details.doctor.files.title" /></h2>
                            </div>
                            <div class="card-body">
<%--                                <form:form modelAttribute="doctorFileForm" method="post" action="${pageContext.request.contextPath}/appointment/${appointment.id}/doctor-files" class="doctor-files-form" enctype="multipart/form-data">--%>
<%--                                    <div class="form-group">--%>
<%--                                        <label for="doctorFiles">--%>
<%--                                            <spring:message code="appointment.details.doctor.files.upload" />--%>
<%--                                        </label>--%>
<%--                                        <div class="file-upload-container">--%>
<%--                                            <div class="file-upload-dropzone" id="doctorDropZone">--%>
<%--                                                <div class="file-upload-icon">--%>
<%--                                                    <i class="fas fa-cloud-upload-alt"></i>--%>
<%--                                                </div>--%>
<%--                                                <div class="file-upload-text">--%>
<%--                                                    <p class="file-upload-primary"><spring:message code="file.upload.dragHere"/></p>--%>
<%--                                                    <p class="file-upload-secondary"><spring:message code="file.upload.onlyPdf"/></p>--%>
<%--                                                </div>--%>
<%--                                                <form:input type="file" path="files" id="doctorFiles" multiple="true" accept=".pdf" class="file-upload-input-hidden" />--%>
<%--                                            </div>--%>
<%--                                            <div id="doctorFilePreview" class="file-upload-preview"></div>--%>
<%--                                            <form:errors path="files" cssClass="error-message" />--%>
<%--                                        </div>--%>
<%--                                    </div>--%>

<%--                                    <div class="form-group">--%>
<%--                                        <label for="doctorComment">--%>
<%--                                            <spring:message code="appointment.details.doctor.comment" />--%>
<%--                                        </label>--%>
<%--                                        <form:textarea path="comment" id="doctorComment" class="form-control" rows="4" />--%>
<%--                                        <form:errors path="comment" cssClass="error-message" />--%>
<%--                                    </div>--%>

<%--                                    <div class="form-group">--%>
<%--                                        <button type="submit" class="btn btn-primary">--%>
<%--                                            <span>--%>
<%--                                                <i class="fas fa-paper-plane"></i>--%>
<%--                                                <spring:message code="appointment.details.doctor.submit" />--%>
<%--                                            </span>--%>
<%--                                        </button>--%>
<%--                                    </div>--%>
<%--                                </form:form>--%>
                            </div>
                        </div>
                    </sec:authorize>

                    <!-- Patient Review Section (Only show if appointment has passed OR CANCELED) -->
<%--                    TODO: show if has been canceled as well--%>
                    <sec:authorize access="hasRole('PATIENT')">
                        <div class="details-card review-card">
                            <div class="card-header">
                                <h2><spring:message code="appointment.details.review.title" /></h2>
                            </div>
                            <div class="card-body">
                                <c:choose>
                                    <c:when test="${not empty existingRating}">
                                        <!-- Display existing rating -->
                                        <div class="existing-review">
                                            <div class="review-header">
                                                <div class="review-stars">
                                                    <c:forEach begin="1" end="5" var="star">
                                                        <i class="fa${star <= existingRating.rating ? ' fa-star' : star <= existingRating.rating + 0.5 ? ' fa-star-half-alt' : 'r fa-star'}" aria-hidden="true"></i>
                                                    </c:forEach>
                                                    <span class="rating-value"><c:out value="${existingRating.rating}"/></span>
                                                </div>
                                                <div class="review-date">
                                                    <fmt:formatDate value="${existingRating.date}" pattern="MMM d, yyyy" />
                                                </div>
                                            </div>
                                            <div class="review-comment">
                                                <c:out value="${existingRating.comment}" />
                                            </div>
                                            <div class="review-actions">
                                                <button id="editReviewBtn" class="btn btn-secondary">
                                                    <i class="fas fa-edit"></i>
                                                    <spring:message code="appointment.details.review.edit" />
                                                </button>
                                            </div>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <h1>NOT IMPLEMENTED</h1>
                                        <!-- Review form -->
<%--                                        <form:form modelAttribute="ratingForm" method="post" action="${pageContext.request.contextPath}/appointment/${appointment.id}/rating" class="review-form">--%>
<%--                                            <div class="form-group">--%>
<%--                                                <label for="rating" class="required-field">--%>
<%--                                                    <spring:message code="appointment.details.review.rating" />--%>
<%--                                                </label>--%>
<%--                                                <div class="star-rating">--%>
<%--                                                    <div class="stars-input">--%>
<%--                                                        <c:forEach begin="1" end="5" var="star">--%>
<%--                                                            <input type="radio" id="star${star}" name="rating" value="${star}" <c:if test="${star == 5}">checked</c:if> />--%>
<%--                                                            <label for="star${star}"><i class="fas fa-star"></i></label>--%>
<%--                                                        </c:forEach>--%>
<%--                                                    </div>--%>
<%--                                                    <div class="rating-value-display">5.0</div>--%>
<%--                                                </div>--%>
<%--                                                <form:errors path="rating" cssClass="error-message" />--%>
<%--                                            </div>--%>

<%--                                            <div class="form-group">--%>
<%--                                                <label for="comment" class="required-field">--%>
<%--                                                    <spring:message code="appointment.details.review.comment" />--%>
<%--                                                </label>--%>
<%--                                                <form:textarea path="comment" id="comment" class="form-control" rows="4" />--%>
<%--                                                <form:errors path="comment" cssClass="error-message" />--%>
<%--                                            </div>--%>

<%--                                            <div class="form-group">--%>
<%--                                                <button type="submit" class="btn btn-primary">--%>
<%--                                                    <span>--%>
<%--                                                        <i class="fas fa-star"></i>--%>
<%--                                                        <spring:message code="appointment.details.review.submit" />--%>
<%--                                                    </span>--%>
<%--                                                </button>--%>
<%--                                            </div>--%>
<%--                                        </form:form>--%>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </sec:authorize>
                </c:if>
            </div>

            <!-- Action Buttons -->
            <sec:authorize access="hasRole('DOCTOR')">
                <div class="appointment-actions">
                    <a href="${pageContext.request.contextPath}/doctor/dashboard/upcoming" class="btn btn-secondary">
                        <i class="fas fa-arrow-left"></i>
                        <spring:message code="appointment.details.back" />
                    </a>

                    <!-- Only show cancel button if appointment is in the future and status is SCHEDULED or CONFIRMED -->
                    <c:if test="${(appointment.status == 'SCHEDULED' || appointment.status == 'CONFIRMED') && appointmentDate.time > now.time}">
                        <a href="${pageContext.request.contextPath}/appointment/${appointment.id}/cancel" class="btn btn-danger">
                            <i class="fas fa-times-circle"></i>
                            <spring:message code="appointment.details.cancel" />
                        </a>
                    </c:if>
                </div>
            </sec:authorize>
            <sec:authorize access="hasRole('PATIENT')">
                <div class="appointment-actions">
                    <a href="${pageContext.request.contextPath}/patient/dashboard/upcoming" class="btn btn-secondary">
                        <i class="fas fa-arrow-left"></i>
                        <spring:message code="appointment.details.back" />
                    </a>

                    <!-- Only show cancel button if appointment is in the future and status is SCHEDULED or CONFIRMED -->
                    <c:if test="${(appointment.status == 'SCHEDULED' || appointment.status == 'CONFIRMED') && appointmentDate.time > now.time}">
                        <a href="${pageContext.request.contextPath}/appointment/${appointment.id}/cancel" class="btn btn-danger">
                            <i class="fas fa-times-circle"></i>
                            <spring:message code="appointment.details.cancel" />
                        </a>
                    </c:if>
                </div>
            </sec:authorize>

        </div>
    </div>
</main>

<!-- JavaScript for the page -->
<script>
    // Handle file uploads for doctor
    document.addEventListener('DOMContentLoaded', function() {
        // Fixed header adjustment
        const fixedHeader = document.querySelector(".main-header");
        const mainContent = document.querySelector("main");

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

        // Star rating functionality
        const starInputs = document.querySelectorAll('.stars-input input');
        const ratingDisplay = document.querySelector('.rating-value-display');

        if (starInputs.length > 0 && ratingDisplay) {
            starInputs.forEach(input => {
                input.addEventListener('change', function() {
                    ratingDisplay.textContent = this.value + '.0';
                });
            });
        }

        // Edit review button functionality
        const editReviewBtn = document.getElementById('editReviewBtn');
        if (editReviewBtn) {
            editReviewBtn.addEventListener('click', function() {
                window.location.href = '${pageContext.request.contextPath}/appointment/${appointment.id}/rating/edit';
            });
        }

        // Doctor file upload functionality
        const doctorDropZone = document.getElementById('doctorDropZone');
        const doctorFileInput = document.getElementById('doctorFiles');
        const doctorFilePreview = document.getElementById('doctorFilePreview');

        if (doctorDropZone && doctorFileInput && doctorFilePreview) {
            // Drag and drop functionality
            doctorDropZone.addEventListener('dragover', function(e) {
                e.preventDefault();
                this.classList.add('dragover');
            });

            doctorDropZone.addEventListener('dragleave', function() {
                this.classList.remove('dragover');
            });

            doctorDropZone.addEventListener('drop', function(e) {
                e.preventDefault();
                this.classList.remove('dragover');
                doctorFileInput.files = e.dataTransfer.files;
                updateFilePreview(doctorFileInput, doctorFilePreview);
            });

            doctorDropZone.addEventListener('click', function() {
                doctorFileInput.click();
            });

            doctorFileInput.addEventListener('change', function() {
                updateFilePreview(this, doctorFilePreview);
            });

            function updateFilePreview(input, preview) {
                preview.innerHTML = '';

                if (input.files.length > 0) {
                    for (let i = 0; i < input.files.length; i++) {
                        const file = input.files[i];
                        const fileItem = document.createElement('div');
                        fileItem.className = 'file-preview-item';

                        const fileIcon = document.createElement('div');
                        fileIcon.className = 'file-preview-icon';
                        fileIcon.innerHTML = '<i class="far fa-file-pdf"></i>';

                        const fileInfo = document.createElement('div');
                        fileInfo.className = 'file-preview-info';

                        const fileName = document.createElement('span');
                        fileName.className = 'file-preview-name';
                        fileName.textContent = file.name;

                        const fileSize = document.createElement('span');
                        fileSize.className = 'file-preview-size';
                        fileSize.textContent = formatFileSize(file.size);

                        fileInfo.appendChild(fileName);
                        fileInfo.appendChild(fileSize);

                        const removeBtn = document.createElement('button');
                        removeBtn.className = 'file-preview-remove';
                        removeBtn.innerHTML = '<i class="fas fa-times"></i>';
                        removeBtn.type = 'button';
                        removeBtn.addEventListener('click', function() {
                            // Remove this file from the input
                            const dataTransfer = new DataTransfer();
                            const files = input.files;

                            for (let j = 0; j < files.length; j++) {
                                if (j !== i) {
                                    dataTransfer.items.add(files[j]);
                                }
                            }

                            input.files = dataTransfer.files;
                            updateFilePreview(input, preview);
                        });

                        fileItem.appendChild(fileIcon);
                        fileItem.appendChild(fileInfo);
                        fileItem.appendChild(removeBtn);

                        preview.appendChild(fileItem);
                    }
                }
            }

            function formatFileSize(bytes) {
                if (bytes === 0) return '0 Bytes';
                const k = 1024;
                const sizes = ['Bytes', 'KB', 'MB', 'GB'];
                const i = Math.floor(Math.log(bytes) / Math.log(k));
                return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
            }
        }
    });
</script>

</body>
</html>