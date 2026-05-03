package io.github.dilluter.model.repository;

import io.github.dilluter.model.entity.Cliente;
import io.github.dilluter.model.entity.ServicoPrestado;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ServicoPrestadoRepositoryTest {

    @Autowired
    private ServicoPrestadoRepository servicoPrestadoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    void deveBuscarPorNomeClienteEMes() {
        Cliente cliente = Cliente.builder()
                .nome("Igor Bruno")
                .cpf("52998224725")
                .build();

        clienteRepository.save(cliente);

        ServicoPrestado servico = ServicoPrestado.builder()
                .descricao("Desenvolvimento de sistema")
                .cliente(cliente)
                .valor(new BigDecimal("1500.00"))
                .data(LocalDate.of(2026, 5, 3))
                .build();

        servicoPrestadoRepository.save(servico);

        Pageable pageable = PageRequest.of(0, 10);

        Page<ServicoPrestado> resultado =
                servicoPrestadoRepository.buscarPorNomeClienteEMes("%Igor%", 5, pageable);

        assertEquals(1, resultado.getTotalElements());
        assertEquals("Desenvolvimento de sistema", resultado.getContent().get(0).getDescricao());
        assertEquals("Igor Bruno", resultado.getContent().get(0).getCliente().getNome());
    }

    @Test
    void deveBuscarPorNomeClienteIgnorandoMaiusculasEMinusculas() {
        Cliente cliente = Cliente.builder()
                .nome("Igor Bruno")
                .cpf("52998224725")
                .build();

        clienteRepository.save(cliente);

        ServicoPrestado servico = ServicoPrestado.builder()
                .descricao("Sistema")
                .cliente(cliente)
                .valor(new BigDecimal("1000.00"))
                .data(LocalDate.of(2026, 5, 3))
                .build();

        servicoPrestadoRepository.save(servico);

        Pageable pageable = PageRequest.of(0, 10);

        Page<ServicoPrestado> resultado =
                servicoPrestadoRepository.buscarPorNomeClienteEMes("%igor%", 5, pageable);

        assertEquals(1, resultado.getTotalElements());
    }

    @Test
    void deveBuscarPorNomeClienteSemFiltrarMesQuandoMesForNulo() {
        Cliente cliente = Cliente.builder()
                .nome("Igor Bruno")
                .cpf("52998224725")
                .build();

        clienteRepository.save(cliente);

        ServicoPrestado servicoJaneiro = ServicoPrestado.builder()
                .descricao("Serviço janeiro")
                .cliente(cliente)
                .valor(new BigDecimal("500.00"))
                .data(LocalDate.of(2026, 1, 10))
                .build();

        ServicoPrestado servicoMaio = ServicoPrestado.builder()
                .descricao("Serviço maio")
                .cliente(cliente)
                .valor(new BigDecimal("1000.00"))
                .data(LocalDate.of(2026, 5, 3))
                .build();

        servicoPrestadoRepository.save(servicoJaneiro);
        servicoPrestadoRepository.save(servicoMaio);

        Pageable pageable = PageRequest.of(0, 10);

        Page<ServicoPrestado> resultado =
                servicoPrestadoRepository.buscarPorNomeClienteEMes("%Igor%", null, pageable);

        assertEquals(2, resultado.getTotalElements());
    }

    @Test
    void deveRetornarVazioQuandoNaoEncontrarServico() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<ServicoPrestado> resultado =
                servicoPrestadoRepository.buscarPorNomeClienteEMes("%Inexistente%", 5, pageable);

        assertEquals(0, resultado.getTotalElements());
    }
}