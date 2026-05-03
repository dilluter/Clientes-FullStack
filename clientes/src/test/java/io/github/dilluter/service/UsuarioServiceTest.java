package io.github.dilluter.service;

import io.github.dilluter.dto.usuario.UsuarioCadastroDTO;
import io.github.dilluter.exception.BusinessException;
import io.github.dilluter.exception.NotFoundException;
import io.github.dilluter.model.entity.Usuario;
import io.github.dilluter.model.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void deveCadastrarUsuario() {
        UsuarioCadastroDTO dto = mock(UsuarioCadastroDTO.class);

        when(dto.getUsername()).thenReturn("igor");
        when(dto.getPassword()).thenReturn("123456");
        when(dto.getConfirmarPassword()).thenReturn("123456");
        when(dto.getEmail()).thenReturn("igor@email.com");
        when(dto.getConfirmarEmail()).thenReturn("igor@email.com");
        when(dto.getNomeCompleto()).thenReturn("Igor Bruno");
        when(dto.getTelefone()).thenReturn("+5561999999999");

        when(repository.existsByUsername("igor")).thenReturn(false);
        when(repository.existsByEmail("igor@email.com")).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("senha-criptografada");

        Usuario usuarioSalvo = Usuario.builder()
                .id(1)
                .username("igor")
                .password("senha-criptografada")
                .email("igor@email.com")
                .nomeCompleto("Igor Bruno")
                .telefone("+5561999999999")
                .build();

        when(repository.save(any(Usuario.class))).thenReturn(usuarioSalvo);

        Usuario resultado = usuarioService.cadastrar(dto);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("igor", resultado.getUsername());
        assertEquals("senha-criptografada", resultado.getPassword());

        verify(repository).existsByUsername("igor");
        verify(repository).existsByEmail("igor@email.com");
        verify(passwordEncoder).encode("123456");
        verify(repository).save(any(Usuario.class));
    }

    @Test
    void deveLancarBusinessExceptionQuandoEmailsNaoConferem() {
        UsuarioCadastroDTO dto = mock(UsuarioCadastroDTO.class);

        when(dto.getEmail()).thenReturn("igor@email.com");
        when(dto.getConfirmarEmail()).thenReturn("outro@email.com");

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> usuarioService.cadastrar(dto)
        );

        assertEquals("Os emails informados não conferem.", exception.getMessage());

        verify(repository, never()).save(any());
    }

    @Test
    void deveLancarBusinessExceptionQuandoSenhasNaoConferem() {
        UsuarioCadastroDTO dto = mock(UsuarioCadastroDTO.class);

        when(dto.getEmail()).thenReturn("igor@email.com");
        when(dto.getConfirmarEmail()).thenReturn("igor@email.com");
        when(dto.getPassword()).thenReturn("123456");
        when(dto.getConfirmarPassword()).thenReturn("654321");

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> usuarioService.cadastrar(dto)
        );

        assertEquals("As senhas informadas não conferem.", exception.getMessage());

        verify(repository, never()).save(any());
    }

    @Test
    void deveLancarBusinessExceptionQuandoUsernameJaEstiverEmUso() {
        UsuarioCadastroDTO dto = mock(UsuarioCadastroDTO.class);

        when(dto.getEmail()).thenReturn("igor@email.com");
        when(dto.getConfirmarEmail()).thenReturn("igor@email.com");
        when(dto.getPassword()).thenReturn("123456");
        when(dto.getConfirmarPassword()).thenReturn("123456");
        when(dto.getUsername()).thenReturn("igor");

        when(repository.existsByUsername("igor")).thenReturn(true);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> usuarioService.cadastrar(dto)
        );

        assertEquals("Username já está em uso.", exception.getMessage());

        verify(repository).existsByUsername("igor");
        verify(repository, never()).save(any());
    }

    @Test
    void deveLancarBusinessExceptionQuandoEmailJaEstiverEmUso() {
        UsuarioCadastroDTO dto = mock(UsuarioCadastroDTO.class);

        when(dto.getEmail()).thenReturn("igor@email.com");
        when(dto.getConfirmarEmail()).thenReturn("igor@email.com");
        when(dto.getPassword()).thenReturn("123456");
        when(dto.getConfirmarPassword()).thenReturn("123456");
        when(dto.getUsername()).thenReturn("igor");

        when(repository.existsByUsername("igor")).thenReturn(false);
        when(repository.existsByEmail("igor@email.com")).thenReturn(true);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> usuarioService.cadastrar(dto)
        );

        assertEquals("Email já está em uso.", exception.getMessage());

        verify(repository).existsByUsername("igor");
        verify(repository).existsByEmail("igor@email.com");
        verify(repository, never()).save(any());
    }

    @Test
    void deveBuscarUsuarioPorUsername() {
        Usuario usuario = Usuario.builder()
                .id(1)
                .username("igor")
                .email("igor@email.com")
                .password("123456")
                .nomeCompleto("Igor Bruno")
                .build();

        when(repository.findByUsername("igor")).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.buscarPorUsername("igor");

        assertEquals(usuario, resultado);
        verify(repository).findByUsername("igor");
    }

    @Test
    void deveLancarNotFoundQuandoUsuarioNaoForEncontrado() {
        when(repository.findByUsername("igor")).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> usuarioService.buscarPorUsername("igor")
        );

        assertEquals("Usuário não encontrado.", exception.getMessage());
        verify(repository).findByUsername("igor");
    }
}