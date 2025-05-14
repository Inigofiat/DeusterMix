package com.deustermix.restapi.dto;

import java.util.List;

public class RecetaDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String instrucciones;
    private String imagenUrl;
    private List<Long> idIngredientes;
    private List<IngredienteDTO> ingredientes; // Added field for complete ingredient information
    private ClienteReducidoDTO cliente;

    public RecetaDTO() {
    }

    public RecetaDTO(Long id, String nombre, String descripcion, String instrucciones, String imagenUrl, List<Long> idIngredientes, ClienteReducidoDTO cliente) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.instrucciones = instrucciones;
        this.imagenUrl = imagenUrl;
        this.idIngredientes = idIngredientes;
        this.cliente = cliente;
    }

    public RecetaDTO(Long id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
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

    public String getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public List<Long> getIdIngredientes() {
        return idIngredientes;
    }

    public void setIdIngredientes(List<Long> idIngredientes) {
        this.idIngredientes = idIngredientes;
    }
    
    // Getter and setter for the new ingredientes field
    public List<IngredienteDTO> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<IngredienteDTO> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public ClienteReducidoDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteReducidoDTO cliente) {
        this.cliente = cliente;
    }
}