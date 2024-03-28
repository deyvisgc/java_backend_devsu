package com.example.prueba_tecnica.service;

import com.example.prueba_tecnica.cliente.ClientDtoFeign;
import com.example.prueba_tecnica.cliente.CustomerClient;
import com.example.prueba_tecnica.dto.CuentaDto;
import com.example.prueba_tecnica.entity.Cuenta;
import com.example.prueba_tecnica.exception.AccountException;
import com.example.prueba_tecnica.exception.RecursoNoEncontradoException;
import com.example.prueba_tecnica.mapper.CuentaMapper;
import com.example.prueba_tecnica.repository.CuentaRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor /*Esto remplaza al autowired*/
public class CuentaServiceImp implements CuentaService {
    private final CuentaRepository cuentaRepository;
    private final CuentaMapper cuentaMapper;
    private final CustomerClient customerClient;
    @Override
    public List<CuentaDto> listAll() {
        try {
            List<Cuenta> lscuenta = cuentaRepository.findAll();
            return lscuenta.stream()
                    .map( cuenta -> {
                        ClientDtoFeign clientDtoFeign = customerClient.findByIdClient(cuenta.getClienteId()).getBody();
                        cuenta.setNameCustomer(clientDtoFeign.getNombre());
                        return cuenta;
                    }).map(cuentaMapper::cuentaTocuentaDto)
                    .collect(Collectors.toList());
        } catch (FeignException feignException) {
            log.error("ERROR FeignException: {}", feignException.getMessage());
            throw new AccountException(feignException.getMessage());
        } catch (RecursoNoEncontradoException ex) {
            throw new RecursoNoEncontradoException("No se encontro informaciòn de cuentas");
        } catch (Exception ex) {
            log.error("ERROR: {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public CuentaDto getById(Long id) {
        try {
            Optional<Cuenta> cuenta = cuentaRepository.findById(id);
            if (cuenta.isPresent()) {
                ClientDtoFeign clientDtoFeign = customerClient.findByIdClient(cuenta.get().getClienteId()).getBody();
                if (Objects.nonNull(clientDtoFeign)) {
                    cuenta.get().setNameCustomer(clientDtoFeign.getNombre());
                }
                return cuentaMapper.cuentaTocuentaDto(cuenta.get());
            }
            throw new RecursoNoEncontradoException("No se encontro cuentas relacionas con el id: "  + id);
        } catch (FeignException feignException) {
            log.error("ERROR FeignException: {}", feignException.getMessage());
            throw new AccountException(feignException.getMessage());
        } catch (Exception ex) {
            log.error("ERROR: {}", ex.getMessage());
            throw ex;
        }

    }
    @Override
    public List<CuentaDto> getByIdClient(Long id) {
        try {
            log.info("INICIO: LISTAR CUENTAS POR ID CLIENTE");
            List<Cuenta> lscuentas = cuentaRepository.findAccountsByClientId(id);
            if (lscuentas.size() == 0) {
                throw new RecursoNoEncontradoException("No se encontro cuentas relacionas con el identificacod del cliente: "  + id);
            }
            List<CuentaDto> list = lscuentas.stream()
                    .map(cuentaMapper::cuentaTocuentaDto)
                    .collect(Collectors.toList());
            log.info("FIN: LISTAR CUENTAS POR ID CLIENTE");
            return list;
        } catch (Exception ex) {
            log.error("ERROR: {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public CuentaDto save(CuentaDto cuentaDto) {
        try {
            Cuenta cuenta = cuentaMapper.cuentaDtoTocuenta(cuentaDto);
            cuenta.setBalanceActual(cuentaDto.getSaldoInicial());
            Cuenta clientResultd = cuentaRepository.save(cuenta);
            return cuentaMapper.cuentaTocuentaDto(clientResultd);
        } catch (Exception ex) {
            log.error("ERROR: {}", ex.getMessage());
            throw ex;
        }
    }
    @Override
    public CuentaDto update(Long id, CuentaDto cuentaDto) {
        try {
            // Verificar si el cliente con el ID dado existe en la base de datos
            Optional<Cuenta> clienteExistenteOptional = cuentaRepository.findById(id);
            CuentaDto result = new CuentaDto();
            if (clienteExistenteOptional.isPresent()) {
                Cuenta cuenta = cuentaMapper.cuentaDtoTocuenta(cuentaDto);
                cuenta.setId(id);
                cuenta.setBalanceActual(cuentaDto.getSaldoInicial());
                result = cuentaMapper.cuentaTocuentaDto(cuentaRepository.save(cuenta));
                return result;
            } else {
                throw new RecursoNoEncontradoException("No se encontro la cuenta relacionada al id: "  + id);
            }
        }  catch (Exception ex) {
            log.error("ERRROR: {}", ex.getMessage());
            throw ex;
        }
    }
    @Override
    public void delete(Long id) {
        try {
            cuentaRepository.deleteById(id);
            log.info("Cliente eliminado correctamente");
        } catch (RecursoNoEncontradoException e) {
            throw new RecursoNoEncontradoException("No se encontro la cuenta relacionada al id: "  + id);
        } catch (Exception ex) {
            log.error("ERRROR: {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void updateSaldoActual(Long cuentaId, BigDecimal nuevoSaldoActual) {
        try {
            cuentaRepository.updateSaldoActual(cuentaId, nuevoSaldoActual);
            log.info("Saldo Actual actualizado correctamente");
        } catch (RecursoNoEncontradoException e) {
            throw new RecursoNoEncontradoException("No se encontro la cuenta relacionada al id: "  + cuentaId);
        } catch (Exception ex) {
            log.error("ERRROR: {}", ex.getMessage());
            throw ex;
        }
    }
}
