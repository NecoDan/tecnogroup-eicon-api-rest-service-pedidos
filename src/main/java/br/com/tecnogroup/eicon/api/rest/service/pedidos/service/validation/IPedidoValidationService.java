package br.com.tecnogroup.eicon.api.rest.service.pedidos.service.validation;


import br.com.tecnogroup.eicon.api.rest.service.pedidos.model.pedido.Pedido;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.utils.exceptions.ServiceException;

public interface IPedidoValidationService {

    void validarPedido(Pedido pedido) throws ServiceException;

    void validarDependenciasPedido(Pedido pedido) throws ServiceException;

    void validarPedidoAoAtualizar(Pedido pedido) throws ServiceException;

    void validarNumeroControlePedido(Long numeroControle) throws ServiceException;

}
