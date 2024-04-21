package com.marine.voyage_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String VOYAGE_QUEUE = "voyageQueue";
    public static final String VESSEL_QUEUE = "vesselQueue";
    public static final String VOYAGE_EXCHANGE = "voyageExchange";

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
    public DirectExchange directVoyageExchange(){
        return new DirectExchange(VOYAGE_EXCHANGE);
    }

    @Bean
    public Queue voyageQueue(){
        return new Queue(VOYAGE_QUEUE);
    }
    @Bean
    public Queue vesselQueue(){return new Queue(VESSEL_QUEUE);}

    @Bean
    public Binding voyageBindind(){
        return BindingBuilder.bind(voyageQueue()).to(directVoyageExchange()).withQueueName();
    }
    @Bean
    public Binding vesselBinding(){
        return BindingBuilder.bind(vesselQueue()).to(directVoyageExchange()).withQueueName();
    }
}