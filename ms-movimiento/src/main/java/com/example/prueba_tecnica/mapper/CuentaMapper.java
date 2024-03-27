package com.example.prueba_tecnica.mapper;

import com.example.prueba_tecnica.dto.CuentaDto;
import com.example.prueba_tecnica.entity.Cuenta;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;
@Mapper(componentModel = "spring", uses = MovimientoMapper.class)
@Component
public interface CuentaMapper {
    @Mappings({
            @Mapping(source = "id", target = "cuenta_id"),
            @Mapping(source = "accountNnumber", target = "numeroCuenta"),
            @Mapping(source = "accountType", target = "tipoCuenta"),
            @Mapping(source = "initialBalance", target = "saldoInicial"),
            @Mapping(source = "status", target = "estado"),
            @Mapping(source = "clienteId", target = "clienteId"),
            @Mapping(source = "movement", target = "movimiento"),
            @Mapping(source = "nameCustomer", target = "nombreCliente"),
            @Mapping(source = "balanceActual", target = "saldoActual")
    })
    CuentaDto cuentaTocuentaDto(Cuenta cuenta);
    @InheritInverseConfiguration
    Cuenta cuentaDtoTocuenta(CuentaDto cuentaDto);
}
