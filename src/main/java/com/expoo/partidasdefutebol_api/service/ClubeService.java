package com.expoo.partidasdefutebol_api.service;

import com.expoo.partidasdefutebol_api.dto.ClubeDTO;
import com.expoo.partidasdefutebol_api.dto.RetroDTO;
import com.expoo.partidasdefutebol_api.model.Clube;
import com.expoo.partidasdefutebol_api.model.Partida;
import com.expoo.partidasdefutebol_api.repository.ClubeRepository;
import com.expoo.partidasdefutebol_api.repository.PartidaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ClubeService {

    private final ClubeRepository clubeRepository;

    @Autowired
    private PartidaRepository partidaRepository;

    @Autowired
    public ClubeService(ClubeRepository clubeRepository) {
        this.clubeRepository = clubeRepository;
    }

    public void criar(ClubeDTO clubeDTO) {
        Clube novoClube = clubeDTO.toEntity(); 
        clubeRepository.save(novoClube);
    }

    public ClubeDTO atualizar(Long id, ClubeDTO clubeDTO) {
    
        Clube clube = clubeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Clube não encontrado."));

        clube.setNome(clubeDTO.getNome());
        clube.setEstado(clubeDTO.getEstado());
        clube.setAtivo(clubeDTO.isAtivo());

        Clube atualizado = clubeRepository.save(clube);

        return ClubeDTO.fromEntity(atualizado);
    }

    public void inativar(Long id) {

        Clube clube = clubeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Clube não encontrado."));

        clube.setAtivo(false);
        clubeRepository.save(clube);
    }

    public ClubeDTO buscar(Long id) {

        Clube clube = clubeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Clube não encontrado."));

        return ClubeDTO.fromEntity(clube);
    }

    public Page<ClubeDTO> listar(String nome, String estado, Boolean ativo, Pageable pageable) {
        Page<Clube> clubes = clubeRepository.findByFiltros(nome, estado, ativo, pageable);

        return clubes.map(ClubeDTO::fromEntity);
    }

    public RetroDTO getRetro(Long clubeId) {
  
        Clube clube = clubeRepository.findById(clubeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não encontrado"));

        List<Partida> partidas = partidaRepository.findByMandanteIdOrVisitanteId(clubeId, clubeId);

        RetroDTO retro = new RetroDTO(clube.getNome(), 0, 0, 0, 0, 0);

        for (Partida partida : partidas) {
            if (partida.getMandante().getId().equals(clubeId)) {
                atualizarRetroMandante(retro, partida);
            } else {
                atualizarRetroVisitante(retro, partida);
            }
        }

        return retro;
    }

    private void atualizarRetroMandante(RetroDTO retro, Partida partida) {
        retro.setGolsFeitos(retro.getGolsFeitos() + partida.getGolsMandante());
        retro.setGolsSofridos(retro.getGolsSofridos() + partida.getGolsVisitante());
        atualizarResultado(retro, partida.getGolsMandante(), partida.getGolsVisitante());
    }

    private void atualizarRetroVisitante(RetroDTO retro, Partida partida) {
        retro.setGolsFeitos(retro.getGolsFeitos() + partida.getGolsVisitante());
        retro.setGolsSofridos(retro.getGolsSofridos() + partida.getGolsMandante());
        atualizarResultado(retro, partida.getGolsVisitante(), partida.getGolsMandante());
    }

    private void atualizarResultado(RetroDTO retro, int golsClube, int golsAdversario) {
        if (golsClube > golsAdversario) {
            retro.setVitorias(retro.getVitorias() + 1);
        } else if (golsClube < golsAdversario) {
            retro.setDerrotas(retro.getDerrotas() + 1);
        } else {
            retro.setEmpates(retro.getEmpates() + 1);
        }
    }
}