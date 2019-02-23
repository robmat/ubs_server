package edu.bator.config.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    static final String ERROR_MESSAGE_UNAUTHORIZED = "{ \"error\": \"unauthorized\" }";
    protected static final String UBS_SERVER_REALM = "ubs_server_realm";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex)
            throws IOException {
        log.debug(AuthenticationException.class.getSimpleName(), ex);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println(ERROR_MESSAGE_UNAUTHORIZED);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName(UBS_SERVER_REALM);
        super.afterPropertiesSet();
    }
}