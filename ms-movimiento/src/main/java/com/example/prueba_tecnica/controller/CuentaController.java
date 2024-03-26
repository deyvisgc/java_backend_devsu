package com.example.prueba_tecnica.controller;

import com.example.prueba_tecnica.dto.CuentaDto;
import com.example.prueba_tecnica.service.CuentaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.jpamodelgen.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
@RestController
@RequestMapping ("/cuentas")
public class CuentaController {
    @Autowired
    private CuentaService cuentaService ;
    @GetMapping("/")
    public ResponseEntity<List<CuentaDto>> getAll(){
        return ResponseEntity.ok(cuentaService.listAll());
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<CuentaDto> getById(@PathVariable("id") Long id) {
        CuentaDto result =  cuentaService.getById(id);
        if (Objects.isNull(result)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CuentaDto cuentaDto, BindingResult result){
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(this.formatMessage(result));
            //throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(cuentaService.save(cuentaDto));
    }
       @PutMapping(value = "/{id}")
    public ResponseEntity<CuentaDto> update(@PathVariable("id") Long id, @RequestBody CuentaDto cuentaDto){
       CuentaDto client =  cuentaService.update(id, cuentaDto);
        if (client == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(client);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id){
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
