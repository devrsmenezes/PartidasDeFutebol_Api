package com.expoo.partidasdefutebol_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.expoo.partidasdefutebol_api.dto.RetroDTO;
import com.expoo.partidasdefutebol_api.service.RetroService;

@RestController
@RequestMapping("/api/clubes")
public class RetroController {

    @Autowired
    private RetroService retroService;

    @GetMapping("/{clubeId}/retro")
    public ResponseEntity<RetroDTO> getRetro(@PathVariable Long clubeId) {
        try {
            RetroDTO retro = retroService.getRetro(clubeId);
            return ResponseEntity.ok(retro);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }
}
