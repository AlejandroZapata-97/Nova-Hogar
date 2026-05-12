package com.novahogar.inventario.controller;

import com.novahogar.inventario.entity.Proveedor;
import com.novahogar.inventario.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/proveedores")
@CrossOrigin(origins = "*")
public class ProveedorController {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @GetMapping
    public List<Proveedor> listarProveedores() {
        return proveedorRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> obtenerProveedor(@PathVariable Integer id) {
        Optional<Proveedor> proveedor = proveedorRepository.findById(id);
        if (proveedor.isPresent()) {
            return ResponseEntity.ok(proveedor.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Proveedor crearProveedor(@RequestBody Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> actualizarProveedor(@PathVariable Integer id, @RequestBody Proveedor proveedorDetails) {
        Optional<Proveedor> proveedor = proveedorRepository.findById(id);
        if (proveedor.isPresent()) {
            Proveedor proveedorToUpdate = proveedor.get();
            proveedorToUpdate.setNombreEmpresa(proveedorDetails.getNombreEmpresa());
            proveedorToUpdate.setContactoPrincipal(proveedorDetails.getContactoPrincipal());
            proveedorToUpdate.setTelefono(proveedorDetails.getTelefono());
            proveedorToUpdate.setEmailProveedor(proveedorDetails.getEmailProveedor());
            Proveedor updatedProveedor = proveedorRepository.save(proveedorToUpdate);
            return ResponseEntity.ok(updatedProveedor);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProveedor(@PathVariable Integer id) {
        if (proveedorRepository.existsById(id)) {
            proveedorRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
