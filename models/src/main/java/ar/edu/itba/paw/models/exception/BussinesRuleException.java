package ar.edu.itba.paw.models.exception;

public class BussinesRuleException extends CustomRuntimeException {
    public BussinesRuleException(String message) {
        super(message, ExceptionUtils.UNPROCESSABLE_ENTITY_STATUS_CODE);
    }
}
