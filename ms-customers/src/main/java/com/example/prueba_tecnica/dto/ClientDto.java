package com.example.prueba_tecnica.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ClientDto {
    @ApiModelProperty(hidden = true)
    private Long id;
    @NotBlank(message = "Nombre es requerido")
    private String nombre;
    @NotBlank(message = "genero es requerido")
    private String genero;
    @NotNull(message = "La edad es requerida")
    @Min(value = 18, message = "La edad debe ser mayor o igual a 18")
    @Max(value = 110, message = "La edad debe ser menor o igual a 110")
    private Integer edad;
    @NotBlank(message = "Identificaciòn es requerido")
    private String identificacion;
    @NotBlank(message = "Direcciòn es requerido")
    @Size(min=10, max=200, message="La direcciòn debe ser mayor de 10 y menor que 200 caracteres")
    private String direccion;
    @Size(min=9, max=9, message="El telefono debe tener 9 digitos")
    private String telefono;
    @NotBlank(message = "La contraseña es requerido")
    private String password;
    /*Cuenta Opcional*/
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    CuentaDto cuentaDto;
    @ApiModelProperty(hidden = true)
    @Builder.Default
    private boolean estado= true;
}
