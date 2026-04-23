package io.github.dilluter.controller;

import io.github.dilluter.dto.UsuarioCadastroDTO;
import io.github.dilluter.Service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void cadastrar(@Valid @RequestBody UsuarioCadastroDTO dto) {
        usuarioService.cadastrar(dto);
    }
}
