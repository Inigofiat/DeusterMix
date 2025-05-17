package com.deustermix.restapi.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cliente")
public class Cliente extends Usuario {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "cliente_recetas_guardadas", 
        joinColumns = @JoinColumn(name = "cliente_id"),
        inverseJoinColumns = @JoinColumn(name = "receta_id")
    )
    private List<Receta> recetas;

    @ManyToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)    
    private List<Libro> libros;

    @Column(nullable = false)
    private String direccion;

    public Cliente() {
        super();
    }

    public Cliente(String dni, String nombre, String apellido, String email, String nombreUsuario, String contrasena) {
        super(dni, nombre, apellido, email, nombreUsuario, contrasena);
    }

    public Cliente(String dni, String nombre, String apellido, String email, String nombreUsuario, String contrasena, 
    List<Receta> recetas, List<Libro> libros, String direccion) {
        super(dni, nombre, apellido, email, nombreUsuario, contrasena);
        this.recetas = recetas;
        this.libros = libros;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void aniadirReceta(Receta receta) {
        if (this.recetas == null) {
            this.recetas = new ArrayList<>();
     }
        if (!this.recetas.contains(receta)) {
            this.recetas.add(receta);
        }
    }

    public void aniadirLibro(Libro libro) {
        if (this.libros == null) {
            this.libros = new ArrayList<>();
     }
        if (!this.libros.contains(libro)) {
            this.libros.add(libro);
        }
    }
}