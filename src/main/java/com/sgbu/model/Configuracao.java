package com.sgbu.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "configuracao")
public class Configuracao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer prazoEmprestimoDias;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorMultaDia;

    @Column(nullable = false)
    private Integer prazoRetiradaReservaDias;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPrazoEmprestimoDias() {
        return prazoEmprestimoDias;
    }

    public void setPrazoEmprestimoDias(Integer prazoEmprestimoDias) {
        this.prazoEmprestimoDias = prazoEmprestimoDias;
    }

    public BigDecimal getValorMultaDia() {
        return valorMultaDia;
    }

    public void setValorMultaDia(BigDecimal valorMultaDia) {
        this.valorMultaDia = valorMultaDia;
    }

    public Integer getPrazoRetiradaReservaDias() {
        return prazoRetiradaReservaDias;
    }

    public void setPrazoRetiradaReservaDias(Integer prazoRetiradaReservaDias) {
        this.prazoRetiradaReservaDias = prazoRetiradaReservaDias;
    }
}
