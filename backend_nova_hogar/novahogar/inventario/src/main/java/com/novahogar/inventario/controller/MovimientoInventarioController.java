package com.novahogar.inventario.controller;

import com.novahogar.inventario.entity.MovimientoInventario;
import com.novahogar.inventario.repository.MovimientoRepository;
import com.novahogar.inventario.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movimientos")
@CrossOrigin(origins = "*")
public class MovimientoInventarioController {

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private InventarioService inventarioService;

    @GetMapping
    public List<MovimientoInventario> listarMovimientos() {
        return movimientoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoInventario> obtenerMovimiento(@PathVariable Integer id) {
        Optional<MovimientoInventario> movimiento = movimientoRepository.findById(id);
        if (movimiento.isPresent()) {
            return ResponseEntity.ok(movimiento.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<MovimientoInventario> crearMovimiento(@RequestBody MovimientoInventario movimiento) {
        try {
            MovimientoInventario nuevoMovimiento = inventarioService.registrarMovimiento(movimiento);
            return ResponseEntity.ok(nuevoMovimiento);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovimientoInventario> actualizarMovimiento(@PathVariable Integer id, @RequestBody MovimientoInventario movimientoDetails) {
        Optional<MovimientoInventario> movimiento = movimientoRepository.findById(id);
        if (movimiento.isPresent()) {
            MovimientoInventario movimientoToUpdate = movimiento.get();
            movimientoToUpdate.setProducto(movimientoDetails.getProducto());
            movimientoToUpdate.setUsuario(movimientoDetails.getUsuario());
            movimientoToUpdate.setTipoMovimiento(movimientoDetails.getTipoMovimiento());
            movimientoToUpdate.setMotivo(movimientoDetails.getMotivo());
            movimientoToUpdate.setCantidad(movimientoDetails.getCantidad());
            movimientoToUpdate.setFechaHora(movimientoDetails.getFechaHora());
            movimientoToUpdate.setObservaciones(movimientoDetails.getObservaciones());
            MovimientoInventario updatedMovimiento = movimientoRepository.save(movimientoToUpdate);
            return ResponseEntity.ok(updatedMovimiento);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMovimiento(@PathVariable Integer id) {
        if (movimientoRepository.existsById(id)) {
            movimientoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
