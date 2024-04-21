package com.example.authorization_service.service.auth;

import com.example.authorization_service.config.rabbit.RabbitConfig;
import com.example.authorization_service.service.token.TokenValidationService;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * The AuthService class provides authentication-related services.
 * It listens for messages containing authentication tokens from RabbitMQ and validates the tokens.
 */
@Service
@EnableRabbit
public class AuthService {
    /**
     * The service for token validation.
     */
    private final TokenValidationService validationService;

    /**
     * Constructs a new AuthService with the specified TokenValidationService.
     *
     * @param validationService The service for token validation.
     */
    @Autowired
    public AuthService(TokenValidationService validationService) {
        this.validationService = validationService;
    }

    /**
     * Listens for messages containing authentication tokens from the RabbitMQ queue specified in the RabbitConfig.
     * Validates the received tokens using the TokenValidationService.
     *
     * @param token The authentication token received from the message queue.
     * @return The role associated with the validated token.
     */
    @RabbitListener(queues = RabbitConfig.AUTH_QUEUE)
    public String getRole(@Payload String token) {
        return validationService.validateToken(token);
    }
}