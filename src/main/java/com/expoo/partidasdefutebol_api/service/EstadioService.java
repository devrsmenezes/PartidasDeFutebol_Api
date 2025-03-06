package com.expoo.partidasdefutebol_api.service;

import com.expoo.partidasdefutebol_api.dto.EstadioDTO;
import com.expoo.partidasdefutebol_api.model.Estadio;
import com.expoo.partidasdefutebol_api.repository.EstadioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;
import java.util.function.Function;

@Service
public class EstadioService {

    @Autowired
    private EstadioRepository estadioRepository;
    private Function<? super Estadio,? extends EstadioDTO> converterParaDTO;

    public Estadio cadastrar(EstadioDTO estadioDTO) {
        if (estadioDTO.getNome() == null || estadioDTO.getNome().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do estádio não pode ser vazio");
        }
        if (estadioDTO.getNome().length() < 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do estádio deve ter pelo menos 3 caracteres");
        }
        if (estadioRepository.existsByNome(estadioDTO.getNome())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Estádio com este nome já existe");
        }

        Estadio novoEstadio = new Estadio();
        novoEstadio.setNome(estadioDTO.getNome());

        return estadioRepository.save(novoEstadio);
    }

    public Estadio editar(Long id, EstadioDTO estadioDTO) {
        if (estadioDTO.getNome() == null || estadioDTO.getNome().length() < 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do estádio deve ter pelo menos 3 caracteres");
        }

        Optional<Estadio> estadioOptional = estadioRepository.findById(id);
        if (estadioOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estádio não encontrado com o id: " + id);
        }
        Estadio estadioExistente = estadioOptional.get();

        if (estadioRepository.existsByNomeAndIdNot(estadioDTO.getNome(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um estádio com o nome: " + estadioDTO.getNome());
        }

        estadioExistente.setNome(estadioDTO.getNome());
        return estadioRepository.save(estadioExistente);
    }

    public EstadioDTO buscar(Long id) {
        Optional<Estadio> estadioOptional = estadioRepository.findById(id);
        if (estadioOptional.isPresent()) {
            return converterParaDTO(estadioOptional.get());
        } else {
            throw new RuntimeException("Estádio não encontrado");
        }
    }

    private EstadioDTO converterParaDTO(Estadio estadio) {
        if (estadio == null) {
            return null;
        }
        EstadioDTO dto = new EstadioDTO();
        dto.setId(estadio.getId());
        dto.setNome(estadio.getNome());
        return dto;
    }

    public Page<EstadioDTO> listar(Pageable pageable) {
        Page<Estadio> estadio = estadioRepository.findAll(pageable);
        return estadio.map(this.converterParaDTO);
    }
}


