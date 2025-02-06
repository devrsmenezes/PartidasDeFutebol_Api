package com.expoo.partidasdefutebol_api.service;

import com.expoo.partidasdefutebol_api.dto.ClubeDTO;
import com.expoo.partidasdefutebol_api.exception.ConflictException;
import com.expoo.partidasdefutebol_api.exception.NotFoundException;
import com.expoo.partidasdefutebol_api.model.Clube;
import com.expoo.partidasdefutebol_api.repository.ClubeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ClubeService {

    @Autowired
    private ClubeRepository clubeRepository;

    private static final List<String> ESTADOS_VALIDOS = Arrays.asList(
            "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA",
            "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"
    );

    public Clube criarClube(ClubeDTO clubeDTO) {
        validarEstado(clubeDTO.getEstado());
        verificarClubeExistente(clubeDTO.getNome(), clubeDTO.getEstado());

        Clube clube = new Clube();
        clube.setNome(clubeDTO.getNome());
        clube.setEstado(clubeDTO.getEstado());
        clube.setDataCriacao(clubeDTO.getDataCriacao());
        clube.setAtivo(clubeDTO.getAtivo());

        return clubeRepository.save(clube);
    }

    public Clube atualizarClube(Long id, ClubeDTO clubeDTO) {
        Clube clube = clubeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Clube não encontrado"));

        validarEstado(clubeDTO.getEstado());
        if (!clube.getNome().equals(clubeDTO.getNome()) || !clube.getEstado().equals(clubeDTO.getEstado())) {
            verificarClubeExistente(clubeDTO.getNome(), clubeDTO.getEstado());
        }

        clube.setNome(clubeDTO.getNome());
        clube.setEstado(clubeDTO.getEstado());
        clube.setDataCriacao(clubeDTO.getDataCriacao());
        clube.setAtivo(clubeDTO.getAtivo());

        return clubeRepository.save(clube);
    }

    private void validarEstado(String estado) {
        if (!ESTADOS_VALIDOS.contains(estado.toUpperCase())) {
            throw new IllegalArgumentException("Estado inválido");
        }
    }

    private void verificarClubeExistente(String nome, String estado) {
        clubeRepository.findByNomeAndEstado(nome, estado).ifPresent(c -> {
            throw new ConflictException("Já existe um clube com este nome neste estado");
        });
    }

    public void inativarClube(Long id) {
        Clube clube = clubeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Clube não encontrado"));
        clube.setAtivo(false);
        clubeRepository.save(clube);
    }
    public Clube buscarClube(Long id) {
        return clubeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Clube não encontrado"));
    }

}
