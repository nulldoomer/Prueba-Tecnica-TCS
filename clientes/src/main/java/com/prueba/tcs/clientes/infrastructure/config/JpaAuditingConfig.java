package com.prueba.tcs.clientes.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Enables JPA auditing, kept out of the main class so slice tests like @WebMvcTest do not load it.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {}
