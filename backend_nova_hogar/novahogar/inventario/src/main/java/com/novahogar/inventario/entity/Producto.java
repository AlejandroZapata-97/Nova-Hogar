package com.novahogar.inventario.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "Producto")
public class Producto {
    private String urlImagen;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idProducto;

    @Column(name = "codigo_sku", unique = true, nullable = false)
    private String codigoSku;

    @Column(name = "nombre_producto", nullable = false)
    private String nombreProducto;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    // Relación con Categoría (Llave Foránea)
    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    // Relación con Proveedor (Llave Foránea)
    // (Asumiendo que ya creaste la clase Proveedor similar a Categoria)
    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;

    @Column(name = "precio_costo")
    private BigDecimal precioCosto; // Usamos BigDecimal para dinero, es más exacto que Double

    @Column(name = "precio_venta")
    private BigDecimal precioVenta;

    @Column(name = "stock_minimo")
    private Integer stockMinimo;

    @Column(name = "stock_actual")
    private Integer stockActual;

}