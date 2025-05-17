package com.deustermix.restapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deustermix.restapi.model.Receta;
import com.deustermix.restapi.model.Cliente;
import com.deustermix.restapi.model.Libro;
import com.deustermix.restapi.repository.ClienteRepository;
import com.deustermix.restapi.repository.LibroRepository;

@Service
public class ServiceLibro {
    
    private final LibroRepository repositorioLibro;
    private final ClienteRepository repositorioCliente;
    private final ServiceReceta repositorioReceta;
    
    public ServiceLibro (LibroRepository repositorioLibro, ClienteRepository repositorioCliente, ServiceReceta repositorioReceta) {
		this.repositorioLibro = repositorioLibro;
		this.repositorioCliente = repositorioCliente;
        this.repositorioReceta = repositorioReceta;
	}
    
    public List<Libro> getLibros() {
        return repositorioLibro.findAllWithRecetas();
    }

    public Optional<Libro> getLibroById(Long id) {
        return repositorioLibro.findByIdWithRecetas(id);
    }
    
    public List<Libro> getLibrosDeCliente(String email) {
        return repositorioLibro.findByCliente_EmailWithRecetas(email);
    }

    @Transactional
    public boolean guardarLibro(Long idLibro, Cliente cliente) {
        try {
            Optional<Libro> optLibro = repositorioLibro.findByIdWithRecetas(idLibro);
            if (optLibro.isEmpty()) {
                System.err.println("Libro no encontrado con id: " + idLibro);
                return false;
            }
            
            Libro libro = optLibro.get();
            
            // Recargar el cliente para asegurarnos de que tenemos una sesión activa
            Cliente clienteRecargado = repositorioCliente.findByEmail(cliente.getEmail())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con email: " + cliente.getEmail()));
            
            // Inicializar la lista de clientes que compran si es null
            if (libro.getClientesQueCompran() == null) {
                libro.setClientesQueCompran(new ArrayList<>());
            }
            
            // Verificar si el libro ya está en la lista de comprados del cliente
            boolean yaComprado = libro.getClientesQueCompran().stream()
                .anyMatch(c -> c.getEmail().equals(clienteRecargado.getEmail()));
                
            if (yaComprado) {
                System.out.println("El libro ya fue comprado por el cliente: " + clienteRecargado.getEmail());
                return true; // El libro ya está comprado por este cliente
            }
                
            // Añadir el cliente a la lista de compradores del libro
            libro.getClientesQueCompran().add(clienteRecargado);
            
            // Guardar los cambios
            repositorioLibro.save(libro);
            
            System.out.println("Libro guardado exitosamente para el cliente: " + clienteRecargado.getEmail());
            return true;
        } catch (Exception e) {
            System.err.println("Error al guardar el libro: " + e.getMessage());
            e.printStackTrace(); // Añadir traza completa para mejor diagnóstico
            return false;
        }
    }
    
    public List<Libro> getLibrosCompradosByClienteEmail(String email) {
    try {
        System.out.println("[ServiceLibro] Iniciando búsqueda de libros comprados para: " + email);
        
        List<Libro> libros = repositorioLibro.findLibrosCompradosByClienteEmail(email);
        
        System.out.println("[ServiceLibro] Consulta ejecutada. Resultados: " + (libros != null ? libros.size() : "null"));
        
        if (libros == null || libros.isEmpty()) {
            System.out.println("[ServiceLibro] No se encontraron libros para: " + email);
            return new ArrayList<>();
        }
        
        // Log cada libro encontrado
        libros.forEach(libro -> {
            System.out.println("[ServiceLibro] Libro encontrado: {" +
                "id: " + libro.getId() + ", " +
                "título: " + libro.getTitulo() + ", " +
                "isbn: " + libro.getIsbn() + ", " +
                "precio: " + libro.getPrecio() + ", " +
                "recetas: " + (libro.getRecetas() != null ? libro.getRecetas().size() : 0) + "}"
            );
        });
        
        return libros;
    } catch (Exception e) {
        System.err.println("[ServiceLibro] Error al obtener libros comprados: " + e.getMessage());
        System.err.println("[ServiceLibro] Detalles del error:");
        e.printStackTrace();
        return new ArrayList<>();
    }
}

    public String getNombreRecetas(Long idReceta) {
        return repositorioReceta.getRecetaById(idReceta)
                .map(Receta::getNombre)
                .orElse(null);
    }
    
    public String getDescripcionRecetas(Long idReceta) {
        return repositorioReceta.getRecetaById(idReceta)
                .map(Receta::getDescripcion)
                .orElse(null);
    }

    public boolean verificarLibroComprado(Long idLibro, String emailCliente) {
        try {
            System.out.println("[ServiceLibro] Verificando libro " + idLibro + " para cliente " + emailCliente);
            List<Libro> librosComprados = repositorioLibro.findLibrosCompradosByClienteEmail(emailCliente);
            boolean tieneLibro = librosComprados.stream()
                .anyMatch(libro -> libro.getId().equals(idLibro));
            System.out.println("[ServiceLibro] Resultado verificación: " + tieneLibro);
            return tieneLibro;
        } catch (Exception e) {
            System.err.println("[ServiceLibro] Error verificando libro comprado: " + e.getMessage());
            return false;
        }
    }
}