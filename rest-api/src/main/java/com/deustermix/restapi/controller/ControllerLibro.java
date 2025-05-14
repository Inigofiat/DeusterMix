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
import com.deustermix.restapi.dto.RecetaDTO;
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
        libroDTO.setRecetas(obtenerRecetasDTOPorIds(libroDTO.getIdRecetas()));
        
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

    @PostMapping("/libros/guardar/{id}")
    public ResponseEntity<Void> guardarLibro(
        @Parameter(name = "id", description = "Libro ID", required = true, example = "1")
        @PathVariable("id") Long idLibro,
        @Parameter(name = "tokenUsuario", description = "Token del usuario", required = true, example = "1a2b3c4d5e")
        @RequestParam("tokenUsuario") String tokenUsuario
    ) {
        if (!authService.esTokenValido(tokenUsuario)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        Cliente cliente = authService.getClienteByToken(tokenUsuario);
        boolean guardadoExitoso = servicioLibro.guardarLibro(idLibro, cliente);
        
        if (guardadoExitoso) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/cliente/{email}/libros-guardados")
    public ResponseEntity<List<LibroDTO>> obtenerLibrosGuardadasDeUsuario(
        @Parameter(name = "email", description = "Email del usuario", required = true, example = "nico.p.cueva@gmail.com")
        @PathVariable String email
    ) {
        List<Libro> librosGuardados = servicioLibro.getLibrosGuardadosDeCliente(email);
        List<LibroDTO> librosDTOs = librosALibroDTO(librosGuardados);
        
        // Añadir información completa de ingredientes para cada receta guardada
        for (LibroDTO libroDTO : librosDTOs) {
            libroDTO.setRecetas(obtenerRecetasDTOPorIds(libroDTO.getIdRecetas()));
        }
        
        return ResponseEntity.ok(librosDTOs);
    }

    @GetMapping("/recetas/{id}/nombre")
    public ResponseEntity<String> getNombreReceta(@PathVariable Long id) {
        String nombreReceta = servicioLibro.getNombreRecetas(id);
        if (nombreReceta == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(nombreReceta);
    }

    @GetMapping("/recetas/{id}/descripcion")
    public ResponseEntity<String> getDescripcionReceta(@PathVariable Long id) {
        String descripcionReceta = servicioLibro.getDescripcionRecetas(id);
        if (descripcionReceta == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(descripcionReceta);
    }
    
    // Método para obtener la lista de IngredienteDTO a partir de una lista de IDs
    private List<RecetaDTO> obtenerRecetasDTOPorIds(List<Long> idRecetas) {
        List<RecetaDTO> recetasDTO = new ArrayList<>();
        if (idRecetas != null) {
            for (Long idReceta : idRecetas) {
                String nombre = servicioLibro.getNombreRecetas(idReceta);
                String descripcion = servicioLibro.getDescripcionRecetas(idReceta);
                if (nombre != null) {
                    RecetaDTO recetaDTO = new RecetaDTO(idReceta, nombre, descripcion);
                    recetasDTO.add(recetaDTO);
                }
            }
        }
        return recetasDTO;
    }

    @GetMapping("/libros/recetas/{id}")  // Changed from "/recetas/{id}"
public ResponseEntity<RecetaDTO> getRecetaCompleta(@PathVariable Long id) {
    String nombre = servicioLibro.getNombreRecetas(id);
    String descripcion = servicioLibro.getDescripcionRecetas(id);
    
    if (nombre == null) {
        return ResponseEntity.notFound().build();
    }
    
    RecetaDTO recetaDTO = new RecetaDTO(id, nombre, descripcion);
    return ResponseEntity.ok(recetaDTO);
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
        List<RecetaDTO> recetasDTO = new ArrayList<>();
        
        if (libro.getRecetas() != null) {
            for (Receta receta : libro.getRecetas()) {
                idRecetas.add(receta.getId());
                recetasDTO.add(new RecetaDTO(
                    receta.getId(),
                    receta.getNombre(),
                    receta.getDescripcion()
                ));
            }
        }
        
        ClienteReducidoDTO clienteDTO = null;
        if (libro.getCliente() != null) {
            clienteDTO = new ClienteReducidoDTO(
                libro.getCliente().getEmail(),
                libro.getCliente().getNombre()
            );
        }
    
        LibroDTO libroDTO = new LibroDTO(
            libro.getId(), 
            libro.getTitulo(), 
            libro.getIsbn(),
            libro.getPrecio(), 
            idRecetas, 
            clienteDTO
        );
        libroDTO.setRecetas(recetasDTO);
        return libroDTO;
    }
}