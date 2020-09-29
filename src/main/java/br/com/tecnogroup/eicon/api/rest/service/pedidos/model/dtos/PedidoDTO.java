package br.com.tecnogroup.eicon.api.rest.service.pedidos.model.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PedidoDTO {
    private Long numeroControle;
    private Long codigoCliente;
    private String nomeProduto;
    private BigDecimal valor;
    private BigDecimal quantidade;
}
