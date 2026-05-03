package io.github.dilluter.model.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ServicoPrestadoTest {

    @Test
    void deveCriarServicoPrestadoComBuilder() {
        Cliente cliente = Cliente.builder()
                .id(1)
                .nome("Igor")
                .cpf("52998224725")
                .build();

        ServicoPrestado servico = ServicoPrestado.builder()
                .id(1)
                .descricao("Desenvolvimento de sistema")
                .cliente(cliente)
                .valor(new BigDecimal("1500.00"))
                .data(LocalDate.of(2026, 5, 3))
                .build();

        assertEquals(1, servico.getId());
        assertEquals("Desenvolvimento de sistema", servico.getDescricao());
        assertEquals(cliente, servico.getCliente());
        assertEquals(new BigDecimal("1500.00"), servico.getValor());
        assertEquals(LocalDate.of(2026, 5, 3), servico.getData());
    }
}