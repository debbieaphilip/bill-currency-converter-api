package com.company.bill.currencyconverter.filter;

import com.company.bill.currencyconverter.util.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class JwtFilterTest {

    @Mock
    private JwtTokenUtil jwtTokenUtil;
    @InjectMocks
    private JwtFilter jwtFilter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    private static final String TEST_USER = "testUser";
    private static final String VALID_TOKEN = "validToken";
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        when(jwtTokenUtil.extractUserName(anyString())).thenReturn(TEST_USER);
        when(jwtTokenUtil.validateToken(anyString(), eq(TEST_USER))).thenReturn(true);

        SecurityContextHolder.clearContext();
    }

    @Test
    void testFilterForValidToken() throws ServletException, IOException {
        request.addHeader(AUTHORIZATION, BEARER + VALID_TOKEN);
        jwtFilter.doFilter(request, response, filterChain);
        verify(filterChain, times(1)).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(TEST_USER, (SecurityContextHolder.getContext().getAuthentication()).getName());
    }


}
