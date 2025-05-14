package com.deustermix.restapi.dto;

import java.util.List;


public class LibroDTO {
    private Long id;
    private String titulo;
    private String isbn;
    private double precio;
    private List<Long> idRecetas;
    private List<RecetaDTO> recetas;
    private ClienteReducidoDTO cliente;

    public LibroDTO() {
    }

    public LibroDTO(Long id, String titulo, String isbn, double precio, List<Long> idRecetas, ClienteReducidoDTO cliente) {
        this.id = id;
        this.titulo = titulo;
        this.isbn = isbn;
        this.precio = precio;
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

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public List<Long> getIdRecetas() {
        return idRecetas;
    }

    public void setIdRecetas(List<Long> idRecetas) {
        this.idRecetas = idRecetas;
    }

    public List<RecetaDTO> getRecetas() {
        return recetas;
    }  

    public void setRecetas(List<RecetaDTO> recetas) {
        this.recetas = recetas;
    }

    public ClienteReducidoDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteReducidoDTO cliente) {
        this.cliente = cliente;
    }
}