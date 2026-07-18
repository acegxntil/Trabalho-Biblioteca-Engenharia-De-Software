package com.sgbu.repository;

import com.sgbu.model.Exemplar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExemplarRepository extends JpaRepository<Exemplar, Long> {

    @Query("SELECT e FROM Exemplar e WHERE e.livro.id = :livroId")
    List<Exemplar> findByLivroId(@Param("livroId") Long livroId);

    @Query("SELECT COUNT(e) FROM Exemplar e WHERE e.livro.id = :livroId AND e.status = :status")
    long countByLivroIdAndStatus(@Param("livroId") Long livroId, @Param("status") Exemplar.Status status);

    @Query("SELECT e FROM Exemplar e WHERE e.livro.id = :livroId AND e.status = :status")
    List<Exemplar> findByLivroIdAndStatus(@Param("livroId") Long livroId, @Param("status") Exemplar.Status status);
}
