package com.expoo.partidasdefutebol_api.service;

import com.expoo.partidasdefutebol_api.dto.ClubeDTO;
import com.expoo.partidasdefutebol_api.dto.RetroDTO;
import com.expoo.partidasdefutebol_api.enums.TipoCampo;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClubeService {

    private final ClubeRepository clubeRepository;
    private final PartidaRepository partidaRepository;

    @Autowired
    public ClubeService(ClubeRepository clubeRepository, PartidaRepository partidaRepository) {
        this.clubeRepository = clubeRepository;
        this.partidaRepository = partidaRepository;
    }

    @Transactional
    public void criar(ClubeDTO clubeDTO) {
        Clube novoClube = clubeDTO.toEntity();
        validarClube(novoClube);
        clubeRepository.save(novoClube);
    }

    @Transactional
    public ClubeDTO atualizar(Long id, ClubeDTO clubeDTO) {
        Clube clube = buscarClubePorId(id);
        atualizarClubeComDTO(clube, clubeDTO);
        validarClube(clube);
        Clube atualizado = clubeRepository.save(clube);
        return ClubeDTO.fromEntity(atualizado);
    }

    @Transactional
    public void inativar(Long id) {
        Clube clube = buscarClubePorId(id);
        clube.setAtivo(false);
        clubeRepository.save(clube);
    }

    @Transactional(readOnly = true)
    public ClubeDTO buscar(Long id) {
        return ClubeDTO.fromEntity(buscarClubePorId(id));
    }

    @Transactional(readOnly = true)
    public Page<ClubeDTO> listar(String nome, String estado, Boolean ativo, Pageable pageable) {
        return clubeRepository.findByFiltros(nome, estado, ativo, pageable)
                .map(ClubeDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public RetroDTO getRetro(Long clubeId, TipoCampo tipoCampo) {
        Clube clube = buscarClubePorId(clubeId);
        List<Partida> partidas = buscarPartidasPorTipo(clubeId, tipoCampo);
        return calcularRetro(clube, partidas);
    }

    @Transactional(readOnly = true)
    public RetroDTO getRetroGoleadas(Long clubeId, TipoCampo tipoCampo) {
        Clube clube = buscarClubePorId(clubeId);
        List<Partida> partidas = buscarPartidasPorTipo(clubeId, tipoCampo);

        List<Partida> goleadas = partidas.stream()
                .filter(p -> {
                    int golsClube = p.getMandante().getId().equals(clubeId) ? p.getGolsMandante() : p.getGolsVisitante();
                    int golsAdversario = p.getMandante().getId().equals(clubeId) ? p.getGolsVisitante() : p.getGolsMandante();
                    return Math.abs(golsClube - golsAdversario) >= 3;
                })
                .collect(Collectors.toList());

        return calcularRetro(clube, goleadas);
    }

    @Transactional(readOnly = true)
    public List<RetroDTO> getRetroAdversarios(Long clubeId, TipoCampo tipoCampo) {
        buscarClubePorId(clubeId);
        List<Partida> partidas = buscarPartidasPorTipo(clubeId, tipoCampo);
        if (partidas.isEmpty()) return new ArrayList<>();
        return calcularRetroAdversarios(clubeId, partidas);
    }

    @Transactional(readOnly = true)
    public List<RetroDTO> compararClubes(List<Long> clubeIds) {
        List<Clube> clubes = clubeIds.stream()
                .map(this::buscarClubePorId)
                .collect(Collectors.toList());

        List<Partida> todasPartidas = buscarPartidasDosClubes(clubeIds);

        return clubes.stream()
                .map(clube -> calcularRetroComparado(clube, todasPartidas, clubeIds))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RetroDTO> getRetroParaCadaAdversario(Long clubeId) {
        Clube clube = buscarClubePorId(clubeId);
        List<Partida> partidas = buscarPartidasDoClube(clubeId);

        Set<Long> idTimesEnfrentados = partidas.stream()
                .map(partida -> getAdversarioId(partida, clubeId))
                .collect(Collectors.toSet());

        return idTimesEnfrentados.stream()
                .map(adversarioId -> calcularRetroContraAdversario(clube, adversarioId, partidas))
                .collect(Collectors.toList());
    }

    private void atualizarClubeComDTO(Clube clube, ClubeDTO clubeDTO) {
        clube.setNome(clubeDTO.getNome());
        clube.setEstado(clubeDTO.getEstado());
        clube.setAtivo(clubeDTO.getAtivo() != null ? clubeDTO.getAtivo() : false);
        clube.setDataCriacao(clubeDTO.getDataCriacao()); 
    }

    private void validarClube(Clube clube) {
        if (clube.getNome() == null || clube.getNome().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome do clube é obrigatório");
        }
        if (clube.getEstado() == null || clube.getEstado().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado do clube é obrigatório");
        }
        if (clube.getDataCriacao() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data de criação do clube é obrigatória");
        }
    }

    private Clube buscarClubePorId(Long id) {
        return clubeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não encontrado"));
    }

    private List<Partida> buscarPartidasPorTipo(Long clubeId, TipoCampo tipoCampo) {
        return switch (tipoCampo != null ? tipoCampo : TipoCampo.TODOS) {
            case MANDANTE -> partidaRepository.findByMandanteId(clubeId);
            case VISITANTE -> partidaRepository.findByVisitanteId(clubeId);
            case TODOS -> buscarPartidasDoClube(clubeId);
        };
    }

    private List<Partida> buscarPartidasDoClube(Long clubeId) {
        return partidaRepository.findByMandanteIdOrVisitanteId(clubeId, clubeId);
    }

    private List<Partida> buscarPartidasDosClubes(List<Long> clubeIds) {
        return partidaRepository.findByMandanteIdInOrVisitanteIdIn(clubeIds, clubeIds);
    }

    private RetroDTO calcularRetro(Clube clube, List<Partida> partidas) {
        RetroDTO retro = new RetroDTO(clube.getNome(), 0, 0, 0, 0, 0);
        partidas.forEach(partida -> atualizarRetro(retro, partida, clube.getId()));
        return retro;
    }

    private List<RetroDTO> calcularRetroAdversarios(Long clubeId, List<Partida> partidas) {
        Map<Long, RetroDTO> retroPorAdversario = new HashMap<>();

        for (Partida partida : partidas) {
            Long adversarioId = getAdversarioId(partida, clubeId);
            String nomeAdversario = getNomeAdversario(partida, clubeId);

            RetroDTO retro = retroPorAdversario.computeIfAbsent(adversarioId,
                    k -> new RetroDTO(nomeAdversario, 0, 0, 0, 0, 0));

            atualizarRetro(retro, partida, clubeId);
        }

        return new ArrayList<>(retroPorAdversario.values());
    }

    private RetroDTO calcularRetroComparado(Clube clube, List<Partida> partidas, List<Long> clubeIds) {
        RetroDTO retro = new RetroDTO(clube.getNome(), 0, 0, 0, 0, 0);
        partidas.stream()
                .filter(partida -> partida.getMandante().getId().equals(clube.getId()) ||
                                   partida.getVisitante().getId().equals(clube.getId()))
                .filter(partida -> clubeIds.contains(partida.getMandante().getId()) &&
                                   clubeIds.contains(partida.getVisitante().getId()))
                .forEach(partida -> atualizarRetro(retro, partida, clube.getId()));
        return retro;
    }

    private RetroDTO calcularRetroContraAdversario(Clube clube, Long adversarioId, List<Partida> partidas) {
        Clube adversario = buscarClubePorId(adversarioId);
        RetroDTO retro = new RetroDTO(adversario.getNome(), 0, 0, 0, 0, 0);

        partidas.stream()
                .filter(partida -> partida.getMandante().getId().equals(adversarioId) ||
                                   partida.getVisitante().getId().equals(adversarioId))
                .forEach(partida -> atualizarRetro(retro, partida, clube.getId()));

        return retro;
    }

    private void atualizarRetro(RetroDTO retro, Partida partida, Long clubeId) {
        boolean isMandante = partida.getMandante().getId().equals(clubeId);
        int golsClube = isMandante ? partida.getGolsMandante() : partida.getGolsVisitante();
        int golsAdversario = isMandante ? partida.getGolsVisitante() : partida.getGolsMandante();

        retro.setGolsFeitos(retro.getGolsFeitos() + golsClube);
        retro.setGolsSofridos(retro.getGolsSofridos() + golsAdversario);
        atualizarResultado(retro, golsClube, golsAdversario);
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

    private Long getAdversarioId(Partida partida, Long clubeId) {
        return partida.getMandante().getId().equals(clubeId)
                ? partida.getVisitante().getId()
                : partida.getMandante().getId();
    }

    private String getNomeAdversario(Partida partida, Long clubeId) {
        return partida.getMandante().getId().equals(clubeId)
                ? partida.getVisitante().getNome()
                : partida.getMandante().getNome();
    }
}