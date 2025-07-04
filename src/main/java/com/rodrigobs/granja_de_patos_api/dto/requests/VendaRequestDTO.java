package com.rodrigobs.granja_de_patos_api.dto.requests;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendaRequestDTO {
	@NotNull(message = "Cliente é obrigatório")
    private UUID clienteId;

    @NotNull(message = "Vendedor é obrigatório")
    private UUID vendedorId;

    @NotEmpty(message = "Deve haver pelo menos 1 pato na venda")
    private List<@NotNull UUID> patoIds;
}
