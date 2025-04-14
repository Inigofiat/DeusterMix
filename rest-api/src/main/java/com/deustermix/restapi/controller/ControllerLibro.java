package com.deustermix.restapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deustermix.restapi.model.Libro;
import com.deustermix.restapi.service.ServiceLibro;

@RestController
@RequestMapping("/api/libros")
public class ControllerLibro {

    @Autowired
    private ServiceLibro serviceLibro;

    // Endpoint para obtener todos los libros
    @GetMapping
    public ResponseEntity<List<Libro>> obtenerTodosLosLibros() {
        List<Libro> libros = serviceLibro.obtenerTodosLosLibros();
        return new ResponseEntity<>(libros, HttpStatus.OK);
    }

    // Endpoint para buscar un libro por ID
    @GetMapping("/{id}")
    public ResponseEntity<Libro> buscarPorId(@PathVariable Long id) {
        Libro libro = serviceLibro.buscarPorId(id);
        if (libro != null) {
            return new ResponseEntity<>(libro, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint para guardar un libro
    @PostMapping
    public ResponseEntity<Libro> guardarLibro(@RequestBody Libro libro) {
        Libro libroGuardado = serviceLibro.guardarLibro(libro);
        return new ResponseEntity<>(libroGuardado, HttpStatus.CREATED);
    }

    // Endpoint para eliminar un libro por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPorId(@PathVariable Long id) {
        serviceLibro.eliminarPorId(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
