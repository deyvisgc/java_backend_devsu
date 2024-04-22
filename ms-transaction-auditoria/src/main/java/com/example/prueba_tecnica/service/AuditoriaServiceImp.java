package com.example.prueba_tecnica.service;

import com.example.prueba_tecnica.dto.AuditoriaDto;
import com.example.prueba_tecnica.entity.Auditoria;
import com.example.prueba_tecnica.exception.RecursoNoEncontradoException;
import com.example.prueba_tecnica.mapper.AuditoriaMapper;
import com.example.prueba_tecnica.repository.AuditoriaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
    public AuditoriaDto getById(Long id) {
        try {
            Optional<Auditoria> auditoria = auditoriaRepository.findById(id);
            if (auditoria.isPresent()) {
                log.info("FIN:  LISTAR UN CLIENTE POR ID");
                return auditoriaMapper.auditoriaToauditoriaDto(auditoria.get());
            } else {
                throw new RecursoNoEncontradoException("No existe informacion con el identificador: " + id);
            }
        } catch (RecursoNoEncontradoException ex) {
            throw new RecursoNoEncontradoException("No se encontro información de transacciones");
        } catch (Exception ex) {
            log.error("ERROR: {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public AuditoriaDto save(AuditoriaDto auditoriaDto) {
        log.info("INICIO: INSERTAR AUDITORIA");
        // Mapear el DTO a la entidad Cliente
        Auditoria auditoria = auditoriaMapper.auditoriaDtoToauditoria(auditoriaDto);
        // Guardar el cliente en la base de datos
        Auditoria result = auditoriaRepository.save(auditoria);
        log.info("FIN: INSERTAR AUDITORIA");
        return auditoriaMapper.auditoriaToauditoriaDto(result);
    }
    @KafkaListener(topics = "auditoria-topic", groupId = "group-audit")
    public void subscribeAuditoria(String messaje) throws JsonProcessingException {
        try {
            log.info("message: {}", messaje);
            ObjectMapper objectMapper = new ObjectMapper();
            AuditoriaDto auditoria = objectMapper.readValue(messaje, AuditoriaDto.class);
            save(auditoria);
            System.out.println("Tipo de transacción: " + auditoria.getTipoTransaccion());
        } catch (Exception ex) {
            log.error("ERROR: {}", ex.getMessage());
            throw ex;
        }
    }

}
