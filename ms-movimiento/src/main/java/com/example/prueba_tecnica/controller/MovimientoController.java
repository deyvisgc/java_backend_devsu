package com.example.prueba_tecnica.controller;

import com.example.prueba_tecnica.dto.MovimientoDto;
import com.example.prueba_tecnica.exception.AccountException;
import com.example.prueba_tecnica.service.MovimientoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@RestController
@RequestMapping ("/movimientos")
public class MovimientoController {
    @Autowired
    private MovimientoService movimientoService ;
    @GetMapping("/")
    public ResponseEntity<List<MovimientoDto>> getAll(){
        return ResponseEntity.ok(movimientoService.listAll());
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<MovimientoDto> getClienById(@PathVariable("id") Long id) {
        MovimientoDto movimientoDto =  movimientoService.getById(id);
        if (null==movimientoDto){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(movimientoDto);
    }
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody MovimientoDto movimientoDto, BindingResult result){
        if (result.hasErrors()) {
            throw new AccountException(this.formatMessage(result));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(movimientoService.save(movimientoDto));
    }

   @PutMapping(value = "/{id}")
    public ResponseEntity<MovimientoDto> update(@PathVariable("id") Long id, @RequestBody MovimientoDto clientDto){
        MovimientoDto client =  movimientoService.update(id, clientDto);
        if (client == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(client);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id){
        movimientoService.delete(id);
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
