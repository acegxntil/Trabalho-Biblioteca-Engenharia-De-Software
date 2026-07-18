package com.sgbu.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "multa")
public class Multa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_emprestimo", nullable = false)
    private Emprestimo emprestimo;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Column(nullable = false)
    private LocalDate dataGeracao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    private LocalDate dataQuitacao;

    public enum Status {
        PENDENTE, QUITADA
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Emprestimo getEmprestimo() {
        return emprestimo;
    }

    public void setEmprestimo(Emprestimo emprestimo) {
        this.emprestimo = emprestimo;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public LocalDate getDataGeracao() {
        return dataGeracao;
    }

    public void setDataGeracao(LocalDate dataGeracao) {
        this.dataGeracao = dataGeracao;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getDataQuitacao() {
        return dataQuitacao;
    }

    public void setDataQuitacao(LocalDate dataQuitacao) {
        this.dataQuitacao = dataQuitacao;
    }
}
