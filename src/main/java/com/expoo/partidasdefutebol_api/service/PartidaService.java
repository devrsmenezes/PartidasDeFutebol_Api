package com.expoo.partidasdefutebol_api.service;

import com.expoo.partidasdefutebol_api.dto.PartidaDTO;
import com.expoo.partidasdefutebol_api.model.Partida;
import com.expoo.partidasdefutebol_api.repository.ClubeRepository;
import com.expoo.partidasdefutebol_api.repository.PartidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PartidaService {
    @Autowired
    private PartidaRepository partidaRepository;

    @Autowired
    private ClubeRepository clubeRepository;

    public Partida cadastrarPartida(PartidaDTO partidaDTO) {
        Partida partida = converterParaPartida(partidaDTO);
        validarPartida(partida);
        return partidaRepository.save(partida);
    }

    private Partida converterParaPartida(PartidaDTO dto) {
        Partida partida = new Partida();
        partida.setClubeMandante(clubeRepository.findById(dto.getClubeMandanteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Clube mandante não encontrado")));
        partida.setClubeVisitante(clubeRepository.findById(dto.getClubeVisitanteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Clube visitante não encontrado")));
        partida.setResultado(dto.getResultado());
        partida.setEstadio(dto.getEstadio());
        partida.setDataHora(dto.getDataHora());
        return partida;
    }
