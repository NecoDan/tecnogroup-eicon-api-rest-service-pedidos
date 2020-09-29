package br.com.tecnogroup.eicon.api.rest.service.pedidos.service.pedidos;

import br.com.tecnogroup.eicon.api.rest.service.pedidos.model.cadastro.Cliente;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.model.pedido.Pedido;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.utils.exceptions.ServiceException;

import javax.transaction.Transactional;
import java.util.List;

public interface IGeraPedidoService {

    List<Pedido> gerarPedidos(List<Pedido> pedidos) throws ServiceException;

    @Transactional(value = Transactional.TxType.REQUIRED)
    Pedido gerarPedido(Pedido pedido) throws ServiceException;

    @Transactional(value = Transactional.TxType.REQUIRED)
    Pedido finalizarGeracaoPedido(Pedido pedidoNovo) throws ServiceException;

    @Transactional(value = Transactional.TxType.REQUIRED)
    Pedido atualizarPedido(Long numeroControle, Pedido pedido) throws ServiceException;

    Cliente gerarCliente(Long codigoCliente);
}
