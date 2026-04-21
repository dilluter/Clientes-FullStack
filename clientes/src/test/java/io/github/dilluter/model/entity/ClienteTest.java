package io.github.dilluter.model.entity;

import org.junit.Assert;
import org.junit.Test;
import java.time.LocalDate;

public class ClienteTest {

    @Test
    public void deveTestarGettersESettersDoCliente() {
        Cliente cliente = new Cliente();
        cliente.setId(1);
        cliente.setNome("João Silva");
        cliente.setCpf("12345678901");

        Assert.assertEquals(Integer.valueOf(1), cliente.getId());
        Assert.assertEquals("João Silva", cliente.getNome());
        Assert.assertEquals("12345678901", cliente.getCpf());
    }

    @Test
    public void deveDefinirDataCadastroNoPrePersist() {
        Cliente cliente = new Cliente();

        cliente.prePersist();

        Assert.assertNotNull(cliente.getDataDeCadastro());
        Assert.assertEquals(LocalDate.now(), cliente.getDataDeCadastro());
    }
}