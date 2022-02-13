CREATE TABLE if not exists cities (
    id SERIAL PRIMARY KEY,
    city TEXT
);

CREATE TABLE if not exists candidate (
    id serial PRIMARY KEY,
    name VARCHAR(50),
    photo_id INT,
    city_id integer references cities(id),
    created TIMESTAMP default now()
);

insert into cities(city) values ('Minsk')