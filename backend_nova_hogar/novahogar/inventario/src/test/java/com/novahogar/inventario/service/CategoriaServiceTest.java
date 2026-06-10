package com.novahogar.inventario.service;

import com.novahogar.inventario.entity.Categoria;
import com.novahogar.inventario.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepo;

    @InjectMocks
    private CategoriaService categoriaService;

    private Categoria categoria;

    @BeforeEach
    void setUp() {

        categoria = new Categoria();
        categoria.setNombreCategoria("Herramientas");
        categoria.setDescripcion("Herramientas manuales y eléctricas");
    }

    @Test
    void listarCategorias_DeberiaRetornarListaDeCategorias() {

        // DATOS
        List<Categoria> listaMock = Arrays.asList(categoria, new Categoria());
        when(categoriaRepo.findAll()).thenReturn(listaMock);

        // EJECUTAR
        List<Categoria> resultado = categoriaService.listarCategorias();

        // VALIDAR
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(categoriaRepo, times(1)).findAll(); // Verifica que el repositorio fue llamado una vez
    }

    @Test
    void obtenerCategoria_DeberiaRetornarCategoria_CuandoExiste() {

        // DATOS
        Integer idBuscado = 1;
        when(categoriaRepo.findById(idBuscado)).thenReturn(Optional.of(categoria));

        // EJECUTAR
        Optional<Categoria> resultado = categoriaService.obtenerCategoria(idBuscado);

        // VALIDAR
        assertTrue(resultado.isPresent());
        assertEquals("Herramientas", resultado.get().getNombreCategoria());
        verify(categoriaRepo, times(1)).findById(idBuscado);
    }

    @Test
    void crearCategoria_DeberiaGuardarYRetornarCategoria() {

        // DATOS
        when(categoriaRepo.save(any(Categoria.class))).thenReturn(categoria);

        // EJECUTAR
        Categoria resultado = categoriaService.crearCategoria(categoria);

        // VALIDAR
        assertNotNull(resultado);
        assertEquals("Herramientas", resultado.getNombreCategoria());
        verify(categoriaRepo, times(1)).save(categoria);
    }

    @Test
    void actualizarCategoria_DeberiaActualizar_CuandoExiste() {

        // DATOS
        Integer idActualizar = 1;
        Categoria detallesNuevos = new Categoria();
        detallesNuevos.setNombreCategoria("Materiales");
        detallesNuevos.setDescripcion("Materiales de construcción");

        when(categoriaRepo.findById(idActualizar)).thenReturn(Optional.of(categoria));
        when(categoriaRepo.save(any(Categoria.class))).thenReturn(categoria); // El save retorna el mismo objeto actualizado

        // EJECUTAR
        Categoria resultado = categoriaService.actualizarCategoria(idActualizar, detallesNuevos);

        // VALIDAR
        assertNotNull(resultado);
        assertEquals("Materiales", resultado.getNombreCategoria());
        assertEquals("Materiales de construcción", resultado.getDescripcion());
        verify(categoriaRepo, times(1)).findById(idActualizar);
        verify(categoriaRepo, times(1)).save(categoria);
    }

    @Test
    void actualizarCategoria_DeberiaLanzarExcepcion_CuandoNoExiste() {
        // DATOS
        Integer idFalso = 99;
        Categoria detallesNuevos = new Categoria();
        when(categoriaRepo.findById(idFalso)).thenReturn(Optional.empty());

        // EJECUTAR & VALIDAR
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            categoriaService.actualizarCategoria(idFalso, detallesNuevos);
        });

        assertEquals("Categoría no encontrada", exception.getMessage());
        verify(categoriaRepo, never()).save(any(Categoria.class)); // Asegura que nunca intente guardar
    }

    @Test
    void eliminarCategoria_DeberiaEliminar_CuandoExiste() {
        // DATOS
        Integer idEliminar = 1;
        when(categoriaRepo.existsById(idEliminar)).thenReturn(true);
        doNothing().when(categoriaRepo).deleteById(idEliminar);

        // EJECUTAR
        categoriaService.eliminarCategoria(idEliminar);

        // VALIDAR
        verify(categoriaRepo, times(1)).existsById(idEliminar);
        verify(categoriaRepo, times(1)).deleteById(idEliminar);
    }

    @Test
    void eliminarCategoria_DeberiaLanzarExcepcion_CuandoNoExiste() {
        // DATOS
        Integer idFalso = 99;
        when(categoriaRepo.existsById(idFalso)).thenReturn(false);

        // EJECUTAR & VALIDAR
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            categoriaService.eliminarCategoria(idFalso);
        });

        assertEquals("Categoría no encontrada", exception.getMessage());
        verify(categoriaRepo, never()).deleteById(anyInt());
    }
}