DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id           INTEGER PRIMARY KEY AUTO_INCREMENT,
    email        VARCHAR(255) NOT NULL,
    first_name   VARCHAR(255) NOT NULL,
    last_name    VARCHAR(255) NOT NULL,
    birthdate    DATE         NOT NULL,
    address      VARCHAR(255),
    phone_number VARCHAR(255)
);
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);