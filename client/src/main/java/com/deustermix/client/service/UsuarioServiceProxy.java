package com.deustermix.client.service;



import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.deustermix.client.data.Credenciales;
import com.deustermix.client.data.Receta;
import com.deustermix.client.data.Usuario;

@Service
public class UsuarioServiceProxy implements IDeusterMixServiceProxy {
    private final RestTemplate restTemplate;


    @Value("${api.base.url}")
    protected String apiBaseUrl;

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

    @Override
    public void crearReceta(String token, Receta receta) {
        String url = String.format("%s/api/recetas?tokenUsuario=%s", apiBaseUrl, token);
        
        try {
            restTemplate.postForObject(url, receta, Void.class);
        } catch (HttpStatusCodeException e) {
            switch (e.getStatusCode().value()) {
                case 401 -> throw new RuntimeException("Logout fallido: Token no valido");
                default -> throw new RuntimeException("Logout fallido: " + e.getStatusText());
            }
        } 
    }

    @Override
    public List<Receta> getRecetas() {
        String url = String.format("%s/api/recetas", apiBaseUrl);
        
        try {
            ResponseEntity<List<Receta>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Receta>>() {}
            );
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("Fallo obteniendo recetas: " + e.getStatusText());
        }
    }

    @Override
    public Receta obtenerReceta(Long idReceta) {
        String url = String.format("%s/api/recetas/%d", apiBaseUrl, idReceta);
        
        try {
            return restTemplate.getForObject(url, Receta.class);
        } catch (HttpStatusCodeException e) {
            switch (e.getStatusCode().value()) {
                case 404 -> throw new RuntimeException("Receta no encontarda");
                default -> throw new RuntimeException("Fallo obteniendo receta: " + e.getStatusText());
            }
        }
    }

    @Override
    public void eliminarReceta(String token, Long idReceta) {
        String url = String.format("%s/api/recetas/%d?tokenUsuario=%s", apiBaseUrl, idReceta, token);
        
        try {
            restTemplate.delete(url);
        } catch (HttpStatusCodeException e) {
            switch (e.getStatusCode().value()) {
                case 401 -> throw new RuntimeException("No autorizado: Token no valido");
                case 404 -> throw new RuntimeException("Receta no encontrada");
                default -> throw new RuntimeException("Fallo eliminando receta: " + e.getStatusText());
            }
        }
    }

    @Override
    public List<Receta> getRecetasDeUsuario(String email) {
        String url = String.format("%s/api/usuario/%s/recetas", apiBaseUrl, email);
        
        try {
            ResponseEntity<List<Receta>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Receta>>() {}
            );
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            switch (e.getStatusCode().value()) {
                case 404 -> throw new RuntimeException("Usuario no encontrado");
                default -> throw new RuntimeException("Falla obteniendo recetas de usuario: " + e.getStatusText());
            }
        }
    }

}
