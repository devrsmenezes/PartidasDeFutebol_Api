package com.expoo.partidasdefutebol_api.service;

import com.expoo.partidasdefutebol_api.dto.PartidaDTO;
import com.expoo.partidasdefutebol_api.dto.RetroDTO;
import com.expoo.partidasdefutebol_api.model.Clube;
import com.expoo.partidasdefutebol_api.model.Partida;
import com.expoo.partidasdefutebol_api.repository.ClubeRepository;
import com.expoo.partidasdefutebol_api.repository.PartidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.*;
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
    public Partida cadastrar(PartidaDTO dto) {
        Partida partida = converterParaPartida(dto);
        validar(partida, null);
        return partidaRepository.save(partida);
    }

    @Transactional
    public Partida atualizar(Long id, PartidaDTO dto) {
        Partida partida = buscarPartidaPorId(id);
        atualizarPartida(partida, dto);
        validar(partida, id);
        return partidaRepository.save(partida);
    }

    @Transactional
    public void remover(Long id) {
        Partida partida = buscarPartidaPorId(id);
        partidaRepository.delete(partida);
    }

    @Transactional(readOnly = true)
    public Partida buscar(Long id) {
        return buscarPartidaPorId(id);
    }

    @Transactional(readOnly = true)
    public Page<Partida> listar(Long clubeId, String estadio, Boolean goleadas, Pageable pageable) {
        Page<Partida> partidas = filtroBasico(clubeId, estadio, pageable);

        if (Boolean.TRUE.equals(goleadas)) {
            List<Partida> filtradas = partidas.stream()
                .filter(p -> Math.abs(p.getGolsMandante() - p.getGolsVisitante()) >= 3)
                .collect(Collectors.toList());
            return new PageImpl<>(filtradas, pageable, filtradas.size());
        }

        return partidas;
    }

    private Page<Partida> filtroBasico(Long clubeId, String estadio, Pageable pageable) {
        if (clubeId != null && estadio != null && !estadio.isEmpty()) {
            return partidaRepository.findByMandanteIdOrVisitanteIdAndEstadioContainingIgnoreCase(clubeId, clubeId, estadio, pageable);
        } else if (clubeId != null) {
            return partidaRepository.findByMandanteIdOrVisitanteId(clubeId, clubeId, pageable);
        } else if (estadio != null && !estadio.isEmpty()) {
            return partidaRepository.findByEstadioContainingIgnoreCase(estadio, pageable);
        } else {
            return partidaRepository.findAll(pageable);
        }
    }

    private Partida converterParaPartida(PartidaDTO dto) {
        Clube mandante = buscarClubePorId(dto.getMandanteId(), "Clube mandante não encontrado");
        Clube visitante = buscarClubePorId(dto.getVisitanteId(), "Clube visitante não encontrado");

        String[] gols = dto.getResultado().split("-");
        if (gols.length != 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Resultado inválido. Formato esperado: 'X-Y'");
        }

        int golsMandante, golsVisitante;
        try {
            golsMandante = Integer.parseInt(gols[0]);
            golsVisitante = Integer.parseInt(gols[1]);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Resultado deve conter números válidos");
        }

        return new Partida(mandante, visitante, golsMandante, golsVisitante, dto.getEstadio(), dto.getDataHora());
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
        String[] gols = partida.getResultado().split("-");
        if (gols.length != 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de resultado inválido. Use 'X-Y'");
        }

        try {
            int g1 = Integer.parseInt(gols[0]);
            int g2 = Integer.parseInt(gols[1]);
            if (g1 < 0 || g2 < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Número de gols não pode ser negativo");
            }
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Gols devem ser números inteiros válidos");
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
        List<Partida> proximas = partidaRepository.findByMandanteOrVisitanteAndDataHoraBetween(
                partida.getMandante(), partida.getVisitante(), inicio, fim);

        if (id != null) {
            proximas = proximas.stream().filter(p -> !p.getId().equals(id)).collect(Collectors.toList());
        }

        if (!proximas.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Um dos clubes já possui partida próxima a essa data e hora");
        }
    }

    private void validarDisponibilidadeEstadio(Partida partida, Long id) {
        boolean conflito = partidaRepository.findAll().stream()
                .anyMatch(p -> p.getEstadio().equals(partida.getEstadio()) &&
                               p.getDataHora().equals(partida.getDataHora()) &&
                               (id == null || !p.getId().equals(id)));

        if (conflito) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Estádio já ocupado neste horário");
        }
    }

    private Partida buscarPartidaPorId(Long id) {
        return partidaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partida não encontrada"));
    }

    private Clube buscarClubePorId(Long id, String erro) {
        return clubeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, erro));
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getConfrontoDireto(Long clube1Id, Long clube2Id) {
        Clube clube1 = buscarClubePorId(clube1Id, "Clube 1 não encontrado");
        Clube clube2 = buscarClubePorId(clube2Id, "Clube 2 não encontrado");

        List<Partida> partidas = partidaRepository.findConfrontosDiretos(clube1Id, clube2Id);

        Map<String, RetroDTO> retroMap = new HashMap<>();
        retroMap.put(clube1.getNome(), new RetroDTO(clube1.getNome(), 0, 0, 0, 0, 0));
        retroMap.put(clube2.getNome(), new RetroDTO(clube2.getNome(), 0, 0, 0, 0, 0));

        for (Partida partida : partidas) {
            atualizarRetro(partida, clube1, clube2, retroMap);
        }

        List<RetroDTO> retro = retroMap.values().stream()
                .sorted(Comparator.comparing(RetroDTO::getNome))
                .collect(Collectors.toList());

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("partidas", partidas);
        resultado.put("retro", retro);
        return resultado;
    }

    private void atualizarRetro(Partida p, Clube c1, Clube c2, Map<String, RetroDTO> map) {
        if (p.getMandante().getId().equals(c1.getId())) {
            atualizarRetroMandante(map.get(c1.getNome()), map.get(c2.getNome()), p.getGolsMandante(), p.getGolsVisitante());
        } else if (p.getMandante().getId().equals(c2.getId())) {
            atualizarRetroMandante(map.get(c2.getNome()), map.get(c1.getNome()), p.getGolsMandante(), p.getGolsVisitante());
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

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getRanking(String criterio, Boolean goleadas, String tipo) {
        List<Partida> partidas = partidaRepository.findAll();

        if (Boolean.TRUE.equals(goleadas)) {
            partidas = partidas.stream()
                    .filter(p -> Math.abs(p.getGolsMandante() - p.getGolsVisitante()) >= 3)
                    .collect(Collectors.toList());
        }

        Map<Clube, RetroDTO> ranking = new HashMap<>();

        for (Partida p : partidas) {
            if (tipo == null || tipo.equalsIgnoreCase("mandante")) {
                atualizarRanking(ranking, p.getMandante(), p.getGolsMandante(), p.getGolsVisitante());
            }
            if (tipo == null || tipo.equalsIgnoreCase("visitante")) {
                atualizarRanking(ranking, p.getVisitante(), p.getGolsVisitante(), p.getGolsMandante());
            }
        }

        return ranking.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("clube", entry.getKey().getNome());
                    map.put("jogos", entry.getValue().getTotalJogos());
                    map.put("vitorias", entry.getValue().getVitorias());
                    map.put("gols", entry.getValue().getGolsFeitos());
                    map.put("pontos", entry.getValue().getPontos());
                    return map;
                })
                .filter(m -> switch (criterio.toLowerCase()) {
                    case "jogos", "vitorias", "gols", "pontos" -> (int) m.get(criterio.toLowerCase()) > 0;
                    default -> false;
                })
                .sorted(Comparator.comparingInt(m -> -((int) m.get(criterio.toLowerCase()))))
                .collect(Collectors.toList());
    }

    private void atualizarRanking(Map<Clube, RetroDTO> ranking, Clube clube, int golsFeitos, int golsSofridos) {
        RetroDTO retro = ranking.getOrDefault(clube, new RetroDTO(clube.getNome(), 0, 0, 0, 0, 0));
        retro.setGolsFeitos(retro.getGolsFeitos() + golsFeitos);
        retro.setGolsSofridos(retro.getGolsSofridos() + golsSofridos);

        if (golsFeitos > golsSofridos) {
            retro.setVitorias(retro.getVitorias() + 1);
        } else if (golsFeitos < golsSofridos) {
            retro.setDerrotas(retro.getDerrotas() + 1);
        } else {
            retro.setEmpates(retro.getEmpates() + 1);
        }

        ranking.put(clube, retro);
    }
}