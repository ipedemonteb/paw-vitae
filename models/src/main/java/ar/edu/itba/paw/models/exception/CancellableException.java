package ar.edu.itba.paw.models.exception;


public class CancellableException extends CustomRuntimeException {

    public CancellableException () {
        super("exception.appointmentNotCancellable", ExceptionUtils.BAD_REQUEST_STATUS_CODE);
    }

}
