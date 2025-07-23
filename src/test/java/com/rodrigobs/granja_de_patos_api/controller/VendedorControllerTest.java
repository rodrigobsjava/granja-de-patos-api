package com.rodrigobs.granja_de_patos_api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigobs.granja_de_patos_api.dto.requests.VendedorRequestDTO;
import com.rodrigobs.granja_de_patos_api.dto.responses.VendedorResponseDTO;
import com.rodrigobs.granja_de_patos_api.model.Vendedor;
import com.rodrigobs.granja_de_patos_api.service.VendedorService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(VendedorController.class)
class VendedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VendedorService vendedorService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID id;
    private VendedorRequestDTO requestDTO;
    private Vendedor vendedor;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        requestDTO = new VendedorRequestDTO("João da Silva", "39053344705", "MAT100"); // CPF válido

        vendedor = new Vendedor(id, "João da Silva", "39053344705", "MAT100");
        VendedorResponseDTO vendedorResponseDTO = new VendedorResponseDTO(id, "", "", "");
        
    }

    @Test
    void deveListarTodosOsVendedores() throws Exception {
    	List<VendedorResponseDTO> listaDTO = List.of(
    		    VendedorResponseDTO.builder()
    		        .id(UUID.randomUUID())
    		        .nome("João da Silva")
    		        .cpf("39053344705")
    		        .matricula("MAT100")
    		        .build()
    		);
        when(vendedorService.findAll()).thenReturn(listaDTO);

        mockMvc.perform(get("/vendedores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("João da Silva"))
                .andExpect(jsonPath("$[0].cpf").value("39053344705"))
                .andExpect(jsonPath("$[0].matricula").value("MAT100"));
    }

    @Test
    void deveBuscarVendedorPorId() throws Exception {
        when(vendedorService.findById(id)).thenReturn(vendedor);

        mockMvc.perform(get("/vendedores/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João da Silva"))
                .andExpect(jsonPath("$.cpf").value("39053344705"))
                .andExpect(jsonPath("$.matricula").value("MAT100"));
    }

    @Test
    void deveCriarVendedorComSucesso() throws Exception {
        when(vendedorService.create(any())).thenReturn(vendedor);

        mockMvc.perform(post("/vendedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("João da Silva"))
                .andExpect(jsonPath("$.cpf").value("39053344705"));
    }

    @Test
    void deveAtualizarVendedorComSucesso() throws Exception {
        when(vendedorService.update(eq(id), any())).thenReturn(vendedor);

        mockMvc.perform(put("/vendedores/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João da Silva"));
    }

    @Test
    void deveDeletarVendedor() throws Exception {
        doNothing().when(vendedorService).delete(id);

        mockMvc.perform(delete("/vendedores/{id}", id))
                .andExpect(status().isNoContent());
    }
}
