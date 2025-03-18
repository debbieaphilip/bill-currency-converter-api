package com.company.bill.currencyconverter.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenUtil {

    private static final long EXPIRATION_TIME = 86400000;

    @Value("${secret-key}")
    private String secretKey;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = getPrivateKey(); // Initialize key after secretKey is injected
    }

    public String generateToken(String userName) {

        return Jwts.builder()
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getPrivateKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token, String userName) {
        return userName.equalsIgnoreCase(extractUserName(token)) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
        } catch (ExpiredJwtException e) {
            return true;
        }
        return false;
    }

    public String extractUserName(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    protected SecretKey getPrivateKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

}
