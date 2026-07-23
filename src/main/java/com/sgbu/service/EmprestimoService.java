package com.sgbu.service;

import com.sgbu.model.Configuracao;
import com.sgbu.model.Emprestimo;
import com.sgbu.model.Exemplar;
import com.sgbu.model.Livro;
import com.sgbu.model.Multa;
import com.sgbu.model.Reserva;
import com.sgbu.model.Usuario;
import com.sgbu.repository.EmprestimoRepository;
import com.sgbu.repository.ExemplarRepository;
import com.sgbu.repository.MultaRepository;
import com.sgbu.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final ExemplarRepository exemplarRepository;
    private final MultaRepository multaRepository;
    private final ReservaRepository reservaRepository;
    private final ConfiguracaoService configuracaoService;

    public EmprestimoService(EmprestimoRepository emprestimoRepository,
                             ExemplarRepository exemplarRepository,
                             MultaRepository multaRepository,
                             ReservaRepository reservaRepository,
                             ConfiguracaoService configuracaoService) {
        this.emprestimoRepository = emprestimoRepository;
        this.exemplarRepository = exemplarRepository;
        this.multaRepository = multaRepository;
        this.reservaRepository = reservaRepository;
        this.configuracaoService = configuracaoService;
    }

    public List<Emprestimo> listarTodos() {
        return emprestimoRepository.findAll();
    }

    public Optional<Emprestimo> buscarPorId(Long id) {
        return emprestimoRepository.findById(id);
    }

    public Emprestimo registrar(Exemplar exemplar, Usuario usuario) {
        Configuracao config = configuracaoService.buscar();
        int prazoDias = config != null ? config.getPrazoEmprestimoDias() : 14;

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setExemplar(exemplar);
        emprestimo.setUsuario(usuario);
        emprestimo.setDataEmprestimo(LocalDate.now());
        emprestimo.setDataPrevistaDevolucao(LocalDate.now().plusDays(prazoDias));
        emprestimo.setStatus(Emprestimo.Status.ATIVO);

        exemplar.setStatus(Exemplar.Status.EMPRESTADO);
        exemplarRepository.save(exemplar);

        Livro livro = exemplar.getLivro();
        reservaRepository.findByLivroAndUsuarioAndStatus(livro, usuario, Reserva.Status.ATIVA)
                .ifPresent(r -> {
                    r.setStatus(Reserva.Status.ATENDIDA);
                    reservaRepository.save(r);
                });

        return emprestimoRepository.save(emprestimo);
    }

    public Emprestimo devolver(Long emprestimoId) {
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
                .orElseThrow(() -> new RuntimeException("Emprestimo nao encontrado"));

        emprestimo.setDataRealDevolucao(LocalDate.now());
        emprestimo.setStatus(Emprestimo.Status.DEVOLVIDO);

        Exemplar exemplar = emprestimo.getExemplar();
        exemplar.setStatus(Exemplar.Status.DISPONIVEL);
        exemplarRepository.save(exemplar);

        if (emprestimo.getDataPrevistaDevolucao().isBefore(LocalDate.now())) {
            long diasAtraso = ChronoUnit.DAYS.between(emprestimo.getDataPrevistaDevolucao(), LocalDate.now());
            Configuracao config = configuracaoService.buscar();
            BigDecimal valorMultaDia = config != null ? config.getValorMultaDia() : new BigDecimal("1.00");
            BigDecimal valorTotal = valorMultaDia.multiply(BigDecimal.valueOf(diasAtraso));

            Multa multa = new Multa();
            multa.setEmprestimo(emprestimo);
            multa.setValorTotal(valorTotal);
            multa.setDataGeracao(LocalDate.now());
            multa.setStatus(Multa.Status.PENDENTE);
            multaRepository.save(multa);

            emprestimo.setStatus(Emprestimo.Status.ATRASADO);
        }

        return emprestimoRepository.save(emprestimo);
    }

    public List<Emprestimo> buscarPorUsuario(Long usuarioId) {
        return emprestimoRepository.findByUsuarioIdOrderByDataEmprestimoDesc(usuarioId);
    }

    public List<Emprestimo> buscarAtivosPorUsuario(Long usuarioId) {
        return emprestimoRepository.findByUsuarioIdOrderByDataEmprestimoDesc(usuarioId)
                .stream()
                .filter(e -> e.getStatus() == Emprestimo.Status.ATIVO)
                .toList();
    }

    public List<Emprestimo> buscarAtrasadosHoje() {
        return emprestimoRepository.findByStatusAndDataPrevistaDevolucaoBefore(
                Emprestimo.Status.ATIVO, LocalDate.now());
    }

    public long contarAtivos() {
        return emprestimoRepository.countByStatus(Emprestimo.Status.ATIVO);
    }

    @Transactional(readOnly = true)
    public List<Emprestimo> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return emprestimoRepository.findByDataEmprestimoBetween(inicio, fim);
    }

    public List<Emprestimo> buscarHistoricoComFiltros(Long usuarioId, Emprestimo.Status status,
                                                       LocalDate dataInicio, LocalDate dataFim) {
        return emprestimoRepository.findHistoricoComFiltros(usuarioId, status, dataInicio, dataFim);
    }

    public List<Emprestimo> buscarRelatorioComFiltros(String titulo, String nomeUsuario,
                                                       LocalDate dataInicio, LocalDate dataFim) {
        return emprestimoRepository.findRelatorioComFiltros(titulo, nomeUsuario, dataInicio, dataFim);
    }
}
