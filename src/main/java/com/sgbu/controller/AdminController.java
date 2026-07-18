package com.sgbu.controller;

import com.sgbu.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UsuarioService usuarioService;
    private final LivroService livroService;
    private final EmprestimoService emprestimoService;
    private final ReservaService reservaService;
    private final MultaService multaService;

    public AdminController(UsuarioService usuarioService,
                           LivroService livroService,
                           EmprestimoService emprestimoService,
                           ReservaService reservaService,
                           MultaService multaService) {
        this.usuarioService = usuarioService;
        this.livroService = livroService;
        this.emprestimoService = emprestimoService;
        this.reservaService = reservaService;
        this.multaService = multaService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalUsuarios", usuarioService.listarTodos().size());
        model.addAttribute("totalLivros", livroService.listarTodos().size());
        model.addAttribute("emprestimosAtivos", emprestimoService.contarAtivos());
        model.addAttribute("multasPendentes", multaService.contarPendentes());
        model.addAttribute("reservasAtivas", reservaService.contarAtivas());
        return "admin/dashboard";
    }
}
