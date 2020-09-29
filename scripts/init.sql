create schema if not exists controle_pedidos;
set schema 'controle_pedidos';

--/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

create table if not exists controle_pedidos.pd02_cliente
(
    id          uuid                    not null,
    codigo      bigint                  not null,
    dt_cadastro timestamp default now() not null,
    ativo       bool      default null,
    primary key (id)
);

create unique index uq_pd02_cliente_codigo on controle_pedidos.pd02_cliente (codigo);

alter table controle_pedidos.pd02_cliente
    owner to postgres;

--/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
create table if not exists controle_pedidos.pd01_pedido
(
    id                  uuid                         not null,
    numero_controle     bigint                       not null,
    codigo_cliente      bigint                       not null,
    nome_produto        varchar(300)                 null,
    valor               decimal(19, 6) default 0     not null,
    qtde                decimal(24, 5) default 1     not null,
    percentual_desconto decimal(19, 6) default 0     not null,
    valor_desconto      decimal(19, 6) default 0     not null,
    valor_total         decimal(19, 6) default 0     not null,
    dt_cadastro         timestamp      default now() not null,
    primary key (id)
);

create unique index uq_pd01_pedido_numero_controle on controle_pedidos.pd01_pedido (numero_controle);

alter table controle_pedidos.pd01_pedido
    owner to postgres;

--/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

alter table controle_pedidos.pd01_pedido
    add constraint cliente_cod_cliente_fkey foreign key (codigo_cliente) references controle_pedidos.pd02_cliente (codigo);


