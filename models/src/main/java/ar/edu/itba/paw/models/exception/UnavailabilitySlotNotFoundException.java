package ar.edu.itba.paw.models.exception;

public class UnavailabilitySlotNotFoundException extends CustomRuntimeException{

    public UnavailabilitySlotNotFoundException() {
        super("exception.unavailabilitySlotNotFound",ExceptionUtils.NOT_FOUND_STATUS_CODE);
    }
}
