<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat, java.util.Date, java.util.TimeZone" %>
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


<!-- Main Content -->
<main class="main-content">
    <div class="container">
        <div class="appointment-container">
            <div class="appointment-header">
                <h1 class="appointment-title"><spring:message code="appointment.details.page.title" /></h1>
                <p class="appointment-subtitle"><spring:message code="appointment.details.subtitle" /></p>
            </div>

            <div class="appointment-body">
                <c:set var="doctor" value="${appointment.doctor}" />
                <!-- Display doctor information if available -->
                <c:if test="${not empty doctor}">
                    <div class="doctor-info">
                        <div class="doctor-image">
                            <img src="<c:url value='/image/${doctor.imageId}'/>" alt="<c:out value="${doctor.name}"/> <c:out value="${doctor.lastName}"/>" class="doctor-avatar">
                        </div>
                        <div class="doctor-details">
                            <h3 class="doctor-name"><c:out value="${doctor.name}"/> <c:out value="${doctor.lastName}"/></h3>
                            <div class="card-specialty-list">
                                <c:forEach var="specialty" items="${doctor.specialtyList}" varStatus="status">
                                    <p class="doctor-specialty"><spring:message code="${specialty.key}" /></p>
                                    <c:if test="${!status.last}">
                                        <span style="white-space: pre">, </span>
                                    </c:if>
                                </c:forEach>
                            </div>
                            <div class="doctor-contact">
                                <div class="contact-item">
                                    <span class="contact-icon"><i class="fas fa-envelope"></i></span>
                                    <span class="contact-text"><c:out value="${doctor.email}"/></span>
                                </div>
                                <div class="contact-item">
                                    <span class="contact-icon"><i class="fas fa-phone"></i></span>
                                    <span class="contact-text"><c:out value="${doctor.phone}"/></span>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>

                <!-- Status field -->
                <div class="specialty-card-appointment status-card">
                    <div class="specialty-icon-appointment">
                        <i class="fas fa-clipboard-check"></i>
                    </div>
                    <div class="specialty-content">
                        <span class="specialty-label-appointment">
                            <spring:message code="appointment.details.info.status" text="Status" />:
                        </span>
                        <div class="status-display">
                        <span class="specialty-value-appointment status-${appointment.status}">
                            <spring:message code="appointment.status.${appointment.status}" text="${appointment.status}" />
                        </span>
                        </div>
                    </div>
                </div>

                <!-- Improved date display -->
                <div class="specialty-card-appointment">
                    <div class="specialty-icon-appointment">
                        <i class="fas fa-calendar"></i>
                    </div>
                    <div class="specialty-content">
                        <span class="specialty-label-appointment"><spring:message code="appointment.form.date" text="Date" />:</span>
                        <div class="enhanced-date-display">
                            <div class="date-time-info">
                                <div class="date-info">
                                    <i class="fas fa-calendar-alt"></i>
                                    <c:out value="${appointment.date.toLocalDate()}"/>
                                </div>
                                <div class="time-info">
                                    <i class="fas fa-clock"></i>
                                    <c:out value="${appointment.date.toLocalTime()}"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="specialty-card-appointment">
                    <div class="specialty-icon-appointment">
                        <i class="fas fa-stethoscope"></i>
                    </div>
                    <div class="specialty-content">
                        <span class="specialty-label-appointment"><spring:message code="appointment.form.specialty" />:</span>
                        <span class="specialty-value-appointment"><spring:message code="${appointment.specialty.key}" /></span>
                    </div>
                </div>

                <div class="specialty-card-appointment">
                    <div class="specialty-icon-appointment">
                        <i class="fas fa-comment"></i>
                    </div>
                    <div class="specialty-content">
                        <span class="specialty-label-appointment"><spring:message code="appointment.form.reason" />:</span>
                        <span class="specialty-value-appointment"><c:out value="${appointment.reason}"/> </span>
                    </div>
                </div>

                <!-- Patient Files Section -->
                <div class="files-section">
                    <h2 class="files-title">
                        <i class="fas fa-file-medical"></i>
                        <spring:message code="appointment.details.yours.files.title"/>
                    </h2>

                    <div class="files-list">
                        <c:set var="hasPatientFiles" value="false" />
                        <c:forEach var="file" items="${patientFiles}">
                            <c:if test="${file.uploader_role == 'patient'}">
                                <c:set var="hasPatientFiles" value="true" />
                                <div class="file-item">
                                    <div class="file-icon">
                                        <i class="far fa-file-pdf"></i>
                                    </div>
                                    <div class="file-info">
                                        <div class="file-name"><c:out value="${file.fileName}" /></div>
                                    </div>
                                    <a href="<c:url value='/appointment/${appointment.id}/file/${file.id}'/>" class="file-download" download>
                                        <i class="fas fa-download"></i>
                                    </a>
                                </div>
                            </c:if>
                        </c:forEach>

                        <c:if test="${not hasPatientFiles}">
                            <div class="no-files-message">
                                <div class="no-files-content">
                                    <i class="fas fa-info-circle"></i>
                                    <p><spring:message code="appointment.details.you.nofiles" /></p>
                                </div>
                            </div>
                        </c:if>
                    </div>
                </div>

                <!-- Check if appointment has passed -->
                <c:if test="${appointment.status == 'completo'}">
                    <c:set var="appointmentPassed" value="true" />
                <sec:authorize access="hasRole('PATIENT')">
                    <h2 class="rating-title">
                        <i class="fas fa-star"></i>
                        <spring:message code="appointment.details.review.title" />
                    </h2>
                        <div class="rating-section">
                            <c:choose>
                                <c:when test="${not empty existingRating}">
                                    <!-- Display existing rating -->
                                    <div class="existing-review">
                                        <div class="review-header">
                                            <div class="review-stars">
                                                <c:forEach begin="1" end="5" var="star">
                                                    <i class="fa${star <= existingRating.rating ? 's' : 'r'} fa-star" aria-hidden="true"></i>
                                                </c:forEach>
                                                <span class="rating-value"><c:out value="${existingRating.rating}"/></span>
                                            </div>
                                        </div>
                                        <div class="review-comment">
                                            <c:out value="${existingRating.comment}" />
                                        </div>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <!-- Review form -->
                                    <form:form modelAttribute="patientRatingForm" method="post" action="${pageContext.request.contextPath}/patient/dashboard/appointment/rate" class="rating-form" id="ratingForm">
                                        <form:hidden path="appointmentId" value="${appointment.id}" />

                                        <div class="form-group">
                                            <label class="required-field">
                                                <spring:message code="appointment.details.review.rating" />
                                            </label>
                                            <div class="star-rating">
                                                <div class="stars-container">
                                                    <c:forEach begin="1" end="5" var="star">
                                                        <form:radiobutton path="rating" id="star${star}" value="${star}" cssClass="star-input" />
                                                        <label for="star${star}" class="star-label" data-value="${star}"><i class="fas fa-star"></i></label>
                                                    </c:forEach>
                                                </div>
                                                <div class="rating-display">0.0</div>
                                            </div>
                                            <form:errors path="rating" cssClass="error-message" />
                                        </div>

                                        <div class="form-group">
                                            <label for="comment" class="required-field">
                                                <spring:message code="appointment.details.review.comment" />
                                            </label>
                                            <form:textarea path="comment" id="comment" class="form-control" rows="4" />
                                            <form:errors path="comment" cssClass="error-message" />
                                        </div>

                                        <div class="form-group">
                                            <button type="submit" class="btn btn-primary">
                                                <i class="fas fa-star"></i>
                                                <spring:message code="appointment.details.review.submit" />
                                            </button>
                                        </div>
                                    </form:form>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </sec:authorize>


                <!-- Doctor Files Section -->
                <div class="files-section">
                    <h2 class="files-title">
                        <i class="fas fa-file-medical-alt"></i>
                        <spring:message code="appointment.details.doctor.files.title"/>
                    </h2>

                    <c:choose>
                    <c:when test="${empty appointment.report}">
                        <div class="specialty-card-appointment">
                            <div class="no-files-content">
                                <i class="fas fa-info-circle"></i>
                                <p><spring:message code="appointment.details.doctor.noreport" /></p>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="specialty-card-appointment">
                            <div class="specialty-icon-appointment">
                                <i class="fas fa-asterisk"></i>
                            </div>
                            <div class="specialty-content">
                                <span class="specialty-value-appointment"><c:out value="${appointment.report}"/> </span>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>

                    <div class="files-list">

                        <c:set var="hasDoctorFiles" value="false" />
                        <c:forEach var="file" items="${doctorFiles}">
                            <c:if test="${file.uploader_role == 'doctor'}">
                                <c:set var="hasDoctorFiles" value="true" />
                                <div class="file-item">
                                    <div class="file-icon">
                                        <i class="far fa-file-pdf"></i>
                                    </div>
                                    <div class="file-info">
                                        <div class="file-name"><c:out value="${file.fileName}" /></div>
                                    </div>
                                    <a href="<c:url value='/appointment/${appointment.id}/file/${file.id}'/>" class="file-download" download>
                                        <i class="fas fa-download"></i>
                                    </a>
                                </div>
                            </c:if>
                        </c:forEach>

                        <c:if test="${not hasDoctorFiles}">
                            <div class="no-files-message">
                                <div class="no-files-content">
                                    <i class="fas fa-info-circle"></i>
                                    <p><spring:message code="appointment.details.doctor.nofiles" /></p>
                                </div>
                            </div>
                        </c:if>
                    </div>
                </div>
                </c:if>

                <div class="back-button-container">
                    <a href="${pageContext.request.contextPath}/patient/dashboard" class="back-button">
                        <i class="fas fa-arrow-left"></i>
                        <span><spring:message code="appointment.details.back" text="Back to My Appointments" /></span>
                    </a>
                </div>
            </div>
        </div>
    </div>
    <!-- Toast notification -->
    <div id="toast-notification" class="toast-notification">
        <div class="toast-icon">
            <i class="fas fa-check-circle"></i>
        </div>
        <div class="toast-content">
            <div class="toast-title"><spring:message code="appointment.rating.success" text="Success!" /></div>
            <div class="toast-message"><spring:message code="appointment.rating.success" text="Your rating has been submitted successfully." /></div>
        </div>
        <button class="toast-close">
            <i class="fas fa-times"></i>
        </button>
    </div>
</main>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Star rating functionality
        const starInputs = document.querySelectorAll('.star-input');
        const ratingDisplay = document.querySelector('.rating-display');
        const starLabels = document.querySelectorAll('.star-label');

        if (starInputs.length > 0 && ratingDisplay) {
            // Initialize star rating
            starInputs.forEach((input, index) => {
                input.addEventListener('change', function() {
                    const value = parseInt(this.value);
                    ratingDisplay.textContent = value + '.0';

                    // Update visual state of stars - left to right
                    starLabels.forEach((label) => {
                        const starValue = parseInt(label.getAttribute('data-value'));
                        if (starValue <= value) {
                            label.classList.add('selected');
                        } else {
                            label.classList.remove('selected');
                        }
                    });
                });
            });

            // Add hover effects - left to right
            starLabels.forEach((label) => {
                label.addEventListener('mouseenter', function() {
                    const value = parseInt(this.getAttribute('data-value'));

                    // Preview stars on hover
                    starLabels.forEach((l) => {
                        const starValue = parseInt(l.getAttribute('data-value'));
                        if (starValue <= value) {
                            l.classList.add('hover');
                        } else {
                            l.classList.remove('hover');
                        }
                    });
                });

                label.addEventListener('mouseleave', function() {
                    // Remove hover class from all stars
                    starLabels.forEach(l => l.classList.remove('hover'));

                    // Restore selected stars
                    const selectedInput = document.querySelector('.star-input:checked');
                    if (selectedInput) {
                        const selectedValue = parseInt(selectedInput.value);
                        starLabels.forEach((l) => {
                            const starValue = parseInt(l.getAttribute('data-value'));
                            if (starValue <= selectedValue) {
                                l.classList.add('selected');
                            } else {
                                l.classList.remove('selected');
                            }
                        });
                    }
                });
            });
        }

        // Handle form submission with AJAX
        const ratingForm = document.getElementById('ratingForm');
        if (ratingForm) {
            ratingForm.addEventListener('submit', function(e) {
                e.preventDefault();

                // Get form data
                const formData = new FormData(ratingForm);
                const url = ratingForm.getAttribute('action');

                // Send AJAX request
                fetch(url, {
                    method: 'POST',
                    body: formData
                })
                    .then(response => {
                        if (response.ok) {
                            // Show toast notification
                            showToast();

                            // Reload the page after a short delay
                            setTimeout(() => {
                                window.location.reload();
                            }, 1500);
                        } else {
                            console.error('Error submitting rating');
                        }
                    })
                    .catch(error => {
                        console.error('Error:', error);
                    });
            });
        }

        // Toast notification functionality
        const toast = document.getElementById('toast-notification');
        const toastClose = document.querySelector('.toast-close');

        function showToast() {
            toast.classList.add('show');

            // Auto-hide after 5 seconds
            setTimeout(() => {
                hideToast();
            }, 5000);
        }

        function hideToast() {
            toast.classList.remove('show');
        }

        if (toastClose) {
            toastClose.addEventListener('click', hideToast);
        }

        // Check if there's a URL parameter indicating a successful rating
        const urlParams = new URLSearchParams(window.location.search);
        if (urlParams.has('rated') && urlParams.get('rated') === 'true') {
            // Show toast notification
            showToast();

            // Remove the parameter from URL without refreshing
            const url = new URL(window.location);
            url.searchParams.delete('rated');
            window.history.replaceState({}, '', url);
        }
    });
</script>

<!-- Include the external JavaScript file -->
<script src="<c:url value='/js/file-upload.js'/>"></script>
</body>
</html>