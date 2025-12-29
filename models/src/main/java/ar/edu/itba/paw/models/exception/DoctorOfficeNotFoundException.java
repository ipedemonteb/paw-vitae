package ar.edu.itba.paw.models.exception;

public class DoctorOfficeNotFoundException extends CustomRuntimeException {

    public DoctorOfficeNotFoundException() {
        super("exception.doctorOfficeNotFound",ExceptionUtils.NOT_FOUND_STATUS_CODE);
    }


}
