package br.com.tecnogroup.eicon.api.rest.service.pedidos.service.strategy;

import br.com.tecnogroup.eicon.api.rest.service.pedidos.service.negocio.Desconto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BuilderDescontoStrategyService {

    private final List<IFactoryDescontoService> factoryDescontoService;

    public BuilderDescontoStrategyService(List<IFactoryDescontoService> factoryDescontoServices) {
        this.factoryDescontoService = factoryDescontoServices;
    }

    public Desconto obter(BigDecimal quantidade) {
        Optional<Desconto> optionalValorPercentualBonus = this.factoryDescontoService
                .stream()
                .filter(Objects::nonNull)
                .filter(descontoService -> descontoService.isAppliable(quantidade))
                .map(descontoService -> descontoService.obterDesconto(quantidade))
                .findFirst();
        return optionalValorPercentualBonus.orElse(Desconto.DESCONTO_SEM_DESCONTO);
    }
}
