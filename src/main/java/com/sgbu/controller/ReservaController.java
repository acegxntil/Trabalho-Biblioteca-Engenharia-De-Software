package com.sgbu.controller;

import com.sgbu.model.Reserva;
import com.sgbu.model.Usuario;
import com.sgbu.service.ReservaService;
import com.sgbu.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;
    private final UsuarioService usuarioService;

    public ReservaController(ReservaService reservaService, UsuarioService usuarioService) {
        this.reservaService = reservaService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/nova")
    public String nova(@RequestParam Long livroId,
                       Authentication authentication,
                       RedirectAttributes redirectAttributes) {
        try {
            String email = authentication.getName();
            var usuarioOpt = usuarioService.buscarPorEmail(email);
            if (usuarioOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("erro", "Usuario nao encontrado");
                return "redirect:/livros";
            }
            reservaService.criarReserva(livroId, usuarioOpt.get());
            redirectAttributes.addFlashAttribute("sucesso", "Reserva realizada com sucesso");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/reservas/minhas";
    }

    @GetMapping("/minhas")
    public String minhas(Model model, Authentication authentication) {
        String email = authentication.getName();
        var usuarioOpt = usuarioService.buscarPorEmail(email);
        if (usuarioOpt.isPresent()) {
            List<Reserva> reservas = reservaService.listarPorUsuario(usuarioOpt.get());
            model.addAttribute("reservas", reservas);
        }
        return "reserva/minhas";
    }

    @GetMapping("/gerenciar")
    public String gerenciar(Model model) {
        List<Reserva> reservas = reservaService.listarAtivas();
        model.addAttribute("reservas", reservas);
        return "reserva/gerenciar";
    }

    @PostMapping("/cancelar/{id}")
    public String cancelar(@PathVariable Long id,
                           Authentication authentication,
                           RedirectAttributes redirectAttributes) {
        try {
            String email = authentication.getName();
            var usuarioOpt = usuarioService.buscarPorEmail(email);
            if (usuarioOpt.isPresent() && usuarioOpt.get().getTipoPerfil() == Usuario.TipoPerfil.BIBLIOTECARIO) {
                reservaService.cancelarReserva(id);
            } else if (usuarioOpt.isPresent()) {
                reservaService.cancelarReserva(id, usuarioOpt.get().getId());
            }
            redirectAttributes.addFlashAttribute("sucesso", "Reserva cancelada com sucesso");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/reservas/minhas";
    }
}
