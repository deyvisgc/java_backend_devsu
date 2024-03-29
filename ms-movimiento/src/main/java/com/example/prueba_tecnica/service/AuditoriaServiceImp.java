package com.example.prueba_tecnica.service;

import com.example.prueba_tecnica.cliente.ClientDtoFeign;
import com.example.prueba_tecnica.dto.AuditoriaDto;
import com.example.prueba_tecnica.entity.Auditoria;
import com.example.prueba_tecnica.entity.Cuenta;
import com.example.prueba_tecnica.exception.AccountException;
import com.example.prueba_tecnica.exception.RecursoNoEncontradoException;
import com.example.prueba_tecnica.mapper.AuditoriaMapper;
import com.example.prueba_tecnica.repository.AuditoriaRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service

@RequiredArgsConstructor /*Esto remplaza al autowired*/
public class AuditoriaServiceImp implements AuditoriaService{
    private final AuditoriaRepository auditoriaRepository;
    private final AuditoriaMapper auditoriaMapper;

    @Override
    public List<AuditoriaDto> listAll() {

        try {
            List<Auditoria> lsAuditoria = auditoriaRepository.findAll();
            return lsAuditoria.stream()
                    .map(auditoriaMapper::auditoriaToauditoriaDto)
                    .collect(Collectors.toList());
        } catch (RecursoNoEncontradoException ex) {
            throw new RecursoNoEncontradoException("No se encontro información de transacciones");
        } catch (Exception ex) {
            log.error("ERROR: {}", ex.getMessage());
            throw ex;
        }
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
