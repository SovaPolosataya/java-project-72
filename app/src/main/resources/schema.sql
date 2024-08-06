DROP TABLE IF EXISTS url_checks ;
DROP TABLE IF EXISTS urls;

CREATE TABLE urls (
    id SERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT pk_url PRIMARY KEY (id)
);

CREATE TABLE url_checks  (
    id SERIAL PRIMARY KEY NOT NULL,
    status_code bigint NOT NULL,
    h1 VARCHAR(255),
    title VARCHAR(255),
    description text,
    url_id bigint REFERENCES urls(id) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT pk_url_checks PRIMARY KEY (id)
);

CREATE INDEX ix_url_check_url_id ON url_checks (url_id);
ALTER TABLE url_checks ADD CONSTRAINT fk_url_checks_url_id FOREIGN KEY (url_id)
REFERENCES urls (id) ON DELETE RESTRICT ON UPDATE RESTRICT;
