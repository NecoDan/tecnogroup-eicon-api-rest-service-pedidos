package br.com.tecnogroup.eicon.api.rest.service.pedidos.controller;

import br.com.tecnogroup.eicon.api.rest.service.pedidos.model.pedido.Pedido;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.service.negocio.Desconto;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.service.pedidos.GeraPedidoService;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.service.pedidos.PedidoService;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.service.strategy.*;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.utils.RandomicoUtil;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.utils.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@RunWith(SpringRunner.class)
@WebMvcTest(PedidoController.class)
public class PedidoControllerWebMvcTest {

    private static final String BASE_URL = "/pedidos";
    private static final String URI = BASE_URL + "/{action}";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService pedidoService;

    @MockBean
    private GeraPedidoService geraPedido;

    private BuilderDescontoService builderDescontoService;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        JavaTimeModule module = new JavaTimeModule();
        LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:dd"));
        module.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);
        this.objectMapper = Jackson2ObjectMapperBuilder.json()
                .modules(module)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();

        List<IFactoryDescontoService> factoryDescontoService = Arrays.asList(new FactoryDescontoCincoPorcentoService(), new FactoryDescontoDezPorcentoService());
        BuilderDescontoStrategyService builderDescontoStrategyService = new BuilderDescontoStrategyService(factoryDescontoService);
        this.builderDescontoService = new BuilderDescontoService(builderDescontoStrategyService);
    }

    @Test
    public void deveRetornarStatus201EProducerJSONAoCriarPedidoMethodPOST() throws Exception {
//        log.info("\n#TEST: deveRetornarStatus201EProducerJSONAoCriarPedidoMethodPOST: ");
//
//        // -- 01_Cenário
//        Pedido pedido = constroiPedidoValido("Balde Vermelho 4L");
//
//        // -- 02_Ação
//        given(geraPedido.gerarPedido(pedido)).willReturn(pedido);
//
//        ResultActions responseResultActions = this.mockMvc.perform(post(BASE_URL+"/save")
//                .accept(MediaType.APPLICATION_JSON)
//                .content(getJsonValuePedidoFromPedidoObj(pedido))
//                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
//        );
//
//        // -- 03_Verificação_Validação
//        responseResultActions
//                .andExpect(status().isCreated())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.id").exists())
//                .andExpect(jsonPath("$.valor").isNumber())
//                .andExpect(jsonPath("$.valorTotal").isNumber())
//                .andExpect(jsonPath("$.percentualDesconto").isNumber())
//                .andExpect(jsonPath("$.valorDesconto").isNumber())
//                .andExpect(jsonPath("$.numeroControle").value(pedido.getNumeroControle()))
//                .andExpect(jsonPath("$.codigoCliente").value(pedido.getCodigoCliente()))
//                .andExpect(jsonPath("$.nomeProduto").value(pedido.getNomeProduto()));
//
//        verify(geraPedido).gerarPedido(any(Pedido.class));
//
//        String statusResponse = String.valueOf(responseResultActions.andReturn().getResponse().getStatus());
//        log.info("#TEST_RESULT_STATUS: ".concat((statusResponse.isEmpty()) ? " " : HttpStatus.valueOf(Integer.parseInt(statusResponse)).toString()));
//        toStringEnd(responseResultActions);
    }

    @Test
    public void deveRetornarProducerJSONContendoUmaListaPedidosPorFiltroCodigoClienteMethodGET() throws Exception {
        log.info("\n#TEST: deveRetornarProducerJSONContendoUmaListaPedidosPorFiltroCodigoClienteMethodGET: ");

        // -- 01_Cenário
        Long codigoCliente = RandomicoUtil.gerarValorRandomicoLong();
        List<Pedido> pedidoList = Arrays.asList(constroiPedidoValido("Toalhas de papel"), constroiPedidoValido("Lapis de cor"),
                constroiPedidoValido("Copo de vidro 300ML"), constroiPedidoValido("Livro O Pequeno Principe"));

        // -- 02_Ação
        given(pedidoService.recuperarPorCodigoCliente(codigoCliente)).willReturn(pedidoList);
        String uri = BASE_URL.concat("/buscarPorCodigoCliente?codigo=" + codigoCliente);
        ResultActions response = getResponseEntityEndPointsMethodGET(uri, MediaType.APPLICATION_JSON);

        // -- 03_Verificação_Validação
        response.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").exists())
                .andExpect(jsonPath("$.[*].dataCadastro").isNotEmpty())
                .andExpect(jsonPath("$.[*].valor").isNotEmpty())
                .andExpect(jsonPath("$.[*].valorTotal").isNotEmpty())
                .andExpect(jsonPath("$.[*].numeroControle").isNotEmpty())
                .andExpect(jsonPath("$.[*].codigoCliente").isNotEmpty());
        assertNotNull(response.andReturn().getResponse().getContentAsString());

        toStringEnd(response);
    }

    @Test
    public void deveRetornarProducerJSONContendoUmaListaPedidosPorIntervaloDatasMethodGET() throws Exception {
        log.info("\n#TEST: deveRetornarProducerJSONContendoUmaListaPedidosPorIntervaloDatasMethodGET: ");

        // -- 01_Cenário
        LocalDate dataInicio = LocalDate.now();
        LocalDate dataFim = LocalDate.now();

        List<Pedido> pedidoList = Arrays.asList(constroiPedidoValido("Toalhas de papel"), constroiPedidoValido("Lapis de cor"),
                constroiPedidoValido("Copo de vidro 300ML"), constroiPedidoValido("Livro O Pequeno Principe"));

        // -- 02_Ação
        given(pedidoService.recuperarPorPeriodo(dataInicio, dataFim)).willReturn(pedidoList);
        String uri = BASE_URL.concat("/buscarPorPeriodo?dataInicio=" + StringUtil.formatLocalDate(dataInicio) + "&" + "dataFim=" + StringUtil.formatLocalDate(dataInicio));
        ResultActions responseResultActions = getResponseEntityEndPointsMethodGET(uri, MediaType.APPLICATION_JSON);

        // -- 03_Verificação_Validação
        responseResultActions.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").exists())
                .andExpect(jsonPath("$.[*].dataCadastro").isNotEmpty())
                .andExpect(jsonPath("$.[*].valor").isNotEmpty())
                .andExpect(jsonPath("$.[*].percentualDesconto").isNotEmpty())
                .andExpect(jsonPath("$.[*].valorDesconto").isNotEmpty())
                .andExpect(jsonPath("$.[*].numeroControle").isNotEmpty())
                .andExpect(jsonPath("$.[*].codigoCliente").isNotEmpty())
                .andExpect(jsonPath("$.[*].valorTotal").isNotEmpty());
        assertNotNull(responseResultActions.andReturn().getResponse().getContentAsString());

        toStringEnd(responseResultActions);
    }

    private Pedido constroiPedidoValido(String nomeProduto) {
        Pedido pedido = Pedido.builder()
                .codigoCliente(RandomicoUtil.gerarValorRandomicoLong())
                .numeroControle(RandomicoUtil.gerarValorRandomicoLong())
                .nomeProduto(nomeProduto)
                .valor(RandomicoUtil.gerarValorRandomicoDecimalAte(5000))
                .quantidade(RandomicoUtil.gerarValorRandomicoDecimalAte(500))
                .build()
                .gerarId()
                .gerarData();

        Desconto desconto = this.builderDescontoService.obterDescontoAPartir(pedido.getQuantidade());
        pedido.calcularValorTotal(desconto);
        return pedido;
    }

    private ResultActions getResponseEntityEndPointsMethodGET(String url, MediaType mediaType) throws Exception {
        return this.mockMvc.perform(get(url).accept(mediaType));
    }

    private String getJsonValuePedidoFromPedidoObj(Pedido pedido) throws JsonProcessingException {
        return this.objectMapper.writeValueAsString(pedido);
    }

    private void toStringEnd(ResultActions response) throws Exception {
        if (Objects.isNull(response)) {
            log.info("#TEST_RESULT: ".concat("Error ao gerar saida. Não existem dados..."));
            log.info("-------------------------------------------------------------");
            return;
        }

        String result = response.andReturn().getResponse().getContentAsString();
        String out = StringUtil.formatConteudoJSONFrom(result);

        log.info("#TEST_RESULT: ".concat(out));
        log.info("-------------------------------------------------------------");
    }
}
