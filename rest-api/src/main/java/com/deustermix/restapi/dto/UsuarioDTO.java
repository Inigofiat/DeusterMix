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
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        UsuarioDTO other = (UsuarioDTO) obj;
        if (dni == null) {
            if (other.dni != null) {
                return false;
            }
        } else if (!dni.equals(other.dni)) {
            return false;
        }
        if (nombre == null) {
            if (other.nombre != null) {
                return false;
            }
        } else if (!nombre.equals(other.nombre)) {
            return false;
        }
        if (apellido == null) {
            if (other.apellido != null) {
                return false;
            }
        } else if (!apellido.equals(other.apellido)) {
            return false;
        }
        if (nombreUsuario == null) {
            if (other.nombreUsuario != null) {
                return false;
            }
        } else if (!nombreUsuario.equals(other.nombreUsuario)) {
            return false;
        }
        if (contrasena == null) {
            if (other.contrasena != null) {
                return false;
            }
        } else if (!contrasena.equals(other.contrasena)) {
            return false;
        }
        if (email == null) {
            if (other.email != null) {
                return false;
            }
        } else if (!email.equals(other.email)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dni == null) ? 0 : dni.hashCode());
        result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
        result = prime * result + ((apellido == null) ? 0 : apellido.hashCode());
        result = prime * result + ((nombreUsuario == null) ? 0 : nombreUsuario.hashCode());
        result = prime * result + ((contrasena == null) ? 0 : contrasena.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        return result;
    }
}