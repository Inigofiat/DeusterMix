package com.deustermix.restapi.service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.deustermix.restapi.model.Receta;
import com.deustermix.restapi.model.Cliente;
import com.deustermix.restapi.model.Ingrediente;
import com.deustermix.restapi.repository.RecetaRepository;

@Service
public class ServiceReceta {
    
    private final RecetaRepository repositorioReceta;
    
    public ServiceReceta (RecetaRepository repositorioReceta) {
		this.repositorioReceta = repositorioReceta;
		
	}
    
    //Obtener TODAS las recetas de la plataforma
    public List<Receta> obtenerRecetas(){
        return repositorioReceta.findAll();
    }

    //Obtener la LISTA de recetas del usuario LOGIN
    public List<Receta> obtenerMisRecetas(String email){
		List<Receta> recetas = new ArrayList<>();
		recetas.addAll(repositorioReceta.findByCliente_Email(email));
        return recetas;
    }

    //Obtener la LISTA de recetas de un usuario buscado
    public List<Receta> obtenerRecetasDeCliente(Cliente cliente){
        return repositorioReceta.findByCliente(cliente);
    }

    //Obtener UNA receta en específico
    public Receta obtenerReceta(Long id){
        return repositorioReceta.findById(id).get();
    }

    //Añadir una receta
    public void aniadirReceta(Cliente cliente, String nombre, String descripcion, List<Ingrediente> ingredientes) {
        SecureRandom aleatorio = new SecureRandom();
        Receta nuevaReceta = new Receta(aleatorio.nextLong(), nombre, descripcion, ingredientes, cliente);
        cliente.getRecetas().add(nuevaReceta);
        repositorioReceta.save(nuevaReceta);
    }

    //Eliminar una receta
    public void eliminarReceta(Cliente cliente, Long id){
        Receta receta = repositorioReceta.findById(id).get();
        cliente.getRecetas().remove(receta);
        repositorioReceta.deleteById(id);
    }   
}

