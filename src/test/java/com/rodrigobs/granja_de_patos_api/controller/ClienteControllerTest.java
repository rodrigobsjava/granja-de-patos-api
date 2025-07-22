package com.rodrigobs.granja_de_patos_api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.rodrigobs.granja_de_patos_api.dto.requests.ClienteRequestDTO;
import com.rodrigobs.granja_de_patos_api.dto.responses.ClienteResponseDTO;
import com.rodrigobs.granja_de_patos_api.model.Cliente;
import com.rodrigobs.granja_de_patos_api.service.ClienteService;

class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    private UUID id;
    private Cliente cliente;
    private ClienteResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        id = UUID.randomUUID();
        cliente = new Cliente(id, "João", true);
        responseDTO = ClienteResponseDTO.builder().id(id).nome("João").elegivelDesconto(true).build();
    }

    @Test
    void testBuscarPorId() {
        when(clienteService.findEntityById(id)).thenReturn(cliente);

        ResponseEntity<ClienteResponseDTO> response = clienteController.buscarPorId(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("João", response.getBody().getNome());
    }

    @Test
    void testCriar() {
        ClienteRequestDTO request = new ClienteRequestDTO("Maria", true);
        when(clienteService.create(request)).thenReturn(responseDTO);

        ResponseEntity<ClienteResponseDTO> response = clienteController.criar(request);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("João", response.getBody().getNome()); // simulado, poderia ser "Maria"
    }

    @Test
    void testListarTodos() {
        when(clienteService.findAll()).thenReturn(List.of(responseDTO));

        List<ClienteResponseDTO> result = clienteController.listarTodos();

        assertEquals(1, result.size());
        assertEquals("João", result.get(0).getNome());
    }

    @Test
    void testAtualizar() {
        ClienteRequestDTO request = new ClienteRequestDTO("João Atualizado", false);
        ClienteResponseDTO atualizado = ClienteResponseDTO.builder().id(id).nome("João Atualizado").elegivelDesconto(false).build();

        when(clienteService.update(id, request)).thenReturn(atualizado);

        ResponseEntity<ClienteResponseDTO> response = clienteController.atualizar(id, request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("João Atualizado", response.getBody().getNome());
    }

    @Test
    void testDeletar() {
        ResponseEntity<Void> response = clienteController.deletar(id);

        verify(clienteService, times(1)).delete(id);
        assertEquals(204, response.getStatusCodeValue());
    }
}
