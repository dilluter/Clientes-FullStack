package io.github.dilluter.model.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    @Test
    void deveDefinirDataDeCadastroAoPersistir() {
        Cliente cliente = Cliente.builder()
                .nome("Igor")
                .cpf("52998224725")
                .build();

        assertNull(cliente.getDataDeCadastro());

        cliente.prePersist();

        assertEquals(LocalDate.now(), cliente.getDataDeCadastro());
    }

    @Test
    void deveCriarClienteComBuilder() {
        Cliente cliente = Cliente.builder()
                .id(1)
                .nome("Igor Bruno")
                .cpf("52998224725")
                .dataDeCadastro(LocalDate.of(2026, 5, 3))
                .build();

        assertEquals(1, cliente.getId());
        assertEquals("Igor Bruno", cliente.getNome());
        assertEquals("52998224725", cliente.getCpf());
        assertEquals(LocalDate.of(2026, 5, 3), cliente.getDataDeCadastro());
    }
}