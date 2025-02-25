package com.expoo.partidasdefutebol_api.service;


import com.expoo.partidasdefutebol_api.dto.EstadioDTO;
import com.expoo.partidasdefutebol_api.model.Estadio;
import com.expoo.partidasdefutebol_api.repository.EstadioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstadioService {

    @Autowired
    private EstadioRepository estadioRepository;

    public Estadio cadastrar (EstadioDTO estadioDTO) {
        Estadio estadio = new Estadio();
        estadio.setNome(estadioDTO.getNome());
        return estadio;
   }
}
