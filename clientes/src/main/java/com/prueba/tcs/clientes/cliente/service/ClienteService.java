package com.prueba.tcs.clientes.cliente.service;

import com.prueba.tcs.clientes.cliente.dto.ClienteRequest;
import com.prueba.tcs.clientes.cliente.dto.ClienteResponse;
import com.prueba.tcs.clientes.cliente.dto.ClienteUpdateRequest;
import com.prueba.tcs.clientes.infrastructure.exception.DuplicateResourceException;
import com.prueba.tcs.clientes.infrastructure.exception.ResourceNotFoundException;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Use cases for managing clientes.
 */
public interface ClienteService {

    /**
     * Creates a new cliente.
     *
     * @param request data of the cliente to create
     * @return the created cliente
     * @throws DuplicateResourceException if the identificacion is already registered
     */
    ClienteResponse create(ClienteRequest request);

    /**
     * Finds a cliente by its id.
     *
     * @param id cliente id
     * @return the cliente found
     * @throws ResourceNotFoundException if the cliente does not exist
     */
    ClienteResponse findById(UUID id);

    /**
     * Finds a cliente by its identificacion.
     *
     * @param identificacion cliente identificacion
     * @return the cliente found
     * @throws ResourceNotFoundException if no cliente has that identificacion
     */
    ClienteResponse findByIdentificacion(String identificacion);

    /**
     * Returns a page of clientes.
     *
     * @param pageable page number, size and sorting
     * @return the requested page of clientes
     */
    Page<ClienteResponse> findAll(Pageable pageable);

    /**
     * Fully replaces a cliente's data (PUT semantics).
     *
     * @param id      cliente id
     * @param request new data of the cliente
     * @return the updated cliente
     * @throws ResourceNotFoundException  if the cliente does not exist
     * @throws DuplicateResourceException if the new identificacion belongs to another cliente
     */
    ClienteResponse replace(UUID id, ClienteRequest request);

    /**
     * Partially updates a cliente: null fields are left unchanged.
     *
     * @param id      cliente id
     * @param request fields to update
     * @return the updated cliente
     * @throws ResourceNotFoundException if the cliente does not exist
     */
    ClienteResponse update(UUID id, ClienteUpdateRequest request);

    /**
     * Logically deletes a cliente by setting its estado to false.
     *
     * @param id cliente id
     * @throws ResourceNotFoundException if the cliente does not exist
     */
    void delete(UUID id);
}
