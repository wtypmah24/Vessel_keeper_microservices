package com.example.authorization_service.service.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

/**
 * The TokenValidationService class provides methods for validating JWT tokens.
 */
@Service
public class TokenValidationService {
    /**
     * The decoder for decoding JWT tokens.
     */
    private final JwtDecoder decoder;

    /**
     * Constructs a new TokenValidationService with the specified JwtDecoder.
     *
     * @param decoder The decoder for decoding JWT tokens.
     */
    @Autowired
    public TokenValidationService(JwtDecoder decoder) {
        this.decoder = decoder;
    }

    /**
     * Validates the provided JWT token.
     *
     * @param token The JWT token to validate.
     * @return The role associated with the token if validation succeeds, or "NOT AUTHORIZED!" otherwise.
     */
    public String validateToken(String token) {

        try {
            Jwt jwt = decoder.decode(token);
            return jwt.getClaim("role").toString();
        } catch (Exception e) {
            return "NOT AUTHORIZED!";
        }
    }
}