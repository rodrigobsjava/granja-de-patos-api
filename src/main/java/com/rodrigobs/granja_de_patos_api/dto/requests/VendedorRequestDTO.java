package com.rodrigobs.granja_de_patos_api.dto.requests;

import br.com.caelum.stella.bean.validation.CPF;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendedorRequestDTO {
	@NotBlank(message = "Nome do vendedor é obrigatório")
	@Size(min = 2, message = "Nome do vendedor deve ter pelo menos 2 caracteres")
	private String nome;

	@NotBlank(message = "CPF é obrigatório")
	@CPF(message = "CPF inválido")
	private String cpf;

	@NotBlank(message = "Matrícula é obrigatória")
	@Size(min = 4, message = "Matrícula deve ter pelo menos 4 caracteres")
	private String matricula;
}
