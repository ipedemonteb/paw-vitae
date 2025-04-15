<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="comp" tagdir="/WEB-INF/tags/components" %>

<link rel="stylesheet" href="<c:url value='/css/landing.css' />" />

<layout:page title="landing.page.title" container="false">
    <!-- Hero Section with Search -->
    <section class="hero-section">
        <div class="hero-container">
            <div class="hero-content">
                <h1 class="hero-title-landing"><spring:message code="landing.hero.title" /></h1>
                <p class="hero-subtitle-landing"><spring:message code="landing.hero.subtitle" /></p>

                <!-- Search Bar -->
                <div class="search-container">
                    <form action="<c:url value='/search' />" method="get" class="search-form" id="searchForm">
                        <div class="search-bar">
                            <div class="search-input-wrapper">
                                <span class="search-icon"></span>
                                <input
                                        type="text"
                                        name="query"
                                        id="doctorSearch"
                                        class="search-input"
                                        placeholder="<spring:message code="landing.search.placeholder" />"
                                        autocomplete="off"
                                />
                                <!-- Autocomplete suggestions container -->
                                <div id="suggestions-container" class="suggestions-container"></div>
                            </div>

                            <div class="specialty-dropdown-wrapper">
                                <select name="specialty" class="specialty-dropdown" id="specialtyDropdown">
                                    <option value=""><spring:message code="landing.search.allSpecialties" /></option>
                                    <c:forEach items="${specialties}" var="specialty">
                                        <option value="${specialty.id}"><spring:message code="${specialty.key}" /></option>
                                    </c:forEach>
                                </select>
                            </div>

                            <button type="submit" class="search-button" id="searchButton">
                                <span class="search-button-text"><spring:message code="landing.search.button" /></span>
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </section>

    <!-- Mission Section -->
    <section class="mission-section">
        <div class="container">
            <div class="section-header">
                <h2 class="section-title"><spring:message code="landing.mission.title" /></h2>
                <p class="section-subtitle"><spring:message code="landing.mission.subtitle" /></p>
            </div>

            <div class="mission-grid">
                <div class="mission-card">
                    <div class="mission-icon accessibility-icon"></div>
                    <h3 class="mission-card-title"><spring:message code="landing.mission.accessibility.title" /></h3>
                    <p class="mission-card-text"><spring:message code="landing.mission.accessibility.text" /></p>
                </div>

                <div class="mission-card">
                    <div class="mission-icon quality-icon"></div>
                    <h3 class="mission-card-title"><spring:message code="landing.mission.quality.title" /></h3>
                    <p class="mission-card-text"><spring:message code="landing.mission.quality.text" /></p>
                </div>

                <div class="mission-card">
                    <div class="mission-icon innovation-icon"></div>
                    <h3 class="mission-card-title"><spring:message code="landing.mission.innovation.title" /></h3>
                    <p class="mission-card-text"><spring:message code="landing.mission.innovation.text" /></p>
                </div>
            </div>
        </div>
    </section>

    <!-- How It Works Section -->
    <section class="how-it-works-section">
        <div class="container">
            <div class="section-header">
                <h2 class="section-title"><spring:message code="landing.howItWorks.title" /></h2>
                <p class="section-subtitle"><spring:message code="landing.howItWorks.subtitle" /></p>
            </div>

            <div class="steps-container">
                <div class="step-item">
                    <div class="step-number">1</div>
                    <div class="step-icon search-step-icon"></div>
                    <h3 class="step-title"><spring:message code="landing.howItWorks.step1.title" /></h3>
                    <p class="step-text"><spring:message code="landing.howItWorks.step1.text" /></p>
                </div>

                <div class="step-connector"></div>

                <div class="step-item">
                    <div class="step-number">2</div>
                    <div class="step-icon calendar-step-icon"></div>
                    <h3 class="step-title"><spring:message code="landing.howItWorks.step2.title" /></h3>
                    <p class="step-text"><spring:message code="landing.howItWorks.step2.text" /></p>
                </div>

                <div class="step-connector"></div>

                <div class="step-item">
                    <div class="step-number">3</div>
                    <div class="step-icon visit-step-icon"></div>
                    <h3 class="step-title"><spring:message code="landing.howItWorks.step3.title" /></h3>
                    <p class="step-text"><spring:message code="landing.howItWorks.step3.text" /></p>
                </div>
            </div>

<%--            <div class="cta-container">--%>
<%--                <a href="<c:url value='/register' />" class="cta-button">--%>
<%--                    <spring:message code="landing.cta.button" />--%>
<%--                </a>--%>
<%--                <p class="cta-subtext"><spring:message code="landing.cta.subtext" /></p>--%>
<%--            </div>--%>
        </div>
    </section>

    <!-- Features Section -->
    <section class="features-section">
        <div class="container">
            <div class="section-header">
                <h2 class="section-title"><spring:message code="landing.features.title" /></h2>
                <p class="section-subtitle"><spring:message code="landing.features.subtitle" /></p>
            </div>

            <div class="features-grid">
                <div class="feature-card">
                    <div class="feature-icon verified-icon"></div>
                    <h3 class="feature-title"><spring:message code="landing.features.verified.title" /></h3>
                    <p class="feature-text"><spring:message code="landing.features.verified.text" /></p>
                </div>

                <div class="feature-card">
                    <div class="feature-icon secure-icon"></div>
                    <h3 class="feature-title"><spring:message code="landing.features.secure.title" /></h3>
                    <p class="feature-text"><spring:message code="landing.features.secure.text" /></p>
                </div>

                <div class="feature-card">
                    <div class="feature-icon easy-icon"></div>
                    <h3 class="feature-title"><spring:message code="landing.features.easy.title" /></h3>
                    <p class="feature-text"><spring:message code="landing.features.easy.text" /></p>
                </div>
            </div>
        </div>
    </section>

    <comp:site-footer />

    <!-- Inline script for autocomplete functionality -->
    <script src="<c:url value='/js/autocomplete.js'/>"></script>
    <script>
        contextPath = "${pageContext.request.contextPath}";
    </script>
</layout:page>