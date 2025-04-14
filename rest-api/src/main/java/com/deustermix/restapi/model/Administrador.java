package com.deustermix.restapi.model;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "administrador")
public class Administrador extends Usuario {

    @ManyToMany
    @JoinTable(
        name = "administrador_reporte",
        joinColumns = @JoinColumn(name = "administrador_id"),
        inverseJoinColumns = @JoinColumn(name = "reporte_id")
    )
    private List<Reporte> reportesRevisados;

    public Administrador() {
        super();
    }

    public Administrador(String dni, String nombre, String apellido, String email, String nombreUsuario, String contrasena) {
        super(dni, nombre, apellido, email, nombreUsuario, contrasena);
    }

    public Administrador(String dni, String nombre, String apellido, String email, String nombreUsuario, String contrasena,
                         List<Reporte> reportesRevisados) {
        super(dni, nombre, apellido, email, nombreUsuario, contrasena);
        this.reportesRevisados = reportesRevisados;
    }

    // Getter and Setter
    public List<Reporte> getReportesRevisados() {
        return reportesRevisados;
    }

    public void setReportesRevisados(List<Reporte> reportesRevisados) {
        this.reportesRevisados = reportesRevisados;
    }

    // toString method
    @Override
    public String toString() {
        return "Administrador{" +
                "reportesRevisados=" + reportesRevisados +
                ", dni='" + getDni() + '\'' +
                ", nombre='" + getNombre() + '\'' +
                ", apellido='" + getApellido() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", nombreUsuario='" + getNombreUsuario() + '\'' +
                ", contrasena='" + getContrasena() + '\'' +
                '}';
    }
}
