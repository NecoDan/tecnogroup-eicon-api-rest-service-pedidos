package br.com.tecnogroup.eicon.api.rest.service.pedidos.service.pedidos;

import br.com.tecnogroup.eicon.api.rest.service.pedidos.model.pedido.Pedido;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.repository.PedidoRepository;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.service.validation.IPedidoValidationService;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.utils.exceptions.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PedidoService implements IPedidoService {

    private final PedidoRepository pedidoRepository;
    private final IPedidoValidationService pedidoValidationService;

    @Override
    public Optional<Pedido> recuperarPorId(UUID id) {
        return this.pedidoRepository.findById(id);
    }

    @Override
    public Page<Pedido> recuperarTodos(Pageable pageable) {
        return this.pedidoRepository.findAll(pageable);
    }

    @Override
    public List<Pedido> recuperarTodos() {
        return this.pedidoRepository.findAll();
    }

    @Override
    public Pedido recuperarPorNumeroControle(Long numeroControle) throws ServiceException {
        if (Objects.isNull(numeroControle))
            throw new ServiceException("Filtro do número controle para buscar o pedido, encontra-se inválido e/ou inexistente {NULL}.");
        return this.pedidoRepository.findByNumeroControle(numeroControle);
    }

    @Override
    public List<Pedido> recuperarPorDtCadastro(LocalDate data) throws ServiceException {
        if (Objects.isNull(data))
            throw new ServiceException("Filtro da data para buscar o(s) pedido(s), encontra-se inválido e/ou inexistente {NULL}.");
        return this.pedidoRepository.recuperarTodosPorDataCadastro(data);
    }

    @Override
    public List<Pedido> recuperarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) throws ServiceException {
        if (Objects.isNull(dataInicio) || Objects.isNull(dataFim))
            throw new ServiceException("Filtro dos periodo das datas para buscar o(s) pedido(s), encontra-se inválido e/ou inexistente {NULL}.");
        return this.pedidoRepository.recuperarTodosPorPeriodoDtCadastro(dataInicio, dataFim);
    }

    @Override
    public List<Pedido> recuperarPorCodigoCliente(Long codigoCliente) throws ServiceException {
        if (Objects.isNull(codigoCliente))
            throw new ServiceException("Filtro do codigo do cliente para buscar o(s) pedido(s), encontra-se inválido e/ou inexistente {NULL}.");
        return this.pedidoRepository.findByCodigoCliente(codigoCliente);
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED)
    public Pedido salvar(Pedido pedido) throws ServiceException {
        this.pedidoValidationService.validarPedido(pedido);
        return this.pedidoRepository.save(pedido);
    }

    @Override
    @Transactional
    public boolean excluir(Pedido pedido) throws ServiceException {
        return (Objects.nonNull(pedido)) && excluirPor(pedido.getNumeroControle());
    }

    @Transactional
    @Override
    public boolean excluirPor(Long numeroControle) throws ServiceException {
        this.pedidoValidationService.validarNumeroControlePedido(numeroControle);
        Pedido pedido = recuperarPorNumeroControle(numeroControle);

        if (Objects.isNull(pedido))
            return false;

        return recuperarPorId(pedido.getId())
                .map(pedidoParam -> {
                    this.pedidoRepository.deleteById(pedidoParam.getId());
                    return true;
                }).orElse(false);
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRED)
    public Pedido atualizar(Pedido pedido) {
        if (Objects.nonNull(pedido))
            this.pedidoRepository.saveAndFlush(pedido);
        return pedido;
    }
}
