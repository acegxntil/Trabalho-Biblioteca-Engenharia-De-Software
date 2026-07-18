package com.sgbu.controller;

import com.sgbu.model.SugestaoAquisicao;
import com.sgbu.model.Usuario;
import com.sgbu.service.SugestaoService;
import com.sgbu.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/sugestoes")
public class SugestaoController {

    private final SugestaoService sugestaoService;
    private final UsuarioService usuarioService;

    public SugestaoController(SugestaoService sugestaoService, UsuarioService usuarioService) {
        this.sugestaoService = sugestaoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/nova")
    public String novaForm(Model model) {
        model.addAttribute("sugestao", new SugestaoAquisicao());
        return "sugestao/form";
    }

    @PostMapping("/salvar")
    public String salvar(SugestaoAquisicao sugestao,
                         Authentication authentication,
                         RedirectAttributes redirectAttributes) {
        try {
            String email = authentication.getName();
            var usuarioOpt = usuarioService.buscarPorEmail(email);
            if (usuarioOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("erro", "Usuario nao encontrado");
                return "redirect:/sugestoes/nova";
            }
            sugestaoService.salvar(sugestao, usuarioOpt.get());
            redirectAttributes.addFlashAttribute("sucesso", "Sugestao enviada com sucesso");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/sugestoes/nova";
    }

    @GetMapping("/listar")
    public String listar(Model model) {
        model.addAttribute("sugestoes", sugestaoService.listarPendentes());
        return "sugestao/listar";
    }

    @PostMapping("/analisar/{id}")
    public String analisar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            sugestaoService.analisar(id);
            redirectAttributes.addFlashAttribute("sucesso", "Sugestao marcada como analisada");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/sugestoes/listar";
    }
}
