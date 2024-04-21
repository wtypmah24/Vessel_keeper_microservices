package com.example.gateway_service.service;

import com.example.gateway_service.config.rabbit.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

/**
 * The TokenCheckService class is responsible for sending token verification requests to the authentication service via RabbitMQ.
 * It utilizes a RabbitTemplate to send and receive messages.
 */
@Service
public class TokenCheckService {
    private final RabbitTemplate rabbitTemplate;

    /**
     * Constructs a new TokenCheckService with the specified RabbitTemplate.
     *
     * @param rabbitTemplate The RabbitTemplate for interacting with RabbitMQ.
     */
    @Autowired
    public TokenCheckService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Sends a token verification request to the authentication service and retrieves the user's role.
     *
     * @param token The token to be verified.
     * @return The role of the user associated with the token.
     */
    public String getRole(String token) {
        return rabbitTemplate
                .convertSendAndReceiveAsType(
                        RabbitConfig.AUTH_QUEUE, token, ParameterizedTypeReference.forType(String.class)
                );
    }
}
