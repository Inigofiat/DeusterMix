package com.deustermix.restapi.dto;

import java.util.List;

import com.deustermix.restapi.model.Libro;
import com.deustermix.restapi.model.Receta;
import com.deustermix.restapi.model.Reporte;

public class ClienteDTO {
    private String nombre;
    private String apellido;
    private String nombreUsuario;
    private String email;
    private List<Receta> recetas;
    private List<Libro> libros;
    private List<Reporte> reportes;
    private String direccion;

    public ClienteDTO(List<Receta> recetas, List<Libro> libros, List<Reporte> reportes, String direccion) {
        this.recetas = recetas;
        this.libros = libros;
        this.reportes = reportes;
        this.direccion = direccion;
    }

    public ClienteDTO(String nombre, String apellido, String nombreUsuario, String email, String direccion) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.direccion = direccion;
    }

    public List<Receta> getRecetas() {
        return recetas;
    }

    public void setRecetas(List<Receta> recetas) {
        this.recetas = recetas;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    public List<Reporte> getReportes() {
        return reportes;
    }

    public void setReportes(List<Reporte> reportes) {
        this.reportes = reportes;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombre() { 
        return nombre; 
    }

    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }

    public String getApellido() { 
        return apellido; 
    }

    public void setApellido(String apellido) { 
        this.apellido = apellido; 
    }

    public String getNombreUsuario() { 
        return nombreUsuario; 
    }

    public void setNombreUsuario(String nombreUsuario) { 
        this.nombreUsuario = nombreUsuario; 
    }

    public String getEmail() { 
        return email; 
    }

    public void setEmail(String email) { 
        this.email = email; 
    }
}
