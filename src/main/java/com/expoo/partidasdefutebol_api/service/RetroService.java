package com.expoo.partidasdefutebol_api.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.expoo.partidasdefutebol_api.dto.RetroDTO;
import com.expoo.partidasdefutebol_api.model.Clube;
import com.expoo.partidasdefutebol_api.model.Partida;
import com.expoo.partidasdefutebol_api.repository.ClubeRepository;
import com.expoo.partidasdefutebol_api.repository.PartidaRepository;

@Service
public class RetroService {

    @Autowired
    private ClubeRepository clubeRepository;

    @Autowired
    private PartidaRepository partidaRepository;

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
