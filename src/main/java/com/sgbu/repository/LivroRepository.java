package com.sgbu.repository;

import com.sgbu.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

    List<Livro> findByTituloContainingIgnoreCaseOrAutoresNomeContainingIgnoreCaseOrIsbnContaining(String titulo, String autorNome, String isbn);

    Optional<Livro> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);
}
