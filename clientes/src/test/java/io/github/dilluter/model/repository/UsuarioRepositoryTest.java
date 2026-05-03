package io.github.dilluter.model.repository;

import io.github.dilluter.model.entity.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void deveBuscarUsuarioPorUsername() {
        Usuario usuario = Usuario.builder()
                .username("igor")
                .password("123456")
                .email("igor@email.com")
                .nomeCompleto("Igor Bruno")
                .telefone("+5561999999999")
                .build();

        usuarioRepository.save(usuario);

        Optional<Usuario> resultado = usuarioRepository.findByUsername("igor");

        assertTrue(resultado.isPresent());
        assertEquals("igor", resultado.get().getUsername());
    }

    @Test
    void deveVerificarSeEmailExiste() {
        Usuario usuario = Usuario.builder()
                .username("igor")
                .password("123456")
                .email("igor@email.com")
                .nomeCompleto("Igor Bruno")
                .telefone("+5561999999999")
                .build();

        usuarioRepository.save(usuario);

        boolean existe = usuarioRepository.existsByEmail("igor@email.com");

        assertTrue(existe);
    }

    @Test
    void deveVerificarSeUsernameExiste() {
        Usuario usuario = Usuario.builder()
                .username("igor")
                .password("123456")
                .email("igor@email.com")
                .nomeCompleto("Igor Bruno")
                .telefone("+5561999999999")
                .build();

        usuarioRepository.save(usuario);

        boolean existe = usuarioRepository.existsByUsername("igor");

        assertTrue(existe);
    }

    @Test
    void deveRetornarFalseQuandoEmailNaoExistir() {
        boolean existe = usuarioRepository.existsByEmail("naoexiste@email.com");

        assertFalse(existe);
    }

    @Test
    void deveRetornarFalseQuandoUsernameNaoExistir() {
        boolean existe = usuarioRepository.existsByUsername("naoexiste");

        assertFalse(existe);
    }
}