package com.example.prueba_tecnica.controllers;

import com.example.prueba_tecnica.dto.MovimientoDto;
import com.example.prueba_tecnica.dto.ReporteDto;
import com.example.prueba_tecnica.exception.AccountException;
import com.example.prueba_tecnica.service.MovimientoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Slf4j
@RestController
@RequestMapping ("/api/movimientos")
@Api(tags = "Api Movimiento", description = "Rutas para el servicio de movimientos")
@CrossOrigin("*")
public class MovimientoController {
    @Autowired
    private MovimientoService movimientoService ;
    @ApiOperation(value = "Listar Movimientos", notes = "Obtiene una lista de los movimientos disponibles")
    @GetMapping("/")
    public ResponseEntity<List<MovimientoDto>> getAll(){
        return ResponseEntity.ok(movimientoService.listAll());
    }
    @ApiOperation(value = "Reporte de movimiento.", notes = "Reporte de movimientos por rango de fechas y cliente.")
    @GetMapping(value = "/reportes")
    public ResponseEntity<List<ReporteDto>> getReporte(@ApiParam(value = "Descripción de la fecha", example = " \"2024-03-27\" a \"2024-03-27\"", required = true) @RequestParam("fecha") String rangoFecha,
                                                       @ApiParam(value = "ID del cliente", example = "123456789", required = true) @RequestParam("cliente") Long cliente) throws ParseException {
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
    public ResponseEntity<MovimientoDto> getClienById( @PathVariable("id") Long id) {
        MovimientoDto movimientoDto =  movimientoService.getById(id);
        if (null==movimientoDto){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(movimientoDto);
    }
    @ApiOperation(value = "Registrar Movimiento", notes = "Para registrar un movimiento, basta con enviar el ID de la cuenta junto con la información adicional necesaria.")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody MovimientoDto movimientoDto, BindingResult result){
        log.info("INICIO:  REGISTRAR UN MOVIMIENTO");
        if (result.hasErrors()) {
            throw new AccountException(this.formatMessage(result));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(movimientoService.save(movimientoDto));
    }
    @ApiOperation(value = "Actualizar Movimiento", notes = "Para actualizar un movimiento, basta con enviar el ID de la cuenta junto con la información adicional necesaria.")
   @PutMapping(value = "/{id}")
    public ResponseEntity<MovimientoDto> update(@PathVariable("id") Long id, @Valid @RequestBody MovimientoDto clientDto, BindingResult result){
       log.info("INICIO:  ACUTUALIZAR UN MOVIMIENTO");
       if (result.hasErrors()) {
           throw new AccountException(this.formatMessage(result));
       }
        return ResponseEntity.ok(movimientoService.update(id, clientDto));
    }
    @ApiOperation(value = "Eliminar Movimiento", notes = "Eliminar la información de un movimiento existente.")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id){
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
