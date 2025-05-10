package com.deustermix.restapi.dto;

import java.util.List;

public class RecetaDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private List<Long> idIngredientes;
    private ClienteReducidoDTO cliente;

    public RecetaDTO() {
    }

    public RecetaDTO(Long id, String nombre, String descripcion, List<Long> idIngredientes, ClienteReducidoDTO cliente) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.idIngredientes = idIngredientes;
        this.cliente = cliente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Long> getIdIngredientes() {
        return idIngredientes;
    }

    public void setIdIngredientes(List<Long> idIngredientes) {
        this.idIngredientes = idIngredientes;
    }

    public ClienteReducidoDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteReducidoDTO cliente) {
        this.cliente = cliente;
    }
}