<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="specialtyName">
  <spring:message code="${specialty.key}"/>
</c:set>

<!DOCTYPE html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title><spring:message code="search.page.title" /></title>
  <link rel="stylesheet" href="<c:url value='/css/search.css' />" />
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
<!-- Include the header -->
<jsp:include page="/WEB-INF/jsp/components/header.jsp" />

<!-- Main Content -->
<main class="main-content">
  <div class="container">
    <div class="search-container">
      <div class="search-header">
        <h1 class="search-title"><spring:message code="search.title" /></h1>
        <p class="search-subtitle"><spring:message code="search.doctors.specialty" arguments="${specialtyName}"/></p>
      </div>

      <div class="search-filters">
        <!-- Specialty Selector -->
        <div class="specialty-selector">
          <label for="specialtySelect" class="specialty-label"><spring:message code="search.change.specialty" />:</label>
          <div class="specialty-select-container">
            <select id="specialtySelect" class="specialty-select" onchange="changeSpecialty(this.value)">
              <c:forEach var="spec" items="${allSpecialties}">
                <option value="<c:out value='${spec.id}'/>" ${spec.id == specialty.id ? 'selected' : ''}>
                  <spring:message code="${spec.key}" />
                </option>
              </c:forEach>
            </select>
          </div>
        </div>

        <div class="view-toggle">
          <button class="view-toggle-btn active" data-view="list">
            <i class="fas fa-list"></i> <span><spring:message code="search.view.list" /></span>
          </button>
          <button class="view-toggle-btn" data-view="grid">
            <i class="fas fa-th-large"></i> <span><spring:message code="search.view.grid" /></span>
          </button>
        </div>
      </div>

      <div class="search-body">
        <c:choose>
          <c:when test="${empty doctors}">
            <div class="empty-results">
              <div class="empty-icon"><i class="fas fa-user-md fa-3x"></i></div>
              <p><spring:message code="search.no.doctors.available" /></p>
            </div>
          </c:when>
          <c:otherwise>
            <div class="doctors-list-view">
              <c:forEach var="doctor" items="${paginatedDoctors}" varStatus="status">
                <div class="doctor-card">
                  <div class="doctor-info">
                    <div class="doctor-avatar-container">
                      <img src="<c:url value='/doctor/${doctor.id}/image'/>" alt="<c:out value='${doctor.name} ${doctor.lastName}'/>" class="doctor-avatar">
                    </div>
                    <div class="doctor-details">
                      <h3 class="doctor-name" title="<c:out value='${doctor.name} ${doctor.lastName}'/>">
                        <c:out value='${doctor.name} ${doctor.lastName}'/>
                      </h3>
                      <div class="doctor-specialty">
                        <i class="fas fa-stethoscope"></i> <c:out value="${specialtyName}"/>
                      </div>
                      <div class="doctor-contact">
                        <div class="contact-item">
                          <i class="fas fa-envelope"></i> <c:out value='${doctor.email}'/>
                        </div>
                        <div class="contact-item">
                          <i class="fas fa-phone"></i> <c:out value='${doctor.phone}'/>
                        </div>
                      </div>
                    </div>
                  </div>

                  <div class="doctor-schedule">
                    <div class="schedule-header">
                      <button type="button" class="nav-btn prev-week" id="prevWeek-${doctor.id}">
                        <i class="fas fa-chevron-left"></i>
                      </button>
                      <div class="current-week" id="currentWeek-${doctor.id}"></div>
                      <button type="button" class="nav-btn next-week" id="nextWeek-${doctor.id}">
                        <i class="fas fa-chevron-right"></i>
                      </button>
                    </div>

                    <div class="weekly-schedule" id="weeklySchedule-${doctor.id}">
                      <!-- Weekly schedule will be populated by JavaScript -->
                    </div>

                    <div class="no-slots-message" id="noSlots-${doctor.id}" style="display: none;">
                      <i class="fas fa-calendar-times"></i>
                      <p><spring:message code="search.no.slots.available" /></p>
                      <span class="next-available" id="nextAvailable-${doctor.id}"></span>
                    </div>
                  </div>

                  <div class="doctor-actions">
                    <a href="<c:url value='/appointment?doctorId=${doctor.id}&specialtyId=${specialty.id}'/>" class="btn-appointment">
                      <i class="fas fa-calendar-check"></i> <spring:message code="search.button.schedule" />
                    </a>
                    <button class="btn-view-profile" onclick="viewDoctorProfile('${doctor.id}')">
                      <i class="fas fa-user-md"></i> <spring:message code="search.button.view.profile" />
                    </button>
                  </div>
                </div>
              </c:forEach>
            </div>

            <!-- Pagination -->
            <c:if test="${totalPages > 1}">
              <div class="pagination">
                <c:if test="${currentPage > 1}">
                  <a href="<c:url value='/search?specialty=${specialty.id}&page=${currentPage - 1}'/>" class="pagination-link prev">
                    <i class="fas fa-chevron-left"></i> <spring:message code="pagination.previous" />
                  </a>
                </c:if>

                <div class="pagination-numbers">
                  <c:forEach begin="1" end="${totalPages}" var="pageNum">
                    <c:choose>
                      <c:when test="${pageNum == currentPage}">
                        <span class="pagination-number active">${pageNum}</span>
                      </c:when>
                      <c:otherwise>
                        <a href="<c:url value='/search?specialty=${specialty.id}&page=${pageNum}'/>" class="pagination-number">${pageNum}</a>
                      </c:otherwise>
                    </c:choose>
                  </c:forEach>
                </div>

                <c:if test="${currentPage < totalPages}">
                  <a href="<c:url value='/search?specialty=${specialty.id}&page=${currentPage + 1}'/>" class="pagination-link next">
                    <spring:message code="pagination.next" /> <i class="fas fa-chevron-right"></i>
                  </a>
                </c:if>
              </div>
            </c:if>
          </c:otherwise>
        </c:choose>
      </div>

      <div class="search-footer">
        <a href="<c:url value='/'/>" class="btn btn-outline">
          <i class="fas fa-home"></i> <spring:message code="search.link.back.home" />
        </a>
      </div>
    </div>
  </div>
</main>

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
    noAvailableSlots: '<spring:message code="appointment.noAvailableHours" />',
    nextAvailable: '<spring:message code="search.next.available" />',
    seeMoreSchedules: '<spring:message code="search.see.more.schedules" />',
    seeLessSchedules: '<spring:message code="search.see.less.schedules" />'
  };

  contextPath = "${pageContext.request.contextPath}";

  function changeSpecialty(specialtyId) {
    window.location.href = "${pageContext.request.contextPath}/search?specialty=" + specialtyId;
  }

  function viewDoctorProfile(doctorId) {
    window.location.href = "${pageContext.request.contextPath}/doctor/" + doctorId;
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

  const fixedHeader = document.querySelector(".main-header");
  const mainContent = document.querySelector("main");

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

  // View toggle functionality
  document.addEventListener('DOMContentLoaded', function() {
    const viewToggleBtns = document.querySelectorAll('.view-toggle-btn');
    const doctorsList = document.querySelector('.doctors-list-view');

    viewToggleBtns.forEach(btn => {
      btn.addEventListener('click', function() {
        const view = this.getAttribute('data-view');

        // Remove active class from all buttons
        viewToggleBtns.forEach(b => b.classList.remove('active'));

        // Add active class to clicked button
        this.classList.add('active');

        // Toggle view class on doctors container
        if (view === 'grid') {
          doctorsList.classList.add('grid-view');
        } else {
          doctorsList.classList.remove('grid-view');
        }
      });
    });
  });
</script>

<!-- Include the weekly schedule script -->
<script src="<c:url value='/js/search-date-time-picker.js'/>"></script>
</body>
