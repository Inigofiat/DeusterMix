package com.deustermix.restapi.service;

import com.deustermix.restapi.model.Usuario;
import com.deustermix.restapi.repository.UsuarioRepository;

import org.springframework.stereotype.Service;


@Service
public class ServiceRegistro {
    private UsuarioRepository usuarioRepository;

    public ServiceRegistro(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public boolean registrar(Usuario usuario) {
    	if (usuario != null && !usuarioRepository.existsByEmail(usuario.getEmail())) {
    		usuarioRepository.save(usuario);
    		return true;
    	}
    	return false;
    }
}
