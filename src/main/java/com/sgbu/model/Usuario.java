package com.sgbu.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false, unique = true)
    private String cpf;

    private String matricula;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPerfil tipoPerfil;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusUsuario status;

    @Column(nullable = false)
    private LocalDate dataCadastro;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<Emprestimo> emprestimos = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<Reserva> reservas = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<SugestaoAquisicao> sugestoes = new ArrayList<>();

    public enum TipoPerfil {
        ESTUDANTE, PROFESSOR, BIBLIOTECARIO, ADMIN
    }

    public enum StatusUsuario {
        ATIVO, INATIVO
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public TipoPerfil getTipoPerfil() {
        return tipoPerfil;
    }

    public void setTipoPerfil(TipoPerfil tipoPerfil) {
        this.tipoPerfil = tipoPerfil;
    }

    public StatusUsuario getStatus() {
        return status;
    }

    public void setStatus(StatusUsuario status) {
        this.status = status;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public List<Emprestimo> getEmprestimos() {
        return emprestimos;
    }

    public void setEmprestimos(List<Emprestimo> emprestimos) {
        this.emprestimos = emprestimos;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    public List<SugestaoAquisicao> getSugestoes() {
        return sugestoes;
    }

    public void setSugestoes(List<SugestaoAquisicao> sugestoes) {
        this.sugestoes = sugestoes;
    }
}
