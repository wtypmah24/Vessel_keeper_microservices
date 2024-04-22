package com.example.crew_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for RabbitMQ settings related to crew service.
 */
@Configuration
public class RabbitConfig {
    public static final String HIRE_CREW_QUEUE = "crewQueue";
    public static final String FIRE_CREW_QUEUE = "crewQueue";
    public static final String CREW_EXCHANGE = "crewExchange";

    /**
     * Configures the connection factory for RabbitMQ.
     *
     * @return The configured connection factory
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory("rabbitmq");
    }

    /**
     * Configures the AMQP admin for RabbitMQ.
     *
     * @return The configured AMQP admin
     */
    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    /**
     * Configures the message converter for RabbitMQ to convert messages to JSON format.
     *
     * @return The configured message converter
     */
    @Bean
    public MessageConverter messageConverter() {
        JacksonMessageConverter jsonMessageConverter = new JacksonMessageConverter();
        jsonMessageConverter.setClassMapper(jsonMessageConverter.getJavaTypeMapper());

        return jsonMessageConverter;
    }

    /**
     * Configures the direct exchange for crew service.
     *
     * @return The configured direct exchange
     */
    @Bean
    public DirectExchange directCrewExchange() {
        return new DirectExchange(CREW_EXCHANGE);
    }

    /**
     * Configures the queue for hiring crew members.
     *
     * @return The configured hire crew queue
     */
    @Bean
    public Queue hireCrewQueue() {
        return new Queue(HIRE_CREW_QUEUE);
    }

    /**
     * Configures the queue for firing crew members.
     *
     * @return The configured fire crew queue
     */
    @Bean
    public Queue fireCrewQueue() {
        return new Queue(FIRE_CREW_QUEUE);
    }

    /**
     * Binds the hire crew queue to the crew exchange.
     *
     * @return The binding configuration
     */
    @Bean
    public Binding hireCrewBinding() {
        return BindingBuilder.bind(hireCrewQueue()).to(directCrewExchange()).withQueueName();
    }

    /**
     * Binds the fire crew queue to the crew exchange.
     *
     * @return The binding configuration
     */
    @Bean
    public Binding fireCrewBinding() {
        return BindingBuilder.bind(fireCrewQueue()).to(directCrewExchange()).withQueueName();
    }
}