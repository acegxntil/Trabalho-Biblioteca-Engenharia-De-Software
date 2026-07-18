package com.sgbu.service;

import com.sgbu.model.Configuracao;
import com.sgbu.model.Livro;
import com.sgbu.model.Reserva;
import com.sgbu.model.Usuario;
import com.sgbu.repository.LivroRepository;
import com.sgbu.repository.ReservaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final LivroRepository livroRepository;
    private final ConfiguracaoService configuracaoService;

    public ReservaService(ReservaRepository reservaRepository,
                          LivroRepository livroRepository,
                          ConfiguracaoService configuracaoService) {
        this.reservaRepository = reservaRepository;
        this.livroRepository = livroRepository;
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

        Reserva reserva = new Reserva();
        reserva.setLivro(livro);
        reserva.setUsuario(usuario);
        reserva.setDataReserva(LocalDate.now());
        reserva.setDataExpiracao(LocalDate.now().plusDays(prazoRetirada));
        reserva.setPosicaoFila((int) posicao);
        reserva.setStatus(Reserva.Status.ATIVA);

        return reservaRepository.save(reserva);
    }

    public void cancelarReserva(Long reservaId, Long usuarioId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva nao encontrada"));
        if (!reserva.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("Voce nao pode cancelar uma reserva de outro usuario");
        }
        reserva.setStatus(Reserva.Status.CANCELADA);
        reservaRepository.save(reserva);
    }

    public void cancelarReserva(Long reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva nao encontrada"));
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
