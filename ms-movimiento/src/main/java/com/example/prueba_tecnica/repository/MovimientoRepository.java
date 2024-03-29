package com.example.prueba_tecnica.repository;

import com.example.prueba_tecnica.entity.Cuenta;
import com.example.prueba_tecnica.entity.Movimiento;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    //@Query("SELECT m FROM Movimiento m WHERE m.date BETWEEN :fechaInicio AND :fechaFin AND m.account.clienteId = :cliente")
    //@Query("SELECT m FROM Movimiento m WHERE m.date >= :fechaInicio AND m.date <= :fechaFin AND m.account.clienteId = :cliente")
    @Query(value = "SELECT m.*,c.* FROM movement m INNER JOIN account c ON m.account_id = c.account_id WHERE m.mov_date >= :fechaInicio AND m.mov_date <= :fechaFin AND c.client_id = :cliente", nativeQuery = true)
    List<Movimiento> obtenerReportesPorFechaYCliente(@Param("fechaInicio") String fechaInicio, @Param("fechaFin") String fechaFin, @Param("cliente") Long cliente);
}
