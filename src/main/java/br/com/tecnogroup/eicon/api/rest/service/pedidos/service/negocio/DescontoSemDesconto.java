package br.com.tecnogroup.eicon.api.rest.service.pedidos.service.negocio;

import br.com.tecnogroup.eicon.api.rest.service.pedidos.model.pedido.Pedido;

import java.math.BigDecimal;

public class DescontoSemDesconto implements IRegraCalculoDesconto {

    @Override
    public BigDecimal calculaDesconto(Pedido pedido) {
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getValorPercentualDesconto(Pedido pedido) {
        return BigDecimal.ZERO;
    }

    @Override
    public double calcularDesconto(Pedido pedido) {
        return calculaDesconto(pedido).doubleValue();
    }

    @Override
    public float efetuarCalculoDesconto(Pedido pedido) {
        return calculaDesconto(pedido).floatValue();
    }
}
