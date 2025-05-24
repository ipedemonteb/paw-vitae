<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div class="modal-overlay" id="${param.id}">
    <div class="modal-container">
        <div class="modal-header">
            <h3><spring:message code="${param.title}" /></h3>
            <button class="modal-close" aria-label="Close modal">
                <i class="fas fa-times"></i>
            </button>
        </div>
        <div class="modal-body">
            <p><spring:message code="${param.message}" /></p>
        </div>
        <div class="modal-footer" id="${param.divId}">
            <button class="modal-btn modal-btn-outline modal-cancel"><spring:message code="logout.confirm.cancel" /></button>
            <form id="${param.formId}" action="<c:url value='${param.actionPath}' />" method="post" class="logout-form">
                <sec:csrfInput />
                <c:if test="${param.actionPath.contains('/cancel')}">
                    <input type="hidden" value="" name="appointmentId" />
                </c:if>
                <button type="submit" id="${param.buttonId}" class="modal-btn modal-btn-primary"><spring:message code="${param.confirm}" /></button>
            </form>
        </div>
    </div>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        const setupModal = function (buttonSelector, modalId, isCancelModal) {
            const triggerButtons = document.querySelectorAll(buttonSelector);
            const modal = document.getElementById(modalId);
            let modalClose;
            let modalCancel;
            if (modal) {
                modalClose = modal.querySelector('.modal-close');
                modalCancel = modal.querySelector('.modal-cancel');
            }
            const body = document.body;

            if (triggerButtons && modal) {
                triggerButtons.forEach(button => {
                    button.addEventListener('click', function () {
                        // Open the correct modal
                        modal.classList.add('show');
                        body.classList.add('modal-open');

                        // If it's a cancel modal, dynamically set the appointmentId
                        if (isCancelModal) {
                            const appointmentId = this.getAttribute('data-id');
                            const hiddenInput = modal.querySelector('input[name="appointmentId"]');
                            if (hiddenInput) {
                                hiddenInput.value = appointmentId;
                            }
                        }
                    });
                });
            }

            const closeModal = function () {
                modal.classList.remove('show');
                body.classList.remove('modal-open');
            };

            if (modalClose) {
                modalClose.addEventListener('click', closeModal);
            }

            if (modalCancel) {
                modalCancel.addEventListener('click', closeModal);
            }

            // Close modal when clicking outside
            if (modal) {
                modal.addEventListener('click', function (e) {
                    if (e.target === modal) {
                        closeModal();
                    }
                });
            }

            if(modal){
                document.addEventListener('keydown', function (e) {
                    if (e.key === 'Escape' && modal.classList.contains('show')) {
                        closeModal();
                    }
                });
            }
        };

        // Setup modals for logout and cancel appointment
        setupModal('.logout-btn', 'logoutModal', false);
        setupModal('.cancel-appointment', 'cancelAppointmentModal', true);
    })
</script>