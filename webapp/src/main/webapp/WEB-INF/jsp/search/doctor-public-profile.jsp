<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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
                        <c:choose>
                            <c:when test="${not empty doctor.profile.bio}">
                                <p class="bio-text"><c:out value="${doctor.profile.bio}" /></p>
                            </c:when>
                            <c:otherwise>
                                <p class="bio-text">
<%--                                    <spring:message code="doctor.profile.default.bio" arguments="${doctor.name},${doctor.lastName}" />--%>
                                    Bio
                                </p>
                            </c:otherwise>
                        </c:choose>
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

        <!-- Doctor Details Grid -->
        <div class="details-grid">
            <!-- About/Description Section -->
            <c:if test="${not empty doctor.profile.description}">
            <div class="detail-card full-width">
                <div class="detail-card-header">
                    <h3><i class="fas fa-user"></i>About</h3>
                </div>
                <div class="detail-card-content">
                            <div class="description-content">
                                <p><c:out value="${doctor.profile.description}" /></p>
                            </div>
                </div>
            </div>
            </c:if>

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

            <!-- Career Path Section -->
            <c:if test="${not empty doctor.experiences}">
            <div class="detail-card full-width">
                <div class="detail-card-header">
                    <h3><i class="fas fa-briefcase"></i> Path</h3>
                </div>
                <div class="detail-card-content">
                            <div class="career-timeline">
                                <c:forEach var="career" items="${doctor.experiences}">
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
                </div>
            </div>
            </c:if>

            <!-- Certificates Section -->
            <c:if test="${not empty doctor.certifications}">
            <div class="detail-card full-width">
                <div class="detail-card-header">
                    <h3><i class="fas fa-certificate"></i> Certificates</h3>
                </div>
                <div class="detail-card-content">
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
                </div>
            </div>
            </c:if>

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
            </div>
        </c:if>

        <!-- Call to Action -->
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
    </div>
</main>

<script>

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

    // Set current year for copyright
    document.addEventListener('DOMContentLoaded', function() {
        window.currentYear = new Date().getFullYear();
    });
</script>

</body>
</html>