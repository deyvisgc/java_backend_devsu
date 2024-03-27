package com.example.prueba_tecnica.dto;

import com.example.prueba_tecnica.entity.Cuenta;
import com.example.prueba_tecnica.entity.Movimiento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoDto {
    private Long movimiento_id;
    //@NotNull(message = "La fecha del movimiento no puede ser nula")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaMovimiento;
    @NotBlank(message = "Tipo movimiento es requerido")
    private String tipoMovimiento;
    private BigDecimal valor;
    private BigDecimal saldo;
    private Cuenta cuenta;
}
