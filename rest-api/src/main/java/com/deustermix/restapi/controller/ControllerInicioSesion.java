package com.deustermix.restapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deustermix.restapi.dto.CredencialesDTO;
import com.deustermix.restapi.dto.UsuarioDTO;
import com.deustermix.restapi.model.Usuario;
import com.deustermix.restapi.service.ServiceInicioSesion;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;



@RestController
@RequestMapping("/auth")
@Tag(name = "Controlador Inicio Sesion", description = "Operaciones de Login y Logout")
public class ControllerInicioSesion {

    @Autowired
    private ServiceInicioSesion serviceInicioSesion;

    public ControllerInicioSesion(ServiceInicioSesion serviceInicioSesion) {
        this.serviceInicioSesion = serviceInicioSesion;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
        @RequestBody CredencialesDTO credenciales) {
        String token = serviceInicioSesion.login(credenciales.getEmail(), credenciales.getContrasena());
        if (token == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(        
    @Parameter(name = "userToken", description = "Token del usuario", required = true, example = "1111111111")
    @RequestParam("userToken") String userToken) {
        boolean logueado = serviceInicioSesion.logout(userToken);
        if (!logueado) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
        
    public Usuario parseDTOToUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario(usuarioDTO.getDni(), usuarioDTO.getNombre(), usuarioDTO.getApellido(), usuarioDTO.getNombreUsuario(), usuarioDTO.getEmail(), usuarioDTO.getContrasena());
        return usuario;   
    }
}

