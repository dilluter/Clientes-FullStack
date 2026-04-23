package io.github.dilluter.controller;

import io.github.dilluter.Service.TokenService;
import io.github.dilluter.Service.UsuarioService;
import io.github.dilluter.model.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        Usuario usuario = usuarioService.buscarPorUsername(username);

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            return ResponseEntity.status(401).body("Senha incorreta");
        }

        String token = tokenService.gerarToken(usuario);
        return ResponseEntity.ok(Map.of("access_token", token));
    }
}