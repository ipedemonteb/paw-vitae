package ar.edu.itba.paw.webapp.auth;


import ar.edu.itba.paw.interfaceServices.ClientService;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.interfaceServices.ImageService;
import ar.edu.itba.paw.interfaceServices.UserService;
import ar.edu.itba.paw.models.Client;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Specialty;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.form.DoctorForm;
import ar.edu.itba.paw.webapp.form.PatientForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Component
public class AuthUserDetailsService implements UserDetailsService {
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private ClientService clientService;
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

        if (!BCRYPT_PATTERN.matcher(user.getPassword()).matches()) {;//manera rustica de hacerlo, se deberia pedir al usuario que cambie su contraseña
            us.changePassword(user.getId(), user.getPassword());
            return loadUserByUsername(email);
        }

        if(user.getLanguage() == null){
            Locale locale = LocaleContextHolder.getLocale();
            String language = locale.getLanguage();
            us.changeLanguage(user.getId(), language);
            return loadUserByUsername(email);
        }

        if (user instanceof Doctor) {
            return new AuthUserDetails(email, user.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_DOCTOR")));
        }
        return new AuthUserDetails(email, user.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_PATIENT")));



//        final Collection<? extends GrantedAuthority> authorities = Arrays.asList(
//                new SimpleGrantedAuthority("ROLE_DOCTOR"),
//                new SimpleGrantedAuthority("ROLE_PATIENT")
//        );
//        return new AuthUserDetails(email, user.getPassword(), authorities);
    }

    public void registerDoctor(DoctorForm form)  {
        Locale locale = LocaleContextHolder.getLocale();
        Doctor doctor = doctorService.create(
                form.getName(), form.getLastName(), form.getEmail(), form.getPassword(),
                form.getPhone(), locale.getLanguage(), form.getSpecialties(),
                form.getCoverages(), form.getAvailabilitySlots()
        );
        try{
        imageService.create(doctor.getId(), form.getImage());
        }catch (IOException e){
           LOGGER.error("Error uploading image", e);
        }
        Authentication authToken = new UsernamePasswordAuthenticationToken(doctor.getEmail(), form.getPassword());
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
    public void registerPatient(PatientForm form){
        Locale locale = LocaleContextHolder.getLocale();
        Client client = clientService.create(form.getName(), form.getLastName(), form.getEmail(), form.getPassword(), form.getPhone(), locale.getLanguage(), form.getCoverage());
        Authentication authToken = new UsernamePasswordAuthenticationToken(client.getEmail(), form.getPassword());
        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
    public Boolean isLoggedUser(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated() && auth.getName().equals(user.getEmail());
    }
}