package com.example.prueba_tecnica.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "ms-movimiento", url = "${ms-movimiento.url}")
public interface CuentaClient {
    @PostMapping(value = "/api/cuentas")
    ResponseEntity<Void> saveCuenta(@RequestBody CuentaDtoFeign cuentaDto);
    @GetMapping(value = "/api/cuentas/clients/{id}")
    ResponseEntity<List<CuentaDtoFeign>> getCuentaById(@PathVariable("id") Long id);
}
