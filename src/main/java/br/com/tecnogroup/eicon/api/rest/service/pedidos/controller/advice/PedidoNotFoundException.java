package br.com.tecnogroup.eicon.api.rest.service.pedidos.controller.advice;

import java.util.UUID;

public class PedidoNotFoundException extends RuntimeException {

    public PedidoNotFoundException(UUID id) {
        super("Pedido com {ID} = " + id + ", não encontrado e/ou localizado.");
    }

    public PedidoNotFoundException(Long numeroControle) {
        super("Pedido com {NUMERO_CONTROLE} = " + numeroControle + ", não encontrado e/ou localizado.");
    }

    public PedidoNotFoundException(String message) {
        super(message);
    }
}
