package com.company.bill.currencyconverter.filter;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class JwtAuthEntryPointTest {

    @InjectMocks
    private JwtAuthEntryPoint jwtAuthEntryPoint;

    @Mock
    private AuthenticationException authException;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void testCommenceShouldReturnUnauthorized() throws IOException {

        String errorMessage = "Unauthorized access";
        when(authException.getMessage()).thenReturn(errorMessage);

        jwtAuthEntryPoint.commence(request, response, authException);

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
        assertEquals("application/json", response.getContentType());

        String expectedJson = "{\"error\":\"Unauthorized\",\"message\":\"" + errorMessage + "\"}";
        assertEquals(expectedJson, response.getContentAsString());
    }
}
