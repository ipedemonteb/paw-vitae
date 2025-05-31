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
    <title><spring:message code="doctor.profile.title" arguments="${doctor.name},${doctor.lastName}" /></title>
    <link rel="icon" type="image/png" href="https://upload.wikimedia.org/wikipedia/commons/thumb/a/aa/V-logo.svg/2048px-V-logo.svg.png" />
    <link rel="stylesheet" href="<c:url value='/css/doctor-profile.css' />" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>

<jsp:include page="/WEB-INF/jsp/components/header.jsp" />

<!-- Main Content -->
<main class="main-content">
    <div class="container">
        <!-- Back Button -->
        <div class="back-navigation">
            <a href="javascript:history.back()" class="back-btn">
                <i class="fas fa-arrow-left"></i>
                <spring:message code="doctor.profile.back" />
            </a>
        </div>

        <!-- Check if logged user is the same as the profile doctor -->
        <c:set var="isOwnProfile" value="${loggedUser != null && loggedUser.id == doctor.id}" />

        <!-- Doctor Profile Header -->
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
                </div>

                <div class="doctor-info-section">
                    <h1 class="doctor-name">
                        <c:out value="Dr. ${doctor.name} ${doctor.lastName}" />
                    </h1>

                    <!-- Bio Section -->
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
                                <textarea id="bio-input" class="form-control" maxlength="220" placeholder="placeholder"><c:out value="${doctor.profile.bio}" /></textarea>
                                <div class="char-counter">
                                    <span id="bio-char-count">0</span>/220
                                </div>
                            </div>
                        </c:if>
                    </div>

                    <!-- Rating Section -->
                    <div class="rating-section">
                        <c:choose>
                            <c:when test="${doctor.ratingCount > 0}">
                                <div class="stars-large">
                                    <c:set var="fullStars" value="${doctor.rating.intValue()}" />
                                    <c:set var="hasHalfStar" value="${doctor.rating - fullStars >= 0.5}" />
                                    <c:set var="emptyStars" value="${5 - fullStars - (hasHalfStar ? 1 : 0)}" />

                                    <!-- Render full stars -->
                                    <c:forEach begin="1" end="${fullStars}" var="i">
                                        <i class="fas fa-star"></i>
                                    </c:forEach>

                                    <!-- Render half star if applicable -->
                                    <c:if test="${hasHalfStar}">
                                        <i class="fas fa-star-half-alt"></i>
                                    </c:if>

                                    <!-- Render empty stars -->
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

        <!-- Global Edit Controls -->
        <c:if test="${isOwnProfile}">
            <div class="global-edit-controls">
                <div id="edit-mode-controls" class="edit-controls-container">
                    <button type="button" id="edit-btn" class="btn btn-primary" onclick="enterEditMode()">
                        <i class="fas fa-edit"></i> <spring:message code="dashboard.profile.edit" />
                    </button>
                </div>

                <div id="save-mode-controls" class="edit-controls-container" style="display: none;">
                    <button type="button" id="save-btn" class="btn btn-success" onclick="saveAllChanges()">
                        <i class="fas fa-save"></i> <spring:message code="appointment.form.save" />
                    </button>
                    <button type="button" id="cancel-btn" class="btn btn-secondary" onclick="cancelAllChanges()">
                        <i class="fas fa-times"></i> <spring:message code="appointment.cancel" />
                    </button>
                </div>
            </div>
        </c:if>

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
                                <p class="no-data"><spring:message code="doctor.profile.default.bio" /></p>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <c:if test="${isOwnProfile}">
                        <div id="description-edit" class="edit-form" style="display: none;">
                            <textarea id="description-input" class="form-control" maxlength="2600" rows="6" placeholder="placeholder"><c:out value="${doctor.profile.description}" /></textarea>
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
                                <div class="specialty-list">
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

                <!-- Coverage Section -->
                <div class="detail-card">
                    <div class="detail-card-header">
                        <h3><i class="fas fa-shield-alt"></i> <spring:message code="doctor.profile.coverages" /></h3>
                    </div>
                    <div class="detail-card-content">
                        <c:choose>
                            <c:when test="${not empty doctor.coverageList}">
                                <div class="coverage-list">
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

            <!-- Career Path Section with Edit Capability -->
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
                                                        ${career.startDate} - ${career.endDate}
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <p class="no-data"><spring:message code="doctor.profile.default.bio" /></p>
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
                                                <label>postion (s)</label>
                                                <input type="text" class="form-control" name="experiences[${status.index}].positionTitle" value="<c:out value='${experience.positionTitle}' />" />
                                            </div>
                                            <div class="form-group">
                                                <label>org (s)</label>
                                                <input type="text" class="form-control" name="experiences[${status.index}].organizationName" value="<c:out value='${experience.organizationName}' />" />
                                            </div>
                                        </div>
                                        <div class="form-row">
                                            <div class="form-group">
                                                <label>start date (s)</label>
                                                <input type="date" class="form-control" name="experiences[${status.index}].startDate" value="${experience.startDate}" />
                                            </div>
                                            <div class="form-group">
                                                <label>end date (s)</label>
                                                <input type="date" class="form-control" name="experiences[${status.index}].endDate" value="${experience.endDate}" />
                                            </div>
                                        </div>
                                        <button type="button" class="btn-remove" onclick="removeExperience(${status.index})">
                                            <i class="fas fa-trash"></i>
                                        </button>
                                    </div>
                                </c:forEach>
                            </div>
                            <button type="button" class="btn btn-secondary" onclick="addExperience()">
                                <i class="fas fa-plus"></i> add experience (s)
                            </button>
                        </div>
                    </c:if>
                </div>
            </div>

            <!-- Certificates Section with Edit Capability -->
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
                                                        ${certificate.issueDate}
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <p class="no-data"><spring:message code="doctor.profile.default.bio" /></p>
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
                                                <label>name (s)</label>
                                                <input type="text" class="form-control" name="certificates[${status.index}].certificateName" value="<c:out value='${certificate.certificateName}' />" />
                                            </div>
                                            <div class="form-group">
                                                <label>entity (s)</label>
                                                <input type="text" class="form-control" name="certificates[${status.index}].issuingEntity" value="<c:out value='${certificate.issuingEntity}' />" />
                                            </div>
                                        </div>
                                        <div class="form-row">
                                            <div class="form-group">
                                                <label>issue date (s)</label>
                                                <input type="date" class="form-control" name="certificates[${status.index}].issueDate" value="${certificate.issueDate}" />
                                            </div>
                                        </div>
                                        <button type="button" class="btn-remove" onclick="removeCertificate(${status.index})">
                                            <i class="fas fa-trash"></i>
                                        </button>
                                    </div>
                                </c:forEach>
                            </div>
                            <button type="button" class="btn btn-secondary" onclick="addCertificate()">
                                <i class="fas fa-plus"></i> add certificate (s)
                            </button>
                        </div>
                    </c:if>
                </div>
            </div>

            <!-- Office Locations Section -->
            <div class="detail-card full-width">
                <div class="detail-card-header">
                    <h3><i class="fas fa-map-marker-alt"></i> <spring:message code="doctor.profile.offices" /></h3>
                </div>
                <div class="detail-card-content">
                    <c:choose>
                        <c:when test="${not empty doctor.doctorOffices}">
                            <div class="office-list">
                                <c:forEach var="office" items="${doctor.doctorOffices}">
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
                                        <h4 class="author-name">${entry.patient.name} ${entry.patient.lastName}</h4>
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

            <sec:authorize access="hasRole('ROLE_PATIENT')">>
            <div class="cta-section">
                <div class="cta-content">
                    <h3><spring:message code="doctor.profile.ready.to.book" /></h3>
                    <p>
                        <spring:message code="doctor.profile.book.description" arguments="${doctor.name},${doctor.lastName}" />
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

<!-- Hidden form for submitting updates -->
<c:if test="${isOwnProfile}">
    <form:form id="updateProfileForm" method="POST" action="/doctor/profile/update" modelAttribute="doctorProfileForm" style="display: none;">
        <form:hidden path="biography" id="hiddenBio" />
        <form:hidden path="description" id="hiddenDescription" />
        <div id="hiddenExperiences"></div>
        <div id="hiddenCertificates"></div>
    </form:form>
</c:if>

<script>
    let experienceIndex = ${fn:length(doctor.experiences)};
    let certificateIndex = ${fn:length(doctor.certifications)};
    let isEditMode = false;

    // Store original values for cancel functionality
    let originalValues = {
        bio: '${doctor.profile.bio}',
        description: '${doctor.profile.description}',
        experiences: [],
        certificates: []
    };

    // Doctor ratings slider controls
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

        // Initialize rating display
        showRating(currentRating);
    }

    // Global edit functionality
    function enterEditMode() {
        isEditMode = true;

        // Store original values
        storeOriginalValues();

        // Show edit forms and hide display content
        const editableSections = ['bio', 'description', 'experiences', 'certificates'];
        editableSections.forEach(section => {
            const displayDiv = document.getElementById(section + '-display');
            const editDiv = document.getElementById(section + '-edit');

            if (displayDiv && editDiv) {
                displayDiv.style.display = 'none';
                editDiv.style.display = 'block';
            }
        });

        // Update character counters
        updateCharCount('bio');
        updateCharCount('description');

        // Switch control buttons
        document.getElementById('edit-mode-controls').style.display = 'none';
        document.getElementById('save-mode-controls').style.display = 'flex';
    }

    function cancelAllChanges() {
        isEditMode = false;

        // Restore original values
        restoreOriginalValues();

        // Hide edit forms and show display content
        const editableSections = ['bio', 'description', 'experiences', 'certificates'];
        editableSections.forEach(section => {
            const displayDiv = document.getElementById(section + '-display');
            const editDiv = document.getElementById(section + '-edit');

            if (displayDiv && editDiv) {
                displayDiv.style.display = 'block';
                editDiv.style.display = 'none';
            }
        });

        // Switch control buttons
        document.getElementById('edit-mode-controls').style.display = 'flex';
        document.getElementById('save-mode-controls').style.display = 'none';
    }

    function saveAllChanges() {
        const form = document.getElementById('updateProfileForm');

        // Save bio
        const bioValue = document.getElementById('bio-input').value;
        document.getElementById('hiddenBio').value = bioValue;

        // Save description
        const descValue = document.getElementById('description-input').value;
        document.getElementById('hiddenDescription').value = descValue;

        // Save experiences and certificates
        saveExperiences();
        saveCertificates();

        form.submit();
    }

    function storeOriginalValues() {
        // Store experiences
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

        // Store certificates
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
                <label>position (S)</label>
                <input type="text" class="form-control" name="experiences[` + index + `].positionTitle" value="` + (data.positionTitle || '') + `" />
            </div>
            <div class="form-group">
                <label>organizatioon (s)</label>
                <input type="text" class="form-control" name="experiences[` + index + `].organizationName" value="` + (data.organizationName || '') + `" />
            </div>
        </div>
        <div class="form-row">
            <div class="form-group">
                <label>start (s)</label>
                <input type="date" class="form-control" name="experiences[` + index + `].startDate" value="` + (data.startDate || '') + `" />
            </div>
            <div class="form-group">
                <label>end (s)</label>
                <input type="date" class="form-control" name="experiences[` + index + `].endDate" value="` + (data.endDate || '') + `" />
            </div>
        </div>
        <button type="button" class="btn-remove" onclick="removeExperience(` + index + `)">
            <i class="fas fa-trash"></i>
        </button>
    `;

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
                <label>name (S)</label>
                <input type="text" class="form-control" name="certificates[` + index + `].certificateName" value="` + (data.certificateName || '') + `" />
            </div>
            <div class="form-group">
                <label>entity (s)</label>
                <input type="text" class="form-control" name="certificates[` + index + `].issuingEntity" value="` + (data.issuingEntity || '') + `" />
            </div>
        </div>
        <div class="form-row">
            <div class="form-group">
                <label>issue date (S)</label>
                <input type="date" class="form-control" name="certificates[` + index + `].issueDate" value="` + (data.issueDate || '') + `" />
            </div>
        </div>
        <button type="button" class="btn-remove" onclick="removeCertificate(` + index + `)">
            <i class="fas fa-trash"></i>
        </button>
    `;

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
    /* Global edit controls */
    .global-edit-controls {
        margin-bottom: 2rem;
        padding: 1.5rem;
        background: var(--card-background);
        border-radius: var(--border-radius-lg);
        box-shadow: var(--shadow-md);
        border-left: 4px solid var(--primary-color);
    }

    .edit-controls-container {
        display: flex;
        gap: 1rem;
        align-items: center;
    }

    .btn-success {
        background-color: var(--success-color);
        color: white;
        border: none;
    }

    .btn-success:hover {
        background-color: #059669;
        transform: translateY(-1px);
        box-shadow: var(--shadow-md);
    }

    /* Remove individual edit button styles */
    .btn-edit, .btn-edit-header {
        display: none;
    }

    .detail-card-header {
        display: flex;
        align-items: center;
    }

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

    @media (max-width: 768px) {
        .form-row {
            grid-template-columns: 1fr;
        }

        .edit-controls-container {
            flex-direction: column;
        }
    }
</style>

</body>
</html>
