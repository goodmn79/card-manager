--liquibase formatted sql

--changeset goodmn:1

CREATE TABLE IF NOT EXISTS users
(
    id         BIGSERIAL PRIMARY KEY,
    username   VARCHAR(256) UNIQUE NOT NULL,
    password   TEXT                NOT NULL,
    first_name VARCHAR(128)        NOT NULL,
    midl_name  VARCHAR(128),
    last_name  VARCHAR(128)        NOT NULL,
    role       VARCHAR(32) DEFAULT 'ROLE_USER',
    enabled BOOLEAN DEFAULT true
);

--changeset goodmn:2

CREATE TABLE IF NOT EXISTS cards
(
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT,
    card_number VARCHAR(255) UNIQUE NOT NULL,
    expiry_date DATE,
    status      VARCHAR(32) DEFAULT 'BLOCKED',
    balance     DECIMAL     DEFAULT 0.00
);

--changeset goodmn:3

CREATE TABLE IF NOT EXISTS transactions
(
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT       NOT NULL,
    card_id     BIGINT       NOT NULL,
    type        VARCHAR(32)  NOT NULL,
    timestamp  TIMESTAMP    NOT NULL,
    amount      DECIMAL      NOT NULL,
    description VARCHAR(256) NOT NULL,
    FOREIGN KEY (card_id) REFERENCES cards (id)
);

--changeset goodmn:4

CREATE TABLE IF NOT EXISTS card_limits
(
    id         BIGSERIAL PRIMARY KEY,
    card_id    BIGINT      NOT NULL,
    limit_type VARCHAR(32) NOT NULL,
    reset_date DATE        NOT NULL,
    amount     DECIMAL     NOT NULL,
    FOREIGN KEY (card_id) REFERENCES cards (id)
);

--changeset goodmn:5

CREATE TABLE IF NOT EXISTS card_requests
(
    id           BIGSERIAL PRIMARY KEY,
    card_id      BIGINT    NOT NULL,
    user_id      BIGINT    NOT NULL,
    request_date TIMESTAMP NOT NULL,
    process_date TIMESTAMP,
    status       VARCHAR(32) DEFAULT 'PENDING',
    FOREIGN KEY (card_id) REFERENCES cards (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);
