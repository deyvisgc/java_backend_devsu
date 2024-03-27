package com.example.prueba_tecnica.repository;

import com.example.prueba_tecnica.entity.Auditoria;
import com.example.prueba_tecnica.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
}
