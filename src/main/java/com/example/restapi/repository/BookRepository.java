package com.example.restapi.repository;
import com.example.restapi.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
    public interface BookRepository extends JpaRepository<Libro, Long> {
}