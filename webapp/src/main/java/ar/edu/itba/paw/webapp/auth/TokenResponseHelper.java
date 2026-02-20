package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest; // IMPORTANTE
import javax.servlet.http.HttpServletResponse;

import static ar.edu.itba.paw.webapp.auth.AuthUtils.HEADER_ACCESS_TOKEN;
import static ar.edu.itba.paw.webapp.auth.AuthUtils.HEADER_REFRESH_TOKEN;

@Component
public class TokenResponseHelper {

    private final JwtService jwtService;

    @Autowired
    public TokenResponseHelper(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public void addAuthenticationHeaders(HttpServletResponse response, User user, HttpServletRequest request) {

        final ServletUriComponentsBuilder uriBuilder = ServletUriComponentsBuilder.fromContextPath(request);

        final String accessToken = jwtService.generateAccessToken(uriBuilder, user);
        final String refreshToken = jwtService.generateRefreshToken(uriBuilder, user);

        response.setHeader(HEADER_ACCESS_TOKEN, accessToken);
        response.setHeader(HEADER_REFRESH_TOKEN, refreshToken);
    }
}