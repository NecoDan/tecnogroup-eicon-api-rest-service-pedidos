package br.com.tecnogroup.eicon.api.rest.service.pedidos.service.strategy;

import br.com.tecnogroup.eicon.api.rest.service.pedidos.service.negocio.Desconto;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.utils.DecimalUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class FactoryDescontoDezPorcentoService implements IFactoryDescontoService {

    private static final double VALOR_QTDE_PADRAO_DEZ_PORCENTO = 10.00;

    @Override
    public boolean isAppliable(BigDecimal quantidade) {
        return (isQuantidadeValida(quantidade)
                && DecimalUtil.isMaiorQueZero(quantidade)
                && DecimalUtil.isMaiorOuIgualQue(quantidade, BigDecimal.valueOf(VALOR_QTDE_PADRAO_DEZ_PORCENTO))
        );
    }

    @Override
    public Desconto obterDesconto(BigDecimal quantidade) {
        return (isAppliable(quantidade)) ? Desconto.DESCONTO_DEZ_POR_CENTO : Desconto.DESCONTO_SEM_DESCONTO;
    }

    private boolean isQuantidadeValida(BigDecimal quantidade) {
        return (Objects.nonNull(quantidade));
    }
}
