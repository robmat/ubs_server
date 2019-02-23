package edu.bator.config.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AccountExpiredException;

import static edu.bator.config.security.AuthenticationEntryPoint.ERROR_MESSAGE_UNAUTHORIZED;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class AuthenticationEntryPointTest {

    AuthenticationEntryPoint authenticationEntryPoint = new AuthenticationEntryPoint();

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    PrintWriter printWriter;

    @BeforeEach
    void setUp() throws IOException {
        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    @DisplayName("Notify client (vaguely) about authentication failure")
    void commence() throws IOException {
        authenticationEntryPoint.commence(request, response, new AccountExpiredException("expired"));

        verify(printWriter).println(ERROR_MESSAGE_UNAUTHORIZED);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}