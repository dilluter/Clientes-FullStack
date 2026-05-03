package io.github.dilluter.service;

import io.github.dilluter.model.entity.Usuario;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    @Test
    void deveGerarTokenJwt() {
        String secret = "minha-chave-secreta-com-tamanho-bom-para-hs256";

        TokenService tokenService = new TokenService(secret);

        Usuario usuario = Usuario.builder()
                .username("igor")
                .email("igor@email.com")
                .build();

        String token = tokenService.gerarToken(usuario);

        assertNotNull(token);
        assertFalse(token.isBlank());

        String[] partes = token.split("\\.");

        assertEquals(3, partes.length);
    }
}