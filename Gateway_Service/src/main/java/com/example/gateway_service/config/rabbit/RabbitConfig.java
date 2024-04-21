package com.example.gateway_service.config.rabbit;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The RabbitConfig class is responsible for configuring RabbitMQ connections and message processing in the gateway service.
 * It defines beans for ConnectionFactory, AmqpAdmin, MessageConverter, DirectExchange, Queue, and Binding.
 */
@Configuration
public class RabbitConfig {
    /**
     * The name of the authentication queue.
     */
    public static final String AUTH_QUEUE = "authQueue";
    /**
     * The name of the authentication exchange.
     */
    public static final String AUTH_EXCHANGE = "authExchange";

    /**
     * Configures the RabbitMQ connection factory.
     *
     * @return The configured ConnectionFactory.
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory("rabbitmq");
    }

    /**
     * Configures the RabbitMQ administrative interface.
     *
     * @return The configured AmqpAdmin.
     */
    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    /**
     * Configures the message converter for JSON serialization and deserialization.
     *
     * @return The configured MessageConverter.
     */
    @Bean
    public MessageConverter messageConverter() {
        JacksonMessageConverter jsonMessageConverter = new JacksonMessageConverter();
        jsonMessageConverter.setClassMapper(jsonMessageConverter.getJavaTypeMapper());

        return jsonMessageConverter;
    }

    /**
     * Configures the direct exchange for authentication messages.
     *
     * @return The configured DirectExchange.
     */
    @Bean
    public DirectExchange directAuthExchange() {
        return new DirectExchange(AUTH_EXCHANGE);
    }

    /**
     * Configures the authentication queue.
     *
     * @return The configured Queue.
     */
    @Bean
    public Queue authQueue() {
        return new Queue(AUTH_QUEUE);
    }

    /**
     * Configures the binding between the authentication queue and exchange.
     *
     * @return The configured Binding.
     */
    @Bean
    public Binding authBinding() {
        return BindingBuilder.bind(authQueue()).to(directAuthExchange()).withQueueName();
    }
}