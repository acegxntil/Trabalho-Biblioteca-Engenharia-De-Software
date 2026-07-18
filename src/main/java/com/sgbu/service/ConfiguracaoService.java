package com.sgbu.service;

import com.sgbu.model.Configuracao;
import com.sgbu.repository.ConfiguracaoRepository;
import org.springframework.stereotype.Service;

@Service
public class ConfiguracaoService {

    private final ConfiguracaoRepository configuracaoRepository;

    public ConfiguracaoService(ConfiguracaoRepository configuracaoRepository) {
        this.configuracaoRepository = configuracaoRepository;
    }

    public Configuracao buscar() {
        return configuracaoRepository.findAll().stream().findFirst().orElse(null);
    }

    public Configuracao salvar(Configuracao configuracao) {
        return configuracaoRepository.save(configuracao);
    }
}
