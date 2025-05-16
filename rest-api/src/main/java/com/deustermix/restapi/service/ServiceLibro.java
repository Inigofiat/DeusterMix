package com.deustermix.restapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deustermix.restapi.model.Receta;
import com.deustermix.restapi.dto.LibroDTO;
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

    public Libro crearLibro(LibroDTO libroDTO, Cliente cliente) {
        Libro libro = new Libro();
        libro.setTitulo(libroDTO.getTitulo());
        libro.setIsbn(libroDTO.getIsbn());
        libro.setPrecio(libroDTO.getPrecio()); // Make sure to set the price
        libro.setCliente(cliente);
        libro.setRecetas(new ArrayList<>());
        for (Long idReceta : libroDTO.getIdRecetas()) {
            Receta receta = new Receta();
            receta.setId(idReceta);
            libro.getRecetas().add(receta);
        }
        repositorioLibro.save(libro);
        cliente.aniadirLibro(libro);
        repositorioCliente.save(cliente);
        return libro;
    }
    
    @Transactional
    public boolean eliminarLibro(Long id, Cliente cliente) {
        try {
            Libro libro = repositorioLibro.findById(id)
                .orElse(null);

            if (libro == null) {
                return false;
            }

            if (libro.getCliente() == null || !libro.getCliente().getEmail().equals(cliente.getEmail())) {
                return false;
            }

            Cliente libroCliente = libro.getCliente();
            libroCliente.getLibros().remove(libro);
            repositorioCliente.save(libroCliente);
            repositorioLibro.delete(libro);
            repositorioLibro.flush();
            return true;
        } catch (Exception e) {
            System.err.println("Error al eliminar libro: " + e.getMessage());
            return false;
        }
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
        System.out.println("Buscando recetas guardadas para el email: " + email);
        List<Libro> libros = repositorioLibro.findLibrosCompradosByClienteEmail(email);
        
        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros comprados para el email: " + email);
        } else {
            System.out.println("Se encontraron " + libros.size() + " recetas guardadas para el email: " + email);
            libros.forEach(libro -> {
                System.out.println("\t- Libro: " + libro.getTitulo() + " (ID: " + libro.getId() + ")");
            });
        }
        
        return libros;
    } catch (Exception e) {
        System.err.println("Error al obtener las libros comprados del cliente: " + e.getMessage());
        System.err.println("Detalles del error:");
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

}