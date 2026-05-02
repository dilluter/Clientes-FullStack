package io.github.dilluter.service;

import io.github.dilluter.dto.UsuarioCadastroDTO;
import io.github.dilluter.exception.BusinessException;
import io.github.dilluter.exception.NotFoundException;
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

    public Usuario cadastrar(UsuarioCadastroDTO usuarioCadastroDTO) {
        validarUsernameDisponivel(usuarioCadastroDTO.getUsername());
        validarEmailDisponivel(usuarioCadastroDTO.getEmail());

        Usuario usuario = Usuario.builder()
                .username(usuarioCadastroDTO.getUsername())
                .password(passwordEncoder.encode(usuarioCadastroDTO.getPassword()))
                .email(usuarioCadastroDTO.getEmail())
                .nomeCompleto(usuarioCadastroDTO.getNomeCompleto())
                .telefone(usuarioCadastroDTO.getTelefone())
                .build();

        return repository.save(usuario);
    }

    public Usuario buscarPorUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
    }

    private void validarUsernameDisponivel(String username) {
        if (repository.existsByUsername(username)) {
            throw new BusinessException("Username já está em uso.");
        }
    }

    private void validarEmailDisponivel(String email) {
        if (repository.existsByEmail(email)) {
            throw new BusinessException("Email já está em uso.");
        }
    }
}