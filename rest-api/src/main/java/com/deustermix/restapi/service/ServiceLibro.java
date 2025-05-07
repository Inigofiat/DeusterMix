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
    
    public ServiceLibro (LibroRepository repositorioLibro, ClienteRepository repositorioCliente) {
		this.repositorioLibro = repositorioLibro;
		this.repositorioCliente = repositorioCliente;
	}
    
    public List<Libro> getLibros() {
        return repositorioLibro.findAll();
    }

    public Optional<Libro> getLibroById(Long id) {
        return repositorioLibro.findById(id);
    }

    public Libro crearLibro(LibroDTO libroDTO, Cliente cliente) {
        Libro libro = new Libro();
        libro.setTitulo(libroDTO.getTitulo());
        libro.setIsbn(libroDTO.getIsbn());
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
        return repositorioLibro.findByCliente_Email(email);
    }
}

