package com.deustermix.restapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deustermix.restapi.model.Receta;
import com.deustermix.restapi.dto.RecetaDTO;
import com.deustermix.restapi.model.Cliente;
import com.deustermix.restapi.model.Ingrediente;
import com.deustermix.restapi.repository.ClienteRepository;
import com.deustermix.restapi.repository.RecetaRepository;

@Service
public class ServiceReceta {
    
    private final RecetaRepository repositorioReceta;
    private final ClienteRepository repositorioCliente;
    
    public ServiceReceta (RecetaRepository repositorioReceta, ClienteRepository repositorioCliente) {
		this.repositorioReceta = repositorioReceta;
		this.repositorioCliente = repositorioCliente;
	}
    
    public List<Receta> getRecetas() {
        return repositorioReceta.findAll();
    }

    public Optional<Receta> getRecetaById(Long id) {
        return repositorioReceta.findById(id);
    }

    public Receta crearReceta(RecetaDTO recetaDTO, Cliente cliente) {
        Receta receta = new Receta();
        receta.setNombre(recetaDTO.getNombre());
        receta.setDescripcion(recetaDTO.getDescripcion());
        receta.setCliente(cliente); // Set the client who created the recipe
        receta.setIngredientes(new ArrayList<>());
        for (Long idIngrediente : recetaDTO.getIdIngredientes()) {
            Ingrediente ingrediente = new Ingrediente();
            ingrediente.setId(idIngrediente);
            receta.getIngredientes().add(ingrediente);
        }
        repositorioReceta.save(receta);
        cliente.aniadirReceta(receta);
        repositorioCliente.save(cliente);
        return receta;
    }
    
    @Transactional
    public boolean eliminarReceta(Long id, Cliente cliente) {
        try {
            Receta receta = repositorioReceta.findById(id)
                .orElse(null);

            if (receta == null) {
                return false;
            }

            if (receta.getCliente() == null || !receta.getCliente().getEmail().equals(cliente.getEmail())) {
                return false;
            }

            Cliente recetaCliente = receta.getCliente();
            recetaCliente.getRecetas().remove(receta);
            repositorioCliente.save(recetaCliente);
            repositorioReceta.delete(receta);
            repositorioReceta.flush();
            return true;
        } catch (Exception e) {
            System.err.println("Error al eliminar receta: " + e.getMessage());
            return false;
        }
    }
    
    public List<Receta> getRecetasDeCliente(String email) {
        return repositorioReceta.findByCliente_Email(email);
    }
}

