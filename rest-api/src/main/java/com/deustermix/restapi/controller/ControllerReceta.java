package com.deustermix.restapi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deustermix.restapi.dto.RecetaDTO;
import com.deustermix.restapi.model.Cliente;
import com.deustermix.restapi.model.Ingrediente;
import com.deustermix.restapi.model.Receta;
import com.deustermix.restapi.service.ServiceInicioSesion;
import com.deustermix.restapi.service.ServiceReceta;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "Controlador Recetas", description = "Operaciones relacionadas con la creacion y visualizacion de recetas")
public class ControllerReceta {
    private final ServiceReceta servicioReceta;
    private final ServiceInicioSesion authService;

    public ControllerReceta(ServiceReceta servicioReceta, ServiceInicioSesion authService) {
        this.servicioReceta = servicioReceta;
        this.authService = authService;
    }

    //Mostrar todas las recetas de la plataforma
    @GetMapping("/recetas")
    public ResponseEntity<List<RecetaDTO>> getAllPosts() {
        List<Receta> recetas = servicioReceta.getRecetas();
        List<RecetaDTO> recetaDTOs = recetasARecetaDTO(recetas);
        return ResponseEntity.ok(recetaDTOs);
    }

    @GetMapping("/recetas/{id}")
    public ResponseEntity<RecetaDTO> getRecetasPorId(@PathVariable Long id) {
        Optional<Receta> receta = servicioReceta.getRecetaById(id);
        if (receta.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        RecetaDTO recetaDTO = recetaARecetaDTO(receta.get());
        return ResponseEntity.ok(recetaDTO);
    }

    @PostMapping("/recetas")
    public ResponseEntity<RecetaDTO> crearReceta(
        @Parameter(name = "tokenUsuario", description = "Token del usuario", required = true, example = "1a2b3c4d5e")
        @RequestParam("tokenUsuario") String tokenUsuario,
        @Parameter(name = "recetaDTO", description = "Post data", required = true)
        @RequestBody RecetaDTO recetaDTO
        ) {

        if (!authService.esTokenValido(tokenUsuario)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Cliente cliente = authService.getClienteByToken(tokenUsuario);
        Receta recetaCreada = servicioReceta.crearReceta(recetaDTO, cliente);
        RecetaDTO postReturnerDTO = recetaARecetaDTO(recetaCreada);
        return ResponseEntity.ok(postReturnerDTO);
    }

    @DeleteMapping("/recetas/{id}")
    public ResponseEntity<Void> eliminarReceta(
        @Parameter(name = "id", description = "Receta ID", required = true, example = "1")
        @PathVariable Long id,
        @Parameter(name = "tokenUsuario", description = "Token del usuario", required = true, example = "1a2b3c4d5e")
        @RequestParam("tokenUsuario") String tokenUsuario
        ) {
        if (!authService.esTokenValido(tokenUsuario)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Cliente cliente = authService.getClienteByToken(tokenUsuario);
        boolean isDeleted = servicioReceta.eliminarReceta(id, cliente);
        return isDeleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/cliente/{email}/recetas")
    public ResponseEntity<List<RecetaDTO>> obtenerRecetasDeUsuario(
        @Parameter(name = "email", description = "Email del usuario", required = true, example = "nico.p.cueva@gmail.com")
        @PathVariable String email
    ) {
        List<Receta> recetas = servicioReceta.getRecetasDeCliente(email);
        List<RecetaDTO> recetaDTOs = recetasARecetaDTO(recetas);
        return ResponseEntity.ok(recetaDTOs);
    }

    private List<RecetaDTO> recetasARecetaDTO(List<Receta> recetas) {
        List<RecetaDTO> recetaDTOs = new ArrayList<>();
        for (Receta receta : recetas) {
            List<Long> idIngredientes = new ArrayList<>();
            for (Ingrediente ingrediente : receta.getIngredientes()) {
                idIngredientes.add(ingrediente.getId());
            }

            RecetaDTO recetaDTO = new RecetaDTO(receta.getId(), receta.getNombre(), receta.getDescripcion(), idIngredientes, receta.getCliente());
            recetaDTOs.add(recetaDTO);
        }
        return recetaDTOs;
    }


    private RecetaDTO recetaARecetaDTO(Receta receta) {
        List<Long> idIngredientes = new ArrayList<>();
        for (Ingrediente ingrediente : receta.getIngredientes()) {
            idIngredientes.add(ingrediente.getId());
        }
    
        RecetaDTO recetaDTO = new RecetaDTO(receta.getId(), receta.getNombre(), receta.getDescripcion(), idIngredientes, receta.getCliente());
        return recetaDTO;
    }
}
