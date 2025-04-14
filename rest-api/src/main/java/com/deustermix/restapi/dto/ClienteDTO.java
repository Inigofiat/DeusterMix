package com.deustermix.restapi.dto;

import java.util.List;

import com.deustermix.restapi.model.Libro;
import com.deustermix.restapi.model.Receta;
import com.deustermix.restapi.model.Reporte;

public class ClienteDTO {
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
}
