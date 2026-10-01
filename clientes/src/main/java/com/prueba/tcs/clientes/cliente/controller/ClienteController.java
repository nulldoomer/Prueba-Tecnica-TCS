package com.prueba.tcs.clientes.cliente.controller;

import com.prueba.tcs.clientes.cliente.dto.ClienteRequest;
import com.prueba.tcs.clientes.cliente.dto.ClienteResponse;
import com.prueba.tcs.clientes.cliente.dto.ClienteUpdateRequest;
import com.prueba.tcs.clientes.cliente.service.ClienteService;
import com.prueba.tcs.clientes.infrastructure.response.ResultResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST endpoints for clientes.
 */
@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    /**
     * Creates a cliente.
     *
     * @param request data of the cliente to create
     * @return 201 with the created cliente and its URI in the Location header
     */
    @PostMapping
    public ResponseEntity<ResultResponse<ClienteResponse, String>> create(@Valid @RequestBody ClienteRequest request) {

        ClienteResponse cliente = clienteService.create(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(cliente.clienteId())
                .toUri();

        return ResponseEntity.created(location).body(ResultResponse.created(cliente));
    }

    /**
     * Paginated list.
     *
     * @param pageable page, size and sort query params.
     * @return 200 with the requested page of clientes
     */
    @GetMapping
    public ResponseEntity<ResultResponse<PagedModel<ClienteResponse>, String>> findAll(
            @PageableDefault(sort = "nombre") Pageable pageable) {

        return ResponseEntity.ok(ResultResponse.success(new PagedModel<>(clienteService.findAll(pageable))));
    }

    /**
     * Finds a cliente by id.
     *
     * @param id cliente id
     * @return 200 with the cliente, or 404 if it does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResultResponse<ClienteResponse, String>> findById(@PathVariable UUID id) {

        return ResponseEntity.ok(ResultResponse.success(clienteService.findById(id)));
    }

    /**
     * Finds a cliente by identificacion.
     *
     * @param identificacion cliente identificacion
     * @return 200 with the cliente, or 404 if it does not exist
     */
    @GetMapping("/identificacion/{identificacion}")
    public ResponseEntity<ResultResponse<ClienteResponse, String>> findByIdentificacion(
            @PathVariable String identificacion) {

        return ResponseEntity.ok(ResultResponse.success(clienteService.findByIdentificacion(identificacion)));
    }

    /**
     * Fully replaces a cliente's data.
     *
     * @param id cliente id
     * @param request new data of the cliente
     * @return 200 with the updated cliente
     */
    @PutMapping("/{id}")
    public ResponseEntity<ResultResponse<ClienteResponse, String>> replace(
            @PathVariable UUID id, @Valid @RequestBody ClienteRequest request) {

        return ResponseEntity.ok(
                ResultResponse.success(clienteService.replace(id, request), "Cliente actualizado", "CLIENTE_UPDATED"));
    }

    /**
     * Partially updates a cliente; omitted fields are left unchanged.
     *
     * @param id cliente id
     * @param request fields to update
     * @return 200 with the updated cliente
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ResultResponse<ClienteResponse, String>> update(
            @PathVariable UUID id, @Valid @RequestBody ClienteUpdateRequest request) {
        return ResponseEntity.ok(
                ResultResponse.success(clienteService.update(id, request), "Cliente actualizado", "CLIENTE_UPDATED"));
    }

    /**
     * Logical delete: the cliente is deactivated, not removed.
     *
     * @param id cliente id
     * @return 200 with a confirmation message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ResultResponse<Void, String>> delete(@PathVariable UUID id) {
        clienteService.delete(id);
        return ResponseEntity.ok(ResultResponse.success(null, "Cliente desactivado", "CLIENTE_DEACTIVATED"));
    }
}
