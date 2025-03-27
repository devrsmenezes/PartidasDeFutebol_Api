package com.expoo.partidasdefutebol_api.service;

import com.expoo.partidasdefutebol_api.dto.PartidaDTO;
import com.expoo.partidasdefutebol_api.dto.RetroDTO;
import com.expoo.partidasdefutebol_api.model.Clube;
import com.expoo.partidasdefutebol_api.model.Partida;
import com.expoo.partidasdefutebol_api.repository.ClubeRepository;
import com.expoo.partidasdefutebol_api.repository.PartidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PartidaService {
    private final PartidaRepository partidaRepository;
    private final ClubeRepository clubeRepository;

    @Autowired
    public PartidaService(PartidaRepository partidaRepository, ClubeRepository clubeRepository) {
        this.partidaRepository = partidaRepository;
        this.clubeRepository = clubeRepository;
    }

    @Transactional
    public Partida cadastrar(PartidaDTO partidaDTO) {
        Partida partida = converterParaPartida(partidaDTO);
        validar(partida, null);
        return partidaRepository.save(partida);
    }

    @Transactional
    public Partida atualizar(Long id, PartidaDTO partidaDTO) {
        Partida partida = buscarPartidaPorId(id);
        atualizarPartida(partida, partidaDTO);
        validar(partida, id);
        return partidaRepository.save(partida);
    }

    @Transactional
    public void remover(Long id) {
        if (!partidaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Partida não encontrada");
        }
        partidaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Partida buscar(Long id) {
        return buscarPartidaPorId(id);
    }

    @Transactional(readOnly = true)
    public Page<Partida> listar(Long clubeId, String estadio, Pageable pageable) {
        Page<Partida> partidas;
        if (clubeId != null && estadio != null && !estadio.isEmpty()) {
            partidas = partidaRepository.findByMandanteIdOrVisitanteIdAndEstadioContainingIgnoreCase(clubeId, clubeId, estadio, pageable);
        } else if (clubeId != null) {
            partidas = partidaRepository.findByMandanteIdOrVisitanteId(clubeId, clubeId, pageable);
        } else if (estadio != null && !estadio.isEmpty()) {
            partidas = partidaRepository.findByEstadioContainingIgnoreCase(estadio, pageable);
        } else {
            partidas = partidaRepository.findAll(pageable);
        }
        
        if (partidas.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Não há lista de partidas");
        }
        
        return partidas;
    }
    
    private Partida converterParaPartida(PartidaDTO dto) {
        Clube mandante = buscarClubePorId(dto.getMandanteId(), "Clube mandante não encontrado");
        Clube visitante = buscarClubePorId(dto.getVisitanteId(), "Clube visitante não encontrado");
        Partida partida = new Partida(mandante, visitante, dto.getResultado(), dto.getEstadio(), dto.getDataHora());
        String[] gols = dto.getResultado().split("-");
        partida.setGolsMandante(Integer.parseInt(gols[0]));
        partida.setGolsVisitante(Integer.parseInt(gols[1]));
        
        return partida;
    }
    
    private void atualizarPartida(Partida partida, PartidaDTO dto) {
        String[] gols = dto.getResultado().split("-");
        partida.setGolsMandante(Integer.parseInt(gols[0]));
        partida.setGolsVisitante(Integer.parseInt(gols[1]));
        partida.setResultado(dto.getResultado());
        partida.setEstadio(dto.getEstadio());
        partida.setDataHora(dto.getDataHora());
    }
    
    private void validar(Partida partida, Long id) {
        validarClubesDiferentes(partida);
        validarResultado(partida);
        validarDataHora(partida);
        validarClubesAtivos(partida);
        validarConflitosHorario(partida, id);
        validarDisponibilidadeEstadio(partida, id);
    }

    private void validarClubesDiferentes(Partida partida) {
        if (partida.getMandante().equals(partida.getVisitante())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Os clubes não podem ser iguais");
        }
    }

    private void validarResultado(Partida partida) {
        if (partida.getResultado().contains("-")) {
            String[] gols = partida.getResultado().split("-");
            if (Integer.parseInt(gols[0]) < 0 || Integer.parseInt(gols[1]) < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Número de gols não pode ser negativo");
            }
        }
    }

    private void validarDataHora(Partida partida) {
        if (partida.getDataHora().isAfter(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data e hora não podem estar no futuro");
        }
        if (partida.getDataHora().isBefore(partida.getMandante().getDataCriacao().atStartOfDay()) ||
                partida.getDataHora().isBefore(partida.getVisitante().getDataCriacao().atStartOfDay())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Data da partida é anterior à criação de um dos clubes");
        }
    }

    private void validarClubesAtivos(Partida partida) {
        if (!partida.getMandante().isAtivo() || !partida.getVisitante().isAtivo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Um dos clubes está inativo");
        }
    }

    private void validarConflitosHorario(Partida partida, Long id) {
        LocalDateTime inicio = partida.getDataHora().minusHours(48);
        LocalDateTime fim = partida.getDataHora().plusHours(48);
        List<Partida> partidasProximas = partidaRepository.findByMandanteOrVisitanteAndDataHoraBetween(
                partida.getMandante(), partida.getVisitante(), inicio, fim);
        if (id != null) {
            partidasProximas = partidasProximas.stream()
                .filter(p -> !p.getId().equals(id))
                .collect(Collectors.toList());
        }
        
        if (!partidasProximas.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Um dos clubes já possui partida marcada em horário próximo");
        }
    }

    private void validarDisponibilidadeEstadio(Partida partida, Long id) {
        if (id != null) {
            boolean existeConflito = partidaRepository.findAll().stream()
                .anyMatch(p -> p.getEstadio().equals(partida.getEstadio()) 
                        && p.getDataHora().equals(partida.getDataHora())
                        && !p.getId().equals(id));
            
            if (existeConflito) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Estádio já possui jogo marcado neste horário");
            }
        } else {
            if (partidaRepository.existsByEstadioAndDataHora(partida.getEstadio(), partida.getDataHora())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Estádio já possui jogo marcado neste horário");
            }
        }
    }

    private Partida buscarPartidaPorId(Long id) {
        return partidaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partida não encontrada"));
    }

    private Clube buscarClubePorId(Long id, String mensagemErro) {
        return clubeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, mensagemErro));
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getConfrontoDireto(Long clube1Id, Long clube2Id) {
        Clube clube1 = buscarClubePorId(clube1Id, "Clube 1 não encontrado");
        Clube clube2 = buscarClubePorId(clube2Id, "Clube 2 não encontrado");
    
        List<Partida> partidas = partidaRepository.findConfrontosDiretos(clube1Id, clube2Id);
    
        RetroDTO retro1 = new RetroDTO(clube1.getNome(), 0, 0, 0, 0, 0);
        RetroDTO retro2 = new RetroDTO(clube2.getNome(), 0, 0, 0, 0, 0);
        
        Map<String, RetroDTO> retroMap = new HashMap<>();
        retroMap.put(clube1.getNome(), retro1);
        retroMap.put(clube2.getNome(), retro2);
    
        for (Partida partida : partidas) {
            atualizarRetro(partida, clube1, clube2, retroMap);
        }
    
        List<RetroDTO> retroDTO = retroMap.values().stream()
            .sorted((a, b) -> b.getNome().compareTo(a.getNome()))
            .collect(Collectors.toList());
    
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("partidas", partidas);
        resultado.put("retro", retroDTO);
    
        return resultado;
    }
    
    private void atualizarRetro(Partida partida, Clube clube1, Clube clube2, Map<String, RetroDTO> retroMap) {
        RetroDTO retro1 = retroMap.get(clube1.getNome());
        RetroDTO retro2 = retroMap.get(clube2.getNome());
    
        if (partida.getMandante().getId().equals(clube1.getId())) {
            atualizarRetroMandante(retro1, retro2, partida.getGolsMandante(), partida.getGolsVisitante());
        } else if (partida.getMandante().getId().equals(clube2.getId())) {
            atualizarRetroMandante(retro2, retro1, partida.getGolsMandante(), partida.getGolsVisitante());
        }
    }
    
    private void atualizarRetroMandante(RetroDTO mandante, RetroDTO visitante, int golsMandante, int golsVisitante) {
        mandante.setGolsFeitos(mandante.getGolsFeitos() + golsMandante);
        mandante.setGolsSofridos(mandante.getGolsSofridos() + golsVisitante);
        visitante.setGolsFeitos(visitante.getGolsFeitos() + golsVisitante);
        visitante.setGolsSofridos(visitante.getGolsSofridos() + golsMandante);
    
        if (golsMandante > golsVisitante) {
            mandante.setVitorias(mandante.getVitorias() + 1);
            visitante.setDerrotas(visitante.getDerrotas() + 1);
        } else if (golsMandante < golsVisitante) {
            mandante.setDerrotas(mandante.getDerrotas() + 1);
            visitante.setVitorias(visitante.getVitorias() + 1);
        } else {
            mandante.setEmpates(mandante.getEmpates() + 1);
            visitante.setEmpates(visitante.getEmpates() + 1);
        }
    } 
}

