package com.prueba.tcs.cuentas;

import org.springframework.boot.SpringApplication;

public class TestCuentasApplication {

	public static void main(String[] args) {
		SpringApplication.from(CuentasApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
