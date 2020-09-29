package br.com.tecnogroup.eicon.api.rest.service.pedidos.service.pedidos;

import br.com.tecnogroup.eicon.api.rest.service.pedidos.model.cadastro.Cliente;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.model.pedido.Pedido;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.service.cadastro.IClienteService;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.service.negocio.Desconto;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.service.strategy.IBuilderDescontoService;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.service.validation.IPedidoValidationService;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.utils.exceptions.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class GeraPedidoService implements IGeraPedidoService {

    private final IPedidoService pedidoService;
    private final IClienteService clienteService;
    private final IPedidoValidationService pedidoValidationService;
    private final IBuilderDescontoService builderDescontoService;

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED)
    public List<Pedido> gerarPedidos(List<Pedido> pedidos) throws ServiceException {
        validarGeracaoPedidosFromList(pedidos);
        List<Pedido> pedidoListNovos = new ArrayList<>();

        pedidos.parallelStream()
                .forEach(pedido -> {
                    try {
                        pedidoListNovos.add(gerarPedido(pedido));
                    } catch (ServiceException e) {
                        log.error(e.getLocalizedMessage());
                        throw new ServiceException(e.getLocalizedMessage());
                    }
                });

        return pedidoListNovos;
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED)
    public Pedido gerarPedido(Pedido pedido) throws ServiceException {
        this.pedidoValidationService.validarDependenciasPedido(pedido);
        Pedido pedidoNovo = getPedidoExistente(pedido);
        return (isPedidoExistente(pedidoNovo)) ? pedidoNovo : finalizarGeracaoPedido(pedidoNovo);
    }

    private boolean isPedidoExistente(Pedido pedido) {
        return (Objects.nonNull(pedido) && pedido.isIdValido() && pedido.isNumeroControleValido());
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED)
    public Pedido finalizarGeracaoPedido(Pedido pedido) throws ServiceException {
        this.pedidoValidationService.validarDependenciasPedido(pedido);
        pedido.setCliente(gerarCliente(pedido.getCodigoCliente()));
        pedido.geraId();
        pedido.gerarDataCorrente();

        Desconto desconto = this.builderDescontoService.obterDescontoAPartir(pedido.getQuantidade());
        pedido.calcularValorTotal(desconto);
        return pedidoService.salvar(pedido);
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED)
    public Pedido atualizarPedido(Long numeroControle, Pedido pedido) throws ServiceException {
        this.pedidoValidationService.validarNumeroControlePedido(numeroControle);
        this.pedidoValidationService.validarPedido(pedido);
        pedido.setNumeroControle(numeroControle);

        Pedido pedidoUpdate = getPedidoExistente(pedido);
        this.pedidoValidationService.validarPedidoAoAtualizar(pedidoUpdate);

        pedidoUpdate.setNumeroControle(numeroControle);
        pedidoUpdate.setCodigoCliente(pedido.getCodigoCliente());
        pedidoUpdate.setNomeProduto(pedido.getNomeProduto());
        pedidoUpdate.setQuantidade(pedido.getQuantidade());
        pedidoUpdate.setValor(pedido.getValor());

        return finalizarGeracaoPedido(pedidoUpdate);
    }

    private void validarGeracaoPedidosFromList(List<Pedido> pedidoList) throws ServiceException {
        if (Objects.isNull(pedidoList) || pedidoList.isEmpty())
            throw new ServiceException("Não existem pedido(s) ou pedido(s) inexistente(s) a serem processados pela API.");

        if (pedidoList.size() > 10)
            throw new ServiceException("O processamento de pedidos permitidos pela API está limitado a uma quantidade de 10. Informe 10 pedidos e tente novamente.");
    }

    private Pedido getPedidoExistente(Pedido pedido) throws ServiceException {
        Pedido pedidoExistente = null;
        boolean isPedidoParamValido = Objects.nonNull(pedido);

        if (isPedidoParamValido && pedido.isIdValido()) {
            pedidoExistente = this.pedidoService.recuperarPorId(pedido.getId()).orElse(null);
        }

        if (isPedidoParamValido && pedido.isNumeroControleValido()) {
            pedidoExistente = this.pedidoService.recuperarPorNumeroControle(pedido.getNumeroControle());
        }

        return (Objects.isNull(pedidoExistente)) ? pedido : pedidoExistente;
    }

    @Override
    public Cliente gerarCliente(Long codigoCliente) {
        Cliente cliente = this.clienteService.recuperarPorCodigo(codigoCliente);
        return (Objects.isNull(cliente)) ? salvaClienteAPartirCodigo(codigoCliente) : cliente;
    }

    private Cliente salvaClienteAPartirCodigo(Long codigoCliente) {
        Cliente cliente = mountClienteFrom(codigoCliente);
        this.clienteService.salvarCliente(cliente);
        return cliente;
    }

    private Cliente mountClienteFrom(Long codigoCliente) {
        return Cliente
                .builder()
                .codigo(codigoCliente)
                .build()
                .gerarId()
                .gerarData()
                .geraAtivado();
    }
}
