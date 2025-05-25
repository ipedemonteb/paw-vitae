package ar.edu.itba.paw.webapp.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {


    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        SavedRequest savedRequest = (SavedRequest) request.getSession()
                .getAttribute("SPRING_SECURITY_SAVED_REQUEST");
        if (savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();
            if (targetUrl != null && isSafeRedirect(targetUrl)) {
                return targetUrl;
            }
        }
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_DOCTOR"))) {
            return "/doctor/dashboard";
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PATIENT"))) {
            return "/patient/dashboard";
        }
        return "/";
    }


    private boolean isSafeRedirect(String url) {
        return url.startsWith("/") &&
                !url.contains(".well-known") &&
                !url.contains("chrome.devtools") &&
                !url.contains("..");
    }
}