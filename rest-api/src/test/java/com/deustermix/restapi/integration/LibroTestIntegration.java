package com.deustermix.restapi.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import com.deustermix.restapi.model.Libro;
import com.deustermix.restapi.model.Cliente;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LibroTestIntegration {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCrearBuscarYEliminarLibro() {
        // Paso 1: Crear un cliente (autor)
        Cliente cliente = new Cliente();
        cliente.setNombre("Miguel de Cervantes");
        cliente.setEmail("cervantes@example.com");
        // Completa con otros campos requeridos para Cliente
        
        ResponseEntity<Cliente> clienteResponse = restTemplate.postForEntity("/api/clientes", cliente, Cliente.class);
        assertEquals(HttpStatus.CREATED, clienteResponse.getStatusCode());
        
        Cliente clienteCreado = clienteResponse.getBody();
        assertNotNull(clienteCreado);
        assertNotNull(clienteCreado.getDni());
        String clienteId = clienteCreado.getDni();
        
        // Paso 2: Crear un libro con el cliente como autor
        Libro libro = new Libro();
        libro.setTitulo("El Quijote");
        libro.setAutor(clienteCreado); // Cambiado de setAutor a setCliente
        
        ResponseEntity<Libro> libroResponse = restTemplate.postForEntity("/api/libros", libro, Libro.class);
        assertEquals(HttpStatus.CREATED, libroResponse.getStatusCode());
        
        Libro libroCreado = libroResponse.getBody();
        assertNotNull(libroCreado);
        assertNotNull(libroCreado.getId());
        assertEquals("El Quijote", libroCreado.getTitulo());
        assertNotNull(libroCreado.getCliente());
        assertEquals(clienteId, libroCreado.getCliente().getDni());
        
        Long libroId = libroCreado.getId();
        
        // Paso 3: Buscar el libro por ID
        ResponseEntity<Libro> busquedaResponse = restTemplate.getForEntity("/api/libros/" + libroId, Libro.class);
        assertEquals(HttpStatus.OK, busquedaResponse.getStatusCode());
        
        Libro libroBuscado = busquedaResponse.getBody();
        assertNotNull(libroBuscado);
        assertEquals(libroId, libroBuscado.getId());
        assertEquals("El Quijote", libroBuscado.getTitulo());
        assertNotNull(libroBuscado.getCliente());
        assertEquals("Miguel de Cervantes", libroBuscado.getCliente().getNombre());
        
        // Paso 4: Obtener todos los libros y verificar que el libro creado está en la lista
        ResponseEntity<List<Libro>> todosLibrosResponse = restTemplate.exchange(
            "/api/libros",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Libro>>() {}
        );
        
        assertEquals(HttpStatus.OK, todosLibrosResponse.getStatusCode());
        List<Libro> libros = todosLibrosResponse.getBody();
        assertNotNull(libros);
        
        // Buscar el libro creado en la lista
        boolean libroEncontrado = false;
        for (Libro l : libros) {
            if (l.getId().equals(libroId)) {
                libroEncontrado = true;
                assertEquals("Miguel de Cervantes", l.getCliente().getNombre());
                break;
            }
        }
        assertTrue(libroEncontrado, "El libro creado debería estar en la lista de todos los libros");
        
        // Paso 5: Eliminar el libro
        restTemplate.delete("/api/libros/" + libroId);
        
        // Paso 6: Verificar que el libro ya no existe
        ResponseEntity<Libro> busquedaDespuesDeEliminar = restTemplate.getForEntity("/api/libros/" + libroId, Libro.class);
        assertEquals(HttpStatus.NOT_FOUND, busquedaDespuesDeEliminar.getStatusCode());
        
        // Paso 7: Eliminar el cliente (autor)
        restTemplate.delete("/api/clientes/" + clienteId);
        
        // Paso 8: Verificar que el cliente ya no existe
        ResponseEntity<Cliente> busquedaClienteDespuesDeEliminar = restTemplate.getForEntity("/api/clientes/" + clienteId, Cliente.class);
        assertEquals(HttpStatus.NOT_FOUND, busquedaClienteDespuesDeEliminar.getStatusCode());
    }

    @Test
    void testBuscarLibroInexistente() {
        // Intentar buscar un libro con un ID que presumiblemente no existe
        Long idInexistente = 999999L;
        ResponseEntity<Libro> response = restTemplate.getForEntity("/api/libros/" + idInexistente, Libro.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}