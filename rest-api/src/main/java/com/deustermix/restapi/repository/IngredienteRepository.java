package com.deustermix.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deustermix.restapi.model.Ingrediente;

public interface IngredienteRepository extends JpaRepository<Ingrediente, Long> {
    
}
