package com.novahogar.inventario.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data @Entity
public class Proveedor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProveedor;
    private String nombreEmpresa;
    private String contactoPrincipal;
    private String telefono;
    private String emailProveedor;
}
