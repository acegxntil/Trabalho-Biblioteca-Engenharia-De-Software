package com.sgbu.config;

import com.sgbu.model.Usuario;
import com.sgbu.repository.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario nao encontrado: " + email));

        if (usuario.getStatus() == Usuario.StatusUsuario.INATIVO) {
            throw new UsernameNotFoundException("Usuario inativo: " + email);
        }

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + usuario.getTipoPerfil().name());

        return new User(usuario.getEmail(), usuario.getSenha(), Collections.singletonList(authority));
    }
}
