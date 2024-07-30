DROP TABLE IF EXISTS url_checks ;
DROP TABLE IF EXISTS urls;

CREATE TABLE urls (
    id SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP
);

CREATE TABLE url_checks  (
    id SERIAL PRIMARY KEY NOT NULL,
    status_code bigint NOT NULL,
    h1 VARCHAR(255),
    title VARCHAR(255),
    description text,
    url_id bigint REFERENCES urls(id),
    created_at TIMESTAMP
);
