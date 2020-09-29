package br.com.tecnogroup.eicon.api.rest.service.pedidos.model.cadastro;

import br.com.tecnogroup.eicon.api.rest.service.pedidos.utils.domain.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.experimental.Tolerate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@ToString
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "pd02_cliente", schema = "controle_pedidos")
@Inheritance(strategy = InheritanceType.JOINED)
public class Cliente extends AbstractEntity {

    @Tolerate
    public Cliente() {
        super();
        this.codigo = 0L;
    }

    @NotNull(message = "Nenhum codigo de cliente informado. Inválido e/ou inexistente {NULL}.")
    @Column(name = "codigo")
    private Long codigo;

    @Column(name = "ativo")
    private boolean ativo;

    public void ativado() {
        this.ativo = true;
    }

    public void desativado() {
        this.ativo = false;
    }

    @JsonIgnore
    public Cliente geraAtivado() {
        ativado();
        return this;
    }

    @JsonIgnore
    public Cliente geraDesativado() {
        desativado();
        return this;
    }

    @JsonIgnore
    public Cliente gerarId() {
        this.geraId();
        return this;
    }

    public Cliente gerarData() {
        this.gerarDataCorrente();
        return this;
    }
}
