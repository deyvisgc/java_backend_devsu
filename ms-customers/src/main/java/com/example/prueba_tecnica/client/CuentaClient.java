package com.example.prueba_tecnica.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
@FeignClient(name = "ms-movimiento", url = "${ms-movimiento.url}")
public interface CuentaClient {
    @PostMapping(value = "/cuentas")
    ResponseEntity<Void> saveCuenta(@RequestBody CuentaDtoFeign cuentaDto);
}
