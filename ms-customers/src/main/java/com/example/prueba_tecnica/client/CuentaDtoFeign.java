package com.example.prueba_tecnica.client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CuentaDtoFeign {
    private String numeroCuenta;
    private String tipoCuenta;
    private double saldoInicial;
    private Long clienteId;
    private boolean estado;
}
