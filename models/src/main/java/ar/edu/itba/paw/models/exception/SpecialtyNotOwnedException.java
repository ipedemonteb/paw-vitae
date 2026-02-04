package ar.edu.itba.paw.models.exception;

public class SpecialtyNotOwnedException extends CustomRuntimeException {

    public SpecialtyNotOwnedException() {
        super("exception.specialtyNotOwned",ExceptionUtils.FORBIDDEN_STATUS_CODE);    }
}
