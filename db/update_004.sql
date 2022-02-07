CREATE TABLE users (
    id serial,
    name VARCHAR(50),
    email VARCHAR(50) unique,
    password VARCHAR(50),
    PRIMARY KEY (id)
);