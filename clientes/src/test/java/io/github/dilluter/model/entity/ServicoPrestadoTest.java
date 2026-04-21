package io.github.dilluter.model.entity;

import org.junit.Assert;
import org.junit.Test;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ServicoPrestadoTest {

    @Test
    public void deveTestarGettersESettersDoServico() {
        Cliente cliente = new Cliente();
        cliente.setNome("Maria Oliveira");
        
        BigDecimal valorServico = new BigDecimal("150.00");
        LocalDate dataServico = LocalDate.of(2023, 10, 20);

        ServicoPrestado servico = new ServicoPrestado();
        servico.setId(10);
        servico.setDescricao("Consultoria");
        servico.setCliente(cliente);
        servico.setValor(valorServico);
        servico.setData(dataServico);

        Assert.assertEquals(Integer.valueOf(10), servico.getId());
        Assert.assertEquals("Consultoria", servico.getDescricao());
        Assert.assertEquals("Maria Oliveira", servico.getCliente().getNome());
        Assert.assertEquals(valorServico, servico.getValor());
        Assert.assertEquals(dataServico, servico.getData());
    }
}