package com.deustermix.restapi.model;

import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "compra")
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String dniCliente;
    
    @ManyToMany
    @JoinTable(
        name = "compra_libro",
        joinColumns = @JoinColumn(name = "compra_id"),
        inverseJoinColumns = @JoinColumn(name = "libro_id")
    )
    private List<Libro> librosComprados; 
    
    @Enumerated(EnumType.STRING)
    private Pago pago; 

    public String getDniCliente() { 
        return dniCliente;
    }

    public void setDniCliente(String dniCliente) { 
        this.dniCliente = dniCliente;
    }

    public List<Libro> getLibrosComprados() { 
        return librosComprados;
    }

    public void setLibrosComprados(List<Libro> librosComprados) { 
        this.librosComprados = librosComprados;
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }
}
