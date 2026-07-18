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
                                @RequestParam(required = false) Long livroId,
                                Model model) {
        model.addAttribute("etapa", usuarioId == null ? 1 : 2);
        if (usuarioId != null) {
            Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(usuarioId);
            usuarioOpt.ifPresent(u -> {
                model.addAttribute("usuarioSelecionado", u);
                if (livroId != null) {
                    Optional<Livro> livroOpt = livroService.buscarPorId(livroId);
                    livroOpt.ifPresent(l -> {
                        List<Exemplar> disponiveis = exemplarService.listarDisponiveisPorLivroId(livroId);
                        model.addAttribute("exemplaresDisponiveis", disponiveis);
                        model.addAttribute("livroSelecionado", l);
                    });
                }
            });
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
    public String meuHistorico(Model model, Authentication authentication) {
        String email = authentication.getName();
        var usuarioOpt = usuarioService.buscarPorEmail(email);
        if (usuarioOpt.isPresent()) {
            List<Emprestimo> emprestimos = emprestimoService.buscarPorUsuario(usuarioOpt.get().getId());
            model.addAttribute("emprestimos", emprestimos);
        }
        return "emprestimo/historico";
    }
}
