package com.novahogar.inventario.service;

import com.novahogar.inventario.entity.Proveedor;
import com.novahogar.inventario.repository.ProveedorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProveedorServiceTest {

    @Mock
    private ProveedorRepository proveedorRepo;

    @InjectMocks
    private ProveedorService proveedorService;

    private Proveedor proveedor;

    @BeforeEach
    void setUp() {
        proveedor = new Proveedor();
        proveedor.setIdProveedor(1);
        proveedor.setNombreEmpresa("Maderas del Sur");
    }

    @Test
    void crearProveedor_DeberiaGuardarExitosamente() {
        // Arrange
        when(proveedorRepo.save(any(Proveedor.class))).thenReturn(proveedor);

        // Act
        Proveedor resultado = proveedorService.crearProveedor(proveedor);

        // Assert
        assertNotNull(resultado);
        assertEquals("Maderas del Sur", resultado.getNombreEmpresa());
        verify(proveedorRepo, times(1)).save(proveedor);
    }

    @Test
    void actualizarProveedor_DeberiaActualizarDatos_CuandoExiste() {
        // Arrange
        Proveedor nuevosDetalles = new Proveedor();
        nuevosDetalles.setNombreEmpresa("Maderas del Norte");
        nuevosDetalles.setTelefono("123456789");

        when(proveedorRepo.findById(1)).thenReturn(Optional.of(proveedor));
        when(proveedorRepo.save(any(Proveedor.class))).thenReturn(proveedor);

        // Act
        Proveedor resultado = proveedorService.actualizarProveedor(1, nuevosDetalles);

        // Assert
        assertEquals("Maderas del Norte", resultado.getNombreEmpresa());
        verify(proveedorRepo).save(proveedor);
    }

    @Test
    void eliminarProveedor_DeberiaLanzarExcepcion_CuandoNoExiste() {
        // Arrange
        when(proveedorRepo.existsById(99)).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            proveedorService.eliminarProveedor(99);
        });
        verify(proveedorRepo, never()).deleteById(anyInt());
    }
}