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
    <title><spring:message code="verification.confirm.title"/></title>

    <!-- Include register.css -->
    <link rel="stylesheet" href="<c:url value='/css/register.css'/> ">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap"
          rel="stylesheet">
    <style>
        .confirmation-container {
            text-align: center;
            padding: 2rem;
        }

        .confirmation-icon {
            font-size: 4rem;
            color: var(--primary-color);
            margin-bottom: 1.5rem;
        }

        .confirmation-title {
            font-size: 1.8rem;
            font-weight: 600;
            margin-bottom: 1rem;
            color: var(--text-color);
        }

        .confirmation-message {
            font-size: 1.1rem;
            color: var(--text-light);
            margin-bottom: 2rem;
            max-width: 600px;
            margin-left: auto;
            margin-right: auto;
        }

        .confirmation-actions {
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
            border: none;
            cursor: pointer;
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

        .token-info {
            background-color: var(--gray-100);
            padding: 1rem;
            border-radius: var(--border-radius);
            margin-bottom: 2rem;
            display: inline-block;
        }
        .card-header {
           margin-top: 10rem;
        }
        .token-label {
            font-weight: 500;
            color: var(--text-light);
            margin-bottom: 0.5rem;
        }

        .token-value {
            font-family: monospace;
            font-size: 1rem;
            color: var(--text-color);
            word-break: break-all;
        }
    </style>
</head>
<body>
<!-- Include Header Component -->
<jsp:include page="/WEB-INF/jsp/components/header.jsp"/>

<div class="register-container">
    <div class="register-card">
        <div class="card-header">
            <h1 class="card-title-register"><spring:message code="verification.confirm.title"/></h1>
            <p class="card-subtitle-register"><spring:message code="verification.confirm.subtitle"/></p>
        </div>

        <div class="card-body">
            <div class="confirmation-container">
                <div class="confirmation-icon">
                    <i class="fas fa-user-check"></i>
                </div>
                <h2 class="confirmation-title"><spring:message code="verification.confirm.heading"/></h2>
                <p class="confirmation-message"><spring:message code="verification.confirm.message"/></p>

                <form action="${pageContext.request.contextPath}/verify" method="get">
                    <input type="hidden" name="token" value="${param.token}">
                    <div class="confirmation-actions">
                        <button type="submit" class="btn-primary">
                            <i class="fas fa-check-circle"></i>
                            <spring:message code="verification.confirm.button"/>
                        </button>
                        <a href="${pageContext.request.contextPath}/" class="btn-secondary">
                            <i class="fas fa-home"></i>
                            <spring:message code="verification.back.to.home"/>
                        </a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

</body>
</html>
