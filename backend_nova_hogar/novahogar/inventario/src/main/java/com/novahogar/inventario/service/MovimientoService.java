package com.novahogar.inventario.service;

import com.novahogar.inventario.entity.MovimientoInventario;
import com.novahogar.inventario.repository.MovimientoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovimientoService {

    private final MovimientoRepository movimientoRepo;
    private final InventarioService inventarioService;

    public MovimientoService(MovimientoRepository movimientoRepo, InventarioService inventarioService) {
        this.movimientoRepo = movimientoRepo;
        this.inventarioService = inventarioService;
    }

    public List<MovimientoInventario> listarMovimientos() {
        return movimientoRepo.findAll();
    }

    public Optional<MovimientoInventario> obtenerMovimiento(Integer id) {
        return movimientoRepo.findById(id);
    }

    @Transactional
    public MovimientoInventario crearMovimiento(MovimientoInventario movimiento) {
        // Delegamos la lógica de actualización de stock a InventarioService
        return inventarioService.registrarMovimiento(movimiento);
    }

    @Transactional
    public MovimientoInventario actualizarMovimiento(Integer id, MovimientoInventario detalles) {
        MovimientoInventario movimiento = movimientoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));

        // Nota: actualizar un movimiento puede requerir recalcular stock.
        // Aquí realizamos una actualización simple sin ajustar stock. Si quieres
        // revertir/aplicar cambios de stock, implementamos la lógica adicional.
        movimiento.setProducto(detalles.getProducto());
        movimiento.setUsuario(detalles.getUsuario());
        movimiento.setTipoMovimiento(detalles.getTipoMovimiento());
        movimiento.setMotivo(detalles.getMotivo());
        movimiento.setCantidad(detalles.getCantidad());
        movimiento.setFechaHora(detalles.getFechaHora());
        movimiento.setObservaciones(detalles.getObservaciones());
        return movimientoRepo.save(movimiento);
    }

    @Transactional
    public void eliminarMovimiento(Integer id) {
        if (!movimientoRepo.existsById(id)) {
            throw new RuntimeException("Movimiento no encontrado");
        }
        // Atención: eliminar no revierte stock aquí. Si necesitas revertir,
        // hay que implementar la lógica que invierta el efecto antes de borrar.
        movimientoRepo.deleteById(id);
    }
}