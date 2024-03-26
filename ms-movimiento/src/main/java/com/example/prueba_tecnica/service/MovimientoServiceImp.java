package com.example.prueba_tecnica.service;

import com.example.prueba_tecnica.dto.MovimientoDto;
import com.example.prueba_tecnica.entity.Movimiento;
import com.example.prueba_tecnica.mapper.MovimientoMapper;
import com.example.prueba_tecnica.repository.MovimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor /*Esto remplaza al autowired*/
public class MovimientoServiceImp implements MovimientoService {
    private final MovimientoRepository movimientoRepository;
    private final MovimientoMapper movimientoMapper;
    @Override
    public List<MovimientoDto> listAll() {
        List<Movimiento> clientes = movimientoRepository.findAll();
        return clientes.stream()
                .map(movimientoMapper::movimientoToMovimientoDto)
                .collect(Collectors.toList());
    }

    @Override
    public MovimientoDto getById(Long id) {
        return movimientoMapper.movimientoToMovimientoDto(movimientoRepository.getById(id));
    }

    @Override
    public MovimientoDto save(MovimientoDto clientDto) {

        // Mapear el DTO a la entidad Cliente
        Movimiento movimiento = movimientoMapper.movimientoDtOtoMovimiento(clientDto);
        // Guardar el cliente en la base de datos
        Movimiento result = movimientoRepository.save(movimiento);
        return movimientoMapper.movimientoToMovimientoDto(result);
    }

    @Override
    public MovimientoDto update(Long id, MovimientoDto clientDto) {

        // Verificar si el cliente con el ID dado existe en la base de datos
        Optional<Movimiento> clienteExistenteOptional = movimientoRepository.findById(id);
        MovimientoDto clientDtoResul = new MovimientoDto();
        if (clienteExistenteOptional.isPresent()) {

            Movimiento movimiento = movimientoMapper.movimientoDtOtoMovimiento(clientDto);
            movimiento.setId(id);
            // Guardar el cliente actualizado en la base de datos
            clientDtoResul = movimientoMapper.movimientoToMovimientoDto(movimientoRepository.save(movimiento));
        } else {
            // Manejar el caso en que el cliente no exista en la base de datos
            System.err.println("Erro no existe informacion");
        }
        return clientDtoResul;

    }
    @Override
    public void delete(Long id) {
        try {
            movimientoRepository.deleteById(id);
            System.out.println("Cliente eliminado correctamente");
        } catch (EmptyResultDataAccessException e) {
            System.out.println("No se encontró ningún cliente con el ID proporcionado");
        }
    }
}
