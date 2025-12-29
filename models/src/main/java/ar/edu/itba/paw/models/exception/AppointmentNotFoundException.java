package ar.edu.itba.paw.models.exception;

public class AppointmentNotFoundException extends CustomRuntimeException {


    public AppointmentNotFoundException() {
        super("exception.appointmentNotFound",ExceptionUtils.NOT_FOUND_STATUS_CODE);
    }


}
