package com.sgbu.repository;

import com.sgbu.model.SugestaoAquisicao;
import com.sgbu.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SugestaoAquisicaoRepository extends JpaRepository<SugestaoAquisicao, Long> {

    List<SugestaoAquisicao> findByStatusOrderByDataSugestaoAsc(SugestaoAquisicao.Status status);

    List<SugestaoAquisicao> findByUsuarioOrderByDataSugestaoDesc(Usuario usuario);
}
