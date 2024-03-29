package com.example.prueba_tecnica.service;

import com.example.prueba_tecnica.dto.MovimientoDto;
import com.example.prueba_tecnica.dto.ReporteDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MovimientoService {
    public List<MovimientoDto> listAll();
    public MovimientoDto getById(Long  id);
    public MovimientoDto save(MovimientoDto movimientoDto);
    public MovimientoDto update(Long id, MovimientoDto movimientoDto);
    public void delete(Long id);
    public List<ReporteDto> generarReporte(String fechaIni, String fechaFin, Long cliente);
}
