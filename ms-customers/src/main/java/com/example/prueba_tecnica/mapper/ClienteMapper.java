package com.example.prueba_tecnica.mapper;

import com.example.prueba_tecnica.dto.ClientDto;
import com.example.prueba_tecnica.entity.Client;
import lombok.Builder;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface ClienteMapper {
    @Mappings({
            @Mapping(source = "name", target = "nombre"),
            @Mapping(source = "gender", target = "genero"),
            @Mapping(source = "age", target = "edad"),
            @Mapping(source = "identification", target = "identificacion"),
            @Mapping(source = "address", target = "direccion"),
            @Mapping(source = "phone", target = "telefono"),
            @Mapping(source = "status", target = "estado"),
            @Mapping(source = "password", target = "password"),
            @Mapping(source = "id", target = "id")
    })
    ClientDto clienteToClienteDTO(Client cliente);

    // Necesitarás implementar este método si deseas convertir un DTO de Cliente a un objeto Cliente
    @InheritInverseConfiguration
    Client clienteDTOtoCliente(ClientDto clienteDTO);
}
