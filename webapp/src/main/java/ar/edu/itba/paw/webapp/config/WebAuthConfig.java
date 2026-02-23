package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.AuthUserDetailsService;
import ar.edu.itba.paw.webapp.filter.BasicFilter;
import ar.edu.itba.paw.webapp.auth.JwtService;
import ar.edu.itba.paw.webapp.filter.JwtTokenFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import ar.edu.itba.paw.webapp.utils.UriUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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
import java.util.*;

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

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Autowired
    public WebAuthConfig(@Lazy AuthUserDetailsService authUserDetailsService) {
        this.authUserDetailsService = authUserDetailsService;
    }

    @Autowired
    private MessageSource messageSource;
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
                .antMatchers(HttpMethod.POST,UriUtils.USERS).permitAll()



                .antMatchers(HttpMethod.GET, UriUtils.APPOINTMENTS).access("(isAuthenticated() AND @accessHandler.checkIllegalParams(request)) OR (hasRole('DOCTOR') AND @accessHandler.canSeeHistory(authentication, request))")
                .antMatchers(HttpMethod.GET, UriUtils.APPOINTMENTS +"/{id:\\d+}/**").access("@accessHandler.canHandleAppointment(authentication, #id) OR (hasRole('DOCTOR') AND @accessHandler.canSeeMedicalHistoryApp(authentication, #id))")
                .antMatchers(HttpMethod.PATCH, UriUtils.APPOINTMENTS +"/{id:\\d+}").access("@accessHandler.canHandleAppointment(authentication, #id)")
                .antMatchers(HttpMethod.PUT, UriUtils.APPOINTMENTS +"/{id:\\d+}/report").access("hasRole('DOCTOR') and @accessHandler.canHandleAppointment(authentication, #id)")
                .antMatchers(HttpMethod.POST, UriUtils.APPOINTMENTS +"/{id:\\d+}/files/patient").access("hasRole('PATIENT') and @accessHandler.canHandleAppointment(authentication, #id)")
                .antMatchers(HttpMethod.POST, UriUtils.APPOINTMENTS +"/{id:\\d+}/files/doctor").access("hasRole('DOCTOR') and @accessHandler.canHandleAppointment(authentication, #id)")

                .antMatchers(HttpMethod.GET, UriUtils.DOCTORS).access("isAnonymous() or hasRole('PATIENT')")
                .antMatchers(HttpMethod.HEAD, UriUtils.DOCTORS).permitAll()
                .antMatchers(HttpMethod.GET, UriUtils.DOCTORS + "/{id:\\d+}").access("isAnonymous() or hasRole('PATIENT') or (hasRole('DOCTOR') and @accessHandler.isUser(authentication, #id))")
                .antMatchers(HttpMethod.GET, UriUtils.DOCTORS + "/{id:\\d+}/**").access("isAnonymous() or hasRole('PATIENT') or (hasRole('DOCTOR') and @accessHandler.isUser(authentication, #id))")
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
                .antMatchers(HttpMethod.GET, "/**/image").permitAll()
                .antMatchers(UriUtils.COVERAGES +"/**").permitAll()
                .antMatchers(UriUtils.SPECIALTIES + "/**").permitAll()
                .antMatchers( HttpMethod.GET,UriUtils.NEIGHBORHOODS +"/**").permitAll()
                .antMatchers("/**").permitAll()


                .and()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, ex) -> {
            String jwtError = (String) request.getAttribute("jwt_error");
            String jwtErrorDesc = (String) request.getAttribute("jwt_error_desc");
            StringBuilder authHeader = new StringBuilder("Bearer realm=\"Vitae\"");
            if (jwtError != null) {
                authHeader.append(", error=\"").append(jwtError).append("\"");
                if (jwtErrorDesc != null) {
                    authHeader.append(", error_description=\"").append(jwtErrorDesc).append("\"");
                }
            }
            response.setHeader(HttpHeaders.WWW_AUTHENTICATE, authHeader.toString());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String message;
            try {
                message = messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale());
            } catch (Exception e) {
                message = ex.getMessage();
            }
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", message);
            objectMapper.writeValue(response.getWriter(), errorResponse);
        })
                .accessDeniedHandler((request, response, ex) -> {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    String message;
                    try {
                        message = messageSource.getMessage("error.accessDenied", null, "Access denied. You do not have the necessary permissions.", LocaleContextHolder.getLocale());
                    } catch (Exception e) {
                        message = "Access denied. You do not have the necessary permissions.";
                    }
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("message", message);
                    objectMapper.writeValue(response.getWriter(), errorResponse);
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
                .antMatchers("/css/**", "/js/**", "/img/**", "/favicon.ico", "/src/main/favicon.ico", "/error/**");
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

        config.setAllowCredentials(true);

        config.setExposedHeaders(Arrays.asList(
                HEADER_ACCESS_TOKEN,
                HEADER_REFRESH_TOKEN,
                "Location",
                "Link",
                "WWW-Authenticate",
                "X-Total-Count",
                "ETag",
                "Last-Modified",
                "Content-Disposition"
        ));

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
