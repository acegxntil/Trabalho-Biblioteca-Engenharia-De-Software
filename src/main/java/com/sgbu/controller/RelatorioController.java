package com.sgbu.controller;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.sgbu.model.Emprestimo;
import com.sgbu.service.EmprestimoService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.awt.Color;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/relatorios")
public class RelatorioController {

    private final EmprestimoService emprestimoService;

    public RelatorioController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @GetMapping("/emprestimos")
    public String formulario() {
        return "relatorio/emprestimos";
    }

    @PostMapping("/emprestimos/gerar")
    public String gerar(@RequestParam(required = false) String dataInicioStr,
                        @RequestParam(required = false) String dataFimStr,
                        @RequestParam(required = false) String titulo,
                        @RequestParam(required = false) String nomeUsuario,
                        Model model,
                        RedirectAttributes redirectAttributes) {
        try {
            LocalDate dataInicio = (dataInicioStr != null && !dataInicioStr.isEmpty())
                    ? LocalDate.parse(dataInicioStr) : null;
            LocalDate dataFim = (dataFimStr != null && !dataFimStr.isEmpty())
                    ? LocalDate.parse(dataFimStr) : null;
            if (dataInicio != null && dataFim != null && dataInicio.isAfter(dataFim)) {
                redirectAttributes.addFlashAttribute("erro", "Data inicial nao pode ser posterior a data final");
                return "redirect:/relatorios/emprestimos";
            }
            List<Emprestimo> emprestimos = emprestimoService.buscarRelatorioComFiltros(
                    titulo, nomeUsuario, dataInicio, dataFim);
            model.addAttribute("emprestimos", emprestimos);
            model.addAttribute("dataInicio", dataInicio);
            model.addAttribute("dataFim", dataFim);
            model.addAttribute("titulo", titulo);
            model.addAttribute("nomeUsuario", nomeUsuario);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Formato de data invalido");
            return "redirect:/relatorios/emprestimos";
        }
        return "relatorio/emprestimos";
    }

    @PostMapping("/emprestimos/pdf")
    public void exportarPdf(@RequestParam(required = false) String dataInicioStr,
                            @RequestParam(required = false) String dataFimStr,
                            @RequestParam(required = false) String titulo,
                            @RequestParam(required = false) String nomeUsuario,
                            HttpServletResponse response) throws IOException {
        LocalDate dataInicio = (dataInicioStr != null && !dataInicioStr.isEmpty())
                ? LocalDate.parse(dataInicioStr) : null;
        LocalDate dataFim = (dataFimStr != null && !dataFimStr.isEmpty())
                ? LocalDate.parse(dataFimStr) : null;

        List<Emprestimo> emprestimos = emprestimoService.buscarRelatorioComFiltros(
                titulo, nomeUsuario, dataInicio, dataFim);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=relatorio_emprestimos.pdf");

        Document doc = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(doc, response.getOutputStream());
        doc.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, new Color(255, 255, 255));
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 9);

        Paragraph title = new Paragraph("Relatorio de Emprestimos", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(10);
        doc.add(title);

        Paragraph periodo = new Paragraph("Periodo: " + dataInicio.format(fmt) + " a " + dataFim.format(fmt),
                FontFactory.getFont(FontFactory.HELVETICA, 10));
        periodo.setSpacingAfter(15);
        doc.add(periodo);

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1, 3, 4, 3, 3, 3, 2});

        String[] headers = {"ID", "Usuario", "Livro", "Data Emprestimo", "Data Prevista", "Data Devolucao", "Status"};
        PdfPCell cell;
        for (String h : headers) {
            cell = new PdfPCell(new Phrase(h, headerFont));
            cell.setBackgroundColor(new Color(40, 167, 69));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);
        }

        for (Emprestimo e : emprestimos) {
            table.addCell(new PdfPCell(new Phrase(String.valueOf(e.getId()), cellFont)));
            table.addCell(new PdfPCell(new Phrase(e.getUsuario().getNome(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(e.getExemplar().getLivro().getTitulo(), cellFont)));
            table.addCell(new PdfPCell(new Phrase(e.getDataEmprestimo().format(fmt), cellFont)));
            table.addCell(new PdfPCell(new Phrase(e.getDataPrevistaDevolucao().format(fmt), cellFont)));
            table.addCell(new PdfPCell(new Phrase(
                    e.getDataRealDevolucao() != null ? e.getDataRealDevolucao().format(fmt) : "-", cellFont)));
            table.addCell(new PdfPCell(new Phrase(e.getStatus().name(), cellFont)));
        }

        doc.add(table);
        doc.close();
    }
}
