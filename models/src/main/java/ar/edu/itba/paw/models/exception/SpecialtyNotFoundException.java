package ar.edu.itba.paw.models.exception;

public class SpecialtyNotFoundException extends RuntimeException{
    public SpecialtyNotFoundException() {
    }

    public SpecialtyNotFoundException(String message) {
        super(message);
    }

    public SpecialtyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SpecialtyNotFoundException(Throwable cause) {
        super(cause);
    }
}
