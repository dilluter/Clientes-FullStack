package io.github.dilluter.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.math.BigDecimal;

public class BigDecimalConverterTest {

    private BigDecimalConverter converter;

    @Before
    public void setUp() {
        this.converter = new BigDecimalConverter();
    }

    @Test
    public void deveConverterStringComVirgulaParaBigDecimal() {
        String valor = "1.500,50";
        
        String valorSimples = "10,50";

        BigDecimal resultado = converter.converter(valorSimples);

        Assert.assertEquals(new BigDecimal("10.50"), resultado);
    }

    @Test
    public void deveRetornarZeroQuandoValorForNulo() {

        BigDecimal resultado = converter.converter(null);

        Assert.assertEquals(BigDecimal.ZERO, resultado);
    }

    @Test
    public void deveRetornarZeroQuandoValorForVazio() {
        BigDecimal resultado = converter.converter("   ");

        Assert.assertEquals(BigDecimal.ZERO, resultado);
    }

    @Test
    public void deveConverterStringComPontoParaBigDecimal() {

        String valor = "100.00";

        BigDecimal resultado = converter.converter(valor);

        Assert.assertEquals(new BigDecimal("100.00"), resultado);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deveLancarExcecaoQuandoValorForInvalido() {

        String valorInvalido = "ABC";

        converter.converter(valorInvalido);

    }
}