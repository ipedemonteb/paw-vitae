<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="comp" tagdir="/WEB-INF/tags/components" %>

<link rel="stylesheet" href="<c:url value='/css/components/forms.css' />" />
<link rel="stylesheet" href="<c:url value='/css/components/register.css' />" />
<style>
    /* Custom multi-select styles */
    .multi-select-container {
        position: relative;
    }

    .multi-select-container .form-control {
        display: none; /* Hide the original select */
    }

    .custom-multi-select {
        border: 1px solid #ced4da;
        border-radius: 0.25rem;
        background-color: #fff;
        width: 100%;
        max-height: 200px;
        overflow-y: auto;
    }

    .custom-multi-select-option {
        padding: 8px 12px;
        cursor: pointer;
        display: flex;
        align-items: center;
        user-select: none; /* Prevent text selection */
        -webkit-user-select: none;
        -moz-user-select: none;
        -ms-user-select: none;
    }

    .custom-multi-select-option:hover {
        background-color: #f8f9fa;
    }

    .custom-multi-select-option.selected {
        background-color: #e9ecef;
    }

    .option-checkbox {
        width: 16px;
        height: 16px;
        border: 1px solid #ced4da;
        border-radius: 3px;
        margin-right: 8px;
        display: inline-flex;
        align-items: center;
        justify-content: center;
    }

    .custom-multi-select-option.selected .option-checkbox::after {
        content: "✓";
        color: #007bff;
        font-size: 12px;
    }

    .option-text {
        flex: 1;
    }

    .selected-options {
        margin-top: 8px;
        display: flex;
        flex-wrap: wrap;
        gap: 5px;
    }

    .selected-option-badge {
        background-color: #e9ecef;
        border-radius: 4px;
        padding: 4px 8px;
        display: flex;
        align-items: center;
        font-size: 0.875rem;
    }

    .remove-option {
        margin-left: 5px;
        cursor: pointer;
        color: #6c757d;
        font-weight: bold;
    }

    .remove-option:hover {
        color: #dc3545;
    }

    .search-box {
        width: 100%;
        padding: 8px;
        border: none;
        border-bottom: 1px solid #ced4da;
        margin-bottom: 8px;
    }
</style>

<layout:page title="register.doctor.title">
    <div class="register-container">
        <div class="register-card">
            <div class="card-header">
                <h1 class="card-title-register"><spring:message code="register.doctor.title" /></h1>
                <p class="card-subtitle-register"><spring:message code="register.doctor.subtitle" /></p>
            </div>

            <div class="card-body">
                <form:form modelAttribute="registerForm" method="post" action="${pageContext.request.contextPath}/register" enctype="multipart/form-data" cssClass="register-form">
                    <div class="form-row">
                        <div class="form-group">
                            <label for="name" class="form-label required-field"><spring:message code="register.firstName" /></label>
                            <form:input path="name" id="name" cssClass="form-control" />
                            <form:errors path="name" cssClass="error-message" />
                        </div>

                        <div class="form-group">
                            <label for="lastName" class="form-label required-field"><spring:message code="register.lastName" /></label>
                            <form:input path="lastName" id="lastName" cssClass="form-control" />
                            <form:errors path="lastName" cssClass="error-message" />
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="email" class="form-label required-field"><spring:message code="register.email" /></label>
                            <form:input path="email" id="email" cssClass="form-control" type="email" />
                            <form:errors path="email" cssClass="error-message" />
                        </div>

                        <div class="form-group">
                            <label for="phone" class="form-label required-field"><spring:message code="register.phone" /></label>
                            <form:input path="phone" id="phone" cssClass="form-control" />
                            <form:errors path="phone" cssClass="error-message" />
                        </div>
                    </div>

                    <div class="form-row">
                        <div class="form-group">
                            <label for="password" class="form-label required-field"><spring:message code="register.password" /></label>
                            <form:password path="password" id="password" cssClass="form-control" />
                            <form:errors path="password" cssClass="error-message" />
                        </div>

                        <div class="form-group">
                            <label for="repeatPassword" class="form-label required-field"><spring:message code="register.confirmPassword" /></label>
                            <form:password path="repeatPassword" id="repeatPassword" cssClass="form-control" />
                            <form:errors path="repeatPassword" cssClass="error-message" />
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="specialties" class="form-label required-field"><spring:message code="register.specialties" /></label>
                        <div class="multi-select-container" id="specialties-container">
                            <div class="custom-multi-select" id="specialties-options">
                                <c:forEach items="${specialtyList}" var="specialty">
                                    <div class="custom-multi-select-option" data-value="${specialty.id}" onclick="toggleOption(this, 'specialties')">
                                        <div class="option-checkbox"></div>
                                        <div class="option-text">
                                            <c:choose>
                                                <c:when test="${not empty specialty.key}">
                                                    <spring:message code="${specialty.key}"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:out value="${specialty.name}"/>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                            <div class="selected-options" id="specialties-selected"></div>
                            <form:select path="specialties" id="specialties" cssClass="form-control" multiple="true">
                                <c:forEach items="${specialtyList}" var="specialty">
                                    <form:option value="${specialty.id}">
                                        <c:choose>
                                            <c:when test="${not empty specialty.key}">
                                                <spring:message code="${specialty.key}"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="${specialty.name}"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </form:option>
                                </c:forEach>
                            </form:select>
                        </div>
                        <small class="form-text text-muted">
                            <spring:message code="register.selectSpecialties" />
                        </small>
                        <form:errors path="specialties" cssClass="error-message" />
                    </div>

                    <div class="form-group">
                        <label for="coverages" class="form-label required-field"><spring:message code="register.coverage" /></label>
                        <div class="multi-select-container" id="coverages-container">
                            <div class="custom-multi-select" id="coverages-options">
                                <c:forEach items="${coverageList}" var="coverage">
                                    <div class="custom-multi-select-option" data-value="${coverage.id}" onclick="toggleOption(this, 'coverages')">
                                        <div class="option-checkbox"></div>
                                        <div class="option-text">
                                            <c:out value="${coverage.name}"/>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                            <div class="selected-options" id="coverages-selected"></div>
                            <form:select path="coverages" id="coverages" cssClass="form-control" multiple="true">
                                <c:forEach items="${coverageList}" var="coverage">
                                    <form:option value="${coverage.id}"><c:out value="${coverage.name}"/></form:option>
                                </c:forEach>
                            </form:select>
                        </div>
                        <small class="form-text text-muted">
                            <spring:message code="register.selectCoverage" />
                        </small>
                        <form:errors path="coverages" cssClass="error-message" />
                    </div>

                    <div class="form-group">
                        <label class="form-label"><spring:message code="register.uploadImage" /></label>
                        <div class="file-upload">
                            <label class="file-upload-label">
                                <span class="file-upload-text"><spring:message code="register.uploadImage" /></span>
                                <form:input type="file" path="image" id="image-upload" accept="image/*" cssClass="file-input" />
                            </label>
                        </div>
                        <div id="file-name" class="file-name"></div>
                        <form:errors path="image" cssClass="error-message" />
                    </div>

                    <div class="form-group terms-checkbox">
                        <input type="checkbox" id="terms" class="custom-checkbox" required />
                        <label for="terms" class="checkbox-label required-field">
                            <spring:message code="register.agreeTerms" />
                            <a class="terms-link"><spring:message code="register.termsLink" /></a>
                        </label>
                    </div>

                    <div class="form-group">
                        <button type="submit" class="btn-submit">
                            <spring:message code="register.button" />
                        </button>
                    </div>
                </form:form>
            </div>
        </div>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Simple file upload preview
            const fileInput = document.getElementById('image-upload');
            const fileName = document.getElementById('file-name');

            if (fileInput) {
                fileInput.addEventListener('change', function() {
                    if (this.files && this.files[0]) {
                        const file = this.files[0];
                        fileName.textContent = file.name;
                    }
                });
            }

            // Initialize search functionality
            initSearch('specialties');
            initSearch('coverages');

            // Initialize selected options display
            updateSelectedDisplay('specialties');
            updateSelectedDisplay('coverages');
        });

        // Global function to toggle option selection
        function toggleOption(optionElement, selectId) {
            // Get the value of the clicked option
            const value = optionElement.getAttribute('data-value');
            const text = optionElement.querySelector('.option-text').textContent.trim();

            // Toggle selected class
            optionElement.classList.toggle('selected');

            // Get the select element
            const select = document.getElementById(selectId);

            // Find the corresponding option in the select element
            const selectOption = Array.from(select.options).find(opt => opt.value === value);

            if (selectOption) {
                // Toggle selection
                selectOption.selected = optionElement.classList.contains('selected');

                // Update the selected options display
                updateSelectedDisplay(selectId);
            }
        }

        function initSearch(selectId) {
            const searchBox = document.getElementById(`${selectId}-search`);
            const options = document.querySelectorAll(`#${selectId}-options .custom-multi-select-option`);

            searchBox.addEventListener('input', function() {
                const searchTerm = this.value.toLowerCase();

                options.forEach(option => {
                    const text = option.querySelector('.option-text').textContent.toLowerCase();
                    if (text.includes(searchTerm)) {
                        option.style.display = 'flex';
                    } else {
                        option.style.display = 'none';
                    }
                });
            });
        }

        function updateSelectedDisplay(selectId) {
            const select = document.getElementById(selectId);
            const selectedContainer = document.getElementById(`${selectId}-selected`);
            const options = document.querySelectorAll(`#${selectId}-options .custom-multi-select-option`);

            // Clear the selected container
            selectedContainer.innerHTML = '';

            // Get all selected options from the select element
            const selectedOptions = Array.from(select.selectedOptions);

            // Update the custom options UI to match the select element
            options.forEach(option => {
                const value = option.getAttribute('data-value');
                const isSelected = selectedOptions.some(opt => opt.value === value);

                if (isSelected) {
                    option.classList.add('selected');
                } else {
                    option.classList.remove('selected');
                }
            });

            // Create badges for selected options
            selectedOptions.forEach(option => {
                const badge = document.createElement('div');
                badge.className = 'selected-option-badge';
                badge.innerHTML = `
                    ${option.text}
                    <span class="remove-option" data-value="${option.value}" onclick="removeOption(this, '${selectId}')">×</span>
                `;

                selectedContainer.appendChild(badge);
            });
        }

        function removeOption(removeButton, selectId) {
            const value = removeButton.getAttribute('data-value');

            // Get the select element
            const select = document.getElementById(selectId);

            // Find and deselect the option in the select element
            const selectOption = Array.from(select.options).find(opt => opt.value === value);
            if (selectOption) {
                selectOption.selected = false;
            }

            // Find and deselect the option in the custom UI
            const customOption = document.querySelector(`#${selectId}-options .custom-multi-select-option[data-value="${value}"]`);
            if (customOption) {
                customOption.classList.remove('selected');
            }

            // Update the selected options display
            updateSelectedDisplay(selectId);
        }
    </script>
</layout:page>