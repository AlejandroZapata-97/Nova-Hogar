package com.novahogar.inventario.service;

import com.novahogar.inventario.entity.Producto;
import com.novahogar.inventario.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepo;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setIdProducto(1);
        producto.setNombreProducto("Silla Oficina");
    }

    @Test
    void crearProducto_DeberiaInicializarStockEnCero_SiEsNulo() {
        // Arrange
        producto.setStockActual(null); // Simulamos el valor nulo
        when(productoRepo.save(any(Producto.class))).thenReturn(producto);

        // Act
        Producto resultado = productoService.crearProducto(producto);

        // Assert
        assertEquals(0, resultado.getStockActual());
        verify(productoRepo).save(producto);
    }

    @Test
    void actualizarProducto_DeberiaActualizarDatos_CuandoExiste() {
        // Arrange
        Producto detallesNuevos = new Producto();
        detallesNuevos.setNombreProducto("Silla Gamer");
        detallesNuevos.setStockActual(50);

        when(productoRepo.findById(1)).thenReturn(Optional.of(producto));
        when(productoRepo.save(any(Producto.class))).thenReturn(producto);

        // Act
        Producto resultado = productoService.actualizarProducto(1, detallesNuevos);

        // Assert
        assertEquals("Silla Gamer", resultado.getNombreProducto());
        assertEquals(50, resultado.getStockActual());
        verify(productoRepo).save(producto);
    }

    @Test
    void eliminarProducto_DeberiaLanzarExcepcion_CuandoNoExiste() {
        // Arrange
        when(productoRepo.existsById(99)).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            productoService.eliminarProducto(99);
        });
        verify(productoRepo, never()).deleteById(anyInt());
    }
}