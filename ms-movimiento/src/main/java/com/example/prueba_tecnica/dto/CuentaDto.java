package com.example.prueba_tecnica.dto;
import com.example.prueba_tecnica.entity.Movimiento;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CuentaDto {
    private Long cuenta_id;
    @Size(min=1, max=30, message="El numero de cuenta debe tener 1 hasta 30 digitos")
    @NotBlank(message = "Numero de cuenta es requerido")
    private String numeroCuenta;
    @NotBlank(message = "Tipo de cuenta es requerido")
    private String tipoCuenta;
    //@Min(value = 0, message = "El saldo inicial debe ser mayor a 0")
    private BigDecimal saldoInicial;
    private BigDecimal saldoActual;
    @Min(value = 0, message = "El cliente id debe ser mayor a 0")
    private Integer clienteId;
    private String nombreCliente;

    @Builder.Default
    private boolean estado= true;
    private List<Movimiento> movimiento;
    // Constructor que acepta una lista de movimientos
    /*
    public CuentaDto(Long cuenta_id, String numeroCuenta, String tipoCuenta, BigDecimal saldoInicial, BigDecimal saldoActual, Integer clienteId, String nombreCliente, boolean estado, List<Movimiento> movimiento) {
        this.cuenta_id = cuenta_id;
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.saldoInicial = saldoInicial;
        this.saldoActual = saldoActual;
        this.clienteId = clienteId;
        this.nombreCliente = nombreCliente;
        this.estado = estado;
        this.movimiento = movimiento;
    }

     */

}
