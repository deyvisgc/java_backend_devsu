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
public class MovimientoDto {
    private Long movimiento_id;

    @NotBlank(message = "Fecha Movimiento es Requerido")
    private String fechaMovimiento;

    @NotBlank(message = "Tipo movimiento es requerido")
    private String tipoMovimiento;

    @Min(value = 0, message = "El valor debe ser mayor a 0")
    private Integer valor;

    @Min(value = 0, message = "El saldo debe ser mayor a 0")
    private Integer saldo;

    private Cuenta cuenta;
}
