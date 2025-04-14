document.addEventListener('DOMContentLoaded', function() {


    function fetchDoctors() {

        const url = `${contextPath}/doctors`;

        return fetch(url)
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Failed to fetch fully booked dates");
                }
                return response.json();
            })
            .then((data) => {
                return data || [];
            })
            .catch((error) => {
                console.error("Error fetching fully booked dates:", error);
                return [];
            });
    }

    let doctorNames = [];
    fetchDoctors().then(data => {
        doctorNames = data;
    })


    const searchInput = document.getElementById('doctorSearch');
    const suggestionsContainer = document.getElementById('suggestions-container');
    const specialtyDropdown = document.getElementById('specialtyDropdown');
    const searchButton = document.getElementById('searchButton');

    if (!searchInput || !suggestionsContainer) {
        console.error('Search input or suggestions container not found');
        return;
    }

    // Function to display suggestions
    function showSuggestions(input) {
        // Clear previous suggestions
        suggestionsContainer.innerHTML = '';

        if (input.length < 2) {
            suggestionsContainer.style.display = 'none';
            return;
        }

        // Filter doctor names based on input
        const filteredNames = doctorNames.filter(name =>
            name.toLowerCase().includes(input.toLowerCase())
        );

        const limitedNames = filteredNames.slice(0, 7); // Limit to 5 suggestions

        if (limitedNames.length === 0) {
            suggestionsContainer.style.display = 'none';
            return;
        }

        // Create and append suggestion elements
        limitedNames.forEach(name => {
            const suggestion = document.createElement('div');
            suggestion.className = 'suggestion-item';
            suggestion.textContent = name;
            suggestion.addEventListener('click', function() {
                searchInput.value = name;
                suggestionsContainer.style.display = 'none';
                checkSearchCriteria();
            });
            suggestionsContainer.appendChild(suggestion);
        });

        // Make sure the suggestions are visible
        suggestionsContainer.style.display = 'block';
    }

    // Function to check if search criteria are met
    function checkSearchCriteria() {
        if (searchInput.value.trim() !== '' || specialtyDropdown.value !== '') {
            searchButton.disabled = false;
        } else {
            searchButton.disabled = true;
        }
    }

    // Event listeners
    searchInput.addEventListener('input', function() {
        const inputValue = this.value.trim();
        console.log('Input changed:', inputValue);
        showSuggestions(inputValue);
        checkSearchCriteria();
    });

    // Also show suggestions when the input is focused if there's text
    searchInput.addEventListener('focus', function() {
        const inputValue = this.value.trim();
        if (inputValue.length >= 2) {
            showSuggestions(inputValue);
        }
    });

    specialtyDropdown.addEventListener('change', checkSearchCriteria);

    // Close suggestions when clicking outside
    document.addEventListener('click', function(event) {
        if (event.target !== searchInput && !suggestionsContainer.contains(event.target)) {
            suggestionsContainer.style.display = 'none';
        }
    });

    // Prevent form submission if button is disabled
    document.getElementById('searchForm').addEventListener('submit', function(event) {
        if (searchButton.disabled) {
            event.preventDefault();
        }
    });

    // Initialize button state
    checkSearchCriteria();
});