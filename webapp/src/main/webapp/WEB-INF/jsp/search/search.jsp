<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags/layouts" %>

<c:set var="specialtyName">
  <spring:message code="${specialty.key}"/>
</c:set>

<link rel="stylesheet" href="<c:url value='/css/search.css' />" />
<link rel="stylesheet" href="<c:url value='/css/components/forms.css' />" />

<t:page title="search.page.title">
  <div class="card">
    <div class="card-header">
      <h1 class="card-title"><spring:message code="search.title" /></h1>
      <p class="card-subtitle"><spring:message code="search.doctors.specialty" arguments="${specialtyName}"/></p>
    </div>

    <div class="card-body">
      <!-- Specialty Selector -->
      <div class="specialty-selector">
        <label for="specialtySelect" class="specialty-label-search"><spring:message code="search.change.specialty" />:</label>
        <div class="specialty-select-wrapper">
          <select id="specialtySelect" class="specialty-select-search" onchange="changeSpecialty(this.value)">
            <c:forEach var="spec" items="${allSpecialties}">
              <option value="<c:out value='${spec.id}'/>" ${spec.id == specialty.id ? 'selected' : ''}>
                <spring:message code="${spec.key}" />
              </option>
            </c:forEach>
          </select>
        </div>
      </div>

      <c:choose>
        <c:when test="${empty doctors}">
          <div class="empty-results">
            <div class="empty-icon">&#x1F614;</div>
            <p><spring:message code="search.no.doctors.available" /></p>
          </div>
        </c:when>
        <c:otherwise>
          <div class="doctors-grid">
            <c:forEach var="doctor" items="${paginatedDoctors}" varStatus="status">
              <div class="doctor-card">
                <div class="doctor-card-header">
                  <div class="doctor-image-search">
                    <img src="<c:url value='/doctor/${doctor.id}/image'/>" alt="<c:out value='${doctor.name} ${doctor.lastName}'/>" class="doctor-avatar-search">
                  </div>
                  <div class="doctor-info-search">
                    <h3 class="doctor-name-search" title="<c:out value='${doctor.name} ${doctor.lastName}'/>">
                      <c:out value='${doctor.name} ${doctor.lastName}'/>
                    </h3>
                  </div>
                </div>

                <div class="doctor-card-body">
                  <div class="doctor-contact">
                    <div class="contact-item">
                      <span class="contact-icon">&#9993;</span>
                      <span class="contact-text"><c:out value='${doctor.email}'/></span>
                    </div>
                    <div class="contact-item">
                      <span class="contact-icon">&#9742;</span>
                      <span class="contact-text"><c:out value='${doctor.phone}'/></span>
                    </div>
                  </div>
                </div>

                <div class="doctor-card-footer">
                  <!-- Date Picker for Available Hours -->
                  <div class="availability-section" id="availability-section-${doctor.id}">
                    <div class="availability-header">
                      <h4><spring:message code="search.available.hours" /></h4>
                    </div>

                    <!-- Date Picker -->
                    <div class="date-picker-container-search compact">
                      <input type="text" id="datePickerInput-${doctor.id}" class="form-control date-picker-input"
                             placeholder="<spring:message code="appointment.placeholder.selectDate"/>" readonly>

                      <!-- Custom Calendar -->
                      <div id="datePickerCalendar-${doctor.id}" class="date-picker-calendar">
                        <div class="date-picker-header">
                          <button type="button" id="prevMonthBtn-${doctor.id}" class="date-picker-nav">&lsaquo;</button>
                          <div id="currentMonthYear-${doctor.id}" class="date-picker-month-year"></div>
                          <button type="button" id="nextMonthBtn-${doctor.id}" class="date-picker-nav">&rsaquo;</button>
                        </div>
                        <div class="date-picker-weekdays" id="calendarWeekdays-${doctor.id}"></div>
                        <div class="date-picker-days" id="calendarDays-${doctor.id}"></div>
                      </div>
                    </div>

                    <!-- Time Slots Container -->
                    <div id="timeSlotsContainer-${doctor.id}" class="time-slots-container-search" style="display: none;">
                      <label><spring:message code="appointment.form.availableTimes"/></label>
                      <div id="timeSlots-${doctor.id}" class="time-slots-grid-search"></div>
                    </div>
                  </div>

                  <a href="<c:url value='/appointment?doctorId=${doctor.id}&specialtyId=${specialty.id}'/>" class="btn-appointment">
                    <spring:message code="search.button.schedule" />
                  </a>
                </div>
              </div>
            </c:forEach>
          </div>

          <!-- Pagination -->
          <c:if test="${totalPages > 1}">
            <div class="pagination">
              <c:if test="${currentPage > 1}">
                <a href="<c:url value='/search?specialty=${specialty.id}&page=${currentPage - 1}'/>" class="pagination-link prev">
                  &laquo; <spring:message code="pagination.previous" />
                </a>
              </c:if>

              <div class="pagination-info">
                <spring:message code="pagination.page" arguments="${currentPage},${totalPages}" />
              </div>

              <c:if test="${currentPage < totalPages}">
                <a href="<c:url value='/search?specialty=${specialty.id}&page=${currentPage + 1}'/>" class="pagination-link next">
                  <spring:message code="pagination.next" /> &raquo;
                </a>
              </c:if>
            </div>
          </c:if>
        </c:otherwise>
      </c:choose>
    </div>

    <div class="card-footer">
      <a href="<c:url value='/'/>" class="btn btn-outline">
        <spring:message code="search.link.back.home" />
      </a>
    </div>
  </div>

  <!-- Add message translations for JavaScript -->
  <script>
    // Create a messages object to be used by the JavaScript
    window.appointmentMessages = {
      months: [
        '<spring:message code="calendar.month.january" />',
        '<spring:message code="calendar.month.february" />',
        '<spring:message code="calendar.month.march" />',
        '<spring:message code="calendar.month.april" />',
        '<spring:message code="calendar.month.may" />',
        '<spring:message code="calendar.month.june" />',
        '<spring:message code="calendar.month.july" />',
        '<spring:message code="calendar.month.august" />',
        '<spring:message code="calendar.month.september" />',
        '<spring:message code="calendar.month.october" />',
        '<spring:message code="calendar.month.november" />',
        '<spring:message code="calendar.month.december" />'
      ],
      weekdays: [
        '<spring:message code="calendar.day.sunday" />',
        '<spring:message code="calendar.day.monday" />',
        '<spring:message code="calendar.day.tuesday" />',
        '<spring:message code="calendar.day.wednesday" />',
        '<spring:message code="calendar.day.thursday" />',
        '<spring:message code="calendar.day.friday" />',
        '<spring:message code="calendar.day.saturday" />'
      ],
      weekdaysShort: [
        '<spring:message code="calendar.day.short.sun" />',
        '<spring:message code="calendar.day.short.mon" />',
        '<spring:message code="calendar.day.short.tue" />',
        '<spring:message code="calendar.day.short.wed" />',
        '<spring:message code="calendar.day.short.thu" />',
        '<spring:message code="calendar.day.short.fri" />',
        '<spring:message code="calendar.day.short.sat" />'
      ],
      appointmentAt: '<spring:message code="appointment.at" />',
      noAvailableSlots: '<spring:message code="appointment.noAvailableHours" />'
    };
    contextPath = "${pageContext.request.contextPath}";

    function changeSpecialty(specialtyId) {
      window.location.href = "${pageContext.request.contextPath}/search?specialty=" + specialtyId;
    }

    // Create a global object to store each doctor's availability slots
    window.doctorAvailabilitySlots = {};

    <c:forEach var="doctor" items="${paginatedDoctors}">
    // Initialize availability slots array for this doctor
    window.doctorAvailabilitySlots['${doctor.id}'] = [
      <c:forEach var="slot" items="${doctor.availabilitySlots}" varStatus="status">
      {
        dayOfWeek: ${slot.dayOfWeek},
        startTime: ${slot.startTime.hour},
        endTime: ${slot.endTime.hour},
        slots: ${slot.endTime.hour - slot.startTime.hour + 1}
      }<c:if test="${!status.last}">,</c:if>
      </c:forEach>
    ];
    </c:forEach>

    const argDate = new Date().toLocaleString("en-US", {
      timeZone: "America/Argentina/Buenos_Aires",
    });
    const today = new Date(argDate);

    const futureAppointmentsMap = [
        <c:forEach var="entry" items="${doctors}" varStatus="status">
        {
            doctorId: '${entry.id}',
            appointments: [
            <c:forEach var="appointment" items="${entry.appointments}" varStatus="status2">
            {
                date: '${appointment.date}',
                hour: ${appointment.date.hour}
            }<c:if test="${!status2.last}">,</c:if>
            </c:forEach>
            ].filter(
                (appointment) => {
                    const appointmentDate = new Date(appointment.date);
                    return appointmentDate > today || (appointmentDate === today && appointment.hour > today.getHours());
                }
            )
        }<c:if test="${!status.last}">,</c:if>
        </c:forEach>
    ];

    const doctorsAvailability = Object.entries(futureAppointmentsMap).map(([doctorId, appointments]) => {
      const groupedByDate = appointments.appointments.reduce((acc, appointment) => {
        const date = new Date(appointment.date).toISOString().split('T')[0]; // Extract date (YYYY-MM-DD)
        const hour = appointment.hour;


        if (!acc[date]) {
          acc[date] = [];
        }
        acc[date].push(hour);

        return acc;
      }, {});

      return {
        id: appointments.doctorId,
        info: Object.entries(groupedByDate).map(([date, hours]) => ({
          date,
          hours
        }))
      };
    });

    console.log(doctorsAvailability)

  </script>

  <!-- Include the modified date-time-picker for search page -->
  <script src="<c:url value='/js/search-date-time-picker.js'/>"></script>
</t:page>