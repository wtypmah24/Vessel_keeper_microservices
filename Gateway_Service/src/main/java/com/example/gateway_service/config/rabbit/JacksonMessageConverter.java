package com.example.gateway_service.config.rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

/**
 * The JacksonMessageConverter class is a custom message converter for converting messages to and from JSON format using Jackson ObjectMapper.
 * It extends Jackson2JsonMessageConverter and configures an ObjectMapper with JavaTimeModule for handling Java 8 date/time types.
 */
public class JacksonMessageConverter extends Jackson2JsonMessageConverter {
    /**
     * The ObjectMapper used for JSON serialization and deserialization.
     */
    private final ObjectMapper objectMapper;

    /**
     * Constructs a new JacksonMessageConverter with a configured ObjectMapper.
     */
    public JacksonMessageConverter() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.findAndRegisterModules();
    }

    /**
     * Converts a Message to an Object using JSON deserialization.
     *
     * @param message The message to be converted.
     * @return The deserialized Object.
     */
    @Override
    public Object fromMessage(Message message) {
        message.getMessageProperties().setContentType("application/json");
        return super.fromMessage(message);
    }
}