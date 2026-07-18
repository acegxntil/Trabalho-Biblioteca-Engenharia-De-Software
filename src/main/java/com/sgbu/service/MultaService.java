package com.sgbu.service;

import com.sgbu.model.Multa;
import com.sgbu.repository.MultaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MultaService {

    private final MultaRepository multaRepository;

    public MultaService(MultaRepository multaRepository) {
        this.multaRepository = multaRepository;
    }

    public List<Multa> buscarPorUsuarioId(Long usuarioId) {
        return multaRepository.findByEmprestimoUsuarioIdOrderByDataGeracaoDesc(usuarioId);
    }

    public void quitar(Long multaId) {
        Multa multa = multaRepository.findById(multaId)
                .orElseThrow(() -> new RuntimeException("Multa nao encontrada"));
        multa.setStatus(Multa.Status.QUITADA);
        multa.setDataQuitacao(LocalDate.now());
        multaRepository.save(multa);
    }

    public List<Multa> buscarPendentesPorUsuarioId(Long usuarioId) {
        return multaRepository.findByEmprestimoUsuarioIdOrderByDataGeracaoDesc(usuarioId)
                .stream()
                .filter(m -> m.getStatus() == Multa.Status.PENDENTE)
                .toList();
    }

    public long contarPendentes() {
        return multaRepository.countByStatus(Multa.Status.PENDENTE);
    }
}
