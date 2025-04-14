package com.deustermix.client.service;

import org.springframework.stereotype.Service;

import com.deustermix.client.data.Credenciales;
import com.deustermix.client.data.Usuario;

@Service
public interface IDeusterMixServiceProxy {
    void registrar(Usuario usuario);
    String login(Credenciales credenciales);
    void logout(String email);
    Usuario getDetalleUsuario(String email);
} 