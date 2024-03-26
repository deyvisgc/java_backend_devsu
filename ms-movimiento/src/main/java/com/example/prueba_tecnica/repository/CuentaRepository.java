package com.example.prueba_tecnica.repository;

import com.example.prueba_tecnica.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
}
