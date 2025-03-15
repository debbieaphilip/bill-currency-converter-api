package com.company.bill.currencyconverter.api.controller;

import com.company.bill.currencyconverter.api.dto.auth.AuthRequest;
import com.company.bill.currencyconverter.api.dto.auth.AuthResponse;
import com.company.bill.currencyconverter.api.util.JwtTokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final JwtTokenUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {

        if ("user".equalsIgnoreCase(request.userName()) && "password".equalsIgnoreCase(request.password())) {

            String token = jwtUtil.generateToken(request.userName());
            return ResponseEntity.ok(new AuthResponse(token));
        }

        return ResponseEntity.status(401).build();

    }

}
