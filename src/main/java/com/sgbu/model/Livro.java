package com.sgbu.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "livro")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, unique = true)
    private String isbn;

    private String editora;

    private Integer anoPublicacao;

    private String edicao;

    private String categoria;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    private String localizacaoPrateleira;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "livro_autor",
            joinColumns = @JoinColumn(name = "id_livro"),
            inverseJoinColumns = @JoinColumn(name = "id_autor"))
    private List<Autor> autores = new ArrayList<>();

    @OneToMany(mappedBy = "livro", fetch = FetchType.LAZY)
    private List<Exemplar> exemplares = new ArrayList<>();

    @OneToMany(mappedBy = "livro", fetch = FetchType.LAZY)
    private List<Reserva> reservas = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public Integer getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(Integer anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public String getEdicao() {
        return edicao;
    }

    public void setEdicao(String edicao) {
        this.edicao = edicao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLocalizacaoPrateleira() {
        return localizacaoPrateleira;
    }

    public void setLocalizacaoPrateleira(String localizacaoPrateleira) {
        this.localizacaoPrateleira = localizacaoPrateleira;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    public List<Exemplar> getExemplares() {
        return exemplares;
    }

    public void setExemplares(List<Exemplar> exemplares) {
        this.exemplares = exemplares;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }
}
