package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.AuthUserDetailsService;
import ar.edu.itba.paw.webapp.handler.CustomAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.concurrent.TimeUnit;

@EnableWebSecurity
@ComponentScan("ar.edu.itba.paw.webapp.auth")
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthUserDetailsService authUserDetailsService;

    @Autowired
    private String rememberMeSecretKey;
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


    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.sessionManagement()
                .invalidSessionUrl("/")
                    .and()
                .authorizeRequests()
                .antMatchers("/","/verify","/verify-confirmation","/verify-result","/email-sent").permitAll()
                    .antMatchers( "/login", "/register-patient", "/register","/recover-password", "/change-password","/recover-password","/email-sent").anonymous()
                    .antMatchers("/portal", "/search", "/appointment/booked-times-by-date").access("isAnonymous() or hasRole('PATIENT')")
                    .antMatchers("/doctor/dashboard/appointment-details/{id}", "/patient/dashboard/appointment-details/{id}", "/appointment/{id}/file/").access("@accessHandler.canHandleAppointment(authentication, #id)")
                    .antMatchers("doctor/dashboard/appointment/cancel", "patient/dashboard/appointment/cancel").access("@accessHandler.canHandleAppointment(authentication, request.getParameter('appointmentId'))")
                    .antMatchers("/appointment/**").hasRole("PATIENT")
                    .antMatchers("/doctor/**").hasRole("DOCTOR")
                    .antMatchers("/patient/**").hasRole("PATIENT")
                    .antMatchers("/appointment/*/file/*").authenticated()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .usernameParameter("j_email")
                    .passwordParameter("j_password")
                    .loginPage("/login")
                    .successHandler(customAuthenticationSuccessHandler())
                    .and()
                .rememberMe()
                    .rememberMeParameter("j_rememberme")
                    .userDetailsService(authUserDetailsService)
                    .key(rememberMeSecretKey)
                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30))
                    .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login")
                    .and()
                .exceptionHandling()
                    .accessDeniedPage("/error/403")
                    .and()
                .csrf()
                    .disable();
    }
    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/css/**", "/js/**", "/img/**","/favicon.ico",  "/src/main/favicon.ico","image/*","**/image", "/error/**" );
    }

    @Bean
    public CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }
}
