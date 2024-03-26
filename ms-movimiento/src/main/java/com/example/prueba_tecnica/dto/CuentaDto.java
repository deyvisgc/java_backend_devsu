package com.example.prueba_tecnica.dto;

import com.example.prueba_tecnica.entity.Cuenta;
import com.example.prueba_tecnica.entity.Movimiento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
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
    private Double saldoInicial;
    @Min(value = 0, message = "El cliente id debe ser mayor a 0")
    private Integer clienteId;
    private String nombreCliente;
    @Builder.Default
    private boolean estado= true;
    private List<Movimiento> movimiento;
}
