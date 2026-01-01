package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.AuthUserDetailsService;
import ar.edu.itba.paw.webapp.auth.BasicFilter;
import ar.edu.itba.paw.webapp.auth.JwtService;
import ar.edu.itba.paw.webapp.auth.JwtTokenFilter;
import ar.edu.itba.paw.webapp.handler.AuthEntryPointHandler;
import ar.edu.itba.paw.webapp.handler.CustomAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static ar.edu.itba.paw.webapp.auth.AuthUtils.HEADER_ACCESS_TOKEN;
import static ar.edu.itba.paw.webapp.auth.AuthUtils.HEADER_REFRESH_TOKEN;

@EnableWebSecurity
@ComponentScan("ar.edu.itba.paw.webapp.auth")
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

    private final AuthUserDetailsService authUserDetailsService;
    private final String rememberMeSecretKey;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Autowired
    private BasicFilter basicFilter;

    @Autowired
    public WebAuthConfig(@Lazy AuthUserDetailsService authUserDetailsService, String rememberMeSecretKey) {
        this.authUserDetailsService = authUserDetailsService;
        this.rememberMeSecretKey = rememberMeSecretKey;
    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }


//    @Override
//    protected void configure(final HttpSecurity http) throws Exception {
//        http.sessionManagement()
//                .invalidSessionUrl("/")
//                .and()
//                .authorizeRequests()
//                .antMatchers("/", "/verify", "/verify-confirmation", "/verify-result", "/email-sent", "/image/*", "/recover-password", "/change-password", "/change-password-result", "/about-us").permitAll()
//                .antMatchers("/search", "/search/doctors/*").access("isAnonymous() or hasRole('PATIENT')")
//                .antMatchers("/search/{id}").access("isAnonymous() or hasRole('PATIENT') or (hasRole('DOCTOR') and @accessHandler.canAccessDoctorProfile(authentication, #id))")
//                .antMatchers("/search/").access("isAnonymous() or hasRole('PATIENT') or !hasRole('DOCTOR')")
//                .antMatchers("/login", "/register-patient", "/register", "/email-sent").anonymous()
//                .antMatchers("/doctor/appointments/{id}/history").access("hasRole('DOCTOR') and @accessHandler.canSeeMedicalHistory(authentication,#id)")
//                .antMatchers("/doctor/dashboard/appointment-details/{id}").access("@accessHandler.canHandleAppointment(authentication, #id) and hasRole('DOCTOR')")
//                .antMatchers( "/appointment/{id}/file/").access("@accessHandler.canHandleAppointment(authentication, #id)")
//                .antMatchers("/patient/dashboard/appointment-details/{id}").access("@accessHandler.canHandleAppointment(authentication, #id) and hasRole('PATIENT')")
//                .antMatchers("doctor/dashboard/appointment/cancel").access("@accessHandler.canHandleAppointment(authentication, request.getParameter('appointmentId')) and hasRole('DOCTOR')")
//                .antMatchers( "patient/dashboard/appointment/cancel").access("@accessHandler.canHandleAppointment(authentication, request.getParameter('appointmentId')) and hasRole('PATIENT')")
//                .antMatchers("/appointment/confirmation/{id}").access("hasRole('PATIENT') and @accessHandler.canHandleAppointment(authentication, #id)")
//                .antMatchers("/appointment").access("hasRole('PATIENT') ")
//                .antMatchers("/appointment/{appointmentId}/file-view/{fileId}", "/appointment/{appointmentId}/file/{fileId}").access("@accessHandler.hasEnabledFullMedicalHistory(authentication, #appointmentId)")
//                .antMatchers("/doctor/dashboard/unavailability/**").access("hasRole('DOCTOR') and @accessHandler.canHandleUnavailability(request)")
//                .antMatchers("appointment/doctor/*/availability").access("hasRole('PATIENT')")
//                .antMatchers("/doctor/**").hasRole("DOCTOR")
//                .antMatchers("/patient/**").hasRole("PATIENT")
//                .antMatchers("/appointment/*/file/*").authenticated()
//                .antMatchers("/doctors/**").anonymous()
//                .antMatchers("/patients/**").anonymous()
//                .antMatchers("/images/**").anonymous()
//                .antMatchers("/appointments/**").anonymous()
//                .antMatchers("/neighborhoods/**").anonymous()
//                .antMatchers("/coverages/**").anonymous()
//                .antMatchers("/specialties/**").anonymous()
//                .antMatchers("/ratings/**").anonymous()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .usernameParameter("j_email")
//                .passwordParameter("j_password")
//                .loginPage("/login")
//                .successHandler(customAuthenticationSuccessHandler())
//                .and()
//                .rememberMe()
//                .rememberMeParameter("j_rememberme")
//                .userDetailsService(authUserDetailsService)
//                .key(rememberMeSecretKey)
//                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30))
//                .and()
//                .logout()
//                .logoutUrl("/logout")
//                .logoutSuccessUrl("/login")
//                .and()
//                .exceptionHandling()
//                    .authenticationEntryPoint(authEntryPointHandler())
//                .and()
//                .csrf()
//                .disable();
//    }

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                // TODO: AGREGAR LAS RUTAS
                .antMatchers("/images/**").permitAll()
                .antMatchers("/coverages/**").permitAll()
                .antMatchers("/specialties/**").permitAll()
                .antMatchers("/neighborhoods/**").permitAll()
                .antMatchers(HttpMethod.POST, "/patients").permitAll()
                .antMatchers(HttpMethod.POST, "/doctors").permitAll()
                .antMatchers("/**").authenticated()

                .and()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, ex) -> {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
                })

                .and().headers().cacheControl().disable()

                .and()
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(basicFilter, UsernamePasswordAuthenticationFilter.class)

                .cors().and().csrf().disable();
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/css/**", "/js/**", "/img/**", "/favicon.ico", "/src/main/favicon.ico", "image/*", "**/image", "/error/**");
    }

    @Bean
    public CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public AuthEntryPointHandler authEntryPointHandler() {
        return new AuthEntryPointHandler();
    }

    @Bean
    public JwtService jwtTokenUtil(@Value("classpath:jwtSecret.key") Resource jwtKeyRes) throws IOException {
        return new JwtService(jwtKeyRes);
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setAllowedMethods(Arrays.asList("GET", "HEAD", "POST", "OPTIONS", "DELETE", "PUT", "PATCH"));

       //TODO: REMOVE THIS IN PRODUCTION
        config.setAllowedOrigins(List.of("http://localhost:5173"));

        config.setAllowCredentials(true);

        config.setExposedHeaders(Arrays.asList(
                HEADER_ACCESS_TOKEN,
                HEADER_REFRESH_TOKEN,
                "Location",
                "Link",
                "WWW-Authenticate"
        ));

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
