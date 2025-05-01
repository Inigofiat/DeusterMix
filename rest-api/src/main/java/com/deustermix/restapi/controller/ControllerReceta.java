package com.deustermix.restapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deustermix.restapi.model.Cliente;
import com.deustermix.restapi.model.Ingrediente;
import com.deustermix.restapi.model.Receta;
import com.deustermix.restapi.service.ServiceReceta;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/recetas")
@Tag(name = "Controlador Recetas", description = "Operaciones relacionadas con la creacion y visualizacion de recetas")
public class ControllerReceta {
    private final ServiceReceta servicioReceta;

    public ControllerReceta(ServiceReceta servicioReceta) {
        this.servicioReceta = servicioReceta;
    }

    //Mostrar todas las recetas de la plataforma
    @GetMapping
    public ResponseEntity<List<Receta>> obtenerRecetas() {
        List<Receta> recetas = servicioReceta.obtenerRecetas();
        return new ResponseEntity<>(recetas, HttpStatus.OK);
    }

    //Mostrar las recetas del usuario logueado
    @GetMapping("/mis-recetas")
    public ResponseEntity<List<Receta>> obtenerMisRecetas(@RequestParam String email) {
        return new ResponseEntity<>(servicioReceta.obtenerMisRecetas(email), HttpStatus.OK);
    }

    //Mostrar las recetas de un usuario buscado
    @GetMapping("/recetas-de-usuario")
    public ResponseEntity<List<Receta>> obtenerRecetasDeUsuario(@RequestParam String email) {
        return new ResponseEntity<>(servicioReceta.obtenerMisRecetas(email), HttpStatus.OK);
    }


    //Mostrar una receta en específico
    @GetMapping("/receta/{id}")
    public ResponseEntity<Receta> obtenerReceta(@RequestParam Long id) {
        return new ResponseEntity<>(servicioReceta.obtenerReceta(id), HttpStatus.OK);
    }

    //Añadir una receta
    @PostMapping("/aniadir-receta")
    public ResponseEntity<Receta> aniadirReceta(
        @RequestParam Cliente cliente, 
        @RequestParam String nombre, 
        @RequestParam String descripcion, 
        @RequestParam List<Ingrediente> ingredientes) {
        servicioReceta.aniadirReceta(cliente, nombre, descripcion, ingredientes);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //Eliminar una receta
    @DeleteMapping("/eliminar-receta")
    public ResponseEntity<Receta> eliminarReceta(
        @RequestParam Cliente cliente, 
        @RequestParam Long id) {
        servicioReceta.eliminarReceta(cliente, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
