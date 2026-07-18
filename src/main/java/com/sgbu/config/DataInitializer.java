package com.sgbu.config;

import com.sgbu.model.Autor;
import com.sgbu.model.Configuracao;
import com.sgbu.model.Exemplar;
import com.sgbu.model.Livro;
import com.sgbu.model.Usuario;
import com.sgbu.repository.AutorRepository;
import com.sgbu.repository.ConfiguracaoRepository;
import com.sgbu.repository.ExemplarRepository;
import com.sgbu.repository.LivroRepository;
import com.sgbu.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final AutorRepository autorRepository;
    private final LivroRepository livroRepository;
    private final ExemplarRepository exemplarRepository;
    private final ConfiguracaoRepository configuracaoRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository, AutorRepository autorRepository, LivroRepository livroRepository, ExemplarRepository exemplarRepository, ConfiguracaoRepository configuracaoRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.autorRepository = autorRepository;
        this.livroRepository = livroRepository;
        this.exemplarRepository = exemplarRepository;
        this.configuracaoRepository = configuracaoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (usuarioRepository.existsByEmail("admin@sgbu.com")) {
            return;
        }

        Configuracao config = new Configuracao();
        config.setPrazoEmprestimoDias(14);
        config.setValorMultaDia(new BigDecimal("2.50"));
        config.setPrazoRetiradaReservaDias(3);
        configuracaoRepository.save(config);

        Usuario admin = new Usuario();
        admin.setNome("Admin SGBU");
        admin.setEmail("admin@sgbu.com");
        admin.setSenha(passwordEncoder.encode("admin123"));
        admin.setCpf("00000000000");
        admin.setMatricula("ADMIN001");
        admin.setTipoPerfil(Usuario.TipoPerfil.ADMIN);
        admin.setStatus(Usuario.StatusUsuario.ATIVO);
        admin.setDataCadastro(LocalDate.now());
        usuarioRepository.save(admin);

        Usuario bibliotecario = new Usuario();
        bibliotecario.setNome("Bibliotecario");
        bibliotecario.setEmail("biblio@sgbu.com");
        bibliotecario.setSenha(passwordEncoder.encode("biblio123"));
        bibliotecario.setCpf("11111111111");
        bibliotecario.setMatricula("BIB001");
        bibliotecario.setTipoPerfil(Usuario.TipoPerfil.BIBLIOTECARIO);
        bibliotecario.setStatus(Usuario.StatusUsuario.ATIVO);
        bibliotecario.setDataCadastro(LocalDate.now());
        usuarioRepository.save(bibliotecario);

        Usuario estudante = new Usuario();
        estudante.setNome("Estudante Teste");
        estudante.setEmail("estudante@sgbu.com");
        estudante.setSenha(passwordEncoder.encode("estudante123"));
        estudante.setCpf("22222222222");
        estudante.setMatricula("2024001");
        estudante.setTipoPerfil(Usuario.TipoPerfil.ESTUDANTE);
        estudante.setStatus(Usuario.StatusUsuario.ATIVO);
        estudante.setDataCadastro(LocalDate.now());
        usuarioRepository.save(estudante);

        Usuario professor = new Usuario();
        professor.setNome("Professor Teste");
        professor.setEmail("professor@sgbu.com");
        professor.setSenha(passwordEncoder.encode("professor123"));
        professor.setCpf("33333333333");
        professor.setMatricula("PROF001");
        professor.setTipoPerfil(Usuario.TipoPerfil.PROFESSOR);
        professor.setStatus(Usuario.StatusUsuario.ATIVO);
        professor.setDataCadastro(LocalDate.now());
        usuarioRepository.save(professor);

        Autor sommerville = new Autor();
        sommerville.setNome("Ian Sommerville");
        sommerville.setNacionalidade("Britânica");
        autorRepository.save(sommerville);

        Autor pressman = new Autor();
        pressman.setNome("Roger S. Pressman");
        pressman.setNacionalidade("Americana");
        autorRepository.save(pressman);

        Livro livro1 = new Livro();
        livro1.setTitulo("Engenharia de Software");
        livro1.setIsbn("978-85-694-9436-8");
        livro1.setEditora("Pearson");
        livro1.setAnoPublicacao(2018);
        livro1.setEdicao("10a");
        livro1.setCategoria("Engenharia de Software");
        livro1.setDescricao("Livro classico de engenharia de software");
        livro1.setLocalizacaoPrateleira("ESTANTE A - 01");
        livro1.setAutores(List.of(sommerville));
        livroRepository.save(livro1);

        Livro livro2 = new Livro();
        livro2.setTitulo("Engenharia de Software: Uma Abordagem Profissional");
        livro2.setIsbn("978-85-8055-278-5");
        livro2.setEditora("McGraw-Hill");
        livro2.setAnoPublicacao(2016);
        livro2.setEdicao("8a");
        livro2.setCategoria("Engenharia de Software");
        livro2.setDescricao("Abordagem profissional de engenharia de software");
        livro2.setLocalizacaoPrateleira("ESTANTE A - 02");
        livro2.setAutores(List.of(pressman));
        livroRepository.save(livro2);

        Livro livro3 = new Livro();
        livro3.setTitulo("Engenharia de Software Moderna");
        livro3.setIsbn("978-85-93768-00-5");
        livro3.setEditora("Editora Moderna");
        livro3.setAnoPublicacao(2020);
        livro3.setEdicao("1a");
        livro3.setCategoria("Engenharia de Software");
        livro3.setDescricao("Livro atual sobre engenharia de software");
        livro3.setLocalizacaoPrateleira("ESTANTE A - 03");
        livro3.setAutores(List.of(sommerville));
        livroRepository.save(livro3);

        int tombo = 1;
        for (Livro livro : List.of(livro1, livro2, livro3)) {
            for (int i = 0; i < 3; i++) {
                Exemplar exemplar = new Exemplar();
                exemplar.setLivro(livro);
                exemplar.setNumeroTombo(String.format("TOMBO-%03d", tombo++));
                exemplar.setStatus(Exemplar.Status.DISPONIVEL);
                exemplarRepository.save(exemplar);
            }
        }
    }
}
