package br.com.tecnogroup.eicon.api.rest.service.pedidos.model.pedido;


import br.com.tecnogroup.eicon.api.rest.service.pedidos.model.cadastro.Cliente;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.service.negocio.Desconto;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.utils.DecimalUtil;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.utils.domain.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.experimental.Tolerate;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Period;
import java.util.Objects;

@ToString
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "pd01_pedido", schema = "controle_pedidos")
@Inheritance(strategy = InheritanceType.JOINED)
public class Pedido extends AbstractEntity {

    @Tolerate
    public Pedido() {
        super();
    }

    @NotNull(message = "Nenhum número de controle atrelado ao pedido informado. Inválido e/ou inexistente.")
    @Positive(message = "O número de control atrelado ao pedido informado deve ser positivo, ou seja, maior que zero (0)")
    @Column(name = "numero_controle")
    private Long numeroControle;

    @NotNull(message = "Nenhum codigo de cliente atrelado ao pedido informado. Inválido e/ou inexistente.")
    @Positive(message = "O código do cliente atrelado ao pedido informado deve ser positivo, ou seja, maior que zero (0)")
    @Column(name = "codigo_cliente")
    private Long codigoCliente;

    @Size(max = 300, message = "Qtde de caracteres da descrição/nome ultrapassa o valor permitido igual à 300")
    @NotBlank(message = "Insira uma descricao e/ou nome válido para o produto")
    @NotNull(message = "Insira uma  descricao e/ou nome válida para o produto")
    @Column(name = "nome_produto")
    private String nomeProduto;

    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 19, fraction = 6)
    @Column(name = "valor")
    private BigDecimal valor;

    @Min(1)
    @PositiveOrZero
    @DecimalMin(value = "1.0", inclusive = true)
    @Digits(integer = 19, fraction = 6)
    @Column(name = "qtde")
    private BigDecimal quantidade = BigDecimal.ONE;

    @Setter(AccessLevel.NONE)
    @DecimalMin(value = "0.0", inclusive = true)
    @Digits(integer = 19, fraction = 6)
    @Column(name = "percentual_desconto")
    private BigDecimal percentualDesconto;

    @Setter(AccessLevel.NONE)
    @DecimalMin(value = "0.0", inclusive = true)
    @Digits(integer = 19, fraction = 6)
    @Column(name = "valor_desconto")
    private BigDecimal valorDesconto;

    @Setter(AccessLevel.NONE)
    @DecimalMin(value = "0.0", inclusive = true)
    @Digits(integer = 19, fraction = 6)
    @Column(name = "valor_total")
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @JsonIgnore
    @Transient
    private Cliente cliente;

    @JsonIgnore
    public Pedido gerarId() {
        this.geraId();
        return this;
    }

    @JsonIgnore
    public Pedido gerarData() {
        this.gerarDataCorrente();
        return this;
    }

    private void inicializarValor() {
        if (Objects.isNull(this.valor) || DecimalUtil.isEqualsToZero(this.valor))
            this.valor = BigDecimal.ZERO;
    }

    public void calcularValorTotal(Desconto desconto) {
        inicializarValor();
        this.valorTotal = this.valor.multiply(this.quantidade);
        this.valorTotal = (Objects.isNull(desconto)) ? BigDecimal.ZERO : efetuarCalculoValorTotalAPartir(desconto);
        this.valorTotal = this.valorTotal.setScale(2, RoundingMode.HALF_UP);
    }

    @JsonIgnore
    private BigDecimal efetuarCalculoValorTotalAPartir(Desconto desconto) {
        this.percentualDesconto = desconto.getRegraCalculoDesconto().getValorPercentualDesconto(this);
        this.valorDesconto = desconto.getRegraCalculoDesconto().calculaDesconto(this);
        this.valorTotal = this.valorTotal.subtract(this.valorDesconto);
        return this.valorTotal;
    }

    @JsonIgnore
    public boolean isIdValido() {
        return (Objects.nonNull(getId()));
    }

    @JsonIgnore
    public boolean isNumeroControleValido() {
        return (Objects.nonNull(this.numeroControle) && this.numeroControle > 0);
    }
}
