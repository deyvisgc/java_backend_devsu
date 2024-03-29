package com.example.prueba_tecnica.dto;

import com.example.prueba_tecnica.entity.Cuenta;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoDto {
    @ApiModelProperty(hidden = true)
    private Long movimiento_id;
    @ApiModelProperty(value = "Fecha del movimiento", example = "2024-03-29")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaMovimiento;
    @ApiModelProperty(value = "Tipo de movimiento", example = "Depósitos o Retiros")
    @NotBlank(message = "Tipo movimiento es requerido")
    private String tipoMovimiento;
    @ApiModelProperty(value = "Valor del Movimiento", example = "-999 o 999")
    @NotNull(message = "El monto del valor es requerido.")
    @Digits(integer = Integer.MAX_VALUE, fraction = Integer.MAX_VALUE, message = "El valor debe ser numérico.")
    private BigDecimal valor;
    @ApiModelProperty(hidden = true)
    private BigDecimal saldo;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Cuenta cuenta;
}
