CREATE TABLE post (
    id SERIAL PRIMARY KEY,
    name TEXT,
    description TEXT,
    created TIMESTAMP
);

CREATE TABLE candidate (
    id SERIAL PRIMARY KEY,
    name TEXT
);

ALTER TABLE candidate ADD COLUMN photo_id int references photo(id);

ALTER TABLE candidate ADD COLUMN city_id int references cities(id);

CREATE TABLE photo (
    id SERIAL PRIMARY KEY,
    title TEXT
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name TEXT,
    email TEXT,
    password TEXT
);

CREATE TABLE cities (
    id SERIAL PRIMARY KEY,
    city TEXT
);