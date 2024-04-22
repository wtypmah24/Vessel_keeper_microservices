package com.marine.voyage_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

/**
 * Custom message converter for RabbitMQ messages using Jackson.
 * This converter extends Jackson2JsonMessageConverter to customize the ObjectMapper
 * and handle conversion between Message and JSON.
 */
public class JacksonMessageConverter extends Jackson2JsonMessageConverter {
    /**
     * ObjectMapper instance for JSON serialization and deserialization.
     */
    private final ObjectMapper objectMapper;

    /**
     * Constructs a new JacksonMessageConverter with a customized ObjectMapper.
     */
    public JacksonMessageConverter() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.findAndRegisterModules();
    }

    /**
     * Converts a RabbitMQ Message to a Java object using Jackson.
     * Overrides the fromMessage method of Jackson2JsonMessageConverter
     * to set the content type of the message to "application/json".
     *
     * @param message the RabbitMQ Message to convert
     * @return the Java object converted from the message
     */

    @Override
    public Object fromMessage(Message message) {
        message.getMessageProperties().setContentType("application/json");
        return super.fromMessage(message);
    }
}