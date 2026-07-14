-- Token de uso único para os links de aprovação/recusa de orçamento enviados por e-mail.
-- Substitui o uso do número sequencial (previsível) da OS como identificador do link.
ALTER TABLE service_orders ADD COLUMN budget_token VARCHAR(36);

CREATE UNIQUE INDEX idx_service_orders_budget_token
    ON service_orders (budget_token)
    WHERE budget_token IS NOT NULL;
