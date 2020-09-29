package br.com.tecnogroup.eicon.api.rest.service.pedidos.service.strategy;

import br.com.tecnogroup.eicon.api.rest.service.pedidos.service.negocio.Desconto;

import java.math.BigDecimal;

public interface IFactoryDescontoService {

    boolean isAppliable(BigDecimal quantidade);

    Desconto obterDesconto(BigDecimal quantidade);
}
