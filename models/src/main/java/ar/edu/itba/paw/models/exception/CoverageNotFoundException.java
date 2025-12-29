package ar.edu.itba.paw.models.exception;

public class CoverageNotFoundException extends CustomRuntimeException{
    public CoverageNotFoundException() {
        super("exception.coverageNotFound",ExceptionUtils.NOT_FOUND_STATUS_CODE);
    }
}
