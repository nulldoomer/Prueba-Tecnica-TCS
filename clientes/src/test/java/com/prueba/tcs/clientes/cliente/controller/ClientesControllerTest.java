package com.prueba.tcs.clientes.cliente.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.prueba.tcs.clientes.cliente.dto.ClienteRequest;
import com.prueba.tcs.clientes.cliente.dto.ClienteResponse;
import com.prueba.tcs.clientes.cliente.service.ClienteService;
import com.prueba.tcs.clientes.infrastructure.exception.ResourceNotFoundException;
import com.prueba.tcs.clientes.persona.Genero;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ClienteController.class)
class ClientesControllerTest {

    // ------------ FIXTURES -----------------------------------

    private static final UUID ID = UUID.randomUUID();

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClienteService clienteService;

    private ClienteResponse cliente;

    @BeforeEach
    void setUp() {

        cliente = new ClienteResponse(
                ID,
                "Jose Lema",
                Genero.MASCULINO,
                30,
                "1234567890",
                "Otavalo sn y principal",
                "098254785",
                true,
                null,
                null);
    }

    @Test
    void should_return201_when_cliente_created() throws Exception {

        // -------------------- Arrange -------------------------
        given(clienteService.create(any(ClienteRequest.class))).willReturn(cliente);

        // -------------------- Act -------------------------
        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "nombre": "Jose Lema",
                          "genero": "MASCULINO",
                          "edad": 30,
                          "identificacion": "1234567890",
                          "direccion": "Otavalo sn y principal",
                          "telefono": "098254785",
                          "contrasena": "1234"
                        }
                        """))

                // -------------------- Assert -------------------------
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/clientes/" + ID))
                .andExpect(jsonPath("$.result.clienteId").value(ID.toString()))
                .andExpect(jsonPath("$.messageCode").value("RESOURCE_CREATED"));
    }

    @Test
    void should_return400_when_request_is_invalid() throws Exception {

        // -------------------- Act -------------------------
        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                // -------------------- Assert -------------------------
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messageCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.result.metadata.nombre").value("El nombre es obligatorio"));

        verifyNoInteractions(clienteService);
    }

    @Test
    void should_return404_when_cliente_not_found() throws Exception {

        // -------------------- Arrange -------------------------
        given(clienteService.findById(ID)).willThrow(new ResourceNotFoundException("Cliente no encontrado"));

        // -------------------- Act -------------------------
        mockMvc.perform(get("/api/clientes/{id}", ID))
                // -------------------- Assert -------------------------
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.messageCode").value("RESOURCE_NOT_FOUND"));
    }

    @Test
    void should_filter_by_estado_when_param_present() throws Exception {

        // -------------------- Arrange -------------------------
        given(clienteService.findAll(eq(true), any(Pageable.class))).willReturn(new PageImpl<>(List.of(cliente)));

        // -------------------- Act -------------------------
        mockMvc.perform(get("/api/clientes").param("estado", "true"))
                // -------------------- Assert -------------------------
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.content[0].clienteId").value(ID.toString()));
    }

    @Test
    void should_deactivate_when_cliente_deleted() throws Exception {

        // -------------------- Act -------------------------
        mockMvc.perform(delete("/api/clientes/{id}", ID))
                // -------------------- Assert -------------------------
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messageCode").value("CLIENTE_DEACTIVATED"));

        verify(clienteService).deactivateCliente(ID);
    }
}
