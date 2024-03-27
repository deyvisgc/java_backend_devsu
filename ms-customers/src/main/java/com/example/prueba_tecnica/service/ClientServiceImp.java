package com.example.prueba_tecnica.service;

import com.example.prueba_tecnica.exception.CustomException;
import com.example.prueba_tecnica.client.CuentaClient;
import com.example.prueba_tecnica.client.CuentaDtoFeign;
import com.example.prueba_tecnica.dto.ClientDto;
import com.example.prueba_tecnica.entity.Client;
import com.example.prueba_tecnica.mapper.ClienteMapper;
import com.example.prueba_tecnica.repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Slf4j
@Service

public class ClientServiceImp implements ClientService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClienteMapper clienteMapper;
    @Autowired
    private CuentaClient cuentaClient;
    @Override
    public List<ClientDto> listAll() {
        List<Client> clientes = clientRepository.findAll();
        return clientes.stream()
                .map(clienteMapper::clienteToClienteDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClientDto getById(Long id) {
        Optional<Client> cuenta = clientRepository.findById(id);
        if (cuenta.isPresent()) {
            return clienteMapper.clienteToClienteDTO(cuenta.get());
        } else {
            throw new CustomException("No existe cliente relacionado con el identificador: " + id);
        }
    }

    /*
     * Existira dos tipos de registros de cliente:
     * El primero se crera un cliente y una cuenta
     * El segundo se creara solo el cliente
    */
    @Override
    public ClientDto save(ClientDto clientDto) {
        Client result = new Client();
        try {
            Client cliente = clienteMapper.clienteDTOtoCliente(clientDto);
            result = clientRepository.save(cliente);
            if (clientDto.getTipo_negocio().equals("1")) {
                // se creara cliente y cuenta
                CuentaDtoFeign cuentaDtoFeign = new CuentaDtoFeign();
                cuentaDtoFeign.setClienteId(result.getId());
                cuentaDtoFeign.setNumeroCuenta(clientDto.getNumeroCuenta());
                cuentaDtoFeign.setTipoCuenta(clientDto.getTipoCuenta());
                cuentaDtoFeign.setSaldoInicial(clientDto.getSaldoInicial());
                cuentaDtoFeign.setEstado(true);
                cuentaClient.saveCuenta(cuentaDtoFeign);
            }
        } catch (EmptyResultDataAccessException e) {
            log.info("Error: {}", e.getMessage());
            throw new CustomException(e.getMessage());
        }
        return clienteMapper.clienteToClienteDTO(result);
    }

    @Override
    public ClientDto update(Long id, ClientDto clientDto) {
        // Verificar si el cliente con el ID dado existe en la base de datos
        Optional<Client> clienteExistenteOptional = clientRepository.findById(id);
        ClientDto clientDtoResul = new ClientDto();
        if (clienteExistenteOptional.isPresent()) {
            Client client = clienteMapper.clienteDTOtoCliente(clientDto);
            client.setId(id);
            // Guardar el cliente actualizado en la base de datos
            clientDtoResul = clienteMapper.clienteToClienteDTO(clientRepository.save(client));
        } else {
            // Manejar el caso en que el cliente no exista en la base de datos
            throw new CustomException("No existe cliente relacionado con el identificador: " + id);
        }
        return clientDtoResul;
    }
    @Override
    public void delete(Long id) {
        try {
            clientRepository.deleteById(id);
            log.info("Cliente eliminado correctamente");
            System.out.println("Cliente eliminado correctamente");
        } catch (EmptyResultDataAccessException e) {
            log.info("Error: {}", e.getMessage());
            throw new CustomException("No existe informaciòn para el id " + id);
        }
    }
}
