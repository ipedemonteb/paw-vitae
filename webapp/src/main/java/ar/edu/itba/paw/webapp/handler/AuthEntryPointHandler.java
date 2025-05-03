package ar.edu.itba.paw.webapp.handler;

import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthEntryPointHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String ctx = request.getContextPath();

        // Check for remember-me cookie even if auth is null
        boolean hasRememberMeCookie = false;
        if (request.getCookies() != null) {
            for (javax.servlet.http.Cookie cookie : request.getCookies()) {
                if ("remember-me".equals(cookie.getName())) {
                    hasRememberMeCookie = true;
                    break;
                }
            }
        }

        if (hasRememberMeCookie) {
            // User has a remember-me cookie but authentication failed
            response.sendRedirect(ctx + "/error/403");
        } else if (auth instanceof RememberMeAuthenticationToken) {
            // Fall back to the previous behavior if auth is not null
            response.sendRedirect(ctx + "/error/403");
        } else {
            // No remember-me cookie or authentication
            response.sendRedirect(ctx + "/login");
        }
    }
}
