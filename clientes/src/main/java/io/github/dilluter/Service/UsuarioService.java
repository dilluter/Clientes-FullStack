package io.github.dilluter.Service;

import io.github.dilluter.dto.UsuarioCadastroDTO;
import io.github.dilluter.model.entity.Usuario;
import io.github.dilluter.model.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public Usuario cadastrar(UsuarioCadastroDTO dto) {
        if (repository.existsByUsername(dto.getUsername()))
            throw new RuntimeException("Username já está em uso.");

        if (repository.existsByEmail(dto.getEmail()))
            throw new RuntimeException("Email já está em uso.");

        Usuario usuario = Usuario.builder().username(dto.getUsername()).password(passwordEncoder
                        .encode(dto.getPassword())).email(dto.getEmail()).nomeCompleto(dto.getNomeCompleto()).telefone(dto.getTelefone())
                .build();

        return repository.save(usuario);
    }

    public Usuario buscarPorUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
}