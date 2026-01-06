package ar.edu.itba.paw.models.exception;

public final class ExceptionUtils {

    public static final int BAD_REQUEST_STATUS_CODE = 400;
    public static final int NOT_FOUND_STATUS_CODE = 404;
    public static final int GONE_STATUS_CODE = 410;
    public static final int FORBIDDEN_STATUS_CODE = 403;
    public static final int UNPROCESSABLE_ENTITY_STATUS_CODE =422 ;

    private ExceptionUtils() {
        throw new AssertionError();
    }
}