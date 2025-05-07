package com.expoo.partidasdefutebol_api.controller;

import com.expoo.partidasdefutebol_api.service.EstatisticaJobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final EstatisticaJobService jobService;

    public JobController(EstatisticaJobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/recalcular-estatisticas")
    public ResponseEntity<String> dispararJobDeEstatisticas() {
        jobService.executarJobManual();
        return ResponseEntity.ok("Job de estatísticas executado com sucesso!");
    }
}