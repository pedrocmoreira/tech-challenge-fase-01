CREATE TABLE customers (
    id            BIGSERIAL PRIMARY KEY,
    document      VARCHAR(20)  NOT NULL UNIQUE,
    document_type VARCHAR(10)  NOT NULL CHECK (document_type IN ('CPF', 'CNPJ')),
    name          VARCHAR(150) NOT NULL,
    phone         VARCHAR(20),
    email         VARCHAR(150)
);

CREATE INDEX idx_customers_document ON customers(document);