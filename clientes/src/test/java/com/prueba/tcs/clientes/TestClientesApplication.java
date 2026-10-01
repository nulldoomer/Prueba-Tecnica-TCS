package com.prueba.tcs.clientes;

import org.springframework.boot.SpringApplication;

public class TestClientesApplication {

    public static void main(String[] args) {
        SpringApplication.from(ClientesApplication::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }
}
