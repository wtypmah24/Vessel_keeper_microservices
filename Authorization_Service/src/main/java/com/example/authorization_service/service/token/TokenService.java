package com.example.authorization_service.service.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * The TokenService class provides methods for generating JWT tokens.
 */
@Service
public class TokenService {
    /**
     * The encoder for encoding JWT tokens.
     */
    private final JwtEncoder encoder;

    /**
     * Constructs a new TokenService with the specified JwtEncoder.
     *
     * @param encoder The encoder for encoding JWT tokens.
     */
    @Autowired
    public TokenService(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    /**
     * Generates a JWT token based on the provided authentication.
     *
     * @param authentication The authentication object containing user details.
     * @return The generated JWT token.
     */
    public String generateToken(Authentication authentication) {

        Instant now = Instant.now();
        String userRole = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("");

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("role", userRole)
                .build();

        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
