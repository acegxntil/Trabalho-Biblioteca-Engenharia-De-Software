package com.sgbu.model;

import jakarta.persistence.*;

@Entity
@Table(name = "exemplar")
public class Exemplar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_livro", nullable = false)
    private Livro livro;

    @Column(nullable = false, unique = true)
    private String numeroTombo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    public enum Status {
        DISPONIVEL, EMPRESTADO, RESERVADO, MANUTENCAO
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public String getNumeroTombo() {
        return numeroTombo;
    }

    public void setNumeroTombo(String numeroTombo) {
        this.numeroTombo = numeroTombo;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
