package com.company.bill.currencyconverter.api.util;

import com.company.bill.currencyconverter.api.exception.BillCurrencyConverterApiException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.util.Date;

@Component
public class JwtTokenUtil {

    private static final long EXPIRATION_TIME = 86400000;

    private static final String SECRET_KEY = "this-is-the-256-bit-secret-for-this-application";
    private final Key key = getPrivateKey();

    public  String generateToken(String userName){

        return Jwts.builder()
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean  validateToken(String token, String userName){
        return userName.equalsIgnoreCase(extractUserName(token)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    public String extractUserName(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    protected SecretKey getPrivateKey() {
       return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    }


}
