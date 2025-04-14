package com.deustermix.restapi.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "libro")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(nullable = false)
    private String isbn;

    @ManyToMany
    @JoinTable(
        name = "libro_receta",
        joinColumns = @JoinColumn(name = "libro_id"),
        inverseJoinColumns = @JoinColumn(name = "receta_id")
    )
    private List<Receta> recetas;

    // No-argument constructor
    public Libro() {
    }

    // All-argument constructor (optional, for convenience)
    public Libro(Long id, String titulo, Cliente cliente, String isbn, List<Receta> recetas) {
        this.id = id;
        this.titulo = titulo;
        this.cliente = cliente;
        this.isbn = isbn;
        this.recetas = recetas;
    }

    // Getters and setters
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setAutor(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public List<Receta> getRecetas() {
        return recetas;
    }

    public void setRecetas(List<Receta> recetas) {
        this.recetas = recetas;
    }

    // toString method
    @Override
    public String toString() {
        return "Libro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", cliente='" + cliente + '\'' +
                ", isbn='" + isbn + '\'' +
                ", recetas=" + recetas +
                '}';
    }
}
