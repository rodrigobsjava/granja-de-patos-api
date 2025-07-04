package com.rodrigobs.granja_de_patos_api.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "venda_pato")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class VendaPato {
	@EmbeddedId
	private VendaPatoId id;

	@ManyToOne
	@MapsId("vendaId")
	@JoinColumn(name = "venda_id")
	private Venda venda;

	@ManyToOne
	@MapsId("patoId")
	@JoinColumn(name = "pato_id")
	private Pato pato;

}
