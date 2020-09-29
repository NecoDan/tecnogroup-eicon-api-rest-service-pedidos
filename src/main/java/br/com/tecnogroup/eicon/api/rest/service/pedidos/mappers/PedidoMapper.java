package br.com.tecnogroup.eicon.api.rest.service.pedidos.mappers;

import br.com.tecnogroup.eicon.api.rest.service.pedidos.model.dtos.PedidoDTO;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.model.pedido.Pedido;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PedidoMapper {

    PedidoMapper INSTANCE = Mappers.getMapper(PedidoMapper.class);

    PedidoDTO pedidoToPedidoDTO(Pedido pedido);

    Pedido pedidoDTOToPedido(PedidoDTO pedidoDTO);
}
