package com.novahogar.inventario.controller;

import com.novahogar.inventario.entity.MovimientoInventario;
import com.novahogar.inventario.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventario")
@CrossOrigin(origins = "*")
public class Controller {

    @Autowired
    private InventarioService inventarioService;

    @PostMapping("/movimiento")
    public ResponseEntity<MovimientoInventario> crearMovimiento(@RequestBody MovimientoInventario movimiento) {
        return ResponseEntity.ok(inventarioService.registrarMovimiento(movimiento));
    }
}