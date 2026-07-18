package com.sgbu.controller;

import com.sgbu.model.Livro;
import com.sgbu.service.LivroService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @GetMapping
    public String listar(Model model) {
        List<Livro> livros = livroService.listarTodos();
        model.addAttribute("livros", livros);
        Map<Long, Long> disponiveisMap = livros.stream()
                .collect(Collectors.toMap(Livro::getId, l -> livroService.contarDisponiveis(l.getId())));
        model.addAttribute("disponiveisMap", disponiveisMap);
        return "livro/listar";
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("livro", new Livro());
        return "livro/cadastrar";
    }

    @PostMapping("/salvar")
    public String salvar(Livro livro, RedirectAttributes redirectAttributes) {
        livroService.salvar(livro);
        redirectAttributes.addFlashAttribute("sucesso", "Livro cadastrado com sucesso");
        return "redirect:/livros";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        var livroOpt = livroService.buscarPorId(id);
        if (livroOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Livro nao encontrado");
            return "redirect:/livros";
        }
        model.addAttribute("livro", livroOpt.get());
        return "livro/editar";
    }

    @PostMapping("/atualizar")
    public String atualizar(Livro livro, RedirectAttributes redirectAttributes) {
        livroService.atualizar(livro);
        redirectAttributes.addFlashAttribute("sucesso", "Livro atualizado com sucesso");
        return "redirect:/livros";
    }

    @GetMapping("/{id}")
    public String detalhes(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        var livroOpt = livroService.buscarPorId(id);
        if (livroOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Livro nao encontrado");
            return "redirect:/livros";
        }
        Livro livro = livroOpt.get();
        long disponiveis = livroService.contarDisponiveis(id);
        model.addAttribute("livro", livro);
        model.addAttribute("disponiveis", disponiveis);
        model.addAttribute("exemplares", livro.getExemplares());
        return "livro/detalhes";
    }

    @GetMapping("/buscar")
    public String buscar(@RequestParam String termo, Model model) {
        List<Livro> livros = livroService.buscarPorTermo(termo);
        model.addAttribute("livros", livros);
        model.addAttribute("termo", termo);
        Map<Long, Long> disponiveisMap = livros.stream()
                .collect(Collectors.toMap(Livro::getId, l -> livroService.contarDisponiveis(l.getId())));
        model.addAttribute("disponiveisMap", disponiveisMap);
        return "livro/listar";
    }
}
