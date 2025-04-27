package com.deustermix.restapi.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import com.deustermix.restapi.dto.CredencialesDTO;
import com.deustermix.restapi.service.ServiceInicioSesion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class ControllerInicioSesionTest {


    private ServiceInicioSesion serviceInicioSesion;
    private ControllerInicioSesion controllerInicioSesion;


    @BeforeEach
    public void setUp() {
        serviceInicioSesion = mock(ServiceInicioSesion.class);
        controllerInicioSesion = new ControllerInicioSesion(serviceInicioSesion);
    }


    @Test
    public void testLoginSuccess() {
        CredencialesDTO credenciales = new CredencialesDTO("test@example.com", "password123");
        when(serviceInicioSesion.login(credenciales.getEmail(), credenciales.getContrasena())).thenReturn("mockToken");


        ResponseEntity<String> response = controllerInicioSesion.login(credenciales);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("mockToken", response.getBody());
        verify(serviceInicioSesion, times(1)).login(credenciales.getEmail(), credenciales.getContrasena());
    }


    @Test
    public void testLoginUnauthorized() {
        CredencialesDTO credenciales = new CredencialesDTO("test@example.com", "wrongPassword");
        when(serviceInicioSesion.login(credenciales.getEmail(), credenciales.getContrasena())).thenReturn(null);


        ResponseEntity<String> response = controllerInicioSesion.login(credenciales);


        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(serviceInicioSesion, times(1)).login(credenciales.getEmail(), credenciales.getContrasena());
    }


    @Test
    public void testLogoutSuccess() {
        String tokenUsuario = "validToken";
        when(serviceInicioSesion.logout(tokenUsuario)).thenReturn(true);


        ResponseEntity<Void> response = controllerInicioSesion.logout(tokenUsuario);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(serviceInicioSesion, times(1)).logout(tokenUsuario);
    }


    @Test
    public void testLogoutUnauthorized() {
        String tokenUsuario = "invalidToken";
        when(serviceInicioSesion.logout(tokenUsuario)).thenReturn(false);


        ResponseEntity<Void> response = controllerInicioSesion.logout(tokenUsuario);


        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(serviceInicioSesion, times(1)).logout(tokenUsuario);
    }
}
