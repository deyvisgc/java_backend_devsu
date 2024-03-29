package com.example.prueba_tecnica.cliente;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@FeignClient(name = "ms-customer", url = "${ms-customer.url}")
public interface CustomerClient {
    @GetMapping(value = "/api/clientes/{id}")
    ResponseEntity<ClientDtoFeign> findByIdClient(@PathVariable("id") Long id);
}
