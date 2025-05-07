<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="icon" type="image/png"
          href="https://upload.wikimedia.org/wikipedia/commons/thumb/a/aa/V-logo.svg/2048px-V-logo.svg.png"/>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><spring:message code="verification.account.title"/></title>

    <!-- Include register.css -->
    <link rel="stylesheet" href="<c:url value='/css/register.css'/> ">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap"
          rel="stylesheet">
    <style>
        .verification-container {
            text-align: center;
            padding: 2rem;
        }

        .verification-icon {
            font-size: 4rem;
            margin-bottom: 1.5rem;
        }

        .verification-icon.success {
            color: var(--success);
        }

        .verification-icon.error {
            color: var(--danger);
        }

        .verification-title {
            font-size: 1.8rem;
            font-weight: 600;
            margin-bottom: 1rem;
            color: var(--text-color);
        }

        .verification-message {
            font-size: 1.1rem;
            color: var(--text-light);
            margin-bottom: 2rem;
            max-width: 600px;
            margin-left: auto;
            margin-right: auto;
        }

        .verification-actions {
            display: flex;
            justify-content: center;
            gap: 1rem;
        }

        .btn-primary {
            display: inline-flex;
            align-items: center;
            gap: 0.5rem;
            padding: 0.75rem 1.5rem;
            background-color: var(--primary-color);
            color: var(--white);
            border-radius: var(--border-radius);
            font-weight: 500;
            text-decoration: none;
            transition: var(--transition);
        }
    .card-header {
        margin-top: 10rem;
    }
        .btn-primary:hover {
            background-color: var(--primary-dark);
            transform: translateY(-2px);
            box-shadow: var(--shadow-md);
        }

        .btn-secondary {
            display: inline-flex;
            align-items: center;
            gap: 0.5rem;
            padding: 0.75rem 1.5rem;
            background-color: var(--white);
            color: var(--text-color);
            border: 1px solid var(--gray-300);
            border-radius: var(--border-radius);
            font-weight: 500;
            text-decoration: none;
            transition: var(--transition);
        }

        .btn-secondary:hover {
            background-color: var(--gray-100);
            border-color: var(--gray-400);
        }
    </style>
</head>
<body>
<!-- Include Header Component -->
<jsp:include page="/WEB-INF/jsp/components/header.jsp">
    <jsp:param name="id" value="${imageId}"/>
</jsp:include>

<div class="register-container">
    <div class="register-card">
        <div class="card-header">
            <h1 class="card-title-register"><spring:message code="verification.account.title"/></h1>
            <p class="card-subtitle-register"><spring:message code="verification.account.subtitle"/></p>
        </div>

        <div class="card-body">
            <div class="verification-container">
                <c:choose>
                    <c:when test="${param.success eq 'true'}">
                        <div class="verification-icon success">
                            <i class="fas fa-check-circle"></i>
                        </div>
                        <h2 class="verification-title"><spring:message code="verification.success.title"/></h2>
                        <p class="verification-message"><spring:message code="verification.success.message"/></p>

                        <div class="verification-actions">
                            <a href="${pageContext.request.contextPath}/" class="btn-secondary">
                                <i class="fas fa-home"></i>
                                <spring:message code="verification.back.to.home"/>
                            </a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="verification-icon error">
                            <i class="fas fa-times-circle"></i>
                        </div>
                        <h2 class="verification-title"><spring:message code="verification.error.title"/></h2>
                        <p class="verification-message"><spring:message code="verification.error.message"/></p>

                        <div class="verification-actions">
                            <a href="${pageContext.request.contextPath}/login" class="btn-secondary">
                                <i class="fas fa-sign-in-alt"></i>
                                <spring:message code="verification.try.login"/>
                            </a>
                            <a href="${pageContext.request.contextPath}/" class="btn-secondary">
                                <i class="fas fa-home"></i>
                                <spring:message code="verification.back.to.home"/>
                            </a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>

</body>
</html>
