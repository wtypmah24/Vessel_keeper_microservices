package com.example.authorization_service.config.rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

/**
 * The JacksonMessageConverter class represents a custom message converter for AMQP,
 * which uses Jackson for serialization and deserialization of objects to JSON format.
 */
public class JacksonMessageConverter extends Jackson2JsonMessageConverter {
    private final ObjectMapper objectMapper;

    /**
     * Constructor for JacksonMessageConverter class.
     * Initializes the ObjectMapper with a registered JavaTimeModule module for proper
     * serialization and deserialization of objects with dates and times in JSON format.
     */
    public JacksonMessageConverter() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.findAndRegisterModules();
    }

    /**
     * The fromMessage method is overridden to set the Content-Type of the message to "application/json",
     * to ensure proper handling of JSON messages when sending and receiving them.
     *
     * @param message The AMQP message to be converted to an object.
     * @return The object created from the content of the AMQP message.
     */
    @Override
    public Object fromMessage(Message message) {
        message.getMessageProperties().setContentType("application/json");
        return super.fromMessage(message);
    }
}