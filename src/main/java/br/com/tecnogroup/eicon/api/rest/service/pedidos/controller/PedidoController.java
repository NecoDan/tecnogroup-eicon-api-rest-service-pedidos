package br.com.tecnogroup.eicon.api.rest.service.pedidos.controller;

import br.com.tecnogroup.eicon.api.rest.service.pedidos.controller.advice.PedidoNotFoundException;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.model.pedido.Pedido;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.service.pedidos.IGeraPedidoService;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.service.pedidos.IPedidoService;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.utils.exceptions.ResourceStatusNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Validated
@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@Api(value = "Pedido")
public class PedidoController {

    private final IPedidoService pedidoService;
    private final IGeraPedidoService geraPedidoService;
    private static final String MSG_VALIDACAO_NOT_FOUND = "Response server: Nenhum pedido(s) encontrado(s).";

    @ApiOperation(value = "Retorna todos os produtos existentes paginável.")
    @GetMapping(path = "/pedidos", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Page<Pedido>> getAllPedidos(@PageableDefault(page = 0, size = 10, sort = "dataCadastro", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Pedido> produtoPage = this.pedidoService.recuperarTodos(pageable);
        if (produtoPage.isEmpty())
            throw new ResourceStatusNotFoundException(MSG_VALIDACAO_NOT_FOUND);
        return new ResponseEntity<>(produtoPage, HttpStatus.OK);
    }

    @ApiOperation(value = "Retorna um único pedido existente, caso exista, a partir de seu número controle {valor inteiro} existente e registrado.")
    @GetMapping("/pedidos/{numeroControle}")
    public ResponseEntity<Pedido> getOnePedido(@PathVariable(value = "numeroControle") @Valid Long numeroControle) {
        Optional<Pedido> optionalPedido = Optional.of(pedidoService.recuperarPorNumeroControle(numeroControle));
        return new ResponseEntity<>(optionalPedido.orElseThrow(() -> new PedidoNotFoundException(numeroControle)), HttpStatus.OK);
    }

    @ApiOperation(value = "Responsável por persistir um Pedido, a partir de um consumer {Pedido} passado como parâmetro no corpo da requisição...")
    @PostMapping("/pedidos/save")
    public ResponseEntity<Pedido> savePedido(@RequestBody @Valid Pedido pedido) {
        return new ResponseEntity<>(geraPedidoService.gerarPedido(pedido), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Responsável por persistir uma lista de Pedido(s), a partir de um consumer {Pedido} passado como parâmetro no corpo da requisição...")
    @PostMapping("/pedidos")
    public ResponseEntity<List<Pedido>> saveAllPedidos(@RequestBody @Valid List<Pedido> pedidos) {
        return new ResponseEntity<>(geraPedidoService.gerarPedidos(pedidos), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Responsável por atualizar um Pedido, a partir de um {@PathVariable} contendo o {Número Controle} do registro do Pedido e um consumer {Pedido} como corpo da requisição.")
    @PutMapping("/pedidos/{numeroControle}")
    public ResponseEntity<Pedido> updatePedido(@PathVariable(value = "numeroControle") Long numeroControle,
                                               @RequestBody @Valid Pedido pedido) {
        Optional<Pedido> pedidoOptional = Optional.of(geraPedidoService.atualizarPedido(numeroControle, pedido));
        return pedidoOptional
                .map(p -> new ResponseEntity<>(p, HttpStatus.ACCEPTED))
                .orElseGet(() -> new ResponseEntity(MSG_VALIDACAO_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    @ApiOperation(value = "Responsável por excluir um Pedido, a partir de um @PathVariable contendo o id {valor inteiro} do registro.")
    @DeleteMapping("/pedidos/{numeroControle}")
    public ResponseEntity<?> deletePedido(@PathVariable(value = "numeroControle") Long numeroControle) {
        return (pedidoService.excluirPor(numeroControle)) ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(MSG_VALIDACAO_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Retorna todos os pedidos, a partir do filtro {Código Cliente} referente ao codigo do cliente aos quais estão associado(s) e pedido(s) gerado(s).")
    @GetMapping("pedidos/buscarPorCodigoCliente")
    public ResponseEntity<List<Pedido>> listAllFromCodigoCliente(@RequestParam("codigo") @Valid Long codigo) {
        return getResponseDefaultFromList(pedidoService.recuperarPorCodigoCliente(codigo));
    }

    @ApiOperation(value = "Retorna todos os pedidos, a partir dos filtro(s) {Data Inicial} e {Data Final}, que representam o intervalo de datas " +
            " aos quais foram inseridas. O filtro da datas passado como parametro devem ser no formato {dd/MM/yyyy}.")
    @GetMapping("/pedidos/buscarPorPeriodo")
    public ResponseEntity<List<Pedido>> listAllFromPeriodo(@RequestParam("dataInicio") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dataInicio,
                                                           @RequestParam("dataFim") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dataFim) {
        return getResponseDefaultFromList(pedidoService.recuperarPorPeriodo(dataInicio, dataFim));
    }

    private ResponseEntity<List<Pedido>> getResponseDefaultFromList(List<Pedido> pedidoList) {
        if (Objects.isNull(pedidoList) || pedidoList.isEmpty())
            throw new ResourceStatusNotFoundException(MSG_VALIDACAO_NOT_FOUND);
        return new ResponseEntity<>(pedidoList, HttpStatus.OK);
    }
}
