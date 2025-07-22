package com.rodrigobs.granja_de_patos_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.rodrigobs.granja_de_patos_api.dto.requests.PatoRequestDTO;
import com.rodrigobs.granja_de_patos_api.exception.NotFoundException;
import com.rodrigobs.granja_de_patos_api.model.Pato;
import com.rodrigobs.granja_de_patos_api.repository.PatoRepository;

class PatoServiceTest {

    @InjectMocks
    private PatoService patoService;

    @Mock
    private PatoRepository patoRepository;

    private Pato pato;
    private PatoRequestDTO patoRequestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        pato = new Pato();
        pato.setId(UUID.randomUUID());
        pato.setNome("Donald");
        pato.setVendido(false);
        pato.setFilhos(new HashSet<>());

        patoRequestDTO = PatoRequestDTO.builder()
                .nome("Donald")
                .pataMaeId(null)
                .vendido(false)
                .build();
    }

    @Test
    void testFindById_Sucesso() {
        when(patoRepository.findById(pato.getId())).thenReturn(Optional.of(pato));

        Pato resultado = patoService.findById(pato.getId());

        assertNotNull(resultado);
        assertEquals("Donald", resultado.getNome());
    }

    @Test
    void testFindById_NaoEncontrado() {
        when(patoRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> patoService.findById(UUID.randomUUID()));
    }

    @Test
    void testCreate_Sucesso() {
        when(patoRepository.save(any(Pato.class))).thenReturn(pato);

        Pato resultado = patoService.create(patoRequestDTO);

        assertNotNull(resultado);
        assertEquals("Donald", resultado.getNome());
        verify(patoRepository, times(1)).save(any(Pato.class));
    }

    @Test
    void testUpdate_Sucesso() {
        when(patoRepository.findById(pato.getId())).thenReturn(Optional.of(pato));
        when(patoRepository.save(any(Pato.class))).thenReturn(pato);

        PatoRequestDTO atualizadoDTO = PatoRequestDTO.builder()
                .nome("Donald Atualizado")
                .pataMaeId(null)
                .vendido(false)
                .build();

        Pato resultado = patoService.update(pato.getId(), atualizadoDTO);

        assertEquals("Donald Atualizado", resultado.getNome());
    }

    @Test
    void testUpdate_PatoNaoEncontrado() {
        when(patoRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> patoService.update(UUID.randomUUID(), patoRequestDTO));
    }

    @Test
    void testDelete_Sucesso() {
        when(patoRepository.findById(pato.getId())).thenReturn(Optional.of(pato));
        doNothing().when(patoRepository).delete(pato);

        patoService.delete(pato.getId());

        verify(patoRepository, times(1)).delete(pato);
    }

    @Test
    void testDelete_PatoNaoEncontrado() {
        when(patoRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> patoService.delete(UUID.randomUUID()));
    }
}
