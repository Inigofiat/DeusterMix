package com.deustermix.restapi.service;

import com.deustermix.restapi.model.Cliente;
import com.deustermix.restapi.model.Usuario;
import com.deustermix.restapi.repository.ClienteRepository;
import com.deustermix.restapi.repository.UsuarioRepository;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ServiceInicioSesion {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;

    public ServiceInicioSesion(UsuarioRepository usuarioRepository, ClienteRepository clienteRepository) {
        this.usuarioRepository = usuarioRepository;
        this.clienteRepository = clienteRepository;
    }

    private final static Map<String, Usuario> almacenToken = new HashMap<>();
    
    public String login(String email, String contrasena) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        if (usuario == null || !usuario.getContrasena().equals(contrasena)) {
            return null;
        }
        String token = generarToken();
        almacenToken.put(token, usuario);
        return token;
    }

    public boolean logout(String token) {
        if (almacenToken.containsKey(token)) {
            almacenToken.remove(token);
            return true;
        } else {
            return false;
        }
    }

    private static synchronized String generarToken() {
        return Long.toHexString(System.currentTimeMillis());
    }

    public Usuario getUsuarioByToken(String token) {
        return almacenToken.get(token);
    }

    public Cliente getClienteByToken(String token) {
        try {
            Usuario usuario = almacenToken.get(token);
            if (usuario == null) {
                return null;
            }
            
            return clienteRepository.findByEmail(usuario.getEmail())
                .orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean esTokenValido(String token) {
        return almacenToken.containsKey(token);
    }

    public boolean actualizarUsuario(Usuario usuario, String token) {
        almacenToken.put(token, usuario);
        usuarioRepository.save(usuario);
        return true;
    }
}
