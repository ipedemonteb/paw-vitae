<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<%--<c:set var="specialtyName">--%>
<%--  <c:choose>--%>
<%--    <c:when test="${empty specialty}">--%>
<%--      <spring:message code="search.all.specialties"/>--%>
<%--    </c:when>--%>
<%--    <c:otherwise>--%>
<%--      <spring:message code="${specialty.key}"/>--%>
<%--    </c:otherwise>--%>
<%--  </c:choose>--%>
<%--</c:set>--%>

<spring:message code="search.all.specialties" var="allSepcs"/>
<spring:message code="search.coverage.all" var="allCovs"/>
<c:set var="specialtyName">
  <c:forEach items="${allSpecialties}" var="s">
    <c:if test="${s.id == specialtyId}">
      <spring:message code="${s.key}"/>
    </c:if>
  </c:forEach>
</c:set>
<c:set var="coverageName">
  <c:forEach items="${coverages}" var="c">
    <c:if test="${not empty coverageId && coverageId == c.id}">
      <c:out value="${c.name}"/>
    </c:if>
  </c:forEach>
</c:set>
<c:if test="${empty coverageName}">
    <c:set var="coverageName">
        <c:out value="${allCovs}"/>
    </c:set>
</c:if>
<c:if test="${empty specialtyName}">
  <c:set var="specialtyName">
    <c:out value="${allSepcs}"/>
  </c:set>
</c:if>



<!DOCTYPE html>
<head>
  <meta charset="UTF-8">
  <link rel="icon" type="image/png" href="https://upload.wikimedia.org/wikipedia/commons/thumb/a/aa/V-logo.svg/2048px-V-logo.svg.png" />
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
    <!-- Search Header -->
    <div class="search-header">
      <div class="search-header-content">
        <h1 class="search-title"><spring:message code="search.title" /></h1>
        <p class="search-subtitle">
          <spring:message code="search.doctors.specialty" arguments="${fn:escapeXml(specialtyName)}"/>
        </p>
      </div>

      <!-- Search Bar -->
            <div class="search-bar-container">
              <div class="search-bar">
                <i class="fas fa-search search-icon"></i>
                <input type="text" id="doctorSearch" placeholder="<spring:message code="search.placeholder.doctor" />" class="search-input">
              </div>
            </div>
    </div>

    <!-- Filters Section -->
    <div class="filters-section">
      <div class="filters-container">
        <!-- Specialty Filter -->
        <div class="filter-group">
          <label for="specialtySelect" class="filter-label"><i class="fas fa-stethoscope"></i> <spring:message code="search.specialty" /></label>
          <div class="select-container">
            <select id="specialtySelect" class="filter-select">
              <option value="0" ${specialtyName.equals(allSepcs) ? 'selected' : ''}><spring:message code="search.all.specialties" /></option>
              <c:forEach var="spec" items="${allSpecialties}">
                <option value="<c:out value='${spec.id}'/>" ${not empty specialtyId && specialtyId == spec.id ? 'selected' : ''}>
                  <spring:message code="${spec.key}" />
                </option>
              </c:forEach>
            </select>
          </div>
        </div>

        <!-- Coverage Filter -->
        <div class="filter-group">
          <label for="coverageSelect" class="filter-label"><i class="fas fa-shield-alt"></i> <spring:message code="search.coverage" /></label>
          <div class="select-container">
            <select id="coverageSelect" class="filter-select">
              <option value="0"><spring:message code="search.coverage.all" /></option>
              <c:forEach var="coverage" items="${coverages}">
                <option value="<c:out value='${coverage.id}'/>" ${coverageId == coverage.id ? 'selected' : ''}>
                  <c:out value="${coverage.name}" />
                </option>
              </c:forEach>
            </select>
          </div>
        </div>

        <!-- Sort Filter -->
        <div class="filter-group">
          <label for="sortSelect" class="filter-label"><i class="fas fa-sort"></i> <spring:message code="search.sort" /></label>
          <div class="select-container">
            <select id="sortSelect" class="filter-select">
              <optgroup label="<spring:message code="search.sort.by.name" />">
                <option value="name_asc" ${param.orderBy == 'name' && param.direction == 'asc' ? 'selected' : ''}>
                  <spring:message code="search.sort.name_asc" />
                </option>
                <option value="name_desc" ${param.orderBy == 'name' && param.direction == 'desc' ? 'selected' : ''}>
                  <spring:message code="search.sort.name_desc" />
                </option>
              </optgroup>
              <optgroup label="<spring:message code="search.sort.by.last_name" />">
                <option value="last_name_asc" ${param.orderBy == 'last_name' && param.direction == 'asc' ? 'selected' : ''}>
                  <spring:message code="search.sort.last_name_asc" />
                </option>
                <option value="last_name_desc" ${param.orderBy == 'last_name' && param.direction == 'desc' ? 'selected' : ''}>
                  <spring:message code="search.sort.last_name_desc" />
                </option>
              </optgroup>
              <optgroup label="<spring:message code="search.sort.by.rating" />">
                <option value="rating_asc" ${param.orderBy == 'rating' && param.direction == 'asc' ? 'selected' : ''}>
                  <spring:message code="search.sort.rating_asc" />
                </option>
                <option value="rating_desc" ${param.orderBy == 'rating' && param.direction == 'desc' ? 'selected' : ''}>
                  <spring:message code="search.sort.rating_desc" />
                </option>
              </optgroup>
              <optgroup label="<spring:message code="search.sort.by.email" />">
                <option value="email_asc" ${param.orderBy == 'email' && param.direction == 'asc' ? 'selected' : ''}>
                  <spring:message code="search.sort.email_asc" />
                </option>
                <option value="email_desc" ${param.orderBy == 'email' && param.direction == 'desc' ? 'selected' : ''}>
                  <spring:message code="search.sort.email_desc" />
                </option>
              </optgroup>
            </select>
          </div>
        </div>
      </div>

      <!-- Interactive Filter Tags -->
      <div class="filter-tags">
        <div class="filter-tags-label">
          <i class="fas fa-filter"></i> <spring:message code="search.active.filters" />
        </div>
        <div class="filter-tags-container">
          <%--          <c:if test="${not empty param.orderBy}">--%>
          <div class="filter-tag">
            <i class="fas fa-sort"></i>
            <span>
                <c:choose>
                  <c:when test="${param.orderBy == 'name' || empty param.orderBy}">
                    <spring:message code="search.sort.by.name" />
                  </c:when>
                  <c:when test="${param.orderBy == 'last_name'}">
                    <spring:message code="search.sort.by.last_name" />
                  </c:when>
                  <c:when test="${param.orderBy == 'rating'}">
                    <spring:message code="search.sort.by.rating" />
                  </c:when>
                  <c:when test="${param.orderBy == 'email'}">
                    <spring:message code="search.sort.by.email" />
                  </c:when>
                </c:choose>
                <c:if test="${param.direction == 'asc' || empty param.direction}">
                  <i class="fas fa-arrow-up"></i>
                </c:if>
                <c:if test="${param.direction == 'desc'}">
                  <i class="fas fa-arrow-down"></i>
                </c:if>
              </span>
          </div>
          <%--          </c:if>--%>
          <c:if test="${not empty specialtyId && specialtyId  != 0 && specialtyName != allSepcs}">
            <div class="filter-tag">
              <i class="fas fa-stethoscope"></i>
              <span>
                <c:forEach var="spec" items="${allSpecialties}">
                  <c:if test="${specialtyId == spec.id}">
                    <spring:message code="${spec.key}" />
                  </c:if>
                </c:forEach>
              </span>
              <button class="filter-tag-remove" onclick="clearSpecialtyFilter()">×</button>
            </div>
          </c:if>
          <c:if test="${not empty coverageId && coverageId  != 0 && coverageName != allCovs}">
            <div class="filter-tag">
              <i class="fas fa-shield-alt"></i>
              <span>
                <c:forEach var="coverage" items="${coverages}">
                  <c:if test="${coverageId == coverage.id}">
                    <c:out value="${coverage.name}" />
                  </c:if>
                </c:forEach>
              </span>
              <button class="filter-tag-remove" onclick="clearCoverageFilter()">×</button>
            </div>
          </c:if>
          <c:if test="${not empty param.weekdays}">
            <div class="filter-tag">
              <i class="fas fa-calendar-week"></i>
              <span><spring:message code="search.filter.weekdays" /></span>
              <button class="filter-tag-remove" onclick="clearWeekdaysFilter()">×</button>
            </div>
          </c:if>
        </div>
      </div>

      <!-- Advanced Filters -->
      <div class="advanced-filters">
        <div class="filter-section">
          <h3 class="filter-section-title"><i class="fas fa-calendar-week"></i> <spring:message code="search.availability" /></h3>
          <div class="weekday-filters">
            <label class="weekday-label">
              <input type="checkbox" class="weekday-checkbox" value="0" ${param.weekdays.contains('0') ? 'checked' : ''}>
              <span class="weekday-text"><spring:message code="search.weekday.monday" /></span>
            </label>
            <label class="weekday-label">
              <input type="checkbox" class="weekday-checkbox" value="1" ${param.weekdays.contains('1') ? 'checked' : ''}>
              <span class="weekday-text"><spring:message code="search.weekday.tuesday" /></span>
            </label>
            <label class="weekday-label">
              <input type="checkbox" class="weekday-checkbox" value="2" ${param.weekdays.contains('2') ? 'checked' : ''}>
              <span class="weekday-text"><spring:message code="search.weekday.wednesday" /></span>
            </label>
            <label class="weekday-label">
              <input type="checkbox" class="weekday-checkbox" value="3" ${param.weekdays.contains('3') ? 'checked' : ''}>
              <span class="weekday-text"><spring:message code="search.weekday.thursday" /></span>
            </label>
            <label class="weekday-label">
              <input type="checkbox" class="weekday-checkbox" value="4" ${param.weekdays.contains('4') ? 'checked' : ''}>
              <span class="weekday-text"><spring:message code="search.weekday.friday" /></span>
            </label>
            <label class="weekday-label">
              <input type="checkbox" class="weekday-checkbox" value="5" ${param.weekdays.contains('5') ? 'checked' : ''}>
              <span class="weekday-text"><spring:message code="search.weekday.saturday" /></span>
            </label>
            <label class="weekday-label">
              <input type="checkbox" class="weekday-checkbox" value="6" ${param.weekdays.contains('6') ? 'checked' : ''}>
              <span class="weekday-text"><spring:message code="search.weekday.sunday" /></span>
            </label>
          </div>
        </div>

        <button id="applyFiltersBtn" class="btn btn-primary apply-filters-btn">
          <i class="fas fa-filter"></i> <spring:message code="search.button.apply_filters" />
        </button>
      </div>

      <!-- Quick Filters -->
      <%--      <div class="quick-filters">--%>
      <%--        <button class="quick-filter-btn active" data-filter="all">--%>
      <%--          <spring:message code="search.filter.all" />--%>
      <%--        </button>--%>
      <%--        <button class="quick-filter-btn" data-filter="top-rated">--%>
      <%--          <i class="fas fa-star"></i> <spring:message code="search.filter.top_rated" />--%>
      <%--        </button>--%>
      <%--        <button class="quick-filter-btn" data-filter="new">--%>
      <%--          <i class="fas fa-certificate"></i> <spring:message code="search.filter.new" />--%>
      <%--        </button>--%>
      <%--      </div>--%>
    </div>

    <!-- Results Section -->
    <div class="results-section">
      <div class="results-header">
        <div class="results-count">
          <c:choose>
            <c:when test="${not empty totalDoctors}">
              <span class="results-span"><spring:message code="search.results.count" arguments="${totalDoctors}" /></span>
            </c:when>
            <c:otherwise>
              <span class="results-span"><spring:message code="search.results.none" /></span>
            </c:otherwise>
          </c:choose>
          <!-- View Toggle -->
          <div class="view-toggle">
            <button class="view-toggle-btn ${not empty param.view ?( param.view == "grid" ? "active" : "") : "active"}" data-view="grid">
              <i class="fas fa-th-large"></i>
            </button>
            <button class="view-toggle-btn ${not empty param.view ? ( param.view == "list" ? "active" : "") : ""}" data-view="list">
              <i class="fas fa-list"></i>
            </button>
          </div>
        </div>

        <!-- Active Filters -->
        <%--        <div class="active-filters">--%>
        <%--          <c:if test="${not empty param.weekdays}">--%>
        <%--            <div class="active-filter">--%>
        <%--              <i class="fas fa-calendar-week"></i> <spring:message code="search.filter.weekdays" />--%>
        <%--              <button class="clear-filter" onclick="clearWeekdaysFilter()">×</button>--%>
        <%--            </div>--%>
        <%--          </c:if>--%>
        <%--          <c:if test="${coverageId != 0 && not empty coverageId}">--%>
        <%--            <div class="active-filter">--%>
        <%--              <i class="fas fa-shield-alt"></i> <spring:message code="search.filter.coverage" />--%>
        <%--              <button class="clear-filter" onclick="clearCoverageFilter()">×</button>--%>
        <%--            </div>--%>
        <%--          </c:if>--%>
        <%--          <c:if test="${specialtyId != 0 && not empty specialtyId}">--%>
        <%--            <div class="active-filter">--%>
        <%--              <i class="fas fa-stethoscope"></i> <spring:message code="search.filter.specialty" />--%>
        <%--              <button class="clear-filter" onclick="clearSpecialtyFilter()">×</button>--%>
        <%--            </div>--%>
        <%--          </c:if>--%>
        <%--        </div>--%>
      </div>

      <c:choose>
        <c:when test="${empty doctors}">
          <div class="empty-results">
            <div class="empty-icon"><i class="fas fa-user-md"></i></div>
            <h3><spring:message code="search.no.doctors.title" /></h3>
            <p><spring:message code="search.no.doctors.available" /></p>
            <a href="<c:url value='/'/>" class="btn btn-primary">
              <i class="fas fa-home"></i> <spring:message code="search.link.back.home" />
            </a>
          </div>
        </c:when>
        <c:otherwise>
          <div class="doctors-grid ${not empty param.view ?( param.view == "grid" ? "" : "list-view") : ""}">
            <c:forEach var="doctor" items="${doctors}" varStatus="status">
              <div class="doctor-card">
                <div class="doctor-card-header">
                  <div class="doctor-avatar">
                    <img src="<c:url value='/image/${empty doctor.imageId ? -1 : doctor.imageId}'/>" alt="<c:out value='${doctor.name} ${doctor.lastName}'/>" class="avatar-img">
                  </div>
                  <div class="doctor-rating">
                    <c:if test="${doctor.ratingCount > 0}">
                      <div class="stars">
                        <c:set var="fullStars" value="${doctor.rating.intValue()}" />
                        <c:set var="hasHalfStar" value="${doctor.rating - fullStars >= 0.5}" />
                        <c:set var="emptyStars" value="${5 - fullStars - (hasHalfStar ? 1 : 0)}" />

                        <!-- Render full stars -->
                        <c:forEach begin="1" end="${fullStars}" var="i">
                          <i class="fas fa-star"></i>
                        </c:forEach>

                        <!-- Render half star if applicable -->
                        <c:if test="${hasHalfStar}">
                          <i class="fas fa-star-half-alt"></i>
                        </c:if>

                        <!-- Render empty stars -->
                        <c:forEach begin="1" end="${emptyStars}" var="i">
                          <i class="far fa-star"></i>
                        </c:forEach>
                      </div>
                      <span class="rating-count"><fmt:formatNumber value="${doctor.rating}" type="number" maxFractionDigits="1" minFractionDigits="1" /></span>
                    </c:if>
                  </div>
                </div>

                <div class="doctor-card-body">
                  <h3 class="doctor-name">
                    <c:out value='${doctor.name} ${doctor.lastName}'/>
                  </h3>
                  <div class="doctor-specialty">
                    <i class="fas fa-stethoscope"></i>
                      <%--                    <c:choose>--%>
                      <%--                      <c:when test="${empty specialty}">--%>
                    <c:set var="specialtiesText" value="" />
                    <c:forEach var="doctorSpecialty" items="${doctor.specialtyList}" varStatus="specStatus">
                      <spring:message code="${doctorSpecialty.key}" var="specialtyName" />
                      <c:set var="specialtiesText" value="${specialtiesText}${not specStatus.first ? ', ' : ''}${specialtyName}" />
                    </c:forEach>
<%--                    <c:choose>--%>
<%--                      <c:when test="${fn:length(specialtiesText) > 30}">--%>
<%--                        <span title="${specialtiesText}"><c:out value="${fn:substring(specialtiesText, 0, 27)}..." /></span>--%>
<%--                      </c:when>--%>
<%--                      <c:otherwise>--%>
                        <c:out value="${specialtiesText}" />
<%--                      </c:otherwise>--%>
<%--                    </c:choose>--%>
                      <%--                      </c:when>--%>
                      <%--                      <c:otherwise>--%>
                      <%--                        <c:out value="${specialtyName}"/>--%>
                      <%--                      </c:otherwise>--%>
                      <%--                    </c:choose>--%>
                  </div>
                  <div class="doctor-info">
                    <div class="info-item">
                      <i class="fas fa-envelope"></i>
                      <span><c:out value='${doctor.email}'/></span>
                    </div>
                    <div class="info-item">
                      <i class="fas fa-phone"></i>
                      <span><c:out value='${doctor.phone}'/></span>
                    </div>
                    <div class="info-item">
                      <i class="fas fa-calendar-check"></i>
                      <span class="availability-badge available">
                        <spring:message code="search.available" />
                      </span>
                    </div>
                  </div>
                </div>

                <div class="doctor-card-footer">
                  <c:set var="defaultSpecId" value="${doctor.specialtyList.stream().findFirst().get().id}"/>
                  <a href="<c:url value='/appointment?doctorId=${doctor.id}'/>" class="btn btn-primary">
                    <i class="fas fa-calendar-check"></i> <spring:message code="search.button.schedule" />
                  </a>
                    <%--                  <button class="btn btn-secondary" onclick="viewDoctorProfile('${doctor.id}')">--%>
                    <%--                    <i class="fas fa-user-md"></i> <spring:message code="search.button.view.profile" />--%>
                    <%--                  </button>--%>
                </div>
              </div>
            </c:forEach>
          </div>

          <!-- Pagination -->
          <c:if test="${totalPages > 1}">
            <div class="pagination">

              <!-- prev link -->
              <c:if test="${currentPage > 1}">
                <c:url var="prevUrl" value="/search">
                  <c:param name="page" value="${currentPage - 1}" />
                  <c:if test="${not empty specialtyId and specialtyId != 0}">
                    <c:param name="specialty" value="${specialtyId}" />
                  </c:if>
                  <c:if test="${not empty coverageId and coverageId != 0}">
                    <c:param name="coverage" value="${coverageId}" />
                  </c:if>
                  <!-- here’s the trick: emit one param per selected weekday -->
                  <c:forEach var="wd" items="${paramValues.weekdays}">
                    <c:param name="weekdays" value="${wd}" />
                  </c:forEach>
                  <c:if test="${not empty param.orderBy}">
                    <c:param name="orderBy"  value="${param.orderBy}" />
                  </c:if>
                  <c:if test="${not empty param.direction}">
                    <c:param name="direction" value="${param.direction}" />
                  </c:if>
                  <c:if test="${not empty param.view}">
                    <c:param name="view" value="${param.view}" />
                  </c:if>
                </c:url>
                <a href="${prevUrl}" class="pagination-btn prev">
                  <i class="fas fa-chevron-left"></i>
                </a>
              </c:if>

              <!-- page‐number links -->
              <div class="pagination-numbers">
                <c:forEach begin="1" end="${totalPages}" var="pageNum">
                  <c:choose>
                    <c:when test="${pageNum == currentPage}">
                      <span class="pagination-number active">${pageNum}</span>
                    </c:when>
                    <c:otherwise>
                      <c:url var="pageUrl" value="/search">
                        <c:param name="page" value="${pageNum}" />
                        <c:if test="${not empty specialtyId and specialtyId != 0}">
                          <c:param name="specialty" value="${specialtyId}" />
                        </c:if>
                        <c:if test="${not empty coverageId and coverageId != 0}">
                          <c:param name="coverage" value="${coverageId}" />
                        </c:if>
                        <c:forEach var="wd" items="${paramValues.weekdays}">
                          <c:param name="weekdays" value="${wd}" />
                        </c:forEach>
                        <c:if test="${not empty param.orderBy}">
                          <c:param name="orderBy"  value="${param.orderBy}" />
                        </c:if>
                        <c:if test="${not empty param.direction}">
                          <c:param name="direction" value="${param.direction}" />
                        </c:if>
                        <c:if test="${not empty param.view}">
                          <c:param name="view" value="${param.view}" />
                        </c:if>
                      </c:url>
                      <a href="${pageUrl}" class="pagination-number">${pageNum}</a>
                    </c:otherwise>
                  </c:choose>
                </c:forEach>
              </div>

              <!-- next link -->
              <c:if test="${currentPage < totalPages}">
                <c:url var="nextUrl" value="/search">
                  <c:param name="page" value="${currentPage + 1}" />
                  <c:if test="${not empty specialtyId and specialtyId != 0}">
                    <c:param name="specialty" value="${specialtyId}" />
                  </c:if>
                  <c:if test="${not empty coverageId and coverageId != 0}">
                    <c:param name="coverage" value="${coverageId}" />
                  </c:if>
                  <c:forEach var="wd" items="${paramValues.weekdays}">
                    <c:param name="weekdays" value="${wd}" />
                  </c:forEach>
                  <c:if test="${not empty param.orderBy}">
                    <c:param name="orderBy"  value="${param.orderBy}" />
                  </c:if>
                  <c:if test="${not empty param.direction}">
                    <c:param name="direction" value="${param.direction}" />
                  </c:if>
                  <c:if test="${not empty param.view}">
                    <c:param name="view" value="${param.view}" />
                  </c:if>
                </c:url>
                <a href="${nextUrl}" class="pagination-btn next">
                  <i class="fas fa-chevron-right"></i>
                </a>
              </c:if>

            </div>
          </c:if>

        </c:otherwise>
      </c:choose>
    </div>
  </div>
</main>

<!-- Include the search script -->
<script src="<c:url value='/js/search.js'/>"></script>

<script>
  contextPath = "${pageContext.request.contextPath}";

  <%--function viewDoctorProfile(doctorId) {--%>
  <%--  window.location.href = "${pageContext.request.contextPath}/doctor/" + doctorId;--%>
  <%--}--%>

  function clearWeekdaysFilter() {
    const currentUrl = new URL(window.location.href);
    currentUrl.searchParams.delete('weekdays');
    currentUrl.searchParams.set('page', '1');
    window.location.href = currentUrl.toString();
  }

  function clearCoverageFilter() {
    const currentUrl = new URL(window.location.href);
    currentUrl.searchParams.delete('coverage');
    currentUrl.searchParams.set('page', '1');
    window.location.href = currentUrl.toString();
  }

  function clearSpecialtyFilter() {
    const currentUrl = new URL(window.location.href);
    currentUrl.searchParams.delete('specialty');
    currentUrl.searchParams.set('page', '1');
    window.location.href = currentUrl.toString();
  }

  // Initialize search functionality
  document.addEventListener('DOMContentLoaded', function() {
    initializeViewToggle();
    initializeQuickFilters();
    initializeSorting();
    initializeCoverageFilter();
    initializeSpecialtyFilter();
    initializeWeekdaysFilter();

    // Add event listener for filter button
    const applyFiltersBtn = document.getElementById('applyFiltersBtn');
    if (applyFiltersBtn) {
      applyFiltersBtn.addEventListener('click', applyAllFilters);
    }
  });
</script>
</body>
