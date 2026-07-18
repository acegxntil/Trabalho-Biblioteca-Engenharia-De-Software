package com.sgbu.service;

import com.sgbu.model.Usuario;
import com.sgbu.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario salvar(Usuario usuario) {
        usuario.setDataCadastro(LocalDate.now());
        usuario.setStatus(Usuario.StatusUsuario.ATIVO);
        return usuarioRepository.save(usuario);
    }

    public Usuario atualizar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void toggleStatus(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));
        if (usuario.getStatus() == Usuario.StatusUsuario.ATIVO) {
            usuario.setStatus(Usuario.StatusUsuario.INATIVO);
        } else {
            usuario.setStatus(Usuario.StatusUsuario.ATIVO);
        }
        usuarioRepository.save(usuario);
    }

    public List<Usuario> buscarPorNomeOuMatricula(String termo) {
        return usuarioRepository.findByNomeContainingIgnoreCaseOrMatriculaContaining(termo, termo);
    }

    public boolean existePorEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public boolean existePorCpf(String cpf) {
        return usuarioRepository.existsByCpf(cpf);
    }
}
