package com.example.crew_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String HIRE_CREW_QUEUE = "crewQueue";
    public static final String FIRE_CREW_QUEUE = "crewQueue";
    public static final String CREW_EXCHANGE = "crewExchange";

    @Bean
    public ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory("rabbitmq");
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public MessageConverter messageConverter() {
        JacksonMessageConverter jsonMessageConverter = new JacksonMessageConverter();
        jsonMessageConverter.setClassMapper(jsonMessageConverter.getJavaTypeMapper());

        return jsonMessageConverter;
    }

    @Bean
    public DirectExchange directCrewExchange() {
        return new DirectExchange(CREW_EXCHANGE);
    }

    @Bean
    public Queue hireCrewQueue() {
        return new Queue(HIRE_CREW_QUEUE);
    }

    @Bean
    public Queue fireCrewQueue() {
        return new Queue(FIRE_CREW_QUEUE);
    }

    @Bean
    public Binding hireCrewBinding() {
        return BindingBuilder.bind(hireCrewQueue()).to(directCrewExchange()).withQueueName();
    }

    @Bean
    public Binding fireCrewBinding() {
        return BindingBuilder.bind(fireCrewQueue()).to(directCrewExchange()).withQueueName();
    }
}