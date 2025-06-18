/**
 * Multiple Date Picker for Forms
 *
 * This script handles multiple custom date picker instances
 * for forms with multiple date inputs.
 */

class DatePicker {
    constructor(container) {
        this.container = container
        this.input = container.querySelector(".date-picker-input")
        this.calendar = null
        this.selectedDate = null
        this.showingYearSelector = false

        // Get messages from the global object or use defaults
        this.messages = window.appointmentMessages || {
            months: [
                "January",
                "February",
                "March",
                "April",
                "May",
                "June",
                "July",
                "August",
                "September",
                "October",
                "November",
                "December",
            ],
            weekdays: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"],
            weekdaysShort: ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"],
        }

        // Set current date to Argentina timezone
        const argDate = new Date().toLocaleString("en-US", {
            timeZone: "America/Argentina/Buenos_Aires",
        })
        this.currentDate = new Date(argDate)
        this.displayDate = new Date(this.currentDate)

        this.init()
    }

    init() {
        this.createCalendar()
        this.setupEventListeners()

        // If input has a value, parse and set it
        if (this.input.value) {
            this.setDateFromValue(this.input.value)
        }
    }

    createCalendar() {
        // Create calendar structure
        this.calendar = document.createElement("div")
        this.calendar.className = "date-picker-calendar"
        this.calendar.style.display = "none"

        this.calendar.innerHTML = `
            <div class="date-picker-header">
                <button type="button" class="date-picker-nav prev-month">&lsaquo;</button>
                <div class="date-picker-month-year">
                    <span class="month-display"></span>
                    <span class="year-display"></span>
                </div>
                <button type="button" class="date-picker-nav next-month">&rsaquo;</button>
            </div>
            <div class="date-picker-year-selector" style="display: none;">
                <div class="year-selector-header">
                    <button type="button" class="year-nav prev-years">&lsaquo;</button>
                    <span class="year-range"></span>
                    <button type="button" class="year-nav next-years">&rsaquo;</button>
                </div>
                <div class="year-grid"></div>
            </div>
            <div class="date-picker-weekdays"></div>
            <div class="date-picker-days"></div>
        `

        // Insert calendar after the input
        this.input.parentNode.insertBefore(this.calendar, this.input.nextSibling)

        // Get calendar elements
        this.monthYearDisplay = this.calendar.querySelector(".date-picker-month-year")
        this.monthDisplay = this.calendar.querySelector(".month-display")
        this.yearDisplay = this.calendar.querySelector(".year-display")
        this.weekdaysContainer = this.calendar.querySelector(".date-picker-weekdays")
        this.daysContainer = this.calendar.querySelector(".date-picker-days")
        this.prevButton = this.calendar.querySelector(".prev-month")
        this.nextButton = this.calendar.querySelector(".next-month")

        // Year selector elements
        this.yearSelector = this.calendar.querySelector(".date-picker-year-selector")
        this.yearGrid = this.calendar.querySelector(".year-grid")
        this.yearRange = this.calendar.querySelector(".year-range")
        this.prevYearsButton = this.calendar.querySelector(".prev-years")
        this.nextYearsButton = this.calendar.querySelector(".next-years")

        this.renderCalendar()
    }

    setupEventListeners() {
        // Input click to toggle calendar
        this.input.addEventListener("click", (e) => {
            e.stopPropagation()
            this.toggleCalendar()
        })

        // Navigation buttons
        this.prevButton.addEventListener("click", (e) => {
            e.stopPropagation()
            this.goToPreviousMonth()
        })

        this.nextButton.addEventListener("click", (e) => {
            e.stopPropagation()
            this.goToNextMonth()
        })

        // Year display click to show year selector
        this.yearDisplay.addEventListener("click", (e) => {
            e.stopPropagation()
            this.toggleYearSelector()
        })

        // Year selector navigation
        this.prevYearsButton.addEventListener("click", (e) => {
            e.stopPropagation()
            this.goToPreviousYears()
        })

        this.nextYearsButton.addEventListener("click", (e) => {
            e.stopPropagation()
            this.goToNextYears()
        })

        // Close calendar when clicking outside
        document.addEventListener("click", (e) => {
            if (!this.container.contains(e.target)) {
                this.hideCalendar()
            }
        })
    }

    toggleCalendar() {
        if (this.calendar.style.display === "block") {
            this.hideCalendar()
        } else {
            this.showCalendar()
        }
    }

    showCalendar() {
        // Hide all other calendars first
        document.querySelectorAll(".date-picker-calendar").forEach((cal) => {
            if (cal !== this.calendar) {
                cal.style.display = "none"
            }
        })

        this.calendar.style.display = "block"
        this.showingYearSelector = false
        this.renderCalendar()
    }

    hideCalendar() {
        this.calendar.style.display = "none"
        this.showingYearSelector = false
    }

    toggleYearSelector() {
        this.showingYearSelector = !this.showingYearSelector
        if (this.showingYearSelector) {
            this.renderYearSelector()
        }
        this.updateCalendarView()
    }

    updateCalendarView() {
        if (this.showingYearSelector) {
            this.yearSelector.style.display = "block"
            this.weekdaysContainer.style.display = "none"
            this.daysContainer.style.display = "none"
        } else {
            this.yearSelector.style.display = "none"
            this.weekdaysContainer.style.display = "grid"
            this.daysContainer.style.display = "grid"
        }
    }

    renderCalendar() {
        this.renderHeader()
        if (!this.showingYearSelector) {
            this.renderWeekdays()
            this.renderDays()
        }
        this.updateCalendarView()
    }

    renderHeader() {
        this.monthDisplay.textContent = this.messages.months[this.displayDate.getMonth()]
        this.yearDisplay.textContent = this.displayDate.getFullYear()
    }

    renderYearSelector() {
        const currentYear = this.displayDate.getFullYear()
        const startYear = Math.floor(currentYear / 12) * 12
        const endYear = startYear + 11

        this.yearRange.textContent = `${startYear} - ${endYear}`
        this.yearGrid.innerHTML = ""

        for (let year = startYear; year <= endYear; year++) {
            const yearElement = document.createElement("div")
            yearElement.className = "year-item"
            yearElement.textContent = year

            if (year === currentYear) {
                yearElement.classList.add("current-year")
            }

            if (this.selectedDate && year === this.selectedDate.getFullYear()) {
                yearElement.classList.add("selected-year")
            }

            yearElement.addEventListener("click", () => {
                this.selectYear(year)
            })

            this.yearGrid.appendChild(yearElement)
        }
    }

    selectYear(year) {
        this.displayDate.setFullYear(year)
        this.showingYearSelector = false
        this.renderCalendar()
    }

    goToPreviousYears() {
        const currentYear = this.displayDate.getFullYear()
        this.displayDate.setFullYear(currentYear - 12)
        this.renderYearSelector()
    }

    goToNextYears() {
        const currentYear = this.displayDate.getFullYear()
        this.displayDate.setFullYear(currentYear + 12)
        this.renderYearSelector()
    }

    renderWeekdays() {
        this.weekdaysContainer.innerHTML = ""
        this.messages.weekdaysShort.forEach((day) => {
            const dayElement = document.createElement("div")
            dayElement.className = "date-picker-weekday"
            dayElement.textContent = day
            this.weekdaysContainer.appendChild(dayElement)
        })
    }

    renderDays() {
        this.daysContainer.innerHTML = ""

        const year = this.displayDate.getFullYear()
        const month = this.displayDate.getMonth()

        // First and last day of the month
        const firstDay = new Date(year, month, 1)
        const lastDay = new Date(year, month + 1, 0)
        const firstDayOfWeek = firstDay.getDay()

        // Create empty cells for days before the first day of the month
        for (let i = 0; i < firstDayOfWeek; i++) {
            const emptyDay = document.createElement("div")
            emptyDay.className = "date-picker-day empty"
            this.daysContainer.appendChild(emptyDay)
        }

        // Create cells for each day of the month
        for (let day = 1; day <= lastDay.getDate(); day++) {
            const date = new Date(year, month, day)
            const dayElement = document.createElement("div")
            dayElement.className = "date-picker-day"
            dayElement.textContent = day

            // Check if this is the selected date
            if (this.selectedDate && this.isSameDate(date, this.selectedDate)) {
                dayElement.classList.add("selected")
            }

            // Allow all dates for profile forms
            dayElement.classList.add("available")
            dayElement.addEventListener("click", () => {
                this.selectDate(date)
            })

            this.daysContainer.appendChild(dayElement)
        }
    }

    goToNextMonth() {
        this.displayDate.setMonth(this.displayDate.getMonth() + 1)
        this.renderCalendar()
    }

    goToPreviousMonth() {
        this.displayDate.setMonth(this.displayDate.getMonth() - 1)
        this.renderCalendar()
    }

    selectDate(date) {
        this.selectedDate = new Date(date)

        // Format date for display and input
        const formattedDate = this.formatDateForDisplay(date)
        const formattedDateForSubmission = this.formatDateForSubmission(date)

        // Update input value
        this.input.value = formattedDateForSubmission

        // Trigger change event
        this.input.dispatchEvent(new Event("change", { bubbles: true }))

        // Hide calendar
        this.hideCalendar()

        // Re-render to show selection
        this.renderCalendar()
    }

    setDateFromValue(dateString) {
        if (!dateString) return

        try {
            // Try to parse the date string
            const date = new Date(dateString)
            if (!isNaN(date.getTime())) {
                this.selectedDate = date
                this.displayDate = new Date(date)
            }
        } catch (e) {
            console.warn("Could not parse date:", dateString)
        }
    }

    formatDateForDisplay(date) {
        const dayOfWeek = this.messages.weekdays[date.getDay()]
        const month = this.messages.months[date.getMonth()]
        const day = date.getDate()
        const year = date.getFullYear()

        return `${dayOfWeek}, ${month} ${day}, ${year}`
    }

    formatDateForSubmission(date) {
        const year = date.getFullYear()
        const month = String(date.getMonth() + 1).padStart(2, "0")
        const day = String(date.getDate()).padStart(2, "0")

        return `${year}-${month}-${day}`
    }

    isSameDate(date1, date2) {
        return (
            date1.getDate() === date2.getDate() &&
            date1.getMonth() === date2.getMonth() &&
            date1.getFullYear() === date2.getFullYear()
        )
    }
}

// Initialize all date pickers when DOM is loaded
document.addEventListener("DOMContentLoaded", () => {
    initializeDatePickers()
})

// Function to initialize all date pickers
function initializeDatePickers() {
    const datePickerContainers = document.querySelectorAll(".date-picker-container")

    datePickerContainers.forEach((container) => {
        // Check if already initialized
        if (!container.datePicker) {
            container.datePicker = new DatePicker(container)
        }
    })
}

// Function to initialize date pickers for dynamically added content
function initializeDatePickersForContainer(container) {
    const datePickerContainers = container.querySelectorAll(".date-picker-container")

    datePickerContainers.forEach((pickerContainer) => {
        if (!pickerContainer.datePicker) {
            pickerContainer.datePicker = new DatePicker(pickerContainer)
        }
    })
}

// Export functions for global use
window.initializeDatePickers = initializeDatePickers
window.initializeDatePickersForContainer = initializeDatePickersForContainer
