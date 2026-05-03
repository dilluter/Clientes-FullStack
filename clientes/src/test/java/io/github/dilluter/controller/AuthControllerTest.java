package io.github.dilluter.controller;

import io.github.dilluter.dto.login.request.LoginRequestDTO;
import io.github.dilluter.dto.login.response.LoginResponseDTO;
import io.github.dilluter.model.entity.Usuario;
import io.github.dilluter.service.TokenService;
import io.github.dilluter.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthController authController;

    @Test
    void deveRealizarLoginComSucesso() {
        LoginRequestDTO dto = mock(LoginRequestDTO.class);

        when(dto.getUsername()).thenReturn("igor");
        when(dto.getPassword()).thenReturn("123456");

        Usuario usuario = Usuario.builder()
                .id(1)
                .username("igor")
                .password("senha-criptografada")
                .email("igor@email.com")
                .nomeCompleto("Igor Bruno")
                .build();

        when(usuarioService.buscarPorUsername("igor")).thenReturn(usuario);
        when(passwordEncoder.matches("123456", "senha-criptografada")).thenReturn(true);
        when(tokenService.gerarToken(usuario)).thenReturn("token-jwt-fake");

        LoginResponseDTO resultado = authController.login(dto);

        assertNotNull(resultado);
        assertEquals("token-jwt-fake", resultado.getAccessToken());
        assertEquals("Bearer", resultado.getTokenType());

        verify(usuarioService).buscarPorUsername("igor");
        verify(passwordEncoder).matches("123456", "senha-criptografada");
        verify(tokenService).gerarToken(usuario);
        verifyNoMoreInteractions(usuarioService, passwordEncoder, tokenService);
    }

    @Test
    void deveLancarBadCredentialsExceptionQuandoSenhaForInvalida() {
        LoginRequestDTO dto = mock(LoginRequestDTO.class);

        when(dto.getUsername()).thenReturn("igor");
        when(dto.getPassword()).thenReturn("senha-errada");

        Usuario usuario = Usuario.builder()
                .id(1)
                .username("igor")
                .password("senha-criptografada")
                .email("igor@email.com")
                .nomeCompleto("Igor Bruno")
                .build();

        when(usuarioService.buscarPorUsername("igor")).thenReturn(usuario);
        when(passwordEncoder.matches("senha-errada", "senha-criptografada")).thenReturn(false);

        BadCredentialsException exception = assertThrows(
                BadCredentialsException.class,
                () -> authController.login(dto)
        );

        assertEquals("Senha inválida", exception.getMessage());

        verify(usuarioService).buscarPorUsername("igor");
        verify(passwordEncoder).matches("senha-errada", "senha-criptografada");
        verify(tokenService, never()).gerarToken(any());
        verifyNoMoreInteractions(usuarioService, passwordEncoder, tokenService);
    }
}
