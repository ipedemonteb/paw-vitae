document.addEventListener("DOMContentLoaded", () => {
    // Initialize weekly schedules for each doctor
    // doctorsIds.forEach((doctor) => {
    //     const doctorId = doctor.id
    //     initializeWeeklySchedule(doctorId)
    // })
    //
    // /**
    //  * Initialize weekly schedule for a specific doctor
    //  * @param {string} doctorId - The doctor's ID
    //  */
    // function initializeWeeklySchedule(doctorId) {
    //     // DOM Elements
    //     const weeklySchedule = document.getElementById(`weeklySchedule-${doctorId}`)
    //     const prevWeekBtn = document.getElementById(`prevWeek-${doctorId}`)
    //     const nextWeekBtn = document.getElementById(`nextWeek-${doctorId}`)
    //     const currentWeekEl = document.getElementById(`currentWeek-${doctorId}`)
    //     const noSlotsMessage = document.getElementById(`noSlots-${doctorId}`)
    //     const nextAvailableEl = document.getElementById(`nextAvailable-${doctorId}`)
    //
    //     // Get messages from the global object created in the JSP
    //     const messages = window.appointmentMessages || {
    //         months: [
    //             "January",
    //             "February",
    //             "March",
    //             "April",
    //             "May",
    //             "June",
    //             "July",
    //             "August",
    //             "September",
    //             "October",
    //             "November",
    //             "December",
    //         ],
    //         weekdays: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"],
    //         weekdaysShort: ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"],
    //         noAvailableSlots: "No available time slots",
    //         nextAvailable: "Next available appointment:",
    //         seeMoreSchedules: "See more schedules",
    //         seeLessSchedules: "See less schedules",
    //     }
    //
    //     // State variables
    //     const argDate = new Date().toLocaleString("en-US", {
    //         timeZone: "America/Argentina/Buenos_Aires",
    //     })
    //     const today = new Date(argDate)
    //     let currentWeekStart = new Date(today)
    //     let isExpanded = false // Track if calendar is expanded
    //
    //     // Adjust to start from today (not from beginning of week)
    //     currentWeekStart.setHours(0, 0, 0, 0)
    //
    //     // Get doctor's availability data
    //     const doctorAvailabilitySlots = window.doctorAvailabilitySlots ? window.doctorAvailabilitySlots[doctorId] || [] : []
    //     const doctorFound = window.doctorsAvailability
    //         ? window.doctorsAvailability.find((doctor) => doctor.id === doctorId)
    //         : null
    //     const doctorBookedSlots = doctorFound ? doctorFound.info : []
    //
    //     // Initialize the weekly schedule
    //     initSchedule()
    //
    //     // Declare contextPath (assuming it's available globally or from a JSP variable)
    //     const contextPath = window.contextPath || "" // Provide a default value if not available
    //
    //     /**
    //      * Initialize the weekly schedule
    //      */
    //     function initSchedule() {
    //         renderWeek()
    //
    //         // Event listeners
    //         prevWeekBtn.addEventListener("click", goToPreviousWeek)
    //         nextWeekBtn.addEventListener("click", goToNextWeek)
    //
    //         // Initially hide the previous week button if we're in the current week
    //         prevWeekBtn.style.visibility = "hidden"
    //     }
    //
    //     // Modify the renderWeek function to adapt to list view
    //     function renderWeek() {
    //         // Update the current week display
    //         updateWeekDisplay()
    //
    //         // Clear the schedule
    //         weeklySchedule.innerHTML = ""
    //
    //         // Create the week row
    //         const weekRow = document.createElement("div")
    //         weekRow.className = "week-row"
    //
    //         // Get the end of the week (7 days from start)
    //         const weekEnd = new Date(currentWeekStart)
    //         weekEnd.setDate(weekEnd.getDate() + 6)
    //
    //         // Check if we have any available slots in this week
    //         let hasAvailableSlots = false
    //
    //         // Check if we're in list view or grid view
    //         const isListView = !weeklySchedule.closest(".doctors-list-view").classList.contains("grid-view")
    //
    //         // Determine how many days to show based on view mode and expanded state
    //         let daysToShow = 5 // Default to 5 days (weekdays)
    //         if (isExpanded) {
    //             daysToShow = 7 // Show all days when expanded
    //         }
    //
    //         // Create columns for each day of the week
    //         for (let i = 0; i < daysToShow; i++) {
    //             const currentDate = new Date(currentWeekStart)
    //             currentDate.setDate(currentDate.getDate() + i)
    //
    //             const dayColumn = document.createElement("div")
    //             dayColumn.className = "day-column"
    //
    //             // Create day header
    //             const dayHeader = document.createElement("div")
    //             dayHeader.className = "day-header"
    //             if (isToday(currentDate)) {
    //                 dayHeader.classList.add("day-today")
    //             }
    //
    //             const dayName = document.createElement("div")
    //             dayName.className = "day-name"
    //             dayName.textContent = messages.weekdaysShort[currentDate.getDay()]
    //
    //             const dayDate = document.createElement("div")
    //             dayDate.className = "day-date"
    //             dayDate.textContent = `${messages.months[currentDate.getMonth()].substring(0, 3)} ${currentDate.getDate()}`
    //
    //             dayHeader.appendChild(dayName)
    //             dayHeader.appendChild(dayDate)
    //             dayColumn.appendChild(dayHeader)
    //
    //             // Create time slots container
    //             const timeSlots = document.createElement("div")
    //             timeSlots.className = "time-slots"
    //
    //             // Get available slots for this day
    //             const availableSlots = getAvailableSlotsForDay(currentDate)
    //
    //             if (availableSlots.length > 0) {
    //                 hasAvailableSlots = true
    //
    //                 // Display slots based on expanded state and view mode
    //                 // Show fewer slots in list view when not expanded
    //                 const maxSlotsToShow = isListView && !isExpanded ? 1 : isExpanded ? availableSlots.length : 2
    //                 const displaySlots = availableSlots.slice(0, maxSlotsToShow)
    //
    //                 displaySlots.forEach((slot) => {
    //                     const timeSlot = document.createElement("div")
    //                     timeSlot.className = "time-slot"
    //                     timeSlot.textContent = formatHour(slot)
    //                     timeSlots.appendChild(timeSlot)
    //                 })
    //
    //                 // If there are more slots and not expanded, add a "more" indicator
    //                 if (availableSlots.length > maxSlotsToShow) {
    //                     const moreSlots = document.createElement("div")
    //                     moreSlots.className = "no-slot"
    //                     moreSlots.textContent = `+${availableSlots.length - maxSlotsToShow} more`
    //                     timeSlots.appendChild(moreSlots)
    //                 }
    //             } else {
    //                 // No slots available for this day
    //                 const noSlot = document.createElement("div")
    //                 noSlot.className = "no-slot"
    //                 noSlot.textContent = "—"
    //                 timeSlots.appendChild(noSlot)
    //             }
    //
    //             dayColumn.appendChild(timeSlots)
    //             weekRow.appendChild(dayColumn)
    //         }
    //
    //         weeklySchedule.appendChild(weekRow)
    //
    //         // If there are no available slots in this week, show the no slots message
    //         if (!hasAvailableSlots) {
    //             // Check if there's a future date with availability
    //             const nextAvailableDate = findNextAvailableDate(weekEnd)
    //
    //             if (nextAvailableDate) {
    //                 noSlotsMessage.style.display = "block"
    //                 nextAvailableEl.textContent = `${messages.nextAvailable} ${formatDate(nextAvailableDate)}`
    //                 weeklySchedule.style.display = "none"
    //             } else {
    //                 noSlotsMessage.style.display = "block"
    //                 nextAvailableEl.textContent = ""
    //                 weeklySchedule.style.display = "none"
    //             }
    //         } else {
    //             noSlotsMessage.style.display = "none"
    //             weeklySchedule.style.display = "block"
    //
    //             // Add "See more schedules" button if needed
    //             const seeMoreBtn = document.createElement("div")
    //             seeMoreBtn.className = "see-more"
    //             const seeMoreLink = document.createElement("button")
    //             seeMoreLink.className = "see-more-btn"
    //
    //             // Change text based on expanded state
    //             if (isExpanded) {
    //                 seeMoreLink.innerHTML = `${messages.seeLessSchedules} <i class="fas fa-chevron-up"></i>`
    //             } else {
    //                 seeMoreLink.innerHTML = `${messages.seeMoreSchedules} <i class="fas fa-chevron-down"></i>`
    //             }
    //
    //             // Toggle expanded state when clicked
    //             seeMoreLink.addEventListener("click", () => {
    //                 isExpanded = !isExpanded
    //                 renderWeek() // Re-render with new expanded state
    //             })
    //
    //             seeMoreBtn.appendChild(seeMoreLink)
    //             weeklySchedule.appendChild(seeMoreBtn)
    //         }
    //     }
    //
    //     /**
    //      * Update the week display text
    //      */
    //     function updateWeekDisplay() {
    //         const weekEnd = new Date(currentWeekStart)
    //         weekEnd.setDate(weekEnd.getDate() + 6)
    //
    //         const startMonth = messages.months[currentWeekStart.getMonth()]
    //         const endMonth = messages.months[weekEnd.getMonth()]
    //
    //         let displayText
    //
    //         if (currentWeekStart.getMonth() === weekEnd.getMonth()) {
    //             // Same month
    //             displayText = `${startMonth} ${currentWeekStart.getDate()} - ${weekEnd.getDate()}, ${weekEnd.getFullYear()}`
    //         } else {
    //             // Different months
    //             displayText = `${startMonth} ${currentWeekStart.getDate()} - ${endMonth} ${weekEnd.getDate()}, ${weekEnd.getFullYear()}`
    //         }
    //
    //         currentWeekEl.textContent = displayText
    //     }
    //
    //     /**
    //      * Go to the previous week
    //      */
    //     function goToPreviousWeek() {
    //         const newStart = new Date(currentWeekStart)
    //         newStart.setDate(newStart.getDate() - 7)
    //
    //         // Don't go before today
    //         if (newStart < today) {
    //             return
    //         }
    //
    //         currentWeekStart = newStart
    //         renderWeek()
    //
    //         // Show the next week button
    //         nextWeekBtn.style.visibility = "visible"
    //
    //         // Check if we should hide the previous week button
    //         const prevWeek = new Date(currentWeekStart)
    //         prevWeek.setDate(prevWeek.getDate() - 7)
    //
    //         if (prevWeek < today) {
    //             prevWeekBtn.style.visibility = "hidden"
    //         }
    //     }
    //
    //     /**
    //      * Go to the next week
    //      */
    //     function goToNextWeek() {
    //         currentWeekStart.setDate(currentWeekStart.getDate() + 7)
    //         renderWeek()
    //
    //         // Show the previous week button
    //         prevWeekBtn.style.visibility = "visible"
    //
    //         // Limit to 8 weeks in the future
    //         const maxDate = new Date(today)
    //         maxDate.setDate(maxDate.getDate() + 56) // 8 weeks
    //
    //         const nextWeek = new Date(currentWeekStart)
    //         nextWeek.setDate(nextWeek.getDate() + 7)
    //
    //         if (nextWeek > maxDate) {
    //             nextWeekBtn.style.visibility = "hidden"
    //         }
    //     }
    //
    //     /**
    //      * Get available time slots for a specific day
    //      * @param {Date} date - The date to check
    //      * @returns {Array} Array of available hours
    //      */
    //     function getAvailableSlotsForDay(date) {
    //         const dayOfWeek = date.getDay()
    //         const formattedDate = formatDateForSubmission(date)
    //         const currentHour = today.getHours()
    //
    //         // Get the doctor's availability slots for this day of week
    //         const availableSlots = doctorAvailabilitySlots.filter((slot) => slot.dayOfWeek === (dayOfWeek + 6) % 7) // Adjust for Monday as day 0
    //
    //         // Get booked slots for this date
    //         const bookedEntry = doctorBookedSlots.find((entry) => entry.date === formattedDate)
    //         const bookedHours = bookedEntry ? bookedEntry.hours : []
    //
    //         // Collect all available hours
    //         const availableHours = []
    //
    //         if (availableSlots.length > 0) {
    //             availableSlots.forEach((slot) => {
    //                 for (let hour = slot.startTime; hour <= slot.endTime; hour++) {
    //                     // Skip past hours if it's today
    //                     if (isToday(date) && hour <= currentHour) {
    //                         continue
    //                     }
    //
    //                     // Skip booked hours
    //                     if (bookedHours.includes(hour)) {
    //                         continue
    //                     }
    //
    //                     availableHours.push(hour)
    //                 }
    //             })
    //         }
    //
    //         return availableHours
    //     }
    //
    //     /**
    //      * Find the next date with available slots
    //      * @param {Date} afterDate - The date to start searching from
    //      * @returns {Date|null} The next available date or null if none found
    //      */
    //     function findNextAvailableDate(afterDate) {
    //         const maxDate = new Date(today)
    //         maxDate.setDate(maxDate.getDate() + 90) // Look up to 90 days ahead
    //
    //         const searchDate = new Date(afterDate)
    //         searchDate.setDate(searchDate.getDate() + 1)
    //
    //         while (searchDate <= maxDate) {
    //             const availableSlots = getAvailableSlotsForDay(searchDate)
    //
    //             if (availableSlots.length > 0) {
    //                 return searchDate
    //             }
    //
    //             searchDate.setDate(searchDate.getDate() + 1)
    //         }
    //
    //         return null
    //     }
    //
    //     /**
    //      * Format hour for display (e.g., "14:00")
    //      * @param {number} hour - The hour to format (0-23)
    //      * @returns {string} Formatted hour string
    //      */
    //     function formatHour(hour) {
    //         return `${hour}:00`
    //     }
    //
    //     /**
    //      * Format date for display (e.g., "Monday, January 1, 2023")
    //      * @param {Date} date - The date to format
    //      * @returns {string} Formatted date string
    //      */
    //     function formatDate(date) {
    //         const dayOfWeek = messages.weekdays[date.getDay()]
    //         const month = messages.months[date.getMonth()]
    //         const day = date.getDate()
    //         const year = date.getFullYear()
    //
    //         return `${dayOfWeek}, ${month} ${day}, ${year}`
    //     }
    //
    //     /**
    //      * Format date for form submission (e.g., "2023-01-01")
    //      * @param {Date} date - The date to format
    //      * @returns {string} Formatted date string for submission
    //      */
    //     function formatDateForSubmission(date) {
    //         const year = date.getFullYear()
    //         const month = String(date.getMonth() + 1).padStart(2, "0")
    //         const day = String(date.getDate()).padStart(2, "0")
    //
    //         return `${year}-${month}-${day}`
    //     }
    //
    //     /**
    //      * Check if the given date is today
    //      * @param {Date} date - The date to check
    //      * @returns {boolean} True if the date is today, false otherwise
    //      */
    //     function isToday(date) {
    //         return (
    //             date.getDate() === today.getDate() &&
    //             date.getMonth() === today.getMonth() &&
    //             date.getFullYear() === today.getFullYear()
    //         )
    //     }
    // }

    // Initialize search functionality
    function initializeSearch() {
        const searchInput = document.getElementById("doctorSearch")
        const doctorCards = document.querySelectorAll(".doctor-card")

        if (!searchInput) return

        searchInput.addEventListener("input", function () {
            const searchTerm = this.value.toLowerCase().trim()

            doctorCards.forEach((card) => {
                const doctorName = card.querySelector(".doctor-name").textContent.toLowerCase()
                const doctorSpecialty = card.querySelector(".doctor-specialty").textContent.toLowerCase()
                const doctorEmail = card.querySelector(".info-item:nth-child(1) span").textContent.toLowerCase()

                if (
                    doctorName.includes(searchTerm) ||
                    doctorSpecialty.includes(searchTerm) ||
                    doctorEmail.includes(searchTerm)
                ) {
                    card.style.display = ""
                } else {
                    card.style.display = "none"
                }
            })

            // Update results count
            const visibleCards = Array.from(doctorCards).filter((card) => card.style.display !== "none")
            updateResultsCount(visibleCards.length)
        })
    }

    // Update results count
    function updateResultsCount(count) {
        const resultsCount = document.querySelector(".results-count span")
        if (resultsCount) {
            // Get the text and replace the number
            const text = resultsCount.textContent
            resultsCount.textContent = text.replace(/\d+/, count)
        }
    }

    // Initialize view toggle
    function initializeViewToggle() {
        const viewToggleBtns = document.querySelectorAll(".view-toggle-btn")
        const doctorsGrid = document.querySelector(".doctors-grid")

        if (!viewToggleBtns.length || !doctorsGrid) return

        viewToggleBtns.forEach((btn) => {
            btn.addEventListener("click", function () {
                // Remove active class from all buttons
                viewToggleBtns.forEach((b) => b.classList.remove("active"))

                // Add active class to clicked button
                this.classList.add("active")

                // Toggle view class on doctors grid
                const view = this.getAttribute("data-view")
                if (view === "list") {
                    doctorsGrid.classList.add("list-view")
                } else {
                    doctorsGrid.classList.remove("list-view")
                }
            })
        })
    }

    // Initialize quick filters
    function initializeQuickFilters() {
        const quickFilterBtns = document.querySelectorAll(".quick-filter-btn")
        const doctorCards = document.querySelectorAll(".doctor-card")

        if (!quickFilterBtns.length) return

        quickFilterBtns.forEach((btn) => {
            btn.addEventListener("click", function () {
                // Remove active class from all buttons
                quickFilterBtns.forEach((b) => b.classList.remove("active"))

                // Add active class to clicked button
                this.classList.add("active")

                const filter = this.getAttribute("data-filter")

                // Apply filter
                if (filter === "all") {
                    doctorCards.forEach((card) => {
                        card.style.display = ""
                    })
                } else if (filter === "top-rated") {
                    doctorCards.forEach((card) => {
                        const rating = card.querySelector(".rating-count")
                        if (rating && Number.parseFloat(rating.textContent) >= 4.5) {
                            card.style.display = ""
                        } else {
                            card.style.display = "none"
                        }
                    })
                } else if (filter === "new") {
                    // For demo purposes, show random doctors as "new"
                    doctorCards.forEach((card, index) => {
                        if (index % 3 === 0) {
                            // Every third doctor is "new"
                            card.style.display = ""
                        } else {
                            card.style.display = "none"
                        }
                    })
                }

                // Update results count
                const visibleCards = Array.from(doctorCards).filter((card) => card.style.display !== "none")
                // updateResultsCount(visibleCards.length)
            })
        })
    }

    // Initialize sorting
    function initializeSorting() {
        const sortSelect = document.getElementById("sortSelect")
        const doctorsGrid = document.querySelector(".doctors-grid")

        if (!sortSelect || !doctorsGrid) return

        sortSelect.addEventListener("change", function () {
            const sortValue = this.value
            const doctorCards = Array.from(document.querySelectorAll(".doctor-card"))

            // Sort the doctor cards
            doctorCards.sort((a, b) => {
                const nameA = a.querySelector(".doctor-name").textContent
                const nameB = b.querySelector(".doctor-name").textContent

                if (sortValue === "name_asc") {
                    return nameA.localeCompare(nameB)
                } else if (sortValue === "name_desc") {
                    return nameB.localeCompare(nameA)
                } else if (sortValue === "recommended") {
                    // For demo purposes, sort by rating
                    const ratingA = Number.parseFloat(a.querySelector(".rating-count").textContent)
                    const ratingB = Number.parseFloat(b.querySelector(".rating-count").textContent)
                    return ratingB - ratingA
                }

                return 0
            })

            // Remove all doctor cards
            doctorCards.forEach((card) => {
                card.remove()
            })

            // Append sorted doctor cards
            doctorCards.forEach((card) => {
                doctorsGrid.appendChild(card)
            })
        })
    }

    // Make these functions globally available
    window.initializeSearch = initializeSearch
    window.initializeViewToggle = initializeViewToggle
    window.initializeQuickFilters = initializeQuickFilters
    window.initializeSorting = initializeSorting
})
