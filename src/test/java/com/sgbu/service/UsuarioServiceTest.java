package com.sgbu.service;

import com.sgbu.model.Usuario;
import com.sgbu.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        usuarioService = new UsuarioService(usuarioRepository, new BCryptPasswordEncoder());
    }

    @Test
    void salvarDeveCodificarSenha() {
        Usuario usuario = new Usuario();
        usuario.setNome("Teste");
        usuario.setEmail("teste@teste.com");
        usuario.setSenha("senha123");
        usuario.setCpf("12345678901");
        usuario.setTipoPerfil(Usuario.TipoPerfil.ESTUDANTE);

        usuarioService.salvar(usuario);

        assertTrue(usuario.getSenha().startsWith("$2a$"));
    }
}
