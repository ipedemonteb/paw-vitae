package ar.edu.itba.paw.webapp.auth;

public class AuthUtils {

    public static final String AUTH_HEADER = "Authorization";
    public static final String BASIC_PREFIX = "Basic ";
    public static final String BEARER_PREFIX = "Bearer ";

    public static final String HEADER_ACCESS_TOKEN = "X-Vitae-AuthToken";
    public static final String HEADER_REFRESH_TOKEN = "X-Vitae-RefreshToken";

    private AuthUtils() {} // Private constructor
}
