package ar.edu.itba.paw.webapp.auth;


import ar.edu.itba.paw.interfaceServices.UserService;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

@Component
public class AuthUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthUserDetailsService.class);
    private final Pattern BCRYPT_PATTERN = Pattern.compile("\\$2[ayb]\\$\\d{2}\\$[\\w./]{53}");
    private final UserService userService;

    @Autowired
    public AuthUserDetailsService(final UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        final User user = userService.getByEmail(email).orElseThrow(() -> new UsernameNotFoundException("No user with the email " + email));
        if (user instanceof Doctor) {
            return new AuthUserDetails(
                    email,
                    user.getPassword(),
                    user.isVerified(),
                    true,
                    true,
                    true,
                    List.of(new SimpleGrantedAuthority("ROLE_DOCTOR"))
            );
        }
        return new AuthUserDetails(
                email,
                user.getPassword(),
                user.isVerified(),
                true,
                true,
                true,
                List.of(new SimpleGrantedAuthority("ROLE_PATIENT"))
        );
    }


}