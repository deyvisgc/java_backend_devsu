package com.example.prueba_tecnica.integration;
import com.example.prueba_tecnica.dto.ClientDto;
import org.junit.jupiter.api.Test;
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
public class ClientApiIntegrationTest  {
    @Autowired
    private TestRestTemplate restTemplate;
    @LocalServerPort
    private int port;
    String baseUrl = "http://localhost:" + port;
    @Test
    public void testGetAllClients() {

        // Realizar la solicitud GET para listar todos los usuarios
        ResponseEntity<ClientDto[]> response = restTemplate.getForEntity(baseUrl +"/api/clientes/", ClientDto[].class);

        // Verificar que la solicitud fue exitosa (código 200)
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verificar que la respuesta no es nula
        assertNotNull(response.getBody());

        // Verificar que se reciben algunos usuarios en la respuesta
        ClientDto[] lsclient = response.getBody();
        Arrays.stream(lsclient).forEach(cli -> {
            System.err.println("Name: " + cli.getNombre());
        });
        assertTrue(lsclient.length > 0);
    }
    @Test
    public void testGetClientsById() {
        Long clientId = 17L;
        ResponseEntity<ClientDto> response = restTemplate.getForEntity("/api/clientes/" + clientId, ClientDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        System.err.println("Name: " + response.getBody().getNombre());
        assertEquals("Deyvis Ronald Garcia", response.getBody().getNombre());
    }
    @Test
    public void testCreateClients() {
        ClientDto clientDto = new ClientDto();
        clientDto.setNombre("Salvatore Torres");
        clientDto.setGenero("Masculino");
        clientDto.setEdad(30);
        clientDto.setDireccion("Lima, Peru");
        clientDto.setIdentificacion("12345678");
        clientDto.setTelefono("123456789");
        clientDto.setPassword("12345");
        clientDto.setEstado(true);
        ResponseEntity<ClientDto> response = restTemplate.postForEntity("/api/clientes", clientDto, ClientDto.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Salvatore Torres", response.getBody().getNombre());
        assertNotNull(response.getBody().getId());
    }
    @Test
    public void testUpdateClients() {
        Long clientId = 18L;
        ClientDto updatedClient = new ClientDto();
        updatedClient.setNombre("Abaya Garcia");
        updatedClient.setGenero("Masculino");
        updatedClient.setEdad(29);
        updatedClient.setDireccion("Chiclayo, Peru");
        updatedClient.setIdentificacion("12345678");
        updatedClient.setTelefono("123456789");
        updatedClient.setPassword("4567896");
        updatedClient.setEstado(true);
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
    public void testDeleteClientsById() {
        // ID del cliente a eliminar
        Long clientId = 18L;
        // Enviar la solicitud DELETE al servidor para eliminar el cliente
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/clientes/{id}", HttpMethod.DELETE, null, Void.class, clientId);
        // Verificar que la solicitud DELETE fue exitosa (código 204 - No Content)
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}