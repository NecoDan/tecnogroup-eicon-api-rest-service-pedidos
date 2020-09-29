package br.com.tecnogroup.eicon.api.rest.service.pedidos.controller;

import br.com.tecnogroup.eicon.api.rest.service.pedidos.controller.advice.AdviceGeneralController;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.model.pedido.Pedido;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.service.negocio.Desconto;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.service.pedidos.GeraPedidoService;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.service.pedidos.PedidoService;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.service.strategy.*;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.utils.FormatterUtil;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.utils.RandomicoUtil;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.utils.exceptions.ServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static java.time.format.DateTimeFormatter.ofPattern;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@Slf4j
public class PedidoControllerWebClientTest {

    private static final String BASE_URI = "/pedidos";

    private PedidoService pedidoService;
    private GeraPedidoService geraPedidoService;
    private WebTestClient webTestClient;
    private ObjectMapper objectMapper;
    private BuilderDescontoService builderDescontoService;

    @Before
    public void setUp() {
        this.pedidoService = Mockito.mock(PedidoService.class);
        this.geraPedidoService = Mockito.mock(GeraPedidoService.class);
        PedidoController pedidoController = new PedidoController(this.pedidoService, this.geraPedidoService);

        webTestClient = WebTestClient
                .bindToController(pedidoController)
                .controllerAdvice(AdviceGeneralController.class)
                .build();

        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(ofPattern("yyyy-MM-dd HH:mm:ss")));
        module.addSerializer(LocalDate.class, new LocalDateSerializer(ofPattern("yyyy-MM-dd")));
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer(ofPattern("yyyy-MM-dd")));
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(ofPattern("yyyy-MM-dd HH:mm:ss")));

        this.objectMapper = Jackson2ObjectMapperBuilder
                .json()
                .modules(module)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();

        List<IFactoryDescontoService> factoryDescontoService = Arrays.asList(new FactoryDescontoCincoPorcentoService(), new FactoryDescontoDezPorcentoService());
        BuilderDescontoStrategyService builderDescontoStrategyService = new BuilderDescontoStrategyService(factoryDescontoService);
        this.builderDescontoService = new BuilderDescontoService(builderDescontoStrategyService);
    }

    @Test
    public void naoDeveRetornarPageUmOuMaisPedidosResultVazioMethodGET() {
        log.info("{} ", "\n#TEST: naoDeveRetornarPageUmOuMaisPedidosResultVazioMethodGET: ");

        // -- 01_Cenário
        AtomicReference<String> resultJson = new AtomicReference<>("");
        List<Pedido> pedidoList = Arrays.asList(constroiPedidoValido("Toalhas de papel"), constroiPedidoValido("Lapis de cor"),
                constroiPedidoValido("Copo de vidro 300ML"), constroiPedidoValido("Livro O Pequeno Principe"));
        Pedido pedidoPosicao1Lista = pedidoList.get(0);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Pedido> pagePedido = new PageImpl<>(pedidoList, pageable, pedidoList.size());

        // -- 02_Ação
        String uri = BASE_URI.concat("/");
        when(pedidoService.recuperarTodos(pageable)).thenReturn(pagePedido);
        assertTrue(resultJson.get().isEmpty());

        log.info("{} ", "\n#TEST: naoDeveRetornarPageUmOuMaisPedidosResultVazioMethodGET: ");
        toStringEnd(resultJson, MediaType.APPLICATION_JSON);
    }

    @Test
    public void deveRetornarPeloMenosUnicoPedidoMethodGET() {
        log.info("{} ", "\n#TEST: deveRetornarPeloMenosUnicoPedidoMethodGET: ");

        // -- 01_Cenário
        AtomicReference<String> resultJson = new AtomicReference<>("");

        UUID id = UUID.randomUUID();
        Long numeroControle = RandomicoUtil.gerarValorRandomicoLong();

        Pedido pedido = constroiPedidoValido("Mouse atomico automatico");
        pedido.setNumeroControle(numeroControle);
        pedido.setId(id);

        // -- 02_Ação
        String uri = BASE_URI.concat("/").concat(String.valueOf(numeroControle));
        when(pedidoService.recuperarPorNumeroControle(numeroControle)).thenReturn(pedido);

        // -- 03_Verificação_Validação
        webTestClient
                .get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Pedido.class)
                .consumeWith(pedidoResourceResult -> {
                    try {
                        resultJson.set(this.objectMapper.writeValueAsString(Objects.requireNonNull(pedidoResourceResult.getResponseBody())));
                        assertThat(Objects.requireNonNull(pedidoResourceResult.getResponseBody()).getNumeroControle()).isEqualTo(numeroControle);
                    } catch (JsonProcessingException e) {
                        log.info(e.getLocalizedMessage());
                    }
                });

        log.info("{} ", "\n#TEST: deveRetornarPeloMenosUnicoPedidoMethodGET: ");
        toStringEnd(resultJson, MediaType.APPLICATION_JSON);
    }

    @Test
    public void deveRetornarPeloMenosUnicoPedidoAoSalvarMethodPOST() throws ServiceException, JsonProcessingException {
        log.info("{} ", "\n#TEST: deveRetornarPeloMenosUnicoPedidoAoSalvarMethodPOST: ");

        // -- 01_Cenário
        AtomicReference<String> resultJson = new AtomicReference<>("");
        String descricaoNomeProduto = "Livro O Pequeno Principe";
        List<Pedido> pedidoList = Collections.singletonList(constroiPedidoValido(descricaoNomeProduto));
        Pedido pedido = pedidoList.get(0);

        // -- 02_Ação
        when(pedidoService.salvar(pedido)).thenReturn(pedido);

        // -- 03_Verificação_Validação
        resultJson.set(this.objectMapper.writeValueAsString(pedido));

        this.webTestClient
                .post()
                .uri(BASE_URI + "/save")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(pedido), Pedido.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody()
                .equals(resultJson.get());

        log.info("{} ", "\n#TEST: deveRetornarPeloMenosUnicoPedidoAoSalvarMethodPOST: ");
        toStringEnd(resultJson, MediaType.APPLICATION_JSON);
    }

    private Pedido constroiPedidoValidoSimplesParam(String nomeProduto) {
        return Pedido.builder()
                .codigoCliente(RandomicoUtil.gerarValorRandomicoLong())
                .numeroControle(RandomicoUtil.gerarValorRandomicoLong())
                .nomeProduto(nomeProduto)
                .valor(RandomicoUtil.gerarValorRandomicoDecimalAte(5000))
                .quantidade(RandomicoUtil.gerarValorRandomicoDecimalAte(500))
                .build();
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

    private void toStringEnd(AtomicReference<String> resultJson, MediaType mediaType) {
        if (Objects.isNull(resultJson) || Objects.isNull(mediaType)) {
            log.info("{} ", "#TEST_RESULT: ".concat("Error ao gerar saida. Não existem dados..."));
            log.info("{} ", "-------------------------------------------------------------");
            return;
        }

        String result = resultJson.get();
        String out = "";

        if (mediaType == MediaType.APPLICATION_JSON)
            out = FormatterUtil.formatConteudoJSONFrom(result);

        log.info("{} ", "#TEST_RESULT: ".concat(out));
        log.info("{} ", "-------------------------------------------------------------");
    }

}
