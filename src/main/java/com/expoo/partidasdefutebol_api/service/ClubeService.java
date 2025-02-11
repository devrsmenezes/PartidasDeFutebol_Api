package com.expoo.partidasdefutebol_api.service;

import com.expoo.partidasdefutebol_api.dto.ClubeDTO;
import com.expoo.partidasdefutebol_api.model.Clube;
import com.expoo.partidasdefutebol_api.repository.ClubeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClubeService {

    private final ClubeRepository clubeRepository;

    public ClubeService(ClubeRepository clubeRepository) {
        this.clubeRepository = clubeRepository;
    }

    public void criarClube(ClubeDTO clubeDTO) {
        Clube novoClube = new Clube(clubeDTO);
        clubeRepository.save(novoClube);
    }

    public ClubeDTO atualizarClube(Long id, ClubeDTO clubeDTO) {
        Clube clube = clubeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Clube não encontrado."));

        clube.setNome(clubeDTO.getNome());
        clube.setEstado(clubeDTO.getEstado());
        clube.setAtivo(clubeDTO.isAtivo());

        Clube atualizado = clubeRepository.save(clube);

        return new ClubeDTO(atualizado);
    }

    public void inativarClube(Long id) {
        Clube clube = clubeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Clube não encontrado."));

        clube.setAtivo(false);
        clubeRepository.save(clube);
    }

    public ClubeDTO buscarClube(Long id) {
        Clube clube = clubeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Clube não encontrado."));

        return new ClubeDTO(clube);
    }

    public Page<ClubeDTO> listarClubes(String nome, String estado, Boolean ativo, Pageable pageable) {
        Page<Clube> clubes = clubeRepository.findByFiltros(nome, estado, ativo, pageable);
        return clubes.map(ClubeDTO::new);
    }
}
