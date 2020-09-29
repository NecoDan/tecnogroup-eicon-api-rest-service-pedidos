package br.com.tecnogroup.eicon.api.rest.service.pedidos.repository;

import br.com.tecnogroup.eicon.api.rest.service.pedidos.model.pedido.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, UUID> {

    Pedido findByNumeroControle(@Param("numeroControle") Long numeroControle);

    List<Pedido> findByCodigoCliente(@Param("codigoCliente") Long codigoCliente);

    @Query(value = "select * from controle_pedidos.pd01_pedido where date(dt_cadastro) = :#{#data} order by dt_cadastro", nativeQuery = true)
    List<Pedido> recuperarTodosPorDataCadastro(@Param("data") LocalDate data);

    @Query(value = "select * from controle_pedidos.pd01_pedido where date(dt_cadastro) between :#{#dataInicio} and :#{#dataFim} order by dt_cadastro", nativeQuery = true)
    List<Pedido> recuperarTodosPorPeriodoDtCadastro(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

}
