package com.deustermix.restapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deustermix.restapi.model.Receta;
import com.deustermix.restapi.model.Cliente;
import com.deustermix.restapi.model.Ingrediente;
import com.deustermix.restapi.repository.ClienteRepository;
import com.deustermix.restapi.repository.IngredienteRepository;
import com.deustermix.restapi.repository.RecetaRepository;

@Service
public class ServiceReceta {
    
    private final RecetaRepository repositorioReceta;
    private final ClienteRepository repositorioCliente;
    private final IngredienteRepository repositorioIngrediente;
    
    public ServiceReceta (RecetaRepository repositorioReceta, ClienteRepository repositorioCliente, IngredienteRepository repositorioIngrediente) {
        this.repositorioReceta = repositorioReceta;
        this.repositorioCliente = repositorioCliente;
        this.repositorioIngrediente = repositorioIngrediente;
    }
    
    public List<Receta> getRecetas() {
        // Usar el nuevo método que carga ingredientes
        return repositorioReceta.findAllWithIngredientes();
    }

    public Optional<Receta> getRecetaById(Long id) {
        // Usar el nuevo método que carga ingredientes
        return repositorioReceta.findByIdWithIngredientes(id);
    }
    
    @Transactional
    public boolean eliminarRecetaFavorita(Long idReceta, Cliente cliente) {
        try {
            Optional<Receta> optReceta = repositorioReceta.findByIdWithIngredientes(idReceta);
            if (optReceta.isEmpty()) {
                return false;
            }
            
            Receta receta = optReceta.get();
            
            // Recargar el cliente para asegurarnos de que tenemos una sesión activa
            Cliente clienteRecargado = repositorioCliente.findByEmail(cliente.getEmail())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            
            // Verificar si la receta está en la lista de favoritos
            if (clienteRecargado.getRecetas() == null || 
                clienteRecargado.getRecetas().stream().noneMatch(r -> r.getId().equals(idReceta))) {
                return false; // La receta no está en favoritos
            }
                
            // Eliminar la receta de la lista de guardadas del cliente
            clienteRecargado.getRecetas().removeIf(r -> r.getId().equals(idReceta));
            
            // Eliminar el cliente de la lista de "clientesQueLesGusta" de la receta
            if (receta.getClientesQueLesGusta() != null) {
                receta.getClientesQueLesGusta().removeIf(c -> c.getEmail().equals(cliente.getEmail()));
            }
            
            // Guardar los cambios
            repositorioCliente.save(clienteRecargado);
            repositorioReceta.save(receta);
            
            return true;
        } catch (Exception e) {
            System.err.println("Error al eliminar la receta de favoritos: " + e.getMessage());
            e.printStackTrace(); // Añadir traza completa para mejor diagnóstico
            return false;
        }
    }
    
    public List<Receta> getRecetasDeCliente(String email) {
        // Usar el nuevo método que carga ingredientes
        return repositorioReceta.findByCliente_EmailWithIngredientes(email);
    }
    
    @Transactional
    public boolean guardarReceta(Long idReceta, Cliente cliente) {
        try {
            Optional<Receta> optReceta = repositorioReceta.findByIdWithIngredientes(idReceta);
            if (optReceta.isEmpty()) {
                return false;
            }
            
            Receta receta = optReceta.get();
            
            // Recargar el cliente para asegurarnos de que tenemos una sesión activa
            Cliente clienteRecargado = repositorioCliente.findByEmail(cliente.getEmail())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            
            // Verificar si la receta ya está en la lista de favoritos
            if (clienteRecargado.getRecetas() != null && 
                clienteRecargado.getRecetas().stream().anyMatch(r -> r.getId().equals(idReceta))) {
                return true; // La receta ya está guardada
            }
                
            // Añadir la receta a la lista de guardadas del cliente
            clienteRecargado.aniadirReceta(receta);
            
            // Añadir el cliente a la lista de "clientesQueLesGusta" de la receta
            if (receta.getClientesQueLesGusta() == null) {
                receta.setClientesQueLesGusta(new ArrayList<>());
            }
            receta.getClientesQueLesGusta().add(clienteRecargado);
            
            // Guardar los cambios
            repositorioCliente.save(clienteRecargado);
            repositorioReceta.save(receta);
            
            return true;
        } catch (Exception e) {
            System.err.println("Error al guardar la receta: " + e.getMessage());
            e.printStackTrace(); // Añadir traza completa para mejor diagnóstico
            return false;
        }
    }

    public List<Receta> getRecetasGuardadasByClienteEmail(String email) {
    try {
        System.out.println("Buscando recetas guardadas para el email: " + email);
        List<Receta> recetas = repositorioReceta.findRecetasGuardadasByClienteEmail(email);
        
        if (recetas.isEmpty()) {
            System.out.println("No se encontraron recetas guardadas para el email: " + email);
        } else {
            System.out.println("Se encontraron " + recetas.size() + " recetas guardadas para el email: " + email);
            recetas.forEach(receta -> {
                System.out.println("\t- Receta: " + receta.getNombre() + " (ID: " + receta.getId() + ")");
            });
        }
        
        return recetas;
    } catch (Exception e) {
        System.err.println("Error al obtener las recetas guardadas del cliente: " + e.getMessage());
        System.err.println("Detalles del error:");
        e.printStackTrace();
        return new ArrayList<>();
    }
}

    public String getNombreIngrediente(Long id) {
    // Aquí implementarías la lógica para obtener el nombre del ingrediente
    // usando un repositorio de ingredientes
    try {
        Optional<Ingrediente> ingrediente = repositorioIngrediente.findById(id);
        return ingrediente.map(Ingrediente::getNombre).orElse(null);
    } catch (Exception e) {
        System.err.println("Error al obtener nombre de ingrediente: " + e.getMessage());
        return null;
    }
}
}