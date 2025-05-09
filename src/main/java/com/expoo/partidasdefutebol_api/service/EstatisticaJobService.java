package com.expoo.partidasdefutebol_api.service;

import com.expoo.partidasdefutebol_api.dto.EstatisticaDTO;
import com.expoo.partidasdefutebol_api.model.Clube;
import com.expoo.partidasdefutebol_api.repository.ClubeRepository;
import com.expoo.partidasdefutebol_api.repository.PartidaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class EstatisticaJobService {

    private static final Logger log = LoggerFactory.getLogger(EstatisticaJobService.class);

    private final ClubeRepository clubeRepository;
    private final PartidaRepository partidaRepository;

    public EstatisticaJobService(ClubeRepository clubeRepository, PartidaRepository partidaRepository) {
        this.clubeRepository = clubeRepository;
        this.partidaRepository = partidaRepository;
    }

    public void executarJobManual() {
        log.info("[JOB MANUAL] Iniciando cálculo de estatísticas dos clubes");

        List<Clube> clubes = clubeRepository.findAll();
        for (Clube clube : clubes) {
            Integer total = partidaRepository.somarGolsDoClube(clube.getId());
            int totalGols = (total != null) ? total : 0;
            log.info("Clube {} marcou {} gols", clube.getNome(), totalGols);
        }

        log.info("[JOB MANUAL] Estatísticas calculadas com sucesso");
    }

    public List<EstatisticaDTO> executarJobComRetorno() {
        List<EstatisticaDTO> resultado = new ArrayList<>();
        List<Clube> clubes = clubeRepository.findAll();

        for (Clube clube : clubes) {
            Integer total = partidaRepository.somarGolsDoClube(clube.getId());
            int totalGols = (total != null) ? total : 0;
            resultado.add(new EstatisticaDTO(clube.getNome(), totalGols));
        }

        return resultado;
    }

    @Scheduled(cron = "*/30 * * * * *")
    public void executarJobAgendado() {
        log.info("[JOB AGENDADO] Disparando cálculo de estatísticas...");
        executarJobManual();
    }
}