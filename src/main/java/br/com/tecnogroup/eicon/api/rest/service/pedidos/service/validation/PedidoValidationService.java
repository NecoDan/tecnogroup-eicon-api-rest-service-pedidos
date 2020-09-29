package br.com.tecnogroup.eicon.api.rest.service.pedidos.service.validation;

import br.com.tecnogroup.eicon.api.rest.service.pedidos.model.pedido.Pedido;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.utils.DecimalUtil;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.utils.exceptions.ServiceException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PedidoValidationService implements IPedidoValidationService {

    @Override
    public void validarPedido(Pedido pedido) throws ServiceException {
        if (Objects.isNull(pedido))
            throw new ServiceException("Pedido encontra-se inválido e/ou inexistente {NULL}.");
    }

    @Override
    public void validarDependenciasPedido(Pedido pedido) throws ServiceException {
        validarPedido(pedido);
        validarNumeroControlePedido(pedido.getNumeroControle());

        if (Objects.isNull(pedido.getNomeProduto()) || pedido.getNomeProduto().isEmpty())
            throw new ServiceException("Nome do produto contido no Pedido, encontra-se inválido e/ou inexistente {NULL}.");

        if (Objects.isNull(pedido.getCodigoCliente()) || pedido.getCodigoCliente() <= 0)
            throw new ServiceException("Cliente contido no Pedido, encontra-se inválido e/ou inexistente {NULL}.");

        if (Objects.isNull(pedido.getValor()))
            throw new ServiceException("Valor referente ao produto contido no Pedido, encontra-se inexistente {NULL}.");

        if (DecimalUtil.isEqualsToZero(pedido.getValor()))
            throw new ServiceException("Valor referente ao produto contido no Pedido, encontra-se inválido e/ou menor ou igual à zero (0).");
    }

    @Override
    public void validarPedidoAoAtualizar(Pedido pedido) throws ServiceException {
        validarPedido(pedido);
        validarNumeroControlePedido(pedido.getNumeroControle());

        if (Objects.isNull(pedido.getId()))
            throw new ServiceException("Pedido encontra-se inválido e/ou inexistente {NULL}.");
    }

    @Override
    public void validarNumeroControlePedido(Long numeroControle) throws ServiceException {
        if (Objects.isNull(numeroControle) || numeroControle <= 0)
            throw new ServiceException("Numero controlador referente ao Pedido, encontra-se inválido e/ou inexistente {NULL}.");
    }
}
