package com.sgbu.service;

import com.sgbu.model.SugestaoAquisicao;
import com.sgbu.model.Usuario;
import com.sgbu.repository.SugestaoAquisicaoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SugestaoService {

    private final SugestaoAquisicaoRepository sugestaoRepository;

    public SugestaoService(SugestaoAquisicaoRepository sugestaoRepository) {
        this.sugestaoRepository = sugestaoRepository;
    }

    public SugestaoAquisicao salvar(SugestaoAquisicao sugestao, Usuario usuario) {
        sugestao.setUsuario(usuario);
        sugestao.setDataSugestao(LocalDate.now());
        sugestao.setStatus(SugestaoAquisicao.Status.PENDENTE);
        return sugestaoRepository.save(sugestao);
    }

    public List<SugestaoAquisicao> listarPendentes() {
        return sugestaoRepository.findByStatusOrderByDataSugestaoAsc(SugestaoAquisicao.Status.PENDENTE);
    }

    public void analisar(Long id) {
        SugestaoAquisicao sugestao = sugestaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sugestao nao encontrada"));
        sugestao.setStatus(SugestaoAquisicao.Status.ANALISADA);
        sugestaoRepository.save(sugestao);
    }
}
