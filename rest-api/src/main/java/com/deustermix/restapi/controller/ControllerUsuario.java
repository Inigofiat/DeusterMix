package com.deustermix.restapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deustermix.restapi.dto.UsuarioDTO;
import com.deustermix.restapi.model.Usuario;
import com.deustermix.restapi.service.ServiceInicioSesion;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "LinkAuto Controller", description = "API for managing social media")
public class ControllerUsuario {

    @Autowired
    private final ServiceInicioSesion serviceInicioSesion;

    public ControllerUsuario(ServiceInicioSesion serviceInicioSesion) {
        this.serviceInicioSesion = serviceInicioSesion;
    }

    @GetMapping("/usuario")
    public ResponseEntity<UsuarioDTO> gtDetalleUsuario(
        @Parameter(name = "tokenUsuario", description = "Token del usuario", required = true, example = "1a2b3c4d5e")
        @RequestParam("tokenUsuario") String tokenUsuario) {
        Usuario usuario = serviceInicioSesion.getUsuarioByToken(tokenUsuario);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        UsuarioDTO usuarioDevuelto = parseUsuarioToDTO(usuario);
        return ResponseEntity.ok(usuarioDevuelto);
    }

    private UsuarioDTO parseUsuarioToDTO(Usuario usuario){
        return new UsuarioDTO(usuario.getDni(), usuario.getNombre(), usuario.getApellido(), usuario.getNombreUsuario(), usuario.getEmail(), usuario.getContrasena());
    }

}
