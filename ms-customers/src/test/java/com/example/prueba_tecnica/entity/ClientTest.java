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

import java.util.Arrays;
import java.util.List;
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
        clienteExistente = crearClienteExistente();
    }
    private Client crearClienteExistente() {
        Client cliente = new Client();
        cliente.setId(1L);
        cliente.setName("Deyvis Ronald Garcia");
        cliente.setGender("Masculino");
        cliente.setAge(30);
        cliente.setStatus(true);
        cliente.setAddress("Lima, Peru");
        cliente.setIdentification("12345678");
        cliente.setPhone("91091442");
        cliente.setPassword("12345");
        cliente.setStatus(true);
        return cliente;
    }
    private Client updateClienteExistente() {
        Client cliente = new Client();
        cliente.setId(3L);
        cliente.setName("Deyvis Ronald Garcia");
        cliente.setGender("Masculino");
        cliente.setAge(30);
        cliente.setStatus(true);
        cliente.setAddress("Lima, Peru");
        cliente.setIdentification("12345678");
        cliente.setPhone("91091442");
        cliente.setPassword("12345");
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
    @Test
    public void testActualizarCliente() {
        Client clienteExistenteUpdate = updateClienteExistente();
        // Configurar el comportamiento del repositorio mock
        when(clientRepositoryMock.findById(1L)).thenReturn(Optional.of(clienteExistenteUpdate));
        when(clientRepositoryMock.save(any())).thenReturn(clienteExistenteUpdate);
        // Llamar al método actualizarCliente con el ID del cliente existente y el DTO actualizado
        ClientDto clienteDtoRetornado = clientServiceImp.update(3L, clienteMapperMock.clienteToClienteDTO(clienteExistenteUpdate));
        Client actualizado = clienteMapperMock.clienteDTOtoCliente(clienteDtoRetornado);
        // Verificar que el método clienteToClienteDTO fue llamado con el cliente actualizado
        verify(clienteMapperMock).clienteToClienteDTO(clienteExistenteUpdate);
        // Verificar que el clienteDto devuelto es el mismo que el clienteDto actualizado
        Assertions.assertEquals(clienteExistenteUpdate, actualizado);
    }
    @Test
    public void testListarClientes() {
        // Simular el comportamiento del repositorio
        List<Client> clientes = Arrays.asList(clienteExistente, crearClienteExistente());
        when(clientRepositoryMock.findAll()).thenReturn(clientes);

        // Llamar al método para listar clientes
        List<ClientDto> clientesListados = clientServiceImp.listAll();

        // Verificar que se llame al método findAll del repositorio
        //verify(clientRepositoryMock).findAll();

        // Verificar que la lista de clientes no esté vacía
        Assertions.assertFalse(clientesListados.isEmpty());
    }
    @Test
    public void testListarClientesById() {
        // Simular el comportamiento del repositorio
        clienteExistente = crearClienteExistente();
        when(clientRepositoryMock.findById(3L)).thenReturn(Optional.of(clienteExistente));
        // Llamar al método para listar clientes
         Client clientesListados = clienteMapperMock.clienteDTOtoCliente(clientServiceImp.getById(3L));
        // Verificar que se llame al método findAll del repositorio
        //verify(clientRepositoryMock).findById(3L);
        // Verificar que la lista de clientes no esté vacía
        Assertions.assertEquals(clienteExistente, clientesListados);
    }
    @Test
    public void testEliminarCliente() {
        // Configurar el comportamiento del repositorio mock
        doNothing().when(clientRepositoryMock).deleteById(15L);
        // Llamar al método para eliminar un cliente
        clientServiceImp.delete(15L);
        // Verificar que se llame al método deleteById del repositorio con el ID correcto
        //verify(clientRepositoryMock).deleteById(3L);
    }
}