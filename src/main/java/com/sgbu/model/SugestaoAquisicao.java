package com.sgbu.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "sugestao_aquisicao")
public class SugestaoAquisicao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private String tituloSugerido;

    private String autorSugerido;

    @Column(columnDefinition = "TEXT")
    private String justificativa;

    @Column(nullable = false)
    private LocalDate dataSugestao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    public enum Status {
        PENDENTE, ANALISADA
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTituloSugerido() {
        return tituloSugerido;
    }

    public void setTituloSugerido(String tituloSugerido) {
        this.tituloSugerido = tituloSugerido;
    }

    public String getAutorSugerido() {
        return autorSugerido;
    }

    public void setAutorSugerido(String autorSugerido) {
        this.autorSugerido = autorSugerido;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public LocalDate getDataSugestao() {
        return dataSugestao;
    }

    public void setDataSugestao(LocalDate dataSugestao) {
        this.dataSugestao = dataSugestao;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
