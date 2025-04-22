document.addEventListener("DOMContentLoaded", () => {
    // Initialize date pickers for each doctor
    const doctorCards = document.querySelectorAll('.doctor-card');

    doctorCards.forEach(card => {
        const doctorId = card.querySelector('.btn-appointment').href.split('doctorId=')[1].split('&')[0];
        initializeDatePicker(doctorId);
    });

    /**
     * Initialize date picker for a specific doctor
     * @param {string} doctorId - The doctor's ID
     */
    function initializeDatePicker(doctorId) {
        // DOM Elements
        const datePickerInput = document.getElementById(`datePickerInput-${doctorId}`);
        const datePickerCalendar = document.getElementById(`datePickerCalendar-${doctorId}`);
        const prevMonthBtn = document.getElementById(`prevMonthBtn-${doctorId}`);
        const nextMonthBtn = document.getElementById(`nextMonthBtn-${doctorId}`);
        const currentMonthYear = document.getElementById(`currentMonthYear-${doctorId}`);
        const calendarWeekdays = document.getElementById(`calendarWeekdays-${doctorId}`);
        const calendarDays = document.getElementById(`calendarDays-${doctorId}`);
        const timeSlotsContainer = document.getElementById(`timeSlotsContainer-${doctorId}`);
        const timeSlots = document.getElementById(`timeSlots-${doctorId}`);

        // Hide previous month button initially
        prevMonthBtn.style.visibility = "hidden";

        // Get messages from the global object created in the JSP
        const messages = window.appointmentMessages || {
            months: [
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
            ],
            weekdays: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"],
            weekdaysShort: ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"],
            noAvailableSlots: "No available time slots for this date"
        };

        // State variables
        const argDate = new Date().toLocaleString("en-US", {
            timeZone: "America/Argentina/Buenos_Aires"
        });
        const currentDate = new Date(argDate);
        let selectedDate = null;
        let doctorAvailability = [];

        // Get doctor's availability slots from the global object
        const doctorAvailabilitySlots = window.doctorAvailabilitySlots ? window.doctorAvailabilitySlots[doctorId] || [] : [];

        // Declare contextPath (assuming it's defined globally or in the JSP)
        const contextPath = window.contextPath || ""; // Provide a default value if not available

        // Move calendar to body for better positioning
        document.body.appendChild(datePickerCalendar);

        // Fetch doctor's availability for the next month
        fetchDoctorAvailability(doctorId).then(availability => {
            doctorAvailability = availability;
            initDatePicker();
        });

        /**
         * Initialize the date picker component
         */
        function initDatePicker() {
            // Initialize calendar
            renderCalendarHeader();
            renderWeekdays();
            renderCalendarDays();

            // Event listeners
            datePickerInput.addEventListener("click", toggleCalendar);
            prevMonthBtn.addEventListener("click", goToPreviousMonth);
            nextMonthBtn.addEventListener("click", goToNextMonth);

            // Close calendar when clicking outside
            document.addEventListener("click", (event) => {
                if (!datePickerCalendar.contains(event.target) && event.target !== datePickerInput) {
                    datePickerCalendar.style.display = "none";
                }
            });
        }

        /**
         * Toggle the calendar visibility
         */
        function toggleCalendar() {
            if (datePickerCalendar.style.display === "block") {
                datePickerCalendar.style.display = "none";
            } else {
                // Close all other calendars first
                document.querySelectorAll('.date-picker-calendar').forEach(calendar => {
                    calendar.style.display = "none";
                });

                // Position the calendar below the input
                positionCalendar();

                datePickerCalendar.style.display = "block";
                renderCalendarDays(); // Re-render days when opening
            }
        }

        /**
         * Position the calendar below the input field
         */
        function positionCalendar() {
            const inputRect = datePickerInput.getBoundingClientRect();
            const scrollTop = window.pageYOffset || document.documentElement.scrollTop;
            const scrollLeft = window.pageXOffset || document.documentElement.scrollLeft;

            datePickerCalendar.style.position = "absolute";
            datePickerCalendar.style.top = (inputRect.bottom + scrollTop) + "px";
            datePickerCalendar.style.left = (inputRect.left + scrollLeft) + "px";
            datePickerCalendar.style.width = inputRect.width + "px";
            datePickerCalendar.style.zIndex = "1050"; // Higher z-index to appear above other elements
        }

        /**
         * Render the calendar header with month and year
         */
        function renderCalendarHeader() {
            currentMonthYear.textContent = `${messages.months[currentDate.getMonth()]} ${currentDate.getFullYear()}`;
        }

        /**
         * Render the weekday headers
         */
        function renderWeekdays() {
            calendarWeekdays.innerHTML = "";
            messages.weekdaysShort.forEach((day) => {
                const dayElement = document.createElement("div");
                dayElement.className = "date-picker-weekday";
                dayElement.textContent = day;
                calendarWeekdays.appendChild(dayElement);
            });
        }

        /**
         * Fetch doctor's availability
         * @param {string} doctorId - The doctor's ID
         * @returns {Promise<Array>} A promise that resolves to an array of availability data
         */
        function fetchDoctorAvailability(doctorId) {
            const url = `${contextPath}/appointment/booked-times-by-date?doctorId=${doctorId}`;

            return fetch(url)
                .then(response => {
                    if (!response.ok) {
                        throw new Error("Failed to fetch doctor availability");
                    }
                    return response.json();
                })
                .then(data => {
                    return data || [];
                })
                .catch(error => {
                    console.error("Error fetching doctor availability:", error);
                    return [];
                });
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
                timeZone: "America/Argentina/Buenos_Aires"
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

                let available = doctorAvailabilitySlots.find(slot => slot.dayOfWeek == ((date.getDay() - 1) % 7));
                let flag = true;

                if (available !== undefined) {
                    flag = doctorAvailability.some(entry => entry.date === formattedDate && entry.hours.length === available.slots);
                }


                // Disable past dates and fully booked dates
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
            selectedDate = date;

            // Format date for display
            const formattedDate = formatDate(date);

            // Update input field
            datePickerInput.value = formattedDate;

            // Hide calendar
            datePickerCalendar.style.display = "none";

            // Show time slots
            renderTimeSlots(date);
            timeSlotsContainer.style.display = "block";
        }

        /**
         * Format date for display (e.g., "Monday, January 1, 2023")
         * @param {Date} date - The date to format
         * @returns {string} Formatted date string
         */
        function formatDate(date) {
            const dayOfWeek = messages.weekdays[date.getDay()];
            const month = messages.months[date.getMonth()];
            const day = date.getDate();
            const year = date.getFullYear();

            return `${dayOfWeek}, ${month} ${day}, ${year}`;
        }

        /**
         * Format date for form submission (e.g., "2023-01-01")
         * @param {Date} date - The date to format
         * @returns {string} Formatted date string for submission
         */
        function formatDateForSubmission(date) {
            const year = date.getFullYear();
            const month = String(date.getMonth() + 1).padStart(2, "0");
            const day = String(date.getDate()).padStart(2, "0");

            return `${year}-${month}-${day}`;
        }

        /**
         * Render available time slots for the selected date
         * @param {Date} date - The selected date
         */
        function renderTimeSlots(date) {
            timeSlots.innerHTML = "";
            const noSlotsMessages = document.getElementsByClassName("no-slots-message");
            Array.from(noSlotsMessages).forEach(message => {
                if (timeSlotsContainer.contains(message)) {
                    timeSlotsContainer.removeChild(message);
                }
            });
            const argDate = new Date().toLocaleString("en-US", {
                timeZone: "America/Argentina/Buenos_Aires"
            });
            const currentHour = new Date(argDate).getHours();

            // Get the day of week (0-6, where 0 is Sunday)
            const dayOfWeek = date.getDay();

            const availableSlots = doctorAvailabilitySlots.filter(slot => slot.dayOfWeek == ((date.getDay() - 1) % 7)); //js getDate works in mysterious ways, 0 is Sunday and 1 is Mondays. I have saved you all from debugging hell

            // Find the booked hours for the selected date
            const formattedDate = formatDateForSubmission(date);
            const bookedEntry = doctorAvailability.find(entry => entry.date === formattedDate);
            const bookedHours = bookedEntry ? bookedEntry.hours : [];

            if (availableSlots.length === 0) {
                const noSlotsMessage = document.createElement("div");
                noSlotsMessage.className = "no-slots-message";
                noSlotsMessage.textContent = messages.noAvailableSlots;
                timeSlotsContainer.appendChild(noSlotsMessage);
                return;
            }



            // Create time slot buttons for each available slot
            availableSlots.forEach(slot => {
                // Generate all hours between start and end time
                for (let hour = slot.startTime; hour <= slot.endTime; hour++) {
                    const timeSlotButton = document.createElement("div");
                    timeSlotButton.className = "time-slot-display";
                    timeSlotButton.textContent = `${hour}:00`;

                    // Check if the slot is available (not booked and not in the past)
                    if ((isToday(date) && hour <= currentHour) || bookedHours.includes(hour)) {
                        timeSlotButton.classList.add("unavailable");
                    } else {
                        timeSlotButton.classList.add("available");

                        // Add click event to select time slot
                        //
                    }

                    timeSlots.appendChild(timeSlotButton);
                }
            });
        }

        /**
         * Get the specialty ID from the URL
         * @returns {string} The specialty ID
         */
        function getSpecialtyId() {
            const urlParams = new URLSearchParams(window.location.search);
            return urlParams.get('specialty') || '';
        }

        /**
         * Check if the given date is today
         * @param {Date} date - The date to check
         * @returns {boolean} True if the date is today, false otherwise
         */
        function isToday(date) {
            const argDate = new Date().toLocaleString("en-US", {
                timeZone: "America/Argentina/Buenos_Aires"
            });
            const today = new Date(argDate);

            return (
                date.getDate() === today.getDate() &&
                date.getMonth() === today.getMonth() &&
                date.getFullYear() === today.getFullYear()
            );
        }
    }
});