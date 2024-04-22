package com.example.prueba_tecnica;

import com.example.prueba_tecnica.kafka.Consumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AuditApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuditApplication.class, args);
		/*
		Consumer consumer = new Consumer();
		consumer.subscribe("auditoria-topic");

		 */
	}

}
