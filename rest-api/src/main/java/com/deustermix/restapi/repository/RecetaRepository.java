package com.deustermix.restapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deustermix.restapi.model.Cliente;
import com.deustermix.restapi.model.Receta;

@Repository
public interface RecetaRepository extends JpaRepository<Receta, Long> {
	List<Receta> findByCliente_Email(String email);
    List<Receta> findByCliente(Cliente cliente);
}
