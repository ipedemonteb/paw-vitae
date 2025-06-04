<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html lang="${pageContext.response.locale}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="doctor.profile.title" arguments="${fn:escapeXml(doctor.name)},${fn:escapeXml(doctor.lastName)}" /></title>
    <link rel="icon" type="image/png" href="https://upload.wikimedia.org/wikipedia/commons/thumb/a/aa/V-logo.svg/2048px-V-logo.svg.png" />
    <link rel="stylesheet" href="<c:url value='/css/doctor-profile.css' />" />
    <link rel="stylesheet" href="<c:url value='/css/toast-notification.css' />" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<c:url value='/css/date-picker-multiple.css' />" /></head>
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
        <div class="success-toast-title"><spring:message code="profile.update.success.title" /></div>
        <div class="success-toast-message"><spring:message code="profile.update.success.message" /></div>
    </div>
    <button class="success-toast-close" onclick="hideSuccessToast()">
        <i class="fas fa-times"></i>
    </button>
</div>

<main class="main-content">
    <div class="container">
        <c:set var="isOwnProfile" value="${loggedUser != null && loggedUser.id == doctor.id}" />
        <c:if test="${!isOwnProfile}">
            <div class="back-navigation">
                <a href="javascript:history.back()" class="back-btn">
                    <i class="fas fa-arrow-left"></i>
                    <spring:message code="doctor.profile.back" />
                </a>
            </div>
        </c:if>

        <div class="profile-header">
            <div class="profile-header-content">
                <div class="doctor-avatar-section">
                    <div class="doctor-avatar-large">
                        <img src="<c:url value='/image/${empty doctor.imageId || doctor.imageId == -1 ? -1 : doctor.imageId}'/>"
                             alt="<c:out value='${doctor.name} ${doctor.lastName}'/>"
                             class="avatar-img">
                    </div>
                    <c:if test="${doctor.verified}">
                        <div class="verified-badge">
                            <i class="fas fa-check-circle"></i>
                            <span><spring:message code="doctor.profile.verified" /></span>
                        </div>
                    </c:if>

                    <c:if test="${isOwnProfile}">
                        <div class="header-edit-section">
                            <div id="header-edit-mode-controls" class="header-edit-controls">
                                <button type="button" id="header-edit-btn" class="btn btn-primary" onclick="enterEditMode()">
                                     <spring:message code="dashboard.profile.edit" />
                                </button>
                            </div>

                            <div id="header-save-mode-controls" class="header-edit-controls" style="display: none;">
                                <button type="button" id="header-save-btn" class="btn btn-primary" onclick="saveAllChanges()">
                                    <spring:message code="appointment.form.save" />
                                </button>
                                <button type="button" id="header-cancel-btn" class="btn btn-secondary" onclick="cancelAllChanges()">
                                     <spring:message code="appointment.cancel" />
                                </button>
                            </div>
                        </div>
                    </c:if>
                </div>

                <div class="doctor-info-section">
                    <h1 class="doctor-name">
                        <c:out value="Dr. ${doctor.name} ${doctor.lastName}" />
                    </h1>

                    <div class="bio-section">
                        <div id="bio-display" class="editable-content">
                            <c:choose>
                                <c:when test="${not empty doctor.profile.bio}">
                                    <p class="bio-text"><c:out value="${doctor.profile.bio}" /></p>
                                </c:when>
                                <c:otherwise>
                                    <p class="bio-text">
                                        <spring:message code="doctor.profile.default.bio"/>
                                    </p>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <c:if test="${isOwnProfile}">
                            <div id="bio-edit" class="edit-form" style="display: none;">
                                <label for="bio-input"></label><textarea id="bio-input" class="form-control" maxlength="220"
                                                                         placeholder="<spring:message code='doctor.profile.default.bio.ph'/>"><c:out value="${doctor.profile.bio}" /></textarea>
                                <div class="char-counter">
                                    <span id="bio-char-count">0</span>/220
                                </div>
                            </div>
                        </c:if>
                    </div>

                    <div class="rating-section">
                        <c:choose>
                            <c:when test="${doctor.ratingCount > 0}">
                                <div class="stars-large">
                                    <c:set var="fullStars" value="${doctor.rating.intValue()}" />
                                    <c:set var="hasHalfStar" value="${doctor.rating - fullStars >= 0.5}" />
                                    <c:set var="emptyStars" value="${5 - fullStars - (hasHalfStar ? 1 : 0)}" />

                                    <c:forEach begin="1" end="${fullStars}" var="i">
                                        <i class="fas fa-star"></i>
                                    </c:forEach>

                                    <c:if test="${hasHalfStar}">
                                        <i class="fas fa-star-half-alt"></i>
                                    </c:if>

                                    <c:forEach begin="1" end="${emptyStars}" var="i">
                                        <i class="far fa-star"></i>
                                    </c:forEach>
                                </div>
                                <div class="rating-info">
                                    <span class="rating-value">
                                        <fmt:formatNumber value="${doctor.rating}" type="number" maxFractionDigits="1" minFractionDigits="1" />
                                    </span>
                                    <span class="rating-count">
                                        (<spring:message code="doctor.profile.rating.count" arguments="${doctor.ratingCount}" />)
                                    </span>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="no-rating">
                                    <i class="fas fa-star-o"></i>
                                    <span><spring:message code="doctor.profile.no.rating" /></span>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <!-- Contact Info -->
                    <div class="contact-info">
                        <div class="contact-item">
                            <i class="fas fa-envelope"></i>
                            <a href="mailto:${doctor.email}"><c:out value="${doctor.email}" /></a>
                        </div>
                        <div class="contact-item">
                            <i class="fas fa-phone"></i>
                            <a href="tel:${doctor.phone}"><c:out value="${doctor.phone}" /></a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Doctor Details Grid -->
        <div class="details-grid">
            <!-- About/Description Section with Edit Capability -->
            <div class="detail-card full-width">
                <div class="detail-card-header">
                    <h3><i class="fas fa-user"></i><spring:message code="doctor.profile.about"/></h3>
                </div>
                <div class="detail-card-content">
                    <div id="description-display" class="editable-content">
                        <c:choose>
                            <c:when test="${not empty doctor.profile.description}">
                                <div class="description-content">
                                    <p><c:out value="${doctor.profile.description}" /></p>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <p class="no-data"><spring:message code="doctor.profile.default.about" /></p>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <c:if test="${isOwnProfile}">
                        <div id="description-edit" class="edit-form" style="display: none;">
                            <textarea id="description-input" class="form-control" maxlength="2600" rows="6" placeholder="<spring:message code="doctor.profile.default.about.ph"/>"><c:out value="${doctor.profile.description}" /></textarea>
                            <div class="char-counter">
                                <span id="description-char-count">0</span>/2600
                            </div>
                        </div>
                    </c:if>
                </div>
            </div>

            <div class="specialty-coverage-row">
                <!-- Specialties Section -->
                <div class="detail-card">
                    <div class="detail-card-header">
                        <h3><i class="fas fa-stethoscope"></i> <spring:message code="doctor.profile.specialties" /></h3>
                    </div>
                    <div class="detail-card-content">
                        <c:choose>
                            <c:when test="${not empty doctor.specialtyList}">
                                <div class="specialty-list scrollable-list">
                                    <c:forEach var="specialty" items="${doctor.specialtyList}">
                                        <div class="specialty-item">
                                            <spring:message code="${specialty.key}" />
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <p class="no-data"><spring:message code="doctor.profile.no.specialties" /></p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="detail-card">
                    <div class="detail-card-header">
                        <h3><i class="fas fa-shield-alt"></i> <spring:message code="doctor.profile.coverages" /></h3>
                    </div>
                    <div class="detail-card-content">
                        <c:choose>
                            <c:when test="${not empty doctor.coverageList}">
                                <div class="coverage-list scrollable-list">
                                    <c:forEach var="coverage" items="${doctor.coverageList}">
                                        <div class="coverage-item">
                                            <c:out value="${coverage.name}" />
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <p class="no-data"><spring:message code="doctor.profile.no.coverages" /></p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>

            <div class="detail-card full-width">
                <div class="detail-card-header">
                    <h3><i class="fas fa-briefcase"></i> <spring:message code="doctor.profile.path"/></h3>
                </div>
                <div class="detail-card-content">
                    <div id="experiences-display" class="editable-content">
                        <c:choose>
                            <c:when test="${not empty doctor.experiences}">
                                <div class="career-timeline">
                                    <c:forEach var="career" items="${doctor.experiences}" varStatus="status">
                                        <div class="career-item">
                                            <div class="career-timeline-dot"></div>
                                            <div class="career-content">
                                                <h4 class="career-position"><c:out value="${career.positionTitle}" /></h4>
                                                <div class="career-organization"><c:out value="${career.organizationName}" /></div>
                                                <div class="career-period">
                                                    <c:if test="${not empty career.startDate}">
                                                        <c:set var="start" value="${career.startDate}" />
                                                        <c:out value="${fn:substring(start,8,10)}/${fn:substring(start,5,7)}/${fn:substring(start,0,4)}" />
                                                    </c:if>
                                                    -
                                                    <c:choose>
                                                        <c:when test="${not empty career.endDate}">
                                                            <c:set var="end" value="${career.endDate}" />
                                                            <c:out value="${fn:substring(end,8,10)}/${fn:substring(end,5,7)}/${fn:substring(end,0,4)}" />
                                                        </c:when>
                                                        <c:otherwise>
                                                            <spring:message code="now" />
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <p class="no-data"><spring:message code="doctor.profile.default.experience" /></p>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <c:if test="${isOwnProfile}">
                        <div id="experiences-edit" class="edit-form" style="display: none;">
                            <div id="experiences-container">
                                <c:forEach var="experience" items="${doctor.experiences}" varStatus="status">
                                    <div class="experience-form-item" data-index="${status.index}">
                                        <div class="form-row">
                                            <div class="form-group">
                                                <spring:message code="doctor.profile.position" />
                                                <input type="text" class="form-control" name="experiences[${status.index}].positionTitle" value="<c:out value='${experience.positionTitle}' />" />
                                            </div>
                                            <div class="form-group">
                                                <spring:message code="doctor.profile.organization" />
                                                <input type="text" class="form-control" name="experiences[${status.index}].organizationName" value="<c:out value='${experience.organizationName}' />" />
                                            </div>
                                        </div>
                                        <div class="form-row">
                                            <div class="form-group">
                                                <spring:message code="doctor.profile.startDate" />
                                                <div class="date-picker-container">
                                                    <input type="text" class="form-control date-picker-input" name="experiences[${status.index}].startDate" value="${experience.startDate}" readonly />
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <spring:message code="doctor.profile.endDate" />
                                                <div class="date-picker-container">
                                                    <input type="text" class="form-control date-picker-input" name="experiences[${status.index}].endDate" value="${experience.endDate}" readonly />
                                                </div>
                                            </div>
                                        </div>
                                        <button type="button" class="btn-remove" onclick="removeExperience(${status.index})">
                                            <i class="fas fa-trash"></i>
                                        </button>
                                    </div>
                                </c:forEach>
                            </div>
                            <button type="button" class="btn-add-new" onclick="addExperience()">
                                <i class="fas fa-plus"></i> <spring:message code="doctor.profile.addExperience" />
                            </button>
                        </div>
                    </c:if>
                </div>
            </div>

            <div class="detail-card full-width">
                <div class="detail-card-header">
                    <h3><i class="fas fa-certificate"></i> <spring:message code="doctor.profile.certificates"/></h3>
                </div>
                <div class="detail-card-content">
                    <div id="certificates-display" class="editable-content">
                        <c:choose>
                            <c:when test="${not empty doctor.certifications}">
                                <div class="certificates-grid">
                                    <c:forEach var="certificate" items="${doctor.certifications}">
                                        <div class="certificate-item">
                                            <div class="certificate-icon">
                                                <i class="fas fa-award"></i>
                                            </div>
                                            <div class="certificate-info">
                                                <h4 class="certificate-name"><c:out value="${certificate.certificateName}" /></h4>
                                                <div class="certificate-issuer"><c:out value="${certificate.issuingEntity}" /></div>
                                                <div class="certificate-date">
                                                    <i class="fas fa-calendar"></i>
                                                    <c:if test="${not empty certificate.issueDate}">
                                                        <c:set var="issue" value="${certificate.issueDate}" />
                                                        <c:out value="${fn:substring(issue,8,10)}/${fn:substring(issue,5,7)}/${fn:substring(issue,0,4)}" />
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <p class="no-data"><spring:message code="doctor.profile.default.certificates" /></p>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <c:if test="${isOwnProfile}">
                        <div id="certificates-edit" class="edit-form" style="display: none;">
                            <div id="certificates-container">
                                <c:forEach var="certificate" items="${doctor.certifications}" varStatus="status">
                                    <div class="certificate-form-item" data-index="${status.index}">
                                        <div class="form-row">
                                            <div class="form-group">
                                                <spring:message code="doctor.profile.certificateDescription" />
                                                <input type="text" class="form-control" name="certificates[${status.index}].certificateName" value="<c:out value='${certificate.certificateName}' />" />
                                            </div>
                                            <div class="form-group">
                                                <spring:message code="doctor.profile.entity" />
                                                <input type="text" class="form-control" name="certificates[${status.index}].issuingEntity" value="<c:out value='${certificate.issuingEntity}' />" />
                                            </div>
                                        </div>
                                        <div class="form-row">
                                            <div class="form-group">
                                                <spring:message code="doctor.profile.issueDate" />
                                                <div class="date-picker-container">
                                                    <input type="text" class="form-control date-picker-input" name="certificates[${status.index}].issueDate" value="${certificate.issueDate}" readonly />
                                                </div>
                                            </div>
                                        </div>
                                        <button type="button" class="btn-remove" onclick="removeCertificate(${status.index})">
                                            <i class="fas fa-trash"></i>
                                        </button>
                                    </div>
                                </c:forEach>
                            </div>
                            <button type="button" class="btn-add-new" onclick="addCertificate()">
                                <i class="fas fa-plus"></i> <spring:message code="doctor.profile.addCertificate" />
                            </button>
                        </div>
                    </c:if>
                </div>
            </div>

            <div class="detail-card full-width">
                <div class="detail-card-header">
                    <h3><i class="fas fa-map-marker-alt"></i> <spring:message code="doctor.profile.offices" /></h3>
                </div>
                <div class="detail-card-content">
                    <c:choose>
                        <c:when test="${not empty offices}">
                            <div class="office-list">
                                <c:forEach var="office" items="${offices}">
                                    <div class="office-item">
                                        <div class="office-info">
                                            <h4 class="office-name">
                                                <i class="fas fa-building"></i>
                                                <c:out value="${office.officeName}" />
                                            </h4>
                                            <div class="office-address">
                                                <i class="fas fa-map-marker-alt"></i>
                                                <span>
                                                    <c:out value="${office.neighborhood.name}" />
                                                </span>
                                            </div>
                                            <c:if test="${not empty doctor.phone}">
                                                <div class="office-phone">
                                                    <i class="fas fa-phone"></i>
                                                    <a href="tel:${doctor.phone}"><c:out value="${doctor.phone}" /></a>
                                                </div>
                                            </c:if>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <p class="no-data"><spring:message code="doctor.profile.no.offices" /></p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <c:if test="${not empty doctorRatings}">
                <div class="detail-card full-width">
                    <div class="detail-card-header">
                        <h3><i class="fas fa-star"></i> <spring:message code="landing.doctor.ratings.tag" /></h3>
                    </div>

                    <div class="testimonials-slider">
                        <c:forEach items="${doctorRatings}" var="entry" varStatus="status">
                            <div class="testimonial-card doctor-rating-card">
                                <div class="testimonial-content">
                                    <div class="quote-icon"><i class="fas fa-quote-left"></i></div>
                                    <p class="testimonial-text"><c:out value="${entry.comment}"/></p>
                                    <div class="testimonial-rating">
                                        <c:forEach begin="1" end="5" var="star">
                                            <c:choose>
                                                <c:when test="${star <= entry.rating}">
                                                    <i class="fas fa-star"></i>
                                                </c:when>
                                                <c:otherwise>
                                                    <i class="far fa-star"></i>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </div>
                                </div>
                                <div class="testimonial-author">
                                    <div class="author-info">
                                        <h4 class="author-name"><c:out value="${entry.patient.name} ${entry.patient.lastName}"/></h4>
                                        <p class="author-title"><spring:message code="landing.doctor.ratings.patient" text="Paciente" /></p>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>

                    <div class="testimonial-controls">
                        <button class="testimonial-prev"><i class="fas fa-arrow-left"></i></button>
                        <button class="testimonial-next"><i class="fas fa-arrow-right"></i></button>
                    </div>
                </div>
            </c:if>

            <sec:authorize access="not hasRole('ROLE_DOCTOR')">>
                <div class="cta-section">
                    <div class="cta-content">
                        <h3><spring:message code="doctor.profile.ready.to.book" /></h3>
                        <p>
                            <spring:message code="doctor.profile.book.description" arguments="${fn:escapeXml(doctor.name)},${fn:escapeXml(doctor.lastName)}" />
                        </p>
                        <a href="<c:url value='/appointment?doctorId=${doctor.id}'/>" class="btn btn-primary btn-large">
                            <i class="fas fa-calendar-plus"></i>
                            <spring:message code="doctor.profile.book.now" />
                        </a>
                    </div>
                </div>
            </sec:authorize>
        </div>
    </div>
</main>

<c:if test="${isOwnProfile}">
    <form:form id="updateProfileForm" method="POST" action="/doctor/profile/update" modelAttribute="doctorProfileForm" style="display: none;">
        <form:hidden path="biography" id="hiddenBio" />
        <form:hidden path="description" id="hiddenDescription" />
        <div id="hiddenExperiences"></div>
        <div id="hiddenCertificates"></div>
    </form:form>
</c:if>

<script src="<c:url value='/js/toast-notification.js' />"></script>

<script>
    let experienceIndex = ${fn:length(doctor.experiences)};
    let certificateIndex = ${fn:length(doctor.certifications)};
    let isEditMode = false;

    let originalValues = {
        bio: '${doctor.profile.bio}',
        description: '${doctor.profile.description}',
        experiences: [],
        certificates: []
    };

    const prevButton = document.querySelector('.testimonial-prev');
    const nextButton = document.querySelector('.testimonial-next');
    const doctorRatings = document.querySelectorAll('.doctor-rating-card');
    let currentRating = 0;

    if (doctorRatings.length > 0) {
        function showRating(index) {
            doctorRatings.forEach((rating, i) => {
                rating.style.display = i === index ? 'flex' : 'none';
            });
        }

        prevButton.addEventListener('click', () => {
            currentRating = (currentRating - 1 + doctorRatings.length) % doctorRatings.length;
            showRating(currentRating);
        });

        nextButton.addEventListener('click', () => {
            currentRating = (currentRating + 1) % doctorRatings.length;
            showRating(currentRating);
        });

        showRating(currentRating);
    }

    function enterEditMode() {
        isEditMode = true;

        storeOriginalValues();

        const editableSections = ['bio', 'description', 'experiences', 'certificates'];
        editableSections.forEach(section => {
            const displayDiv = document.getElementById(section + '-display');
            const editDiv = document.getElementById(section + '-edit');

            if (displayDiv && editDiv) {
                displayDiv.style.display = 'none';
                editDiv.style.display = 'block';
            }
        });

        updateCharCount('bio');
        updateCharCount('description');

        document.getElementById('header-edit-mode-controls').style.display = 'none';
        document.getElementById('header-save-mode-controls').style.display = 'flex';
    }

    function cancelAllChanges() {
        isEditMode = false;

        restoreOriginalValues();

        const editableSections = ['bio', 'description', 'experiences', 'certificates'];
        editableSections.forEach(section => {
            const displayDiv = document.getElementById(section + '-display');
            const editDiv = document.getElementById(section + '-edit');

            if (displayDiv && editDiv) {
                displayDiv.style.display = 'block';
                editDiv.style.display = 'none';
            }
        });

        document.getElementById('header-edit-mode-controls').style.display = 'flex';
        document.getElementById('header-save-mode-controls').style.display = 'none';
    }

    function saveAllChanges() {
        const form = document.getElementById('updateProfileForm');

        const bioValue = document.getElementById('bio-input').value;
        document.getElementById('hiddenBio').value = bioValue;

        const descValue = document.getElementById('description-input').value;
        document.getElementById('hiddenDescription').value = descValue;

        saveExperiences();
        saveCertificates();

        form.submit();
    }

    function storeOriginalValues() {
        originalValues.experiences = [];
        const experienceItems = document.querySelectorAll('.experience-form-item');
        experienceItems.forEach((item, index) => {
            const experience = {};
            const inputs = item.querySelectorAll('input');
            inputs.forEach(input => {
                const fieldName = input.name.split('.').pop();
                experience[fieldName] = input.value;
            });
            originalValues.experiences.push(experience);
        });

        originalValues.certificates = [];
        const certificateItems = document.querySelectorAll('.certificate-form-item');
        certificateItems.forEach((item, index) => {
            const certificate = {};
            const inputs = item.querySelectorAll('input');
            inputs.forEach(input => {
                const fieldName = input.name.split('.').pop();
                certificate[fieldName] = input.value;
            });
            originalValues.certificates.push(certificate);
        });
    }

    function restoreOriginalValues() {
        // Restore bio
        document.getElementById('bio-input').value = originalValues.bio;

        // Restore description
        document.getElementById('description-input').value = originalValues.description;

        // Restore experiences
        const experiencesContainer = document.getElementById('experiences-container');
        experiencesContainer.innerHTML = '';
        originalValues.experiences.forEach((experience, index) => {
            addExperienceWithData(experience, index);
        });
        experienceIndex = originalValues.experiences.length;

        // Restore certificates
        const certificatesContainer = document.getElementById('certificates-container');
        certificatesContainer.innerHTML = '';
        originalValues.certificates.forEach((certificate, index) => {
            addCertificateWithData(certificate, index);
        });
        certificateIndex = originalValues.certificates.length;
    }

    function updateCharCount(section) {
        const input = document.getElementById(section + '-input');
        const counter = document.getElementById(section + '-char-count');

        function updateCount() {
            counter.textContent = input.value.length;
        }

        input.addEventListener('input', updateCount);
        updateCount();
    }

    function addExperience() {
        addExperienceWithData({}, experienceIndex);
        experienceIndex++;
    }

    function addExperienceWithData(data, index) {
        const container = document.getElementById('experiences-container');
        const newExperience = document.createElement('div');
        newExperience.className = 'experience-form-item';
        newExperience.setAttribute('data-index', index);

        newExperience.innerHTML = `
    <div class="form-row">
        <div class="form-group">
            <spring:message code="doctor.profile.position" />
            <input type="text" class="form-control" name="experiences[` + index + `].positionTitle" value="` + (data.positionTitle || '') + `" />
        </div>
        <div class="form-group">
            <spring:message code="doctor.profile.organization" />
            <input type="text" class="form-control" name="experiences[` + index + `].organizationName" value="` + (data.organizationName || '') + `" />
        </div>
    </div>
    <div class="form-row">
        <div class="form-group">
            <spring:message code="doctor.profile.startDate" />
            <div class="date-picker-container">
                <input type="text" class="form-control date-picker-input" name="experiences[` + index + `].startDate" value="` + (data.startDate || '') + `" readonly />
            </div>
        </div>
        <div class="form-group">
            <spring:message code="doctor.profile.endDate" />
            <div class="date-picker-container">
                <input type="text" class="form-control date-picker-input" name="experiences[` + index + `].endDate" value="` + (data.endDate || '') + `" readonly />
            </div>
        </div>
    </div>
    <button type="button" class="btn-remove" onclick="removeExperience(` + index + `)">
        <i class="fas fa-trash"></i>
    </button>
`;

        setTimeout(() => {
            initializeDatePickersForContainer(newExperience);
        }, 100);
        container.appendChild(newExperience);
    }

    function removeExperience(index) {
        const item = document.querySelector('.experience-form-item[data-index="' + index + '"]');
        if (item) {
            item.remove();
        }
    }

    function addCertificate() {
        addCertificateWithData({}, certificateIndex);
        certificateIndex++;
    }

    function addCertificateWithData(data, index) {
        const container = document.getElementById('certificates-container');
        const newCertificate = document.createElement('div');
        newCertificate.className = 'certificate-form-item';
        newCertificate.setAttribute('data-index', index);

        newCertificate.innerHTML = `
    <div class="form-row">
        <div class="form-group">
            <spring:message code="doctor.profile.certificateDescription" />
            <input type="text" class="form-control" name="certificates[` + index + `].certificateName" value="` + (data.certificateName || '') + `" />
        </div>
        <div class="form-group">
            <spring:message code="doctor.profile.entity" />
            <input type="text" class="form-control" name="certificates[` + index + `].issuingEntity" value="` + (data.issuingEntity || '') + `" />
        </div>
    </div>
    <div class="form-row">
        <div class="form-group">
            <spring:message code="doctor.profile.issueDate" />
            <div class="date-picker-container">
                <input type="text" class="form-control date-picker-input" name="certificates[` + index + `].issueDate" value="` + (data.issueDate || '') + `" readonly />
            </div>
        </div>
    </div>
    <button type="button" class="btn-remove" onclick="removeCertificate(` + index + `)">
        <i class="fas fa-trash"></i>
    </button>
`;


        setTimeout(() => {
            initializeDatePickersForContainer(newCertificate);
        }, 100);

        container.appendChild(newCertificate);
    }

    function removeCertificate(index) {
        const item = document.querySelector('.certificate-form-item[data-index="' + index + '"]');
        if (item) {
            item.remove();
        }
    }

    function saveExperiences() {
        const container = document.getElementById('hiddenExperiences');
        container.innerHTML = '';

        const experienceItems = document.querySelectorAll('.experience-form-item');
        experienceItems.forEach((item, index) => {
            const inputs = item.querySelectorAll('input');
            inputs.forEach(input => {
                const hiddenInput = document.createElement('input');
                hiddenInput.type = 'hidden';
                hiddenInput.name = input.name.replace(/\[\d+\]/, '[' + index + ']');
                hiddenInput.value = input.value;
                container.appendChild(hiddenInput);
            });
        });
    }

    function saveCertificates() {
        const container = document.getElementById('hiddenCertificates');
        container.innerHTML = '';

        const certificateItems = document.querySelectorAll('.certificate-form-item');
        certificateItems.forEach((item, index) => {
            const inputs = item.querySelectorAll('input');
            inputs.forEach(input => {
                const hiddenInput = document.createElement('input');
                hiddenInput.type = 'hidden';
                hiddenInput.name = input.name.replace(/\[\d+\]/, '[' + index + ']');
                hiddenInput.value = input.value;
                container.appendChild(hiddenInput);
            });
        });
    }

    // Set current year for copyright
    document.addEventListener('DOMContentLoaded', function() {
        window.currentYear = new Date().getFullYear();
    });
</script>

<style>
    /* Doctor Avatar Section Layout */
    .doctor-avatar-section {
        position: relative;
        display: flex;
        align-items: flex-end;
        gap: 1rem;
        margin-bottom: 16px;
    }

    /* Header Edit Section positioned at bottom right */
    .header-edit-section {
        position: absolute;
        bottom: 0;
        right: 0;
        display: flex;
        flex-direction: column;
        align-items: flex-end;
        gap: 0.5rem;
    }

    .header-edit-controls {
        display: flex;
        gap: 0.75rem;
        align-items: center;
    }

    .btn-edit-header {
        background-color: var(--linkedin-blue);
        color: white;
        border: none;
        padding: 8px 16px;
        border-radius: 20px;
        font-size: 13px;
        font-weight: 500;
        transition: all 0.2s ease;
        box-shadow: var(--shadow-sm);
    }

    .btn-edit-header:hover {
        background-color: var(--linkedin-blue-hover);
        transform: translateY(-1px);
        box-shadow: var(--shadow-md);
    }

    .btn-success {
        background-color: var(--success-color);
        color: white;
        border: none;
        padding: 8px 16px;
        border-radius: 20px;
        font-size: 13px;
        font-weight: 500;
        transition: all 0.2s ease;
        box-shadow: var(--shadow-sm);
    }

    .btn-success:hover {
        background-color: #059669;
        transform: translateY(-1px);
        box-shadow: var(--shadow-md);
    }

    /* Edit Form Styles */
    .edit-form {
        margin-top: 1rem;
    }

    .form-control {
        width: 100%;
        padding: 0.75rem;
        border: 1px solid var(--border-color);
        border-radius: 0.25rem;
        font-size: 1rem;
        transition: border-color 0.2s ease;
    }

    .form-control:focus {
        outline: none;
        border-color: var(--primary-color);
        box-shadow: 0 0 0 2px rgba(37, 99, 235, 0.1);
    }

    .form-group {
        margin-bottom: 1rem;
    }

    .form-group label {
        display: block;
        margin-bottom: 0.5rem;
        font-weight: 500;
        color: var(--text-primary);
    }

    .form-row {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 1rem;
        margin-bottom: 1rem;
    }

    .experience-form-item, .certificate-form-item {
        position: relative;
        padding: 1.5rem;
        border: 1px solid var(--border-color);
        border-radius: 0.5rem;
        margin-bottom: 1rem;
        background: var(--background-color);
    }

    .btn-remove {
        position: absolute;
        top: 0.5rem;
        right: 0.5rem;
        background: var(--danger-color);
        color: white;
        border: none;
        border-radius: 50%;
        width: 2rem;
        height: 2rem;
        cursor: pointer;
        display: flex;
        align-items: center;
        justify-content: center;
        transition: background-color 0.2s ease;
    }

    .btn-remove:hover {
        background: #dc2626;
    }

    .char-counter {
        text-align: right;
        font-size: 0.75rem;
        color: var(--text-secondary);
        margin-top: 0.5rem;
    }

    .btn-add-new {
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 0.5rem;
        width: 100%;
        padding: 0.75rem;
        background-color: var(--white);
        border: 1px dashed var(--primary-color);
        border-radius: var(--border-radius);
        color: var(--primary-color);
        font-weight: 500;
        cursor: pointer;
        transition: var(--transition);
    }

    .btn-add-new:hover {
        background-color: rgba(59, 130, 246, 0.1);
    }

    /* Responsive Design */
    @media (max-width: 768px) {
        .doctor-avatar-section {
            flex-direction: column;
            align-items: center;
            text-align: center;
        }

        .header-edit-section {
            position: static;
            align-items: center;
            margin-top: 1rem;
        }

        .header-edit-controls {
            justify-content: center;
            flex-wrap: wrap;
        }

        .form-row {
            grid-template-columns: 1fr;
        }
    }

    @media (max-width: 480px) {
        .header-edit-controls {
            flex-direction: column;
            gap: 0.5rem;
        }

        .btn-edit-header,
        .btn-success,
        .btn-secondary {
            padding: 6px 12px;
            font-size: 12px;
        }
    }

    .scrollable-list {
        max-height: 180px; /* Adjust as needed */
        overflow-y: auto;
        padding-right: 8px; /* Optional: space for scrollbar */
    }
    .specialty-list, .coverage-list {
        display: flex;
        flex-direction: column;
        gap: 0.5rem;
    }
</style>
<script>
    // Create a messages object to be used by the JavaScript
    window.appointmentMessages = {
        months: [
            '<spring:message code="calendar.month.january" />',
            '<spring:message code="calendar.month.february" />',
            '<spring:message code="calendar.month.march" />',
            '<spring:message code="calendar.month.april" />',
            '<spring:message code="calendar.month.may" />',
            '<spring:message code="calendar.month.june" />',
            '<spring:message code="calendar.month.july" />',
            '<spring:message code="calendar.month.august" />',
            '<spring:message code="calendar.month.september" />',
            '<spring:message code="calendar.month.october" />',
            '<spring:message code="calendar.month.november" />',
            '<spring:message code="calendar.month.december" />'
        ],
        weekdays: [
            '<spring:message code="calendar.day.sunday" />',
            '<spring:message code="calendar.day.monday" />',
            '<spring:message code="calendar.day.tuesday" />',
            '<spring:message code="calendar.day.wednesday" />',
            '<spring:message code="calendar.day.thursday" />',
            '<spring:message code="calendar.day.friday" />',
            '<spring:message code="calendar.day.saturday" />'
        ],
        weekdaysShort: [
            '<spring:message code="calendar.day.short.sun" />',
            '<spring:message code="calendar.day.short.mon" />',
            '<spring:message code="calendar.day.short.tue" />',
            '<spring:message code="calendar.day.short.wed" />',
            '<spring:message code="calendar.day.short.thu" />',
            '<spring:message code="calendar.day.short.fri" />',
            '<spring:message code="calendar.day.short.sat" />'
        ]
    };
</script>
<script
        src="<c:url value='/js/date-time-picker-multiple.js'/>"></script>
</body>
</html>
