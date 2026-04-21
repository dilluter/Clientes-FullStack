package io.github.dilluter.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class InternacionalizacaoConfigTest {

    @Autowired
    private MessageSource messageSource;

    @Test
    void deveRecuperarMensagemEmPortugues() {

        String mensagem = messageSource.getMessage("campo.obrigatorio", null, new Locale("pt", "BR"));
        assertThat(mensagem).isEqualTo("Campo obrigatório");
    }
}