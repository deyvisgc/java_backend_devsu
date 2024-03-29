package com.example.prueba_tecnica.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteDto {
   private String fecha;
   private String cliente;
   private String tipo;
   private String numeroCuenta;
   private BigDecimal SaldoInicial;
   private boolean estado;
   private BigDecimal movimiento;
   private BigDecimal SaldoDisponible;
   private String tipoMovimiento;

}
