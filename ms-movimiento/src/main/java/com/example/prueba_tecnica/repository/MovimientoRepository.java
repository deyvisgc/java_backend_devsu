package com.example.prueba_tecnica.repository;

import com.example.prueba_tecnica.entity.Movimiento;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    @Query(value = "SELECT m.*,c.* FROM movement m INNER JOIN account c ON m.account_id = c.account_id WHERE m.mov_date >= :fechaInicio AND m.mov_date <= :fechaFin AND c.client_id = :cliente", nativeQuery = true)
    List<Movimiento> obtenerReportesPorFechaYCliente(@Param("fechaInicio") String fechaInicio, @Param("fechaFin") String fechaFin, @Param("cliente") Long cliente);
    @Modifying
    @Query(value = "UPDATE Movimiento SET acc_balance_actual = ?2 WHERE account_id = ?1")
    void updateSaldoActual(Long cuentaId, BigDecimal nuevoSaldoActual);
}
