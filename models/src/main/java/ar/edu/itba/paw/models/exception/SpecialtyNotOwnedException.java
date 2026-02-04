package ar.edu.itba.paw.models.exception;

public class SpecialtyNotOwnedException extends CustomRuntimeException {

    public SpecialtyNotOwnedException() {
        super("exception.specialtyNotOwned",ExceptionUtils.BAD_REQUEST_STATUS_CODE);    }
}
