package com.sgbu.controller;

import com.sgbu.service.PasswordResetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final PasswordResetService passwordResetService;

    public AuthController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/recuperar-senha")
    public String recuperarSenhaForm() {
        return "recuperar-senha";
    }

    @PostMapping("/recuperar-senha")
    public String recuperarSenha(@RequestParam String email,
                                  RedirectAttributes redirectAttributes) {
        passwordResetService.criarToken(email);
        redirectAttributes.addFlashAttribute("sucesso",
                "Se o email estiver cadastrado, voce recebera um link de recuperacao.");
        return "redirect:/login";
    }

    @GetMapping("/redefinir-senha")
    public String redefinirSenhaForm(@RequestParam String token, Model model,
                                      RedirectAttributes redirectAttributes) {
        if (!passwordResetService.validarToken(token)) {
            redirectAttributes.addFlashAttribute("erro",
                    "Token invalido ou expirado. Solicite uma nova recuperacao.");
            return "redirect:/login";
        }
        model.addAttribute("token", token);
        return "redefinir-senha";
    }

    @PostMapping("/redefinir-senha")
    public String redefinirSenha(@RequestParam String token,
                                  @RequestParam String senha,
                                  @RequestParam String confirmacaoSenha,
                                  RedirectAttributes redirectAttributes) {
        if (!senha.equals(confirmacaoSenha)) {
            redirectAttributes.addFlashAttribute("erro", "As senhas nao conferem");
            return "redirect:/redefinir-senha?token=" + token;
        }
        try {
            passwordResetService.redefinirSenha(token, senha);
            redirectAttributes.addFlashAttribute("sucesso", "Senha redefinida com sucesso");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/login";
    }
}
