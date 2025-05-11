<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link rel="icon" type="image/png" href="https://upload.wikimedia.org/wikipedia/commons/thumb/a/aa/V-logo.svg/2048px-V-logo.svg.png" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="about.page.title" /></title>
    <link rel="stylesheet" href="<c:url value='/css/landing.css' />" />
    <link rel="stylesheet" href="<c:url value='/css/about.css' />" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/components/header.jsp" >
    <jsp:param name="id" value="${imageId}"/>
</jsp:include>

<main class="landing-container">
    <!-- Hero Section -->
    <section class="hero-section about-hero">
        <div class="container">
            <div class="hero-content">
                <h1 class="hero-title"><spring:message code="about.hero.title" /></h1>
                <p class="hero-subtitle"><spring:message code="about.hero.subtitle" /></p>
            </div>
        </div>
    </section>

    <!-- Our Story Section -->
    <section class="story-section">
        <div class="container">
            <div class="section-header">
                <span class="section-tag"><spring:message code="about.story.tag" /></span>
                <h2 class="section-title"><spring:message code="about.story.title" /></h2>
                <p class="section-subtitle"><spring:message code="about.story.subtitle" /></p>
            </div>

            <div class="story-content">
                <div class="story-text">
                    <p><spring:message code="about.story.paragraph1" /></p>
                    <p><spring:message code="about.story.paragraph2" /></p>
                    <p><spring:message code="about.story.paragraph3" /></p>
                </div>
            </div>
        </div>
    </section>

    <!-- Our Mission Section -->
    <section class="mission-section about-mission">
        <div class="container">
            <div class="section-header">
                <span class="section-tag"><spring:message code="about.mission.tag" /></span>
                <h2 class="section-title"><spring:message code="about.mission.title" /></h2>
                <p class="section-subtitle"><spring:message code="about.mission.subtitle" /></p>
            </div>

            <div class="mission-cards">
                <div class="mission-card">
                    <div class="card-icon">
                        <i class="fas fa-universal-access"></i>
                    </div>
                    <h3><spring:message code="about.mission.accessibility.title" /></h3>
                    <p><spring:message code="about.mission.accessibility.text" /></p>
                </div>

                <div class="mission-card">
                    <div class="card-icon">
                        <i class="fas fa-award"></i>
                    </div>
                    <h3><spring:message code="about.mission.quality.title" /></h3>
                    <p><spring:message code="about.mission.quality.text" /></p>
                </div>

                <div class="mission-card">
                    <div class="card-icon">
                        <i class="fas fa-lightbulb"></i>
                    </div>
                    <h3><spring:message code="about.mission.innovation.title" /></h3>
                    <p><spring:message code="about.mission.innovation.text" /></p>
                </div>
            </div>
        </div>
    </section>

    <!-- Values Section -->
    <section class="values-section">
        <div class="container">
            <div class="section-header">
                <span class="section-tag"><spring:message code="about.values.tag" /></span>
                <h2 class="section-title"><spring:message code="about.values.title" /></h2>
                <p class="section-subtitle"><spring:message code="about.values.subtitle" /></p>
            </div>

            <div class="values-grid">
                <div class="value-card">
                    <div class="value-icon">
                        <i class="fas fa-heart"></i>
                    </div>
                    <h3><spring:message code="about.values.care.title" /></h3>
                    <p><spring:message code="about.values.care.text" /></p>
                </div>

                <div class="value-card">
                    <div class="value-icon">
                        <i class="fas fa-shield-alt"></i>
                    </div>
                    <h3><spring:message code="about.values.trust.title" /></h3>
                    <p><spring:message code="about.values.trust.text" /></p>
                </div>

                <div class="value-card">
                    <div class="value-icon">
                        <i class="fas fa-users"></i>
                    </div>
                    <h3><spring:message code="about.values.community.title" /></h3>
                    <p><spring:message code="about.values.community.text" /></p>
                </div>

                <div class="value-card">
                    <div class="value-icon">
                        <i class="fas fa-rocket"></i>
                    </div>
                    <h3><spring:message code="about.values.innovation.title" /></h3>
                    <p><spring:message code="about.values.innovation.text" /></p>
                </div>
            </div>
        </div>
    </section>

    <!-- Contact Section -->
    <section class="contact-section">
        <div class="container">
            <div class="section-header">
                <span class="section-tag"><spring:message code="about.contact.tag" /></span>
                <h2 class="section-title"><spring:message code="about.contact.title" /></h2>
                <p class="section-subtitle"><spring:message code="about.contact.subtitle" /></p>
            </div>

            <div class="contact-container">
                <div class="contact-info">
                    <div class="contact-item">
                        <div class="contact-icon">
                            <i class="fas fa-map-marker-alt"></i>
                        </div>
                        <div class="contact-text">
                            <h3><spring:message code="about.contact.address.title" /></h3>
                            <p><spring:message code="about.contact.address.text" /></p>
                        </div>
                    </div>

                    <div class="contact-item">
                        <div class="contact-icon">
                            <i class="fas fa-envelope"></i>
                        </div>
                        <div class="contact-text">
                            <h3><spring:message code="about.contact.email.title" /></h3>
                            <p><spring:message code="about.contact.email.text" /></p>
                        </div>
                    </div>

                    <div class="contact-item">
                        <div class="contact-icon">
                            <i class="fas fa-phone-alt"></i>
                        </div>
                        <div class="contact-text">
                            <h3><spring:message code="about.contact.phone.title" /></h3>
                            <p><spring:message code="about.contact.phone.text" /></p>
                        </div>
                    </div>
                </div>

                <div class="contact-map">
                    <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3282.5899976326584!2d-58.40244492425507!3d-34.64006077294961!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x95bccb0d94c6f2c3%3A0x4d3c471df45e6b7f!2sIguaz%C3%BA%20341%2C%20C1437ELE%20CABA!5e0!3m2!1sen!2sar!4v1682456788954!5m2!1sen!2sar" width="100%" height="450" style="border:0;" allowfullscreen="" loading="lazy" referrerpolicy="no-referrer-when-downgrade"></iframe>
                </div>
            </div>
        </div>
    </section>




    <!-- Back to top button -->
    <button id="backToTop" class="back-to-top">
        <i class="fas fa-arrow-up"></i>
    </button>
</main>

<!-- Scripts -->
<script>
    // Mobile menu toggle
    document.addEventListener('DOMContentLoaded', function() {

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
    });
</script>
</body>
</html>