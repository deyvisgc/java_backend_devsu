package com.example.prueba_tecnica.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditoriaDto {
    private Long auditoriaId;
    private String tipoTransaccion;
    private Date fecha;
    private String usuario;
    private String descripcion;
    private BigDecimal monto;
    private Long cuentaId;
}
