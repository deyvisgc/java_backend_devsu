package com.example.prueba_tecnica.repository;

import com.example.prueba_tecnica.entity.Cuenta;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Transactional
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    @Modifying
    @Query(value = "UPDATE account SET acc_balance_actual = ?2 WHERE account_id = ?1", nativeQuery = true)
    void updateSaldoActual(Long cuentaId, BigDecimal nuevoSaldoActual);
    @Query(value = "SELECT * FROM account WHERE client_id = :clientId", nativeQuery = true)
    List<Cuenta> findAccountsByClientId(@Param("clientId") Long clientId);
    Page<Cuenta> findAllByStatus(Pageable pageable, char estado);
}
