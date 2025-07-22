package com.rodrigobs.granja_de_patos_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.rodrigobs.granja_de_patos_api.dto.requests.ClienteRequestDTO;
import com.rodrigobs.granja_de_patos_api.dto.responses.ClienteResponseDTO;
import com.rodrigobs.granja_de_patos_api.exception.NotFoundException;
import com.rodrigobs.granja_de_patos_api.model.Cliente;
import com.rodrigobs.granja_de_patos_api.repository.ClienteRepository;

public class ClienteServiceTest {

	@InjectMocks
	private ClienteService clienteService;

	@Mock
	private ClienteRepository clienteRepository;

	private Cliente cliente;
	private UUID id;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		id = UUID.randomUUID();
		cliente = new Cliente(id, "João da Silva", true);
	}

	@Test
	void testFindById_ClienteExistente() {
		when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));

		Cliente result = clienteService.findEntityById(id);

		assertNotNull(result);
		assertEquals(cliente.getNome(), result.getNome());
	}

	@Test
	void testFindById_ClienteNaoEncontrado() {
		when(clienteRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> clienteService.findById(id));
	}

	@Test
	void testCreate_ClienteValido() {
		ClienteRequestDTO dto = new ClienteRequestDTO("João da Silva", true);
		Cliente clienteEsperado = new Cliente(id, "João da Silva", true);

		when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteEsperado);

		ClienteResponseDTO result = clienteService.create(dto);

		assertNotNull(result);
		assertEquals("João da Silva", result.getNome());
		assertTrue(result.isElegivelDesconto());
	}

	@Test
	void testUpdate_ClienteExistente() {
		ClienteRequestDTO dto = new ClienteRequestDTO("João Atualizado", false);
		when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));
		when(clienteRepository.save(any(Cliente.class))).thenReturn(new Cliente(id, "João Atualizado", false));

		ClienteResponseDTO result = clienteService.update(id, dto);

		assertNotNull(result);
		assertEquals("João Atualizado", result.getNome());
		assertFalse(result.isElegivelDesconto());
	}

	@Test
	void testUpdate_ClienteNaoEncontrado() {
		ClienteRequestDTO dto = new ClienteRequestDTO("João Atualizado", false);
		when(clienteRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> clienteService.update(id, dto));
	}

	@Test
	void testDelete_ClienteExistente() {
		when(clienteRepository.existsById(id)).thenReturn(true);
		doNothing().when(clienteRepository).deleteById(id);

		clienteService.delete(id);

		verify(clienteRepository, times(1)).deleteById(id);
	}

	@Test
	void testDelete_ClienteNaoEncontrado() {
		when(clienteRepository.existsById(id)).thenReturn(false);

		assertThrows(NotFoundException.class, () -> clienteService.delete(id));
	}
}
