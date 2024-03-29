package com.example.prueba_tecnica.controllers;

import com.example.prueba_tecnica.exception.CustomException;
import com.example.prueba_tecnica.dto.ClientDto;
import com.example.prueba_tecnica.service.ClientService;
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
import java.util.Objects;
import java.util.stream.Collectors;
@Slf4j
@RestController
@RequestMapping ("api/clientes")
@Api(tags = "Api Cliente", description = "Rutas para el servicio de clientes")
public class ClientController {
    @Autowired
    private ClientService clientService ;
    @ApiOperation(value = "Obtener clientes", notes = "Obtiene una lista de todos los clientes disponibles")
    @GetMapping("/")
    public ResponseEntity<List<ClientDto>> getAll(){
        return ResponseEntity.ok(clientService.listAll());
    }
    @ApiOperation(value = "Obtener Cliente", notes = "Recuperar un cliente a través de su identificación única.")
    @GetMapping(value = "/{id}")
    public ResponseEntity<ClientDto> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(clientService.getById(id));
    }

    @ApiOperation(value = "Crear Cliente", notes = "Si pretende crear una cuenta junto con el cliente, la información de la cuenta (cuentaDto) es necesaria; de lo contrario, es opcional.")
    @PostMapping
    public ResponseEntity<ClientDto> create(@Valid @RequestBody ClientDto clientDto, BindingResult result){
        log.info("INICIO:  CREAR UN CIENTE");
        if (result.hasErrors()) {
            throw new CustomException(this.formatMessage(result));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.save(clientDto));

    }
    @ApiOperation(value = "Actualizar Cliente", notes = "Modificar la información de un cliente existente.")
   @PutMapping(value = "/{id}")
    public ResponseEntity<ClientDto> update( @PathVariable("id") Long id, @Valid @RequestBody ClientDto clientDto, BindingResult result){
        log.info("INICIO:  ACTUALIZAR UN CIENTE");
        if (result.hasErrors()) {
            throw new CustomException(this.formatMessage(result));
        }
        if (Objects.nonNull(clientDto.getCuentaDto())) {
            throw new CustomException("La actualización de cuentas está restringida al microservicio de clientes.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.update(id, clientDto));
    }
    @ApiOperation(value = "Eliminar Cliente", notes = "Eliminar la información de un cliente existente.")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteProduct( @PathVariable("id") Long id){
        clientService.delete(id);
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
