package com.example.prueba_tecnica.controllers;

import com.example.prueba_tecnica.dto.AuditoriaDto;
import com.example.prueba_tecnica.dto.CuentaDto;
import com.example.prueba_tecnica.service.AuditoriaService;
import com.example.prueba_tecnica.service.CuentaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/auditoria")
@Api(tags = "Api Auditoria", description = "Rutas para el servicio de auditoria")
public class AuditoriaController {

    @Autowired
    private AuditoriaService auditoriaService ;
    @ApiOperation(value = "Recuperar cuentas", notes = "Recuperar el listado de todas las transacciones realizadas.")

    @GetMapping("/")
    public ResponseEntity<List<AuditoriaDto>> getAll(){
        return ResponseEntity.ok(auditoriaService.listAll());
    }
}
