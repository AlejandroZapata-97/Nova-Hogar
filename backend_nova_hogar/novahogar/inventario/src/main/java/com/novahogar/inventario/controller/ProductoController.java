package com.novahogar.inventario.controller;

import com.novahogar.inventario.entity.Producto;
import com.novahogar.inventario.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepo;

    @GetMapping
    public List<Producto> listarProductos() {
        return productoRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable Integer id) {
        Optional<Producto> producto = productoRepo.findById(id);
        if (producto.isPresent()) {
            return ResponseEntity.ok(producto.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Producto crearProducto(@RequestBody Producto producto) {
        return productoRepo.save(producto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Integer id, @RequestBody Producto productoDetails) {
        Optional<Producto> producto = productoRepo.findById(id);
        if (producto.isPresent()) {
            Producto productoToUpdate = producto.get();
            productoToUpdate.setCodigoSku(productoDetails.getCodigoSku());
            productoToUpdate.setNombreProducto(productoDetails.getNombreProducto());
            productoToUpdate.setDescripcion(productoDetails.getDescripcion());
            productoToUpdate.setCategoria(productoDetails.getCategoria());
            productoToUpdate.setProveedor(productoDetails.getProveedor());
            productoToUpdate.setPrecioCosto(productoDetails.getPrecioCosto());
            productoToUpdate.setPrecioVenta(productoDetails.getPrecioVenta());
            productoToUpdate.setStockMinimo(productoDetails.getStockMinimo());
            productoToUpdate.setStockActual(productoDetails.getStockActual());
            productoToUpdate.setUrlImagen(productoDetails.getUrlImagen());
            Producto updatedProducto = productoRepo.save(productoToUpdate);
            return ResponseEntity.ok(updatedProducto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Integer id) {
        if (productoRepo.existsById(id)) {
            productoRepo.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}