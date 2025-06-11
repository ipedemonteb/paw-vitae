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
<jsp:include page="/WEB-INF/jsp/components/header.jsp" />
<jsp:include page="/WEB-INF/jsp/components/modal.jsp">
    <jsp:param name="id" value="cancelAppointmentModal"/>
    <jsp:param name="confirm" value="appointment.action.cancel"/>
    <jsp:param name="title" value="appointment.cancel.title"/>
    <jsp:param name="message" value="appointment.cancel.message"/>
    <jsp:param name="actionPath" value="/patient/dashboard/appointment/cancel"/>
    <jsp:param name="divId" value="cancelModal"/>
    <jsp:param name="formId" value="cancelForm"/>
    <jsp:param name="buttonId" value="cancelAppointmentBtn"/>
    <jsp:param name="buttonClass" value="cancel-appointment"/>
</jsp:include>



<main class="main-content">
    <div class="container">
        <div class="appointment-container">
            <div class="appointment-header">
                <h1 class="appointment-title"><spring:message code="appointment.details.page.title" /></h1>
                <p class="appointment-subtitle"><spring:message code="appointment.details.subtitle" /></p>
            </div>

            <div class="appointment-body">
                <c:set var="doctor" value="${appointment.doctor}" />
                <c:if test="${not empty doctor}">
                    <div class="doctor-info">
                        <div class="doctor-image">
                            <img src="<c:url value='/image/${empty doctor.imageId ? -1 : doctor.imageId}'/>" alt="<c:out value="${doctor.name}"/> <c:out value="${doctor.lastName}"/>" class="doctor-avatar">
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
                            <spring:message code="${appointment.status}" />
                        </span>
                        </div>
                    </div>
                </div>

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
                        <i class="fas fa-building"></i>
                    </div>
                    <div class="specialty-content">
                        <span class="specialty-label-appointment"><spring:message
                                code="appointment.form.office"/>:</span>
                        <span class="specialty-value-appointment">
                            <c:out value="${office.officeName} - ${office.neighborhood.name}"/>
                        </span>
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

                <div class="files-section">
                    <h2 class="files-title">
                        <i class="fas fa-file-medical"></i>
                        <spring:message code="appointment.details.yours.files.title"/>
                    </h2>

                    <div class="files-list">
                        <c:set var="hasPatientFiles" value="false" />
                        <c:forEach var="file" items="${patientFiles}">
                            <c:if test="${file.uploaderRole == 'patient'}">
                                <c:set var="hasPatientFiles" value="true" />
                                <div class="file-item">
                                    <div class="file-icon">
                                        <i class="far fa-file-pdf"></i>
                                    </div>
                                    <div class="file-info">
                                        <div class="file-name"><c:out value="${file.fileName}" /></div>
                                    </div>
                                    <a href="<c:url value='/appointment/${appointment.id}/file-view/${file.id}' />"
                                       class="btn-view-file"
                                       target="_blank"
                                       title="<spring:message code='dashboard.medicalHistory.viewFile' />">
                                        <i class="fas fa-eye"></i>
                                        <span><spring:message code="dashboard.medicalHistory.view" /></span>
                                    </a>
                                    <a href="<c:url value='/appointment/${appointment.id}/file/${file.id}'/>"
                                       class="file-download" download>
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

                <c:choose>
                    <c:when test="${appointment.status == 'completo'}">
                        <%-- Rating Section (Unlocked) --%>
                        <sec:authorize access="hasRole('PATIENT')">
                            <h2 class="rating-title">
                                <i class="fas fa-star"></i>
                                <spring:message code="appointment.details.review.title" />
                            </h2>
                            <div class="rating-section">
                                <c:choose>
                                    <c:when test="${not empty existingRating}">
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
                                        <form:form modelAttribute="patientRatingForm" method="post" action="${pageContext.request.contextPath}/patient/dashboard/appointment/rate" class="rating-form" id="ratingForm">
                                            <form:hidden path="appointmentId" value="${appointment.id}" />
                                            <form:hidden path="doctorId" value="${doctor.id}" />

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
                                                <label for="comment">
                                                    <spring:message code="appointment.details.review.comment" />
                                                </label>
                                                <form:textarea path="comment" id="comment" class="form-control" rows="4" />
                                                <form:errors path="comment" cssClass="error-message" />
                                            </div>

                                            <div class="form-group">
                                                <button id="submitButton" type="submit" class="btn btn-primary">
                                                    <i class="fas fa-star"></i>
                                                    <spring:message code="appointment.details.review.submit" />
                                                </button>
                                            </div>
                                        </form:form>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </sec:authorize>

                        <%-- Doctor Files Section (Unlocked) --%>
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
                                    <c:if test="${file.uploaderRole == 'doctor'}">
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
                    </c:when>
                    <c:otherwise>
                        <c:if test="${!isCancelled}">
                            <%-- Rating Section (Locked) --%>
                            <div class="locked-section">
                                <div class="locked-section-header">
                                    <div class="locked-section-icon">
                                        <i class="fas fa-lock"></i>
                                    </div>
                                    <h3 class="locked-section-title">
                                        <spring:message code="appointment.locked.rating.title"/>
                                    </h3>
                                </div>
                                <p class="locked-section-message">
                                    <spring:message code="appointment.locked.rating.message"/>
                                </p>
                                <div class="locked-section-content">
                                    <h2 class="rating-title">
                                        <i class="fas fa-star"></i>
                                        <spring:message code="appointment.details.review.title" />
                                    </h2>
                                    <div class="rating-section">
                                        <div class="form-group">
                                            <label class="required-field">
                                                <spring:message code="appointment.details.review.rating" />
                                            </label>
                                            <div class="star-rating">
                                                <div class="stars-container">
                                                    <c:forEach begin="1" end="5" var="star">
                                                        <input type="radio" id="star${star}" value="${star}" class="star-input" disabled />
                                                        <label for="star${star}" class="star-label" data-value="${star}"><i class="fas fa-star"></i></label>
                                                    </c:forEach>
                                                </div>
                                                <div class="rating-display">0.0</div>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label for="comment-disabled">
                                                <spring:message code="appointment.details.review.comment" />
                                            </label>
                                            <textarea id="comment-disabled" class="form-control" rows="4" disabled></textarea>
                                        </div>
                                        <form:errors path="" cssClass="error-message" />
                                        <div class="form-group">
                                            <button type="button" class="btn btn-primary" disabled>
                                                <i class="fas fa-star"></i>
                                                <spring:message code="appointment.details.review.submit" />
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <%-- Doctor Files Section (Locked) --%>
                            <div class="locked-section">
                                <div class="locked-section-header">
                                    <div class="locked-section-icon">
                                        <i class="fas fa-lock"></i>
                                    </div>
                                    <h3 class="locked-section-title">
                                        <spring:message code="appointment.locked.doctor.files.title"/>
                                    </h3>
                                </div>
                                <p class="locked-section-message">
                                    <spring:message code="appointment.locked.doctor.files.message"/>
                                </p>
                                <div class="locked-section-content">
                                    <h2 class="files-title">
                                        <i class="fas fa-file-medical-alt"></i>
                                        <spring:message code="appointment.details.doctor.files.title"/>
                                    </h2>

                                    <div class="specialty-card-appointment">
                                        <div class="no-files-content">
                                            <i class="fas fa-info-circle"></i>
                                            <p><spring:message code="appointment.details.doctor.noreport" /></p>
                                        </div>
                                    </div>

                                    <div class="files-list">
                                        <div class="no-files-message">
                                            <div class="no-files-content">
                                                <i class="fas fa-info-circle"></i>
                                                <p><spring:message code="appointment.details.doctor.nofiles" /></p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                    </c:otherwise>
                </c:choose>

                <div class="back-button-container">
                    <a href="${pageContext.request.contextPath}/patient/dashboard" class="back-button">
                        <i class="fas fa-arrow-left"></i>
                        <span><spring:message code="appointment.details.back" text="Back to My Appointments" /></span>
                    </a>
                    <c:if test="${appointment.status == 'confirmado' && appointment.cancellable}">
                        <a class="back-button-cancel cancel-appointment" data-id="${appointment.id}">
                            <i class="fas fa-times-circle"></i>
                            <span><spring:message code="appointment.cancel" /></span>
                        </a>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
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
        const starInputs = document.querySelectorAll('.star-input');
        const ratingDisplay = document.querySelector('.rating-display');
        const starLabels = document.querySelectorAll('.star-label');
        const form = document.getElementById('ratingForm');
        const submitButton = document.getElementById('submitButton');

        if (form && submitButton) {
            form.addEventListener('submit', function () {
                submitButton.disabled = true;
            });
        }
        if (starInputs.length > 0 && ratingDisplay) {
            starInputs.forEach((input, index) => {
                input.addEventListener('change', function() {
                    const value = parseInt(this.value);
                    ratingDisplay.textContent = value + '.0';
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

            starLabels.forEach((label) => {
                label.addEventListener('mouseenter', function() {
                    const value = parseInt(this.getAttribute('data-value'));
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
                    starLabels.forEach(l => l.classList.remove('hover'));

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

        const urlParams = new URLSearchParams(window.location.search);
        if (urlParams.has('rated') && urlParams.get('rated') === 'true') {
            showToast();

            const url = new URL(window.location);
            url.searchParams.delete('rated');
            window.history.replaceState({}, '', url);
        }
    });
</script>

<script src="<c:url value='/js/file-upload.js'/>"></script>
</body>
</html>