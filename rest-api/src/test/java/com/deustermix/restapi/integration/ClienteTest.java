package com.deustermix.restapi.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.deustermix.restapi.model.Cliente;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClienteTestIntegration {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCrearObtenerActualizarYEliminarCliente() {
        // Paso 1: Crear un cliente
        Cliente cliente = new Cliente();
        cliente.setDni("12345678A");
        cliente.setNombre("Juan");
        cliente.setApellido("Pérez");
        cliente.setEmail("juan.perez@example.com");
        cliente.setNombreUsuario("juanperez");
        cliente.setContrasena("password123");
        cliente.setDireccion("Calle Falsa 123");

        ResponseEntity<Cliente> clienteResponse = restTemplate.postForEntity("/api/clientes", cliente, Cliente.class);
        assertEquals(HttpStatus.CREATED, clienteResponse.getStatusCode());

        Cliente clienteCreado = clienteResponse.getBody();
        assertNotNull(clienteCreado);
        assertEquals("Juan", clienteCreado.getNombre());
        assertEquals("juan.perez@example.com", clienteCreado.getEmail());
        String clienteId = clienteCreado.getDni();

        // Paso 2: Obtener el cliente por ID
        ResponseEntity<Cliente> obtenerResponse = restTemplate.getForEntity("/api/clientes/" + clienteId, Cliente.class);
        assertEquals(HttpStatus.OK, obtenerResponse.getStatusCode());

        Cliente clienteObtenido = obtenerResponse.getBody();
        assertNotNull(clienteObtenido);
        assertEquals(clienteId, clienteObtenido.getDni());
        assertEquals("Juan", clienteObtenido.getNombre());

        // Paso 3: Actualizar el cliente
        Cliente clienteActualizado = new Cliente();
        clienteActualizado.setNombre("Juan Actualizado");
        clienteActualizado.setApellido("Pérez Actualizado");
        clienteActualizado.setEmail("juan.actualizado@example.com");
        clienteActualizado.setDireccion("Calle Nueva 456");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Cliente> requestEntity = new HttpEntity<>(clienteActualizado, headers);

        ResponseEntity<Void> actualizarResponse = restTemplate.exchange("/api/clientes/" + clienteId, HttpMethod.PUT, requestEntity, Void.class);
        assertEquals(HttpStatus.OK, actualizarResponse.getStatusCode());

        // Verificar los cambios
        ResponseEntity<Cliente> verificarResponse = restTemplate.getForEntity("/api/clientes/" + clienteId, Cliente.class);
        Cliente clienteVerificado = verificarResponse.getBody();
        assertNotNull(clienteVerificado);
        assertEquals("Juan Actualizado", clienteVerificado.getNombre());
        assertEquals("juan.actualizado@example.com", clienteVerificado.getEmail());

        // Paso 4: Eliminar el cliente
        restTemplate.delete("/api/clientes/" + clienteId);

        // Paso 5: Verificar que el cliente ya no existe
        ResponseEntity<Cliente> eliminarResponse = restTemplate.getForEntity("/api/clientes/" + clienteId, Cliente.class);
        assertEquals(HttpStatus.NOT_FOUND, eliminarResponse.getStatusCode());
    }

    @Test
    void testObtenerClienteInexistente() {
        // Intentar obtener un cliente con un ID que no existe
        String idInexistente = "99999999Z";
        ResponseEntity<Cliente> response = restTemplate.getForEntity("/api/clientes/" + idInexistente, Cliente.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}