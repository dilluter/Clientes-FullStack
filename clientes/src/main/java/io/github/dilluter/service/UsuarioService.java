package io.github.dilluter.service;

import io.github.dilluter.dto.usuario.UsuarioCadastroDTO;
import io.github.dilluter.exception.BusinessException;
import io.github.dilluter.exception.NotFoundException;
import io.github.dilluter.model.entity.Usuario;
import io.github.dilluter.model.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public Usuario cadastrar(UsuarioCadastroDTO usuarioCadastroDTO) {
        validarConfirmacaoEmail(usuarioCadastroDTO);
        validarConfirmacaoSenha(usuarioCadastroDTO);
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

    private void validarConfirmacaoEmail(UsuarioCadastroDTO usuarioCadastroDTO) {
        if (!Objects.equals(usuarioCadastroDTO.getEmail(), usuarioCadastroDTO.getConfirmarEmail())) {
            throw new BusinessException("Os emails informados não conferem.");
        }
    }

    private void validarConfirmacaoSenha(UsuarioCadastroDTO usuarioCadastroDTO) {
        if (!Objects.equals(usuarioCadastroDTO.getPassword(), usuarioCadastroDTO.getConfirmarPassword())) {
            throw new BusinessException("As senhas informadas não conferem.");
        }
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