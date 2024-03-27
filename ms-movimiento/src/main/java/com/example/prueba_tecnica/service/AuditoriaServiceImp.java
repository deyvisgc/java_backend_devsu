package com.example.prueba_tecnica.service;

import com.example.prueba_tecnica.dto.AuditoriaDto;
import com.example.prueba_tecnica.entity.Auditoria;
import com.example.prueba_tecnica.mapper.AuditoriaMapper;
import com.example.prueba_tecnica.repository.AuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor /*Esto remplaza al autowired*/
public class AuditoriaServiceImp implements AuditoriaService{
    private final AuditoriaRepository auditoriaRepository;
    private final AuditoriaMapper auditoriaMapper;

    @Override
    public List<AuditoriaDto> listAll() {
        List<Auditoria> lsAuditoria = auditoriaRepository.findAll();
        return lsAuditoria.stream()
                .map(auditoriaMapper::auditoriaToauditoriaDto)
                .collect(Collectors.toList());
    }

    @Override
    public AuditoriaDto save(AuditoriaDto auditoriaDto) {
        // Mapear el DTO a la entidad Cliente
        Auditoria auditoria = auditoriaMapper.auditoriaDtoToauditoria(auditoriaDto);
        // Guardar el cliente en la base de datos
        Auditoria result = auditoriaRepository.save(auditoria);
        return auditoriaMapper.auditoriaToauditoriaDto(result);
    }
}
