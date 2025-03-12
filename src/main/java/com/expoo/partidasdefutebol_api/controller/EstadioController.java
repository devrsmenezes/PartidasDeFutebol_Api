package com.expoo.partidasdefutebol_api.controller;

import com.expoo.partidasdefutebol_api.dto.EstadioDTO;
import com.expoo.partidasdefutebol_api.model.Estadio;
import com.expoo.partidasdefutebol_api.service.EstadioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/estadios")
public class EstadioController {

    @Autowired
    private EstadioService estadioService;

    @PostMapping
    public ResponseEntity<String> cadastrar(@RequestBody EstadioDTO estadioDTO) {
        try {
            Estadio novoEstadio = estadioService.cadastrar(estadioDTO);
            String mensagem = novoEstadio.getNome() + " foi cadastrado com sucesso.";
            return new ResponseEntity<>(mensagem, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> editar (@PathVariable Long id, @RequestBody EstadioDTO estadioDTO) {
        try {
            Estadio estadioAtualizado = estadioService.editar(id, estadioDTO);
            String mensagem = estadioAtualizado.getNome() + " Estádio atualizado com sucesso.";
            return new ResponseEntity<>(estadioAtualizado.getNome(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadioDTO> buscar(@PathVariable Long id) {
        try {
            EstadioDTO estadio = estadioService.buscar(id);
            return ResponseEntity.ok(estadio);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();}
    }

    @GetMapping
    public ResponseEntity<Page<EstadioDTO>> listar(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho,
            @RequestParam(defaultValue = "nome") String ordenarPor,
            @RequestParam(defaultValue = "asc") String direcao) {

        Sort.Direction sortDirecao = direcao.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(pagina, tamanho, Sort.by(sortDirecao, ordenarPor));

        Page<EstadioDTO> estadio = estadioService.listar(pageable);

        if (estadio.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(estadio);
    }
}