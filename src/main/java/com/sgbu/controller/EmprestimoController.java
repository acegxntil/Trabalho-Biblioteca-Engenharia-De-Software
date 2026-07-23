package com.sgbu.controller;

import com.sgbu.model.Emprestimo;
import com.sgbu.model.Exemplar;
import com.sgbu.model.Livro;
import com.sgbu.model.Usuario;
import com.sgbu.service.EmprestimoService;
import com.sgbu.service.ExemplarService;
import com.sgbu.service.LivroService;
import com.sgbu.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/emprestimos")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;
    private final UsuarioService usuarioService;
    private final ExemplarService exemplarService;
    private final LivroService livroService;

    public EmprestimoController(EmprestimoService emprestimoService,
                                 UsuarioService usuarioService,
                                 ExemplarService exemplarService,
                                 LivroService livroService) {
        this.emprestimoService = emprestimoService;
        this.usuarioService = usuarioService;
        this.exemplarService = exemplarService;
        this.livroService = livroService;
    }

    @GetMapping("/registrar")
    public String registrarForm(@RequestParam(required = false) Long usuarioId,
                                @RequestParam(required = false) String termo,
                                Model model) {
        if (usuarioId != null) {
            Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(usuarioId);
            usuarioOpt.ifPresent(u -> {
                model.addAttribute("usuarioSelecionado", u);
            });
        } else if (termo != null && !termo.isBlank()) {
            List<Usuario> usuarios = usuarioService.buscarPorNomeOuMatricula(termo);
            model.addAttribute("usuarios", usuarios);
            model.addAttribute("termo", termo);
            model.addAttribute("etapa", 1);
            return "emprestimo/registrar";
        }
        model.addAttribute("etapa", usuarioId == null ? 1 : 2);
        if (usuarioId != null && termo != null && !termo.isBlank()) {
            List<Livro> livros = livroService.buscarPorTermo(termo);
            model.addAttribute("livros", livros);
            model.addAttribute("termo", termo);
        }
        return "emprestimo/registrar";
    }

    @PostMapping("/registrar")
    public String registrar(@RequestParam Long usuarioId,
                            @RequestParam Long exemplarId,
                            RedirectAttributes redirectAttributes) {
        try {
            var usuarioOpt = usuarioService.buscarPorId(usuarioId);
            var exemplarOpt = exemplarService.buscarPorId(exemplarId);
            if (usuarioOpt.isEmpty() || exemplarOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("erro", "Usuario ou exemplar nao encontrado");
                return "redirect:/emprestimos/registrar";
            }
            if (exemplarOpt.get().getStatus() != Exemplar.Status.DISPONIVEL) {
                redirectAttributes.addFlashAttribute("erro", "Exemplar nao esta disponivel");
                return "redirect:/emprestimos/registrar";
            }
            emprestimoService.registrar(exemplarOpt.get(), usuarioOpt.get());
            redirectAttributes.addFlashAttribute("sucesso", "Emprestimo registrado com sucesso");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/emprestimos/registrar";
    }

    @GetMapping("/devolucao")
    public String devolucaoForm(Model model) {
        return "emprestimo/devolucao";
    }

    @PostMapping("/devolver")
    public String devolver(@RequestParam Long emprestimoId, RedirectAttributes redirectAttributes) {
        try {
            emprestimoService.devolver(emprestimoId);
            redirectAttributes.addFlashAttribute("sucesso", "Devolucao registrada com sucesso");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/emprestimos/devolucao";
    }

    @GetMapping("/historico/{usuarioId}")
    public String historico(@PathVariable Long usuarioId, Model model, Authentication authentication) {
        String email = authentication.getName();
        var usuarioLogadoOpt = usuarioService.buscarPorEmail(email);
        if (usuarioLogadoOpt.isEmpty() || !usuarioLogadoOpt.get().getId().equals(usuarioId)) {
            model.addAttribute("erro", "Voce so pode ver seu proprio historico");
            return "redirect:/home";
        }
        List<Emprestimo> emprestimos = emprestimoService.buscarPorUsuario(usuarioId);
        model.addAttribute("emprestimos", emprestimos);
        return "emprestimo/historico";
    }

    @GetMapping("/meu-historico")
    public String meuHistorico(@RequestParam(required = false) String status,
                               @RequestParam(required = false) String dataInicioStr,
                               @RequestParam(required = false) String dataFimStr,
                               Model model, Authentication authentication) {
        String email = authentication.getName();
        var usuarioOpt = usuarioService.buscarPorEmail(email);
        if (usuarioOpt.isPresent()) {
            Emprestimo.Status statusFilter = null;
            LocalDate dataInicio = null;
            LocalDate dataFim = null;
            if (status != null && !status.isEmpty()) {
                statusFilter = Emprestimo.Status.valueOf(status);
            }
            if (dataInicioStr != null && !dataInicioStr.isEmpty()) {
                dataInicio = LocalDate.parse(dataInicioStr);
            }
            if (dataFimStr != null && !dataFimStr.isEmpty()) {
                dataFim = LocalDate.parse(dataFimStr);
            }
            List<Emprestimo> emprestimos = emprestimoService.buscarHistoricoComFiltros(
                    usuarioOpt.get().getId(), statusFilter, dataInicio, dataFim);
            model.addAttribute("emprestimos", emprestimos);
            model.addAttribute("statusFiltro", status);
            model.addAttribute("dataInicioStr", dataInicioStr);
            model.addAttribute("dataFimStr", dataFimStr);
        }
        model.addAttribute("statusOptions", Emprestimo.Status.values());
        return "emprestimo/historico";
    }
}
