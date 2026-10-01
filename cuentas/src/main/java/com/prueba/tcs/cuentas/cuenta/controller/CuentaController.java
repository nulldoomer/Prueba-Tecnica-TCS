package com.prueba.tcs.cuentas.cuenta.controller;

import com.prueba.tcs.cuentas.cuenta.dto.CuentaRequest;
import com.prueba.tcs.cuentas.cuenta.dto.CuentaResponse;
import com.prueba.tcs.cuentas.cuenta.dto.CuentaUpdateRequest;
import com.prueba.tcs.cuentas.cuenta.service.CuentaService;
import com.prueba.tcs.cuentas.infrastructure.response.ResultResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST endpoints for cuentas.
 */
@RestController
@RequestMapping("/api/cuentas")
@RequiredArgsConstructor
public class CuentaController {

    private final CuentaService cuentaService;

    /**
     * Opens a cuenta.
     *
     * @param request
     */
    @PostMapping
    public ResponseEntity<ResultResponse<CuentaResponse, String>> create(@Valid @RequestBody CuentaRequest request) {

        CuentaResponse cuenta = cuentaService.create(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(cuenta.cuentaId())
                .toUri();

        return ResponseEntity.created(location).body(ResultResponse.created(cuenta));
    }

    /**
     * Paginated list.
     *
     * @param pageable page, size and sort
     */
    @GetMapping
    public ResponseEntity<ResultResponse<PagedModel<CuentaResponse>, String>> findAll(
            @PageableDefault(sort = "numeroCuenta") Pageable pageable) {

        return ResponseEntity.ok(ResultResponse.success(new PagedModel<>(cuentaService.findAll(pageable))));
    }

    /**
     * Finds a cuenta by id.
     *
     * @param id
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResultResponse<CuentaResponse, String>> findById(@PathVariable UUID id) {

        return ResponseEntity.ok(ResultResponse.success(cuentaService.findById(id)));
    }

    /**
     * Finds a cuenta by numero de cuenta.
     *
     * @param numeroCuenta
     */
    @GetMapping("/numero/{numeroCuenta}")
    public ResponseEntity<ResultResponse<CuentaResponse, String>> findByNumeroCuenta(
            @PathVariable String numeroCuenta) {

        return ResponseEntity.ok(ResultResponse.success(cuentaService.findByNumeroCuenta(numeroCuenta)));
    }

    /**
     * Partially updates a cuenta.
     *
     * @param id
     * @param request
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ResultResponse<CuentaResponse, String>> update(
            @PathVariable UUID id, @Valid @RequestBody CuentaUpdateRequest request) {

        return ResponseEntity.ok(
                ResultResponse.success(cuentaService.update(id, request), "Cuenta actualizada", "CUENTA_UPDATED"));
    }

    /**
     * Logical delete.
     *
     * @param id
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ResultResponse<Void, String>> delete(@PathVariable UUID id) {

        cuentaService.delete(id);
        return ResponseEntity.ok(ResultResponse.success(null, "Cuenta desactivada", "CUENTA_DEACTIVATED"));
    }
}
