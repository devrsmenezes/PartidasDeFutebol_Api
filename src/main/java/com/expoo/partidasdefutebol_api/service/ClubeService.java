package com.expoo.partidasdefutebol_api.service;

import com.expoo.partidasdefutebol_api.dto.ClubeDTO;
import com.expoo.partidasdefutebol_api.exception.ConflictException;
import com.expoo.partidasdefutebol_api.exception.EntradaInvalidaException;
import com.expoo.partidasdefutebol_api.exception.NotFoundException;
import com.expoo.partidasdefutebol_api.model.Clube;
import com.expoo.partidasdefutebol_api.repository.ClubeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        try {
            validarEstado(clubeDTO.getEstado());
            verificarClubeExistente(clubeDTO.getNome(), clubeDTO.getEstado());

            if (clubeDTO.getDataCriacao().isAfter(LocalDate.now())) {
                throw new EntradaInvalidaException("A data de criação não pode ser no futuro");
            }

            Clube clube = converterClube(new Clube(),clubeDTO);

            return clubeRepository.save(clube);
        } catch (IllegalArgumentException e) {
            throw new EntradaInvalidaException(e.getMessage());
        } catch (ConflictException e) {
            throw e;
        }
    }

    public Clube atualizarClube(Long id, ClubeDTO clubeDTO) {
        try {
            Clube clube = clubeRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Clube não encontrado"));

            validarEstado(clubeDTO.getEstado());
            if (!clube.getNome().equals(clubeDTO.getNome()) || !clube.getEstado().equals(clubeDTO.getEstado())) {
                verificarClubeExistente(clubeDTO.getNome(), clubeDTO.getEstado());
            }

            if (clubeDTO.getDataCriacao().isAfter(LocalDate.now())) {
                throw new EntradaInvalidaException("A data de criação não pode ser no futuro");
            }

            converterClube(clube, clubeDTO);

            return clubeRepository.save(clube);
        } catch (IllegalArgumentException e) {
            throw new EntradaInvalidaException(e.getMessage());
        } catch (ConflictException e) {
            throw e;
        }
    }

    public Clube converterClube (Clube clube, ClubeDTO clubedto) {
        clube.setNome(clubedto.getNome());
        clube.setEstado(clubedto.getEstado());
        clube.setDataCriacao(clubedto.getDataCriacao());
        clube.setAtivo(clubedto.getAtivo());

        return clube;

    }

    private void validarEstado(String estado) {
        if (!ESTADOS_VALIDOS.contains(estado.toUpperCase())) {
            throw new IllegalArgumentException("Estado inválido: " + estado);
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
