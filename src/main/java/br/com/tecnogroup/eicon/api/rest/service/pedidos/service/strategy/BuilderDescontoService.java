package br.com.tecnogroup.eicon.api.rest.service.pedidos.service.strategy;

import br.com.tecnogroup.eicon.api.rest.service.pedidos.service.negocio.Desconto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BuilderDescontoService implements IBuilderDescontoService {

    private final BuilderDescontoStrategyService builderDescontoStrategyService;

    @Override
    public Desconto obterDescontoAPartir(BigDecimal quantidade) {
        return builderDescontoStrategyService.obter(quantidade);
    }
}
