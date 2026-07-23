package com.sgbu.service;

import com.sgbu.model.*;
import com.sgbu.repository.EmprestimoRepository;
import com.sgbu.repository.ExemplarRepository;
import com.sgbu.repository.MultaRepository;
import com.sgbu.repository.ReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmprestimoServiceTest {

    @Mock
    private EmprestimoRepository emprestimoRepository;
    @Mock
    private ExemplarRepository exemplarRepository;
    @Mock
    private MultaRepository multaRepository;
    @Mock
    private ReservaRepository reservaRepository;
    @Mock
    private ConfiguracaoService configuracaoService;

    private EmprestimoService emprestimoService;

    @BeforeEach
    void setUp() {
        emprestimoService = new EmprestimoService(emprestimoRepository, exemplarRepository,
                multaRepository, reservaRepository, configuracaoService);
    }

    @Test
    void registrarDeveCriarEmprestimoComPrazoPadrao() {
        Configuracao config = new Configuracao();
        config.setPrazoEmprestimoDias(14);
        when(configuracaoService.buscar()).thenReturn(config);

        Exemplar exemplar = new Exemplar();
        exemplar.setStatus(Exemplar.Status.DISPONIVEL);
        Livro livro = new Livro();
        livro.setTitulo("Teste");
        exemplar.setLivro(livro);

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        when(emprestimoRepository.save(any(Emprestimo.class))).thenAnswer(i -> i.getArgument(0));
        when(exemplarRepository.save(any(Exemplar.class))).thenAnswer(i -> i.getArgument(0));

        Emprestimo result = emprestimoService.registrar(exemplar, usuario);

        assertNotNull(result);
        assertEquals(LocalDate.now().plusDays(14), result.getDataPrevistaDevolucao());
        assertEquals(Emprestimo.Status.ATIVO, result.getStatus());
    }
}
