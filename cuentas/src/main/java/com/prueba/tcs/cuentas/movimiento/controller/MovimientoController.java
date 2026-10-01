package com.prueba.tcs.cuentas.movimiento.controller;

import com.prueba.tcs.cuentas.infrastructure.response.ResultResponse;
import com.prueba.tcs.cuentas.movimiento.dto.MovimientoRequest;
import com.prueba.tcs.cuentas.movimiento.dto.MovimientoResponse;
import com.prueba.tcs.cuentas.movimiento.service.MovimientoService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST endpoints for movimientos. There is no update or delete: movimientos are immutable
 * and corrections are registered as a new movimiento.
 */
@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;

    /**
     * Registers a movimiento.
     *
     * @param request
     */
    @PostMapping
    public ResponseEntity<ResultResponse<MovimientoResponse, String>> create(
            @Valid @RequestBody MovimientoRequest request) {

        MovimientoResponse movimiento = movimientoService.create(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(movimiento.movimientoId())
                .toUri();

        return ResponseEntity.created(location).body(ResultResponse.created(movimiento));
    }

    /**
     * Paginated list, newest first.
     *
     * @param pageable page, size and sort
     */
    @GetMapping
    public ResponseEntity<ResultResponse<PagedModel<MovimientoResponse>, String>> findAll(
            @PageableDefault(sort = "fecha", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok(ResultResponse.success(new PagedModel<>(movimientoService.findAll(pageable))));
    }

    /**
     * Finds a movimiento by id.
     *
     * @param id
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResultResponse<MovimientoResponse, String>> findById(@PathVariable UUID id) {

        return ResponseEntity.ok(ResultResponse.success(movimientoService.findById(id)));
    }
}
