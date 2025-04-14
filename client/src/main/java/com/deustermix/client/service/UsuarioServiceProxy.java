package com.deustermix.client.service;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.deustermix.client.data.Credenciales;
import com.deustermix.client.data.Usuario;

@Service
public class UsuarioServiceProxy implements IDeusterMixServiceProxy {
    private final RestTemplate restTemplate;


    @Value("${api.base.url}")
    private String apiBaseUrl;

    public UsuarioServiceProxy(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    @Override
    public void registrar(Usuario usuario) {
        String url = apiBaseUrl + "/auth/registro";
        
        try {
            restTemplate.postForObject(url, usuario, Void.class);
        } catch (HttpStatusCodeException e) {
            switch (e.getStatusCode().value()) {
                case 400 -> throw new RuntimeException("Registro erroneo: Datos no validos");
                case 405 -> throw new RuntimeException("Registro erroneo: El usuario ya existe");
                default -> throw new RuntimeException("Registro erroneo: " + e.getStatusText());
            }
        }
    }

    @Override
    public String login(Credenciales credenciales) {
        String url = apiBaseUrl + "/auth/login";
            
        try {
            String token = restTemplate.postForObject(url, credenciales, String.class);
            return token;
        } catch (HttpStatusCodeException e) {
            switch (e.getStatusCode().value()) {
                case 400 -> throw new RuntimeException("Login erroneo: Credenciales no validas");
                default -> throw new RuntimeException("Login erroneo: " + e.getStatusText());
            }
        }
    }

    @Override
    public void logout(String tokenUsuario) {
        String url = String.format("%s/auth/logout?userToken=%s", apiBaseUrl, tokenUsuario);
        try {            
            restTemplate.postForObject(url, tokenUsuario, Void.class);
        } catch (HttpStatusCodeException e) {
            switch (e.getStatusCode().value()) {
                case 400 -> throw new RuntimeException("Logout erroneo: Token no valido");
                default -> throw new RuntimeException("Logout erroneo: " + e.getStatusText());
            }
        }
    }

    @Override
    public Usuario getDetalleUsuario(String token) {
        String url = String.format("%s/api/usuario?tokenUsuario=%s", apiBaseUrl, token);
        
        try {
            return restTemplate.getForObject(url, Usuario.class);
        } catch (HttpStatusCodeException e) {
            switch (e.getStatusCode().value()) {
                case 400 -> throw new RuntimeException("No autorizado: Email no valido");
                case 405 -> throw new RuntimeException("Usuario no encontrado");
                default -> throw new RuntimeException("Fallo al cargar perfil del usuario: " + e.getStatusText());
            }
        }
    }

}
