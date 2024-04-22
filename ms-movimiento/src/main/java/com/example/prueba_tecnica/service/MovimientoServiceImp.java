package com.example.prueba_tecnica.service;

import com.example.prueba_tecnica.Kafka.Producer;
import com.example.prueba_tecnica.cliente.ClientDtoFeign;
import com.example.prueba_tecnica.cliente.CustomerClient;
import com.example.prueba_tecnica.dto.ReporteDto;
import com.example.prueba_tecnica.exception.AccountException;
import com.example.prueba_tecnica.dto.AuditoriaDto;
import com.example.prueba_tecnica.dto.CuentaDto;
import com.example.prueba_tecnica.dto.MovimientoDto;
import com.example.prueba_tecnica.entity.Movimiento;
import com.example.prueba_tecnica.exception.RecursoNoEncontradoException;
import com.example.prueba_tecnica.mapper.MovimientoMapper;
import com.example.prueba_tecnica.repository.MovimientoRepository;
import com.example.prueba_tecnica.util.TipoMovimiento;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor /*Esto remplaza al autowired*/
public class MovimientoServiceImp implements MovimientoService {
    private final MovimientoRepository movimientoRepository;
    private final MovimientoMapper movimientoMapper;
    private final CuentaService cuentaService;
    private final CustomerClient customerClient;
    private final KafkaTemplate<String, AuditoriaDto> kafkaTemplate;
    @Value("${topic}")
    String topico;
    @Override
    public List<MovimientoDto> listAll() {

        try {
            List<Movimiento> clientes = movimientoRepository.findAll();
            return clientes.stream()
                    .map(movimientoMapper::movimientoToMovimientoDto)
                    .collect(Collectors.toList());
        } catch (RecursoNoEncontradoException ex) {
            throw new RecursoNoEncontradoException("No se encontro información de movimientos");
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
            throw new RecursoNoEncontradoException("No se encontro movimientos relacionado con el identificador: "  + id);
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
                throw new RecursoNoEncontradoException("El identificador de la cuenta no puede ser null");
            }

            CuentaDto cuenta = cuentaService.getById(movimientoDto.getCuenta().getId());
            if (movimientoDto.getTipoMovimiento().equalsIgnoreCase(TipoMovimiento.DEPOSITO.getDescripcion())) {
                newSaldo = cuenta.getSaldoActual().add(movimientoDto.getValor().abs());
                movimientoDto.setValor(movimientoDto.getValor().abs());
            } else if (movimientoDto.getTipoMovimiento().equalsIgnoreCase(TipoMovimiento.RETIRO.getDescripcion())) {
                // Comprobar si el saldo actual es menor que el valor del movimiento
                if (cuenta.getSaldoActual().compareTo(BigDecimal.ZERO) == 0) {
                    throw new AccountException("Saldo no disponible");
                } else if (cuenta.getSaldoActual().compareTo(movimientoDto.getValor().abs()) < 0) {
                    String mensaje = String.format("Saldo Actual: %s, %s: %s - No tienes saldo suficiente para el movimiento",
                            cuenta.getSaldoActual(),
                            movimientoDto.getTipoMovimiento(),
                            movimientoDto.getValor().abs());
                    throw new AccountException(mensaje);

                }
                newSaldo = cuenta.getSaldoActual().subtract(movimientoDto.getValor().abs()); //devuelvo el valor absoluto
                // Valido si el valor es positivo o negativo
                movimientoDto.setValor(movimientoDto.getValor().signum() == 1 ?  movimientoDto.getValor().negate() : movimientoDto.getValor());
            } else {
                throw new AccountException("El tipo de movimiento debe ser Depósito o Retiro.");
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
            Producer producer = new Producer();
            CompletableFuture<SendResult<String, AuditoriaDto>>  future = producer.send(kafkaTemplate, topico, producer.setAuditoria(movimiento.getTypeMovement(), cuenta.getNombreCliente(), movimientoDto.getValor(), movimientoDto.getCuenta().getId()));
            future.whenCompleteAsync((r, t) -> {
                if(t!=null) {
                    throw new RuntimeException();
                }
                log.info("Se ha enviado el mensaje al topico: {}", topico);
            });
            log.info("FIN: REGISTRAR UN MOVIMIENTO");
            return movimientoMapper.movimientoToMovimientoDto(result);
        }  catch (Exception ex) {
            log.error("ERROR: {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public MovimientoDto update(Long id, MovimientoDto movimientoDto) {

        try {
            BigDecimal newSaldo = BigDecimal.ZERO;
            if (id == null){
                throw new AccountException("El identificador del movimiento no puede ser null: " + id);
            }
            CuentaDto cuenta = cuentaService.getById(movimientoDto.getCuenta().getId());
            Optional<Movimiento> existMovimiento = movimientoRepository.findById(id);

            if (existMovimiento.isPresent()) {
                if (movimientoDto.getTipoMovimiento().equalsIgnoreCase(TipoMovimiento.DEPOSITO.getDescripcion())) {
                    movimientoDto.setValor(movimientoDto.getValor().abs());
                    newSaldo = cuenta.getSaldoActual().add(movimientoDto.getValor().abs());
                } else if (movimientoDto.getTipoMovimiento().equalsIgnoreCase(TipoMovimiento.RETIRO.getDescripcion())) {
                    if (cuenta.getSaldoActual().compareTo(BigDecimal.ZERO) == 0) {
                        BigDecimal newSaldoRest = existMovimiento.get().getValue().subtract(movimientoDto.getValor());
                        if (newSaldoRest.compareTo(BigDecimal.ZERO) == 0) {
                            throw new AccountException("Saldo no disponible");
                        }
                        newSaldo = newSaldoRest.abs();
                    } else {
                        if (movimientoDto.getValor().abs().compareTo(existMovimiento.get().getValue().abs()) < 0) {
                            // Si el valor del nuevo movimiento es menor que el valor del movimiento registrado previamente, se debe sumar el monto restante
                            newSaldo = existMovimiento.get().getBalance().add(movimientoDto.getValor().abs());
                        }  else if (cuenta.getSaldoActual().compareTo(movimientoDto.getValor().abs()) < 0) {
                            String mensaje = String.format("Saldo Actual: %s, %s: %s - No tienes saldo suficiente para el movimiento",
                                    cuenta.getSaldoActual(),
                                    movimientoDto.getTipoMovimiento(),
                                    movimientoDto.getValor().abs());
                            throw new AccountException(mensaje);
                        } else {
                            newSaldo = cuenta.getSaldoActual().subtract(movimientoDto.getValor().abs());
                        }
                    }
                    movimientoDto.setValor(movimientoDto.getValor().signum() == 1 ? movimientoDto.getValor().negate() : movimientoDto.getValor());
                } else {
                    throw new AccountException("El tipo de movimiento debe ser Depósito o Retiro.");
                }
                log.info("NEW SALDO: {}", newSaldo);
                movimientoDto.setSaldo(newSaldo);
                // Mapear el DTO a la entidad Cliente
                Movimiento movimiento = movimientoMapper.movimientoDtOtoMovimiento(movimientoDto);
                movimiento.setId(id);
                Movimiento result = movimientoRepository.save(movimiento);
                //Actualizado el saldoActual de la entidad cuenta
                cuentaService.updateSaldoActual(cuenta.getCuenta_id(), newSaldo);
                /*
                // Insertar Transacciones
                AuditoriaDto auditoriaDto = AuditoriaDto.builder()
                        .tipoTransaccion(movimiento.getTypeMovement())
                        .fecha(new Date())
                        .usuario(cuenta.getNombreCliente())
                        .descripcion("Se realizo una actualizacion de transacction de " + movimientoDto.getTipoMovimiento())
                        .monto(movimientoDto.getValor())
                        .cuentaId(movimientoDto.getCuenta().getId())
                        .build();
                Producer producer = new Producer();

                 */
                log.info("FIN: ACTUALIZAR UN MOVIMIENTO");
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
            log.info("Movimiento eliminado correctamente");
        } catch (Exception ex) {
            log.error("ERROR: {}", ex.getMessage());
            throw ex;
        }
    }
    @Override
    public List<ReporteDto> generarReporte(String fechaIni, String fechaFin, Long cliente) {
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
                        reporteDto.setEstado(movimiento.getAccount().getStatus());
                        reporteDto.setMovimiento(movimiento.getValue());
                        reporteDto.setSaldoDisponible(movimiento.getBalance());
                        reporteDto.setTipoMovimiento(movimiento.getTypeMovement());
                        return reporteDto;
                    }).collect(Collectors.toList());
            if (reporte.size() == 0) {
                throw new RecursoNoEncontradoException("No se encontro información de movimientos");
            }
            log.info("FIN: OBTENER REPORTES MOVIMIENTOS");
            return reporte;
        } catch (Exception ex) {
            log.error("ERROR: {}", ex.getMessage());
            throw ex;
        }
    }
}
