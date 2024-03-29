package com.example.prueba_tecnica.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CuentaDto {
    private String numeroCuenta;
    private String tipoCuenta;
    private double saldoInicial;
}
