package ar.edu.itba.paw.models.exception;

public class CoverageNotFoundException extends RuntimeException{
    public CoverageNotFoundException() {
    }

    public CoverageNotFoundException(final String message) {
        super(message);
    }

    public CoverageNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CoverageNotFoundException(final Throwable cause) {
        super(cause);
    }
}
