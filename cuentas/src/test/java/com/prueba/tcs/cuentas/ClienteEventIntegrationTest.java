package com.prueba.tcs.cuentas;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.prueba.tcs.cuentas.cliente_ref.entity.ClienteRefEntity;
import com.prueba.tcs.cuentas.cliente_ref.event.ClienteEvent;
import com.prueba.tcs.cuentas.cliente_ref.repository.ClienteRefRepository;
import com.prueba.tcs.cuentas.infrastructure.config.RabbitConfig;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class ClienteEventIntegrationTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ClienteRefRepository clienteRefRepository;

    @Test
    void should_save_cliente_ref_when_cliente_created_event_is_published() {

        // ---------------------- Arrange ---------------------------------------------------
        UUID clienteId = UUID.randomUUID();
        ClienteEvent event = new ClienteEvent(
                UUID.randomUUID(), ClienteEvent.Type.CREATED, clienteId, "Jose Lema", true, LocalDateTime.now());

        // ---------------------- Act -------------------------------------------------------
        rabbitTemplate.convertAndSend(RabbitConfig.CLIENTES_EXCHANGE, "cliente.created", event);

        // ---------------------- Assert ----------------------------------------------------
        // The listener consumes the message asynchronously, so wait until the row shows up
        await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            ClienteRefEntity cliente = clienteRefRepository.findById(clienteId).orElseThrow();
            assertEquals("Jose Lema", cliente.getNombre());
            assertTrue(cliente.getEstado());
        });
    }
}
