package com.example.prueba_tecnica.service;

import com.example.prueba_tecnica.cliente.ClientDtoFeign;
import com.example.prueba_tecnica.cliente.CustomerClient;
import com.example.prueba_tecnica.dto.ReporteDto;
import com.example.prueba_tecnica.entity.Cuenta;
import com.example.prueba_tecnica.exception.AccountException;
import com.example.prueba_tecnica.dto.AuditoriaDto;
import com.example.prueba_tecnica.dto.CuentaDto;
import com.example.prueba_tecnica.dto.MovimientoDto;
import com.example.prueba_tecnica.entity.Movimiento;
import com.example.prueba_tecnica.exception.RecursoNoEncontradoException;
import com.example.prueba_tecnica.mapper.MovimientoMapper;
import com.example.prueba_tecnica.repository.MovimientoRepository;
import com.example.prueba_tecnica.util.TipoMovimiento;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor /*Esto remplaza al autowired*/
public class MovimientoServiceImp implements MovimientoService {
    private final MovimientoRepository movimientoRepository;
    private final MovimientoMapper movimientoMapper;
    private final CuentaService cuentaService;
    private final AuditoriaService auditoriaService;
    private final CustomerClient customerClient;
    @Override
    public List<MovimientoDto> listAll() {

        try {
            List<Movimiento> clientes = movimientoRepository.findAll();
            return clientes.stream()
                    .map(movimientoMapper::movimientoToMovimientoDto)
                    .collect(Collectors.toList());
        } catch (RecursoNoEncontradoException ex) {
            throw new RecursoNoEncontradoException("No se encontro informaciòn de movimientos");
        } catch (Exception ex) {
            log.error("ERROR: {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public MovimientoDto getById(Long id) {
        try {
            Optional<Movimiento> movimiento = movimientoRepository.findById(id);
            if (movimiento.isPresent()) {
                return movimientoMapper.movimientoToMovimientoDto(movimiento.get());
            }
            throw new RecursoNoEncontradoException("No se encontro movimientos relacionado con el id: "  + id);
        } catch (Exception ex) {
            log.error("ERROR: {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public MovimientoDto save(MovimientoDto movimientoDto) {

        try {
            log.info("INICIO:  REGISTRAR UN MOVIMIENTO");
            BigDecimal newSaldo = BigDecimal.ZERO;
            if ( Objects.isNull(movimientoDto.getCuenta()) || movimientoDto.getCuenta().getId() == null){
                throw new RecursoNoEncontradoException("El identificador de la cuenta no puede ser nullo");
            }

            CuentaDto cuenta = cuentaService.getById(movimientoDto.getCuenta().getId());
            if (movimientoDto.getTipoMovimiento().equalsIgnoreCase(TipoMovimiento.DEPOSITO.getDescripcion())) {
                newSaldo = cuenta.getSaldoActual().add(movimientoDto.getValor().abs());
                movimientoDto.setValor(movimientoDto.getValor().abs());
            } else {
                // Comprobar si el saldo actual es menor que el valor del movimiento
                if (cuenta.getSaldoActual().compareTo(BigDecimal.ZERO) == 0) {
                    throw new AccountException("Saldo no disponible");
                } else if (cuenta.getSaldoActual().compareTo(movimientoDto.getValor()) < 0) {
                    throw new AccountException("Saldo Actual: " + cuenta.getSaldoActual() + " Valor Movimiento: " + movimientoDto.getValor() + " no tienes saldo suficiente para el movimiento");
                }
                newSaldo = cuenta.getSaldoActual().subtract(movimientoDto.getValor().abs()); //devuelvo el valor absoluto
            }
            log.info("NEW SALDO: {}", newSaldo);
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
                    .descripcion("Se realizo un registro de transacction de" + movimientoDto.getTipoMovimiento())
                    .monto(movimientoDto.getValor())
                    .cuentaId(movimientoDto.getCuenta().getId())
                    .build();
            auditoriaService.save(auditoriaDto);
            log.info("FIN:  REGISTRAR UN MOVIMIENTO");
            return movimientoMapper.movimientoToMovimientoDto(result);
        }  catch (Exception ex) {
            log.error("ERROR: {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public MovimientoDto update(Long id, MovimientoDto movimientoDto) {

        try {
            log.info("INICIO:  ACTUALIZAR UN MOVIMIENTO");
            BigDecimal newSaldo = BigDecimal.ZERO;
            if (id == null){
                throw new AccountException("El identificador del movimiento no puede ser nullo: " + id);
            }
            CuentaDto cuenta = cuentaService.getById(movimientoDto.getCuenta().getId());
            Optional<Movimiento> existMovimiento = movimientoRepository.findById(id);

            if (existMovimiento.isPresent()) {
                if (movimientoDto.getTipoMovimiento().equalsIgnoreCase(TipoMovimiento.DEPOSITO.getDescripcion())) {
                    movimientoDto.setValor(movimientoDto.getValor().abs());
                    newSaldo = cuenta.getSaldoActual().add(movimientoDto.getValor().abs());
                } else {

                    BigDecimal newSaldoRest = existMovimiento.get().getValue().subtract(movimientoDto.getValor());

                    if (cuenta.getSaldoActual().compareTo(BigDecimal.ZERO) == 0) {

                        if (newSaldoRest.compareTo(BigDecimal.ZERO) == 0) {
                            throw new AccountException("Saldo no disponible");
                        }
                        newSaldo = newSaldoRest.abs();
                    } else {
                        newSaldo = cuenta.getSaldoActual().subtract(movimientoDto.getValor().abs());
                    }
                    log.info(newSaldoRest.toString());
                }
                log.info("NEW SALDO: {}", newSaldo);
                movimientoDto.setSaldo(newSaldo);
                // Mapear el DTO a la entidad Cliente
                Movimiento movimiento = movimientoMapper.movimientoDtOtoMovimiento(movimientoDto);
                movimiento.setId(id);
                Movimiento result = movimientoRepository.save(movimiento);
                //Actualizado el saldoActual de la entidad cuenta
                log.info("ID: {}", cuenta.getCuenta_id());
                cuentaService.updateSaldoActual(cuenta.getCuenta_id(), newSaldo);
                // Insertar Transacciones
                AuditoriaDto auditoriaDto = AuditoriaDto.builder()
                        .tipoTransaccion(movimiento.getTypeMovement())
                        .fecha(new Date())
                        .usuario(cuenta.getNombreCliente())
                        .descripcion("Se realizo una actualizacion de transacction de " + movimientoDto.getTipoMovimiento())
                        .monto(movimientoDto.getValor())
                        .cuentaId(movimientoDto.getCuenta().getId())
                        .build();
                auditoriaService.save(auditoriaDto);
                log.info("Fin:  ACTUALIZAR UN MOVIMIENTO");
                return movimientoMapper.movimientoToMovimientoDto(result);
            } else {
                throw new RecursoNoEncontradoException("No existe cuenta relacionada con el identificador: " + id);
            }
        }  catch (Exception ex) {
            log.error("ERROR: {}", ex.getMessage());
            throw ex;
        }
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

    @Override
    public List<ReporteDto> generarReporte(Date fechaIni, Date fechaFin, Long cliente) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            List<Movimiento> lmovimientos = movimientoRepository.obtenerReportesPorFechaYCliente(fechaIni, fechaFin, cliente);
             lmovimientos.stream()
                    .map( mov -> {
                        ClientDtoFeign clientDtoFeign = customerClient.findByIdClient(mov.getAccount().getClienteId()).getBody();
                        mov.getAccount().setNameCustomer(clientDtoFeign.getNombre());
                        return mov;
                    }).map(movimientoMapper::movimientoToMovimientoDto)
                    .collect(Collectors.toList());
            List<ReporteDto> reporte = lmovimientos.stream()
                    .map(movimiento -> {
                        ReporteDto reporteDto = new ReporteDto();
                        reporteDto.setFecha(dateFormat.format(movimiento.getDate()));
                        reporteDto.setCliente(movimiento.getAccount().getNameCustomer());
                        reporteDto.setNumeroCuenta(movimiento.getAccount().getAccountNumber());
                        reporteDto.setTipo(movimiento.getAccount().getAccountType());
                        reporteDto.setSaldoInicial(movimiento.getAccount().getInitialBalance());
                        reporteDto.setEstado(movimiento.getAccount().isStatus());
                        reporteDto.setMovimiento(movimiento.getValue());
                        reporteDto.setSaldoDisponible(movimiento.getBalance());
                        reporteDto.setTipoMovimiento(movimiento.getTypeMovement());
                        return reporteDto;
                    }).collect(Collectors.toList());
            return reporte;
        } catch (RecursoNoEncontradoException ex) {
            throw new RecursoNoEncontradoException("No se encontro informaciòn de movimientos");
        } catch (Exception ex) {
            log.error("ERROR: {}", ex.getMessage());
            throw ex;
        }
    }
}
