package com.example.prueba_tecnica.dto;

import com.example.prueba_tecnica.entity.Cuenta;
import com.example.prueba_tecnica.entity.Movimiento;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoDto {
    private Long movimiento_id;
    @NotNull(message = "La fecha del movimiento es requerida")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaMovimiento;
    @NotBlank(message = "Tipo movimiento es requerido")
    private String tipoMovimiento;
    @NotNull(message = "El monto del valor es requerido.")
    @Digits(integer = Integer.MAX_VALUE, fraction = Integer.MAX_VALUE, message = "El valor debe ser numérico.")
    private BigDecimal valor;
    private BigDecimal saldo;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Cuenta cuenta;
}
