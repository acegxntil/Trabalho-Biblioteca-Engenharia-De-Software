package com.sgbu.service;

import com.sgbu.model.Configuracao;
import com.sgbu.model.Exemplar;
import com.sgbu.model.Livro;
import com.sgbu.model.Reserva;
import com.sgbu.model.Usuario;
import com.sgbu.repository.ExemplarRepository;
import com.sgbu.repository.LivroRepository;
import com.sgbu.repository.ReservaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final LivroRepository livroRepository;
    private final ExemplarRepository exemplarRepository;
    private final ConfiguracaoService configuracaoService;

    public ReservaService(ReservaRepository reservaRepository,
                          LivroRepository livroRepository,
                          ExemplarRepository exemplarRepository,
                          ConfiguracaoService configuracaoService) {
        this.reservaRepository = reservaRepository;
        this.livroRepository = livroRepository;
        this.exemplarRepository = exemplarRepository;
        this.configuracaoService = configuracaoService;
    }

    public Reserva criarReserva(Long livroId, Usuario usuario) {
        Livro livro = livroRepository.findById(livroId)
                .orElseThrow(() -> new RuntimeException("Livro nao encontrado"));

        boolean jaReservou = reservaRepository.existsByUsuarioAndLivroAndStatus(
                usuario, livro, Reserva.Status.ATIVA);
        if (jaReservou) {
            throw new RuntimeException("Voce ja possui uma reserva ativa para este livro");
        }

        Configuracao config = configuracaoService.buscar();
        int prazoRetirada = config != null ? config.getPrazoRetiradaReservaDias() : 3;

        long posicao = reservaRepository.findByLivroAndStatusOrderByPosicaoFilaAsc(livro, Reserva.Status.ATIVA).size() + 1;

        List<Exemplar> disponiveis = exemplarRepository.findByLivroIdAndStatus(livro.getId(), Exemplar.Status.DISPONIVEL);
        Exemplar exemplar;
        if (!disponiveis.isEmpty()) {
            exemplar = disponiveis.getFirst();
            exemplar.setStatus(Exemplar.Status.RESERVADO);
            exemplarRepository.save(exemplar);
        } else {
            exemplar = null;
        }

        Reserva reserva = new Reserva();
        reserva.setLivro(livro);
        reserva.setUsuario(usuario);
        reserva.setExemplar(exemplar);
        reserva.setDataReserva(LocalDate.now());
        reserva.setDataExpiracao(LocalDate.now().plusDays(prazoRetirada));
        reserva.setPosicaoFila((int) posicao);
        reserva.setStatus(Reserva.Status.ATIVA);

        return reservaRepository.save(reserva);
    }

    private void liberarExemplar(Reserva reserva) {
        if (reserva.getExemplar() != null) {
            Exemplar ex = reserva.getExemplar();
            ex.setStatus(Exemplar.Status.DISPONIVEL);
            exemplarRepository.save(ex);
            reserva.setExemplar(null);
        }
    }

    public void cancelarReserva(Long reservaId, Long usuarioId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva nao encontrada"));
        if (!reserva.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("Voce nao pode cancelar uma reserva de outro usuario");
        }
        liberarExemplar(reserva);
        reserva.setStatus(Reserva.Status.CANCELADA);
        reservaRepository.save(reserva);
    }

    public void cancelarReserva(Long reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva nao encontrada"));
        liberarExemplar(reserva);
        reserva.setStatus(Reserva.Status.CANCELADA);
        reservaRepository.save(reserva);
    }

    public List<Reserva> listarPorUsuario(Usuario usuario) {
        return reservaRepository.findByUsuarioOrderByDataReservaDesc(usuario);
    }

    public List<Reserva> listarAtivas() {
        return reservaRepository.findByStatusOrderByDataReservaAsc(Reserva.Status.ATIVA);
    }

    public long contarAtivas() {
        return reservaRepository.countByStatus(Reserva.Status.ATIVA);
    }
}
