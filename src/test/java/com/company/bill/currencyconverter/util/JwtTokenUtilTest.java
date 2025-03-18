package com.company.bill.currencyconverter.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class JwtTokenUtilTest {

    @Value("${secret-key}")
    private String secretKey;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    private Key signingKey;

    private static final String TEST_USER = "testUser";


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        signingKey = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    @Test
    void testSecretKeyInjection() {
        assertNotNull(secretKey);

    }

    @Test
    void testGenerateToken() {
        String token = jwtTokenUtil.generateToken(TEST_USER);
        assertNotNull(token);
        assertFalse(token.isEmpty());

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals("testUser", claims.getSubject());

    }

    @Test
    void testValidateTokenForValidToken() {
        String token = jwtTokenUtil.generateToken(TEST_USER);
        assertTrue(jwtTokenUtil.validateToken(token, TEST_USER));
    }

    @Test
    void testValidateTokenWithWrongUser() {
        String token = jwtTokenUtil.generateToken(TEST_USER);
        assertFalse(jwtTokenUtil.validateToken(token, "invalidUser"));
    }

    @Test
    void testIsExpiredToken() {
        String expiredToken = Jwts.builder()
                .setSubject(TEST_USER)
                .setIssuedAt(new Date(System.currentTimeMillis() - 10000))
                .setExpiration(new Date(System.currentTimeMillis() - 5000))
                .signWith(signingKey)
                .compact();

        assertTrue(jwtTokenUtil.isTokenExpired(expiredToken));
    }

    @Test
    void testExtractUserName() {
        String token = jwtTokenUtil.generateToken(TEST_USER);
        assertEquals(TEST_USER, jwtTokenUtil.extractUserName(token));
    }

}
