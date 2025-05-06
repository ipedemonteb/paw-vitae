package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaceServices.UserService;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private UserDetailsService userDetailsService;
    private UserService userService;

    @Autowired
    public AuthService(UserDetailsService userDetailsService, UserService userService) {
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    public boolean verifyAndLoginUser(String token) {
        Optional<? extends User> user =userService.verifyValidationToken(token);
        if (user.isEmpty()) {
            return false;
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.get().getEmail());
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        return true;
    }
}