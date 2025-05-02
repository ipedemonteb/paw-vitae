package ar.edu.itba.paw.webapp.auth;


import ar.edu.itba.paw.interfaceServices.PatientService;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.interfaceServices.ImageService;
import ar.edu.itba.paw.interfaceServices.UserService;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.form.DoctorForm;
import ar.edu.itba.paw.webapp.form.PatientForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Component
public class AuthUserDetailsService implements UserDetailsService {
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private ImageService imageService;

    @Autowired
    private AuthenticationManager authenticationManager;
    private UserService us;


    private static final Logger LOGGER = LoggerFactory.getLogger(AuthUserDetailsService.class);
    private final Pattern BCRYPT_PATTERN = Pattern
            .compile("\\$2[ayb]\\$\\d{2}\\$[\\w./]{53}");

    @Autowired
    public AuthUserDetailsService(final UserService us) {
        this.us = us;
    }

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        final User user = us.getByEmail(email).orElseThrow(() -> new UsernameNotFoundException("No user with the email " + email));

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



//        final Collection<? extends GrantedAuthority> authorities = Arrays.asList(
//                new SimpleGrantedAuthority("ROLE_DOCTOR"),
//                new SimpleGrantedAuthority("ROLE_PATIENT")
//        );
//        return new AuthUserDetails(email, user.getPassword(), authorities);
    }




}