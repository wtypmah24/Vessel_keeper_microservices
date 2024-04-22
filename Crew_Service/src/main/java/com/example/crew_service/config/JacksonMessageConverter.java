package com.example.crew_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

/**
 * Custom Jackson message converter for converting messages to JSON format.
 * Extends Jackson2JsonMessageConverter provided by Spring AMQP.
 */
public class JacksonMessageConverter extends Jackson2JsonMessageConverter {
    private final ObjectMapper objectMapper;

    /**
     * Constructs a new JacksonMessageConverter and configures the ObjectMapper with JavaTimeModule.
     */
    public JacksonMessageConverter() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.findAndRegisterModules();
    }

    /**
     * Converts a message to JSON format.
     * Sets the content type of the message to "application/json".
     *
     * @param message The message to convert
     * @return The converted object
     */
    @Override
    public Object fromMessage(Message message) {
        message.getMessageProperties().setContentType("application/json");
        return super.fromMessage(message);
    }
}