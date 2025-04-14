package com.deustermix.restapi.dto;

public class CredencialesDTO {

    private String email;
    private String contrasena;

    
    public CredencialesDTO() {
    }

    public CredencialesDTO(String email, String contrasena) {
        this.email = email;
        this.contrasena = contrasena;
    }

  
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    @Override
    public String toString() {
        return "CredencialesDTO{" +
                "email='" + email + '\'' +
                ", contrasena='" + contrasena + '\'' +
                '}';
    }
}
