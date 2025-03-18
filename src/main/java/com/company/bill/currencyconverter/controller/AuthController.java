package com.company.bill.currencyconverter.controller;

import com.company.bill.currencyconverter.dto.auth.AuthRequest;
import com.company.bill.currencyconverter.dto.auth.AuthResponse;
import com.company.bill.currencyconverter.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JwtTokenUtil jwtTokenUtil;

    @Value("${user-name}")
    private String userName;

    @Value("${password}")
    private String password;

    public AuthController(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {

        if (userName.equalsIgnoreCase(request.userName()) && password.equalsIgnoreCase(request.password())) {

            String token = jwtTokenUtil.generateToken(request.userName());
            return ResponseEntity.ok(new AuthResponse(token));
        }

        return ResponseEntity.status(401).build();

    }

}
