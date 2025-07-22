package com.rodrigobs.granja_de_patos_api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rodrigobs.granja_de_patos_api.dto.responses.VendedorResponseDTO;
import com.rodrigobs.granja_de_patos_api.exception.BusinessException;
import com.rodrigobs.granja_de_patos_api.exception.NotFoundException;
import com.rodrigobs.granja_de_patos_api.model.Vendedor;
import com.rodrigobs.granja_de_patos_api.repository.VendaRepository;
import com.rodrigobs.granja_de_patos_api.repository.VendedorRepository;

@Service
public class VendedorService {

	@Autowired
	private VendedorRepository vendedorRepository;

	@Autowired
	private VendaRepository vendaRepository;

	public List<Vendedor> findAll() {
		return vendedorRepository.findAll();
	}

	public Vendedor findById(UUID id) {
		return buscarOuFalhar(id);
	}

	public Vendedor create(Vendedor vendedor) {
		validarDuplicidadeCpfMatricula(vendedor.getCpf(), vendedor.getMatricula());
		return vendedorRepository.save(vendedor);
	}

	public Vendedor update(UUID id, Vendedor vendedorAtualizado) {
		Vendedor existente = buscarOuFalhar(id);

		if (!existente.getCpf().equals(vendedorAtualizado.getCpf())
				&& vendedorRepository.existsByCpf(vendedorAtualizado.getCpf())) {
			throw new BusinessException("Outro vendedor já utiliza este CPF");
		}

		if (!existente.getMatricula().equals(vendedorAtualizado.getMatricula())
				&& vendedorRepository.existsByMatricula(vendedorAtualizado.getMatricula())) {
			throw new BusinessException("Outro vendedor já utiliza esta matrícula");
		}

		existente.setNome(vendedorAtualizado.getNome());
		existente.setCpf(vendedorAtualizado.getCpf());
		existente.setMatricula(vendedorAtualizado.getMatricula());

		return vendedorRepository.save(existente);
	}

	public void delete(UUID id) {
		Vendedor vendedor = buscarOuFalhar(id);

		if (vendaRepository.existsByVendedorId(vendedor.getId())) {
			throw new BusinessException("Não é possível excluir vendedor com vendas registradas");
		}

		vendedorRepository.deleteById(id);
	}

	private void validarDuplicidadeCpfMatricula(String cpf, String matricula) {
		if (vendedorRepository.existsByCpf(cpf)) {
			throw new BusinessException("CPF já cadastrado");
		}
		if (vendedorRepository.existsByMatricula(matricula)) {
			throw new BusinessException("Matrícula já cadastrada");
		}
	}

	private Vendedor buscarOuFalhar(UUID id) {
		return vendedorRepository.findById(id).orElseThrow(() -> new NotFoundException("Vendedor não encontrado"));
	}

	public VendedorResponseDTO toResponseDTO(Vendedor vendedor) {
		if (vendedor == null)
			return null;

		return VendedorResponseDTO.builder().id(vendedor.getId()).nome(vendedor.getNome()).cpf(vendedor.getCpf())
				.matricula(vendedor.getMatricula()).build();
	}
}
