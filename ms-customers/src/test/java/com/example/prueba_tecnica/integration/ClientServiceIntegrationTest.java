package com.example.prueba_tecnica.integration;

import com.example.prueba_tecnica.client.CuentaDtoFeign;
import com.example.prueba_tecnica.dto.ClientDto;
import com.example.prueba_tecnica.dto.CuentaDto;
import com.example.prueba_tecnica.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
public class ClientServiceIntegrationTest {
    @Autowired
    private ClientService clientService;

    @Test
    public void testCreateClienteAndAccount_Successful() {
        // Datos de la cuenta
        CuentaDto cuentaDto = CuentaDto.builder()
                .numeroCuenta("12345678")
                .tipoCuenta("Corriente")
                .saldoInicial(1000)
                .build();
        ClientDto clientDto = ClientDto.builder()
                .nombre("Salvatore Torres")
                .genero("Masculino")
                .direccion("Lima, Peru")
                .identificacion("12345678")
                .telefono("123456789")
                .password("12345")
                .cuentaDto(cuentaDto)
                .build();
        ClientDto client = clientService.save(clientDto);
        List<CuentaDtoFeign> lscuentaDtoFeign = clientService.getByAccount(client.getId());
        for (CuentaDtoFeign dtoFeign :lscuentaDtoFeign) {
            // Verificar que se crea la cuenta correctamente en el microservicio cliente
            assertEquals("12345678", dtoFeign.getNumeroCuenta());
            assertEquals("Corriente", dtoFeign.getTipoCuenta());
        }
    }
}
