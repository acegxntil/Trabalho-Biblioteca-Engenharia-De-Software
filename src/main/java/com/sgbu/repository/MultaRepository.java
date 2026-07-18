package com.sgbu.repository;

import com.sgbu.model.Multa;
import com.sgbu.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface MultaRepository extends JpaRepository<Multa, Long> {

    List<Multa> findByEmprestimoUsuarioOrderByDataGeracaoDesc(Usuario usuario);

    List<Multa> findByEmprestimoUsuarioIdOrderByDataGeracaoDesc(Long usuarioId);

    List<Multa> findByStatus(Multa.Status status);

    long countByStatus(Multa.Status status);

    @Query("SELECT COALESCE(SUM(m.valorTotal), 0) FROM Multa m WHERE m.emprestimo.usuario.id = :usuarioId AND m.status = 'PENDENTE'")
    BigDecimal sumMultasPendentesByUsuarioId(Long usuarioId);
}
