package com.prueba.tcs.clientes.infrastructure.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ topology owned by clientes: the exchange where cliente events are published.
 */
@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "clientes.exchange";

    @Bean
    public TopicExchange clientesExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    /**
     * Sends events as JSON instead of Java serialization; Spring Boot applies it to the RabbitTemplate.
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
