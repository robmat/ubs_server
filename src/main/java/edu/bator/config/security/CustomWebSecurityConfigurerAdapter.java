package edu.bator.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class CustomWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    private static final String BASE_ROLE = "USER";
    private static final String USERNAME = "ubs";
    private static final String UBS_PASSWD = "ubs_passwd";
    private static final String ACTUATOR_PATH = "/actuator/**";

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(USERNAME).password(passwordEncoder().encode(UBS_PASSWD))
                .authorities(BASE_ROLE);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(ACTUATOR_PATH)
                    .permitAll()
                .antMatchers(HttpMethod.OPTIONS)
                    .permitAll()
                .anyRequest()
                        .authenticated()
                    .and()
                        .httpBasic()
                        .realmName(AuthenticationEntryPoint.UBS_SERVER_REALM);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}