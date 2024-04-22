package com.example.prueba_tecnica.service;

import com.example.prueba_tecnica.client.CuentaDtoFeign;
import com.example.prueba_tecnica.dto.ClientDto;
import org.hibernate.service.spi.ServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ClientService {
    Page<ClientDto> listAll(Pageable pageable)  throws ServiceException;
    public ClientDto getById(Long  id);
    public List<CuentaDtoFeign> getByAccount(Long id);
    public ClientDto save(ClientDto clientDto);
    public ClientDto update(Long id, ClientDto clientDto);
    public void delete(Long  id);
}
