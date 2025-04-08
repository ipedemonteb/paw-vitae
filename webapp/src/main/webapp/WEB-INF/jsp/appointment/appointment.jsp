<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<%@ taglib prefix="comp" tagdir="/WEB-INF/tags/components" %>

<layout:page title="appointment.page.title">
    <div class="card">
        <div class="card-header">
            <h1 class="card-title"><spring:message code="appointment.title" /></h1>
            <p class="card-subtitle"><spring:message code="appointment.subtitle" /></p>
        </div>

        <div class="card-body">
            <!-- Display doctor information if available -->
            <c:if test="${not empty doctor}">
                <comp:doctor-info doctor="${doctor}" />
            </c:if>

            <form:form modelAttribute="appointmentForm" method="post" action="${pageContext.request.contextPath}/appointment">
                <!-- Hidden field for doctor ID -->
                <form:hidden path="doctorId" />
                <form:hidden path="specialtyId" />
                <form:hidden path="clientId" />

                <div class="form-row">
                    <comp:form-group path="name" label="appointment.form.firstName" />
                    <comp:form-group path="lastName" label="appointment.form.lastName" />
                </div>

                <div class="form-row">
                    <comp:form-group path="email" label="appointment.form.email" type="email" />
                    <comp:form-group path="phone" label="appointment.form.phone" />
                </div>

                <div class="form-group">
                    <label for="specialty"><spring:message code="appointment.form.specialty" /></label>
                    <input type="text" id="specialty" name="specialty" class="form-control bg-light"
                           value="<spring:message code='${specialty.key}' />" readonly />
                </div>

                <div class="form-group">
                    <label for="coverageId"><spring:message code="appointment.form.coverage" /></label>
                    <form:select path="coverageId" id="coverageId" class="form-control">
                        <option value=""><spring:message code="appointment.placeholder.coverage" /></option>
                        <c:forEach var="coverage" items="${coverages}">
                            <option value="${coverage.id}">${coverage.name}</option>
                        </c:forEach>
                    </form:select>
                    <form:errors path="coverageId" cssClass="error-message" />
                </div>

                <div class="form-group">
                    <label for="appointmentDate"><spring:message code="appointment.form.date"/></label>
                    <input type="date" id="appointmentDate" name="appointmentDate" class="form-control" min="${today}" />
                </div>

                <div class="form-group">
                    <label for="appointmentHour"><spring:message code="appointment.form.time"/></label>
                    <select id="appointmentHour" name="appointmentHour" class="form-control" disabled>
                        <c:forEach var="hour" begin="8" end="18">
                            <c:if test="${!bookedHours.contains(hour)}">
                                <option value="${hour}">${hour}:00</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </div>

                <comp:form-group path="reason" label="appointment.form.reason" type="textarea" />

                <div class="form-group">
                    <button type="submit" class="btn btn-primary btn-block">
                        <spring:message code="appointment.form.submit" />
                    </button>
                </div>
            </form:form>
        </div>
    </div>

    <script>

        document.addEventListener('DOMContentLoaded', function() {
            const dateInput = document.getElementById('appointmentDate');
            const hourSelect = document.getElementById('appointmentHour');
            const doctorId = document.getElementById('doctorId').value;

            // Set minimum date to today
            const today = new Date();
            const yyyy = today.getFullYear();
            const mm = String(today.getMonth() + 1).padStart(2, '0');
            const dd = String(today.getDate()).padStart(2, '0');
            dateInput.min = yyyy + `-`+ mm + `-` + dd;

            dateInput.addEventListener('change', function() {
                const selectedDate = this.value;

                hourSelect.disabled = !selectedDate;

                console.log("I HAVE CHANGED");
                if (!selectedDate) {
                    console.log("WHY GOD WHY");
                    return;
                }

                fetch(`${pageContext.request.contextPath}/appointment/available-hours?doctorId=${doctor.id}&date=` + selectedDate)
                    .then(response => {
                        if (!response.ok) {
                            throw new Error(`HTTP error! status: ` + response.status);
                        }
                        return response.json();
                    })
                    .then(data => {
                        console.log("Full response:", data);  // Log the entire JSON response
                        // Clear current options
                        hourSelect.innerHTML = '';

                        // Add available hours (8 AM to 6 PM, excluding booked hours)
                        for (let hour = 8; hour <= 18; hour++) {
                            if (!data.bookedHours.includes(hour.toString())) {
                                const option = document.createElement('option');
                                option.value = hour;
                                option.textContent = hour + ':00';
                                hourSelect.appendChild(option);
                            }
                        }

                        // Show message if no hours available
                        if (hourSelect.options.length === 0) {
                            const option = document.createElement('option');
                            option.value = '';
                            option.textContent = 'No available hours';
                            option.disabled = true;
                            option.selected = true;
                            hourSelect.appendChild(option);
                        }
                    })
                    .catch(error => console.error('Error fetching available hours:', error));

            });
        });
    </script>
</layout:page>