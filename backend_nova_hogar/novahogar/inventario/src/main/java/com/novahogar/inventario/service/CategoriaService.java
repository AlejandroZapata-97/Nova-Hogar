package com.novahogar.inventario.service;

import com.novahogar.inventario.entity.Categoria;
import com.novahogar.inventario.repository.CategoriaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepo;

    public CategoriaService(CategoriaRepository categoriaRepo) {
        this.categoriaRepo = categoriaRepo;
    }

    public List<Categoria> listarCategorias() {
        return categoriaRepo.findAll();
    }

    public Optional<Categoria> obtenerCategoria(Integer id) {
        return categoriaRepo.findById(id);
    }

    @Transactional
    public Categoria crearCategoria(Categoria categoria) {
        return categoriaRepo.save(categoria);
    }

    @Transactional
    public Categoria actualizarCategoria(Integer id, Categoria detalles) {
        Categoria categoria = categoriaRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        categoria.setNombreCategoria(detalles.getNombreCategoria());
        categoria.setDescripcion(detalles.getDescripcion());
        return categoriaRepo.save(categoria);
    }

    @Transactional
    public void eliminarCategoria(Integer id) {
        if (!categoriaRepo.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada");
        }
        categoriaRepo.deleteById(id);
    }
}