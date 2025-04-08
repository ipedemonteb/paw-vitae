<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="comp" tagdir="/WEB-INF/tags/components" %>


<layout:page title="landing.page.title" container="false">
    <comp:site-header />

    <main class="main-content">
        <comp:hero-section
                title="landing.welcome"
                description="landing.welcome.description"
                buttonText="startnow"
                buttonUrl="/register" />

        <comp:features-section title="features.title">
            <comp:feature-item
                    title="features.header"
                    description="features.description" />
            <comp:feature-item
                    title="features.header2"
                    description="features.description2" />
            <comp:feature-item
                    title="features.header3"
                    description="features.description3" />
        </comp:features-section>
    </main>

    <comp:site-footer />
</layout:page>