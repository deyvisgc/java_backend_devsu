package com.example.prueba_tecnica.repository;

import com.example.prueba_tecnica.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
