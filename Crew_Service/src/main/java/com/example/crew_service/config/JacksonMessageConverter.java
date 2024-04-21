package com.example.crew_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

public class JacksonMessageConverter extends Jackson2JsonMessageConverter {
    private final ObjectMapper objectMapper;

    public JacksonMessageConverter() {
        this.objectMapper = new ObjectMapper();
        // Регистрация модуля для работы с LocalDate
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.findAndRegisterModules();
    }

    @Override
    public Object fromMessage(Message message) {
        message.getMessageProperties().setContentType("application/json");
        return super.fromMessage(message);
    }
}