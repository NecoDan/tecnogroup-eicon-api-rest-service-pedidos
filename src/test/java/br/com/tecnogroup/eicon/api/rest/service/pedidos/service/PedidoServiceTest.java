package br.com.tecnogroup.eicon.api.rest.service.pedidos.service;

import br.com.tecnogroup.eicon.api.rest.service.pedidos.model.pedido.Pedido;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.repository.PedidoRepository;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.service.pedidos.PedidoService;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.service.validation.PedidoValidationService;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.utils.RandomicoUtil;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.utils.exceptions.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@Slf4j
public class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private PedidoValidationService pedidoValidationService;
    @Spy
    @InjectMocks
    private PedidoService pedidoServiceMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private void resetarMocks() {
        reset(pedidoRepository);
        reset(pedidoValidationService);
        reset(pedidoServiceMock);
    }

    @Test
    public void deveRecuperarPorIdUmUnicoPedido() {
        log.info("{} ", "#TEST: deveRecuperarPorIdUmUnicoPedido:");

        // -- 01_Cenário
        resetarMocks();
        UUID id = UUID.randomUUID();
        Pedido pedido = mock(Pedido.class);

        // -- 01_Cenário && 02_Ação
        doCallRealMethod().when(pedidoServiceMock).recuperarPorId(any(UUID.class));
        when(pedidoServiceMock.recuperarPorId(id)).thenReturn(Optional.of(new Pedido()));

        // -- 03_Verificação_Validação
        assertTrue(pedidoServiceMock.recuperarPorId(id).isPresent());
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveRecuperarTodosUmaListaPedidosPaginavel() {
        log.info("{} ", "#TEST: deveRecuperarTodosUmaListaPedidosPaginavel:");

        // -- 01_Cenário
        resetarMocks();
        Pageable pageable = PageRequest.of(0, 8);
        List<Pedido> pedidoList = Arrays.asList(mock(Pedido.class), mock(Pedido.class), mock(Pedido.class), mock(Pedido.class));
        Page<Pedido> pedidoPage = new PageImpl<>(pedidoList);

        // -- 02_Ação
        doCallRealMethod().when(pedidoServiceMock).recuperarTodos(isA(Pageable.class));
        when(pedidoServiceMock.recuperarTodos(pageable)).thenReturn(pedidoPage);

        // -- 03_Verificação_Validação
        assertEquals(pedidoPage, pedidoServiceMock.recuperarTodos(pageable));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveRecuperarTodosPedidosUmaListaPedidos() {
        log.info("{} ", "#TEST: deveRecuperarTodosPedidosUmaListaPedidos:");

        // -- 01_Cenário
        resetarMocks();
        List<Pedido> pedidoList = Arrays.asList(mock(Pedido.class), mock(Pedido.class), mock(Pedido.class));

        // -- 02_Ação
        doCallRealMethod().when(pedidoServiceMock).recuperarTodos();
        when(pedidoServiceMock.recuperarTodos()).thenReturn(pedidoList);

        // -- 03_Verificação_Validação
        assertEquals(pedidoList, pedidoServiceMock.recuperarTodos());
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveSalvarPedido() throws ServiceException {
        log.info("{} ", "#TEST: deveSalvarPedido: ");

        // -- 01_Cenário
        resetarMocks();
        Pedido pedidoParam = Pedido.builder().build();
        Pedido pedidoResult = mock(Pedido.class);

        // -- 02_Ação
        doCallRealMethod().when(pedidoServiceMock).salvar(isA(Pedido.class));
        when(pedidoServiceMock.salvar(pedidoParam)).thenReturn(pedidoResult);
        pedidoServiceMock.salvar(pedidoParam);

        // -- 03_Verificação_Validação
        verify(pedidoServiceMock).salvar(any(Pedido.class));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveAtualizarPedido() {
        log.info("{} ", "#TEST: deveSalvarPedido: ");

        // -- 01_Cenário
        resetarMocks();
        Pedido pedidoParam = Pedido.builder().build();
        Pedido pedidoResult = mock(Pedido.class);

        // -- 02_Ação
        doCallRealMethod().when(pedidoServiceMock).atualizar(isA(Pedido.class));
        when(pedidoServiceMock.atualizar(pedidoParam)).thenReturn(pedidoResult);
        pedidoServiceMock.atualizar(pedidoParam);

        // -- 03_Verificação_Validação
        verify(pedidoServiceMock).atualizar(any(Pedido.class));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveTentarExcluirPedidoERetornarFalsePorNaoEncontrarPedido() throws ServiceException {
        log.info("{} ", "#TEST: deveTentarExcluirPedidoERetornarFalsePorNaoEncontrarPedido: ");

        // -- 01_Cenário
        resetarMocks();
        Pedido pedidoParam = Pedido.builder().numeroControle(RandomicoUtil.gerarValorRandomicoLong()).build();

        // -- 02_Ação
        doCallRealMethod().when(pedidoServiceMock).excluir(isA(Pedido.class));
        boolean result = pedidoServiceMock.excluir(pedidoParam);

        // -- 03_Verificação_Validação
        assertFalse(result);
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveRecuperarUmaListaDePedidosAPartirNumeroControlePedido() throws ServiceException {
        log.info("{} ", "#TEST: deveRecuperarUmaListaDePedidosAPartirNumeroControlePedido: ");

        // -- 01_Cenário
        resetarMocks();
        Long numeroControle = RandomicoUtil.gerarValorRandomicoLong();
        Pedido pedido = mock(Pedido.class);

        // -- 02_Ação
        doCallRealMethod().when(pedidoServiceMock).recuperarPorNumeroControle(isA(Long.class));
        when(pedidoServiceMock.recuperarPorNumeroControle(numeroControle)).thenReturn(pedido);

        // -- 03_Verificação_Validação
        assertEquals(pedido, pedidoServiceMock.recuperarPorNumeroControle(numeroControle));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveRecuperarUmaListaDePedidosAPartirCodigoCliente() throws ServiceException {
        log.info("{} ", "#TEST: deveRecuperarUmaListaDePedidosAPartirCodigoCliente: ");

        // -- 01_Cenário
        resetarMocks();
        Long codigoCliente = RandomicoUtil.gerarValorRandomicoLong();
        List<Pedido> pedidoList = Arrays.asList(mock(Pedido.class), mock(Pedido.class), mock(Pedido.class), mock(Pedido.class));

        // -- 02_Ação
        doCallRealMethod().when(pedidoServiceMock).recuperarPorCodigoCliente(isA(Long.class));
        when(pedidoServiceMock.recuperarPorCodigoCliente(codigoCliente)).thenReturn(pedidoList);

        // -- 03_Verificação_Validação
        assertEquals(pedidoList, pedidoServiceMock.recuperarPorCodigoCliente(codigoCliente));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveRecuperarPedidosPorIntervaloDatas() throws ServiceException {
        log.info("{} ", "#TEST: deveRecuperarPedidosPorIntervaloDatas:");

        // -- 01_Cenário
        resetarMocks();
        LocalDate filtroDataInicial = LocalDate.now();
        LocalDate filtroDataFinal = LocalDate.now();
        List<Pedido> pedidoList = Arrays.asList(mock(Pedido.class), mock(Pedido.class), mock(Pedido.class), mock(Pedido.class), mock(Pedido.class), mock(Pedido.class));

        // -- 02_Ação
        doCallRealMethod().when(pedidoServiceMock).recuperarPorPeriodo(isA(LocalDate.class), isA(LocalDate.class));
        when(pedidoServiceMock.recuperarPorPeriodo(filtroDataInicial, filtroDataFinal)).thenReturn(pedidoList);

        // -- 03_Verificação_Validação
        assertEquals(pedidoList, pedidoServiceMock.recuperarPorPeriodo(filtroDataInicial, filtroDataFinal));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveRecuperarPedidosPorFiltroDataCadastro() throws ServiceException {
        log.info("{} ", "#TEST: deveRecuperarPedidosPorFiltroDataCadastro:");

        // -- 01_Cenário
        resetarMocks();
        LocalDate filtroData = LocalDate.now();
        List<Pedido> pedidoList = Arrays.asList(mock(Pedido.class), mock(Pedido.class), mock(Pedido.class), mock(Pedido.class));

        // -- 02_Ação
        doCallRealMethod().when(pedidoServiceMock).recuperarPorDtCadastro(isA(LocalDate.class));
        when(pedidoServiceMock.recuperarPorDtCadastro(filtroData)).thenReturn(pedidoList);

        // -- 03_Verificação_Validação
        assertEquals(pedidoList, pedidoServiceMock.recuperarPorDtCadastro(filtroData));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveLancarExceptionAoRecuperarPedidosPorIntervaloDatas() throws ServiceException {
        log.info("{} ", "#TEST: deveLancarExceptionAoRecuperarPedidosPorIntervaloDatas: ");

        // -- 01_Cenário && 02_Ação
        doCallRealMethod().when(pedidoServiceMock).recuperarPorPeriodo(isA(LocalDate.class), isA(LocalDate.class));
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class,
                () -> pedidoServiceMock.recuperarPorPeriodo(LocalDate.now(), null));

        // -- 03_Verificação_Validação
        assertTrue(exception.getMessage().contains("Filtro dos periodo das datas"));
        log.info("{} ", "EXCEPTION: ".concat(exception.getMessage()));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveLancarExceptionAoRecuperarPedidosPorDataCadastro() throws ServiceException {
        log.info("{} ", "#TEST: deveLancarExceptionAoRecuperarPedidosPorDataCadastro: ");

        // -- 01_Cenário && 02_Ação
        doCallRealMethod().when(pedidoServiceMock).recuperarPorDtCadastro(isA(LocalDate.class));
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class,
                () -> pedidoServiceMock.recuperarPorDtCadastro(null));

        // -- 03_Verificação_Validação
        assertTrue(exception.getMessage().contains("Filtro da data para buscar o(s) pedido(s)"));
        log.info("{} ", "EXCEPTION: ".concat(exception.getMessage()));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveLancarExceptionAoRecuperarPedidosPorCodigoCliente() throws ServiceException {
        log.info("{} ", "#TEST: deveLancarExceptionAoRecuperarPedidosPorCodigoCliente: ");

        // -- 01_Cenário && 02_Ação
        doCallRealMethod().when(pedidoServiceMock).recuperarPorCodigoCliente(isA(Long.class));
        ServiceException exception = assertThrows(ServiceException.class,
                () -> pedidoServiceMock.recuperarPorCodigoCliente(null));

        // -- 03_Verificação_Validação
        assertTrue(exception.getMessage().contains("Filtro do codigo do cliente para buscar o(s) pedido(s)"));
        log.info("{} ", "EXCEPTION: ".concat(exception.getMessage()));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveLancarExceptionAoRecuperarPedidosPorNumeroControlePedido() throws ServiceException {
        log.info("{} ", "#TEST: deveLancarExceptionAoRecuperarPedidosPorNumeroControlePedido: ");

        // -- 01_Cenário && 02_Ação
        doCallRealMethod().when(pedidoServiceMock).recuperarPorNumeroControle(isA(Long.class));
        ServiceException exception = assertThrows(ServiceException.class,
                () -> pedidoServiceMock.recuperarPorNumeroControle(null));

        // -- 03_Verificação_Validação
        assertTrue(exception.getMessage().contains("Filtro do número controle para buscar o pedido"));
        log.info("{} ", "EXCEPTION: ".concat(exception.getMessage()));
        log.info("{} ", "-------------------------------------------------------------");
    }
}
