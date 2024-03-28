package com.example.prueba_tecnica.controller;

import com.example.prueba_tecnica.exception.CustomException;
import com.example.prueba_tecnica.dto.ClientDto;
import com.example.prueba_tecnica.service.ClientService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@RequestMapping ("api/clientes")
public class ClientController {
    @Autowired
    private ClientService clientService ;
    @GetMapping("/")
    public ResponseEntity<List<ClientDto>> getAll(){
        return ResponseEntity.ok(clientService.listAll());
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<ClientDto> findById(@PathVariable("id") Long id) {
        ClientDto client =  clientService.getById(id);
        if (null==client){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(client);
    }
    @PostMapping
    public ResponseEntity<ClientDto> create(@Valid @RequestBody ClientDto clientDto, BindingResult result){
        if (result.hasErrors()) {
            throw new CustomException(this.formatMessage(result));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.save(clientDto));
    }

   @PutMapping(value = "/{id}")
    public ResponseEntity<ClientDto> update(@PathVariable("id") Long id, @RequestBody ClientDto clientDto){
        ClientDto client =  clientService.update(id, clientDto);
        if (client == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(client);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id){
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
