/**
 * Date and Time Picker for Appointment Booking
 *
 * This script handles the custom date and time picker functionality
 * for the appointment booking form.
 */
document.addEventListener("DOMContentLoaded", () => {
    // DOM Elements
    const datePickerInput = document.getElementById("datePickerInput")
    const datePickerCalendar = document.getElementById("datePickerCalendar")
    const prevMonthBtn = document.getElementById("prevMonthBtn")
    const nextMonthBtn = document.getElementById("nextMonthBtn")
    const currentMonthYear = document.getElementById("currentMonthYear")
    const calendarWeekdays = document.getElementById("calendarWeekdays")
    const calendarDays = document.getElementById("calendarDays")
    const timeSlotsContainer = document.getElementById("timeSlotsContainer")
    const timeSlots = document.getElementById("timeSlots")
    const appointmentSummary = document.getElementById("appointmentSummary")
    const appointmentSummaryText = document.getElementById("appointmentSummaryText")
    const appointmentDateInput = document.getElementById("appointmentDate")
    const appointmentHourInput = document.getElementById("appointmentHour")
    prevMonthBtn.style.visibility = "hidden";

    // Get messages from the global object created in the JSP
    const messages = window.appointmentMessages || {
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
        appointmentAt: "at",
        noAvailableSlots: "No available time slots for this date",
    }

    // State variables
    const argDate = new Date().toLocaleString("en-US", {
        timeZone: "America/Argentina/Buenos_Aires",
    })
    const currentDate = new Date(argDate)
    let selectedDate = null
    let selectedTimeSlot = null

    let fullyBookedDates = [];

    // // Fetch fully booked dates for the next month on page load
    // fetchFullyBookedDates().then((dates) => { //TODO implement this correctly for future sprint
    //     fullyBookedDates = dates;
    //     initDatePicker(); // Initialize the date picker after fetching the dates
    // });

    initDatePicker();

    /**
     * Initialize the date picker component
     */
    function initDatePicker() {
        // Initialize calendar
        renderCalendarHeader()
        renderWeekdays()
        renderCalendarDays()

        // Event listeners
        datePickerInput.addEventListener("click", toggleCalendar)
        prevMonthBtn.addEventListener("click", goToPreviousMonth)
        nextMonthBtn.addEventListener("click", goToNextMonth)

        // Close calendar when clicking outside
        document.addEventListener("click", (event) => {
            if (!datePickerCalendar.contains(event.target) && event.target !== datePickerInput) {
                datePickerCalendar.style.display = "none"
            }
        })
    }

    /**
     * Toggle the calendar visibility
     */
    function toggleCalendar() {
        if (datePickerCalendar.style.display === "block") {
            datePickerCalendar.style.display = "none"
        } else {
            datePickerCalendar.style.display = "block"
            renderCalendarDays() // Re-render days when opening
        }
    }

    /**
     * Render the calendar header with month and year
     */
    function renderCalendarHeader() {
        currentMonthYear.textContent = `${messages.months[currentDate.getMonth()]} ${currentDate.getFullYear()}`
    }

    /**
     * Render the weekday headers
     */
    function renderWeekdays() {
        calendarWeekdays.innerHTML = ""
        messages.weekdaysShort.forEach((day) => {
            const dayElement = document.createElement("div")
            dayElement.className = "date-picker-weekday"
            dayElement.textContent = day
            calendarWeekdays.appendChild(dayElement)
        })
    }

    // /**
    //  * Fetch fully booked dates from today to one month in advance
    //  * @returns {Promise<Array>} A promise that resolves to an array of fully booked dates
    //  */
    // function fetchFullyBookedDates() {
    //     const newDate = new Date().toLocaleString("en-US", {
    //         timeZone: "America/Argentina/Buenos_Aires",
    //     })
    //     const today = new Date(newDate);
    //     const startDate = formatDateForSubmission(today);
    //     const endDate = formatDateForSubmission(new Date(today.setMonth(today.getMonth() + 1)));
    //
    //     const url = `/appointment/fully-booked-dates?doctorId=${appointmentForm.doctorId.value}&startDate=${startDate}&endDate=${endDate}`;
    //
    //     console.log("enter")
    //
    //     return fetch(url)
    //         .then((response) => {
    //             if (!response.ok) {
    //                 throw new Error("Failed to fetch fully booked dates");
    //             }
    //             return response.json();
    //         })
    //         .then((data) => {
    //             console.log("YAY: " + data.fullyBookedDates);
    //             return data.fullyBookedDates || [];
    //         })
    //         .catch((error) => {
    //             console.error("Error fetching fully booked dates:", error);
    //             return [];
    //         });
    // }

    /**
     * Render the calendar days for the current month
     */
    function renderCalendarDays() {
        calendarDays.innerHTML = "";

        const year = currentDate.getFullYear();
        const month = currentDate.getMonth();

        // First and last day of the month
        const firstDay = new Date(year, month, 1);
        const lastDay = new Date(year, month + 1, 0);

        // Get the day of the week for the first day (0-6)
        const firstDayOfWeek = firstDay.getDay();

        const argDate = new Date().toLocaleString("en-US", {
            timeZone: "America/Argentina/Buenos_Aires",
        });
        const today = new Date(argDate);
        const currentHour = today.getHours();
        const lastAvailableSlot = 18; // Example: last slot is 6:00 PM
        const minDate = currentHour >= lastAvailableSlot ? new Date(today.setDate(today.getDate() + 1)) : today;

        // Create empty cells for days before the first day of the month
        for (let i = 0; i < firstDayOfWeek; i++) {
            const emptyDay = document.createElement("div");
            emptyDay.className = "date-picker-day empty";
            calendarDays.appendChild(emptyDay);
        }

        // Create cells for each day of the month
        for (let day = 1; day <= lastDay.getDate(); day++) {
            const date = new Date(year, month, day);
            const formattedDate = formatDateForSubmission(date);

            const dayElement = document.createElement("div");
            dayElement.className = "date-picker-day";
            dayElement.textContent = day;

            // Disable past dates
            if (date < minDate.setHours(0, 0, 0, 0)) {
                dayElement.classList.add("disabled");
            }
            // Disable fully booked dates
            else if (fullyBookedDates.includes(formattedDate)) {
                dayElement.classList.add("disabled");
            }
            // Enable available dates
            else {
                dayElement.classList.add("available");

                // Check if this is the selected date
                if (
                    selectedDate &&
                    date.getDate() === selectedDate.getDate() &&
                    date.getMonth() === selectedDate.getMonth() &&
                    date.getFullYear() === selectedDate.getFullYear()
                ) {
                    dayElement.classList.add("selected");
                }

                // Add click event to select date
                dayElement.addEventListener("click", () => {
                    selectDate(date);
                });
            }

            calendarDays.appendChild(dayElement);
        }
    }

    /**
     * Go to the next month
     */
    function goToNextMonth() {
        const today = new Date();
        const nextMonth = new Date(today.getFullYear(), today.getMonth() + 1, 1);

        if (
            currentDate.getFullYear() === nextMonth.getFullYear() &&
            currentDate.getMonth() === nextMonth.getMonth()
        ) {
            return; // Prevent going beyond the next month
        }

        currentDate.setMonth(currentDate.getMonth() + 1);
        renderCalendarHeader();
        renderCalendarDays();

        // Hide the "Next Month" button if the current month is the last allowed month
        if (
            currentDate.getFullYear() === nextMonth.getFullYear() &&
            currentDate.getMonth() === nextMonth.getMonth()
        ) {
            nextMonthBtn.style.visibility = "hidden";
        }

        // Ensure the "Previous Month" button is visible
        prevMonthBtn.style.visibility = "visible";
    }

    /**
     * Go to the previous month
     */
    function goToPreviousMonth() {
        const today = new Date();
        if (
            currentDate.getFullYear() === today.getFullYear() &&
            currentDate.getMonth() === today.getMonth()
        ) {
            return; // Prevent going to a previous month
        }

        currentDate.setMonth(currentDate.getMonth() - 1);
        renderCalendarHeader();
        renderCalendarDays();

        // Show the "Next Month" button when navigating back
        nextMonthBtn.style.visibility = "visible";

        // Hide the "Previous Month" button if the current month is the first allowed month
        if (
            currentDate.getFullYear() === today.getFullYear() &&
            currentDate.getMonth() === today.getMonth()
        ) {
            prevMonthBtn.style.visibility = "hidden";
        }
    }

    /**
     * Select a date and show available time slots
     * @param {Date} date - The selected date
     */
    function selectDate(date) {
        // Update selected date
        selectedDate = date

        // Format date for display
        const formattedDate = formatDate(date)

        // Update input field
        datePickerInput.value = formattedDate

        // Update hidden input
        appointmentDateInput.value = formatDateForSubmission(date)

        // Hide calendar
        datePickerCalendar.style.display = "none"

        // Reset time slot selection
        selectedTimeSlot = null
        appointmentHourInput.value = ""

        // Show time slots
        renderTimeSlots(date)
        timeSlotsContainer.style.display = "block"

        // Hide appointment summary until time is selected
        appointmentSummary.classList.add("hidden")
    }

    /**
     * Format date for display (e.g., "Monday, January 1, 2023")
     * @param {Date} date - The date to format
     * @returns {string} Formatted date string
     */
    function formatDate(date) {
        const dayOfWeek = messages.weekdays[date.getDay()]
        const month = messages.months[date.getMonth()]
        const day = date.getDate()
        const year = date.getFullYear()

        return `${dayOfWeek}, ${month} ${day}, ${year}`
    }

    /**
     * Format date for form submission (e.g., "2023-01-01")
     * @param {Date} date - The date to format
     * @returns {string} Formatted date string for submission
     */
    function formatDateForSubmission(date) {
        const year = date.getFullYear()
        const month = String(date.getMonth() + 1).padStart(2, "0")
        const day = String(date.getDate()).padStart(2, "0")

        return `${year}-${month}-${day}`
    }

    /**
     * Fetch available time slots for a given date
     * @param {Date} date - The date to fetch time slots for
     * @returns {Promise<Array>} A promise that resolves to an array of available time slots
     */
    function getBookedTimeSlots(date) {
        const formattedDate = formatDateForSubmission(date);

        return fetch(`/appointment/available-hours?doctorId=${appointmentForm.doctorId.value}&date=${formattedDate}`)
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Failed to fetch available time slots");
                }
                return response.json();
            })
            .then((data) => {
               return data.bookedHours || [];
            });
    }

    /**
     * Render available time slots for the selected date
     * @param {Date} date - The selected date
     */
    function renderTimeSlots(date) {
        timeSlots.innerHTML = "";

        const argDate = new Date().toLocaleString("en-US", {
            timeZone: "America/Argentina/Buenos_Aires",
        });
        const currentHour = new Date(argDate).getHours();

        const allSlots = Array.from({ length: 11 }, (_, i) => `${8 + i}`);

        getBookedTimeSlots(date)
            .then((unavailableSlots) => {
                allSlots.forEach((slot) => {
                    const slotHour = parseInt(slot, 10);
                    const timeSlotButton = document.createElement("button");
                    timeSlotButton.type = "button";
                    timeSlotButton.className = "time-slot-btn";
                    timeSlotButton.textContent = slot;

                    if ((isToday(date) && slotHour <= currentHour) || unavailableSlots.includes(slot)) {
                        timeSlotButton.disabled = true;
                        timeSlotButton.classList.add("disabled");
                    } else {
                        timeSlotButton.addEventListener("click", () => {
                            selectTimeSlot(slot);
                            document.querySelectorAll(".time-slot-btn").forEach((btn) => {
                                btn.classList.remove("selected");
                            });
                            timeSlotButton.classList.add("selected");
                        });
                    }

                    timeSlots.appendChild(timeSlotButton);
                });
            })
            .catch((error) => {
                console.error("Error fetching time slots:", error);
                const message = document.createElement("div");
                message.className = "error-message";
                message.textContent = "Failed to load time slots. Please try again later.";
                timeSlots.appendChild(message);
            });
    }

    /**
     * Check if the given date is today
     * @param {Date} date - The date to check
     * @returns {boolean} True if the date is today, false otherwise
     */
    function isToday(date) {
        const argDate = new Date().toLocaleString("en-US", {
            timeZone: "America/Argentina/Buenos_Aires",
        });
        const today = new Date(argDate);

        return (
            date.getDate() === today.getDate() &&
            date.getMonth() === today.getMonth() &&
            date.getFullYear() === today.getFullYear()
        );
    }

    /**
     * Select a time slot and update the appointment summary
     * @param {string} timeSlot - The selected time slot
     */
    function selectTimeSlot(timeSlot) {
        selectedTimeSlot = timeSlot

        // Update hidden input
        appointmentHourInput.value = timeSlot

        // Update and show appointment summary
        updateAppointmentSummary()
        appointmentSummary.classList.remove("hidden")
    }

    /**
     * Update the appointment summary text
     */
    function updateAppointmentSummary() {
        if (selectedDate && selectedTimeSlot) {
            const formattedDate = formatDate(selectedDate)
            appointmentSummaryText.textContent = `${formattedDate} ${messages.appointmentAt} ${selectedTimeSlot}:00`
        }
    }
})
