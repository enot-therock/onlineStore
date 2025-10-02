-- liquibase formatted sql

-- changeSet enot:09.09.2025_01

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(32) NOT NULL UNIQUE,
    password VARCHAR(16) NOT NULL,
    firstName VARCHAR(16),
    lastName VARCHAR(16),
    role TEXT NOT NULL,
    image TEXT,
    phone VARCHAR() NOT NULL,

    CONSTRAINT check_username_length CHECK (LENGTH(username) BETWEEN 4 AND 32),
    CONSTRAINT check_password_length CHECK (LENGTH(password) BETWEEN 8 AND 16),
    CONSTRAINT check_firstName_length CHECK (LENGTH(firstName) BETWEEN 2 AND 16),
    CONSTRAINT check_lastName_length CHECK (LENGTH(lastName) BETWEEN 2 AND 16),
    CONSTRAINT check_phone_pattern CHECK (phone REGEXP '\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}')
);

CREATE TABLE advertisement (
    id SERIAL PRIMARY KEY,
    description VARCHAR(64),
    image TEXT,
    title VARCHAR(32) NOT NULL,
    price INT NOT NULL,
    user_id BEGIN,
    FOREIGN KEY (user_id) REFERENCES (id),

    CONSTRAINT check_description_length CHECK (LENGTH(description) BETWEEN 8 AND 64),
    CONSTRAINT check_title_length CHECK (LENGTH(title) BETWEEN 4 AND 32),
    CONSTRAINT check_price_length CHECK (price BETWEEN 0 AND 10000000)
);

CREATE TABLE comment (
    id SERIAL PRIMARY KEY,
    createdAt TEXT,
    text TEXT,
    user_id BEGIN,
    FOREIGN KEY (user_id) REFERENCES (id),
    advertisement_id BEGIN,
    FOREIGN KEY (advertisement_id) REFERENCES (id)
);