package com.rodrigobs.granja_de_patos_api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import com.rodrigobs.granja_de_patos_api.dto.requests.VendaRequestDTO;
import com.rodrigobs.granja_de_patos_api.exception.BusinessException;
import com.rodrigobs.granja_de_patos_api.exception.NotFoundException;
import com.rodrigobs.granja_de_patos_api.model.*;
import com.rodrigobs.granja_de_patos_api.repository.VendaRepository;

class VendaServiceTest {

    @InjectMocks
    private VendaService vendaService;

    @Mock
    private VendaRepository vendaRepository;

    @Mock
    private ClienteService clienteService;

    @Mock
    private VendedorService vendedorService;

    @Mock
    private PatoService patoService;

    private UUID clienteId, vendedorId, patoId, vendaId;
    private Cliente cliente;
    private Vendedor vendedor;
    private Pato pato;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        clienteId = UUID.randomUUID();
        vendedorId = UUID.randomUUID();
        patoId = UUID.randomUUID();
        vendaId = UUID.randomUUID();

        cliente = new Cliente(clienteId, "João Cliente", false);
        vendedor = new Vendedor(vendedorId, "Maria Vendedora", "123456789", "545352");
        pato = new Pato(patoId, "Pato Quack", null, null, new HashSet<>(), false);
    }

    @Test
    void deveCriarVendaComSucesso() {
        VendaRequestDTO dto = new VendaRequestDTO(clienteId, vendedorId, List.of(patoId));

        when(clienteService.findEntityById(clienteId)).thenReturn(cliente);
        when(vendedorService.findById(vendedorId)).thenReturn(vendedor);
        when(patoService.findById(patoId)).thenReturn(pato);
        when(patoService.save(any())).thenReturn(pato);
        when(vendaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Venda venda = vendaService.create(dto);

        assertEquals(cliente, venda.getCliente());
        assertEquals(vendedor, venda.getVendedor());
        assertTrue(venda.getPatos().contains(pato));
        verify(vendaRepository).save(any());
    }

    @Test
    void deveLancarExcecao_QuandoPatoJaVendido() {
        pato.setVendido(true);
        VendaRequestDTO dto = new VendaRequestDTO(clienteId, vendedorId, List.of(patoId));

        when(clienteService.findEntityById(clienteId)).thenReturn(cliente);
        when(vendedorService.findById(vendedorId)).thenReturn(vendedor);
        when(patoService.findById(patoId)).thenReturn(pato);

        assertThrows(BusinessException.class, () -> vendaService.create(dto));
    }

    @Test
    void deveAtualizarVendaExistente() {
        VendaRequestDTO dto = new VendaRequestDTO(clienteId, vendedorId, List.of(patoId));
        Venda antiga = new Venda(vendaId, LocalDateTime.now(), 0.0, cliente, vendedor, Set.of(pato));

        when(vendaRepository.findById(vendaId)).thenReturn(Optional.of(antiga));
        when(clienteService.findEntityById(clienteId)).thenReturn(cliente);
        when(vendedorService.findById(vendedorId)).thenReturn(vendedor);
        when(patoService.findById(patoId)).thenReturn(pato);
        when(vendaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Venda atualizada = vendaService.update(vendaId, dto);

        assertEquals(vendaId, atualizada.getId());
        verify(vendaRepository).save(any());
    }

    @Test
    void deveLancarExcecao_SeVendaNaoExisteAoAtualizar() {
        when(vendaRepository.findById(vendaId)).thenReturn(Optional.empty());
        VendaRequestDTO dto = new VendaRequestDTO(clienteId, vendedorId, List.of(patoId));

        assertThrows(NotFoundException.class, () -> vendaService.update(vendaId, dto));
    }

    @Test
    void deveDeletarVendaExistente() {
        Venda venda = new Venda(vendaId, LocalDateTime.now(), 0.0, cliente, vendedor, Set.of(pato));
        when(vendaRepository.findById(vendaId)).thenReturn(Optional.of(venda));

        vendaService.delete(vendaId);

        verify(vendaRepository).delete(venda);
    }

    @Test
    void deveLancarExcecao_SeVendaNaoExisteAoDeletar() {
        when(vendaRepository.findById(vendaId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> vendaService.delete(vendaId));
    }
}
