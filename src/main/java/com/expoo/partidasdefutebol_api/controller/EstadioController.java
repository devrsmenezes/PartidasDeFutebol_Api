package com.expoo.partidasdefutebol_api.controller;

import com.expoo.partidasdefutebol_api.dto.EstadioDTO;
import com.expoo.partidasdefutebol_api.model.Estadio;
import com.expoo.partidasdefutebol_api.service.EstadioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("estadio")
public class EstadioController {

    @Autowired
    private EstadioService estadioService;

    @PostMapping
    public ResponseEntity<String> cadastrar(@RequestBody EstadioDTO estadioDTO) {
        Estadio novoEstadio = estadioService.cadastrar(estadioDTO);
        String mensagem = novoEstadio.getNome() + " foi cadastrado com sucesso.";
        return new ResponseEntity<>(mensagem, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> editar (@PathVariable Long id, @RequestBody EstadioDTO estadioDTO) {
        Estadio estadioAtualizado = estadioService.editar(id, estadioDTO);
        String mensagem = estadioAtualizado.getNome() + " Estádio atualizado com sucesso.";
        return new ResponseEntity<>(mensagem, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadioDTO> buscar(@PathVariable Long id) {
        EstadioDTO estadio = estadioService.buscar(id);
        return ResponseEntity.ok(estadio);
    }

}