package com.novahogar.inventario.service;

import com.novahogar.inventario.entity.MovimientoInventario;
import com.novahogar.inventario.entity.Producto;
import com.novahogar.inventario.repository.MovimientoRepository;
import com.novahogar.inventario.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventarioService {

    @Autowired
    private MovimientoRepository movimientoRepo;

    @Autowired
    private ProductoRepository productoRepo;

    @Transactional
    public MovimientoInventario registrarMovimiento(MovimientoInventario movimiento) {
        // 1. Obtener el producto afectado
        Producto producto = productoRepo.findById(movimiento.getProducto().getIdProducto())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // 2. Actualizar el stock según el tipo
        if ("Entrada".equalsIgnoreCase(movimiento.getTipoMovimiento())) {
            producto.setStockActual(producto.getStockActual() + movimiento.getCantidad());
        } else if ("Salida".equalsIgnoreCase(movimiento.getTipoMovimiento())) {
            if (producto.getStockActual() < movimiento.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para realizar la salida");
            }
            producto.setStockActual(producto.getStockActual() - movimiento.getCantidad());
        }

        // 3. Guardar cambios en el producto y registrar el movimiento
        productoRepo.save(producto);
        return movimientoRepo.save(movimiento);
    }
}