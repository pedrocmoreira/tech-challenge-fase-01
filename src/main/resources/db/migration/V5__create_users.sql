CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(80) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(150) NOT NULL,
    user_type VARCHAR(20) NOT NULL CHECK ( user_type IN ('ADMIN', 'ATENDENTE', 'MECANICO')),
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX idx_users_username ON users(username);