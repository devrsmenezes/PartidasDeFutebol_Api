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
        return EstadioDTO.of(estadioRepository.save(novoEstadio));
    }

    @Transactional
    public EstadioDTO editar(Long id, EstadioDTO estadioDTO) {
        validarEstadio(estadioDTO);
        Estadio estadioExistente = buscarEstadioPorId(id);

        if (estadioRepository.existsByNomeAndIdNot(estadioDTO.getNome(), id)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Já existe um estádio com o nome: " + estadioDTO.getNome()
            );
        }

        estadioExistente.setNome(estadioDTO.getNome());
        return EstadioDTO.of(estadioRepository.save(estadioExistente));
    }

    @Transactional(readOnly = true)
    public EstadioDTO buscar(Long id) {
        return EstadioDTO.of(buscarEstadioPorId(id));
    }

    @Transactional(readOnly = true)
    public Page<EstadioDTO> listar(Pageable pageable) {
        return estadioRepository.findAll(pageable).map(EstadioDTO::of);
    }

    private void validarEstadio(EstadioDTO estadioDTO) {
        String nome = estadioDTO.getNome();

        if (nome == null || nome.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do estádio não pode ser vazio");
        }

        if (nome.trim().length() < 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do estádio deve ter pelo menos 3 caracteres");
        }
    }

    private Estadio buscarEstadioPorId(Long id) {
        return estadioRepository.findById(id)
            .orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Estádio não encontrado com o id: " + id));
    }
}
