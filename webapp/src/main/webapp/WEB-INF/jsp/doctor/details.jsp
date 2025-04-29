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
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
<!-- Include the header -->
<jsp:include page="/WEB-INF/jsp/components/header.jsp" />

<!-- Main Content -->
<main class="main-content">
    <div class="container">
        <div class="appointment-details-container">
            <!-- Appointment Status Banner -->
            <div class="appointment-status-banner status-${appointment.status.toLowerCase()}">
                <div class="status-icon">
                    <i class="fas fa-${appointment.status == 'CONFIRMED' ? 'check-circle' : appointment.status == 'COMPLETED' ? 'clipboard-check' : 'times-circle'}"></i>
                </div>
                <div class="status-text">
                    <h1><spring:message code='${appointment.status}'/></h1>
                    <p>
                        <fmt:parseDate value="${appointment.date}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both" />
                        <fmt:formatDate value="${parsedDate}" pattern="EEEE, MMMM d, yyyy 'at' h:mm a" />
                    </p>
                </div>

                <!-- Check if appointment has passed -->
                <c:set var="now" value="<%= new java.util.Date() %>" />
                <fmt:parseDate value="${appointment.date}" pattern="yyyy-MM-dd'T'HH:mm" var="appointmentDate" type="both" />
                <c:if test="${appointmentDate.time < now.time}">
                    <div class="past-indicator">
                        <i class="fas fa-history"></i>
                        <spring:message code="appointment.details.past" />
                    </div>
                </c:if>
            </div>

            <!-- Appointment Progress -->
            <div class="appointment-progress">
                <div class="progress-track">
                    <div class="progress-bar" style="width: <c:choose>
                    <c:when test="${appointment.status == 'confirmado'}">50%</c:when>
                    <c:when test="${appointment.status == 'completado'}">100%</c:when>
                    <c:otherwise>0%</c:otherwise>
                    </c:choose>;">
                    </div>                </div>
                <div class="progress-steps">
                    <div class="progress-step ${appointment.status == 'confirmado' || appointment.status == 'completado' ? 'active' : ''}">
                        <div class="step-marker"><i class="fas fa-check-circle"></i></div>
                        <div class="step-label">Confirmed</div>
                    </div>
                    <div class="progress-step ${appointment.status == 'completado' ? 'active' : ''}">
                        <div class="step-marker"><i class="fas fa-clipboard-check"></i></div>
                        <div class="step-label">Completed</div>
                    </div>
                </div>
            </div>

            <!-- Appointment Details -->
            <section class="details-section">
                <h2 class="section-title"><i class="fas fa-info-circle"></i> <spring:message code="appointment.details.info.title" /></h2>

                <div class="details-list">
                    <div class="detail-item">
                        <div class="detail-label"><i class="fas fa-hashtag"></i> <spring:message code="appointment.details.info.id" /></div>
                        <div class="detail-value"><c:out value="${appointment.id}"/></div>
                    </div>

                    <div class="detail-item">
                        <div class="detail-label"><i class="fas fa-calendar-day"></i> <spring:message code="appointment.details.info.date" /></div>
                        <div class="detail-value">
                            <fmt:formatDate value="${parsedDate}" pattern="EEEE, MMMM d, yyyy" />
                        </div>
                    </div>

                    <div class="detail-item">
                        <div class="detail-label"><i class="fas fa-clock"></i> <spring:message code="appointment.details.info.time" /></div>
                        <div class="detail-value">
                            <fmt:formatDate value="${parsedDate}" pattern="h:mm a" />
                        </div>
                    </div>

                    <div class="detail-item">
                        <div class="detail-label"><i class="fas fa-stethoscope"></i> <spring:message code="appointment.details.info.specialty" /></div>
                        <div class="detail-value"><spring:message code="${appointment.specialty.key}" /></div>
                    </div>
                </div>

                <div class="reason-container">
                    <h3 class="reason-title"><i class="fas fa-comment-medical"></i> <spring:message code="appointment.details.info.reason" /></h3>
                    <div class="reason-content">
                        <c:out value="${appointment.reason}" />
                    </div>
                </div>
            </section>

            <!-- Participants Section -->
            <section class="details-section">
                <h2 class="section-title"><i class="fas fa-users"></i> <spring:message code="appointment.details.page.title" /></h2>

                <!-- Patient Information -->
                <div class="participant-container">
                    <div class="participant-header">
                        <i class="fas fa-user"></i>
                        <h3><spring:message code="appointment.details.patient.title" /></h3>
                    </div>

                    <div class="participant-profile">
                        <div class="profile-image">
                            <img src="<c:url value='/patient/${appointment.patient.id}/image'/>" alt="<c:out value="${appointment.patient.name}"/> <c:out value="${appointment.patient.lastName}"/>" />
                        </div>

                        <div class="profile-info">
                            <h4 class="profile-name"><c:out value="${appointment.patient.name}"/> <c:out value="${appointment.patient.lastName}"/></h4>
                        </div>
                    </div>

                    <div class="participant-contact">
                        <a href="mailto:<c:out value="${appointment.patient.email}"/>" class="contact-link">
                            <i class="fas fa-envelope"></i>
                            <span><c:out value="${appointment.patient.email}"/></span>
                        </a>
                        <a href="tel:<c:out value="${appointment.patient.phone}"/>" class="contact-link">
                            <i class="fas fa-phone"></i>
                            <span><c:out value="${appointment.patient.phone}"/></span>
                        </a>
                    </div>
                </div>
            </section>

            <!-- Files Section -->
            <c:if test="${not empty patientFiles}">
                <section class="details-section">
                    <h2 class="section-title"><i class="fas fa-file-medical"></i> <spring:message code="appointment.details.patient.files.title" /></h2>

                    <div class="files-list">
                        <c:forEach var="file" items="${patientFiles}">
                            <c:if test="${file.uploader_role == 'patient'}">
                                <div class="file-item">
                                    <div class="file-icon">
                                        <i class="far fa-file-pdf"></i>
                                    </div>
                                    <div class="file-info">
                                        <div class="file-name"><c:out value="${file.fileName}" /></div>
                                        <div class="file-date">
                                            <fmt:formatDate value="${file.uploadDate}" pattern="MMM d, yyyy" />
                                        </div>
                                    </div>
                                    <a href="<c:url value='/appointment/${appointment.id}/file/${file.id}'/>" class="file-download" download>
                                        <i class="fas fa-download"></i>
                                    </a>
                                </div>
                            </c:if>
                        </c:forEach>
                    </div>
                </section>
            </c:if>

            <!-- Doctor Files Section (Only show if appointment has passed) -->
            <c:if test="${appointmentDate.time < now.time}">
                    <section class="details-section">
                        <h2 class="section-title"><i class="fas fa-file-medical-alt"></i> <spring:message code="appointment.details.doctor.files.title" /></h2>

                        <form:form modelAttribute="doctorFileForm" method="post" action="${pageContext.request.contextPath}/appointment/${appointment.id}" class="appointment-form" enctype="multipart/form-data">
                            <!-- File Upload Section -->
                            <div class="form-group">
                                <label for="files">
                                    <spring:message code="appointment.form.files"  />
                                </label>
                                <div class="file-upload-container">
                                    <div class="file-upload-dropzone" id="dropZone">
                                        <div class="file-upload-icon">
                                            <i class="fas fa-cloud-upload-alt"></i>
                                        </div>
                                        <div class="file-upload-text">
                                            <p class="file-upload-primary"><spring:message code="file.upload.dragHere"/></p>
                                            <p class="file-upload-secondary"><spring:message code="file.upload.onlyPdf"/></p>
                                        </div>
                                        <form:input type="file" path="files" id="files" multiple="true" accept=".pdf" class="file-upload-input-hidden" />
                                    </div>
                                    <div id="filePreview" class="file-upload-preview"></div>
                                    <form:errors path="files" cssClass="error-message" />
                                </div>
                            </div>

                            <div class="form-group">
                                <button type="submit" class="btn btn-primary btn-block">
                            <span>
                                <i class="fas fa-calendar-check"></i>
                                <spring:message code="appointment.form.submit" />
                            </span>
                                </button>
                            </div>
                        </form:form>

                    </section>
                <!-- Review Section (Only show if appointment has passed) -->
            </c:if>

            <!-- Action Buttons -->
            <div class="appointment-actions">
                <a href="${pageContext.request.contextPath}/doctor/dashboard" class="btn btn-secondary">
                    <i class="fas fa-arrow-left"></i>
                    <span><spring:message code="appointment.details.back" /></span>
                </a>

                <div class="action-buttons">
                    <button class="btn btn-icon" id="printBtn">
                        <i class="fas fa-print"></i>
                    </button>
                    <button class="btn btn-icon" id="shareBtn">
                        <i class="fas fa-share-alt"></i>
                    </button>
                </div>
            </div>
        </div>
    </div>
</main>

<!-- JavaScript for the page -->
<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Print functionality
        const printBtn = document.getElementById('printBtn');
        if (printBtn) {
            printBtn.addEventListener('click', function() {
                window.print();
            });
        }

        // Share functionality
        const shareBtn = document.getElementById('shareBtn');
        if (shareBtn) {
            shareBtn.addEventListener('click', function() {
                if (navigator.share) {
                    navigator.share({
                        title: 'Appointment Details',
                        text: 'Check out my appointment details',
                        url: window.location.href
                    })
                        .catch(error => console.log('Error sharing:', error));
                } else {
                    // Fallback - copy to clipboard
                    const tempInput = document.createElement('input');
                    tempInput.value = window.location.href;
                    document.body.appendChild(tempInput);
                    tempInput.select();
                    document.execCommand('copy');
                    document.body.removeChild(tempInput);

                    // Show copied notification
                    const notification = document.createElement('div');
                    notification.className = 'copy-notification';
                    notification.textContent = 'Link copied to clipboard!';
                    document.body.appendChild(notification);

                    setTimeout(() => {
                        notification.classList.add('show');
                    }, 10);

                    setTimeout(() => {
                        notification.classList.remove('show');
                        setTimeout(() => {
                            document.body.removeChild(notification);
                        }, 300);
                    }, 2000);
                }
            });
        }

        // Star rating functionality
        const starInputs = document.querySelectorAll('.stars-container input');
        const ratingDisplay = document.querySelector('.rating-display');

        if (starInputs.length > 0 && ratingDisplay) {
            starInputs.forEach(input => {
                input.addEventListener('change', function() {
                    ratingDisplay.textContent = this.value + '.0';
                    ratingDisplay.classList.add('pulse');

                    setTimeout(() => {
                        ratingDisplay.classList.remove('pulse');
                    }, 500);
                });
            });
        }

        // Edit review button functionality
        const editReviewBtn = document.getElementById('editReviewBtn');
        if (editReviewBtn) {
            editReviewBtn.addEventListener('click', function() {
                const existingReview = document.querySelector('.existing-review');
                if (existingReview) {
                    existingReview.classList.add('slide-out');

                    setTimeout(() => {
                        window.location.href = '${pageContext.request.contextPath}/appointment/${appointment.id}/rating/edit';
                    }, 300);
                } else {
                    window.location.href = '${pageContext.request.contextPath}/appointment/${appointment.id}/rating/edit';
                }
            });
        }

        // File upload functionality
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

                this.classList.add('drop-success');
                setTimeout(() => {
                    this.classList.remove('drop-success');
                }, 1000);
            });

            doctorDropZone.addEventListener('click', function() {
                doctorFileInput.click();
            });

            doctorFileInput.addEventListener('change', function() {
                updateFilePreview(this, doctorFilePreview);

                doctorDropZone.classList.add('drop-success');
                setTimeout(() => {
                    doctorDropZone.classList.remove('drop-success');
                }, 1000);
            });

            function updateFilePreview(input, preview) {
                preview.innerHTML = '';

                if (input.files.length > 0) {
                    for (let i = 0; i < input.files.length; i++) {
                        const file = input.files[i];
                        const fileItem = document.createElement('div');
                        fileItem.className = 'preview-item';

                        fileItem.classList.add('slide-in');
                        setTimeout(() => {
                            fileItem.classList.remove('slide-in');
                        }, 500);

                        const fileIcon = document.createElement('div');
                        fileIcon.className = 'preview-icon';
                        fileIcon.innerHTML = '<i class="far fa-file-pdf"></i>';

                        const fileInfo = document.createElement('div');
                        fileInfo.className = 'preview-info';

                        const fileName = document.createElement('div');
                        fileName.className = 'preview-name';
                        fileName.textContent = file.name;

                        const fileSize = document.createElement('div');
                        fileSize.className = 'preview-size';
                        fileSize.textContent = formatFileSize(file.size);

                        fileInfo.appendChild(fileName);
                        fileInfo.appendChild(fileSize);

                        const removeBtn = document.createElement('button');
                        removeBtn.className = 'preview-remove';
                        removeBtn.innerHTML = '<i class="fas fa-times"></i>';
                        removeBtn.type = 'button';
                        removeBtn.addEventListener('click', function() {
                            fileItem.classList.add('slide-out');

                            setTimeout(() => {
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
                            }, 300);
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

        // Animate progress bar on load
        const progressBar = document.querySelector('.progress-bar');
        if (progressBar) {
            setTimeout(() => {
                progressBar.style.transition = 'width 1s ease-in-out';
                progressBar.style.width = progressBar.getAttribute('style').split(':')[1];
            }, 300);
        }

        // Reveal sections on scroll
        const sections = document.querySelectorAll('.details-section');

        const revealSection = (entries, observer) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('section-visible');
                    observer.unobserve(entry.target);
                }
            });
        };

        const sectionObserver = new IntersectionObserver(revealSection, {
            root: null,
            threshold: 0.15
        });

        sections.forEach(section => {
            sectionObserver.observe(section);
            section.classList.add('section-hidden');
        });
    });
</script>
</body>
</html>