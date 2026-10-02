package com.prueba.tcs.clientes.cliente.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.prueba.tcs.clientes.cliente.dto.ClienteRequest;
import com.prueba.tcs.clientes.cliente.dto.ClienteResponse;
import com.prueba.tcs.clientes.cliente.dto.ClienteUpdateRequest;
import com.prueba.tcs.clientes.cliente.entity.ClienteEntity;
import com.prueba.tcs.clientes.cliente.event.ClienteEvent;
import com.prueba.tcs.clientes.cliente.mapper.ClienteMapper;
import com.prueba.tcs.clientes.cliente.repository.ClienteRepository;
import com.prueba.tcs.clientes.infrastructure.exception.DuplicateResourceException;
import com.prueba.tcs.clientes.infrastructure.exception.ResourceNotFoundException;
import com.prueba.tcs.clientes.persona.Genero;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    // --------------------------- FIXTURES ---------------------------------

    private static final UUID ID = UUID.randomUUID();

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Spy
    private ClienteMapper clienteMapper = Mappers.getMapper(ClienteMapper.class);

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private ClienteEntity cliente;

    @BeforeEach
    void setUp() {

        cliente = ClienteEntity.builder()
                .id(ID)
                .nombre("Jose Lema")
                .genero(Genero.MASCULINO)
                .edad(30)
                .identificacion("1234567890")
                .direccion("Otavalo sn y principal")
                .telefono("098254785")
                .contrasena("hashed")
                .build();
    }

    @Test
    void should_encode_password_and_publish_created_when_cliente_created() {

        // ---------------------- Arrange ---------------------------------------------------
        given(passwordEncoder.encode("1234")).willReturn("hashed");
        given(clienteRepository.save(any(ClienteEntity.class))).willAnswer(inv -> inv.getArgument(0));

        // ---------------------- Act -------------------------------------------------------
        ClienteResponse response = clienteService.create(request("1234567890"));

        // ---------------------- Assert ----------------------------------------------------
        ArgumentCaptor<ClienteEntity> saved = ArgumentCaptor.forClass(ClienteEntity.class);
        verify(clienteRepository).save(saved.capture());
        assertThat(saved.getValue().getContrasena()).isEqualTo("hashed");
        assertThat(response.estado()).isTrue();
        assertThat(publishedEventType()).isEqualTo(ClienteEvent.Type.CREATED);
    }

    @Test
    void should_throw_duplicate_when_identificacion_exists() {

        // ---------------------- Arrange ---------------------------------------------------
        given(clienteRepository.existsByIdentificacion("1234567890")).willReturn(true);

        // ---------------------- Act & Assert ----------------------------------------------
        assertThatThrownBy(() -> clienteService.create(request("1234567890")))
                .isInstanceOf(DuplicateResourceException.class);

        verify(clienteRepository, never()).save(any());
        verifyNoInteractions(eventPublisher);
    }

    @Test
    void should_throw_not_found_when_cliente_does_not_exist() {

        // ---------------------- Arrange ---------------------------------------------------
        given(clienteRepository.findById(ID)).willReturn(Optional.empty());

        // ---------------------- Act & Assert ----------------------------------------------
        assertThatThrownBy(() -> clienteService.findById(ID)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void should_not_validate_identificacion_when_replace_keeps_it() {

        // ---------------------- Arrange ---------------------------------------------------
        given(clienteRepository.findById(ID)).willReturn(Optional.of(cliente));
        given(clienteRepository.saveAndFlush(cliente)).willReturn(cliente);

        // ---------------------- Act -------------------------------------------------------
        clienteService.replace(ID, request("1234567890"));

        // ---------------------- Assert ----------------------------------------------------
        verify(clienteRepository, never()).existsByIdentificacion(any());
    }

    @Test
    void should_throw_duplicate_when_replace_uses_taken_identificacion() {

        // ---------------------- Arrange ---------------------------------------------------
        given(clienteRepository.findById(ID)).willReturn(Optional.of(cliente));
        given(clienteRepository.existsByIdentificacion("0987654321")).willReturn(true);

        // ---------------------- Act & Assert ----------------------------------------------
        assertThatThrownBy(() -> clienteService.replace(ID, request("0987654321")))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    void should_keep_password_when_update_omits_it() {

        // ---------------------- Arrange ---------------------------------------------------
        given(clienteRepository.findById(ID)).willReturn(Optional.of(cliente));
        given(clienteRepository.saveAndFlush(cliente)).willReturn(cliente);

        // ---------------------- Act -------------------------------------------------------
        clienteService.update(ID, new ClienteUpdateRequest("Nuevo Nombre", null, null, null, null, null, null));

        // ---------------------- Assert ----------------------------------------------------
        assertThat(cliente.getNombre()).isEqualTo("Nuevo Nombre");
        assertThat(cliente.getContrasena()).isEqualTo("hashed");
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void should_filter_by_estado_when_present() {

        // ---------------------- Arrange ---------------------------------------------------
        given(clienteRepository.findByEstado(false, Pageable.unpaged())).willReturn(Page.empty());

        // ---------------------- Act -------------------------------------------------------
        Page<ClienteResponse> result = clienteService.findAll(false, Pageable.unpaged());

        // ---------------------- Assert ----------------------------------------------------
        assertThat(result).isEmpty();
    }

    @Test
    void should_set_estado_false_and_publish_deactivated_when_cliente_deactivated() {

        // ---------------------- Arrange ---------------------------------------------------
        given(clienteRepository.findById(ID)).willReturn(Optional.of(cliente));
        given(clienteRepository.saveAndFlush(cliente)).willReturn(cliente);

        // ---------------------- Act -------------------------------------------------------
        ClienteResponse response = clienteService.deactivateCliente(ID);

        // ---------------------- Assert ----------------------------------------------------
        assertThat(response.estado()).isFalse();
        assertThat(publishedEventType()).isEqualTo(ClienteEvent.Type.DEACTIVATED);
    }

    private ClienteEvent.Type publishedEventType() {

        ArgumentCaptor<ClienteEvent> event = ArgumentCaptor.forClass(ClienteEvent.class);
        verify(eventPublisher).publishEvent(event.capture());
        return event.getValue().eventType();
    }

    private static ClienteRequest request(String identificacion) {

        return new ClienteRequest(
                "Jose Lema", Genero.MASCULINO, 30, identificacion, "Otavalo sn y principal", "098254785", "1234", null);
    }
}
