package com.deustermix.client.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.deustermix.client.data.Cliente;
import com.deustermix.client.data.Credenciales;
import com.deustermix.client.data.Libro;
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
    public void logout(String token) {
        String url = String.format("%s/auth/logout?userToken=%s", apiBaseUrl, token);
        try {            
            restTemplate.postForObject(url, null, Void.class);
        } catch (HttpStatusCodeException e) {
            switch (e.getStatusCode().value()) {
                case 400 -> throw new RuntimeException("Logout erroneo: Token no válido");
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
    public Cliente getDetalleCliente(String token) {
        try {
            String url = String.format("%s/api/cliente?tokenUsuario=%s", apiBaseUrl, token);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Cliente> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Cliente.class
            );
            
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return null;
            }
            throw new RuntimeException("Error al obtener detalle del cliente: " + e.getMessage());
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
    public List<Receta> getRecetas(String token) {
        return getRecetas();
    }

    @Override
public Receta obtenerReceta(Long idReceta) {
    String url = String.format("%s/api/recetas/%d", apiBaseUrl, idReceta);
    
    try {
        ResponseEntity<Receta> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            Receta.class
        );
        return response.getBody();
    } catch (HttpStatusCodeException e) {
        switch (e.getStatusCode().value()) {
            case 404 -> throw new RuntimeException("Receta no encontrada");
            default -> throw new RuntimeException("Fallo obteniendo receta: " + e.getStatusText());
        }
    }
}

// Método sobrecargado para incluir token
public Receta obtenerReceta(String token, Long idReceta) {
    String url = String.format("%s/api/recetas/%d?tokenUsuario=%s", apiBaseUrl, idReceta, token);
    
    try {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<Receta> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            Receta.class
        );
        return response.getBody();
    } catch (HttpStatusCodeException e) {
        switch (e.getStatusCode().value()) {
            case 401 -> throw new RuntimeException("No autorizado: Token no válido");
            case 404 -> throw new RuntimeException("Receta no encontrada");
            default -> throw new RuntimeException("Fallo obteniendo receta: " + e.getStatusText());
        }
    }
}

     @Override
    public void guardarReceta(String token, Long idReceta) {
        String url = String.format("%s/api/recetas/guardar/%d?tokenUsuario=%s", apiBaseUrl, idReceta, token);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        try {
            restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                Void.class
            );
        } catch (HttpStatusCodeException e) {
            switch (e.getStatusCode().value()) {
                case 401 -> throw new RuntimeException("No autorizado: Token no válido");
                case 404 -> throw new RuntimeException("Receta no encontrada");
                default -> throw new RuntimeException("Error al guardar la receta: " + e.getStatusText());
            }
        }
    }
    
    @Override
    public void eliminarRecetaFavorita(String token, Long idReceta) {
        String url = String.format("%s/api/recetas/favorito/%d?tokenUsuario=%s", apiBaseUrl, idReceta, token);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        try {
            restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                entity,
                Void.class
            );
        } catch (HttpStatusCodeException e) {
            switch (e.getStatusCode().value()) {
                case 401 -> throw new RuntimeException("No autorizado: Token no válido");
                case 404 -> throw new RuntimeException("Receta no encontrada");
                default -> throw new RuntimeException("Error al eliminar la receta de favoritos: " + e.getStatusText());
            }
        }
    }

    @Override
    public List<Receta> getRecetasGuardadas(String token) {
        String url = String.format("%s/api/cliente/recetas/guardadas?tokenUsuario=%s", apiBaseUrl, token);
        
        try {
            System.out.println("[Proxy] Solicitando recetas guardadas con token: " + token);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + token); // Añadir token en header
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<List<Receta>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Receta>>() {}
            );
            
            List<Receta> recetas = response.getBody();
            System.out.println("[Proxy] Respuesta exitosa, recetas obtenidas: " + 
                (recetas != null ? recetas.size() : 0));
            return recetas != null ? recetas : List.of();
            
        } catch (HttpStatusCodeException e) {
            System.err.println("[Proxy] Error HTTP: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            return List.of(); // Devolver lista vacía en lugar de error
        } catch (Exception e) {
            System.err.println("[Proxy] Error general: " + e.getMessage());
            return List.of(); // Devolver lista vacía en lugar de error
        }
    }

        @Override
    public List<Libro> getLibros() {
        String url = String.format("%s/api/libros", apiBaseUrl);
        
        try {
            ResponseEntity<List<Libro>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Libro>>() {}
            );
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("Fallo obteniendo libros: " + e.getStatusText());
        }
    }
    
    public List<Libro> getLibros(String token) {
        return getLibros();
    }

    
    @Override
public Libro obtenerLibro(Long idLibro) {
    String url = String.format("%s/api/libros/%d", apiBaseUrl, idLibro);
    
    try {
        ResponseEntity<Libro> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            Libro.class
        );
        return response.getBody();
    } catch (HttpStatusCodeException e) {
        switch (e.getStatusCode().value()) {
            case 404 -> throw new RuntimeException("Libro no encontrada");
            default -> throw new RuntimeException("Fallo obteniendo libro: " + e.getStatusText());
        }
    }
}
    
    public Libro obtenerLibro(String token, Long idLibro) {
    String url = String.format("%s/api/libros/%d?tokenUsuario=%s", apiBaseUrl, idLibro, token);
    
    try {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<Libro> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            Libro.class
        );
        return response.getBody();
    } catch (HttpStatusCodeException e) {
        switch (e.getStatusCode().value()) {
            case 401 -> throw new RuntimeException("No autorizado: Token no válido");
            case 404 -> throw new RuntimeException("Libro no encontrada");
            default -> throw new RuntimeException("Fallo obteniendo libro: " + e.getStatusText());
        }
    }
}


    public void guardarLibro(String token, Long idLibro) {
    String url = String.format("%s/api/libros/guardar/%d?tokenUsuario=%s", apiBaseUrl, idLibro, token);
    
    try {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        // Crear una entidad HTTP con los encabezados
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        System.out.println("Enviando petición a: " + url);
        
        // Realizar la solicitud POST al endpoint
        ResponseEntity<Void> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            entity,
            Void.class
        );
        
        System.out.println("Respuesta recibida: " + response.getStatusCode());
        
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Error al guardar el libro: código de estado " + response.getStatusCode());
        }
    } catch (HttpStatusCodeException e) {
        System.err.println("Error HTTP al guardar el libro: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        switch (e.getStatusCode().value()) {
            case 401 -> throw new RuntimeException("No autorizado: Token no válido");
            case 404 -> throw new RuntimeException("Libro no encontrado");
            default -> throw new RuntimeException("Error al guardar el libro: " + e.getStatusText());
        }
    } catch (Exception e) {
        System.err.println("Error general al guardar el libro: " + e.getMessage());
        e.printStackTrace();
        throw new RuntimeException("Error al guardar el libro: " + e.getMessage(), e);
    }
}
    
    @Override
    public List<Libro> getLibrosComprados(String token) {
        String url = String.format("%s/api/cliente/libros/comprados?tokenUsuario=%s", apiBaseUrl, token);
        
        try {
            System.out.println("[Proxy] Solicitando libros comprados con token: " + token);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<List<Libro>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Libro>>() {}
            );
            
            List<Libro> libros = response.getBody();
            System.out.println("[Proxy] Respuesta exitosa, libros obtenidos: " + 
                (libros != null ? libros.size() : 0));
            return libros != null ? libros : List.of();
            
        } catch (HttpStatusCodeException e) {
            System.err.println("[Proxy] Error HTTP: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            return List.of();
        } catch (Exception e) {
            System.err.println("[Proxy] Error general: " + e.getMessage());
            return List.of();
        }
    }

    @Override
public Boolean verificarLibroComprado(String token, Long idLibro) {
    String url = String.format("%s/api/usuario/tiene-libro/%d?tokenUsuario=%s", apiBaseUrl, idLibro, token);
    
    try {
        System.out.println("[Proxy] Verificando libro comprado. ID: " + idLibro);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<Boolean> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            Boolean.class
        );
        
        return response.getBody() != null ? response.getBody() : false;
    } catch (HttpStatusCodeException e) {
        System.err.println("[Proxy] Error HTTP: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        return false;
    } catch (Exception e) {
        System.err.println("[Proxy] Error general: " + e.getMessage());
        return false;
    }
}
}