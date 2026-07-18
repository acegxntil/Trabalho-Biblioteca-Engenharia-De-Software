package com.sgbu.controller;

import com.sgbu.model.Configuracao;
import com.sgbu.service.ConfiguracaoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class ConfiguracaoController {

    private final ConfiguracaoService configuracaoService;

    public ConfiguracaoController(ConfiguracaoService configuracaoService) {
        this.configuracaoService = configuracaoService;
    }

    @GetMapping("/config")
    public String configForm(Model model) {
        Configuracao config = configuracaoService.buscar();
        if (config == null) {
            config = new Configuracao();
        }
        model.addAttribute("config", config);
        return "admin/config";
    }

    @PostMapping("/config/salvar")
    public String salvarConfig(Configuracao config, RedirectAttributes redirectAttributes) {
        configuracaoService.salvar(config);
        redirectAttributes.addFlashAttribute("sucesso", "Configuracoes salvas com sucesso");
        return "redirect:/admin/config";
    }
}
