package com.deustermix.restapi.repository;

import com.deustermix.restapi.model.Cliente;
import com.deustermix.restapi.model.Libro;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    List<Libro> findByCliente_Email(String email);
    List<Libro> findByCliente(Cliente cliente);
}