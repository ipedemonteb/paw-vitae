package ar.edu.itba.paw.models.exception;

public class ResourceOwnershipException extends CustomRuntimeException {
    public ResourceOwnershipException() {
        super("exception.resourceOwnership", ExceptionUtils.FORBIDDEN_STATUS_CODE);
    }
}
