package com.example.prueba_tecnica.dto;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Este objeto es opcional; solo se requiere enviar su información si se desea crear un cliente y su cuenta simultáneamente.")
public class CuentaDto {
    private String numeroCuenta;
    private String tipoCuenta;
    private double saldoInicial;
}
