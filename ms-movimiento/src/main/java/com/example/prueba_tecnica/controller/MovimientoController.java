package com.example.prueba_tecnica.controller;

import com.example.prueba_tecnica.dto.MovimientoDto;
import com.example.prueba_tecnica.dto.ReporteDto;
import com.example.prueba_tecnica.exception.AccountException;
import com.example.prueba_tecnica.service.MovimientoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Slf4j
@RestController
@RequestMapping ("/api/movimientos")
public class MovimientoController {

    @Autowired
    private MovimientoService movimientoService ;
    @GetMapping("/")
    public ResponseEntity<List<MovimientoDto>> getAll(){
        return ResponseEntity.ok(movimientoService.listAll());
    }
    @GetMapping(value = "/reportes")
    public ResponseEntity<List<ReporteDto>> getReporte(@RequestParam("fecha") String rangoFecha, @RequestParam("cliente") Long cliente) throws ParseException {
        log.info("INICIO: OBTENER REPORTES MOVIMIENTOS");
        // Divido el String en las dos fechas separadas
        String[] fechasSeparadas = rangoFecha.split("\" a \"");
        // Verifico si se han obtenido dos fechas
        if (fechasSeparadas.length == 2) {
            // Convertir las cadenas de fecha en objetos Date
            String fechaInicio = fechasSeparadas[0].replace("\"", "").trim();
            String fechaFin    = fechasSeparadas[1].replace("\"", "").trim();
            List<ReporteDto> movimientoDto = movimientoService.generarReporte(fechaInicio, fechaFin, cliente);
            return ResponseEntity.ok(movimientoDto);
        } else {
            // Manejar el caso en el que no se reciben dos fechas correctamente
            throw new IllegalArgumentException("El formato del rango de fechas no es válido");
        }
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
        log.info("INICIO:  REGISTRAR UN MOVIMIENTO");
        if (result.hasErrors()) {
            throw new AccountException(this.formatMessage(result));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(movimientoService.save(movimientoDto));
    }

   @PutMapping(value = "/{id}")
    public ResponseEntity<MovimientoDto> update(@PathVariable("id") Long id, @Valid @RequestBody MovimientoDto clientDto, BindingResult result){
       log.info("INICIO:  ACUTUALIZAR UN MOVIMIENTO");
       if (result.hasErrors()) {
           throw new AccountException(this.formatMessage(result));
       }
        return ResponseEntity.ok(movimientoService.update(id, clientDto));
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
