package br.com.tecnogroup.eicon.api.rest.service.pedidos.utils;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class FormatterUtilTest {

    @Before
    public void setUp() {
    }

    @Test
    public void deveRetirarCaracteresNaoNumericosDeUmaString() {
        System.out.println("#TEST: deveRetirarCaracteresNaoNumericosDeUmaString: ");

        // -- 01_Cenário
        String texto = "807.870.320-18";
        System.out.println("String com caracters: ".concat(texto));

        // -- 02_Ação
        String resultSemCaracters = FormatterUtil.retirarCaracteresNaoNumericos(texto);

        // -- 03_Verificacao-Validação
        assertTrue(!resultSemCaracters.contains(".") || !resultSemCaracters.contains("-"));

        System.out.println("String sem caracters: ".concat(resultSemCaracters));
        System.out.println("-------------------------------------------------------------");
    }
}
