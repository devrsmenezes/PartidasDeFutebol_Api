package com.expoo.partidasdefutebol_api.controllerTest;

import com.expoo.partidasdefutebol_api.controller.EstadioController;
import com.expoo.partidasdefutebol_api.dto.EstadioDTO;
import com.expoo.partidasdefutebol_api.service.EstadioService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstadioControllerTest {

    @Mock
    private EstadioService estadioService;

    @InjectMocks
    private EstadioController estadioController;

    private EstadioDTO dto;

    @BeforeEach
    void setup() {
        dto = new EstadioDTO();
    }

    @Test
    void deveCadastrarEstadioComSucesso() {
        when(estadioService.cadastrar(dto)).thenReturn(dto);

        EstadioDTO resultado = estadioController.cadastrar(dto);

        assertEquals(dto, resultado);
        verify(estadioService).cadastrar(dto);
    }

    @Test
    void deveListarEstadiosComOrdenacaoAscendente() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "nome"));
        Page<EstadioDTO> page = new PageImpl<>(List.of(dto));

        when(estadioService.listar(pageable)).thenReturn(page);

        Page<EstadioDTO> resultado = estadioController.listar(0, 10, "nome", "asc");

        assertEquals(1, resultado.getTotalElements());
        verify(estadioService).listar(pageable);
    }

    @Test
    void deveListarVariosEstadios() {
        List<EstadioDTO> lista = List.of(new EstadioDTO(), new EstadioDTO(), new EstadioDTO());
        Page<EstadioDTO> page = new PageImpl<>(lista);

        when(estadioService.listar(any())).thenReturn(page);

        Page<EstadioDTO> resultado = estadioController.listar(0, 10, "nome", "asc");

        assertEquals(3, resultado.getContent().size());
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverEstadios() {
        Page<EstadioDTO> pageVazia = new PageImpl<>(Collections.emptyList());

        when(estadioService.listar(any())).thenReturn(pageVazia);

        Page<EstadioDTO> resultado = estadioController.listar(0, 10, "nome", "asc");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveLancarExcecaoQuandoEstadioNaoEncontrado() {
        Long idInvalido = 999L;

        when(estadioService.buscar(idInvalido)).thenThrow(new EntityNotFoundException("Estádio não encontrado"));

        assertThrows(EntityNotFoundException.class, () -> estadioController.buscar(idInvalido));
    }

    @Test
    void deveTratarOrdenacaoComDirecaoUpperCase() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "nome"));
        Page<EstadioDTO> page = new PageImpl<>(List.of(dto));

        when(estadioService.listar(pageable)).thenReturn(page);

        Page<EstadioDTO> resultado = estadioController.listar(0, 10, "nome", "ASC");

        assertEquals(1, resultado.getTotalElements());
    }
}

