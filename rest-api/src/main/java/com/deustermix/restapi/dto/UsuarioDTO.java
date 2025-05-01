package com.deustermix.restapi.dto;

public class UsuarioDTO {
    private String dni;
    private String nombre;
    private String apellido;
    private String nombreUsuario;
    private String contrasena;
    private String email;

    public UsuarioDTO(String dni, String nombre, String apellido, String nombreUsuario, String contrasena, String email) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.email = email;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
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

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        UsuarioDTO other = (UsuarioDTO) obj;
        return dni.equals(other.dni) && 
                nombre.equals(other.nombre) && 
                apellido.equals(other.apellido) && 
                nombreUsuario.equals(other.nombreUsuario) && 
                contrasena.equals(other.contrasena) && 
                email.equals(other.email);
    }
}
