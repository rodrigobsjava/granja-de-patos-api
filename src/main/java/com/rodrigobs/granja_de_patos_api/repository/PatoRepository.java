package com.rodrigobs.granja_de_patos_api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rodrigobs.granja_de_patos_api.model.Pato;

public interface PatoRepository extends JpaRepository<Pato, UUID> {
	boolean existsByIdAndVendidoTrue(UUID id);

	List<Pato> findByVendidoTrue();

	List<Pato> findByVendidoFalse();
}
