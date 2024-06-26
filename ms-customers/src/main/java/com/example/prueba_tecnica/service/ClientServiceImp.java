package com.example.prueba_tecnica.service;

import com.example.prueba_tecnica.dto.AuditoriaDto;
import com.example.prueba_tecnica.exception.CustomException;
import com.example.prueba_tecnica.client.CuentaClient;
import com.example.prueba_tecnica.client.CuentaDtoFeign;
import com.example.prueba_tecnica.dto.ClientDto;
import com.example.prueba_tecnica.entity.Client;
import com.example.prueba_tecnica.exception.RecursoNoEncontradoException;
import com.example.prueba_tecnica.kafka.Producer;
import com.example.prueba_tecnica.mapper.ClienteMapper;
import com.example.prueba_tecnica.repository.ClientRepository;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.concurrent.CompletableFuture;
import org.springframework.util.concurrent.ListenableFuture;
@Slf4j
@Service

public class ClientServiceImp implements ClientService {
    @Value("${topic}")
    String topico;
    /*
    @Autowired
    KafkaTemplate<String, AuditoriaDto> kafkaTemplate;

     */
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClienteMapper clienteMapper;
    @Autowired
    private CuentaClient cuentaClient;
    @Override
    public Page<ClientDto> listAll(Pageable pageable) {

        log.info("INICIO: LISTAR CLIENTES");
        try {
            Page<ClientDto> lsclientes = clientRepository.findAllByStatus(pageable, '1')
                    .map(c -> clienteMapper.clienteToClienteDTO(c));
            log.info("FIN: LISTAR CLIENTES");
             return  lsclientes;
        } catch (RecursoNoEncontradoException ex) {
            throw new RecursoNoEncontradoException("No se encontro información de clientes");
        } catch (Exception ex) {
            log.error("ERROR: {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public ClientDto getById(Long id) {
        log.info("INICIO:  LISTAR UN CLIENTE POR ID");
        try {
            if (id == null){
                throw new RecursoNoEncontradoException("El identificador del cliente no puede ser null: " + id);
            }
            Optional<Client> cuenta = clientRepository.findById(id);
            if (cuenta.isPresent()) {
                log.info("FIN:  LISTAR UN CLIENTE POR ID");
                return clienteMapper.clienteToClienteDTO(cuenta.get());
            } else {
                throw new RecursoNoEncontradoException("No existe informacion con el identificador: " + id);
            }
        } catch (Exception ex) {
            log.error("ERROR: {}", ex.getMessage());
            throw ex;
        }
    }
    @Override
    public List<CuentaDtoFeign> getByAccount(Long id) {
        try {
            if (id == null){
                throw new RecursoNoEncontradoException("El identificador del cliente no puede ser null: " + id);
            }
            Optional<Client> client = clientRepository.findById(id);
            if (client.isPresent()) {
                List<CuentaDtoFeign> lsCuenta = cuentaClient.getCuentaById(id).getBody();
                return lsCuenta;
            } else {
                throw new RecursoNoEncontradoException("No se encontro clientes con el identificador: "  + id);
            }
        } catch (FeignException feignException) {
            log.error("ERROR FeignException: {}", feignException.getMessage());
            throw new CustomException(feignException.getMessage());
        } catch (Exception ex) {
            log.error("ERROR: {}", ex.getMessage());
            throw ex;
        }
    }

    /*
     * CuentaDto: igual a null creara cliente, caso contrario creara cliente y cuenta
    */
    @Override
    public ClientDto save(ClientDto clientDto) {

        Client result = new Client();
        try {
            Client cliente = clienteMapper.clienteDTOtoCliente(clientDto);
            result = clientRepository.save(cliente);
            if (Objects.nonNull(clientDto.getCuentaDto())) {
                CuentaDtoFeign cuentaDtoFeign = new CuentaDtoFeign();
                cuentaDtoFeign.setClienteId(result.getId());
                cuentaDtoFeign.setNumeroCuenta(clientDto.getCuentaDto().getNumeroCuenta());
                cuentaDtoFeign.setTipoCuenta(clientDto.getCuentaDto().getTipoCuenta());
                cuentaDtoFeign.setSaldoInicial(clientDto.getCuentaDto().getSaldoInicial());
                cuentaDtoFeign.setEstado(true);
                cuentaClient.saveCuenta(cuentaDtoFeign);
            }
            log.info("FIN: CREAR UN CIENTE");
            return clienteMapper.clienteToClienteDTO(result);
        } catch (FeignException feignException) {
            log.error("ERROR FeignException: {}", feignException.getMessage());
            throw new CustomException(feignException.getMessage());
        }  catch (Exception ex) {
            log.error("ERROR: {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public ClientDto update(Long id, ClientDto clientDto) {
        try {

            if (id == null){
                throw new RecursoNoEncontradoException("El identificador del cliente no puede ser null: " + id);
            }
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
                throw new RecursoNoEncontradoException("No existe información con el identificador: " + id);
            }
            log.info("FIN: ACTUALIZAR UN CIENTE");
            return clientDtoResul;
        } catch (Exception ex) {
            log.error("ERROR: {}", ex.getMessage());
            throw ex;
        }
    }
    @Override
    public void delete(Long id) {
        log.info("INICIO:  ELIMINAR UN CIENTE");
        try {
            if (id == null){
                throw new RecursoNoEncontradoException("El identificador del cliente no puede ser null: " + id);
            }
            clientRepository.deleteById(id);
            log.info("FIN: ELIMINAR UN CIENTE");
        } catch (Exception ex) {
            log.error("ERROR: {}", ex.getMessage());
            throw ex;
        }
    }
}
