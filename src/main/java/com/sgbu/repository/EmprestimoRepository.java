package com.sgbu.repository;

import com.sgbu.model.Emprestimo;
import com.sgbu.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    List<Emprestimo> findByUsuarioIdOrderByDataEmprestimoDesc(Long usuarioId);

    List<Emprestimo> findByUsuarioOrderByDataEmprestimoDesc(Usuario usuario);

    List<Emprestimo> findByDataRealDevolucaoBetween(LocalDate inicio, LocalDate fim);

    List<Emprestimo> findByStatusAndDataPrevistaDevolucaoBefore(Emprestimo.Status status, LocalDate data);

    List<Emprestimo> findByStatus(Emprestimo.Status status);

    long countByStatus(Emprestimo.Status status);

    Optional<Emprestimo> findByExemplarIdAndStatus(Long exemplarId, Emprestimo.Status status);

    @Query("SELECT e FROM Emprestimo e WHERE e.dataRealDevolucao IS NOT NULL ORDER BY e.dataRealDevolucao DESC")
    List<Emprestimo> findEmprestimosFinalizados();

    @Query("SELECT e.exemplar.livro.titulo, COUNT(e) FROM Emprestimo e GROUP BY e.exemplar.livro.id ORDER BY COUNT(e) DESC")
    List<Object[]> findLivrosMaisEmprestados();
}
