package com.novahogar.inventario.service;

import com.novahogar.inventario.entity.Producto;
import com.novahogar.inventario.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepo;

    public ProductoService(ProductoRepository productoRepo) {
        this.productoRepo = productoRepo;
    }

    public List<Producto> listarProductos() {
        return productoRepo.findAll();
    }

    public Optional<Producto> obtenerProducto(Integer id) {
        return productoRepo.findById(id);
    }

    @Transactional
    public Producto crearProducto(Producto producto) {
        if (producto.getStockActual() == null) {
            producto.setStockActual(0);
        }
        return productoRepo.save(producto);
    }

    @Transactional
    public Producto actualizarProducto(Integer id, Producto detalles) {
        Producto producto = productoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setCodigoSku(detalles.getCodigoSku());
        producto.setNombreProducto(detalles.getNombreProducto());
        producto.setDescripcion(detalles.getDescripcion());
        producto.setCategoria(detalles.getCategoria());
        producto.setProveedor(detalles.getProveedor());
        producto.setPrecioCosto(detalles.getPrecioCosto());
        producto.setPrecioVenta(detalles.getPrecioVenta());
        producto.setStockMinimo(detalles.getStockMinimo());
        producto.setStockActual(detalles.getStockActual());
        producto.setUrlImagen(detalles.getUrlImagen());
        return productoRepo.save(producto);
    }

    @Transactional
    public void eliminarProducto(Integer id) {
        if (!productoRepo.existsById(id)) {
            throw new RuntimeException("Producto no encontrado");
        }
        productoRepo.deleteById(id);
    }
}