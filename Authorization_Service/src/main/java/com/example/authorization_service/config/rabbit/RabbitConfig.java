package com.example.authorization_service.config.rabbit;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The RabbitConfig class contains the configuration for RabbitMQ in the authorization service.
 */
@Configuration
public class RabbitConfig {
    /**
     * The name of the queue for authentication messages.
     */
    public static final String AUTH_QUEUE = "authQueue";
    /**
     * The name of the exchange for authentication messages.
     */
    public static final String AUTH_EXCHANGE = "authExchange";

    /**
     * Creates and configures the connection factory for RabbitMQ.
     *
     * @return The configured connection factory.
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory("rabbitmq");
    }

    /**
     * Creates an instance of AmqpAdmin to interact with RabbitMQ.
     *
     * @return The AmqpAdmin instance.
     */
    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    /**
     * Creates and configures the message converter for RabbitMQ to convert messages to and from JSON format.
     *
     * @return The configured message converter.
     */
    @Bean
    public MessageConverter messageConverter() {
        JacksonMessageConverter jsonMessageConverter = new JacksonMessageConverter();
        jsonMessageConverter.setClassMapper(jsonMessageConverter.getJavaTypeMapper());

        return jsonMessageConverter;
    }

    /**
     * Creates a direct exchange for authentication messages.
     *
     * @return The direct exchange for authentication messages.
     */
    @Bean
    public DirectExchange directAuthExchange() {
        return new DirectExchange(AUTH_EXCHANGE);
    }

    /**
     * Creates the queue for authentication messages.
     *
     * @return The queue for authentication messages.
     */
    @Bean
    public Queue authQueue() {
        return new Queue(AUTH_QUEUE);
    }

    /**
     * Creates a binding between the authentication queue and the authentication exchange.
     *
     * @return The binding between the authentication queue and exchange.
     */
    @Bean
    public Binding authBinding() {
        return BindingBuilder.bind(authQueue()).to(directAuthExchange()).withQueueName();
    }
}