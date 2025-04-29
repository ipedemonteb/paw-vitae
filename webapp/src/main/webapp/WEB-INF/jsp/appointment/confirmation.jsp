<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="icon" type="image/png" href="https://upload.wikimedia.org/wikipedia/commons/thumb/a/aa/V-logo.svg/2048px-V-logo.svg.png" />
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="appointment.confirmation.title" /></title>
    <link rel="stylesheet" href="<c:url value='/css/confirmation.css' />" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
<!-- Include the header -->
<jsp:include page="/WEB-INF/jsp/components/header.jsp" />

<!-- Main Content -->
<main class="main-content">
    <div class="container">
        <div class="confirmation-container">
            <div class="confirmation-header">
                <div class="confirmation-icon">
                    <i class="fas fa-check"></i>
                </div>
                <h1 class="confirmation-title"><spring:message code="appointment.confirmation.title"/></h1>
                <p class="confirmation-subtitle"><spring:message code="appointment.confirmation.subtitle"/></p>
            </div>

            <div class="confirmation-body">
                <!-- Appointment Details -->
                <div class="confirmation-details">
                    <div class="confirmation-item">
                        <div class="confirmation-label"><spring:message code="appointment.form.reason"/></div>
                        <div class="confirmation-value"><c:out value="${appointment.reason}"/></div>
                    </div>

                    <div class="confirmation-item">
                        <div class="confirmation-label"><spring:message code="appointment.selectedDoctor"/></div>
                        <div class="confirmation-value"><c:out value="${appointment.doctor.name} ${appointment.doctor.lastName}"/></div>
                    </div>

                    <div class="confirmation-item">
                        <div class="confirmation-label"><spring:message code="appointment.form.specialty"/></div>
                        <div class="confirmation-value"><spring:message code='${specialty.key}' /></div>
                    </div>

                    <div class="confirmation-item">
                        <div class="confirmation-label"><spring:message code="appointment.form.date"/></div>
                        <div class="confirmation-value">
                            <div style="display: flex; align-items: center;">
                                <div class="calendar-icon">
                                    <div class="calendar-month">
                                        <c:out value="${appointment.date.month}"/>
                                    </div>
                                    <div class="calendar-day">
                                        <c:out value="${appointment.date.dayOfMonth}"/>
                                    </div>
                                </div>
                                <span><c:out value="${appointment.date.toLocalDate().toString()}"/></span>
                            </div>
                        </div>
                    </div>

                    <div class="confirmation-item">
                        <div class="confirmation-label"><spring:message code="appointment.form.time"/></div>
                        <div class="confirmation-value">
                            <div style="display: flex; align-items: center;">
                                <i class="fas fa-clock" style="color: var(--primary-color); margin-right: 10px; font-size: 1.5rem;"></i>
                                <span><c:out value="${appointment.date.hour}"/>:00hs</span>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Attached Files Section -->
                <c:if test="${not empty patientFiles}">
                    <div class="files-section">
                        <h3 class="files-title">
                            <i class="fas fa-paperclip"></i>
                            <spring:message code="appointment.confirmation.attachedFiles" text="Archivos adjuntos" />
                        </h3>
                        <div class="files-container">
                            <c:forEach var="file" items="${patientFiles}" varStatus="status">
                                <div class="file-card" data-file-id="<c:out value="${file.id}"/>">
                                    <div class="file-icon">
                                        <i class="fas fa-file-pdf"></i>
                                    </div>
                                    <div class="file-details">
                                        <div class="file-name">${file.fileName}</div>
                                        <div class="file-meta">
                                            <span class="file-type">PDF</span>
                                        </div>
                                    </div>
                                    <a href="<c:url value='/appointment/${appointment.id}/file/${file.id}'/>" class="file-download" download="<c:out value="${file.fileName}"/>">
                                        <i class="fas fa-download"></i>
                                    </a>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </c:if>

                <!-- Doctor Information -->
                <c:if test="${not empty appointment.doctor}">
                    <div class="doctor-info">
                        <div class="doctor-image">
                            <img src="<c:url value='/doctor/${appointment.doctor.id}/image'/>" alt="<c:out value="${appointment.doctor.name}"/> <c:out value="${appointment.doctor.lastName}"/> " class="doctor-avatar">
                        </div>
                        <div class="doctor-details">
                            <h3 class="doctor-name"><c:out value="${appointment.doctor.name}"/> <c:out value="${appointment.doctor.lastName}"/> </h3>
                            <p class="doctor-specialty"><spring:message code="${specialty.key}" /></p>
                            <div class="doctor-contact">
                                <div class="contact-item">
                                    <span class="contact-icon"><i class="fas fa-envelope"></i></span>
                                    <span class="contact-text"><c:out value="${appointment.doctor.email}"/> </span>
                                </div>
                                <div class="contact-item">
                                    <span class="contact-icon"><i class="fas fa-phone"></i></span>
                                    <span class="contact-text"><c:out value="${appointment.doctor.phone}"/> </span>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>

            </div>

            <div class="confirmation-footer">
                <a href="<c:url value="/patient/dashboard"/>" class="btn btn-primary">
                    <i class="fas fa-home"></i> <spring:message code="appointment.confirmation.backToHome"/>
                </a>
                <a href="<c:url value='/appointment/${appointment.id}'/>" class="btn btn-outline">
                    <i class="fas fa-calendar-alt"></i> <spring:message code="appointment.confirmation.viewAppointments"/>
                </a>
            </div>
        </div>
    </div>
</main>

<script>
    // Add some interactive elements
    document.addEventListener('DOMContentLoaded', function() {
        // Add animation to confirmation items
        const items = document.querySelectorAll('.confirmation-item, .file-card');
        items.forEach((item, index) => {
            item.style.animation = `fadeIn 0.5s ease forwards ${index * 0.1}s`;
            item.style.opacity = '0';
        });

        // Add calendar animation
        const calendar = document.querySelector('.calendar-icon');
        if (calendar) {
            calendar.addEventListener('mouseenter', function() {
                this.style.transform = 'scale(1.1)';
            });
            calendar.addEventListener('mouseleave', function() {
                this.style.transform = 'scale(1)';
            });
        }

        // Add file card hover effects
        const fileCards = document.querySelectorAll('.file-card');
        fileCards.forEach(card => {
            card.addEventListener('mouseenter', function() {
                this.classList.add('hover');
            });
            card.addEventListener('mouseleave', function() {
                this.classList.remove('hover');
            });
        });

        const fixedHeader = document.querySelector(".main-header");
        const mainContent = document.querySelector(".main-content");

        if (fixedHeader && mainContent) {
            const adjustContentMargin = () => {
                const headerHeight = fixedHeader.offsetHeight;
                mainContent.style.marginTop = (headerHeight * 1.25) + `px`;
            };

            // Adjust on page load
            adjustContentMargin();

            // Adjust on window resize
            window.addEventListener("resize", adjustContentMargin);
        }
    });
</script>
</body>
</html>
