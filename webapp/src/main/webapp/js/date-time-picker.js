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


    fetchOfficesAvailability();

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
     * Fetch the availability for all the offices
     */
    let officeAvailability = [];
    function fetchOfficesAvailability() {
        const url = `${contextPath}/appointment/doctor/${doctorId}/availability`;
        fetch(url)
            .then(response => response.json())
            .then(data => {
                officeAvailability = data || [];
                initDatePicker();
            })
            .catch(error => {
                console.error("Error fetching availability:", error);
            });
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
            dayElement.textContent = day.toString()
            calendarWeekdays.appendChild(dayElement)
        })
    }

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
        const lastAvailableSlot = 20; // Example: last slot is 6:00 PM
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
            dayElement.textContent = day.toString();

            let available = officeAvailability[currentOfficeId]?.filter(slot => slot.dayOfWeek.toString() === ((date.getDay() + 6) % 7).toString());
            let flag = true;

            const isUnavailable = unavailabilitySlots.some(slot => {
                const startDate = new Date(slot.startDate);
                const endDate = new Date(slot.endDate);
                return date >= startDate && date < new Date(endDate.setDate(endDate.getDate() + 1));
            });

            if(isUnavailable){
                dayElement.classList.add("disabled");
            }
            else if (available !== undefined && available.length > 0) {
                const totalSlots = available.reduce((sum, slot) => sum + slot.slots, 0);
                // flag = FutureAppointments.some(entry => entry.hours.length === totalSlots && entry.date === formattedDate);
                if (isToday(date)) {
                    const passedSlots = available.reduce((sum, slot) => {
                        const passed = Math.max(0, currentHour - slot.startTime + 1);
                        const slots = slot.endTime.hour - slot.startTime.hour + 1;
                        return sum + Math.min(passed, slots);
                    }, 0);

                    flag = FutureAppointments.some(entry =>
                        entry.date === formattedDate &&
                        entry.hours.filter(hour => hour > currentHour).length + passedSlots === totalSlots
                    ) || passedSlots === totalSlots;
                } else {
                    flag = FutureAppointments.some(entry =>
                        entry.date === formattedDate &&
                        entry.hours.length === totalSlots
                    );
                }
            }

            // Disable past dates
            if (date < minDate.setHours(0, 0, 0, 0) || flag) {
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
        // Update input field
        datePickerInput.value = formatDate(date)

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
     * Format date for display (e.g., "Sunday, January 1, 2023")
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
     * Render available time slots for the selected date
     * @param {Date} date - The selected date
     */
    function renderTimeSlots(date) {
        timeSlots.innerHTML = "";

        const argDate = new Date().toLocaleString("en-US", {
            timeZone: "America/Argentina/Buenos_Aires",
        });
        const currentHour = new Date(argDate).getHours();

        const allSlots = Array.from({ length: 13 }, (_, i) => `${8 + i}`);

        let available = officeAvailability[currentOfficeId]?.filter(slot => slot.dayOfWeek.toString() === ((date.getDay() + 6) % 7).toString()); //js getDate works in mysterious ways, 0 is Sunday and 1 is Mondays. I have saved you all from debugging hell
        // Find the fully booked hours for the selected date
        const formattedDate = formatDateForSubmission(date);
        const fullyBookedEntry = FutureAppointments.find(entry => entry.date === formattedDate);
        const unavailableSlots = fullyBookedEntry ? fullyBookedEntry.hours : [];


        if (available.length === 0) {
            const noSlotsMessage = document.createElement("div");
            noSlotsMessage.className = "no-slots-message";
            noSlotsMessage.textContent = messages.noAvailableSlots;
            timeSlots.appendChild(noSlotsMessage);
            return;
        }

        allSlots.forEach((slot) => {
            const slotHour = parseInt(slot, 10);
            const timeSlotButton = document.createElement("button");
            timeSlotButton.type = "button";
            timeSlotButton.className = "time-slot-btn";
            timeSlotButton.textContent = slot + ":00";

            if ((isToday(date) && slotHour <= currentHour) || unavailableSlots.includes(slotHour)) {
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
            if (available.find(slot => slot.startTime <= slotHour && slot.endTime >= slotHour)) {
                timeSlots.appendChild(timeSlotButton);
            }
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
