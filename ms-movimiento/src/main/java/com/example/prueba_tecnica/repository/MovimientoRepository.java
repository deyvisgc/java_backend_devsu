package com.example.prueba_tecnica.repository;

import com.example.prueba_tecnica.entity.Cuenta;
import com.example.prueba_tecnica.entity.Movimiento;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    @Query("SELECT m FROM Movimiento m WHERE m.date BETWEEN :fechaInicio AND :fechaFin AND m.account.clienteId = :cliente")
    List<Movimiento> obtenerReportesPorFechaYCliente(@Param("fechaInicio") Date fechaInicio, @Param("fechaFin") Date fechaFin, @Param("cliente") Long cliente);

}
