package com.deustermix.restapi.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import com.deustermix.restapi.model.Libro;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LibroTestIntegration {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testConsultarLibros() {
        // Obtener todos los libros
        ResponseEntity<List<Libro>> todosLibrosResponse = restTemplate.exchange(
            "/api/libros",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Libro>>() {}
        );
        
        assertEquals(HttpStatus.OK, todosLibrosResponse.getStatusCode());
        List<Libro> libros = todosLibrosResponse.getBody();
        assertNotNull(libros, "La lista de libros no debería ser null");
    }

    @Test
    void testBuscarLibroInexistente() {
        // Intentar buscar un libro con un ID que presumiblemente no existe
        Long idInexistente = 999999L;
        ResponseEntity<Libro> response = restTemplate.getForEntity(
            "/api/libros/" + idInexistente, 
            Libro.class
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}