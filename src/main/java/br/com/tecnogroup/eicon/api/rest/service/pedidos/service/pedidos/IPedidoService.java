package br.com.tecnogroup.eicon.api.rest.service.pedidos.service.pedidos;

import br.com.tecnogroup.eicon.api.rest.service.pedidos.model.pedido.Pedido;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.utils.exceptions.ServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IPedidoService {

    Optional<Pedido> recuperarPorId(UUID id);

    Page<Pedido> recuperarTodos(Pageable pageable);

    List<Pedido> recuperarTodos();

    Pedido recuperarPorNumeroControle(Long numeroControle) throws ServiceException;

    List<Pedido> recuperarPorDtCadastro(LocalDate data) throws ServiceException;

    List<Pedido> recuperarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) throws ServiceException;

    List<Pedido> recuperarPorCodigoCliente(Long codigoCliente) throws ServiceException;

    @Transactional
    Pedido salvar(Pedido pedido) throws ServiceException;

    @Transactional
    boolean excluir(Pedido pedido) throws ServiceException;

    @Transactional
    boolean excluirPor(Long numeroControle) throws ServiceException;

    @Transactional(value = Transactional.TxType.REQUIRED)
    Pedido atualizar(Pedido pedido);
}
