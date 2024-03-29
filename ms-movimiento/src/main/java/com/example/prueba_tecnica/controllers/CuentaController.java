package com.example.prueba_tecnica.controllers;

import com.example.prueba_tecnica.dto.CuentaDto;
import com.example.prueba_tecnica.exception.AccountException;
import com.example.prueba_tecnica.service.CuentaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping ("/api/cuentas")
@Api(tags = "Api Cuenta", description = "Rutas para el servicio de cuentas")
public class CuentaController {
    @Autowired
    private CuentaService cuentaService ;
    @ApiOperation(value = "Recuperar cuentas", notes = "Obtiene una lista de todas los cuentas disponibles")

    @GetMapping("/")
    public ResponseEntity<List<CuentaDto>> getAll(){
        return ResponseEntity.ok(cuentaService.listAll());
    }
    @ApiOperation(value = "Recuperar una cuenta.", notes = "Recuperar una cuenta a través de su identificación única.")
    @GetMapping(value = "/{id}")
    public ResponseEntity<CuentaDto> getById(@PathVariable("id") Long id) {
        CuentaDto result =  cuentaService.getById(id);
        return ResponseEntity.ok(result);
    }
    @ApiOperation(value = "Recuperar cuentas por ID de cliente.", notes = "Recuperar cuentas por ID de cliente: obtener información de cuentas asociadas a un cliente.")

    @GetMapping(value = "/clients/{id}")
    public ResponseEntity<List<CuentaDto>> getByIdClient(@PathVariable("id") Long id) {
        return ResponseEntity.ok(cuentaService.getByIdClient(id));
    }
    @ApiOperation(value = "Crear Cuenta", notes = "Registrar nueva cuenta con detalles como número, saldo inicial y tipo.")

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CuentaDto cuentaDto, BindingResult result){
        if (result.hasErrors()) {
            throw new AccountException(this.formatMessage(result));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(cuentaService.save(cuentaDto));
    }
    @ApiOperation(value = "Actualizar Cuenta", notes = "Modificar la información de una cuenta existente.")

    @PutMapping(value = "/{id}")
    public ResponseEntity<CuentaDto> update(@PathVariable("id") Long id, @Valid @RequestBody CuentaDto cuentaDto, BindingResult result){
        log.info("Result: {}", result);
        if (result.hasErrors()) {
            throw new AccountException(this.formatMessage(result));
        }
        return ResponseEntity.ok(cuentaService.update(id, cuentaDto));
    }
    @ApiOperation(value = "Eliminar Cuenta", notes = "Eliminar la información de una cuenta existente.")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id){
        cuentaService.delete(id);
        return ResponseEntity.ok("Eliminado Correctamente");
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
