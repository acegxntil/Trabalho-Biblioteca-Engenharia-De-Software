package com.sgbu.service;

import com.sgbu.model.Emprestimo;
import com.sgbu.repository.EmprestimoRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class NotificacaoService {

    private final JavaMailSender mailSender;
    private final EmprestimoRepository emprestimoRepository;

    public NotificacaoService(JavaMailSender mailSender, EmprestimoRepository emprestimoRepository) {
        this.mailSender = mailSender;
        this.emprestimoRepository = emprestimoRepository;
    }

    public void notificarDisponibilidadeReserva(String email, String titulo) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(email);
            msg.setSubject("Reserva Disponivel - SGBU");
            msg.setText("O livro \"" + titulo + "\" que voce reservou esta disponivel para retirada.");
            mailSender.send(msg);
        } catch (Exception e) {
            System.out.println("Erro ao enviar notificacao de disponibilidade: " + e.getMessage());
        }
    }

    public void notificarVencimentoEmprestimo(String email, String titulo, LocalDate dataVencimento) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(email);
            msg.setSubject("Vencimento de Emprestimo - SGBU");
            msg.setText("O emprestimo do livro \"" + titulo + "\" vence em " + dataVencimento + ".");
            mailSender.send(msg);
        } catch (Exception e) {
            System.out.println("Erro ao enviar notificacao de vencimento: " + e.getMessage());
        }
    }

    public void notificarMulta(String email, BigDecimal valor) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(email);
            msg.setSubject("Multa Registrada - SGBU");
            msg.setText("Uma multa no valor de R$ " + valor + " foi registrada em sua conta.");
            mailSender.send(msg);
        } catch (Exception e) {
            System.out.println("Erro ao enviar notificacao de multa: " + e.getMessage());
        }
    }

    @Scheduled(cron = "0 0 8 * * ?")
    public void agendarNotificacoesVencimento() {
        List<Emprestimo> ativos = emprestimoRepository.findByStatus(Emprestimo.Status.ATIVO);
        LocalDate hoje = LocalDate.now();

        for (Emprestimo emp : ativos) {
            LocalDate vencimento = emp.getDataPrevistaDevolucao();
            if (vencimento.equals(hoje) || vencimento.equals(hoje.plusDays(3))) {
                String email = emp.getUsuario().getEmail();
                String titulo = emp.getExemplar().getLivro().getTitulo();
                notificarVencimentoEmprestimo(email, titulo, vencimento);
            }
        }
    }
}
