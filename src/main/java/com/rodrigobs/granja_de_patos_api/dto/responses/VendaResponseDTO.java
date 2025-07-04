package com.rodrigobs.granja_de_patos_api.dto.responses;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendaResponseDTO {
	private UUID id;
	private ClienteResponseDTO cliente;
	private VendedorResponseDTO vendedor;
	private List<PatoResponseDTO> patos;
	private Double valorTotal;
	
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime dataVenda;
}
