package com.rodrigobs.granja_de_patos_api.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.rodrigobs.granja_de_patos_api.service.RelatorioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/vendas/excel")
    public ResponseEntity<InputStreamResource> gerarRelatorioExcel() throws IOException {
        ByteArrayInputStream in = relatorioService.gerarRelatorioVendasExcel();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=vendas.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(new InputStreamResource(in));
    }
}
