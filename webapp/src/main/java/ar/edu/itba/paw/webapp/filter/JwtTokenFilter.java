package ar.edu.itba.paw.webapp.filter;

import ar.edu.itba.paw.interfaceServices.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.JwtDetails;
import ar.edu.itba.paw.webapp.auth.JwtService;
import ar.edu.itba.paw.webapp.auth.TokenResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static ar.edu.itba.paw.webapp.auth.AuthUtils.BEARER_PREFIX;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = BEARER_PREFIX;

    private final UserService userService;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenResponseHelper tokenResponseHelper;


    @Autowired
    public JwtTokenFilter(UserService userService, final JwtService jwtService, UserDetailsService userDetailsService, TokenResponseHelper tokenResponseHelper) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.tokenResponseHelper = tokenResponseHelper;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!StringUtils.hasText(header) || !header.startsWith(AUTH_HEADER)) {
            chain.doFilter(request, response);
            return;
        }

        final String token = header.substring(AUTH_HEADER.length()).trim();
        final JwtDetails jwtDetails = jwtService.validate(token, response);
        if (jwtDetails == null) {
            response.addHeader("WWW-Authenticate", "Bearer realm=\"Vitae\"");
            chain.doFilter(request, response);
            return;
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(jwtDetails.getEmail());
        if (userDetails == null) {
            response.addHeader("WWW-Authenticate", "Bearer realm=\"Vitae\"");
            chain.doFilter(request, response);
            return;
        }

        if (jwtDetails.getType().isRefreshToken()) {
            final Optional<? extends User> maybeUser = userService.getByEmail(userDetails.getUsername());
            maybeUser.ifPresent(user -> tokenResponseHelper.addAuthenticationHeaders(response, user, request));
        }

        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }


}
