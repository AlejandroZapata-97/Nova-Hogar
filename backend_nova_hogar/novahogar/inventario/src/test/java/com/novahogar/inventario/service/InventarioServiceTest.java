package com.novahogar.inventario.service;

import com.novahogar.inventario.entity.MovimientoInventario;
import com.novahogar.inventario.entity.Producto;
import com.novahogar.inventario.repository.MovimientoRepository;
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
class InventarioServiceTest {

    @Mock
    private MovimientoRepository movimientoRepo;

    @Mock
    private ProductoRepository productoRepo;

    @InjectMocks
    private InventarioService inventarioService;

    private Producto producto;
    private MovimientoInventario movimiento;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setIdProducto(1);
        producto.setStockActual(10);

        movimiento = new MovimientoInventario();
        movimiento.setProducto(producto);
        movimiento.setCantidad(5);
    }

    @Test
    void registrarMovimiento_Entrada_DeberiaAumentarStock() {
        // Arrange
        movimiento.setTipoMovimiento("Entrada");
        when(productoRepo.findById(1)).thenReturn(Optional.of(producto));
        when(productoRepo.save(any(Producto.class))).thenReturn(producto);
        when(movimientoRepo.save(any(MovimientoInventario.class))).thenReturn(movimiento);

        // Act
        inventarioService.registrarMovimiento(movimiento);

        // Assert
        assertEquals(15, producto.getStockActual());
        verify(productoRepo).save(producto);
        verify(movimientoRepo).save(movimiento);
    }

    @Test
    void registrarMovimiento_Salida_DeberiaDisminuirStock() {
        // Arrange
        movimiento.setTipoMovimiento("Salida");
        when(productoRepo.findById(1)).thenReturn(Optional.of(producto));
        when(productoRepo.save(any(Producto.class))).thenReturn(producto);
        when(movimientoRepo.save(any(MovimientoInventario.class))).thenReturn(movimiento);

        // Act
        inventarioService.registrarMovimiento(movimiento);

        // Assert
        assertEquals(5, producto.getStockActual());
    }

    @Test
    void registrarMovimiento_Salida_DeberiaLanzarExcepcion_CuandoStockInsuficiente() {
        // Arrange
        movimiento.setTipoMovimiento("Salida");
        movimiento.setCantidad(20); // Más de los 10 que hay en stock
        when(productoRepo.findById(1)).thenReturn(Optional.of(producto));

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            inventarioService.registrarMovimiento(movimiento);
        });
        assertEquals("Stock insuficiente para realizar la salida", ex.getMessage());
        verify(movimientoRepo, never()).save(any());
    }

    @Test
    void registrarMovimiento_DeberiaLanzarExcepcion_CuandoProductoNoExiste() {
        // Arrange
        when(productoRepo.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            inventarioService.registrarMovimiento(movimiento);
        });
    }
}