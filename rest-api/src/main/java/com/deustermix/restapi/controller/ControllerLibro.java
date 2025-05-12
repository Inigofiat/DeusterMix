package com.deustermix.restapi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deustermix.restapi.dto.ClienteReducidoDTO;
import com.deustermix.restapi.dto.LibroDTO;
import com.deustermix.restapi.model.Cliente;
import com.deustermix.restapi.model.Libro;
import com.deustermix.restapi.model.Receta;
import com.deustermix.restapi.service.ServiceInicioSesion;
import com.deustermix.restapi.service.ServiceLibro;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "Controlador Libros", description = "Operaciones relacionadas con la creacion y visualizacion de libros")
public class ControllerLibro {
    private final ServiceLibro servicioLibro;
    private final ServiceInicioSesion authService;

    public ControllerLibro(ServiceLibro servicioLibro, ServiceInicioSesion authService) {
        this.servicioLibro = servicioLibro;
        this.authService = authService;
    }

    //Mostrar todas las recetas de la plataforma
    @GetMapping("/libros")
    public ResponseEntity<List<LibroDTO>> getLibros() {
        List<Libro> libros = servicioLibro.getLibros();
        List<LibroDTO> libroDTOs = librosALibroDTO(libros);
        return ResponseEntity.ok(libroDTOs);
    }

    @GetMapping("/libros/{id}")
    public ResponseEntity<LibroDTO> getLibrosPorId(@PathVariable Long id) {
        Optional<Libro> libro = servicioLibro.getLibroById(id);
        if (libro.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        LibroDTO libroDTO = libroALibroDTO(libro.get());
        return ResponseEntity.ok(libroDTO);
    }

    @PostMapping("/libros")
    public ResponseEntity<LibroDTO> crearLibro(
        @Parameter(name = "tokenUsuario", description = "Token del usuario", required = true, example = "1a2b3c4d5e")
        @RequestParam("tokenUsuario") String tokenUsuario,
        @Parameter(name = "libroDTO", description = "Post data", required = true)
        @RequestBody LibroDTO libroDTO
        ) {

        if (!authService.esTokenValido(tokenUsuario)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Cliente cliente = authService.getClienteByToken(tokenUsuario);
        Libro libroCreado = servicioLibro.crearLibro(libroDTO, cliente);
        LibroDTO postReturnerDTO = libroALibroDTO(libroCreado);
        return ResponseEntity.ok(postReturnerDTO);
    }

    @DeleteMapping("/libros/{id}")
    public ResponseEntity<Void> eliminarLibro(
        @Parameter(name = "id", description = "Libro ID", required = true, example = "1")
        @PathVariable Long id,
        @Parameter(name = "tokenUsuario", description = "Token del usuario", required = true, example = "1a2b3c4d5e")
        @RequestParam("tokenUsuario") String tokenUsuario
        ) {
        if (!authService.esTokenValido(tokenUsuario)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Cliente cliente = authService.getClienteByToken(tokenUsuario);
        boolean isDeleted = servicioLibro.eliminarLibro(id, cliente);
        return isDeleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/cliente/{email}/libros")
    public ResponseEntity<List<LibroDTO>> obtenerLibrosDeUsuario(
        @Parameter(name = "email", description = "Email del usuario", required = true, example = "nico.p.cueva@gmail.com")
        @PathVariable String email
    ) {
        List<Libro> libros = servicioLibro.getLibrosDeCliente(email);
        List<LibroDTO> libroDTOs = librosALibroDTO(libros);
        return ResponseEntity.ok(libroDTOs);
    }

    private List<LibroDTO> librosALibroDTO(List<Libro> libros) {
        List<LibroDTO> libroDTOs = new ArrayList<>();
        for (Libro libro : libros) {
            LibroDTO libroDTO = libroALibroDTO(libro);
            libroDTOs.add(libroDTO);
        }
        return libroDTOs;
    }

    private LibroDTO libroALibroDTO(Libro libro) {
        List<Long> idRecetas = new ArrayList<>();
        if (libro.getRecetas() != null) {
            for (Receta receta : libro.getRecetas()) {
                idRecetas.add(receta.getId());
            }
        }
        
        // Crear un DTO reducido del cliente para evitar referencias circulares
        Cliente clienteOriginal = libro.getCliente();
        ClienteReducidoDTO clienteDTO = null;
        if (clienteOriginal != null) {
            clienteDTO = new ClienteReducidoDTO(
                clienteOriginal.getEmail(),
                clienteOriginal.getNombre() // Asumiendo que Cliente tiene un método getNombre()
            );
        }
    
        LibroDTO libroDTO = new LibroDTO(
        libro.getId(), 
        libro.getTitulo(), 
        libro.getIsbn(), 
        idRecetas, 
        libro.getCliente()
        );
        return libroDTO;
    }
}