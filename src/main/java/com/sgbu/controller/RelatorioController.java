package com.sgbu.controller;

import com.sgbu.model.Emprestimo;
import com.sgbu.service.EmprestimoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
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
    public String gerar(@RequestParam(required = false) LocalDate dataInicio,
                        @RequestParam(required = false) LocalDate dataFim,
                        Model model,
                        RedirectAttributes redirectAttributes) {
        if (dataInicio == null) {
            dataInicio = LocalDate.now().minusMonths(1);
        }
        if (dataFim == null) {
            dataFim = LocalDate.now();
        }
        if (dataInicio.isAfter(dataFim)) {
            redirectAttributes.addFlashAttribute("erro", "Data inicial nao pode ser posterior a data final");
            return "redirect:/relatorios/emprestimos";
        }
        List<Emprestimo> emprestimos = emprestimoService.buscarPorPeriodo(dataInicio, dataFim);
        model.addAttribute("emprestimos", emprestimos);
        model.addAttribute("dataInicio", dataInicio);
        model.addAttribute("dataFim", dataFim);
        return "relatorio/emprestimos";
    }
}
