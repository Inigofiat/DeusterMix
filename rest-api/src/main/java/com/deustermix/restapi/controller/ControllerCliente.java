package com.deustermix.restapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deustermix.restapi.dto.ClienteDTO;
import com.deustermix.restapi.model.Cliente;
import com.deustermix.restapi.service.ServiceInicioSesion;


@RestController
@RequestMapping("/api")
@Transactional
public class ControllerCliente {

    @Autowired
    private ServiceInicioSesion serviceInicioSesion;

    @GetMapping("/cliente")
    public ResponseEntity<ClienteDTO> getDetalleCliente(
            @RequestParam("tokenUsuario") String tokenUsuario) {
        try {
            Cliente cliente = serviceInicioSesion.getClienteByToken(tokenUsuario);
            if (cliente == null) {
                return ResponseEntity.notFound().build();
            }

            // Convert to DTO to avoid lazy loading issues
            ClienteDTO clienteDTO = new ClienteDTO(
                cliente.getNombre(),
                cliente.getApellido(),
                cliente.getNombreUsuario(),
                cliente.getEmail(),
                cliente.getDireccion()
            );

            return ResponseEntity.ok(clienteDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}