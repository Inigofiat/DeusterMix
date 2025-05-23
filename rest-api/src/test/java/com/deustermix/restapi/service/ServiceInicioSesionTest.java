package com.deustermix.restapi.service;


import com.deustermix.restapi.model.Usuario;
import com.deustermix.restapi.model.Cliente;


import com.deustermix.restapi.repository.UsuarioRepository;
import com.deustermix.restapi.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ServiceInicioSesionTest {


    @Mock
    private UsuarioRepository usuarioRepository;
   
    @Mock
    private ClienteRepository clienteRepository;


    @InjectMocks
    private ServiceInicioSesion serviceInicioSesion;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testLoginSuccess() {
        String email = "test@example.com";
        String contrasena = "password";
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setContrasena(contrasena);


        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));


        String token = serviceInicioSesion.login(email, contrasena);


        assertNotNull(token);
        assertTrue(serviceInicioSesion.esTokenValido(token));
        verify(usuarioRepository, times(1)).findByEmail(email);
    }


    @Test
    void testLoginFailureInvalidEmail() {
        String email = "invalid@example.com";
        String contrasena = "password";


        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());


        String token = serviceInicioSesion.login(email, contrasena);


        assertNull(token);
        verify(usuarioRepository, times(1)).findByEmail(email);
    }


    @Test
    void testLoginFailureInvalidPassword() {
        String email = "test@example.com";
        String contrasena = "wrongPassword";
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setContrasena("password");


        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));


        String token = serviceInicioSesion.login(email, contrasena);


        assertNull(token);
        verify(usuarioRepository, times(1)).findByEmail(email);
    }


    @Test
    void testLogoutSuccess() {
        String email = "test@example.com";
        String contrasena = "password";
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setContrasena(contrasena);


        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));


        String token = serviceInicioSesion.login(email, contrasena);


        boolean result = serviceInicioSesion.logout(token);


        assertTrue(result);
        assertFalse(serviceInicioSesion.esTokenValido(token));
    }


    @Test
    void testLogoutFailure() {
        String token = "invalidToken";


        boolean result = serviceInicioSesion.logout(token);


        assertFalse(result);
    }


    @Test
    void testGetUsuarioByToken() {
        String email = "test@example.com";
        String contrasena = "password";
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setContrasena(contrasena);


        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));


        String token = serviceInicioSesion.login(email, contrasena);


        Usuario result = serviceInicioSesion.getUsuarioByToken(token);


        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }


    @Test
    void testActualizarUsuario() {
        String token = "validToken";
        Usuario usuario = new Usuario();
        usuario.setEmail("test@example.com");


        when(usuarioRepository.save(usuario)).thenReturn(usuario);


        boolean result = serviceInicioSesion.actualizarUsuario(usuario, token);


        assertTrue(result);
        assertEquals(usuario, serviceInicioSesion.getUsuarioByToken(token));


        verify(usuarioRepository, times(1)).save(usuario);
    }


    @Test
    void testGetClienteByTokenWithValidClienteToken() {
        String email = "cliente@example.com";
        String contrasena = "password";
       
        Cliente cliente = new Cliente();
        cliente.setEmail(email);
        cliente.setContrasena(contrasena);
       
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(cliente));
        when(clienteRepository.findByEmail(email)).thenReturn(Optional.of(cliente));
       
        String token = serviceInicioSesion.login(email, contrasena);
       
        Cliente result = serviceInicioSesion.getClienteByToken(token);
       
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertTrue(result instanceof Cliente);
    }


    @Test
    void testGetClienteByTokenWithNonClienteToken() {
        String email = "admin@example.com";
        String contrasena = "password";
       
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setContrasena(contrasena);
       
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
        when(clienteRepository.findByEmail(email)).thenReturn(Optional.empty());
       
        String token = serviceInicioSesion.login(email, contrasena);
       
        Cliente result = serviceInicioSesion.getClienteByToken(token);
       
        assertNull(result);
    }


    @Test
    void testGetClienteByTokenWithInvalidToken() {
        String invalidToken = "invalid-token";
       
        Cliente result = serviceInicioSesion.getClienteByToken(invalidToken);
        assertNull(result);
    }
}
