package ar.edu.itba.paw.webapp.auth;


import ar.edu.itba.paw.interfaceServices.UserService;
import ar.edu.itba.paw.models.Client;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Specialty;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class AuthUserDetailsService implements UserDetailsService {

    private UserService us;

    private final Pattern BCRYPT_PATTERN = Pattern
            .compile("\\$2[ayb]\\$\\d{2}\\$[\\w./]{53}");

    @Autowired
    public AuthUserDetailsService(final UserService us) {
        this.us = us;
    }

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        final User user = us.getByEmail(email).orElseThrow(() -> new UsernameNotFoundException("No user with the email " + email));

//        if (!BCRYPT_PATTERN.matcher(user.getPassword()).matches()) { //manera rustica de hacerlo, se deberia pedir al usuario que cambie su contraseña
//            us.changePassword(user.getId(), user.getPassword());
//            return loadUserByUsername(email);
//        }

        if(user.getClass() == Doctor.class) {
            return new AuthUserDetails(email, user.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_DOCTOR")));
        }

        return new AuthUserDetails(email, user.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_PATIENT")));



//        final Collection<? extends GrantedAuthority> authorities = Arrays.asList(
//                new SimpleGrantedAuthority("ROLE_DOCTOR"),
//                new SimpleGrantedAuthority("ROLE_PATIENT")
//        );
//        return new AuthUserDetails(email, user.getPassword(), authorities);
    }
}