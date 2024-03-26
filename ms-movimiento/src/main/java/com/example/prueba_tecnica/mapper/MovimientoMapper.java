package com.example.prueba_tecnica.mapper;

import com.example.prueba_tecnica.dto.MovimientoDto;
import com.example.prueba_tecnica.entity.Cuenta;
import com.example.prueba_tecnica.entity.Movimiento;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Mapper(componentModel = "spring")
@Component
public interface MovimientoMapper {
    @Mappings({
            @Mapping(source = "id", target = "movimiento_id"),
            @Mapping(source = "date", target = "fechaMovimiento"),
            @Mapping(source = "typeMovement", target = "tipoMovimiento"),
            @Mapping(source = "value", target = "valor"),
            @Mapping(source = "balance", target = "saldo"),
            @Mapping(source = "account", target = "cuenta"),
    })
    MovimientoDto movimientoToMovimientoDto(Movimiento movimiento);

    @InheritInverseConfiguration
    Movimiento movimientoDtOtoMovimiento(MovimientoDto movimientoDto);

}
