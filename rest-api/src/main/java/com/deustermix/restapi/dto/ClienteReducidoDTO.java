package com.deustermix.restapi.dto;

/**
 * DTO simplificado para el Cliente que evita referencias circulares
 */
public class ClienteReducidoDTO {
    private String email;
    private String nombre;

    public ClienteReducidoDTO() {
    }

    public ClienteReducidoDTO(String email, String nombre) {
        this.email = email;
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}