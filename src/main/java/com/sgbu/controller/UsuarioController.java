package com.sgbu.controller;

import com.sgbu.model.Usuario;
import com.sgbu.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuario/listar";
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("perfis", Usuario.TipoPerfil.values());
        return "usuario/cadastrar";
    }

    @PostMapping("/salvar")
    public String salvar(Usuario usuario, RedirectAttributes redirectAttributes) {
        if (usuarioService.existePorEmail(usuario.getEmail())) {
            redirectAttributes.addFlashAttribute("erro", "Email ja cadastrado");
            return "redirect:/usuarios/novo";
        }
        if (usuarioService.existePorCpf(usuario.getCpf())) {
            redirectAttributes.addFlashAttribute("erro", "CPF ja cadastrado");
            return "redirect:/usuarios/novo";
        }
        usuarioService.salvar(usuario);
        redirectAttributes.addFlashAttribute("sucesso", "Usuario cadastrado com sucesso");
        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        var usuarioOpt = usuarioService.buscarPorId(id);
        if (usuarioOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Usuario nao encontrado");
            return "redirect:/usuarios";
        }
        model.addAttribute("usuario", usuarioOpt.get());
        model.addAttribute("perfis", Usuario.TipoPerfil.values());
        return "usuario/editar";
    }

    @PostMapping("/atualizar")
    public String atualizar(Usuario usuario, Authentication authentication,
                            RedirectAttributes redirectAttributes) {
        String emailLogado = authentication.getName();
        var logadoOpt = usuarioService.buscarPorEmail(emailLogado);
        if (logadoOpt.isPresent() && logadoOpt.get().getTipoPerfil() == Usuario.TipoPerfil.BIBLIOTECARIO) {
            var targetOpt = usuarioService.buscarPorId(usuario.getId());
            if (targetOpt.isPresent() && targetOpt.get().getTipoPerfil() == Usuario.TipoPerfil.BIBLIOTECARIO) {
                redirectAttributes.addFlashAttribute("erro", "Bibliotecario nao pode alterar outro bibliotecario");
                return "redirect:/usuarios";
            }
        }
        usuarioService.atualizar(usuario);
        redirectAttributes.addFlashAttribute("sucesso", "Usuario atualizado com sucesso");
        return "redirect:/usuarios";
    }

    @GetMapping("/toggle-status/{id}")
    public String toggleStatus(@PathVariable Long id, Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        String emailLogado = authentication.getName();
        var logadoOpt = usuarioService.buscarPorEmail(emailLogado);
        if (logadoOpt.isPresent() && logadoOpt.get().getTipoPerfil() == Usuario.TipoPerfil.BIBLIOTECARIO) {
            var targetOpt = usuarioService.buscarPorId(id);
            if (targetOpt.isPresent() && targetOpt.get().getTipoPerfil() == Usuario.TipoPerfil.BIBLIOTECARIO) {
                redirectAttributes.addFlashAttribute("erro", "Bibliotecario nao pode alterar outro bibliotecario");
                return "redirect:/usuarios";
            }
        }
        usuarioService.toggleStatus(id);
        redirectAttributes.addFlashAttribute("sucesso", "Status do usuario alterado com sucesso");
        return "redirect:/usuarios";
    }

    @GetMapping("/buscar")
    public String buscar(@RequestParam String termo, Model model) {
        List<Usuario> usuarios = usuarioService.buscarPorNomeOuMatricula(termo);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("termo", termo);
        return "usuario/listar";
    }
}
