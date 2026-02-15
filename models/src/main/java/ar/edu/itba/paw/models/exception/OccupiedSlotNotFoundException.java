package ar.edu.itba.paw.models.exception;

public class OccupiedSlotNotFoundException extends CustomRuntimeException {
    public OccupiedSlotNotFoundException() {
        super("exception.occupiedSlotNotFound",ExceptionUtils.NOT_FOUND_STATUS_CODE);    }
}
