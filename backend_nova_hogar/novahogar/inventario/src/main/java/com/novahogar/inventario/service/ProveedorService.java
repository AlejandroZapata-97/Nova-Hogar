package com.novahogar.inventario.service;

import com.novahogar.inventario.entity.Proveedor;
import com.novahogar.inventario.repository.ProveedorRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepo;

    public ProveedorService(ProveedorRepository proveedorRepo) {
        this.proveedorRepo = proveedorRepo;
    }

    public List<Proveedor> listarProveedores() {
        return proveedorRepo.findAll();
    }

    public Optional<Proveedor> obtenerProveedor(Integer id) {
        return proveedorRepo.findById(id);
    }

    @Transactional
    public Proveedor crearProveedor(Proveedor proveedor) {
        return proveedorRepo.save(proveedor);
    }

    @Transactional
    public Proveedor actualizarProveedor(Integer id, Proveedor detalles) {
        Proveedor proveedor = proveedorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        proveedor.setNombreEmpresa(detalles.getNombreEmpresa());
        proveedor.setContactoPrincipal(detalles.getContactoPrincipal());
        proveedor.setTelefono(detalles.getTelefono());
        proveedor.setEmailProveedor(detalles.getEmailProveedor());
        return proveedorRepo.save(proveedor);
    }

    @Transactional
    public void eliminarProveedor(Integer id) {
        if (!proveedorRepo.existsById(id)) {
            throw new RuntimeException("Proveedor no encontrado");
        }
        proveedorRepo.deleteById(id);
    }
}