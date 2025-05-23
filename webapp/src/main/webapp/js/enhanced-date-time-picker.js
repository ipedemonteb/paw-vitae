// Date Range Picker for Unavailability - Multilingual Support
document.addEventListener("DOMContentLoaded", () => {
    // DOM Elements
    const startDateInput = document.getElementById("startDate");
    const endDateInput = document.getElementById("endDate");
    const startDateDisplay = document.getElementById("startDateDisplay");
    const endDateDisplay = document.getElementById("endDateDisplay");
    const datePickerCalendar = document.getElementById("datePickerCalendar");
    const prevMonthBtn = document.getElementById("prevMonthBtn");
    const nextMonthBtn = document.getElementById("nextMonthBtn");
    const currentMonthYear = document.getElementById("currentMonthYear");
    const calendarWeekdays = document.getElementById("calendarWeekdays");
    const calendarDays = document.getElementById("calendarDays");
    const dateErrorMessage = document.getElementById("date-error-message");

    // State variables
    const today = new Date();
    let currentDate = new Date();
    let selectionStart = null;
    let selectionEnd = null;
    let selectionMode = 'start'; // 'start' or 'end'

    // Detect browser language
    const browserLang = navigator.language || navigator.userLanguage;
    const isSpanish = browserLang.startsWith('es');

    // Multilingual content
    const translations = {
        en: {
            months: [
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
            ],
            monthsShort: [
                "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
            ],
            weekdaysShort: ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"],
            selectStartDate: "Select start date",
            selectEndDate: "Select end date",
            endDateBeforeStart: "End date cannot be before start date",
            bothDatesRequired: "Both start and end dates are required",
            startBeforeEnd: "Start date must be before or equal to end date",
            dateRangeOverlaps: "This date range overlaps with an existing unavailability period"
        },
        es: {
            months: [
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
            ],
            monthsShort: [
                "Ene", "Feb", "Mar", "Abr", "May", "Jun",
                "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"
            ],
            weekdaysShort: ["Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb"],
            selectStartDate: "Seleccionar fecha de inicio",
            selectEndDate: "Seleccionar fecha de fin",
            endDateBeforeStart: "La fecha de fin no puede ser anterior a la fecha de inicio",
            bothDatesRequired: "Se requieren ambas fechas de inicio y fin",
            startBeforeEnd: "La fecha de inicio debe ser anterior o igual a la fecha de fin",
            dateRangeOverlaps: "Este rango de fechas se superpone con un período de no disponibilidad existente"
        }
    };

    // Get current language translations
    const t = translations[isSpanish ? 'es' : 'en'];

    // Initialize the date picker
    initDatePicker();

    // Show calendar when modal opens
    function showUnavailabilityModal() {
        document.getElementById('unavailabilityModal').classList.add('show');
        datePickerCalendar.style.display = 'block';
        resetDateSelection();
    }

    // Reset date selection
    function resetDateSelection() {
        selectionStart = null;
        selectionEnd = null;
        selectionMode = 'start';
        startDateInput.value = '';
        endDateInput.value = '';
        updateDateDisplays();
        renderCalendarDays();
    }

    // Initialize date picker
    function initDatePicker() {
        // Initialize calendar
        renderCalendarHeader();
        renderWeekdays();
        renderCalendarDays();

        // Event listeners
        prevMonthBtn.addEventListener("click", goToPreviousMonth);
        nextMonthBtn.addEventListener("click", goToNextMonth);

        // Show calendar by default in the modal
        datePickerCalendar.style.display = 'block';

        // Update display when inputs change
        startDateInput.addEventListener('change', function() {
            if (this.value) {
                selectionStart = new Date(this.value);
                selectionMode = 'end';
                updateDateDisplays();
                renderCalendarDays();
            }
        });

        endDateInput.addEventListener('change', function() {
            if (this.value) {
                selectionEnd = new Date(this.value);
                updateDateDisplays();
                renderCalendarDays();
            }
        });

        // Click on date displays to focus on calendar
        startDateDisplay.addEventListener('click', function() {
            selectionMode = 'start';
            datePickerCalendar.style.display = 'block';
        });

        endDateDisplay.addEventListener('click', function() {
            selectionMode = 'end';
            datePickerCalendar.style.display = 'block';
        });
    }

    // Render calendar header
    function renderCalendarHeader() {
        currentMonthYear.textContent = `${t.months[currentDate.getMonth()]} ${currentDate.getFullYear()}`;
    }

    // Render weekdays
    function renderWeekdays() {
        calendarWeekdays.innerHTML = "";
        t.weekdaysShort.forEach(day => {
            const dayElement = document.createElement("div");
            dayElement.className = "date-picker-weekday";
            dayElement.textContent = day;
            calendarWeekdays.appendChild(dayElement);
        });
    }

    // Render calendar days
    function renderCalendarDays() {
        calendarDays.innerHTML = "";

        const year = currentDate.getFullYear();
        const month = currentDate.getMonth();

        // First and last day of the month
        const firstDay = new Date(year, month, 1);
        const lastDay = new Date(year, month + 1, 0);

        // Get the day of the week for the first day (0-6)
        const firstDayOfWeek = firstDay.getDay();

        // Create empty cells for days before the first day of the month
        for (let i = 0; i < firstDayOfWeek; i++) {
            const emptyDay = document.createElement("div");
            emptyDay.className = "date-picker-day empty";
            calendarDays.appendChild(emptyDay);
        }

        // Create cells for each day of the month
        for (let day = 1; day <= lastDay.getDate(); day++) {
            const date = new Date(year, month, day);

            const dayElement = document.createElement("div");
            dayElement.className = "date-picker-day";
            dayElement.textContent = day;

            // Disable past dates
            if (date < today) {
                dayElement.classList.add("disabled");
            } else {
                // Check if this date is in the selected range
                if (selectionStart && selectionEnd) {
                    if (date >= selectionStart && date <= selectionEnd) {
                        dayElement.classList.add("in-range");
                    }
                    if (isSameDay(date, selectionStart)) {
                        dayElement.classList.add("range-start");
                    }
                    if (isSameDay(date, selectionEnd)) {
                        dayElement.classList.add("range-end");
                    }
                } else if (selectionStart && isSameDay(date, selectionStart)) {
                    dayElement.classList.add("selected");
                }

                // Add click event to select date
                dayElement.addEventListener("click", () => selectDate(date));
            }

            calendarDays.appendChild(dayElement);
        }
    }

    // Go to previous month
    function goToPreviousMonth() {
        currentDate.setMonth(currentDate.getMonth() - 1);
        renderCalendarHeader();
        renderCalendarDays();
    }

    // Go to next month
    function goToNextMonth() {
        currentDate.setMonth(currentDate.getMonth() + 1);
        renderCalendarHeader();
        renderCalendarDays();
    }

    // Select a date
    function selectDate(date) {
        if (selectionMode === 'start') {
            selectionStart = date;
            startDateInput.value = formatDateForInput(date);
            selectionMode = 'end';
            selectionEnd = null;
            endDateInput.value = '';
        } else {
            // Ensure end date is not before start date
            if (date < selectionStart) {
                dateErrorMessage.textContent = t.endDateBeforeStart;
                dateErrorMessage.style.display = 'block';
                return;
            }

            selectionEnd = date;
            endDateInput.value = formatDateForInput(date);
            selectionMode = 'start'; // Reset for next selection
            dateErrorMessage.style.display = 'none';
        }

        updateDateDisplays();
        renderCalendarDays();
    }

    // Update date displays
    function updateDateDisplays() {
        // Update start date display
        if (selectionStart) {
            startDateDisplay.innerHTML = `
                <i class="fas fa-calendar-day"></i>
                <span>${formatDateForDisplay(selectionStart)}</span>
            `;
            startDateDisplay.classList.add('date-selected');
        } else {
            startDateDisplay.innerHTML = `
                <i class="fas fa-calendar-day"></i>
                <span>${t.selectStartDate}</span>
            `;
            startDateDisplay.classList.remove('date-selected');
        }

        // Update end date display
        if (selectionEnd) {
            endDateDisplay.innerHTML = `
                <i class="fas fa-calendar-day"></i>
                <span>${formatDateForDisplay(selectionEnd)}</span>
            `;
            endDateDisplay.classList.add('date-selected');
        } else {
            endDateDisplay.innerHTML = `
                <i class="fas fa-calendar-day"></i>
                <span>${t.selectEndDate}</span>
            `;
            endDateDisplay.classList.remove('date-selected');
        }

        // Highlight active selection mode
        if (selectionMode === 'start') {
            startDateDisplay.classList.add('active-selection');
            endDateDisplay.classList.remove('active-selection');
        } else {
            startDateDisplay.classList.remove('active-selection');
            endDateDisplay.classList.add('active-selection');
        }
    }

    // Format date for display (e.g., "Jan 1, 2023" or "Ene 1, 2023")
    function formatDateForDisplay(date) {
        const month = t.monthsShort[date.getMonth()];
        const day = date.getDate();
        const year = date.getFullYear();
        return `${month} ${day}, ${year}`;
    }

    // Format date for input field (YYYY-MM-DD)
    function formatDateForInput(date) {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
    }

    // Check if two dates are the same day
    function isSameDay(date1, date2) {
        return date1.getDate() === date2.getDate() &&
            date1.getMonth() === date2.getMonth() &&
            date1.getFullYear() === date2.getFullYear();
    }

    // Modified addUnavailabilityDate function with multilingual support
    window.addUnavailabilityDate = function() {
        const startDate = document.getElementById('startDate').value;
        const endDate = document.getElementById('endDate').value;

        // Validate dates
        if (!startDate || !endDate) {
            dateErrorMessage.textContent = t.bothDatesRequired;
            dateErrorMessage.style.display = 'block';
            return;
        }

        if (new Date(startDate) > new Date(endDate)) {
            dateErrorMessage.textContent = t.startBeforeEnd;
            dateErrorMessage.style.display = 'block';
            return;
        }

        // Check for overlaps with existing unavailability dates
        const overlaps = unavailabilityDates.some(date => {
            return !(endDate < date.startDate || startDate > date.endDate);
        });

        if (overlaps) {
            dateErrorMessage.textContent = t.dateRangeOverlaps;
            dateErrorMessage.style.display = 'block';
            return;
        }

        // Add to unavailability dates array
        unavailabilityDates.push({
            index: unavailabilityCounter,
            startDate: startDate,
            endDate: endDate
        });

        // Create and add the unavailability date row
        const container = document.getElementById('unavailability-dates-container');

        // Format dates for display using the same format as JSP
        const startDateObj = new Date(startDate);
        const endDateObj = new Date(endDate);
        const formattedStartDate = formatDateForDisplay(startDateObj);
        const formattedEndDate = formatDateForDisplay(endDateObj);

        // Create row
        const row = document.createElement('div');
        row.className = 'unavailability-date-row';
        row.id = 'unavailability-row-' + unavailabilityCounter;

        // Create date info
        const dateInfo = document.createElement('div');
        dateInfo.className = 'unavailability-date-info';

        const dateRange = document.createElement('div');
        dateRange.className = 'date-range';

        const icon = document.createElement('i');
        icon.className = 'fas fa-calendar-day';
        dateRange.appendChild(icon);

        const dateText = document.createElement('span');
        dateText.textContent = formattedStartDate + ' - ' + formattedEndDate;
        dateRange.appendChild(dateText);

        dateInfo.appendChild(dateRange);

        // Create hidden inputs
        const startDateInput = document.createElement('input');
        startDateInput.type = 'hidden';
        startDateInput.name = 'unavailabilitySlots[' + unavailabilityCounter + '].startDate';
        startDateInput.value = startDate;
        dateInfo.appendChild(startDateInput);

        const endDateInput = document.createElement('input');
        endDateInput.type = 'hidden';
        endDateInput.name = 'unavailabilitySlots[' + unavailabilityCounter + '].endDate';
        endDateInput.value = endDate;
        dateInfo.appendChild(endDateInput);

        // Create remove button
        const removeBtn = document.createElement('button');
        removeBtn.type = 'button';
        removeBtn.className = 'btn-remove-unavailability';
        removeBtn.innerHTML = '<i class="fas fa-trash"></i>';
        removeBtn.onclick = function() {
            removeUnavailabilityDate(unavailabilityCounter);
        };

        // Add elements to row
        row.appendChild(dateInfo);
        row.appendChild(removeBtn);

        // Add row to container
        container.appendChild(row);

        // Update no unavailability message
        updateNoUnavailabilityMessage();

        // Increment counter
        unavailabilityCounter++;

        // Reset date selection and hide modal
        resetDateSelection();
        hideUnavailabilityModal();
    };

    // Override the original showUnavailabilityModal function
    window.showUnavailabilityModal = showUnavailabilityModal;
});