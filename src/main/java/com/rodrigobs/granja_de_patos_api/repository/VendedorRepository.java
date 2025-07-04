package com.rodrigobs.granja_de_patos_api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rodrigobs.granja_de_patos_api.model.Vendedor;

public interface VendedorRepository extends JpaRepository<Vendedor, UUID> {
	boolean existsByCpf(String cpf);

	boolean existsByMatricula(String matricula);
}
