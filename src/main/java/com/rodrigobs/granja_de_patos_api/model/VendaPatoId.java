package com.rodrigobs.granja_de_patos_api.model;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class VendaPatoId implements Serializable {

	private static final long serialVersionUID = 1L;
	private UUID vendaId;
	private UUID patoId;

}
