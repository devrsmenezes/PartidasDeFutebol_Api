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

@Service
public class EstadioService {

    @Autowired
    private EstadioRepository estadioRepository;

    public Estadio cadastrar(EstadioDTO estadioDTO) {
        validarEstadio(estadioDTO);
        Estadio novoEstadio = new Estadio();
        novoEstadio.setNome(estadioDTO.getNome());
        return estadioRepository.save(novoEstadio);
    }

    public Estadio editar(Long id, EstadioDTO estadioDTO) {
        validarEstadio(estadioDTO);
        Estadio estadioExistente = buscarEstadioExistente(id);
        if (estadioRepository.existsByNomeAndIdNot(estadioDTO.getNome(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um estádio com o nome: " + estadioDTO.getNome());
        }
        estadioExistente.setNome(estadioDTO.getNome());
        return estadioRepository.save(estadioExistente);
    }

    public EstadioDTO buscar(Long id) {
        Estadio estadio = buscarEstadioExistente(id);
        return converterParaDTO(estadio);
    }

    public Page<EstadioDTO> listar(Pageable pageable) {
        Page<Estadio> estadios = estadioRepository.findAll(pageable);
        return estadios.map(this::converterParaDTO);
    }

    private void validarEstadio(EstadioDTO estadioDTO) {
        if (estadioDTO.getNome() == null || estadioDTO.getNome().isEmpty()) {
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

    private EstadioDTO converterParaDTO(Estadio estadio) {
        EstadioDTO dto = new EstadioDTO();
        dto.setId(estadio.getId());
        dto.setNome(estadio.getNome());
        return dto;
    }
}
