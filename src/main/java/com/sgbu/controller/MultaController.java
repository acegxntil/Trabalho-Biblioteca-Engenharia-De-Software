package com.sgbu.controller;

import com.sgbu.model.Multa;
import com.sgbu.model.Usuario;
import com.sgbu.service.MultaService;
import com.sgbu.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/multas")
public class MultaController {

    private final MultaService multaService;
    private final UsuarioService usuarioService;

    public MultaController(MultaService multaService, UsuarioService usuarioService) {
        this.multaService = multaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/minhas")
    public String minhas(Model model, Authentication authentication) {
        String email = authentication.getName();
        var usuarioOpt = usuarioService.buscarPorEmail(email);
        if (usuarioOpt.isPresent()) {
            List<Multa> multas = multaService.buscarPorUsuarioId(usuarioOpt.get().getId());
            model.addAttribute("multas", multas);
        }
        return "multa/visualizar";
    }

    @PostMapping("/quitar/{id}")
    public String quitar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            multaService.quitar(id);
            redirectAttributes.addFlashAttribute("sucesso", "Multa quitada com sucesso");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/multas/minhas";
    }
}
