package com.example.prueba_tecnica.service;

import com.example.prueba_tecnica.dto.CuentaDto;
import com.example.prueba_tecnica.dto.MovimientoDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public interface CuentaService {
    public List<CuentaDto> listAll();
    public CuentaDto getById(Long  id);
    public CuentaDto save(CuentaDto cuentaDto);
    public CuentaDto update(Long id, CuentaDto cuentaDto);
    public void delete(Long  id);
    public void updateSaldoActual(Long cuentaId, BigDecimal nuevoSaldoActual);
}
