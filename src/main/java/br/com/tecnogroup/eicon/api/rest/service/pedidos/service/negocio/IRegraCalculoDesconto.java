package br.com.tecnogroup.eicon.api.rest.service.pedidos.service.negocio;

import br.com.tecnogroup.eicon.api.rest.service.pedidos.model.pedido.Pedido;

import java.math.BigDecimal;

public interface IRegraCalculoDesconto {

    BigDecimal calculaDesconto(Pedido pedido);

    BigDecimal getValorPercentualDesconto(Pedido pedido);

    double calcularDesconto(Pedido pedido);

    float efetuarCalculoDesconto(Pedido pedido);
}
