package com.example.prueba_tecnica.integration;

import com.example.prueba_tecnica.client.CuentaDtoFeign;
import com.example.prueba_tecnica.dto.ClientDto;
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
    public void testCreateAccount_Successful() {
        /*
        * setTipo_negocio: 0 crea cliente, 1 crea clientes y cuentas
        * */
        ClientDto clientDto = new ClientDto();
        clientDto.setNombre("Salvatore Torres");
        clientDto.setGenero("Masculino");
        clientDto.setEdad(30);
        clientDto.setDireccion("Lima, Peru");
        clientDto.setIdentificacion("12345678");
        clientDto.setTelefono("123456789");
        clientDto.setPassword("12345");
        // Datos de la cuenta
        clientDto.setTipo_negocio("1");
        clientDto.setNumeroCuenta("12345678");
        clientDto.setTipoCuenta("Corriente");
        clientDto.setSaldoInicial(1000);
        clientDto.setEstado(true);
        // Simular llamada al microservicio de movimiento utilizando Feign
        ClientDto createdAccount = clientService.save(clientDto);
        List<CuentaDtoFeign> lscuentaDtoFeign = clientService.getByAccount(createdAccount.getId());
        for (CuentaDtoFeign dtoFeign :lscuentaDtoFeign) {
            // Verificar que se crea la cuenta correctamente en el microservicio cliente
            assertEquals("12345678", dtoFeign.getNumeroCuenta());
            assertEquals("Corriente", dtoFeign.getTipoCuenta());
        }
    }
}
