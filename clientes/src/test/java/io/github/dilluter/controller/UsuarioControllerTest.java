package io.github.dilluter.controller;

import io.github.dilluter.dto.usuario.UsuarioCadastroDTO;
import io.github.dilluter.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    @Test
    void deveCadastrarUsuario() {
        UsuarioCadastroDTO usuarioCadastroDTO = mock(UsuarioCadastroDTO.class);

        usuarioController.cadastrar(usuarioCadastroDTO);

        verify(usuarioService).cadastrar(usuarioCadastroDTO);
        verifyNoMoreInteractions(usuarioService);
    }
}