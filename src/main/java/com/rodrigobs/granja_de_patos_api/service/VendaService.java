package com.rodrigobs.granja_de_patos_api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rodrigobs.granja_de_patos_api.dto.requests.VendaRequestDTO;
import com.rodrigobs.granja_de_patos_api.exception.BusinessException;
import com.rodrigobs.granja_de_patos_api.exception.NotFoundException;
import com.rodrigobs.granja_de_patos_api.model.Cliente;
import com.rodrigobs.granja_de_patos_api.model.Pato;
import com.rodrigobs.granja_de_patos_api.model.Venda;
import com.rodrigobs.granja_de_patos_api.model.Vendedor;
import com.rodrigobs.granja_de_patos_api.repository.VendaRepository;

@Service
public class VendaService {

	@Autowired
	private VendaRepository vendaRepository;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private VendedorService vendedorService;

	@Autowired
	private PatoService patoService;

	public List<Venda> findAll() {
		return vendaRepository.findAll();
	}

	public Venda findById(UUID id) {
		return vendaRepository.findById(id).orElseThrow(() -> new NotFoundException("Venda não encontrada"));
	}

	public Venda create(VendaRequestDTO dto) {
		Venda novaVenda = construirVenda(dto, true);
		return vendaRepository.save(novaVenda);
	}

	public Venda update(UUID id, VendaRequestDTO dto) {
		Venda existente = findById(id);

		Venda atualizada = construirVenda(dto, false);
		atualizada.setId(existente.getId());
		atualizada.setData(existente.getData());

		return vendaRepository.save(atualizada);
	}

	public void delete(UUID id) {
		Venda venda = findById(id);
		vendaRepository.delete(venda);
	}

	private Venda construirVenda(VendaRequestDTO dto, boolean isNovaVenda) {
		Cliente cliente = clienteService.findEntityById(dto.getClienteId());
		Vendedor vendedor = vendedorService.findById(dto.getVendedorId());

		Set<Pato> patos = carregarPatosValidados(dto.getPatoIds(), isNovaVenda);

		Venda venda = new Venda();
		venda.setCliente(cliente);
		venda.setVendedor(vendedor);
		venda.setPatos(patos);
		venda.setData(LocalDateTime.now());

		double valorTotal = calculaValorTotal(patos);
		if (cliente.isElegivelDesconto()) {
			valorTotal *= 0.8; // Aplica 20% de desconto
		}

		venda.setValorTotal(valorTotal);

		if (isNovaVenda) {
			marcarPatosComoVendidos(patos);
		}

		return venda;
	}

	private Set<Pato> carregarPatosValidados(List<UUID> patoIds, boolean verificarVenda) {
		if (patoIds == null || patoIds.isEmpty()) {
			throw new BusinessException("Deve haver pelo menos um pato na venda");
		}

		return patoIds.stream().map(id -> {
			Pato pato = patoService.findById(id);

			if (verificarVenda && pato.isVendido()) {
				throw new BusinessException("O pato com ID " + id + " já foi vendido");
			}

			return pato;
		}).collect(Collectors.toSet());
	}

	private void marcarPatosComoVendidos(Set<Pato> patos) {
		patos.forEach(p -> {
			p.setVendido(true);
			patoService.save(p); // ou create(p), conforme sua implementação
		});
	}

	private double calculaValorTotal(Set<Pato> patos) {
		return patos.stream().mapToDouble(Pato::getPreco).sum();
	}
}
