package ar.edu.itba.paw.models.exception;

public class UserNotFoundException extends CustomRuntimeException {

    public UserNotFoundException() {
        super("exception.userNotFound",ExceptionUtils.NOT_FOUND_STATUS_CODE);
    }
}
