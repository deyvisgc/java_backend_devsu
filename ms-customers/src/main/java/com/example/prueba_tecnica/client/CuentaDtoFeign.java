package com.example.prueba_tecnica.client;
import com.example.prueba_tecnica.dto.CuentaDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuentaDtoFeign extends CuentaDto {
    private Long clienteId;
    private boolean estado;
}
