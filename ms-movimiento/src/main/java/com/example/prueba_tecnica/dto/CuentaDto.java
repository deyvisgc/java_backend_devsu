package com.example.prueba_tecnica.dto;
import com.example.prueba_tecnica.entity.Movimiento;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CuentaDto {
    @ApiModelProperty(hidden = true)
    private Long cuenta_id;
    @Size(min=1, max=30, message="El numero de cuenta debe tener 1 hasta 30 digitos.")
    @NotBlank(message = "Numero de cuenta es requerido.")
    private String numeroCuenta;
    @NotBlank(message = "Tipo de cuenta es requerido.")
    private String tipoCuenta;
    @NotNull(message = "El saldo Inicial es requerido.")
    @Min(value = 1, message = "El saldo inicial debe ser mayor a 0")
    private BigDecimal saldoInicial;
    @ApiModelProperty(hidden = true)
    private BigDecimal saldoActual;
    @Min(value = 1, message = "El identificador del cliente debe ser mayor a 0.")
    @NotNull(message = "Se requiere el identificador del cliente.")
    private Long clienteId;
    @ApiModelProperty(hidden = true)
    private String nombreCliente;
    @ApiModelProperty(hidden = true)
    @Builder.Default
    private char estado= '1';
    @ApiModelProperty(hidden = true)
    private List<Movimiento> movimiento;

}
