// Medical History Dashboard JavaScript
document.addEventListener("DOMContentLoaded", () => {
    // Initialize search functionality
    initializeSearch()
    initializeFilters()

    // Check for success messages
    checkForSuccessMessages()
    formatAppointmentDates()
})

function formatAppointmentDates() {
    document.querySelectorAll(".appointment-card").forEach(card => {
        const rawDate = card.dataset.date
        if (rawDate) {
            const formatted = formatDate(rawDate)
            const dateDisplayElement = card.querySelector(".appointment-date-format")
            if (dateDisplayElement) {
                dateDisplayElement.textContent = formatted
            }
        }
    })
}


// Search functionality
function initializeSearch() {
    const searchInput = document.getElementById("searchInput")
    const clearSearch = document.getElementById("clearSearch")

    if (searchInput) {
        searchInput.addEventListener("input", function () {
            const searchTerm = this.value.toLowerCase().trim()

            if (clearSearch) {
                if (searchTerm.length > 0) {
                    clearSearch.style.display = "block"
                } else {
                    clearSearch.style.display = "none"
                }
            }

            filterAppointments()
        })
    }

    if (clearSearch) {
        clearSearch.addEventListener("click", function () {
            if (searchInput) {
                searchInput.value = ""
            }
            this.style.display = "none"
            filterAppointments()
        })
    }
}

// Filter functionality
function initializeFilters() {
    const dateFilter = document.getElementById("dateFilter")

    if (dateFilter) {
        dateFilter.addEventListener("change", filterAppointments)
    }
}

// Main filter function
function filterAppointments() {
    const searchInput = document.getElementById("searchInput")
    const dateFilter = document.getElementById("dateFilter")

    // Add null checks to prevent errors
    const searchTerm = searchInput ? searchInput.value.toLowerCase().trim() : ""
    const dateFilterValue = dateFilter ? dateFilter.value : "all"

    const appointmentCards = document.querySelectorAll(".appointment-card")
    const appointmentsContainer = document.getElementById("appointmentsContainer")
    const noResults = document.getElementById("noResults")
    const emptyState = document.getElementById("emptyState")

    let visibleCount = 0

    appointmentCards.forEach((card) => {
        let shouldShow = true

        // Search filter
        if (searchTerm) {
            const appointmentText = card.textContent.toLowerCase()
            const doctorName = card.dataset.doctor ? card.dataset.doctor.toLowerCase() : ""

            if (!appointmentText.includes(searchTerm) && !doctorName.includes(searchTerm)) {
                shouldShow = false
            }
        }

        // Date filter
        if (shouldShow && dateFilterValue !== "all") {
            const appointmentDateStr = card.dataset.date
            if (appointmentDateStr) {
                const appointmentDate = new Date(appointmentDateStr)
                const now = new Date()

                switch (dateFilterValue) {
                    case "thisMonth":
                        if (appointmentDate.getMonth() !== now.getMonth() || appointmentDate.getFullYear() !== now.getFullYear()) {
                            shouldShow = false
                        }
                        break
                    case "lastMonth":
                        const lastMonth = new Date(now.getFullYear(), now.getMonth() - 1)
                        if (
                            appointmentDate.getMonth() !== lastMonth.getMonth() ||
                            appointmentDate.getFullYear() !== lastMonth.getFullYear()
                        ) {
                            shouldShow = false
                        }
                        break
                    case "last3Months":
                        const threeMonthsAgo = new Date(now.getFullYear(), now.getMonth() - 3)
                        if (appointmentDate < threeMonthsAgo) {
                            shouldShow = false
                        }
                        break
                    case "thisYear":
                        if (appointmentDate.getFullYear() !== now.getFullYear()) {
                            shouldShow = false
                        }
                        break
                }
            }
        }

        // Show/hide card with animation
        if (shouldShow) {
            card.style.display = "block"
            card.classList.remove("filtered-out")
            card.classList.add("filtered-in")
            visibleCount++
        } else {
            card.classList.add("filtered-out")
            card.classList.remove("filtered-in")
            setTimeout(() => {
                if (card.classList.contains("filtered-out")) {
                    card.style.display = "none"
                }
            }, 300)
        }
    })

    // Show/hide no results message with null checks
    if (visibleCount === 0) {
        if (searchTerm || dateFilterValue !== "all") {
            if (noResults) noResults.style.display = "block"
            if (emptyState) emptyState.style.display = "none"
        } else {
            if (noResults) noResults.style.display = "none"
            if (emptyState) emptyState.style.display = "block"
        }
        if (appointmentsContainer) appointmentsContainer.style.display = "none"
    } else {
        if (noResults) noResults.style.display = "none"
        if (emptyState) emptyState.style.display = "none"
        if (appointmentsContainer) appointmentsContainer.style.display = "block"
    }
}

// Toggle files section
function toggleFiles(appointmentId) {
    const filesSection = document.getElementById(`files-${appointmentId}`)
    const toggleBtn = document.querySelector(`[onclick="toggleFiles(${appointmentId})"]`)
    const toggleText = toggleBtn.querySelector(".toggle-text")
    const toggleIcon = toggleBtn.querySelector(".toggle-icon")

    if (filesSection.style.display === "none" || filesSection.style.display === "") {
        filesSection.style.display = "block"
        toggleBtn.classList.add("expanded")
        toggleText.textContent = toggleText.textContent.includes("Show")
            ? toggleText.textContent.replace("Show", "Hide")
            : toggleText.textContent.replace("Mostrar", "Ocultar")
        toggleIcon.style.transform = "rotate(180deg)"
    } else {
        filesSection.style.display = "none"
        toggleBtn.classList.remove("expanded")
        toggleText.textContent = toggleText.textContent.includes("Hide")
            ? toggleText.textContent.replace("Hide", "Show")
            : toggleText.textContent.replace("Ocultar", "Mostrar")
        toggleIcon.style.transform = "rotate(0deg)"
    }
}

// Check for success messages in URL
function checkForSuccessMessages() {
    const urlParams = new URLSearchParams(window.location.search)

    if (urlParams.get("downloaded") === "true") {
        showSuccessToast()
        // Clean URL
        const newUrl = window.location.pathname
        window.history.replaceState({}, document.title, newUrl)
    }
}

// Show success toast
function showSuccessToast() {
    const toast = document.getElementById("successToast")
    if (toast) {
        toast.classList.add("show")

        // Auto hide after 5 seconds
        setTimeout(() => {
            hideSuccessToast()
        }, 5000)
    }
}

// Hide success toast
function hideSuccessToast() {
    const toast = document.getElementById("successToast")
    if (toast) {
        toast.classList.remove("show")
    }
}

function formatDate(dateStr) {
    const date = new Date(dateStr)
    const day = String(date.getDate()).padStart(2, '0')
    const month = String(date.getMonth() + 1).padStart(2, '0') // months are 0-indexed
    const year = date.getFullYear()
    const hours = String(date.getHours()).padStart(2, '0')
    const minutes = String(date.getMinutes()).padStart(2, '0')
    return `${day}/${month}/${year} - ${hours}:${minutes}`
}

document.addEventListener('DOMContentLoaded', function() {
    // Custom select functionality
    const selectTrigger = document.querySelector('.custom-select-trigger');
    const selectOptions = document.querySelector('.custom-select-options');
    const selectOptionItems = document.querySelectorAll('.custom-select-option');

    if (selectTrigger) {
        selectTrigger.addEventListener('click', function() {
            this.classList.toggle('active');
            selectOptions.classList.toggle('active');
        });

        selectOptionItems.forEach(option => {
            option.addEventListener('click', function() {
                const value = this.dataset.value;
                const text = this.textContent;

                // Update trigger text
                selectTrigger.querySelector('.select-text').textContent = text;

                // Update form and submit
                document.querySelector('input[name="direction"]').value = value;
                document.getElementById('sortForm').submit();

                // Close dropdown
                selectTrigger.classList.remove('active');
                selectOptions.classList.remove('active');
            });
        });

        // Close dropdown when clicking outside
        document.addEventListener('click', function(e) {
            if (!selectTrigger.contains(e.target)) {
                selectTrigger.classList.remove('active');
                selectOptions.classList.remove('active');
            }
        });
    }
});

