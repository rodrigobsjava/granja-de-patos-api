package com.rodrigobs.granja_de_patos_api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.rodrigobs.granja_de_patos_api.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
	List<Cliente> findByNomeContainingIgnoreCase(String nome);

	Page<Cliente> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
