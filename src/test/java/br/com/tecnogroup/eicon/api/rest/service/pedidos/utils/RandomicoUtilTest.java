package br.com.tecnogroup.eicon.api.rest.service.pedidos.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static br.com.tecnogroup.eicon.api.rest.service.pedidos.utils.DecimalUtil.isMaiorQueZero;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class RandomicoUtilTest {

    @Before
    public void setUp() {
    }

    @Test
    public void deveGerarValorRandomicoInteiro() {
        log.info("{} ", "#TEST: deveGerarValorRandomicoInteiro: ");

        // -- 01_Cenário && -- 02_Ação
        final int valor = RandomicoUtil.gerarValorRandomico();

        // -- 03_Verificacao_Validacao
        assertTrue(valor > 0);
        log.info("{} ", "Valor resultado: ".concat(String.valueOf(valor)));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveGerarValorRandomicoLong() {
        log.info("{} ", "#TEST: deveGerarValorRandomicoLong: ");

        // -- 01_Cenário && -- 02_Ação
        final Long valor = RandomicoUtil.gerarValorRandomicoLong();

        // -- 03_Verificacao_Validacao
        assertTrue(valor > 0);
        log.info("{} ", "Valor resultado: ".concat(String.valueOf(valor)));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveGerarValorRandomicoInteger() {
        log.info("{} ", "#TEST: deveGerarValorRandomicoInteger: ");

        // -- 01_Cenário && -- 02_Ação
        final Integer valor = RandomicoUtil.gerarValorRandomicoInteger();

        // -- 03_Verificacao_Validacao
        assertTrue(valor > 0);
        log.info("{} ", "Valor resultado: ".concat(String.valueOf(valor)));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveGerarValorRandomicoDecimal() {
        log.info("{} ", "#TEST: deveGerarValorRandomicoDecimal: ");

        // -- 01_Cenário && -- 02_Ação
        final BigDecimal valor = RandomicoUtil.gerarValorRandomicoDecimal();

        // -- 03_Verificacao_Validacao
        assertTrue(isMaiorQueZero(valor));
        log.info("{} ", "Valor resultado: ".concat(String.valueOf(valor)));
        log.info("{} ", "-------------------------------------------------------------");
    }
}
