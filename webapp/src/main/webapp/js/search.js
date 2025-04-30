// Doctor Search Functionality
function initializeSearch() {
    const searchInput = document.getElementById("doctorSearch")
    if (searchInput) {
        searchInput.addEventListener("input", function () {
            const searchTerm = this.value.toLowerCase()
            const doctorCards = document.querySelectorAll(".doctor-card")

            doctorCards.forEach((card) => {
                const doctorName = card.querySelector(".doctor-name").textContent.toLowerCase()
                const doctorInfo = card.querySelector(".doctor-info").textContent.toLowerCase()

                if (doctorName.includes(searchTerm) || doctorInfo.includes(searchTerm)) {
                    card.style.display = ""
                } else {
                    card.style.display = "none"
                }
            })
        })
    }
}

// View Toggle Functionality
function initializeViewToggle() {
    const viewToggleBtns = document.querySelectorAll(".view-toggle-btn")
    const doctorsGrid = document.querySelector(".doctors-grid")

    if (viewToggleBtns.length && doctorsGrid) {
        viewToggleBtns.forEach((btn) => {
            btn.addEventListener("click", function () {
                // Remove active class from all buttons
                viewToggleBtns.forEach((b) => b.classList.remove("active"))
                // Add active class to clicked button
                this.classList.add("active")

                const currentUrl = new URL(window.location.href)

                const viewType = this.getAttribute("data-view")
                if (viewType === "list") {
                    doctorsGrid.classList.add("list-view")
                    currentUrl.searchParams.set('view', 'list')
                } else {
                    doctorsGrid.classList.remove("list-view")
                    currentUrl.searchParams.set('view', 'grid')
                }
                window.location.href = currentUrl.toString()
            })
        })
    }
}

// Quick Filters Functionality
function initializeQuickFilters() {
    const quickFilterBtns = document.querySelectorAll(".quick-filter-btn")

    if (quickFilterBtns.length) {
        quickFilterBtns.forEach((btn) => {
            btn.addEventListener("click", function () {
                // Remove active class from all buttons
                quickFilterBtns.forEach((b) => b.classList.remove("active"))
                // Add active class to clicked button
                this.classList.add("active")

                const filterType = this.getAttribute("data-filter")
                // Implement filter logic based on filter type
                applyQuickFilter(filterType)
            })
        })
    }
}

function applyQuickFilter(filterType) {
    const doctorCards = document.querySelectorAll(".doctor-card")

    doctorCards.forEach((card) => {
        if (filterType === "all") {
            card.style.display = ""
        } else if (filterType === "top-rated") {
            const rating = card.querySelector(".rating-count")
            if (rating && Number.parseFloat(rating.textContent) >= 4.5) {
                card.style.display = ""
            } else {
                card.style.display = "none"
            }
        } else if (filterType === "new") {
            // Show only new doctors (you might need to add a data attribute to identify new doctors)
            if (card.classList.contains("new-doctor")) {
                card.style.display = ""
            } else {
                card.style.display = "none"
            }
        }
    })
}

// Sorting Functionality
function initializeSorting() {
    const sortSelect = document.getElementById("sortSelect")

    if (sortSelect) {
        sortSelect.addEventListener("change", function () {
            const sortValue = this.value
            const lastUnderscoreIndex = sortValue.lastIndexOf("_");
            const orderBy = sortValue.substring(0, lastUnderscoreIndex);
            const direction = sortValue.substring(lastUnderscoreIndex + 1);

            // Update URL with sort parameters
            const currentUrl = new URL(window.location.href)
            currentUrl.searchParams.set("orderBy", orderBy || "name")
            currentUrl.searchParams.set("direction", direction || "asc")
            currentUrl.searchParams.set("page", "1")

            // Redirect to the updated URL
            window.location.href = currentUrl.toString()
        })

        // Set initial value based on URL parameters
        const urlParams = new URLSearchParams(window.location.search)
        const orderBy = urlParams.get("orderBy") || "name"
        const direction = urlParams.get("direction") || "asc"

        // Try to find the matching option
        const optionValue = `${orderBy}_${direction}`

        // Check if the option exists in the select
        const optionExists = Array.from(sortSelect.options).some((option) => option.value === optionValue)

        if (optionExists) {
            sortSelect.value = optionValue
        } else {
            // Default to name_asc if the option doesn't exist
            sortSelect.value = "name_asc"
        }
    }
}

// Coverage Filter Functionality
function initializeCoverageFilter() {
    const coverageSelect = document.getElementById("coverageSelect")

    if (coverageSelect) {
        coverageSelect.addEventListener("change", function () {
            const coverageId = this.value

            // Update URL with coverage parameter
            const currentUrl = new URL(window.location.href)
            if (coverageId === "0") {
                currentUrl.searchParams.delete("coverage")
            } else {
                currentUrl.searchParams.set("coverage", coverageId)
            }
            currentUrl.searchParams.set("page", "1")

            // Redirect to the updated URL
            window.location.href = currentUrl.toString()
        })

        // Set initial value based on URL parameters
        const urlParams = new URLSearchParams(window.location.search)
        const coverageId = urlParams.get("coverage") || "0"
        coverageSelect.value = coverageId
    }
}

// Specialty Filter Functionality
function initializeSpecialtyFilter() {
    const specialtySelect = document.getElementById("specialtySelect")

    if (specialtySelect) {
        specialtySelect.addEventListener("change", function () {
            const specialtyId = this.value

            // Update URL with specialty parameter
            const currentUrl = new URL(window.location.href)

            if (specialtyId === "0") {
                // Remove specialty parameter for "All Specialties"
                currentUrl.searchParams.delete("specialty")
            } else {
                currentUrl.searchParams.set("specialty", specialtyId)
            }

            currentUrl.searchParams.set("page", "1")

            // Redirect to the updated URL
            window.location.href = currentUrl.toString()
        })

        // Set initial value based on URL parameters
        const urlParams = new URLSearchParams(window.location.search)
        const specialtyId = urlParams.get("specialty") || "0"
        specialtySelect.value = specialtyId
    }
}

// Weekdays Filter Functionality
function initializeWeekdaysFilter() {
    const weekdayCheckboxes = document.querySelectorAll(".weekday-checkbox")

    if (weekdayCheckboxes.length) {
        // Get current weekdays from URL
        const urlParams = new URLSearchParams(window.location.search)
        const selectedWeekdays = urlParams.getAll("weekdays")

        // Set initial checkbox states
        weekdayCheckboxes.forEach((checkbox) => {
            const weekdayValue = checkbox.value
            if (selectedWeekdays.includes(weekdayValue)) {
                checkbox.checked = true
            }

            // checkbox.addEventListener('change', function() {
            //     applyWeekdaysFilter();
            // });
        })
    }
}

function applyWeekdaysFilter() {
    const weekdayCheckboxes = document.querySelectorAll(".weekday-checkbox:checked")
    const selectedWeekdays = Array.from(weekdayCheckboxes).map((cb) => cb.value)

    // Update URL with weekdays parameters
    const currentUrl = new URL(window.location.href)

    // Remove all existing weekdays parameters
    currentUrl.searchParams.delete("weekdays")

    // Add selected weekdays
    selectedWeekdays.forEach((weekday) => {
        currentUrl.searchParams.append("weekdays", weekday)
    })

    currentUrl.searchParams.set("page", "1")

    // Redirect to the updated URL
    window.location.href = currentUrl.toString()
}

// Apply All Filters
function applyAllFilters() {
    const specialtyId = document.getElementById("specialtySelect").value
    const coverageId = document.getElementById("coverageSelect").value
    const sortValue = document.getElementById("sortSelect").value
    const lastUnderscoreIndex = sortValue.lastIndexOf("_");
    const orderBy = sortValue.substring(0, lastUnderscoreIndex);
    const direction = sortValue.substring(lastUnderscoreIndex + 1);

    const weekdayCheckboxes = document.querySelectorAll(".weekday-checkbox:checked")
    const selectedWeekdays = Array.from(weekdayCheckboxes).map((cb) => cb.value)

    // Build URL with all parameters
    const currentUrl = new URL(window.location.href)

    // Handle specialty parameter
    if (specialtyId === "0") {
        currentUrl.searchParams.delete("specialty")
    } else {
        currentUrl.searchParams.set("specialty", specialtyId)
    }

    if (coverageId === "0") {
        currentUrl.searchParams.delete("coverage")
    } else {
        currentUrl.searchParams.set("coverage", coverageId)
    }

    currentUrl.searchParams.set("orderBy", orderBy || "name")
    currentUrl.searchParams.set("direction", direction || "asc")

    // Remove all existing weekdays parameters
    currentUrl.searchParams.delete("weekdays")

    // Add selected weekdays
    selectedWeekdays.forEach((weekday) => {
        currentUrl.searchParams.append("weekdays", weekday)
    })

    // Reset to page 1 when filters change
    currentUrl.searchParams.set("page", "1")

    // Redirect to the updated URL
    window.location.href = currentUrl.toString()
}

// Initialize all functionality
document.addEventListener("DOMContentLoaded", () => {
    initializeSearch()
    initializeViewToggle()
    initializeQuickFilters()
    initializeSorting()
    initializeCoverageFilter()
    initializeSpecialtyFilter()
    initializeWeekdaysFilter()

    // Add event listener for filter button if exists
    const applyFiltersBtn = document.getElementById("applyFiltersBtn")
    if (applyFiltersBtn) {
        applyFiltersBtn.addEventListener("click", applyAllFilters)
    }
})
