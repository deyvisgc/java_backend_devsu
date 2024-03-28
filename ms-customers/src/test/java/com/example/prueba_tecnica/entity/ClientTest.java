package com.example.prueba_tecnica.entity;


import com.example.prueba_tecnica.dto.ClientDto;
import com.example.prueba_tecnica.mapper.ClienteMapper;
import com.example.prueba_tecnica.repository.ClientRepository;
import com.example.prueba_tecnica.service.ClientServiceImp;
import org.junit.jupiter.api.*;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Optional;

import static org.mockito.Mockito.*;
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClientTest {
    @Mock
    private ClientRepository clientRepositoryMock;
    @Autowired
    private ClientServiceImp clientServiceImp;
    @Spy
    ClienteMapper clienteMapperMock = Mappers.getMapper(ClienteMapper.class);

    private Client clienteExistente;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        clienteExistente = crearClienteExistente();;
    }
    private Client crearClienteExistente() {
        Client cliente = new Client();
        cliente.setId(1L);
        cliente.setName("Deyvis Garcia");
        cliente.setGender("Masculino");
        cliente.setAge(28);
        cliente.setStatus(true);
        cliente.setAddress("Puno, Peru");
        cliente.setIdentification("12345678");
        cliente.setPhone("91091443");
        cliente.setPassword("password123");
        cliente.setStatus(true);
        return cliente;
    }

    @Test
    public void testCrearCliente() {
        when(clientRepositoryMock.findById(1L)).thenReturn(Optional.of(clienteExistente));
        // Llamar al método del servicio para crear un cliente existente
        ClientDto clienteDtoEsperado = clienteMapperMock.clienteToClienteDTO(clienteExistente);
        System.out.println("Cliente DTO esperado: " + clienteDtoEsperado);
        Client nuevoCliente = clienteMapperMock.clienteDTOtoCliente(clientServiceImp.save(clienteDtoEsperado));
        System.out.println("Nuevo Cliente: " + nuevoCliente);
        // Verificar que el cliente devuelto es el mismo que el cliente existente
        Assertions.assertEquals(clienteExistente, nuevoCliente);
        // Verificar que no se haya llamado al método save del repositorio
        verify(clientRepositoryMock, never()).save(clienteExistente);
    }
}