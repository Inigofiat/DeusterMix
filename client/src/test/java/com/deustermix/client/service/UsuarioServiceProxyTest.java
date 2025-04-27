package com.deustermix.client.service;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import com.deustermix.client.data.Credenciales;
import com.deustermix.client.data.Usuario;


class UsuarioServiceProxyTest {


    @Mock
    private RestTemplate restTemplate;


    @InjectMocks
    private UsuarioServiceProxy usuarioServiceProxy;


    private final String API_BASE_URL = "http://localhost:8080";


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuarioServiceProxy.apiBaseUrl = API_BASE_URL;
    }


    @Test
    void testRegistrar_Success() {
        Usuario usuario = new Usuario("12345678A", "John", "Doe", "johndoe", "john.doe@example.com", "password123");
        String url = API_BASE_URL + "/auth/registro";


        // Simula que no se lanza ninguna excepción al realizar la solicitud
        when(restTemplate.postForObject(eq(url), eq(usuario), eq(Void.class))).thenReturn(null);


        // Verifica que no se lanza ninguna excepción al registrar el usuario
        assertDoesNotThrow(() -> usuarioServiceProxy.registrar(usuario));


        // Verifica que el método postForObject fue llamado con los argumentos correctos
        verify(restTemplate).postForObject(url, usuario, Void.class);
    }


    @Test
    void testRegistrar_BadRequest() {
        Usuario usuario = new Usuario("12345678A", "John", "Doe", "johndoe", "john.doe@example.com", "password123");
        String url = API_BASE_URL + "/auth/registro";


        when(restTemplate.postForObject(eq(url), eq(usuario), eq(Void.class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));


        RuntimeException exception = assertThrows(RuntimeException.class, () -> usuarioServiceProxy.registrar(usuario));
        assertEquals("Registro erroneo: Datos no validos", exception.getMessage());
    }


    @Test
    void testRegistrar_UserAlreadyExists() {
        Usuario usuario = new Usuario("12345678A", "John", "Doe", "johndoe", "john.doe@example.com", "password123");
        String url = API_BASE_URL + "/auth/registro";


        when(restTemplate.postForObject(eq(url), eq(usuario), eq(Void.class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.METHOD_NOT_ALLOWED));


        RuntimeException exception = assertThrows(RuntimeException.class, () -> usuarioServiceProxy.registrar(usuario));
        assertEquals("Registro erroneo: El usuario ya existe", exception.getMessage());
    }


    @Test
    void testLogin_Success() {
        Credenciales credenciales = new Credenciales("testuser", "password");
        String url = API_BASE_URL + "/auth/login";
        String token = "test-token";


        when(restTemplate.postForObject(eq(url), eq(credenciales), eq(String.class))).thenReturn(token);


        String result = usuarioServiceProxy.login(credenciales);
        assertEquals(token, result);
    }


    @Test
    void testLogin_BadRequest() {
        Credenciales credenciales = new Credenciales("testuser", "password");
        String url = API_BASE_URL + "/auth/login";


        when(restTemplate.postForObject(eq(url), eq(credenciales), eq(String.class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));


        RuntimeException exception = assertThrows(RuntimeException.class, () -> usuarioServiceProxy.login(credenciales));
        assertEquals("Login erroneo: Credenciales no validas", exception.getMessage());
    }


    @Test
    void testLogout_Success() {
        String token = "test-token";
        String url = String.format("%s/auth/logout?userToken=%s", API_BASE_URL, token);


        // Simula que no se lanza ninguna excepción al realizar la solicitud
        when(restTemplate.postForObject(eq(url), eq(token), eq(Void.class))).thenReturn(null);


        // Verifica que no se lanza ninguna excepción al cerrar sesión
        assertDoesNotThrow(() -> usuarioServiceProxy.logout(token));


        // Verifica que el método postForObject fue llamado con los argumentos correctos
        verify(restTemplate).postForObject(url, token, Void.class);
    }


    @Test
    void testLogout_BadRequest() {
        String token = "test-token";
        String url = String.format("%s/auth/logout?userToken=%s", API_BASE_URL, token);


        when(restTemplate.postForObject(eq(url), eq(token), eq(Void.class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));


        RuntimeException exception = assertThrows(RuntimeException.class, () -> usuarioServiceProxy.logout(token));
        assertEquals("Logout erroneo: Token no valido", exception.getMessage());
    }


    @Test
    void testGetDetalleUsuario_Success() {
        String token = "test-token";
        Usuario usuario = new Usuario("12345678A", "John", "Doe", "johndoe", "john.doe@example.com", "password123");
        String url = String.format("%s/api/usuario?tokenUsuario=%s", API_BASE_URL, token);


        when(restTemplate.getForObject(eq(url), eq(Usuario.class))).thenReturn(usuario);


        Usuario result = usuarioServiceProxy.getDetalleUsuario(token);
        assertEquals(usuario, result);
    }


    @Test
    void testGetDetalleUsuario_BadRequest() {
        String token = "test-token";
        String url = String.format("%s/api/usuario?tokenUsuario=%s", API_BASE_URL, token);


        when(restTemplate.getForObject(eq(url), eq(Usuario.class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));


        RuntimeException exception = assertThrows(RuntimeException.class, () -> usuarioServiceProxy.getDetalleUsuario(token));
        assertEquals("No autorizado: Email no valido", exception.getMessage());
    }


    @Test
    void testGetDetalleUsuario_UserNotFound() {
        String token = "test-token";
        String url = String.format("%s/api/usuario?tokenUsuario=%s", API_BASE_URL, token);


        when(restTemplate.getForObject(eq(url), eq(Usuario.class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.METHOD_NOT_ALLOWED));


        RuntimeException exception = assertThrows(RuntimeException.class, () -> usuarioServiceProxy.getDetalleUsuario(token));
        assertEquals("Usuario no encontrado", exception.getMessage());
    }
}