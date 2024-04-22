package com.example.prueba_tecnica.service;

import com.example.prueba_tecnica.dto.AuditoriaDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface AuditoriaService {
    public List<AuditoriaDto> listAll();
    public AuditoriaDto getById(Long  id);
    public AuditoriaDto save(AuditoriaDto AuditoriaDto);
}
