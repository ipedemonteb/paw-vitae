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
    <!-- Search Header -->
    <div class="search-header">
      <div class="search-header-content">
        <h1 class="search-title"><spring:message code="search.title" /></h1>
        <p class="search-subtitle"><spring:message code="search.doctors.specialty" arguments="${specialtyName}"/></p>
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
            <select id="specialtySelect" class="filter-select" onchange="changeSpecialty(this.value)">
              <c:forEach var="spec" items="${allSpecialties}">
                <option value="<c:out value='${spec.id}'/>" ${spec.id == specialty.id ? 'selected' : ''}>
                  <spring:message code="${spec.key}" />
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
              <option value="recommended"><spring:message code="search.sort.recommended" /></option>
              <option value="name_asc"><spring:message code="search.sort.name_asc" /></option>
              <option value="name_desc"><spring:message code="search.sort.name_desc" /></option>
            </select>
          </div>
        </div>

        <!-- View Toggle -->
        <div class="view-toggle">
          <button class="view-toggle-btn active" data-view="grid">
            <i class="fas fa-th-large"></i>
          </button>
          <button class="view-toggle-btn" data-view="list">
            <i class="fas fa-list"></i>
          </button>
        </div>
      </div>

      <!-- Quick Filters -->
      <div class="quick-filters">
        <button class="quick-filter-btn" data-filter="all">
          <spring:message code="search.filter.all" />
        </button>
        <button class="quick-filter-btn" data-filter="top-rated">
          <i class="fas fa-star"></i> <spring:message code="search.filter.top_rated" />
        </button>
        <button class="quick-filter-btn" data-filter="new">
          <i class="fas fa-certificate"></i> <spring:message code="search.filter.new" />
        </button>
      </div>
    </div>

    <!-- Results Section -->
    <div class="results-section">
      <div class="results-header">
        <div class="results-count">
          <c:choose>
            <c:when test="${not empty paginatedDoctors}">
              <span><spring:message code="search.results.count" arguments="${doctors.size()}" /></span>
            </c:when>
            <c:otherwise>
              <span><spring:message code="search.results.none" /></span>
            </c:otherwise>
          </c:choose>
        </div>
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
          <div class="doctors-grid">
            <c:forEach var="doctor" items="${paginatedDoctors}" varStatus="status">
              <div class="doctor-card">
                <div class="doctor-card-header">
                  <div class="doctor-avatar">
                    <img src="<c:url value='/doctor/${doctor.id}/image'/>" alt="<c:out value='${doctor.name} ${doctor.lastName}'/>" class="avatar-img">
                  </div>
                  <div class="doctor-rating">
                    <div class="stars">
                      <i class="fas fa-star"></i>
                      <i class="fas fa-star"></i>
                      <i class="fas fa-star"></i>
                      <i class="fas fa-star"></i>
                      <i class="fas fa-star-half-alt"></i>
                    </div>
                    <span class="rating-count">4.5</span>
                  </div>
                </div>

                <div class="doctor-card-body">
                  <h3 class="doctor-name">
                    <c:out value='${doctor.name} ${doctor.lastName}'/>
                  </h3>
                  <div class="doctor-specialty">
                    <i class="fas fa-stethoscope"></i> <c:out value="${specialtyName}"/>
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
                  <a href="<c:url value='/appointment?doctorId=${doctor.id}&specialtyId=${specialty.id}'/>" class="btn btn-primary">
                    <i class="fas fa-calendar-check"></i> <spring:message code="search.button.schedule" />
                  </a>
                  <button class="btn btn-secondary" onclick="viewDoctorProfile('${doctor.id}')">
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
                <a href="<c:url value='/search?specialty=${specialty.id}&page=${currentPage - 1}'/>" class="pagination-btn prev">
                  <i class="fas fa-chevron-left"></i>
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
                <a href="<c:url value='/search?specialty=${specialty.id}&page=${currentPage + 1}'/>" class="pagination-btn next">
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

  function changeSpecialty(specialtyId) {
    window.location.href = "${pageContext.request.contextPath}/search?specialty=" + specialtyId;
  }

  function viewDoctorProfile(doctorId) {
    window.location.href = "${pageContext.request.contextPath}/doctor/" + doctorId;
  }

  // Adjust content margin based on header height
  const fixedHeader = document.querySelector(".main-header");
  const mainContent = document.querySelector(".main-content");

  if (fixedHeader && mainContent) {
    const adjustContentMargin = () => {
      const headerHeight = fixedHeader.offsetHeight;
      mainContent.style.marginTop = (headerHeight * 1.1) + `px`;
    };

    // Adjust on page load
    adjustContentMargin();

    // Adjust on window resize
    window.addEventListener("resize", adjustContentMargin);
  }

  const doctorsIds = [
          <c:forEach var="doctor" items="${doctors}" varStatus="status">
            {
              id: "${doctor.id}",
            }<c:if test="${!status.last}">,</c:if>
    </c:forEach>
  ];

  // Initialize search functionality
  document.addEventListener('DOMContentLoaded', function() {
    initializeSearch();
    initializeViewToggle();
    initializeQuickFilters();
    initializeSorting();
  });
</script>
</body>
