package br.com.tecnogroup.eicon.api.rest.service.pedidos.service.cadastro;

import br.com.tecnogroup.eicon.api.rest.service.pedidos.model.cadastro.Cliente;
import br.com.tecnogroup.eicon.api.rest.service.pedidos.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClienteService implements IClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    public void salvarCliente(Cliente cliente) {
        this.clienteRepository.save(cliente);
    }

    @Override
    public Cliente recuperarPorCodigo(Long codigo) {
        return this.clienteRepository.findByCodigo(codigo);
    }
}
