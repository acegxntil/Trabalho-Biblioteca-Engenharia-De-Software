package com.sgbu.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "emprestimo")
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_exemplar", nullable = false)
    private Exemplar exemplar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDate dataEmprestimo;

    @Column(nullable = false)
    private LocalDate dataPrevistaDevolucao;

    private LocalDate dataRealDevolucao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @OneToOne(mappedBy = "emprestimo", fetch = FetchType.LAZY)
    private Multa multa;

    public enum Status {
        ATIVO, DEVOLVIDO, ATRASADO
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Exemplar getExemplar() {
        return exemplar;
    }

    public void setExemplar(Exemplar exemplar) {
        this.exemplar = exemplar;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public void setDataPrevistaDevolucao(LocalDate dataPrevistaDevolucao) {
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
    }

    public LocalDate getDataRealDevolucao() {
        return dataRealDevolucao;
    }

    public void setDataRealDevolucao(LocalDate dataRealDevolucao) {
        this.dataRealDevolucao = dataRealDevolucao;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Multa getMulta() {
        return multa;
    }

    public void setMulta(Multa multa) {
        this.multa = multa;
    }
}
