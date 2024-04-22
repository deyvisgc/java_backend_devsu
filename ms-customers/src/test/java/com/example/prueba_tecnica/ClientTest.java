package com.example.prueba_tecnica;


import com.example.prueba_tecnica.dto.ClientDto;
import com.example.prueba_tecnica.entity.Client;
import com.example.prueba_tecnica.mapper.ClienteMapper;
import com.example.prueba_tecnica.repository.ClientRepository;
import com.example.prueba_tecnica.service.ClientServiceImp;
import org.junit.jupiter.api.*;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class ClientTest {
    @Mock
    private ClientRepository clientRepositoryMock;
    @Autowired
    private ClientServiceImp clientServiceImp;
    @Spy
    ClienteMapper clienteMapperMock = Mappers.getMapper(ClienteMapper.class);

    private Client clienteExistente;
    private Long clientId;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        clienteExistente = crearClienteExistente();
    }
    private Client crearClienteExistente() {
        Client cliente = new Client();
        cliente.setName("Jose Luis Garcia");
        cliente.setGender("Masculino");
        cliente.setAge(30);
        cliente.setStatus('1');
        cliente.setAddress("Lima, Peru");
        cliente.setIdentification("345342");
        cliente.setPhone("123456789");
        cliente.setPassword("123456");
        return cliente;
    }
    private Client updateClienteExistente() {
        Client cliente = new Client();
        cliente.setName("Deyvis Ronald Garcia");
        cliente.setGender("Masculino");
        cliente.setAge(30);
        cliente.setStatus('1');
        cliente.setAddress("Lima, Peru");
        cliente.setIdentification("12345678");
        cliente.setPhone("91091442");
        cliente.setPassword("12345");
        return cliente;
    }
    @Test
    @Order(1)
    public void testCrearCliente() {
         //when(clientRepositoryMock.findById(clientId)).thenReturn(Optional.of(clienteExistente));
        // Llamar al método del servicio para crear un cliente existente
        ClientDto clienteDtoEsperado = clienteMapperMock.clienteToClienteDTO(clienteExistente);
        System.out.println("Cliente DTO esperado: " + clienteDtoEsperado);
        Client nuevoCliente = clienteMapperMock.clienteDTOtoCliente(clientServiceImp.save(clienteDtoEsperado));
        clientId = nuevoCliente.getId();
        System.out.println("Nuevo Cliente: " + nuevoCliente);
        // Verificar que el cliente devuelto es el mismo que el cliente existente
        Assertions.assertEquals(clienteExistente, nuevoCliente);
        // Verificar que no se haya llamado al método save del repositorio
        verify(clientRepositoryMock, never()).save(clienteExistente);
    }
    @Order(2)
    @Test
    public void testListarClientes() {
        // Simular el comportamiento del repositorio
        List<Client> clientes = Arrays.asList(clienteExistente, crearClienteExistente());
        when(clientRepositoryMock.findAll()).thenReturn(clientes);

        // Llamar al método para listar clientes
        //List<ClientDto> clientesListados = clientServiceImp.listAll();

        // Verificar que se llame al método findAll del repositorio
        //verify(clientRepositoryMock).findAll();

        // Verificar que la lista de clientes no esté vacía
        //Assertions.assertFalse(clientesListados.isEmpty());
    }
    @Order(3)
    @Test
    public void testListarClientesById() {
        // Simular el comportamiento del repositorio
        clienteExistente = crearClienteExistente();
        when(clientRepositoryMock.findById(clientId)).thenReturn(Optional.of(clienteExistente));
        // Llamar al método para listar clientes
         Client clientesListados = clienteMapperMock.clienteDTOtoCliente(clientServiceImp.getById(clientId));
        // Verificar que se llame al método findAll del repositorio
        //verify(clientRepositoryMock).findById(3L);
        // Verificar que la lista de clientes no esté vacía
        Assertions.assertEquals(clienteExistente, clientesListados);
    }
    @Test
    @Order(4)
    public void testActualizarCliente() {
        Client clienteExistenteUpdate = updateClienteExistente();
        // Configurar el comportamiento del repositorio mock
        when(clientRepositoryMock.findById(clientId)).thenReturn(Optional.of(clienteExistenteUpdate));
        when(clientRepositoryMock.save(any())).thenReturn(clienteExistenteUpdate);
        // Llamar al método actualizarCliente con el ID del cliente existente y el DTO actualizado
        ClientDto clienteDtoRetornado = clientServiceImp.update(clientId, clienteMapperMock.clienteToClienteDTO(clienteExistenteUpdate));
        Client actualizado = clienteMapperMock.clienteDTOtoCliente(clienteDtoRetornado);
        // Verificar que el método clienteToClienteDTO fue llamado con el cliente actualizado
        verify(clienteMapperMock).clienteToClienteDTO(clienteExistenteUpdate);
        // Verificar que el clienteDto devuelto es el mismo que el clienteDto actualizado
        Assertions.assertEquals(clienteExistenteUpdate, actualizado);
    }
    @Test
    @Order(5)
    public void testEliminarCliente() {
        // Configurar el comportamiento del repositorio mock
        doNothing().when(clientRepositoryMock).deleteById(clientId);
        // Llamar al método para eliminar un cliente
        clientServiceImp.delete(clientId);
    }
}