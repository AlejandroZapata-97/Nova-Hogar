package com.novahogar.inventario.service;

import com.novahogar.inventario.entity.MovimientoInventario;
import com.novahogar.inventario.repository.MovimientoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovimientoServiceTest {

    @Mock
    private MovimientoRepository movimientoRepo;

    @Mock
    private InventarioService inventarioService;

    @InjectMocks
    private MovimientoService movimientoService;

    @Test
    void listarMovimientos_DeberiaRetornarLista() {
        when(movimientoRepo.findAll()).thenReturn(Arrays.asList(new MovimientoInventario()));

        List<MovimientoInventario> result = movimientoService.listarMovimientos();

        assertEquals(1, result.size());
        verify(movimientoRepo).findAll();
    }

    @Test
    void crearMovimiento_DeberiaDelegarAlInventarioService() {
        MovimientoInventario movimiento = new MovimientoInventario();
        when(inventarioService.registrarMovimiento(movimiento)).thenReturn(movimiento);

        MovimientoInventario result = movimientoService.crearMovimiento(movimiento);

        assertNotNull(result);
        verify(inventarioService).registrarMovimiento(movimiento);
    }

    @Test
    void actualizarMovimiento_DeberiaActualizarDatos_CuandoExiste() {
        Integer id = 1;
        MovimientoInventario existente = new MovimientoInventario();
        MovimientoInventario detalles = new MovimientoInventario();
        detalles.setCantidad(10);
        detalles.setTipoMovimiento("Salida");

        when(movimientoRepo.findById(id)).thenReturn(Optional.of(existente));
        when(movimientoRepo.save(any(MovimientoInventario.class))).thenReturn(existente);

        MovimientoInventario result = movimientoService.actualizarMovimiento(id, detalles);

        assertEquals(10, result.getCantidad());
        verify(movimientoRepo).save(existente);
    }

    @Test
    void eliminarMovimiento_DeberiaLlamarDelete_CuandoExiste() {
        Integer id = 1;
        when(movimientoRepo.existsById(id)).thenReturn(true);

        movimientoService.eliminarMovimiento(id);

        verify(movimientoRepo).deleteById(id);
    }
}