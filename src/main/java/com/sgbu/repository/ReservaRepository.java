package com.sgbu.repository;

import com.sgbu.model.Livro;
import com.sgbu.model.Reserva;
import com.sgbu.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByUsuarioOrderByDataReservaDesc(Usuario usuario);

    List<Reserva> findByStatusOrderByDataReservaAsc(Reserva.Status status);

    List<Reserva> findByLivroAndStatusOrderByPosicaoFilaAsc(Livro livro, Reserva.Status status);

    Optional<Reserva> findByLivroAndUsuarioAndStatus(Livro livro, Usuario usuario, Reserva.Status status);

    boolean existsByUsuarioAndLivroAndStatus(Usuario usuario, Livro livro, Reserva.Status status);

    long countByStatus(Reserva.Status status);

    List<Reserva> findByUsuarioIdOrderByDataReservaDesc(Long usuarioId);

    List<Reserva> findByLivroIdAndStatusOrderByPosicaoFilaAsc(Long livroId, Reserva.Status status);
}
