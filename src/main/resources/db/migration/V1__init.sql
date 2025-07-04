-- ===============================================
-- 	Flyway - Script Inicial do Banco - PostgreSQL
-- ===============================================

-- CLIENTE
CREATE TABLE cliente (
    id UUID PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    tem_desconto BOOLEAN NOT NULL
);

-- VENDEDOR
CREATE TABLE vendedor (
    id UUID PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    matricula VARCHAR(20) NOT NULL UNIQUE
);

-- PATO
CREATE TABLE pato (
    id UUID PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    nome_mae VARCHAR(100),
    pata_mae_id UUID,
    vendido BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_pata_mae FOREIGN KEY (pata_mae_id) REFERENCES pato(id)
);


-- VENDA
CREATE TABLE venda (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    vendedor_id UUID NOT NULL,
    data_venda TIMESTAMP NOT NULL,
    valor_total NUMERIC(10, 2) NOT NULL,

    CONSTRAINT fk_venda_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id),
    CONSTRAINT fk_venda_vendedor FOREIGN KEY (vendedor_id) REFERENCES vendedor(id)
);

-- VENDA_PATOS (tabela associativa)
CREATE TABLE venda_patos (
    venda_id UUID NOT NULL,
    pato_id UUID NOT NULL,

    PRIMARY KEY (venda_id, pato_id),

    CONSTRAINT fk_venda_pato_venda FOREIGN KEY (venda_id) REFERENCES venda(id),
    CONSTRAINT fk_venda_pato_pato FOREIGN KEY (pato_id) REFERENCES pato(id)
);
