<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags/layouts" %>

<c:set var="specialtyName">
  <spring:message code="${specialty.key}"/>
</c:set>

<link rel="stylesheet" href="<c:url value='/css/search.css' />" />

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
<%--                    <div class="doctor-specialties-search">--%>
<%--                      <c:forEach var="doctorSpecialty" items="${doctor.specialtyList}" varStatus="specStatus">--%>
<%--                        <c:if test="${specStatus.index < 2}">--%>
<%--                          <span class="doctor-specialty-tag-search" title="<spring:message code='${doctorSpecialty.key}'/>">--%>
<%--                            <spring:message code="${doctorSpecialty.key}" />--%>
<%--                          </span>--%>
<%--                        </c:if>--%>
<%--                      </c:forEach>--%>
<%--                      <c:if test="${doctor.specialtyList.size() > 2}">--%>
<%--                        <span class="doctor-specialty-tag-search more-tag" title="<spring:message code='search.more.specialties' arguments='${doctor.specialtyList.size() - 2}'/>">--%>
<%--                          +<c:out value='${doctor.specialtyList.size() - 2}'/>--%>
<%--                        </span>--%>
<%--                      </c:if>--%>
<%--                    </div>--%>
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
                  <div class="availability-calendar">
                    <div class="calendar-header">
                      <div class="calendar-title"><spring:message code="search.next.available" /></div>
                    </div>
                    <div class="calendar-days">
                      <c:forEach var="i" begin="0" end="6">
                        <div class="calendar-day">
                          <div class="day-name"><spring:message code="calendar.day.short.${i == 0 ? 'sun' : i == 1 ? 'mon' : i == 2 ? 'tue' : i == 3 ? 'wed' : i == 4 ? 'thu' : i == 5 ? 'fri' : 'sat'}" /></div>
                          <div class="day-availability ${i == 0 || i == 6 ? 'unavailable' : 'available'}">
                              ${i == 0 || i == 6 ? '—' : '✓'}
                          </div>
                          <c:if test="${i != 0 && i != 6}">
                            <div class="time-slot">9:00</div>
                          </c:if>
                        </div>
                      </c:forEach>
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

  <script>
    function changeSpecialty(specialtyId) {
      window.location.href = "${pageContext.request.contextPath}/search?specialty=" + specialtyId;
    }
  </script>
</t:page>