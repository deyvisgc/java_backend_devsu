package com.example.prueba_tecnica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//@EnableEurekaClient
@EnableFeignClients
public class MovimientoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovimientoApplication.class, args);
	}

}
