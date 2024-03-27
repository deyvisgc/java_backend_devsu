package com.example.prueba_tecnica.service;

import com.example.prueba_tecnica.cliente.ClientDtoFeign;
import com.example.prueba_tecnica.cliente.CustomerClient;
import com.example.prueba_tecnica.dto.CuentaDto;
import com.example.prueba_tecnica.entity.Cuenta;
import com.example.prueba_tecnica.mapper.CuentaMapper;
import com.example.prueba_tecnica.repository.CuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor /*Esto remplaza al autowired*/
public class CuentaServiceImp implements CuentaService {
    private final CuentaRepository cuentaRepository;
    private final CuentaMapper cuentaMapper;
    private final CustomerClient customerClient;
    @Override
    public List<CuentaDto> listAll() {
        List<Cuenta> lscuenta = cuentaRepository.findAll();
        return lscuenta.stream()
                .map( cuenta -> {
                    ClientDtoFeign clientDtoFeign = customerClient.findByIdClient(cuenta.getClienteId()).getBody();
                    cuenta.setNameCustomer(clientDtoFeign.getNombre());
                    return cuenta;
                }).map(cuentaMapper::cuentaTocuentaDto)
                .collect(Collectors.toList());
    }

    @Override
    public CuentaDto getById(Long id) {
        Optional<Cuenta> cuenta = cuentaRepository.findById(id);
        if (cuenta.isPresent()) {
            ClientDtoFeign clientDtoFeign = customerClient.findByIdClient(cuenta.get().getClienteId()).getBody();
            if (Objects.nonNull(clientDtoFeign)) {
                cuenta.get().setNameCustomer(clientDtoFeign.getNombre());
            }
            return cuentaMapper.cuentaTocuentaDto(cuenta.get());
        }
        return null;
    }

    @Override
    public CuentaDto save(CuentaDto cuentaDto) {
        Cuenta cuenta = cuentaMapper.cuentaDtoTocuenta(cuentaDto);
        cuenta.setBalanceActual(cuentaDto.getSaldoInicial());
        Cuenta clientResultd = cuentaRepository.save(cuenta);
        return cuentaMapper.cuentaTocuentaDto(clientResultd);
    }

    @Override
    public CuentaDto update(Long id, CuentaDto clientDto) {
        // Verificar si el cliente con el ID dado existe en la base de datos
        Optional<Cuenta> clienteExistenteOptional = cuentaRepository.findById(id);
        CuentaDto result = new CuentaDto();
        if (clienteExistenteOptional.isPresent()) {
            Cuenta client = cuentaMapper.cuentaDtoTocuenta(clientDto);
            client.setId(id);
            // Guardar el cliente actualizado en la base de datos
            result = cuentaMapper.cuentaTocuentaDto(cuentaRepository.save(client));
        } else {
            // Manejar el caso en que el cliente no exista en la base de datos
            System.err.println("Erro no existe informacion");
        }
        return result;

    }
    @Override
    public void delete(Long id) {
        try {
            cuentaRepository.deleteById(id);
            System.out.println("Cliente eliminado correctamente");
        } catch (EmptyResultDataAccessException e) {
            System.out.println("No se encontró ningún cliente con el ID proporcionado");
        }
    }

    @Override
    public void updateSaldoActual(Long cuentaId, BigDecimal nuevoSaldoActual) {
        try {
            cuentaRepository.updateSaldoActual(cuentaId, nuevoSaldoActual);
        } catch (EmptyResultDataAccessException e) {
            System.out.println("No se encontró ningúna cuenta con el ID proporcionado");
        }
    }
}
