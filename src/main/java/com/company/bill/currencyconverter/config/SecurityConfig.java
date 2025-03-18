package com.company.bill.currencyconverter.config;

import com.company.bill.currencyconverter.filter.JwtAuthEntryPoint;
import com.company.bill.currencyconverter.filter.JwtFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final JwtAuthEntryPoint authEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // Disable CSRF protection (only for stateless APIs)
                .csrf(csrf -> csrf.ignoringRequestMatchers("/auth/**")  // CSRF protection disabled for login and auth endpoints
                )
                // Authorization configuration
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/auth/login").permitAll()  // Allow login without authentication
                        .anyRequest().authenticated()  // All other requests need authentication
                )
                // Session management - stateless for API endpoints (e.g., JWT-based authentication)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // No HTTP sessions for stateless API
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authEntryPoint)  // Custom entry point for unauthorized requests
                )

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }


}
