CREATE TABLE IF NOT EXISTS clients (
    client_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    login TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    phone_number TEXT UNIQUE,
    email_address TEXT UNIQUE,
    birth_date DATE NOT NULL,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    patronymic TEXT
);

CREATE TABLE IF NOT EXISTS bank_accounts (
    account_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    client BIGINT UNIQUE NOT NULL,
    deposit NUMERIC(25, 2) DEFAULT 0,
    FOREIGN KEY (client) REFERENCES clients(client_id)
);