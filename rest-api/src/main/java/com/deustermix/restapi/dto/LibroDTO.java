package com.deustermix.restapi.dto;

import java.util.List;

import com.deustermix.restapi.model.Cliente;

public class LibroDTO {
    private Long id;
    private String titulo;
    private String isbn;
    private List<Long> idRecetas;
    private Cliente cliente;

    public LibroDTO() {
    }

    public LibroDTO(Long id, String titulo, String isbn, List<Long> idRecetas, Cliente cliente) {
        this.id = id;
        this.titulo = titulo;
        this.isbn = isbn;
        this.idRecetas = idRecetas;
        this.cliente = cliente;
    }

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

    public List<Long> getIdRecetas() {
        return idRecetas;
    }

    public void setIdRecetas(List<Long> idRecetas) {
        this.idRecetas = idRecetas;
    }

    public Cliente getClienteDTO() {
        return cliente;
    }

    public void setClienteDTO(Cliente cliente) {
        this.cliente = cliente;
    }
}