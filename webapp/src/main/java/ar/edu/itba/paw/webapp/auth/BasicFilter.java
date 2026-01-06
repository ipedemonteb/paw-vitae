package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.interfaceServices.UserService; // Asegúrate de usar tu interface correcta
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64; // Usar Java util standard
import java.util.Optional;

import static ar.edu.itba.paw.webapp.auth.AuthUtils.BASIC_PREFIX;

@Component
public class BasicFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = BASIC_PREFIX;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenResponseHelper tokenResponseHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(AUTH_HEADER)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String credsBase64 = authHeader.substring(AUTH_HEADER.length()).trim();
            final byte[] credsBytes = Base64.getDecoder().decode(credsBase64);
            final String credsDecoded = new String(credsBytes, StandardCharsets.UTF_8);

            final String[] split = credsDecoded.split(":", 2);
            if (split.length != 2) {
                filterChain.doFilter(request, response);
                return;
            }

            final String email = split[0];
            final String password = split[1];


            final Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );


            SecurityContextHolder.getContext().setAuthentication(auth);

            final Optional<? extends User> user = userService.getByEmail(email);

            user.ifPresent(value -> tokenResponseHelper.addAuthenticationHeaders(response, value, request));

        } catch (AuthenticationException e) {
            response.addHeader("WWW-Authenticate", "Basic realm=\"Vitae\"");
        }

        filterChain.doFilter(request, response);
    }
}