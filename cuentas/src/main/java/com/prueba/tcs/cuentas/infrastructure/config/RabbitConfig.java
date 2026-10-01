package com.prueba.tcs.cuentas.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The exchange is declared here too so cuentas can start before clientes.
 */
@Configuration
public class RabbitConfig {

    public static final String CLIENTES_EXCHANGE = "clientes.exchange";
    public static final String CLIENTE_EVENTS_QUEUE = "cuentas.cliente-events";

    // Receives cliente.created, cliente.updated and cliente.deactivated
    private static final String CLIENTE_EVENTS_ROUTING_KEY = "cliente.*";

    @Bean
    public TopicExchange clientesExchange() {

        return new TopicExchange(CLIENTES_EXCHANGE, true, false);
    }

    @Bean
    public Queue clienteEventsQueue() {

        return QueueBuilder.durable(CLIENTE_EVENTS_QUEUE).build();
    }

    @Bean
    public Binding clienteEventsBinding(Queue clienteEventsQueue, TopicExchange clientesExchange) {

        return BindingBuilder.bind(clienteEventsQueue).to(clientesExchange).with(CLIENTE_EVENTS_ROUTING_KEY);
    }

    /**
     * Reads events as JSON; Spring Boot applies it to the @RabbitListener containers.
     */
    @Bean
    public MessageConverter jsonMessageConverter() {

        return new JacksonJsonMessageConverter();
    }
}
