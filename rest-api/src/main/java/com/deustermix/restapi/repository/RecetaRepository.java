package com.deustermix.restapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deustermix.restapi.model.Cliente;
import com.deustermix.restapi.model.Receta;

@Repository
public interface RecetaRepository extends JpaRepository<Receta, Long> {
    List<Receta> findByCliente_Email(String email);
    List<Receta> findByCliente(Cliente cliente);
    Optional<Receta> getRecetaById(Long id);

    
    // Añadir estos métodos nuevos con JOIN FETCH:
    @Query("SELECT r FROM Receta r LEFT JOIN FETCH r.ingredientes WHERE r.id = :id")
    Optional<Receta> findByIdWithIngredientes(@Param("id") Long id);
    
    @Query("SELECT r FROM Receta r LEFT JOIN FETCH r.ingredientes")
    List<Receta> findAllWithIngredientes();
    
    @Query("SELECT r FROM Receta r LEFT JOIN FETCH r.ingredientes WHERE r.cliente.email = :email")
    List<Receta> findByCliente_EmailWithIngredientes(@Param("email") String email);
    
    // Método para encontrar las recetas guardadas por un cliente
    @Query("SELECT DISTINCT r FROM Receta r LEFT JOIN FETCH r.ingredientes JOIN r.clientesQueLesGusta c WHERE c.email = :email")
    List<Receta> findRecetasGuardadasByClienteEmail(@Param("email") String email);
}