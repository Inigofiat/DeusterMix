package com.deustermix.client.service;

import java.util.List;

import com.deustermix.client.data.Credenciales;
import com.deustermix.client.data.Libro;
import com.deustermix.client.data.Receta;
import com.deustermix.client.data.Usuario;

public interface IDeusterMixServiceProxy {
    void registrar(Usuario usuario);
    String login(Credenciales credenciales);
    void logout(String tokenUsuario);
    Usuario getDetalleUsuario(String token);

    void crearReceta(String token, Receta receta);
    List<Receta> getRecetas();
    List<Receta> getRecetas(String token); // Método sobrecargado para incluir token
    Receta obtenerReceta(Long idReceta);
    Receta obtenerReceta(String token, Long idReceta); // Método sobrecargado para incluir token
    void eliminarReceta(String token, Long idReceta);
    List<Receta> getRecetasDeUsuario(String email);
    void guardarReceta(String token, Long idReceta);
    void eliminarRecetaFavorita(String token, Long idReceta);

    List<Libro> getLibros();
    List<Libro> getLibros(String token); // Método sobrecargado para incluir token
    Libro obtenerLibro(Long idLibro);
    Libro obtenerLibro(String token, Long idLibro); // Método sobrecargado para incluir token
    void crearLibro(String token, Libro libro);
    void eliminarLibro(String token, Long idLibro);
    void guardarLibro(String token, Long idLibro);
}