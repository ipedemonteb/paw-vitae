<%@ page import="java.util.TimeZone" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script src="<c:url value='/js/file-upload.js'/>"></script>

<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="icon" type="image/png"
          href="https://upload.wikimedia.org/wikipedia/commons/thumb/a/aa/V-logo.svg/2048px-V-logo.svg.png"/>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="appointment.details.page.title"/></title>
    <link rel="stylesheet" href="<c:url value='/css/appointment-details.css' />"/>

    <link rel="stylesheet" href="<c:url value='/css/toast-notification.css' />"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap"
          rel="stylesheet">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/components/header.jsp">
    <jsp:param name="id" value="${doctor.imageId}" />
</jsp:include>

<div id="successToast" class="success-toast">
    <div class="success-toast-icon">
        <i class="fas fa-check"></i>
    </div>
    <div class="success-toast-content">
        <div class="success-toast-title"><spring:message code="file.upload.fileAdded"/></div>
        <div class="success-toast-message"><spring:message code="file.upload.message"/></div>
    </div>
    <button class="success-toast-close" onclick="hideSuccessToast()">
        <i class="fas fa-times"></i>
    </button>
</div>

<div id="reportSuccessToast" class="success-toast">
    <div class="success-toast-icon">
        <i class="fas fa-check"></i>
    </div>
    <div class="success-toast-content">
        <div class="success-toast-title"><spring:message code="report.added"/></div>
        <div class="success-toast-message"><spring:message code="report.added.message"/></div>
    </div>
    <button class="success-toast-close" onclick="hideSuccessToast()">
        <i class="fas fa-times"></i>
    </button>
</div>

<!-- Main Content -->
<main class="main-content">
    <div class="container">
        <div class="appointment-container">
            <div class="appointment-header">
                <h1 class="appointment-title"><spring:message code="appointment.details.page.title"/></h1>
                <p class="appointment-subtitle"><spring:message code="appointment.details.subtitle"/></p>
            </div>

            <div class="appointment-body">
                <!-- Display doctor information if available -->
                <c:if test="${not empty appointment.patient}">
                    <div class="doctor-info">
                        <div class="doctor-image">
                            <img src="<c:url value='/image/-1'/>"
                                 alt="<c:out value="${doctor.name}"/> <c:out value="${doctor.lastName}"/>"
                                 class="doctor-avatar">
                        </div>
                        <div class="doctor-details">
                            <h3 class="doctor-name"><c:out value="${appointment.patient.name}"/> <c:out
                                    value="${appointment.patient.lastName}"/></h3>
                            <div class="doctor-contact">
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
                </c:if>

                <!-- Status field - NEW -->
                <div class="specialty-card-appointment status-card">
                    <div class="specialty-icon-appointment">
                        <i class="fas fa-clipboard-check"></i>
                    </div>
                    <div class="specialty-content">
                        <span class="specialty-label-appointment">
                            <spring:message code="appointment.details.info.status" text="Status"/>:
                        </span>
                        <div class="status-display">
                        <span class="specialty-value-appointment status-${appointment.status}">
                            <spring:message code="appointment.status.${appointment.status}"
                                            text="${appointment.status}"/>
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
                        <span class="specialty-label-appointment"><spring:message code="appointment.form.date"
                                                                                  text="Date"/>:</span>
                        <div class="enhanced-date-display">
                            <div class="date-time-info">
                                <div class="date-info">
                                    <i class="fas fa-calendar-alt"></i>
                                    <c:out value="${appointment.date.toLocalDate()}"/></div>
                                <div class="time-info">
                                    <i class="fas fa-clock"></i>
                                    <c:out value="${appointment.date.toLocalTime()}"/></div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="specialty-card-appointment">
                    <div class="specialty-icon-appointment">
                        <i class="fas fa-stethoscope"></i>
                    </div>
                    <div class="specialty-content">
                        <span class="specialty-label-appointment"><spring:message
                                code="appointment.form.specialty"/>:</span>
                        <span class="specialty-value-appointment"><spring:message
                                code="${appointment.specialty.key}"/></span>
                    </div>
                </div>

                <div class="specialty-card-appointment">
                    <div class="specialty-icon-appointment">
                        <i class="fas fa-comment"></i>
                    </div>
                    <div class="specialty-content">
                        <span class="specialty-label-appointment"><spring:message
                                code="appointment.form.reason"/>:</span>
                        <span class="specialty-value-appointment"><c:out value="${appointment.reason}"/> </span>
                    </div>
                </div>


                <div class="files-section">
                    <h2 class="files-title">
                        <i class="fas fa-file-medical-alt"></i>
                        <spring:message code="appointment.details.patient.files.title"/>
                    </h2>

                    <div class="files-list">
                        <c:set var="hasPatientFiles" value="false"/>
                        <c:forEach var="file" items="${patientFiles}">
                            <c:if test="${file.uploader_role == 'patient'}">
                                <c:set var="hasPatientFiles" value="true"/>
                                <div class="file-item">
                                    <div class="file-icon">
                                        <i class="far fa-file-pdf"></i>
                                    </div>
                                    <div class="file-info">
                                        <div class="file-name"><c:out value="${file.fileName}"/></div>
                                    </div>
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
                                    <p><spring:message code="appointment.details.patient.nofiles"/></p>
                                </div>
                            </div>
                        </c:if>
                    </div>
                </div>

                <c:choose>
                    <c:when test="${appointment.status == 'completo'}">
                        <%-- Rating Section (Unlocked) --%>
                        <div class="rating-section">
                            <h2 class="rating-title">
                                <i class="fas fa-star"></i>
                                <spring:message code="appointment.details.review.title.doctor"/>
                            </h2>
                            <c:choose>
                                <c:when test="${not empty existingRating}">
                                    <!-- Display existing rating with title -->
                                    <div class="review-card">
                                        <div class="review-card-content">
                                            <div class="review-header">
                                                <div class="review-stars">
                                                    <c:forEach begin="1" end="5" var="star">
                                                        <i class="fa${star <= existingRating.rating ? 's' : 'r'} fa-star"
                                                           aria-hidden="true"></i>
                                                    </c:forEach>
                                                    <span class="rating-value"><c:out value="${existingRating.rating}"/></span>
                                                </div>
                                            </div>
                                            <div class="review-comment">
                                                <c:out value="${existingRating.comment}"/>
                                            </div>
                                        </div>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <!-- Styled No Reviews Message -->
                                    <div class="review-card">
                                        <div class="no-reviews-message" style="border: none; box-shadow: none; margin-bottom: 0;">
                                            <div class="no-reviews-icon">
                                                <i class="far fa-star"></i>
                                            </div>
                                            <h3 class="no-reviews-title"><spring:message code="appointment.details.review.none"
                                                                                         text="No Reviews Yet"/></h3>
                                            <p class="no-reviews-text"><spring:message
                                                    code="appointment.details.review.none.message"
                                                    text="This doctor hasn't received any reviews for this appointment."/></p>
                                        </div>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <%-- File Upload Section (Unlocked) --%>
                        <form:form modelAttribute="doctorFileForm" method="post"
                                   action="${pageContext.request.contextPath}/doctor/dashboard/appointment-details/${appointment.id}"
                                   class="appointment-form" enctype="multipart/form-data">
                            <form:hidden path="appointmentId" />

                            <div class="files-section">
                                <h2 class="files-title">
                                    <i class="fas fa-file-medical-alt"></i>
                                    <spring:message code="appointment.details.doctor.files.title"/>
                                </h2>

                                <!-- Report textarea -->
                                <c:choose>
                                    <c:when test="${empty appointment.report}">
                                        <div class="form-group">
                                            <label for="report"><spring:message code="appointment.details.report"/></label>
                                            <form:textarea path="report" id="report" class="form-control" rows="4"/>
                                            <form:errors path="report" cssClass="error-message"/>
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
                                    <c:set var="hasDoctorFiles" value="false"/>
                                    <c:forEach var="file" items="${doctorFiles}">
                                        <c:if test="${file.uploader_role == 'doctor'}">
                                            <c:set var="hasDoctorFiles" value="true"/>
                                            <div class="file-item">
                                                <div class="file-icon">
                                                    <i class="far fa-file-pdf"></i>
                                                </div>
                                                <div class="file-info">
                                                    <div class="file-name"><c:out value="${file.fileName}"/></div>
                                                </div>
                                                <a href="<c:url value='/appointment/${appointment.id}/file/${file.id}'/>"
                                                   class="file-download" download>
                                                    <i class="fas fa-download"></i>
                                                </a>
                                            </div>
                                        </c:if>
                                    </c:forEach>

                                    <c:if test="${not hasDoctorFiles}">
                                        <div class="no-files-message">
                                            <div class="no-files-content">
                                                <i class="fas fa-info-circle"></i>
                                                <p><spring:message code="appointment.details.you.nofiles"/></p>
                                            </div>
                                        </div>
                                    </c:if>
                                </div>
                            </div>

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
                                <button id="submitButton" type="submit" class="btn btn-primary btn-block">
                <span>
                    <spring:message code="appointment.details.doctor.submit"/>
                </span>
                                </button>
                            </div>
                        </form:form>
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
                                    <div class="rating-section">
                                        <h2 class="rating-title">
                                            <i class="fas fa-star"></i>
                                            <spring:message code="appointment.details.review.title.doctor"/>
                                        </h2>
                                        <div class="review-card">
                                            <div class="no-reviews-message" style="border: none; box-shadow: none; margin-bottom: 0;">
                                                <div class="no-reviews-icon">
                                                    <i class="far fa-star"></i>
                                                </div>
                                                <h3 class="no-reviews-title">
                                                    <spring:message code="appointment.details.review.none" text="No Reviews Yet"/>
                                                </h3>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <%-- File Upload Section (Locked) --%>
                            <div class="locked-section">
                                <div class="locked-section-header">
                                    <div class="locked-section-icon">
                                        <i class="fas fa-lock"></i>
                                    </div>
                                    <h3 class="locked-section-title">
                                        <spring:message code="appointment.locked.files.title"/>
                                    </h3>
                                </div>
                                <p class="locked-section-message">
                                    <spring:message code="appointment.locked.files.message"/>
                                </p>
                                <div class="locked-section-content">
                                    <div class="files-section">
                                        <h2 class="files-title">
                                            <i class="fas fa-file-medical-alt"></i>
                                            <spring:message code="appointment.details.doctor.files.title"/>
                                        </h2>
                                        <div class="form-group">
                                            <label><spring:message code="appointment.details.report"/></label>
                                            <textarea label="report" id="report" class="form-control" rows="4" disabled></textarea>
                                        </div>
                                        <div class="no-files-message">
                                            <div class="no-files-content">
                                                <i class="fas fa-info-circle"></i>
                                                <p><spring:message code="appointment.details.you.nofiles"/></p>
                                            </div>
                                        </div>
                                        <div class="file-upload-container">
                                            <div class="file-upload-dropzone disabled">
                                                <div class="file-upload-icon">
                                                    <i class="fas fa-cloud-upload-alt"></i>
                                                </div>
                                                <div class="file-upload-text">
                                                    <p class="file-upload-primary"><spring:message code="file.upload.dragHere"/></p>
                                                    <p class="file-upload-secondary"><spring:message code="file.upload.onlyPdf"/></p>
                                                </div>
                                            </div>
                                        </div>
                                        <form:errors path="" cssClass="error-message" />
                                        <div class="form-group">
                                            <button type="button" class="btn btn-primary btn-block" disabled>
                            <span>
                                <spring:message code="appointment.details.doctor.submit"/>
                            </span>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:if>

                    </c:otherwise>
                </c:choose>
                <div class="back-button-container">
                    <a href="${pageContext.request.contextPath}/doctor/dashboard" class="back-button">
                        <i class="fas fa-arrow-left"></i>
                        <span><spring:message code="appointment.details.back"/></span>
                    </a>
                </div>
            </div>
        </div>
    </div>
</main>

<script src="<c:url value="/js/toast-notification.js"/>"></script>


<script>


    window.appointmentMessages = {
        fileUpload: {
            dragHere: '<spring:message code="file.upload.dragHere"  />',
            onlyPdf: '<spring:message code="file.upload.onlyPdf"  />',
            tooManyFiles: '<spring:message code="file.upload.tooManyFiles"  />',
            invalidType: '<spring:message code="file.upload.invalidType"  />',
            fileAdded: '<spring:message code="file.upload.fileAdded"  />',
            fileRemoved: '<spring:message code="file.upload.fileRemoved" />',
            fileTooLarge: '<spring:message code="file.upload.fileTooLarge" />'
        }
    }

    document.addEventListener('DOMContentLoaded', function() {



        const form = document.getElementById('doctorFileForm');
        const submitButton = document.getElementById('submitButton');

        if (form && submitButton) {
            form.addEventListener('submit', function () {
                submitButton.disabled = true;
            });
        }



        const urlParams = new URLSearchParams(window.location.search);
        if (urlParams.get('updated') === 'true') {
            showSuccessToast();

            // Remove the query parameter without refreshing the page
            const newUrl = window.location.pathname;
            window.history.replaceState({}, document.title, newUrl);
        }
    });



</script>

</body>
</html>
