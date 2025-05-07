package ar.edu.itba.paw.webapp.handler;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthEntryPointHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String ctx = request.getContextPath();

        boolean hasRememberMeCookie = false;
        if (request.getCookies() != null) {
            for (javax.servlet.http.Cookie cookie : request.getCookies()) {
                if ("remember-me".equals(cookie.getName())) {
                    hasRememberMeCookie = true;
                    break;
                }
            }
        }

        if (hasRememberMeCookie || auth instanceof RememberMeAuthenticationToken) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
        } else {
            response.sendRedirect(ctx + "/login");
        }
    }
}
