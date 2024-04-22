package com.example.prueba_tecnica.service;

import com.example.prueba_tecnica.dto.CuentaDto;
import org.hibernate.service.spi.ServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public interface CuentaService {
    Page<CuentaDto> listAll(Pageable pageable)  throws ServiceException;
    public CuentaDto getById(Long  id);
    public List<CuentaDto> getByIdClient(Long  id);
    public CuentaDto save(CuentaDto cuentaDto);
    public CuentaDto update(Long id, CuentaDto cuentaDto);
    public void delete(Long  id);
    public void updateSaldoActual(Long cuentaId, BigDecimal nuevoSaldoActual);
}
