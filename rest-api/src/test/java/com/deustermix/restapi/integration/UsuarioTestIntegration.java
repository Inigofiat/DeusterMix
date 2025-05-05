package com.deustermix.restapi.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.deustermix.restapi.model.Usuario;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UsuarioTestIntegration {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCrearObtenerActualizarYEliminarUsuario() {
        // Paso 1: Crear un usuario
        Usuario u = new Usuario();
        u.setDni("12345678A");
        u.setNombre("Juan");
        u.setApellido("Pérez");
        u.setEmail("juan.perez@example.com");
        u.setNombreUsuario("juanperez");
        u.setContrasena("password123");

        ResponseEntity<Usuario> usuarioResponse = restTemplate.postForEntity("/api/usuario", u, Usuario.class);
        assertEquals(HttpStatus.CREATED, usuarioResponse.getStatusCode());

        Usuario usuarioCreado = usuarioResponse.getBody();
        assertNotNull(usuarioCreado);
        assertEquals("Juan", usuarioCreado.getNombre());
        assertEquals("juan.perez@example.com", usuarioCreado.getEmail());
        String usuarioId = usuarioCreado.getDni();

        // Paso 2: Obtener el usuario por ID
        ResponseEntity<Usuario> obtenerResponse = restTemplate.getForEntity("/api/usuario/" + usuarioId, Usuario.class);
        assertEquals(HttpStatus.OK, obtenerResponse.getStatusCode());

        Usuario usuarioObtenido = obtenerResponse.getBody();
        assertNotNull(usuarioObtenido);
        assertEquals(usuarioId, usuarioObtenido.getDni());
        assertEquals("Juan", usuarioObtenido.getNombre());

        // Paso 3: Actualizar el usuario
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setNombre("Juan Actualizado");
        usuarioActualizado.setApellido("Pérez Actualizado");
        usuarioActualizado.setEmail("juan.actualizado@example.com");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Usuario> requestEntity = new HttpEntity<>(usuarioActualizado, headers);

        ResponseEntity<Void> actualizarResponse = restTemplate.exchange("/api/usuario/" + usuarioId, HttpMethod.PUT, requestEntity, Void.class);
        assertEquals(HttpStatus.OK, actualizarResponse.getStatusCode());

        // Verificar los cambios
        ResponseEntity<Usuario> verificarResponse = restTemplate.getForEntity("/api/usuario/" + usuarioId, Usuario.class);
        Usuario usuarioVerificado = verificarResponse.getBody();
        assertNotNull(usuarioVerificado);
        assertEquals("Juan Actualizado", usuarioVerificado.getNombre());
        assertEquals("juan.actualizado@example.com", usuarioVerificado.getEmail());

        // Paso 4: Eliminar el usuario
        restTemplate.delete("/api/usuario/" + usuarioId);

        // Paso 5: Verificar que el usuario ya no existe
        ResponseEntity<Usuario> eliminarResponse = restTemplate.getForEntity("/api/usuario/" + usuarioId, Usuario.class);
        assertEquals(HttpStatus.NOT_FOUND, eliminarResponse.getStatusCode());
    }
}