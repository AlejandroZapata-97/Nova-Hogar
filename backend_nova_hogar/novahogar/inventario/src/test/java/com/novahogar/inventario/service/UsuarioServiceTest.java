package com.novahogar.inventario.service;

import com.novahogar.inventario.entity.Usuario;
import com.novahogar.inventario.repository.UsuarioRepository;
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
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepo;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setNombre("Alejo");
        usuario.setEmail("alejo@novahogar.com");
    }

    @Test
    void crearUsuario_DeberiaGuardarUsuario() {
        when(usuarioRepo.save(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioService.crearUsuario(usuario);

        assertNotNull(resultado);
        assertEquals("Alejo", resultado.getNombre());
        verify(usuarioRepo, times(1)).save(usuario);
    }

    @Test
    void actualizarUsuario_DeberiaActualizarDatos_CuandoExiste() {
        Usuario detalles = new Usuario();
        detalles.setNombre("Alejo Actualizado");
        detalles.setEmail("nuevo@novahogar.com");

        when(usuarioRepo.findById(1)).thenReturn(Optional.of(usuario));
        when(usuarioRepo.save(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioService.actualizarUsuario(1, detalles);

        assertEquals("Alejo Actualizado", resultado.getNombre());
        verify(usuarioRepo).save(usuario);
    }

    @Test
    void eliminarUsuario_DeberiaLanzarExcepcion_CuandoNoExiste() {
        when(usuarioRepo.existsById(99)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            usuarioService.eliminarUsuario(99);
        });
        verify(usuarioRepo, never()).deleteById(anyInt());
    }
}