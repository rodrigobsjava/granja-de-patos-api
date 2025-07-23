package com.rodrigobs.granja_de_patos_api.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import com.rodrigobs.granja_de_patos_api.model.Venda;
import com.rodrigobs.granja_de_patos_api.repository.VendaRepository;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RelatorioService {

    @Autowired
    private VendaRepository vendaRepository;

    public ByteArrayInputStream gerarRelatorioVendasExcel() throws IOException {
        List<Venda> vendas = vendaRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Relatório de Vendas");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Cliente");
            header.createCell(1).setCellValue("Vendedor");
            header.createCell(2).setCellValue("Valor Total");
            header.createCell(3).setCellValue("Data da Venda");
            header.createCell(4).setCellValue("Quantidade de Patos");

            int linha = 1;
            for (Venda venda : vendas) {
                Row row = sheet.createRow(linha++);
                row.createCell(0).setCellValue(venda.getCliente().getNome());
                row.createCell(1).setCellValue(venda.getVendedor().getNome());
                row.createCell(2).setCellValue(venda.getValorTotal());
                row.createCell(3).setCellValue(venda.getData().toString());
                row.createCell(4).setCellValue(venda.getPatos().size());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
