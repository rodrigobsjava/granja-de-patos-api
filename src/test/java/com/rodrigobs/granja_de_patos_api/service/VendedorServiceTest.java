package com.rodrigobs.granja_de_patos_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.rodrigobs.granja_de_patos_api.dto.responses.VendedorResponseDTO;
import com.rodrigobs.granja_de_patos_api.exception.BusinessException;
import com.rodrigobs.granja_de_patos_api.exception.NotFoundException;
import com.rodrigobs.granja_de_patos_api.model.Vendedor;
import com.rodrigobs.granja_de_patos_api.repository.VendaRepository;
import com.rodrigobs.granja_de_patos_api.repository.VendedorRepository;

public class VendedorServiceTest {

    @Mock
    private VendedorRepository vendedorRepository;

    @Mock
    private VendaRepository vendaRepository;

    @InjectMocks
    private VendedorService vendedorService;

    private UUID id;
    private Vendedor vendedor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();
        vendedor = new Vendedor(id, "Maria Silva", "12345678901", "MAT1234");
    }

    @Test
    void testFindAll() {
        when(vendedorRepository.findAll()).thenReturn(List.of(vendedor));

        List<VendedorResponseDTO> result = vendedorService.findAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Maria Silva", result.get(0).getNome());
    }

    @Test
    void testFindById_Exists() {
        when(vendedorRepository.findById(id)).thenReturn(Optional.of(vendedor));

        Vendedor result = vendedorService.findById(id);

        assertNotNull(result);
        assertEquals("Maria Silva", result.getNome());
    }

    @Test
    void testFindById_NotFound() {
        when(vendedorRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> vendedorService.findById(id));
    }

    @Test
    void testCreate_Success() {
        when(vendedorRepository.existsByCpf(vendedor.getCpf())).thenReturn(false);
        when(vendedorRepository.existsByMatricula(vendedor.getMatricula())).thenReturn(false);
        when(vendedorRepository.save(vendedor)).thenReturn(vendedor);

        Vendedor result = vendedorService.create(vendedor);

        assertNotNull(result);
        assertEquals("Maria Silva", result.getNome());
        verify(vendedorRepository, times(1)).save(vendedor);
    }

    @Test
    void testCreate_CpfExists() {
        when(vendedorRepository.existsByCpf(vendedor.getCpf())).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> vendedorService.create(vendedor));
        assertEquals("CPF já cadastrado", exception.getMessage());
    }

    @Test
    void testCreate_MatriculaExists() {
        when(vendedorRepository.existsByCpf(vendedor.getCpf())).thenReturn(false);
        when(vendedorRepository.existsByMatricula(vendedor.getMatricula())).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> vendedorService.create(vendedor));
        assertEquals("Matrícula já cadastrada", exception.getMessage());
    }

    @Test
    void testUpdate_Success() {
        Vendedor updated = new Vendedor(id, "Maria Atualizada", "12345678901", "MAT1234");

        when(vendedorRepository.findById(id)).thenReturn(Optional.of(vendedor));
        when(vendedorRepository.existsByCpf(updated.getCpf())).thenReturn(false);
        when(vendedorRepository.existsByMatricula(updated.getMatricula())).thenReturn(false);
        when(vendedorRepository.save(any(Vendedor.class))).thenReturn(updated);

        Vendedor result = vendedorService.update(id, updated);

        assertNotNull(result);
        assertEquals("Maria Atualizada", result.getNome());
    }

    @Test
    void testUpdate_CpfUsedByAnother() {
        Vendedor updated = new Vendedor(id, "Maria Atualizada", "99999999999", "MAT1234");

        when(vendedorRepository.findById(id)).thenReturn(Optional.of(vendedor));
        when(vendedorRepository.existsByCpf(updated.getCpf())).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> vendedorService.update(id, updated));
        assertEquals("Outro vendedor já utiliza este CPF", exception.getMessage());
    }

    @Test
    void testUpdate_MatriculaUsedByAnother() {
        Vendedor updated = new Vendedor(id, "Maria Atualizada", "12345678901", "MAT9999");

        when(vendedorRepository.findById(id)).thenReturn(Optional.of(vendedor));
        when(vendedorRepository.existsByCpf(updated.getCpf())).thenReturn(false);
        when(vendedorRepository.existsByMatricula(updated.getMatricula())).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> vendedorService.update(id, updated));
        assertEquals("Outro vendedor já utiliza esta matrícula", exception.getMessage());
    }

    @Test
    void testDelete_Success() {
        when(vendedorRepository.findById(id)).thenReturn(Optional.of(vendedor));
        when(vendaRepository.existsByVendedorId(id)).thenReturn(false);

        vendedorService.delete(id);

        verify(vendedorRepository, times(1)).deleteById(id);
    }

    @Test
    void testDelete_VendedorComVendas() {
        when(vendedorRepository.findById(id)).thenReturn(Optional.of(vendedor));
        when(vendaRepository.existsByVendedorId(id)).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> vendedorService.delete(id));
        assertEquals("Não é possível excluir vendedor com vendas registradas", exception.getMessage());
    }
}
