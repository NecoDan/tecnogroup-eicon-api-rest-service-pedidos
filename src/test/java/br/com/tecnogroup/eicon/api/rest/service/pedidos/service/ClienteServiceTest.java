package br.com.tecnogroup.eicon.api.rest.service.pedidos.service;

import br.com.tecnogroup.eicon.api.rest.service.pedidos.model.cadastro.Cliente;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.repository.ClienteRepository;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.service.cadastro.ClienteService;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.utils.RandomicoUtil;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.utils.exceptions.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;
    @Spy
    @InjectMocks
    private ClienteService clienteService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private void resetarMocks() {
        reset(clienteRepository);
        reset(clienteService);
    }

    @Test
    public void deveRecuperarPorIdUmUnicoPedido() {
        log.info("{} ", "#TEST: deveRecuperarPorIdUmUnicoPedido:");

        // -- 01_Cenário
        resetarMocks();
        Long codigoCliente = RandomicoUtil.gerarValorRandomicoLong();
        Cliente cliente = mock(Cliente.class);

        // -- 01_Cenário && 02_Ação
        doCallRealMethod().when(clienteService).recuperarPorCodigo(any(Long.class));
        when(clienteService.recuperarPorCodigo(codigoCliente)).thenReturn(cliente);

        // -- 03_Verificação_Validação
        assertEquals(cliente, clienteService.recuperarPorCodigo(codigoCliente));
        log.info("{} ", "-------------------------------------------------------------");
    }

    @Test
    public void deveSalvarCliente() throws ServiceException {
        log.info("{} ", "#TEST: deveSalvarCliente: ");

        // -- 01_Cenário
        resetarMocks();
        Cliente clienteParam = Cliente
                .builder()
                .codigo(RandomicoUtil.gerarValorRandomicoLong())
                .build()
                .gerarId()
                .geraAtivado()
                .gerarData();

        // -- 02_Ação
        doCallRealMethod().when(clienteService).salvarCliente(isA(Cliente.class));
        clienteService.salvarCliente(clienteParam);

        // -- 03_Verificação_Validação
        verify(clienteService).salvarCliente(any(Cliente.class));
        log.info("{} ", "-------------------------------------------------------------");
    }

}
