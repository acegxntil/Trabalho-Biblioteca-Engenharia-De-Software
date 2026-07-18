package com.sgbu.service;

import com.sgbu.model.Emprestimo;
import com.sgbu.model.Multa;
import com.sgbu.model.Usuario;
import com.sgbu.repository.EmprestimoRepository;
import com.sgbu.repository.ExemplarRepository;
import com.sgbu.repository.MultaRepository;
import com.sgbu.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RelatorioService {

    private final EmprestimoRepository emprestimoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ExemplarRepository exemplarRepository;
    private final MultaRepository multaRepository;

    public RelatorioService(EmprestimoRepository emprestimoRepository,
                            UsuarioRepository usuarioRepository,
                            ExemplarRepository exemplarRepository,
                            MultaRepository multaRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.usuarioRepository = usuarioRepository;
        this.exemplarRepository = exemplarRepository;
        this.multaRepository = multaRepository;
    }

    public List<Emprestimo> gerarRelatorioEmprestimos(LocalDate inicio, LocalDate fim) {
        return emprestimoRepository.findByDataRealDevolucaoBetween(inicio, fim);
    }

    public Map<String, Object> gerarDashboard() {
        Map<String, Object> dashboard = new HashMap<>();

        long usuariosAtivos = usuarioRepository.findAll().stream()
                .filter(u -> u.getStatus() == Usuario.StatusUsuario.ATIVO)

                .count();
        dashboard.put("usuariosAtivos", usuariosAtivos);

        long livrosNoAcervo = exemplarRepository.count();
        dashboard.put("livrosNoAcervo", livrosNoAcervo);

        long emprestimosAtivos = emprestimoRepository.findByStatus(Emprestimo.Status.ATIVO).size();
        dashboard.put("emprestimosAtivos", emprestimosAtivos);

        List<Multa> multasPendentes = multaRepository.findByStatus(Multa.Status.PENDENTE);
        dashboard.put("multasPendentes", multasPendentes.size());

        List<Object[]> maisEmprestados = emprestimoRepository.findLivrosMaisEmprestados();
        dashboard.put("livrosMaisEmprestados", maisEmprestados);

        return dashboard;
    }
}
