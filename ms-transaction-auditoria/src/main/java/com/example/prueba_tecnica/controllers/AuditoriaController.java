package com.example.prueba_tecnica.controllers;

import com.example.prueba_tecnica.dto.AuditoriaDto;
import com.example.prueba_tecnica.exception.AuditException;
import com.example.prueba_tecnica.service.AuditoriaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Slf4j
@RestController
@RequestMapping ("api/Transaction Auditoria")
@Api(tags = "Api Auditoria", description = "Rutas para el servicio de Auditoria")
public class AuditoriaController {
    @Autowired
    private AuditoriaService auditoriaService ;
    @ApiOperation(value = "Listar Auditoria", notes = "Recuperar todas las transacciones realizadas")
    @GetMapping("/")
    public ResponseEntity<List<AuditoriaDto>> getAll(){
        return ResponseEntity.ok(auditoriaService.listAll());
    }
    @ApiOperation(value = "Listar por id auditoria", notes = "Recuperar la trancciòn realizada por identificación.")
    @GetMapping(value = "/{id}")
    public ResponseEntity<AuditoriaDto> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(auditoriaService.getById(id));
    }

    @ApiOperation(value = "Crear Auditoria", notes = "La creacion de la tabla auditoria sera por cada tranzaccion realizada")
    @PostMapping
    public ResponseEntity<AuditoriaDto> create(@Valid @RequestBody AuditoriaDto AuditoriaDto, BindingResult result){
        log.info("INICIO:  CREAR Auditoria");
        if (result.hasErrors()) {
            throw new AuditException(this.formatMessage(result));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(auditoriaService.save(AuditoriaDto));

    }
    private String formatMessage( BindingResult result){
        List<Map<String,String>> errors = result.getFieldErrors().stream()
                .map(err ->{
                    Map<String,String>  error =  new HashMap<>();
                    error.put(err.getField(), err.getDefaultMessage());
                    return error;

                }).collect(Collectors.toList());
        ErrorMessage errorMessage = ErrorMessage.builder()
                .code("01")
                .messages(errors).build();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString="";
        try {
            jsonString = mapper.writeValueAsString(errorMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}
