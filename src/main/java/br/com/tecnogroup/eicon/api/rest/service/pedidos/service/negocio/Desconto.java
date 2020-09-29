package br.com.tecnogroup.eicon.api.rest.service.pedidos.service.negocio;

public enum Desconto {

    DESCONTO_SEM_DESCONTO(new DescontoSemDesconto()),

    DESCONTO_CINCO_POR_CENTO(new DescontoCincoPorCento()),

    DESCONTO_DEZ_POR_CENTO(new DescontoDezPorCento());

    private final IRegraCalculoDesconto regraCalculoDesconto;

    Desconto(IRegraCalculoDesconto regraCalculoDesconto) {
        this.regraCalculoDesconto = regraCalculoDesconto;
    }

    public IRegraCalculoDesconto getRegraCalculoDesconto() {
        return this.regraCalculoDesconto;
    }
}
