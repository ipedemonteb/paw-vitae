package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.interfaceServices.UserService;
import ar.edu.itba.paw.webapp.auth.TokenResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

import static ar.edu.itba.paw.webapp.auth.AuthUtils.BASIC_PREFIX;

@Component
public class BasicFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = BASIC_PREFIX;

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final TokenResponseHelper tokenResponseHelper;

    @Autowired
    public BasicFilter(AuthenticationManager authenticationManager, UserService userService, UserDetailsService userDetailsService, TokenResponseHelper tokenResponseHelper) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.tokenResponseHelper = tokenResponseHelper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(AUTH_HEADER)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String credsBase64 = authHeader.substring(AUTH_HEADER.length()).trim();
            final String credsDecoded = new String(Base64.getDecoder().decode(credsBase64), StandardCharsets.UTF_8);
            final String[] split = credsDecoded.split(":", 2);

            if (split.length != 2) {
                filterChain.doFilter(request, response);
                return;
            }

            final String email = split[0];
            final String potentialPasswordOrToken = split[1];

            Authentication auth;
            Optional<? extends User> targetUser;

            Optional<? extends User> userByVerificationToken = userService.getByVerificationToken(potentialPasswordOrToken);

            if (userByVerificationToken.isPresent() && userByVerificationToken.get().getEmail().equals(email)) {
                User user = userByVerificationToken.get();
                userService.verifyValidationToken(potentialPasswordOrToken);
                auth = createManualAuthentication(email, request);
                targetUser = Optional.of(user);

            } else {
                Optional<? extends User> userByResetToken = userService.getByResetToken(potentialPasswordOrToken);
                if (userByResetToken.isPresent() && userByResetToken.get().getEmail().equals(email)) {
                    User user = userByResetToken.get();
                    auth = createManualAuthentication(email, request);
                    targetUser = Optional.of(user);

                } else {
                    auth = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(email, potentialPasswordOrToken)
                    );
                    targetUser = userService.getByEmail(email);
                }
            }

            SecurityContextHolder.getContext().setAuthentication(auth);

            targetUser.ifPresent(user -> tokenResponseHelper.addAuthenticationHeaders(response, user, request));

        } catch (AuthenticationException e) {
            SecurityContextHolder.clearContext();
            response.addHeader("WWW-Authenticate", "Basic realm=\"Vitae\"");
        }

        filterChain.doFilter(request, response);
    }

    private Authentication createManualAuthentication(String email, HttpServletRequest request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken manualAuth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        manualAuth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return manualAuth;
    }
}