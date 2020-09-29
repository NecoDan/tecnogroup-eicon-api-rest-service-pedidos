package br.com.tecnogroup.eicon.api.rest.service.pedidos.repository;

import br.com.tecnogroup.eicon.api.rest.service.pedidos.model.cadastro.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    Cliente findByCodigo(@Param("codigo") Long codigo);
}
