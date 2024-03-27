package com.example.prueba_tecnica.service;

import com.example.prueba_tecnica.Execption.CustomException;
import com.example.prueba_tecnica.dto.AuditoriaDto;
import com.example.prueba_tecnica.dto.CuentaDto;
import com.example.prueba_tecnica.dto.MovimientoDto;
import com.example.prueba_tecnica.entity.Cuenta;
import com.example.prueba_tecnica.entity.Movimiento;
import com.example.prueba_tecnica.mapper.MovimientoMapper;
import com.example.prueba_tecnica.repository.MovimientoRepository;
import com.example.prueba_tecnica.util.TipoMovimiento;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor /*Esto remplaza al autowired*/
public class MovimientoServiceImp implements MovimientoService {
    private final MovimientoRepository movimientoRepository;
    private final MovimientoMapper movimientoMapper;
    private final CuentaService cuentaService;
    private final AuditoriaService auditoriaService;
    @Override
    public List<MovimientoDto> listAll() {
        List<Movimiento> clientes = movimientoRepository.findAll();
        return clientes.stream()
                .map(movimientoMapper::movimientoToMovimientoDto)
                .collect(Collectors.toList());
    }

    @Override
    public MovimientoDto getById(Long id) {
        return movimientoMapper.movimientoToMovimientoDto(movimientoRepository.getById(id));
    }

    @Override
    public MovimientoDto save(MovimientoDto movimientoDto) {

        try {
            BigDecimal newSaldo = BigDecimal.ZERO;
            CuentaDto cuenta = cuentaService.getById(movimientoDto.getCuenta().getId());
            if (movimientoDto.getTipoMovimiento().equalsIgnoreCase(TipoMovimiento.DEPOSITO.getDescripcion())) {
                newSaldo = cuenta.getSaldoActual().add(movimientoDto.getValor());
            } else {
                if (cuenta.getSaldoActual().compareTo(BigDecimal.ZERO) == 0) {
                    throw new CustomException("Saldo no disponible");
                }
                newSaldo = cuenta.getSaldoActual().subtract(movimientoDto.getValor().abs()); //devuelvo el valor absoluto
            }
            System.err.println("New Saldo: " + newSaldo);
            movimientoDto.setSaldo(newSaldo);
            // Mapear el DTO a la entidad Cliente
            Movimiento movimiento = movimientoMapper.movimientoDtOtoMovimiento(movimientoDto);
            // Guardar el cliente en la base de datos
            Movimiento result = movimientoRepository.save(movimiento);
            //Actualizado el saldoActual de la entidad cuenta
            cuentaService.updateSaldoActual(movimientoDto.getCuenta().getId(), newSaldo);
            // Insertar Transacciones
            AuditoriaDto auditoriaDto = AuditoriaDto.builder()
                    .tipoTransaccion(movimiento.getTypeMovement())
                    .fecha(new Date())
                    .usuario(cuenta.getNombreCliente())
                    .descripcion("Se realizo una transacction de " + movimientoDto.getTipoMovimiento())
                    .monto(movimientoDto.getValor())
                    .cuentaId(movimientoDto.getCuenta().getId())
                    .build();
            auditoriaService.save(auditoriaDto);
            return movimientoMapper.movimientoToMovimientoDto(result);
        } catch (EmptyResultDataAccessException e) {
            System.out.println("Error " +  e.getMessage());
        }
        return null;
    }

    @Override
    public MovimientoDto update(Long id, MovimientoDto clientDto) {

        // Verificar si el cliente con el ID dado existe en la base de datos
        Optional<Movimiento> clienteExistenteOptional = movimientoRepository.findById(id);
        MovimientoDto clientDtoResul = new MovimientoDto();
        if (clienteExistenteOptional.isPresent()) {

            Movimiento movimiento = movimientoMapper.movimientoDtOtoMovimiento(clientDto);
            movimiento.setId(id);
            // Guardar el cliente actualizado en la base de datos
            clientDtoResul = movimientoMapper.movimientoToMovimientoDto(movimientoRepository.save(movimiento));
        } else {
            // Manejar el caso en que el cliente no exista en la base de datos
            System.err.println("Erro no existe informacion");
        }
        return clientDtoResul;

    }
    @Override
    public void delete(Long id) {
        try {
            movimientoRepository.deleteById(id);
            System.out.println("Cliente eliminado correctamente");
        } catch (EmptyResultDataAccessException e) {
            System.out.println("No se encontró ningún cliente con el ID proporcionado");
        }
    }
}
