package br.com.tecnogroup.eicon.api.rest.service.pedidos.service.cadastro;

import br.com.tecnogroup.eicon.api.rest.service.pedidos.model.cadastro.Cliente;

public interface IClienteService {

    void salvarCliente(Cliente cliente);

    Cliente recuperarPorCodigo(Long codigo);
}
