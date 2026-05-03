package io.github.dilluter.model.repository;

import io.github.dilluter.model.entity.Cliente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ClienteRepositoryTest {

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    void deveBuscarClientePorCpf() {
        Cliente cliente = Cliente.builder()
                .nome("Igor")
                .cpf("52998224725")
                .build();

        clienteRepository.save(cliente);

        Optional<Cliente> resultado = clienteRepository.findByCpf("52998224725");

        assertTrue(resultado.isPresent());
        assertEquals("Igor", resultado.get().getNome());
        assertEquals("52998224725", resultado.get().getCpf());
    }

    @Test
    void deveRetornarVazioQuandoCpfNaoExistir() {
        Optional<Cliente> resultado = clienteRepository.findByCpf("00000000000");

        assertTrue(resultado.isEmpty());
    }
}