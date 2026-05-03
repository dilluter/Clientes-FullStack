package io.github.dilluter.model.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void deveCriarUsuarioComBuilder() {
        Usuario usuario = Usuario.builder()
                .id(1)
                .username("igor")
                .password("123456")
                .email("igor@email.com")
                .nomeCompleto("Igor Bruno")
                .telefone("+5561999999999")
                .build();

        assertEquals(1, usuario.getId());
        assertEquals("igor", usuario.getUsername());
        assertEquals("123456", usuario.getPassword());
        assertEquals("igor@email.com", usuario.getEmail());
        assertEquals("Igor Bruno", usuario.getNomeCompleto());
        assertEquals("+5561999999999", usuario.getTelefone());
    }
}