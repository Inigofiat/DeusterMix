package com.deustermix.client.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.deustermix.client.data.Credenciales;
import com.deustermix.client.data.Usuario;
import com.deustermix.client.service.UsuarioServiceProxy;


import jakarta.servlet.http.HttpServletRequest;


public class ClienteControllerTest {


    private ClienteController clienteController;
    private UsuarioServiceProxy usuarioServiceProxy;
    private Model model;
    private RedirectAttributes redirectAttributes;


    @BeforeEach
    public void setUp() {
        usuarioServiceProxy = mock(UsuarioServiceProxy.class);
        clienteController = new ClienteController();
        clienteController.usuarioServiceProxy = usuarioServiceProxy;
        model = mock(Model.class);
        redirectAttributes = mock(RedirectAttributes.class);
    }


    @Test
    public void testHome() {
        String result = clienteController.home();
        assertEquals("index", result);
    }


    @Test
    public void testMostrarRegistro() {
        String redirectUrl = "/principal";
        String result = clienteController.mostrarRegistro(redirectUrl, model);


        verify(model).addAttribute("redirectUrl", redirectUrl);
        assertEquals("registro", result);
    }


    @Test
    public void testRegistrar_Success() {
        Usuario usuario = mock(Usuario.class);


        String result = clienteController.regitrar(usuario, redirectAttributes);


        verify(usuarioServiceProxy).registrar(usuario);
        verify(redirectAttributes).addFlashAttribute("exito", "Usuario registrado con exito");
        assertEquals("redirect:/", result);
    }


    @Test
    public void testRegistrar_Error() {
        Usuario usuario = mock(Usuario.class);
        doThrow(new RuntimeException("Error al registrar")).when(usuarioServiceProxy).registrar(usuario);


        String result = clienteController.regitrar(usuario, redirectAttributes);


        verify(redirectAttributes).addFlashAttribute("error", "Error al registrar usuario: Error al registrar");
        assertEquals("redirect:/registro", result);
    }


    @Test
    public void testShowLogin() {
        String redirectUrl = "/principal";
        String result = clienteController.showLogin(redirectUrl, model);


        verify(model).addAttribute("redirectUrl", redirectUrl);
        assertEquals("login", result);
    }


    @Test
    public void testLogin_Success() {
        String email = "test@example.com";
        String contrasena = "password";
        String token = "validToken";


        // Configurar el mock para devolver un token válido
        when(usuarioServiceProxy.login(any(Credenciales.class))).thenReturn(token);


        // Configurar el mock para devolver un usuario válido al validar el token
        Usuario usuarioMock = mock(Usuario.class);
        when(usuarioMock.email()).thenReturn(email);
        when(usuarioServiceProxy.getDetalleUsuario(token)).thenReturn(usuarioMock);


        // Ejecutar el método
        String result = clienteController.login(email, contrasena, redirectAttributes, model);


        // Verificar el resultado
        assertEquals("redirect:/principal", result);
        assertEquals(token, clienteController.token); // Verificar que el token se asignó correctamente
    }


    @Test
    public void testLogin_Error() {
        String email = "test@example.com";
        String contrasena = "wrongPassword";


        when(usuarioServiceProxy.login(new Credenciales(email, contrasena))).thenThrow(new RuntimeException("Credenciales incorrectas"));


        String result = clienteController.login(email, contrasena, redirectAttributes, model);


        verify(redirectAttributes).addFlashAttribute("error", "Credenciales incorrectas");
        assertEquals("redirect:/", result);
    }


    @Test
    public void testLogout() {
        clienteController.token = "validToken";


        String result = clienteController.logout(redirectAttributes);


        verify(usuarioServiceProxy).logout("validToken");
        assertEquals("redirect:/", result);
        assertEquals(null, clienteController.token);
    }


    @Test
    public void testMostrarPrincipal() {
        String redirectUrl = "/registro";
        String result = clienteController.mostrarPrincipal(redirectUrl, model);


        verify(model).addAttribute("redirectUrl", redirectUrl);
        assertEquals("principal", result);
    }


    @Test
    public void testAddAttributes() {
        HttpServletRequest request = mock(HttpServletRequest.class);


        clienteController.addAttributes(model, request);


        verify(model).addAttribute(eq("currentUrl"), any());
    }


    @Test
    public void testGetDetalleUsuario() {
        Usuario usuarioMock = mock(Usuario.class);
        when(usuarioMock.email()).thenReturn("test@example.com");
        when(usuarioServiceProxy.getDetalleUsuario("validToken")).thenReturn(usuarioMock);
    }
}