package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.AuthUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthUserDetailsService authUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }


    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.sessionManagement()
                .invalidSessionUrl("/login")
                .and().authorizeRequests()
                .antMatchers("/login", "/register", "/{id:\\d+}").anonymous()
                .antMatchers("/admin/**").hasRole("ADMIN")
//                .antMatchers("/appointment/**").hasRole("PATIENT")
                .antMatchers("/**").authenticated()
                .and().formLogin()
                .usernameParameter("j_email")
                .passwordParameter("j_password")
                .defaultSuccessUrl("/", false)
                .loginPage("/login")
                .and().rememberMe()
                .rememberMeParameter("j_rememberme")
                .userDetailsService(authUserDetailsService)
                .key("mysupersecretketthatnobodyknowsabout") //no deberia ser constante o harcodeado. usar ssl
                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30))
                .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .and().exceptionHandling()
                .accessDeniedPage("/error/403")
                .and().csrf().disable();
    }
    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/css/**", "/js/**", "/img/**", "/favicon.ico", "/error/403");
    }
}
