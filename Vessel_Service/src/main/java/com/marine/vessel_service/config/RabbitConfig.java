package com.marine.vessel_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The RabbitConfig class is responsible for configuring RabbitMQ connections, exchanges, queues, and bindings.
 */
@Configuration
public class RabbitConfig {
    /**
     * The name of the queue for handling voyage-related messages.
     */
    public static final String VOYAGE_QUEUE = "voyageQueue";
    /**
     * The name of the queue for handling vessel-related messages.
     */
    public static final String VESSEL_QUEUE = "vesselQueue";
    /**
     * The name of the queue for hiring crew members.
     */
    public static final String HIRE_CREW_QUEUE = "crewQueue";
    /**
     * The name of the queue for firing crew members.
     */
    public static final String FIRE_CREW_QUEUE = "crewQueue";
    /**
     * The name of the exchange for crew-related messages.
     */
    public static final String CREW_EXCHANGE = "crewExchange";
    /**
     * The name of the exchange for voyage-related messages.
     */
    public static final String VOYAGE_EXCHANGE = "voyageExchange";

    /**
     * Configures and provides a RabbitMQ ConnectionFactory bean.
     *
     * @return A RabbitMQ ConnectionFactory bean.
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory("rabbitmq");
    }

    /**
     * Configures and provides a RabbitMQ AmqpAdmin bean.
     *
     * @return A RabbitMQ AmqpAdmin bean.
     */
    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    /**
     * Configures and provides a JacksonMessageConverter bean as the message converter for RabbitMQ.
     *
     * @return A JacksonMessageConverter bean.
     */
    @Bean
    public MessageConverter messageConverter() {
        JacksonMessageConverter jsonMessageConverter = new JacksonMessageConverter();
        jsonMessageConverter.setClassMapper(jsonMessageConverter.getJavaTypeMapper());

        return jsonMessageConverter;
    }

    /**
     * Configures and provides a DirectExchange bean for voyage-related messages.
     *
     * @return A DirectExchange bean for voyage-related messages.
     */
    @Bean
    public DirectExchange directVoyageExchange() {
        return new DirectExchange(VOYAGE_EXCHANGE);
    }

    /**
     * Configures and provides a Queue bean for voyage-related messages.
     *
     * @return A Queue bean for voyage-related messages.
     */
    @Bean
    public Queue voyageQueue() {
        return new Queue(VOYAGE_QUEUE);
    }

    /**
     * Configures and provides a Queue bean for vessel-related messages.
     *
     * @return A Queue bean for vessel-related messages.
     */
    @Bean
    public Queue vesselQueue() {
        return new Queue(VESSEL_QUEUE);
    }

    /**
     * Configures and provides a Binding bean to bind the voyage queue to the voyage exchange.
     *
     * @return A Binding bean for binding the voyage queue to the voyage exchange.
     */
    @Bean
    public Binding voyageBindind() {
        return BindingBuilder.bind(voyageQueue()).to(directVoyageExchange()).withQueueName();
    }

    /**
     * Configures and provides a Binding bean to bind the vessel queue to the voyage exchange.
     *
     * @return A Binding bean for binding the vessel queue to the voyage exchange.
     */
    @Bean
    public Binding vesselBinding() {
        return BindingBuilder.bind(vesselQueue()).to(directVoyageExchange()).withQueueName();
    }

    /**
     * Configures and provides a DirectExchange bean for crew-related messages.
     *
     * @return A DirectExchange bean for crew-related messages.
     */
    @Bean
    public DirectExchange directCrewExchange() {
        return new DirectExchange(CREW_EXCHANGE);
    }

    /**
     * Configures and provides a Queue bean for hiring crew members.
     *
     * @return A Queue bean for hiring crew members.
     */
    @Bean
    public Queue hireCrewQueue() {
        return new Queue(HIRE_CREW_QUEUE);
    }

    /**
     * Configures and provides a Queue bean for firing crew members.
     *
     * @return A Queue bean for firing crew members.
     */
    @Bean
    public Queue fireCrewQueue() {
        return new Queue(FIRE_CREW_QUEUE);
    }

    /**
     * Configures and provides a Binding bean to bind the hire crew queue to the crew exchange.
     *
     * @return A Binding bean for binding the hire crew queue to the crew exchange.
     */
    @Bean
    public Binding hireCrewBinding() {
        return BindingBuilder.bind(hireCrewQueue()).to(directCrewExchange()).withQueueName();
    }

    /**
     * Configures and provides a Binding bean to bind the fire crew queue to the crew exchange.
     *
     * @return A Binding bean for binding the fire crew queue to the crew exchange.
     */
    @Bean
    public Binding fireCrewBinding() {
        return BindingBuilder.bind(fireCrewQueue()).to(directCrewExchange()).withQueueName();
    }
}