package com.expoo.partidasdefutebol_api.service;

import com.expoo.partidasdefutebol_api.dto.PartidaDTO;
import com.expoo.partidasdefutebol_api.model.Clube;
import com.expoo.partidasdefutebol_api.model.Partida;
import com.expoo.partidasdefutebol_api.repository.ClubeRepository;
import com.expoo.partidasdefutebol_api.repository.PartidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PartidaService {
    @Autowired
    private PartidaRepository partidaRepository;

    @Autowired
    private ClubeRepository clubeRepository;

    public Partida cadastrar(PartidaDTO partidaDTO) {
        Partida partida = converterParaPartida(partidaDTO);
        validar(partida);
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

    public Partida atualizar(Long id, PartidaDTO partidaDTO) {
        Partida partida = partidaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partida não encontrada"));

        atualizar(partida, partidaDTO);
        validar(partida);
        return partidaRepository.save(partida);
    }

    private void atualizar(Partida partida, PartidaDTO dto) {
        Clube clubeMandante = clubeRepository.findById(dto.getClubeMandanteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Clube mandante não encontrado"));
        Clube clubeVisitante = clubeRepository.findById(dto.getClubeVisitanteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Clube visitante não encontrado"));

        partida.setClubeMandante(clubeMandante);
        partida.setClubeVisitante(clubeVisitante);
        partida.setResultado(dto.getResultado());
        partida.setEstadio(dto.getEstadio());
        partida.setDataHora(dto.getDataHora());
    }

    private void validar(Partida partida) {
        if (partida.getClubeMandante().equals(partida.getClubeVisitante())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Os clubes não podem ser iguais");
        }

        if (partida.getResultado().contains("-")) {
            String[] gols = partida.getResultado().split("-");
            if (Integer.parseInt(gols[0]) < 0 || Integer.parseInt(gols[1]) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Número de gols não pode ser negativo");
            }
        }

        if (partida.getDataHora().isAfter(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data e hora não podem estar no futuro");
        }

        if (partida.getDataHora().isBefore(partida.getClubeMandante().getDataCriacao().atStartOfDay()) ||
                partida.getDataHora().isBefore(partida.getClubeVisitante().getDataCriacao().atStartOfDay())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Data da partida é anterior à criação de um dos clubes");
        }

        if (!partida.getClubeMandante().isAtivo() || !partida.getClubeVisitante().isAtivo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Um dos clubes está inativo");
        }

        LocalDateTime inicio = partida.getDataHora().minusHours(48);
        LocalDateTime fim = partida.getDataHora().plusHours(48);
        List<Partida> partidasProximas = partidaRepository.findByClubeMandanteOrClubeVisitanteAndDataHoraBetween(
                partida.getClubeMandante(), partida.getClubeVisitante(), inicio, fim);
        if (!partidasProximas.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Um dos clubes já possui partida marcada em horário próximo");
        }

        if (partidaRepository.existsByEstadioAndDataHora(partida.getEstadio(), partida.getDataHora())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Estádio já possui jogo marcado neste horário");
        }
    }

    public void remover(Long id) {
        if (!partidaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Partida não encontrada");
        }
        partidaRepository.deleteById(id);
    }

    public Partida buscar(Long id) {
        return partidaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partida não encontrada"));
    }

    public Page<Partida> listar(Long clubeId, String estadio, Pageable pageable) {
        if (clubeId != null && estadio != null && !estadio.isEmpty()) {
            return partidaRepository.findByClubeMandanteIdOrClubeVisitanteIdAndEstadioContainingIgnoreCase(clubeId, clubeId, estadio, pageable);
        } else if (clubeId != null) {
            return partidaRepository.findByClubeMandanteIdOrClubeVisitanteId(clubeId, clubeId, pageable);
        } else if (estadio != null && !estadio.isEmpty()) {
            return partidaRepository.findByEstadioContainingIgnoreCase(estadio, pageable);
        } else {
            return partidaRepository.findAll(pageable);
        }
    }
}