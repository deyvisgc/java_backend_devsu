package com.example.prueba_tecnica.service;

import com.example.prueba_tecnica.client.CuentaDtoFeign;
import com.example.prueba_tecnica.dto.ClientDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ClientService {
    public List<ClientDto> listAll();
    public ClientDto getById(Long  id);
    public List<CuentaDtoFeign> getByAccount(Long id);
    public ClientDto save(ClientDto clientDto);
    public ClientDto update(Long id, ClientDto clientDto);
    public void delete(Long  id);
}
