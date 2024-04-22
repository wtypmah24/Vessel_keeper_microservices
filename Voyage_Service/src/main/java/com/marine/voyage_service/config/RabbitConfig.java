package com.marine.voyage_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for RabbitMQ setup in the Voyage service.
 */
@Configuration
public class RabbitConfig {
    /**
     * Name of the queue for voyage messages.
     */
    public static final String VOYAGE_QUEUE = "voyageQueue";
    /**
     * Name of the queue for vessel messages.
     */
    public static final String VESSEL_QUEUE = "vesselQueue";
    /**
     * Name of the exchange for voyage messages.
     */
    public static final String VOYAGE_EXCHANGE = "voyageExchange";

    /**
     * Bean definition for creating a RabbitMQ ConnectionFactory.
     *
     * @return the RabbitMQ ConnectionFactory
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory("rabbitmq");
    }

    /**
     * Bean definition for creating a RabbitMQ AmqpAdmin.
     *
     * @return the RabbitMQ AmqpAdmin
     */
    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    /**
     * Bean definition for creating a JacksonMessageConverter for JSON message conversion.
     *
     * @return the JacksonMessageConverter
     */
    @Bean
    public MessageConverter messageConverter() {
        JacksonMessageConverter jsonMessageConverter = new JacksonMessageConverter();
        jsonMessageConverter.setClassMapper(jsonMessageConverter.getJavaTypeMapper());

        return jsonMessageConverter;
    }

    /**
     * Bean definition for creating a DirectExchange for voyage messages.
     *
     * @return the DirectExchange for voyage messages
     */
    @Bean
    public DirectExchange directVoyageExchange() {
        return new DirectExchange(VOYAGE_EXCHANGE);
    }

    /**
     * Bean definition for creating a queue for voyage messages.
     *
     * @return the queue for voyage messages
     */
    @Bean
    public Queue voyageQueue() {
        return new Queue(VOYAGE_QUEUE);
    }

    /**
     * Bean definition for creating a queue for vessel messages.
     *
     * @return the queue for vessel messages
     */
    @Bean
    public Queue vesselQueue() {
        return new Queue(VESSEL_QUEUE);
    }

    /**
     * Bean definition for creating a binding between the voyage queue and exchange.
     *
     * @return the binding between voyage queue and exchange
     */
    @Bean
    public Binding voyageBindind() {
        return BindingBuilder.bind(voyageQueue()).to(directVoyageExchange()).withQueueName();
    }

    /**
     * Bean definition for creating a binding between the vessel queue and exchange.
     *
     * @return the binding between vessel queue and exchange
     */
    @Bean
    public Binding vesselBinding() {
        return BindingBuilder.bind(vesselQueue()).to(directVoyageExchange()).withQueueName();
    }
}