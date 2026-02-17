package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.AuthUserDetailsService;
import ar.edu.itba.paw.webapp.filter.BasicFilter;
import ar.edu.itba.paw.webapp.auth.JwtService;
import ar.edu.itba.paw.webapp.filter.JwtTokenFilter;
import ar.edu.itba.paw.webapp.handler.AuthEntryPointHandler;
import ar.edu.itba.paw.webapp.handler.CustomAuthenticationSuccessHandler;
import ar.edu.itba.paw.webapp.utils.UriUtils;
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

import static ar.edu.itba.paw.webapp.auth.AuthUtils.HEADER_ACCESS_TOKEN;
import static ar.edu.itba.paw.webapp.auth.AuthUtils.HEADER_REFRESH_TOKEN;

@EnableWebSecurity
@ComponentScan("ar.edu.itba.paw.webapp.auth")
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

    private final AuthUserDetailsService authUserDetailsService;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Autowired
    private BasicFilter basicFilter;

    @Autowired
    public WebAuthConfig(@Lazy AuthUserDetailsService authUserDetailsService) {
        this.authUserDetailsService = authUserDetailsService;
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



    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                // TODO: AGREGAR LAS RUTAS
                .antMatchers(HttpMethod.POST,UriUtils.USERS).permitAll()



                .antMatchers(HttpMethod.GET, UriUtils.APPOINTMENTS).access("@accessHandler.isUserQuery(authentication, request) OR hasRole('DOCTOR') AND @accessHandler.canSeeHistory(authentication, request) ")
                .antMatchers(HttpMethod.GET, UriUtils.APPOINTMENTS +"/{id:\\d+}/**").access("@accessHandler.canHandleAppointment(authentication, #id) OR (hasRole('DOCTOR') AND @accessHandler.canSeeMedicalHistoryApp(authentication, #id))")
                .antMatchers(HttpMethod.PATCH, UriUtils.APPOINTMENTS +"/{id:\\d+}").access("@accessHandler.canHandleAppointment(authentication, #id)")
                .antMatchers(HttpMethod.PUT, UriUtils.APPOINTMENTS +"/{id:\\d+}/report").access("hasRole('DOCTOR') and @accessHandler.canHandleAppointment(authentication, #id)")
                .antMatchers(HttpMethod.POST, UriUtils.APPOINTMENTS +"/{id:\\d+}/files/patient").access("hasRole('PATIENT') and @accessHandler.canHandleAppointment(authentication, #id)")
                .antMatchers(HttpMethod.POST, UriUtils.APPOINTMENTS +"/{id:\\d+}/files/doctor").access("hasRole('DOCTOR') and @accessHandler.canHandleAppointment(authentication, #id)")

                .antMatchers(HttpMethod.GET, UriUtils.DOCTORS +"/**").permitAll()
                .antMatchers(HttpMethod.HEAD, UriUtils.DOCTORS).permitAll()
                .antMatchers(HttpMethod.POST, UriUtils.DOCTORS).permitAll()
                .antMatchers(HttpMethod.PATCH, UriUtils.DOCTORS + "/{id:\\d+}").access("hasRole('DOCTOR') and @accessHandler.isUser(authentication, #id)")
                .antMatchers(HttpMethod.PUT, UriUtils.DOCTORS +"/{id:\\d+}/**").access("hasRole('DOCTOR') and @accessHandler.isUser(authentication, #id)")
                .antMatchers(HttpMethod.POST,UriUtils.DOCTORS +"/{id:\\d+}/offices").access("hasRole('DOCTOR') and @accessHandler.isUser(authentication,#id)")
                .antMatchers(HttpMethod.PATCH,UriUtils.DOCTORS +"/{id:\\d+}/offices/{officeId:\\d+}").access("hasRole('DOCTOR') and @accessHandler.isUser(authentication,#id) and @accessHandler.hasOfficeOwnership(authentication, #officeId)")
                .antMatchers(HttpMethod.DELETE,UriUtils.DOCTORS +"/{id:\\d+}/offices/{officeId:\\d+}").access("hasRole('DOCTOR') and @accessHandler.isUser(authentication,#id ) and @accessHandler.hasOfficeOwnership(authentication, #officeId)")

                .antMatchers(HttpMethod.POST, UriUtils.PATIENTS).permitAll()
                .antMatchers(HttpMethod.HEAD, UriUtils.PATIENTS).permitAll()
                .antMatchers(HttpMethod.PATCH,UriUtils.PATIENTS +"/{id:\\d+}").access("hasRole('PATIENT') and @accessHandler.isUser(authentication, #id)")
                .antMatchers(HttpMethod.GET, UriUtils.PATIENTS +"/{id:\\d+}").access("@accessHandler.isUser(authentication, #id) or (hasRole('DOCTOR') and @accessHandler.hasAppointmentWithPatient(authentication, #id))")
                .antMatchers(HttpMethod.POST,UriUtils.RATINGS).access("hasRole('PATIENT')")
                .antMatchers(HttpMethod.GET, UriUtils.RATINGS +"/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers(UriUtils.COVERAGES +"/**").permitAll()
                .antMatchers(UriUtils.SPECIALTIES + "/**").permitAll()
                .antMatchers( HttpMethod.GET,UriUtils.NEIGHBORHOODS +"/**").permitAll()
                .antMatchers("/**").permitAll()

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
                "WWW-Authenticate",
                "X-Total-Count"
        ));

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
