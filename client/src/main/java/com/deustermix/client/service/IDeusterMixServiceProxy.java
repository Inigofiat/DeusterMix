package com.deustermix.client.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.deustermix.client.data.Credenciales;
import com.deustermix.client.data.Receta;
import com.deustermix.client.data.Usuario;

@Service
public interface IDeusterMixServiceProxy {
    void registrar(Usuario usuario);
    String login(Credenciales credenciales);
    void logout(String email);
    Usuario getDetalleUsuario(String email);

    void crearReceta (String token, Receta receta);
    void eliminarReceta (String token, Long idReceta);
    Receta obtenerReceta(Long idReceta);
    List<Receta> getRecetas();
    List<Receta> getRecetasDeUsuario(String email);
} 