CREATE TABLE service_orders(
    id BIGSERIAL PRIMARY KEY,
    number VARCHAR(20) NOT NULL UNIQUE,
    status VARCHAR(30) NOT NULL UNIQUE DEFAULT 'RECEBIDA',
    total_value NUMERIC(12, 2) NOT NULL DEFAULT 0,
    observations TEXT,
    created_at TIMESTAMP,
    execution_start_date TIMESTAMP,
    completion_date TIMESTAMP,
    delivery_date TIMESTAMP,
    customer_id BIGINT NOT NULL REFERENCES customers(id),
    vehicle_id BIGINT NOT NULL REFERENCES vehicles(id)
);

CREATE TABLE service_items (
    id BIGSERIAL PRIMARY KEY,
    service_order_id BIGINT NOT NULL REFERENCES service_orders(id) ON DELETE CASCADE,
    service_id BIGINT NOT NULL REFERENCES services(id),
    applied_price NUMERIC(10, 2) NOT NULL
);

CREATE TABLE part_items (
    id BIGSERIAL PRIMARY KEY,
    service_order_id BIGINT NOT NULL REFERENCES service_orders(id) ON DELETE CASCADE,
    part_id BIGINT NOT NULL REFERENCES parts(id),
    quantity INTEGER NOT NULL,
    unit_price_applied NUMERIC(10, 2) NOT NULL
);

CREATE INDEX idx_so_status ON service_orders(status);
CREATE INDEX idx_so_customers ON service_orders(customer_id);
CREATE INDEX id_so_number ON service_orders(number);