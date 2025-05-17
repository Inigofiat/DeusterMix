package com.deustermix.client.service;

import java.util.List;

import com.deustermix.client.data.Cliente;
import com.deustermix.client.data.Credenciales;
import com.deustermix.client.data.Libro;
import com.deustermix.client.data.Receta;
import com.deustermix.client.data.Usuario;

public interface IDeusterMixServiceProxy {
    void registrar(Usuario usuario);
    String login(Credenciales credenciales);
    void logout(String tokenUsuario);
    Usuario getDetalleUsuario(String token);
    Cliente getDetalleCliente(String token);

    List<Receta> getRecetas();
    List<Receta> getRecetas(String token); // Método sobrecargado para incluir token
    Receta obtenerReceta(Long idReceta);
    Receta obtenerReceta(String token, Long idReceta); // Método sobrecargado para incluir token
    void guardarReceta(String token, Long idReceta);
    void eliminarRecetaFavorita(String token, Long idReceta);
    List<Receta> getRecetasGuardadas(String token);

    List<Libro> getLibros();
    List<Libro> getLibros(String token); // Método sobrecargado para incluir token
    Libro obtenerLibro(Long idLibro);
    Libro obtenerLibro(String token, Long idLibro); // Método sobrecargado para incluir token
    void guardarLibro(String token, Long idLibro);
    List<Libro> getLibrosComprados(String token);
    Boolean verificarLibroComprado(String token, Long idLibro);
}