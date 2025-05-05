package com.deustermix.restapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.deustermix.restapi.dto.UsuarioDTO;
import com.deustermix.restapi.model.Usuario;
import com.deustermix.restapi.service.ServiceRegistro;


import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Controlador registro", description = "Opercaiones de registro")
public class ControllerRegistro {
    
    private final ServiceRegistro serviceRegistro;

    public ControllerRegistro(ServiceRegistro serviceRegistro) {
        this.serviceRegistro = serviceRegistro;  
    }

    @PostMapping("/registro")
    public ResponseEntity<Void> registrar(
        @RequestBody UsuarioDTO usuario) {
        Usuario u = parseUsuarioToDTO(usuario);
        boolean registrado = serviceRegistro.registrar(u);
        if (!registrado) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);   
    }

    public Usuario parseUsuarioToDTO(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario(
            usuarioDTO.getDni(), 
            usuarioDTO.getNombre(), 
            usuarioDTO.getApellido(), 
            usuarioDTO.getEmail(),      // Cambiado: ahora email va antes
            usuarioDTO.getNombreUsuario(), 
            usuarioDTO.getContrasena()
        );
        return usuario;   
    }
}
