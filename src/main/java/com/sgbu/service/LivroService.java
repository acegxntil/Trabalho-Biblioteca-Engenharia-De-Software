package com.sgbu.service;

import com.sgbu.model.Exemplar;
import com.sgbu.model.Livro;
import com.sgbu.repository.ExemplarRepository;
import com.sgbu.repository.LivroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivroService {

    private final LivroRepository livroRepository;
    private final ExemplarRepository exemplarRepository;

    public LivroService(LivroRepository livroRepository, ExemplarRepository exemplarRepository) {
        this.livroRepository = livroRepository;
        this.exemplarRepository = exemplarRepository;
    }

    public List<Livro> listarTodos() {
        return livroRepository.findAll();
    }

    public Optional<Livro> buscarPorId(Long id) {
        return livroRepository.findById(id);
    }

    public Livro salvar(Livro livro) {
        return livroRepository.save(livro);
    }

    public Livro atualizar(Livro livro) {
        return livroRepository.save(livro);
    }

    public List<Livro> buscarPorTermo(String termo) {
        return livroRepository.findByTituloContainingIgnoreCaseOrAutoresNomeContainingIgnoreCaseOrIsbnContaining(termo, termo, termo);
    }

    public Optional<Livro> buscarPorIsbn(String isbn) {
        return livroRepository.findByIsbn(isbn);
    }

    public long contarDisponiveis(Long livroId) {
        return exemplarRepository.countByLivroIdAndStatus(livroId, Exemplar.Status.DISPONIVEL);
    }
}
