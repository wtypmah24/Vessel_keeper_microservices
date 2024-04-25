package com.example.authorization_service.config.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;

/**
 * The SecurityConfig class configures security settings for the authorization service.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures the security filter chain.
     *
     * @param http The HttpSecurity object.
     * @return The configured security filter chain.
     * @throws Exception If an error occurs while configuring security.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/register/**", "/login/**", "/swagger-ui.html").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .oauth2ResourceServer((oauth2ResourceServer) ->
                        oauth2ResourceServer
                                .jwt((jwt) ->
                                        {
                                            try {
                                                jwt.decoder(jwtDecoder());
                                            } catch (NoSuchAlgorithmException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                )
                );
        return http.build();
    }

    /**
     * Generates an RSA key pair for JWT decoding.
     *
     * @return The generated RSA key pair.
     * @throws NoSuchAlgorithmException If the specified algorithm for key pair generation is not available.
     */
    @Bean
    public KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * Configures the JWT decoder.
     *
     * @return The configured JWT decoder.
     * @throws NoSuchAlgorithmException If the specified algorithm for RSA key pair generation is not available.
     */
    @Bean
    public JwtDecoder jwtDecoder() throws NoSuchAlgorithmException {
        return NimbusJwtDecoder.withPublicKey((RSAPublicKey) generateRSAKeyPair().getPublic()).build();
    }

    /**
     * Configures the password encoder.
     *
     * @return The configured password encoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the JWT encoder.
     *
     * @return The configured JWT encoder.
     * @throws NoSuchAlgorithmException If the specified algorithm for RSA key pair generation is not available.
     */
    @Bean
    public JwtEncoder jwtEncoder() throws NoSuchAlgorithmException {
        JWK jwk = new RSAKey.Builder((RSAPublicKey) generateRSAKeyPair().getPublic()).privateKey(generateRSAKeyPair().getPrivate()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }
}