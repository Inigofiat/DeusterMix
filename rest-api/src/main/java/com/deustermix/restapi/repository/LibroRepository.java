package com.deustermix.restapi.repository;

import com.deustermix.restapi.model.Cliente;
import com.deustermix.restapi.model.Libro;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    List<Libro> findByCliente_Email(String email);
    List<Libro> findByCliente(Cliente cliente);

    // Añadir estos métodos nuevos con JOIN FETCH:
    @Query("SELECT l FROM Libro l LEFT JOIN FETCH l.recetas WHERE l.id = :id")
    Optional<Libro> findByIdWithRecetas(@Param("id") Long id);
    
    @Query("SELECT l FROM Libro l LEFT JOIN FETCH l.recetas")
    List<Libro> findAllWithRecetas();
    
    @Query("SELECT l FROM Libro l LEFT JOIN FETCH l.recetas WHERE l.cliente.email = :email")
    List<Libro> findByCliente_EmailWithRecetas(@Param("email") String email);

    @Query("SELECT DISTINCT l FROM Libro l LEFT JOIN FETCH l.recetas JOIN l.clientesQueCompran c WHERE c.email = :email")
    List<Libro> findLibrosCompradosByClienteEmail(@Param("email") String email);
}