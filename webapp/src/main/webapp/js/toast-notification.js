/**
 * Toast Notification System
 * Handles success toast notifications for profile updates
 */

// Show success toast notification
function showSuccessToast() {
    const toast = document.getElementById('successToast');
    if (!toast) return;
    toast.classList.add('show');
    // Auto hide after 5 seconds
    setTimeout(() => {
        hideSuccessToast();
    }, 5000);
}

function showErrorToast() {
    const toast = document.getElementById('errorToast');
    if (!toast) return;
    toast.classList.add('show');
    // Auto hide after 5 seconds
    setTimeout(() => {
        hideSuccessToast();
    }, 5000);
}

// Hide success toast notification
function hideSuccessToast() {
    const toast = document.getElementById('successToast');
    if (!toast) return;

    toast.style.animation = 'fadeOut 0.3s ease forwards';

    setTimeout(() => {
        toast.classList.remove('show');
        toast.style.animation = '';
    }, 300);
}

function hideErrorToast() {
    const toast = document.getElementById('errorToast');
    if (!toast) return;

    toast.style.animation = 'fadeOut 0.3s ease forwards';

    setTimeout(() => {
        toast.classList.remove('show');
        toast.style.animation = '';
    }, 300);
}

// Check for URL parameters on page load
document.addEventListener('DOMContentLoaded', function() {
    // Check if profile was updated successfully
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.get('updated') === 'true') {
        showSuccessToast();

        // Remove the query parameter without refreshing the page
        const newUrl = window.location.pathname;
        window.history.replaceState({}, document.title, newUrl);
    }
});