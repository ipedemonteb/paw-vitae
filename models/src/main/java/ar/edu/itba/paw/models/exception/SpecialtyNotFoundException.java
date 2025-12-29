package ar.edu.itba.paw.models.exception;

public class SpecialtyNotFoundException extends CustomRuntimeException {


    public SpecialtyNotFoundException() {
        super("exception.specialtyNotFound",ExceptionUtils.NOT_FOUND_STATUS_CODE);    }
}
