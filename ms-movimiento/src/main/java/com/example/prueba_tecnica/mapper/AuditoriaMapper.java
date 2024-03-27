package com.example.prueba_tecnica.mapper;

import com.example.prueba_tecnica.dto.AuditoriaDto;
import com.example.prueba_tecnica.entity.Auditoria;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface AuditoriaMapper {
    @Mappings({
            @Mapping(source = "id", target = "auditoriaId"),
            @Mapping(source = "typeTransacction", target = "tipoTransaccion"),
            @Mapping(source = "date", target = "fecha"),
            @Mapping(source = "user", target = "usuario"),
            @Mapping(source = "description", target = "descripcion"),
            @Mapping(source = "amount", target = "monto"),
            @Mapping(source = "accountId", target = "cuentaId"),
    })
    AuditoriaDto auditoriaToauditoriaDto(Auditoria auditoria);
    @InheritInverseConfiguration
    Auditoria auditoriaDtoToauditoria(AuditoriaDto auditoriaDto);
}
