package br.com.tecnogroup.eicon.api.rest.service.pedidos.service.negocio;

import br.com.tecnogroup.eicon.api.rest.service.pedidos.model.pedido.Pedido;

import java.math.BigDecimal;
import java.util.Objects;

public class DescontoDezPorCento implements IRegraCalculoDesconto {

    private static final double VALOR_DESCONTO_DEZ = 10.00;

    @Override
    public BigDecimal calculaDesconto(Pedido pedido) {
        return isPedidoInValido(pedido) ? BigDecimal.ZERO : getValorDescontoCalculado(pedido);
    }

    @Override
    public BigDecimal getValorPercentualDesconto(Pedido pedido) {
        return BigDecimal.valueOf(VALOR_DESCONTO_DEZ);
    }

    @Override
    public double calcularDesconto(Pedido pedido) {
        return this.calculaDesconto(pedido).doubleValue();
    }

    @Override
    public float efetuarCalculoDesconto(Pedido pedido) {
        return calculaDesconto(pedido).floatValue();
    }

    private boolean isPedidoInValido(Pedido pedido) {
        return (Objects.isNull(pedido) || Objects.isNull(pedido.getValorTotal()));
    }

    private BigDecimal getValorDescontoCalculado(Pedido pedido) {
        return pedido.getValorTotal().multiply(BigDecimal.valueOf(VALOR_DESCONTO_DEZ / 100));
    }
}
