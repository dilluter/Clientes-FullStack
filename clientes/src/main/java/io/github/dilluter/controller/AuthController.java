package io.github.dilluter.controller;

import io.github.dilluter.dto.login.request.LoginRequestDTO;
import io.github.dilluter.dto.login.response.LoginResponseDTO;
import io.github.dilluter.model.entity.Usuario;
import io.github.dilluter.service.TokenService;
import io.github.dilluter.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody @Valid LoginRequestDTO dto) {
        Usuario usuario = usuarioService.buscarPorUsername(dto.getUsername());

        if (!passwordEncoder.matches(dto.getPassword(), usuario.getPassword())) {
            throw new BadCredentialsException("Senha inválida");
        }

        String token = tokenService.gerarToken(usuario);
        return new LoginResponseDTO(token, "Bearer");
    }
}