package com.sgbu.service;

import com.sgbu.model.PasswordResetToken;
import com.sgbu.model.Usuario;
import com.sgbu.repository.PasswordResetTokenRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;
    private final UsuarioService usuarioService;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetService(PasswordResetTokenRepository tokenRepository,
                                UsuarioService usuarioService,
                                JavaMailSender mailSender,
                                PasswordEncoder passwordEncoder) {
        this.tokenRepository = tokenRepository;
        this.usuarioService = usuarioService;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
    }

    public void criarToken(String email) {
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorEmail(email);
        if (usuarioOpt.isEmpty() || usuarioOpt.get().getStatus() == Usuario.StatusUsuario.INATIVO) {
            return;
        }

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(
                token, email, LocalDateTime.now().plusHours(1));
        tokenRepository.save(resetToken);

        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(email);
            msg.setSubject("Redefinicao de Senha - SGBU");
            msg.setText("Para redefinir sua senha, acesse o link:\n\n"
                    + "http://localhost:8080/redefinir-senha?token=" + token
                    + "\n\nEste link expira em 1 hora.");
            mailSender.send(msg);
        } catch (Exception e) {
            System.out.println("Erro ao enviar email de recuperacao: " + e.getMessage());
        }
    }

    public boolean validarToken(String token) {
        return tokenRepository.findByToken(token)
                .filter(t -> !t.isExpirado() && !t.isUtilizado())
                .isPresent();
    }

    public void redefinirSenha(String token, String novaSenha) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token invalido"));

        if (resetToken.isExpirado()) {
            throw new RuntimeException("Token expirado");
        }
        if (resetToken.isUtilizado()) {
            throw new RuntimeException("Token ja utilizado");
        }

        Usuario usuario = usuarioService.buscarPorEmail(resetToken.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));

        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuarioService.atualizar(usuario);

        resetToken.setUtilizado(true);
        tokenRepository.save(resetToken);
    }
}
