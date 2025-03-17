package com.expoo.partidasdefutebol_api.service;

import com.expoo.partidasdefutebol_api.dto.ClubeDTO;
import com.expoo.partidasdefutebol_api.dto.RetroDTO;
import com.expoo.partidasdefutebol_api.model.Clube;
import com.expoo.partidasdefutebol_api.model.Partida;
import com.expoo.partidasdefutebol_api.repository.ClubeRepository;
import com.expoo.partidasdefutebol_api.repository.PartidaRepository;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ClubeService {

    private final ClubeRepository clubeRepository;
    private final PartidaRepository partidaRepository;

    @Autowired
    public ClubeService(ClubeRepository clubeRepository, PartidaRepository partidaRepository) {
        this.clubeRepository = clubeRepository;
        this.partidaRepository = partidaRepository;
    }

    public void criar(ClubeDTO clubeDTO) {
        Clube novoClube = clubeDTO.toEntity();
        clubeRepository.save(novoClube);
    }

    public ClubeDTO atualizar(Long id, ClubeDTO clubeDTO) {
        Clube clube = buscarClubePorId(id);
        atualizarClubeComDTO(clube, clubeDTO);
        Clube atualizado = clubeRepository.save(clube);
        return ClubeDTO.fromEntity(atualizado);
    }

    public void inativar(Long id) {
        Clube clube = buscarClubePorId(id);
        clube.setAtivo(false);
        clubeRepository.save(clube);
    }

    public ClubeDTO buscar(Long id) {
        return ClubeDTO.fromEntity(buscarClubePorId(id));
    }

    public Page<ClubeDTO> listar(String nome, String estado, Boolean ativo, Pageable pageable) {
        return clubeRepository.findByFiltros(nome, estado, ativo, pageable).map(ClubeDTO::fromEntity);
    }

    public RetroDTO getRetro(Long clubeId) {
        Clube clube = buscarClubePorId(clubeId);
        List<Partida> partidas = buscarPartidasDoClube(clubeId);
        return calcularRetro(clube, partidas);
    }

    public List<RetroDTO> getRetroAdversarios(Long clubeId) {
        buscarClubePorId(clubeId); // Verifica se o clube existe
        List<Partida> partidas = buscarPartidasDoClube(clubeId);
        return calcularRetroAdversarios(clubeId, partidas);
    }

    private Clube buscarClubePorId(Long id) {
        return clubeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clube não encontrado"));
    }

    private void atualizarClubeComDTO(Clube clube, ClubeDTO clubeDTO) {
        clube.setNome(clubeDTO.getNome());
        clube.setEstado(clubeDTO.getEstado());
        clube.setAtivo(clubeDTO.isAtivo());
    }

    private List<Partida> buscarPartidasDoClube(Long clubeId) {
        return partidaRepository.findByMandanteIdOrVisitanteId(clubeId, clubeId);
    }

    private RetroDTO calcularRetro(Clube clube, List<Partida> partidas) {
        RetroDTO retro = new RetroDTO(clube.getNome(), 0, 0, 0, 0, 0);
        partidas.forEach(partida -> atualizarRetro(retro, partida, clube.getId()));
        return retro;
    }

    private List<RetroDTO> calcularRetroAdversarios(Long clubeId, List<Partida> partidas) {
        return partidas.stream()
                .collect(Collectors.groupingBy(partida -> getAdversarioId(partida, clubeId)))
                .entrySet().stream()
                .map(entry -> criarRetroDTO(entry.getKey(), entry.getValue(), clubeId))
                .collect(Collectors.toList());
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

    private RetroDTO criarRetroDTO(Long adversarioId, List<Partida> partidas, Long clubeId) {
        String nomeAdversario = partidas.get(0).getMandante().getId().equals(clubeId) 
            ? partidas.get(0).getVisitante().getNome() 
            : partidas.get(0).getMandante().getNome();

        RetroDTO retro = new RetroDTO(nomeAdversario, 0, 0, 0, 0, 0);
        partidas.forEach(partida -> atualizarRetro(retro, partida, clubeId));
        return retro;
    }
}