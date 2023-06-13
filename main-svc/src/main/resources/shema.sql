CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name  VARCHAR(250)        NOT NULL,
    email VARCHAR(254) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    initiator          BIGINT    NOT NULL,
    annotation         VARCHAR   NOT NULL,
    category           BIGINT    NOT NULL,
    created_on         TIMESTAMP NOT NULL,
    description        VARCHAR   NOT NULL,
    event_date         TIMESTAMP NOT NULL,
    location_lat       FLOAT     NOT NULL,
    location_lon       FLOAT     NOT NULL,
    paid               BOOLEAN   NOT NULL,
    participant_limit  INTEGER   NOT NULL,
    published_on       TIMESTAMP,
    request_moderation BOOLEAN   NOT NULL,
    state              VARCHAR   NOT NULL,
    title              VARCHAR   NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (initiator) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_category FOREIGN KEY (category) REFERENCES categories (id)
);