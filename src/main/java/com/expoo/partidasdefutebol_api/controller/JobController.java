package com.expoo.partidasdefutebol_api.controller;

import com.expoo.partidasdefutebol_api.dto.EstatisticaDTO;
import com.expoo.partidasdefutebol_api.service.EstatisticaJobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
@Tag(name = "Job de Estatísticas", description = "Endpoints para disparar o job de cálculo de estatísticas dos clubes")
public class JobController {

    private final EstatisticaJobService jobService;

    public JobController(EstatisticaJobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/recalcular-estatisticas")
    @Operation(
        summary = "Executa o job de estatísticas manualmente",
        description = "Dispara o cálculo de estatísticas dos clubes com base nas partidas cadastradas e retorna a lista com os totais."
    )
    public ResponseEntity<List<EstatisticaDTO>> dispararJobDeEstatisticas() {
        List<EstatisticaDTO> estatisticas = jobService.executarJobComRetorno();
        return ResponseEntity.ok(estatisticas);
    }
}