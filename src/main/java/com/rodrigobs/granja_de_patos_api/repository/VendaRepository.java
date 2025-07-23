package com.rodrigobs.granja_de_patos_api.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rodrigobs.granja_de_patos_api.model.Venda;

public interface VendaRepository extends JpaRepository<Venda, UUID> {
	boolean existsByPatos_Id(UUID patoId);

	List<Venda> findByClienteId(UUID clienteId);

	List<Venda> findByVendedorId(UUID vendedorId);

	@Query("SELECT v.vendedor.id, COUNT(v.id), SUM(v.valorTotal) FROM Venda v GROUP BY v.vendedor.id")
	List<Object[]> gerarRankingVendedores();

	boolean existsByVendedorId(UUID vendedorId);

	long countByVendedorId(UUID vendedorId);

	@Query("SELECT SUM(v.valorTotal) FROM Venda v WHERE v.vendedor.id = :vendedorId")
	Optional<Double> sumValorTotalByVendedorId(@Param("vendedorId") UUID vendedorId);

}
