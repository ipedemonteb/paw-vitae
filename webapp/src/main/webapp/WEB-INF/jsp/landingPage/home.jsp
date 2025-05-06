<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link rel="icon" type="image/png" href="https://upload.wikimedia.org/wikipedia/commons/thumb/a/aa/V-logo.svg/2048px-V-logo.svg.png" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="landing.page.title" /></title>
    <link rel="stylesheet" href="<c:url value='/css/landing.css' />" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/components/header.jsp">
    <jsp:param name="id" value="${imageId}" />
</jsp:include>

<main>
    <!-- Hero Section -->
    <section class="hero-section">
        <div class="container">
            <div class="hero-content">
                <h1 class="hero-title"><spring:message code="landing.hero.title" /></h1>
                <p class="hero-subtitle"><spring:message code="landing.hero.subtitle" /></p>

                <sec:authorize access="!hasRole('ROLE_DOCTOR')">
                    <div class="search-container">
                        <form action="<c:url value='/search' />" method="get" class="search-form" id="searchForm">
                            <div class="search-bar">
                                <div class="specialty-select-wrapper">
                                    <i class="fas fa-stethoscope"></i>
                                    <select name="specialty" class="specialty-select" id="specialtyDropdown">
                                        <option value="0"><spring:message code="search.all.specialties"/></option>
                                        <c:forEach items="${specialties}" var="specialty">
                                            <option value="${specialty.id}"
                                                    <c:if test="${specialty.id == selectedSpecialtyId}">selected</c:if>>
                                                <spring:message code="${specialty.key}" />
                                            </option>
                                        </c:forEach>
                                    </select>
                                    <i class="fas fa-chevron-down"></i>
                                </div>
                                <button type="submit" class="search-button" id="searchButton">
                                    <i class="fas fa-search"></i>
                                    <span><spring:message code="landing.search.button" /></span>
                                </button>
                            </div>
                        </form>
                    </div>
                </sec:authorize>

                <div class="hero-stats">
                    <div class="stat-item">
                        <span class="stat-number"><spring:message code="landing.stats.doctors.number" /></span>
                        <span class="stat-label"><spring:message code="landing.stats.doctors.label" /></span>
                    </div>
                    <div class="stat-item">
                        <span class="stat-number"><spring:message code="landing.stats.patients.number" /></span>
                        <span class="stat-label"><spring:message code="landing.stats.patients.label" /></span>
                    </div>
                    <div class="stat-item">
                        <span class="stat-number"><spring:message code="landing.stats.rating.number" /></span>
                        <span class="stat-label"><spring:message code="landing.stats.rating.label" /></span>
                    </div>
                </div>
            </div>
            <div class="hero-image">
                <%--            <img src="<c:url value='/images/doctor-hero.png' />" alt="<spring:message code="landing.hero.image.alt" />" />--%>
            </div>
        </div>
    </section>

    <!-- Mission Section -->
    <section class="mission-section">
        <div class="container">
            <div class="section-header">
                <span class="section-tag"><spring:message code="landing.mission.tag" /></span>
                <h2 class="section-title"><spring:message code="landing.mission.title" /></h2>
                <p class="section-subtitle"><spring:message code="landing.mission.subtitle" /></p>
            </div>

            <div class="mission-cards">
                <div class="mission-card">
                    <div class="card-icon">
                        <i class="fas fa-universal-access"></i>
                    </div>
                    <h3><spring:message code="landing.mission.accessibility.title" /></h3>
                    <p><spring:message code="landing.mission.accessibility.text" /></p>
                </div>

                <div class="mission-card">
                    <div class="card-icon">
                        <i class="fas fa-award"></i>
                    </div>
                    <h3><spring:message code="landing.mission.quality.title" /></h3>
                    <p><spring:message code="landing.mission.quality.text" /></p>
                </div>

                <div class="mission-card">
                    <div class="card-icon">
                        <i class="fas fa-lightbulb"></i>
                    </div>
                    <h3><spring:message code="landing.mission.innovation.title" /></h3>
                    <p><spring:message code="landing.mission.innovation.text" /></p>
                </div>
            </div>
        </div>
    </section>

    <!-- How It Works Section -->
    <section class="how-it-works-section">
        <div class="container">
            <div class="section-header">
                <span class="section-tag"><spring:message code="landing.howItWorks.tag" /></span>
                <h2 class="section-title"><spring:message code="landing.howItWorks.title" /></h2>
                <p class="section-subtitle"><spring:message code="landing.howItWorks.subtitle" /></p>
            </div>

            <div class="steps-container">
                <div class="step">
                    <div class="step-number">1</div>
                    <div class="step-content">
                        <div class="step-icon">
                            <i class="fas fa-search"></i>
                        </div>
                        <h3><spring:message code="landing.howItWorks.step1.title" /></h3>
                        <p><spring:message code="landing.howItWorks.step1.text" /></p>
                    </div>
                </div>

                <div class="step">
                    <div class="step-number">2</div>
                    <div class="step-content">
                        <div class="step-icon">
                            <i class="fas fa-calendar-alt"></i>
                        </div>
                        <h3><spring:message code="landing.howItWorks.step2.title" /></h3>
                        <p><spring:message code="landing.howItWorks.step2.text" /></p>
                    </div>
                </div>

                <div class="step">
                    <div class="step-number">3</div>
                    <div class="step-content">
                        <div class="step-icon">
                            <i class="fas fa-stethoscope"></i>
                        </div>
                        <h3><spring:message code="landing.howItWorks.step3.title" /></h3>
                        <p><spring:message code="landing.howItWorks.step3.text" /></p>
                    </div>
                </div>
            </div>

            <div class="cta-container">
                <a href="<c:url value='/register' />" class="btn btn-primary btn-large">
                    <spring:message code="landing.cta.button" />
                </a>
            </div>
        </div>
    </section>

    <!-- Features Section -->
    <section class="features-section">
        <div class="container">
            <div class="section-header">
                <span class="section-tag"><spring:message code="landing.features.tag" /></span>
                <h2 class="section-title"><spring:message code="landing.features.title" /></h2>
                <p class="section-subtitle"><spring:message code="landing.features.subtitle" /></p>
            </div>

            <div class="features-grid">
                <div class="feature-card">
                    <div class="feature-icon">
                        <i class="fas fa-check-circle"></i>
                    </div>
                    <h3><spring:message code="landing.features.verified.title" /></h3>
                    <p><spring:message code="landing.features.verified.text" /></p>
                </div>

                <div class="feature-card">
                    <div class="feature-icon">
                        <i class="fas fa-shield-alt"></i>
                    </div>
                    <h3><spring:message code="landing.features.secure.title" /></h3>
                    <p><spring:message code="landing.features.secure.text" /></p>
                </div>

                <div class="feature-card">
                    <div class="feature-icon">
                        <i class="fas fa-hand-pointer"></i>
                    </div>
                    <h3><spring:message code="landing.features.easy.title" /></h3>
                    <p><spring:message code="landing.features.easy.text" /></p>
                </div>

                <div class="feature-card">
                    <div class="feature-icon">
                        <i class="fas fa-comments"></i>
                    </div>
                    <h3><spring:message code="landing.features.communication.title" /></h3>
                    <p><spring:message code="landing.features.communication.text" /></p>
                </div>
            </div>
        </div>
    </section>

    <!-- Doctor Ratings Section -->
    <c:if test="${not empty ratings}">
        <section class="testimonials-section doctor-ratings-section">
            <div class="container">
                <div class="section-header">
                    <span class="section-tag"><spring:message code="landing.doctor.ratings.tag" text="Calificaciones" /></span>
                    <h2 class="section-title"><spring:message code="landing.doctor.ratings.title" text="Calificaciones de Doctores" /></h2>
                    <p class="section-subtitle"><spring:message code="landing.doctor.ratings.subtitle" text="Vea lo que nuestros pacientes dicen sobre nuestros doctores" /></p>
                </div>

                <div class="testimonials-slider">
                    <c:forEach items="${ratings}" var="entry" varStatus="status">
                        <div class="testimonial-card doctor-rating-card">
                            <div class="testimonial-content">
                                <div class="quote-icon"><i class="fas fa-quote-left"></i></div>
                                <p class="testimonial-text"><c:out value="${entry.key.comment}"/></p>
                                <div class="testimonial-rating">
                                    <c:forEach begin="1" end="5" var="star">
                                        <i class="fas fa-star ${star <= entry.key.rating ? '' : 'far'}"></i>
                                    </c:forEach>
                                </div>
                            </div>
                            <div class="testimonial-author">
                                <div class="author-avatar">
                                        <%-- <img src="<c:url value='/images/avatar-${status.index + 1}.jpg' />" alt="${entry.value.name}" /> --%>
                                </div>
                                <div class="author-info">
                                    <h4 class="author-name">${entry.value.name} ${entry.value.lastName}</h4>
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
        </section>
    </c:if>

    <!-- Footer -->

    <!-- Back to top button -->
    <button id="backToTop" class="back-to-top">
        <i class="fas fa-arrow-up"></i>
    </button>
</main>

<!-- Scripts -->
<script src="<c:url value='/js/autocomplete.js'/>"></script>
<script>
    contextPath = "${pageContext.request.contextPath}";
    doctorNames = [
        <c:forEach items="${doctors}" var="doctor" varStatus="status">
        "${fn:escapeXml(doctor.name)} ${fn:escapeXml(doctor.lastName)}"<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    ]

    document.getElementById('searchForm').addEventListener('submit', function(event) {
        const specialtyDropdown = document.getElementById('specialtyDropdown');
        if (specialtyDropdown.value === '0') {
            specialtyDropdown.name = '';
        }
    });

    // Mobile menu toggle
    document.querySelector('.mobile-menu-toggle').addEventListener('click', function() {
        document.querySelector('.main-nav').classList.toggle('active');
        this.classList.toggle('active');
    });

    // Back to top button
    const backToTopButton = document.getElementById('backToTop');

    window.addEventListener('scroll', () => {
        if (window.pageYOffset > 300) {
            backToTopButton.classList.add('show');
        } else {
            backToTopButton.classList.remove('show');
        }
    });

    backToTopButton.addEventListener('click', () => {
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    });

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
