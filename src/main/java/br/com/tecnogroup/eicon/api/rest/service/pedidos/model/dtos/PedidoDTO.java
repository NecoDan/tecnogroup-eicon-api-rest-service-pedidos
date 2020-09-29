package br.com.tecnogroup.eicon.api.rest.service.pedidos.model.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
public class PedidoDTO {

    @NotNull(message = "Nenhum número de controle atrelado ao pedido informado. Inválido e/ou inexistente {NULL}.")
    @Positive(message = "O número de control atrelado ao pedido informado deve ser positivo, ou seja, maior que zero (0)")
    private Long numeroControle;

    @NotNull(message = "Nenhum codigo de cliente atrelado ao pedido informado. Inválido e/ou inexistente {NULL}.")
    @Positive(message = "O código do cliente atrelado ao pedido informado deve ser positivo, ou seja, maior que zero (0)")
    private Long codigoCliente;

    @Size(max = 300, message = "Qtde de caracteres da descrição/nome ultrapassa o valor permitido igual à 300.")
    @NotNull(message = "Insira uma descrição e/ou nome válida para o produto.")
    @NotBlank(message = "Insira uma descrição e/ou nome válido para o produto.")
    private String nomeProduto;

    @NotNull(message = "Nenhum valor referente ao pedido informado. Inválido e/ou inexistente {NULL}.")
    @Positive(message = "Valor referente ao pedido informado deve ser maior que zero (0).")
    @Digits(integer = 19, fraction = 6)
    private BigDecimal valor;

    private BigDecimal quantidade;
}
