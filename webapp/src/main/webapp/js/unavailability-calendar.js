/**
 * Unavailability Calendar for Doctor Dashboard
 *
 * This script handles the interactive calendar for managing doctor unavailability periods.
 * Features:
 * - Monthly calendar view
 * - Dynamic loading of unavailability data
 * - Range selection for new unavailability periods
 * - Visual indicators for existing unavailability periods
 * - AJAX integration with backend
 */

document.addEventListener("DOMContentLoaded", () => {
    // Calendar state
    const currentDate = new Date()
    let unavailabilitySlots = []
    let unavailabilityCounter = 0
    let isSelecting = false
    let selectionStart = null
    let selectionEnd = null
    let tempSelection = []



    // DOM elements
    const prevMonthBtn = document.getElementById("prevMonthBtn")
    const nextMonthBtn = document.getElementById("nextMonthBtn")
    const currentMonthYear = document.getElementById("currentMonthYear")
    const calendarDays = document.getElementById("calendarDays")
    const selectionInfo = document.getElementById("selectionInfo")
    const selectionText = document.getElementById("selectionText")
    const calendarLoading = document.getElementById("calendarLoading")
    const unavailabilityInputsContainer = document.getElementById("unavailability-inputs-container")

    const contextPath = "" // Replace with actual context path if needed
    const showSuccessToast = () => {} // Replace with actual toast function if needed


    // Initialize calendar
    initializeCalendar()

    /**
     * Initialize the calendar with current data and event listeners
     */
    function initializeCalendar() {
        // Load existing unavailability slots from form
        loadExistingUnavailabilitySlots()

        // Set up event listeners
        if (prevMonthBtn) {
            prevMonthBtn.addEventListener("click", (e) => {
                e.preventDefault()
                goToPreviousMonth()
            })
        }

        if (nextMonthBtn) {
            nextMonthBtn.addEventListener("click", (e) => {
                e.preventDefault()
                goToNextMonth()
            })
        }

        // Render initial calendar
        renderCalendar()

        // Load data for current month
        loadUnavailabilityData(currentDate.getFullYear(), currentDate.getMonth() + 1)
    }

    /**
     * Load existing unavailability slots from the form inputs
     */
    function loadExistingUnavailabilitySlots() {
        if (!unavailabilityInputsContainer) return

        const existingInputs = unavailabilityInputsContainer.querySelectorAll('input[type="hidden"]')
        const slots = []

        for (let i = 0; i < existingInputs.length; i += 2) {
            if (existingInputs[i] && existingInputs[i + 1]) {
                let startDate = existingInputs[i].value
                let endDate = existingInputs[i + 1].value

                // Fix: Ensure dates are in correct format
                startDate = normalizeDate(startDate)
                endDate = normalizeDate(endDate)

                if (startDate && endDate) {
                    slots.push({
                        startDate: startDate,
                        endDate: endDate,
                        index: unavailabilityCounter++,
                    })
                }
            }
        }

        unavailabilitySlots = slots
    }

    /**
     * Normalize date to YYYY-MM-DD format
     */
    function normalizeDate(dateValue) {
        if (!dateValue) return ""

        // If it's already in correct format, return as is
        if (typeof dateValue === "string" && /^\d{4}-\d{2}-\d{2}$/.test(dateValue)) {
            return dateValue
        }

        // If it's in array format like "2025,5,24", convert it
        if (typeof dateValue === "string" && dateValue.includes(",")) {
            const parts = dateValue.split(",").map((part) => Number.parseInt(part.trim()))
            if (parts.length === 3) {
                const [year, month, day] = parts
                // Note: month in the array format might be 0-based or 1-based, need to check
                const normalizedMonth = month < 12 ? month : month - 1 // Adjust if needed
                const date = new Date(year, normalizedMonth, day)
                return formatDateForSubmission(date)
            }
        }

        // If it's a Date object
        if (dateValue instanceof Date) {
            return formatDateForSubmission(dateValue)
        }

        // Try to parse as Date
        try {
            const date = new Date(dateValue)
            if (!isNaN(date.getTime())) {
                return formatDateForSubmission(date)
            }
        } catch (e) {
        }

        return ""
    }

    /**
     * Navigate to previous month
     */
    function goToPreviousMonth() {
        const today = new Date()
        const prevMonth = new Date(currentDate.getFullYear(), currentDate.getMonth() - 1, 1)

        // Don't allow navigation to months before current month
        if (prevMonth < new Date(today.getFullYear(), today.getMonth(), 1)) {
            return
        }

        currentDate.setMonth(currentDate.getMonth() - 1)
        renderCalendar()
        loadUnavailabilityData(currentDate.getFullYear(), currentDate.getMonth() + 1)
    }

    /**
     * Navigate to next month
     */
    function goToNextMonth() {
        currentDate.setMonth(currentDate.getMonth() + 1)
        renderCalendar()
        loadUnavailabilityData(currentDate.getFullYear(), currentDate.getMonth() + 1)
    }

    /**
     * Render the calendar for the current month
     */
    function renderCalendar() {
        if (!currentMonthYear || !calendarDays) return

        // Update header
        currentMonthYear.textContent = `${monthNames[currentDate.getMonth()]} ${currentDate.getFullYear()}`

        // Clear calendar days
        calendarDays.innerHTML = ""

        const year = currentDate.getFullYear()
        const month = currentDate.getMonth()
        const firstDay = new Date(year, month, 1)
        const lastDay = new Date(year, month + 1, 0)
        const today = new Date()

        // Get first day of week (Monday = 0)
        const firstDayOfWeek = (firstDay.getDay() + 6) % 7

        // Add empty cells for days before first day of month
        for (let i = 0; i < firstDayOfWeek; i++) {
            const emptyDay = document.createElement("div")
            emptyDay.className = "calendar-day empty"
            calendarDays.appendChild(emptyDay)
        }

        // Add days of the month
        for (let day = 1; day <= lastDay.getDate(); day++) {
            const date = new Date(year, month, day)
            const dayElement = createDayElement(date, day)
            calendarDays.appendChild(dayElement)
        }

        // Update navigation buttons
        updateNavigationButtons()
    }

    /**
     * Create a day element for the calendar
     */
    function createDayElement(date, day) {
        const dayElement = document.createElement("div")
        dayElement.className = "calendar-day"
        dayElement.textContent = day
        dayElement.dataset.date = formatDateForComparison(date)

        const today = new Date()
        const isPast = date < today.setHours(0, 0, 0, 0)

        // Disable past dates
        if (isPast) {
            dayElement.classList.add("disabled")
            return dayElement
        }

        // Check if this date is in an unavailability period
        const unavailabilityStatus = getUnavailabilityStatus(date)
        if (unavailabilityStatus) {
            dayElement.classList.add(unavailabilityStatus.type)
            dayElement.dataset.unavailabilityIndex = unavailabilityStatus.index

            // Add direct delete functionality for unavailable dates - no confirmation
            if (
                unavailabilityStatus.type === "unavailable" ||
                unavailabilityStatus.type === "range-start" ||
                unavailabilityStatus.type === "range-end" ||
                unavailabilityStatus.type === "in-range"
            ) {
                dayElement.addEventListener("click", (e) => {
                    e.preventDefault()
                    e.stopPropagation()
                    deleteUnavailabilityPeriod(unavailabilityStatus.index)
                })
                dayElement.title = messages.deleteTitle;
                dayElement.style.cursor = "pointer"
            }
        } else {
            // Add click handler for available dates
            dayElement.addEventListener("click", (e) => {
                e.preventDefault()
                e.stopPropagation()
                handleDayClick(date, dayElement)
            })
            dayElement.classList.add("available")
        }

        return dayElement
    }

    /**
     * Get unavailability status for a specific date
     */
    function getUnavailabilityStatus(date) {
        const dateStr = formatDateForComparison(date)

        for (let i = 0; i < unavailabilitySlots.length; i++) {
            const slot = unavailabilitySlots[i]
            const startDateStr = normalizeDate(slot.startDate)
            const endDateStr = normalizeDate(slot.endDate)

            // Use string comparison to ensure exact date matching
            if (dateStr >= startDateStr && dateStr <= endDateStr) {
                if (dateStr === startDateStr && dateStr === endDateStr) {
                    return { type: "unavailable", index: i }
                } else if (dateStr === startDateStr) {
                    return { type: "range-start", index: i }
                } else if (dateStr === endDateStr) {
                    return { type: "range-end", index: i }
                } else {
                    return { type: "in-range", index: i }
                }
            }
        }

        return null
    }

    /**
     * Handle day click for selection
     */
    function handleDayClick(date, dayElement) {
        if (!isSelecting) {
            // Start new selection
            startSelection(date, dayElement)
        } else {
            // Complete selection
            completeSelection(date, dayElement)
        }
    }

    /**
     * Start a new date range selection
     */
    function startSelection(date, dayElement) {
        isSelecting = true
        selectionStart = date
        selectionEnd = null
        tempSelection = [dayElement]

        dayElement.classList.add("temp-start")

        updateSelectionInfo()

        // Add mouseover handlers for range preview
        const availableDays = document.querySelectorAll(".calendar-day.available")
        availableDays.forEach((day) => {
            day.addEventListener("mouseover", handleDayHover)
        })
    }

    /**
     * Handle day hover during selection
     */
    function handleDayHover(event) {
        if (!isSelecting || !selectionStart) return

        const hoveredDate = new Date(event.target.dataset.date)
        if (hoveredDate < selectionStart) return

        // Clear previous temp selection
        clearTempSelection()

        // Highlight range from start to hovered date
        const startDate = new Date(selectionStart)
        const endDate = new Date(hoveredDate)

        tempSelection = []
        const d = new Date(startDate)

        // Include the end date in the loop
        while (d <= endDate) {
            const current = new Date(d)
            const dayElement = document.querySelector(`[data-date="${formatDateForComparison(current)}"]`)
            if (dayElement && dayElement.classList.contains("available")) {
                // Add proper temp classes based on position
                if (
                    formatDateForComparison(current) === formatDateForComparison(startDate) &&
                    formatDateForComparison(current) === formatDateForComparison(endDate)
                ) {
                    dayElement.classList.add("temp-single")
                } else if (formatDateForComparison(current) === formatDateForComparison(startDate)) {
                    dayElement.classList.add("temp-start")
                } else if (formatDateForComparison(current) === formatDateForComparison(endDate)) {
                    dayElement.classList.add("temp-end")
                } else {
                    dayElement.classList.add("temp-range")
                }
                tempSelection.push(dayElement)
            }
            d.setDate(d.getDate() + 1)
        }

        // Update selection info
        selectionEnd = hoveredDate
        updateSelectionInfo()
    }

    /**
     * Complete the date range selection
     */
    function completeSelection(date, dayElement) {
        if (date < selectionStart) {
            // Invalid selection, restart
            cancelSelection()
            startSelection(date, dayElement)
            return
        }

        selectionEnd = date

        // Remove hover handlers
        const availableDays = document.querySelectorAll(".calendar-day.available")
        availableDays.forEach((day) => {
            day.removeEventListener("mouseover", handleDayHover)
        })

        // Update temp selection to final state
        clearTempSelection()
        highlightSelectedRange(selectionStart, selectionEnd)

        updateSelectionInfo()
    }

    /**
     * Highlight the selected date range
     */
    function highlightSelectedRange(startDate, endDate) {
        const dayElements = document.querySelectorAll(".calendar-day")
        dayElements.forEach((el) => el.classList.remove("selected"))

        if (startDate && endDate) {
            const startDateStr = formatDateForComparison(startDate)
            const endDateStr = formatDateForComparison(endDate)

            const d = new Date(startDate)
            // Include the end date in the highlighting using string comparison
            while (formatDateForComparison(d) <= endDateStr) {
                const current = new Date(d)
                const dayElement = document.querySelector(`[data-date="${formatDateForComparison(current)}"]`)
                if (dayElement && dayElement.classList.contains("available")) {
                    dayElement.classList.add("selected")
                }
                d.setDate(d.getDate() + 1)
            }
        } else if (startDate) {
            const dayElement = document.querySelector(`[data-date="${formatDateForComparison(startDate)}"]`)
            if (dayElement && dayElement.classList.contains("available")) {
                dayElement.classList.add("selected")
            }
        }
    }

    /**
     * Clear temporary selection highlighting
     */
    function clearTempSelection() {
        tempSelection.forEach((element) => {
            element.classList.remove("temp-start", "temp-end", "temp-range", "temp-single")
        })
        tempSelection = []
    }

    /**
     * Update the selection info display
     */
    function updateSelectionInfo() {
        if (!selectionInfo || !selectionText) return

        if (!selectionStart) {
            selectionInfo.style.display = "none"
            return
        }

        let text
        if (!selectionEnd || selectionStart.getTime() === selectionEnd.getTime()) {
            text = `${formatDateForDisplay(selectionStart)}`
        } else {
            text = `${formatDateForDisplay(selectionStart)} hasta ${formatDateForDisplay(selectionEnd)}`
        }

        selectionText.textContent = text
        selectionInfo.style.display = "block"
    }

    /**
     * Confirm the current selection and add it as unavailability period
     */
    window.confirmSelection = () => {
        if (!selectionStart || !selectionEnd) {
            showError("Please select an end date")
            return
        }

        // Check for overlaps with existing periods
        if (hasOverlapWithExisting(selectionStart, selectionEnd)) {
            showError("Este período se superpone con una indisponibilidad existente")
            return
        }

        // Add new unavailability period with proper string formatting
        const startDateStr = formatDateForSubmission(selectionStart)
        const endDateStr = formatDateForSubmission(selectionEnd)


        const newSlot = {
            startDate: startDateStr,
            endDate: endDateStr,
            index: unavailabilityCounter++,
        }

        unavailabilitySlots.push(newSlot)

        // Update form inputs
        updateFormInputs()

        // Clear selection
        cancelSelection()

        // Re-render calendar
        renderCalendar()

        showSuccess("Unavailability period added successfully")
    }

    /**
     * Cancel the current selection
     */
    function cancelSelection() {
        clearTempSelection()

        // Clear selected highlighting
        const selectedElements = document.querySelectorAll(".calendar-day.selected")
        selectedElements.forEach((el) => el.classList.remove("selected"))

        isSelecting = false
        selectionStart = null
        selectionEnd = null

        if (selectionInfo) {
            selectionInfo.style.display = "none"
        }

        // Remove hover handlers
        const availableDays = document.querySelectorAll(".calendar-day.available")
        availableDays.forEach((day) => {
            day.removeEventListener("mouseover", handleDayHover)
        })
    }

    // Make cancelSelection available globally
    window.cancelSelection = cancelSelection

    /**
     * Check if new period overlaps with existing ones
     */
    function hasOverlapWithExisting(startDate, endDate) {
        return unavailabilitySlots.some((slot) => {
            const existingStart = new Date(normalizeDate(slot.startDate))
            const existingEnd = new Date(normalizeDate(slot.endDate))

            return !(endDate < existingStart || startDate > existingEnd)
        })
    }

    /**
     * Delete an unavailability period - no confirmation needed
     */
    function deleteUnavailabilityPeriod(index) {
        unavailabilitySlots.splice(index, 1)

        // Update indices for remaining slots
        for (let i = index; i < unavailabilitySlots.length; i++) {
            unavailabilitySlots[i].index = i
        }

        // Update form inputs
        updateFormInputs()

        // Re-render calendar
        renderCalendar()

        showSuccess("Unavailability period deleted successfully")
        const warningElement = document.getElementById('trash-warning-unavailability');
        if (warningElement) {
            warningElement.style.display = 'block';
        }
    }

    /**
     * Update form inputs with current unavailability slots
     */
    function updateFormInputs() {
        if (!unavailabilityInputsContainer) return

        // Clear existing inputs
        unavailabilityInputsContainer.innerHTML = ""

        // Add inputs for each slot
        unavailabilitySlots.forEach((slot, index) => {
            // Normalize the dates to ensure correct format
            const startDateValue = normalizeDate(slot.startDate)
            const endDateValue = normalizeDate(slot.endDate)


            const startInput = document.createElement("input")
            startInput.type = "hidden"
            startInput.name = `unavailabilitySlots[${index}].startDate`
            startInput.value = startDateValue
            unavailabilityInputsContainer.appendChild(startInput)

            const endInput = document.createElement("input")
            endInput.type = "hidden"
            endInput.name = `unavailabilitySlots[${index}].endDate`
            endInput.value = endDateValue
            unavailabilityInputsContainer.appendChild(endInput)
        })

        // Log the final HTML to verify
    }

    /**
     * Load unavailability data for a specific month and year
     */
    function loadUnavailabilityData(year, month) {
        showLoading(true)

        const url = `${contextPath}/doctor/dashboard/unavailability/${year}/${month}`

        fetch(url, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "X-Requested-With": "XMLHttpRequest",
            },
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Network response was not ok")
                }
                return response.json()
            })
            .then((data) => {
                // Merge server data with existing data, avoiding duplicates
                const serverSlots = data.unavailabilitySlots || []

                // Filter out slots that are already in our local array
                const newSlots = serverSlots.filter((serverSlot) => {
                    return !unavailabilitySlots.some(
                        (localSlot) =>
                            normalizeDate(localSlot.startDate) === normalizeDate(serverSlot.startDate) &&
                            normalizeDate(localSlot.endDate) === normalizeDate(serverSlot.endDate),
                    )
                })

                // Add new slots with proper indices and normalized dates
                newSlots.forEach((slot) => {
                    unavailabilitySlots.push({
                        startDate: normalizeDate(slot.startDate),
                        endDate: normalizeDate(slot.endDate),
                        index: unavailabilityCounter++,
                    })
                })

                // Re-render calendar with new data
                renderCalendar()
            })
            .catch((error) => {
                // Continue without server data
            })
            .finally(() => {
                showLoading(false)
            })
    }

    /**
     * Update navigation buttons based on current date
     */
    function updateNavigationButtons() {
        const today = new Date()
        const currentMonth = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1)
        const thisMonth = new Date(today.getFullYear(), today.getMonth(), 1)

        // Disable previous button if we're at current month
        if (prevMonthBtn) {
            prevMonthBtn.disabled = currentMonth <= thisMonth
        }

        // You can add logic to disable next button after a certain future date if needed
        // For now, we'll allow unlimited future navigation
        if (nextMonthBtn) {
            nextMonthBtn.disabled = false
        }
    }

    /**
     * Show/hide loading indicator
     */
    function showLoading(show) {
        if (calendarLoading) {
            calendarLoading.style.display = show ? "flex" : "none"
        }
    }

    /**
     * Show success message
     */
    function showSuccess(message) {
        // You can integrate this with your existing toast notification system
        if (typeof showSuccessToast === "function") {
            showSuccessToast()
        }
    }

    /**
     * Show error message
     */
    function showError(message) {
        const errorElement = document.getElementById("unavailability-error")
        if (errorElement) {
            errorElement.textContent = message
            errorElement.style.display = "block"

            // Hide after 5 seconds
            setTimeout(() => {
                errorElement.style.display = "none"
            }, 5000)
        }
    }

    /**
     * Format date for comparison (YYYY-MM-DD)
     */
    function formatDateForComparison(date) {
        // Ensure we have a valid Date object
        if (!(date instanceof Date) || isNaN(date.getTime())) {
            return ""
        }

        const year = date.getFullYear()
        const month = String(date.getMonth() + 1).padStart(2, "0")
        const day = String(date.getDate()).padStart(2, "0")

        return `${year}-${month}-${day}`
    }

    /**
     * Format date for form submission (YYYY-MM-DD format for LocalDate)
     */
    function formatDateForSubmission(date) {
        // Ensure we have a valid Date object
        if (!(date instanceof Date) || isNaN(date.getTime())) {
            return ""
        }

        const year = date.getFullYear()
        const month = String(date.getMonth() + 1).padStart(2, "0")
        const day = String(date.getDate()).padStart(2, "0")

        const formatted = `${year}-${month}-${day}`
        return formatted
    }

    /**
     * Format date for display (localized)
     */
    function formatDateForDisplay(date) {
        if (!(date instanceof Date) || isNaN(date.getTime())) {
            return ""
        }

        const day = date.getDate()
        const month = monthNames[date.getMonth()]
        const year = date.getFullYear()

        return `${day} ${month} ${year}`
    }
})
