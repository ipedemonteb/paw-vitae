package ar.edu.itba.paw.models.exception;

public class DoctorOfficeNotFoundException extends RuntimeException {

    public DoctorOfficeNotFoundException(String message) {
        super(message);
    }

    public DoctorOfficeNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DoctorOfficeNotFoundException(final Throwable cause) {
        super(cause);
    }

    public DoctorOfficeNotFoundException() {
        super("Doctor office not found");
    }

}
