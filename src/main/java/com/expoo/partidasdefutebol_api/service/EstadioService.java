package com.expoo.partidasdefutebol_api.service;

import com.expoo.partidasdefutebol_api.dto.EstadioDTO;
import com.expoo.partidasdefutebol_api.model.Estadio;
import com.expoo.partidasdefutebol_api.repository.EstadioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EstadioService {

    private final EstadioRepository estadioRepository;

    public EstadioService(EstadioRepository estadioRepository) {
        this.estadioRepository = estadioRepository;
    }

    @Transactional
    public EstadioDTO cadastrar(EstadioDTO estadioDTO) {
        validarEstadio(estadioDTO);
        Estadio novoEstadio = new Estadio(estadioDTO.getNome());
        Estadio estadioSalvo = estadioRepository.save(novoEstadio);
        return EstadioDTO.of(estadioSalvo);
    }

    @Transactional
    public EstadioDTO editar(Long id, EstadioDTO estadioDTO) {
        validarEstadio(estadioDTO);
        Estadio estadioExistente = buscarEstadioExistente(id);
        if (estadioRepository.existsByNomeAndIdNot(estadioDTO.getNome(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um estádio com o nome: " + estadioDTO.getNome());
        }
        estadioExistente.setNome(estadioDTO.getNome());
        Estadio estadioAtualizado = estadioRepository.save(estadioExistente);
        return EstadioDTO.of(estadioAtualizado);
    }

    @Transactional(readOnly = true)
    public EstadioDTO buscar(Long id) {
        Estadio estadio = buscarEstadioExistente(id);
        return EstadioDTO.of(estadio);
    }

    @Transactional(readOnly = true)
    public Page<EstadioDTO> listar(Pageable pageable) {
        return estadioRepository.findAll(pageable).map(EstadioDTO::of);
    }

    private void validarEstadio(EstadioDTO estadioDTO) {
        if (estadioDTO.getNome() == null || estadioDTO.getNome().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do estádio não pode ser vazio");
        }
        if (estadioDTO.getNome().length() < 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do estádio deve ter pelo menos 3 caracteres");
        }
    }

    private Estadio buscarEstadioExistente(Long id) {
        return estadioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estádio não encontrado com o id: " + id));
    }
}
