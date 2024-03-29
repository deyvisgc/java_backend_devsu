package com.example.prueba_tecnica.integration;
import com.example.prueba_tecnica.dto.ClientDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClientApiIntegrationTest  {
    private static Long clientId;
    @Autowired
    private TestRestTemplate restTemplate;
    @LocalServerPort
    private int port;

    @Test
    @Order(1)
    public void testCreateClients() {
        ClientDto clientDto = new ClientDto();
        clientDto.setNombre("Salvatore Torres");
        clientDto.setGenero("Masculino");
        clientDto.setEdad(30);
        clientDto.setDireccion("Lima, Peru");
        clientDto.setIdentificacion("12345678");
        clientDto.setTelefono("123456789");
        clientDto.setPassword("12345");
        ResponseEntity<ClientDto> response = restTemplate.postForEntity("/api/clientes", clientDto, ClientDto.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Salvatore Torres", response.getBody().getNombre());
        assertNotNull(response.getBody().getId());
        clientId = response.getBody().getId();
    }

    @Test
    @Order(2)
    public void testGetAllClients() {

        // Realizar la solicitud GET para listar todos los usuarios
        ResponseEntity<ClientDto[]> response = restTemplate.getForEntity("/api/clientes/", ClientDto[].class);

        // Verificar que la solicitud fue exitosa (código 200)
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verificar que la respuesta no es nula
        assertNotNull(response.getBody());

        // Verificar que se reciben algunos usuarios en la respuesta
        ClientDto[] lsclient = response.getBody();
        assertTrue(lsclient.length > 0);
    }
    @Test
    @Order(3)
    public void testUpdateClients() {
        ClientDto updatedClient = new ClientDto();
        updatedClient.setNombre("Abaya Garcia");
        updatedClient.setGenero("Masculino");
        updatedClient.setEdad(29);
        updatedClient.setDireccion("Chiclayo, Peru");
        updatedClient.setIdentificacion("12345678");
        updatedClient.setTelefono("123456789");
        updatedClient.setPassword("4567896");
        // Enviar la solicitud PUT al servidor para actualizar el cliente
        ResponseEntity<ClientDto> response = restTemplate.exchange( "/api/clientes/{id}", HttpMethod.PUT, new HttpEntity<>(updatedClient), ClientDto.class, clientId);
        // Verificar que la solicitud PUT fue exitosa (código 200)
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verificar que el cliente actualizado coincide con los datos enviados
        ClientDto updatedClientResponse = response.getBody();
        assertNotNull(updatedClientResponse);
        assertEquals(clientId, updatedClientResponse.getId());
        assertEquals(updatedClient.getNombre(), updatedClientResponse.getNombre());
        assertEquals(updatedClient.getPassword(), updatedClientResponse.getPassword());
    }
    @Test
    @Order(4)
    public void testGetClientsById() {
        ResponseEntity<ClientDto> response = restTemplate.getForEntity("/api/clientes/" + clientId, ClientDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        System.err.println("Name: " + response.getBody().getNombre());
        assertEquals("Abaya Garcia", response.getBody().getNombre());
    }
    @Test
    @Order(5)
    public void testDeleteClientsById() {
        // Enviar la solicitud DELETE al servidor para eliminar el cliente
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/clientes/{id}", HttpMethod.DELETE, null, Void.class, clientId);
        // Verificar que la solicitud DELETE fue exitosa (código 204 - No Content)
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}