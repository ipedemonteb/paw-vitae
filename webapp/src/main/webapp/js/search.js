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
                    // doctorsGrid.classList.add("list-view")
                    currentUrl.searchParams.set('view', 'list')
                } else {
                    // doctorsGrid.classList.remove("list-view")
                    currentUrl.searchParams.set('view', 'grid')
                }
                window.location.href = currentUrl.toString()
            })
        })
    }
}

// Enhanced search functionality with suggestions dropdown
function initializeSearchInput() {
    const searchInput = document.getElementById("doctorSearch")
    const searchContainer = document.querySelector(".search-bar-container")
    let debounceTimer = null
    let suggestionsContainer = null

    if (searchInput) {
        // Create suggestions container
        createSuggestionsContainer()

        searchInput.addEventListener('input', function () {
            const query = searchInput.value.trim()

            // Clear the previous timer
            clearTimeout(debounceTimer)

            // Set a new timer for debounce
            debounceTimer = setTimeout(async () => {
                if (query.length >= 1) {
                    // Fetch search results
                    await fetchSearchResults(query)
                    if (searchResults && searchResults.length > 0) {
                        displaySuggestions(searchResults)
                    } else {
                        hideSuggestions()
                    }
                } else {
                    hideSuggestions()
                }
            }, 300)
        })

        // Hide suggestions when clicking outside
        document.addEventListener('click', function(event) {
            if (!searchContainer.contains(event.target)) {
                hideSuggestions()
            }
        })

        // Handle keyboard navigation
        searchInput.addEventListener('keydown', function(event) {
            handleKeyboardNavigation(event)
        })
    }

    function createSuggestionsContainer() {
        suggestionsContainer = document.createElement('div')
        suggestionsContainer.className = 'search-suggestions'
        suggestionsContainer.style.display = 'none'
        searchContainer.appendChild(suggestionsContainer)
    }

    function displaySuggestions(doctors) {
        if (!suggestionsContainer) return

        // Clear previous suggestions
        suggestionsContainer.innerHTML = ''

        // Limit to 8 suggestions for better UX
        const limitedDoctors = doctors.slice(0, 8)

        limitedDoctors.forEach((doctor, index) => {
            const suggestionItem = document.createElement('div')
            suggestionItem.className = 'suggestion-item'
            suggestionItem.setAttribute('data-index', index)

            suggestionItem.innerHTML = `
                <div class="suggestion-avatar">
                    <img src="${contextPath}/image/${doctor.imageId || -1}" 
                         alt="${doctor.name} ${doctor.lastName}" 
                         class="suggestion-avatar-img">
                </div>
                <div class="suggestion-content">
                    <div class="suggestion-name">${doctor.name} ${doctor.lastName}</div>
                    <div class="suggestion-specialty">
                        <i class="fas fa-stethoscope"></i>
                        <span>Doctor</span>
                    </div>
                </div>
                <div class="suggestion-action">
                    <i class="fas fa-arrow-right"></i>
                </div>
            `

            // Add click handler
            suggestionItem.addEventListener('click', function() {
                selectDoctor(doctor)
            })

            // Add hover handler for keyboard navigation
            suggestionItem.addEventListener('mouseenter', function() {
                clearActiveSelection()
                this.classList.add('active')
            })

            suggestionsContainer.appendChild(suggestionItem)
        })

        // Show suggestions
        suggestionsContainer.style.display = 'block'
    }

    function hideSuggestions() {
        if (suggestionsContainer) {
            suggestionsContainer.style.display = 'none'
        }
    }

    function selectDoctor(doctor) {
        // Fill the search input with the selected doctor's name
        searchInput.value = `${doctor.name} ${doctor.lastName}`

        // Hide suggestions
        hideSuggestions()

        // Optionally redirect to doctor's profile or appointment page
        window.location.href = `${contextPath}/search/${doctor.id}`
    }

    function handleKeyboardNavigation(event) {
        if (!suggestionsContainer || suggestionsContainer.style.display === 'none') {
            return
        }

        const suggestions = suggestionsContainer.querySelectorAll('.suggestion-item')
        const currentActive = suggestionsContainer.querySelector('.suggestion-item.active')
        let currentIndex = currentActive ? parseInt(currentActive.getAttribute('data-index')) : -1

        switch(event.key) {
            case 'ArrowDown':
                event.preventDefault()
                currentIndex = currentIndex < suggestions.length - 1 ? currentIndex + 1 : 0
                setActiveSelection(currentIndex)
                break

            case 'ArrowUp':
                event.preventDefault()
                currentIndex = currentIndex > 0 ? currentIndex - 1 : suggestions.length - 1
                setActiveSelection(currentIndex)
                break

            case 'Enter':
                event.preventDefault()
                if (currentActive) {
                    const doctorIndex = parseInt(currentActive.getAttribute('data-index'))
                    selectDoctor(searchResults[doctorIndex])
                }
                break

            case 'Escape':
                hideSuggestions()
                searchInput.blur()
                break
        }
    }

    function setActiveSelection(index) {
        clearActiveSelection()
        const suggestions = suggestionsContainer.querySelectorAll('.suggestion-item')
        if (suggestions[index]) {
            suggestions[index].classList.add('active')
        }
    }

    function clearActiveSelection() {
        const activeItems = suggestionsContainer.querySelectorAll('.suggestion-item.active')
        activeItems.forEach(item => item.classList.remove('active'))
    }
}

let searchResults = [];

// Enhanced fetch function with better error handling
async function fetchSearchResults(query) {
    try {
        const response = await fetch(`/search/doctors/${encodeURIComponent(query)}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "X-Requested-With": "XMLHttpRequest",
            },
        })

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`)
        }

        const data = await response.json()
        searchResults = data.doctors || []
    } catch (error) {
        console.error("Error fetching search results:", error)
        searchResults = []
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
            if (coverageId === "0" || !isValidNumber(coverageId)) {
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
        coverageSelect.value = isValidNumber(coverageId) ? coverageId : "0"
    }
}


function isValidNumber(str) {
    if (str === null || str.trim() === "") {
        return false; // Null, empty, or whitespace-only strings are not valid numbers
    }
    const num = Number(str);
    return !isNaN(num); // Returns true if the string can be converted to a number
}

// Specialty Filter Functionality
function initializeSpecialtyFilter() {
    const specialtySelect = document.getElementById("specialtySelect")

    if (specialtySelect) {
        specialtySelect.addEventListener("change", function () {
            const specialtyId = this.value

            // Update URL with specialty parameter
            const currentUrl = new URL(window.location.href)

            if (specialtyId === "0" || !isValidNumber(specialtyId)) {
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
        specialtySelect.value = isValidNumber(specialtyId)? specialtyId : "0"
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
    if (specialtyId === "0" || !isValidNumber(specialtyId)) {
        currentUrl.searchParams.delete("specialty")
    } else {
        currentUrl.searchParams.set("specialty", specialtyId)
    }

    if (coverageId === "0" || !isValidNumber(coverageId)) {
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
