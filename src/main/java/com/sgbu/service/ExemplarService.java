package com.sgbu.service;

import com.sgbu.model.Exemplar;
import com.sgbu.repository.ExemplarRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExemplarService {

    private final ExemplarRepository exemplarRepository;

    public ExemplarService(ExemplarRepository exemplarRepository) {
        this.exemplarRepository = exemplarRepository;
    }

    public List<Exemplar> listarPorLivroId(Long livroId) {
        return exemplarRepository.findByLivroId(livroId);
    }

    public List<Exemplar> listarDisponiveisPorLivroId(Long livroId) {
        return exemplarRepository.findByLivroIdAndStatus(livroId, Exemplar.Status.DISPONIVEL);
    }

    public Optional<Exemplar> buscarPorId(Long id) {
        return exemplarRepository.findById(id);
    }

    public Exemplar salvar(Exemplar exemplar) {
        return exemplarRepository.save(exemplar);
    }
}
