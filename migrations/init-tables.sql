CREATE TABLE IF NOT EXISTS bank_accounts
(
    account_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    deposit    NUMERIC(25, 2) DEFAULT 0 NOT NULL,
    max_deposit NUMERIC(25, 2) DEFAULT 0 NOT NULL
);

CREATE TABLE IF NOT EXISTS clients
(
    client_id     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    login         TEXT          NOT NULL UNIQUE,
    password      TEXT          NOT NULL,
    account       BIGINT UNIQUE NOT NULL,
    phone_number  TEXT UNIQUE,
    email_address TEXT UNIQUE,
    birth_date    TIMESTAMP     NOT NULL,
    first_name    TEXT          NOT NULL,
    last_name     TEXT          NOT NULL,
    patronymic    TEXT,
    FOREIGN KEY (account) REFERENCES bank_accounts (account_id)
);